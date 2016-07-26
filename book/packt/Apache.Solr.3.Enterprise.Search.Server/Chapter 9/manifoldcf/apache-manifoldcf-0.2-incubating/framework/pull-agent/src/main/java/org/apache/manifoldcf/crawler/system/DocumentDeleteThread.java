/* $Id: DocumentDeleteThread.java 988245 2010-08-23 18:39:35Z kwright $ */

/**
* Licensed to the Apache Software Foundation (ASF) under one or more
* contributor license agreements. See the NOTICE file distributed with
* this work for additional information regarding copyright ownership.
* The ASF licenses this file to You under the Apache License, Version 2.0
* (the "License"); you may not use this file except in compliance with
* the License. You may obtain a copy of the License at
*
* http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/
package org.apache.manifoldcf.crawler.system;

import org.apache.manifoldcf.core.interfaces.*;
import org.apache.manifoldcf.agents.interfaces.*;
import org.apache.manifoldcf.crawler.interfaces.*;
import org.apache.manifoldcf.crawler.system.Logging;
import java.util.*;
import java.lang.reflect.*;

/** This class represents a document delete thread.  This thread's job is to pull document sets to be deleted off of
* a queue, and kill them.  It finishes a delete set by getting rid of the corresponding rows in the job queue.
*
* There are very few decisions that this thread needs to make; essentially all the hard thought went into deciding
* what documents to queue in the first place.
*
* The only caveat is that the ingestion API may not be accepting delete requests at the time that this thread wants it
* to be able to accept them.  In that case, it's acceptable for the thread to block until the ingestion service is
* functioning again.
*
* Transactions are not much needed for this class; it simply needs to not fail to remove the appropriate jobqueue
* table rows at the end of the delete.
*/
public class DocumentDeleteThread extends Thread
{
  public static final String _rcsid = "@(#)$Id: DocumentDeleteThread.java 988245 2010-08-23 18:39:35Z kwright $";


  // Local data
  protected String id;
  // This is a reference to the static main document queue
  protected DocumentDeleteQueue documentDeleteQueue;
  /** Delete thread pool reset manager */
  protected DocDeleteResetManager resetManager;

  /** Constructor.
  *@param id is the worker thread id.
  */
  public DocumentDeleteThread(String id, DocumentDeleteQueue documentDeleteQueue, DocDeleteResetManager resetManager)
    throws ManifoldCFException
  {
    super();
    this.id = id;
    this.documentDeleteQueue = documentDeleteQueue;
    this.resetManager = resetManager;
    setName("Document delete thread '"+id+"'");
    setDaemon(true);
  }

