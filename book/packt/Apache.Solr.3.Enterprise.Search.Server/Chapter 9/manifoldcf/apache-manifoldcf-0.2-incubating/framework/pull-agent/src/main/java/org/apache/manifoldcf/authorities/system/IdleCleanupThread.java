/* $Id: IdleCleanupThread.java 988245 2010-08-23 18:39:35Z kwright $ */

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
package org.apache.manifoldcf.authorities.system;

import org.apache.manifoldcf.core.interfaces.*;
import org.apache.manifoldcf.agents.interfaces.*;
import org.apache.manifoldcf.authorities.interfaces.*;
import org.apache.manifoldcf.authorities.system.Logging;
import java.util.*;
import java.lang.reflect.*;

/** This thread periodically calls the cleanup method in all connected repository connectors.  The ostensible purpose
* is to allow the connectors to shutdown idle connections etc.
*/
public class IdleCleanupThread extends Thread
{
  public static final String _rcsid = "@(#)$Id: IdleCleanupThread.java 988245 2010-08-23 18:39:35Z kwright $";

  // Local data


  /** Constructor.
  */
  public IdleCleanupThread()
    throws ManifoldCFException
  {
    super();
    setName("Idle cleanup thread");
    setDaemon(true);
  }

  public void run()
  {
    // Create a thread context object.
    IThreadContext threadContext = ThreadContextFactory.make();

    // Loop
    while (true)
    {
      // Do another try/catch around everything in the loop
      try
      {
        // Do the cleanup
        AuthorityConnectorFactory.pollAllConnectors(threadContext);

        // Sleep for the retry interval.
        ManifoldCF.sleep(15000L);
      }
      catch (ManifoldCFException e)
      {
        if (e.getErrorCode() == ManifoldCFException.INTERRUPTED)
          break;

        // Log it, but keep the thread alive
        Logging.authorityService.error("Exception tossed",e);

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
      catch (Throwable e)
      {
        // A more severe error - but stay alive
        Logging.authorityService.fatal("Error tossed",e);
      }
    }
  }

}
