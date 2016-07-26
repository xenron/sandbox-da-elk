/* $Id: UnRegisterAllOutputs.java 988245 2010-08-23 18:39:35Z kwright $ */

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
package org.apache.manifoldcf.agents;

import org.apache.manifoldcf.core.interfaces.*;
import org.apache.manifoldcf.agents.interfaces.*;
import org.apache.manifoldcf.agents.system.*;

/**
 * Un-register all current output connector classes
 */
public class UnRegisterAllOutputs extends BaseAgentsInitializationCommand
{
  public static final String _rcsid = "@(#)$Id: UnRegisterAllOutputs.java 988245 2010-08-23 18:39:35Z kwright $";

  public UnRegisterAllOutputs()
  {
  }

  protected void doExecute(IThreadContext tc) throws ManifoldCFException
  {
    IDBInterface database = DBInterfaceFactory.make(tc,
      ManifoldCF.getMasterDatabaseName(),
      ManifoldCF.getMasterDatabaseUsername(),
      ManifoldCF.getMasterDatabasePassword());
    IOutputConnectorManager mgr = OutputConnectorManagerFactory.make(tc);
    IOutputConnectionManager connManager = OutputConnectionManagerFactory.make(tc);
    IResultSet classNames = mgr.getConnectors();
    int i = 0;
    while (i < classNames.getRowCount())
    {
      IResultRow row = classNames.getRow(i++);
      String className = (String)row.getValue("classname");
      // Deregistration should be done in a transaction
      database.beginTransaction();
      try
      {
        // Find the connection names that come with this class
        String[] connectionNames = connManager.findConnectionsForConnector(className);
        // For all connection names, notify all agents of the deregistration
        AgentManagerFactory.noteOutputConnectorDeregistration(tc,connectionNames);
        // Now that all jobs have been placed into an appropriate state, actually do the deregistration itself.
        mgr.unregisterConnector(className);
      }
      catch (ManifoldCFException e)
      {
        database.signalRollback();
        throw e;
      }
      catch (Error e)
      {
        database.signalRollback();
        throw e;
      }
      finally
      {
        database.endTransaction();
      }
    }
    Logging.root.info("Successfully unregistered all output connectors");
  }


  public static void main(String[] args)
  {
    if (args.length > 0)
    {
      System.err.println("Usage: UnRegisterAllOutputs");
      System.exit(1);
    }

    try
    {
      UnRegisterAllOutputs unRegisterAllOutputs = new UnRegisterAllOutputs();
      unRegisterAllOutputs.execute();
      System.err.println("Successfully unregistered all output connectors");
    }
    catch (ManifoldCFException e)
    {
      e.printStackTrace();
      System.exit(1);
    }
  }
}