  public void run()
  {
    resetManager.registerMe();

    try
    {
      // Create a thread context object.
      IThreadContext threadContext = ThreadContextFactory.make();
      IJobManager jobManager = JobManagerFactory.make(threadContext);
      IIncrementalIngester ingester = IncrementalIngesterFactory.make(threadContext);
      IRepositoryConnectionManager connMgr = RepositoryConnectionManagerFactory.make(threadContext);

      // Loop
      while (true)
      {
        // Do another try/catch around everything in the loop
        try
        {
          // Before we begin, conditionally reset
          resetManager.waitForReset(threadContext);

          // See if there is anything on the queue for me
          DocumentDeleteSet dds = documentDeleteQueue.getDocuments();
          if (dds == null)
            // Reset
            continue;

          if (Logging.threads.isDebugEnabled())
            Logging.threads.debug("Document delete thread received "+Integer.toString(dds.getCount())+" documents to delete for job "+dds.getJobDescription().getID().toString());
          
          IJobDescription job = dds.getJobDescription();
          String connectionName = job.getConnectionName();
          String outputConnectionName = job.getOutputConnectionName();

          try
          {
            // Do the delete work.

            // Delete these identifiers.  The underlying IIncrementalIngester method will need to be provided an activities object consistent
            // with the individual connection, so the first job is to segregate what came in into connection bins.  Then, we process each connection
            // bin appropriately.

            boolean[] deleteFromQueue = new boolean[dds.getCount()];
                
            String[] docClassesToRemove = new String[dds.getCount()];
            String[] hashedDocsToRemove = new String[dds.getCount()];
            DeleteQueuedDocument[] docsToDelete = new DeleteQueuedDocument[dds.getCount()];
            int j = 0;
            while (j < dds.getCount())
            {
              DeleteQueuedDocument dqd = dds.getDocument(j);
              DocumentDescription ddd = dqd.getDocumentDescription();
              docClassesToRemove[j] = connectionName;
              hashedDocsToRemove[j] = ddd.getDocumentIdentifierHash();
              docsToDelete[j] = dqd;
              deleteFromQueue[j] = false;
              j++;
            }
                
            OutputRemoveActivity logger = new OutputRemoveActivity(connectionName,connMgr,outputConnectionName);
                
            try
            {
              ingester.documentDeleteMultiple(outputConnectionName,docClassesToRemove,hashedDocsToRemove,logger);
              j = 0;
              while (j < dds.getCount())
              {
                deleteFromQueue[j] = true;
                j++;
              }
            }
            catch (ServiceInterruption e)
            {
              // We don't know which failed, or maybe they all did.
              // Go through the list of documents we just tried, and reset them on the queue based on the
              // ServiceInterruption parameters.  Then we must proceed to delete ONLY the documents that
              // were not part of the index deletion attempt.
              j = 0;
              while (j < dds.getCount())
              {
                DeleteQueuedDocument cqd = docsToDelete[j];
                DocumentDescription dd = cqd.getDocumentDescription();
                // To recover from an expiration failure, requeue the document to COMPLETED etc.
                jobManager.resetDeletingDocument(dd,e.getRetryTime());
                cqd.setProcessed();
                j++;
              }
            }

            // Count the records we're actually going to delete
            int recordCount = 0;
            j = 0;
            while (j < dds.getCount())
            {
              if (deleteFromQueue[j])
                recordCount++;
              j++;
            }
                
            // Delete the records
            DocumentDescription[] deleteDescriptions = new DocumentDescription[recordCount];
            j = 0;
            recordCount = 0;
            while (j < dds.getCount())
            {
              if (deleteFromQueue[j])
                deleteDescriptions[recordCount++] = docsToDelete[j].getDocumentDescription();
              j++;
            }
            jobManager.deleteIngestedDocumentIdentifiers(deleteDescriptions);
            // Mark them as gone
            j = 0;
            while (j < dds.getCount())
            {
              if (deleteFromQueue[j])
                docsToDelete[j].wasProcessed();
              j++;
            }
            // Go around again
          }
          finally
          {
            // Here we should take steps to insure that the documents that have been handed to us
            // are dealt with appropriately.  This may involve setting the document state to "complete"
            // so that they will be picked up again.
            int j = 0;
            while (j < dds.getCount())
            {
              DeleteQueuedDocument dqd = dds.getDocument(j++);

              if (dqd.wasProcessed() == false)
              {
                // Pop this document back into the jobqueue in an appropriate state
                DocumentDescription ddd = dqd.getDocumentDescription();
                // Requeue this document!
                jobManager.resetDeletingDocument(ddd,0L);
                dqd.setProcessed();

              }
            }
          }
        }
        catch (ManifoldCFException e)
        {
          if (e.getErrorCode() == ManifoldCFException.INTERRUPTED)
            break;

          if (e.getErrorCode() == ManifoldCFException.DATABASE_CONNECTION_ERROR)
          {
            resetManager.noteEvent();
            documentDeleteQueue.reset();

            Logging.threads.error("Document delete thread aborting and restarting due to database connection reset: "+e.getMessage(),e);
            try
            {
              // Give the database a chance to catch up/wake up
              ManifoldCF.sleep(10000L);
            }
            catch (InterruptedException se)
            {
              break;
            }
            continue;
          }

          // Log it, but keep the thread alive
          Logging.threads.error("Exception tossed: "+e.getMessage(),e);

          if (e.getErrorCode() == ManifoldCFException.SETUP_ERROR)
          {
            // Shut the whole system down!
            System.exit(1);
          }

        }
        catch (InterruptedException e)
        {
          // We're supposed to quit
          break;
        }
        catch (OutOfMemoryError e)
        {
          System.err.println("agents process ran out of memory - shutting down");
          e.printStackTrace(System.err);
          System.exit(-200);
        }
        catch (Throwable e)
        {
          // A more severe error - but stay alive
          Logging.threads.fatal("Error tossed: "+e.getMessage(),e);
        }
      }
    }
    catch (Throwable e)
    {
      // Severe error on initialization
      System.err.println("agents process could not start - shutting down");
      Logging.threads.fatal("DocumentDeleteThread initialization error tossed: "+e.getMessage(),e);
      System.exit(-300);
    }
  }

  /** The OutputRemoveActivity class */
  protected static class OutputRemoveActivity implements IOutputRemoveActivity
  {
    // Connection name
    protected String connectionName;
    // Connection manager
    protected IRepositoryConnectionManager connMgr;
    // Output connection name
    protected String outputConnectionName;

    /** Constructor */
    public OutputRemoveActivity(String connectionName, IRepositoryConnectionManager connMgr, String outputConnectionName)
    {
      this.connectionName = connectionName;
      this.connMgr = connMgr;
      this.outputConnectionName = outputConnectionName;
    }

    /** Record time-stamped information about the activity of the output connector.
    *@param startTime is either null or the time since the start of epoch in milliseconds (Jan 1, 1970).  Every
    *       activity has an associated time; the startTime field records when the activity began.  A null value
    *       indicates that the start time and the finishing time are the same.
    *@param activityType is a string which is fully interpretable only in the context of the connector involved, which is
    *       used to categorize what kind of activity is being recorded.  For example, a web connector might record a
    *       "fetch document" activity.  Cannot be null.
    *@param dataSize is the number of bytes of data involved in the activity, or null if not applicable.
    *@param entityURI is a (possibly long) string which identifies the object involved in the history record.
    *       The interpretation of this field will differ from connector to connector.  May be null.
    *@param resultCode contains a terse description of the result of the activity.  The description is limited in
    *       size to 255 characters, and can be interpreted only in the context of the current connector.  May be null.
    *@param resultDescription is a (possibly long) human-readable string which adds detail, if required, to the result
    *       described in the resultCode field.  This field is not meant to be queried on.  May be null.
    */
    public void recordActivity(Long startTime, String activityType, Long dataSize,
      String entityURI, String resultCode, String resultDescription)
      throws ManifoldCFException
    {
      connMgr.recordHistory(connectionName,startTime,ManifoldCF.qualifyOutputActivityName(activityType,outputConnectionName),dataSize,entityURI,resultCode,
        resultDescription,null);
    }
  }

}
