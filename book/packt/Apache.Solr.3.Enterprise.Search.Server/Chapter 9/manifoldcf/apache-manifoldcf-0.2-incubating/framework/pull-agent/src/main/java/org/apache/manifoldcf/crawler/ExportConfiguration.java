/* $Id: ExportConfiguration.java 988245 2010-08-23 18:39:35Z kwright $ */

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
package org.apache.manifoldcf.crawler;

import org.apache.manifoldcf.core.interfaces.*;
import org.apache.manifoldcf.crawler.system.*;

/**
 * This class provides a script hook to export the crawler configuration to a file.
 */
public class ExportConfiguration extends BaseCrawlerInitializationCommand
{
  public static final String _rcsid = "@(#)$Id: ExportConfiguration.java 988245 2010-08-23 18:39:35Z kwright $";

  private final String exportFilename;

  public ExportConfiguration(String exportFilename)
  {
    this.exportFilename = exportFilename;
  }

  protected void doExecute(IThreadContext tc) throws ManifoldCFException
  {
    ManifoldCF.exportConfiguration(tc,exportFilename);
    Logging.root.info("Configuration exported");
  }

  public static void main(String[] args)
  {
    if (args.length != 1)
    {
      System.err.println("Usage: ExportConfiguration <filename>");
      System.exit(1);
    }

    String exportFilename = args[0];

    try
    {
      ExportConfiguration exportConfiguration = new ExportConfiguration(exportFilename);
      exportConfiguration.execute();
      System.err.println("Configuration exported");
    }
    catch (Exception e)
    {
      e.printStackTrace();
      System.exit(2);
    }
  }
}
