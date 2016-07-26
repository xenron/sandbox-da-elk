/* $Id: AuthRequest.java 988245 2010-08-23 18:39:35Z kwright $ */

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
import org.apache.manifoldcf.authorities.interfaces.*;
import java.util.*;

/** This class describes a authorization request.  The request has state: It can be in an incomplete state, or it can be in a complete state.
* The thread that cares whether the request is complete needs to be able to wait for that situation to occur, so the request has
* a method that does just that.
*/
public class AuthRequest
{
  public static final String _rcsid = "@(#)$Id: AuthRequest.java 988245 2010-08-23 18:39:35Z kwright $";

  // This is where the request data actually lives
  protected String userID;
  protected String className;
  protected String identifyingString;
  protected ConfigParams configParameters;
  protected int maxConnections;

  // These are the possible results of the request
  protected boolean answerComplete = false;
  protected AuthorizationResponse answerResponse = null;
  protected Throwable answerException = null;

  /** Construct the request, and record the question.
  */
  public AuthRequest(String userID, String className, String identifyingString, ConfigParams configParameters, int maxConnections)
  {
    this.userID = userID;
    this.className = className;
    this.identifyingString = identifyingString;
    this.configParameters = configParameters;
    this.maxConnections = maxConnections;
  }

  /** Get the user id */
  public String getUserID()
  {
    return userID;
  }

  /** Get the class name */
  public String getClassName()
  {
    return className;
  }

  /** Get the identifying string, to pass back to the user if there was a problem */
  public String getIdentifyingString()
  {
    return identifyingString;
  }

  /** Get the configuration parameters */
  public ConfigParams getConfigurationParams()
  {
    return configParameters;
  }

  /** Get the maximum number of connections */
  public int getMaxConnections()
  {
    return maxConnections;
  }

  /** Wait for an auth request to be complete.
  */
  public void waitForComplete()
    throws InterruptedException
  {
    synchronized (this)
    {
      if (answerComplete)
        return;
      this.wait();
    }
  }

  /** Note that the request is complete, and record the answers.
  */
  public void completeRequest(AuthorizationResponse answerResponse, Throwable answerException)
  {
    synchronized (this)
    {
      if (answerComplete)
        return;

      // Record the answer.
      answerComplete = true;
      this.answerResponse = answerResponse;
      this.answerException = answerException;

      // Notify threads waiting on the answer.
      this.notifyAll();
    }
  }

  /** Get the answer tokens */
  public AuthorizationResponse getAnswerResponse()
  {
    return answerResponse;
  }

  /** Get the answer exception */
  public Throwable getAnswerException()
  {
    return answerException;
  }

}
