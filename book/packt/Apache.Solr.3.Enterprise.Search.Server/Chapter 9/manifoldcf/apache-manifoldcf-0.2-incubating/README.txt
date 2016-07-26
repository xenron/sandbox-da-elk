# Licensed to the Apache Software Foundation (ASF) under one or more
# contributor license agreements.  See the NOTICE file distributed with
# this work for additional information regarding copyright ownership.
# The ASF licenses this file to You under the Apache License, Version 2.0
# (the "License"); you may not use this file except in compliance with
# the License.  You may obtain a copy of the License at
#
#     http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.

Welcome to the Apache ManifoldCF project!
-----------------------------------------

Apache ManifoldCF is a multi-repository crawler framework, with multiple connectors,
under incubation.

For a complete description of the ManifoldCF project, team composition, source
code repositories, and other details, please see the ManifoldCF web site at
http://incubator.apache.org/connectors


Getting Started
---------------

You will need to build ManifoldCF before you can do anything else.

Instructions for Building Apache ManifoldCF from Source
-----------------------------------------------------

1. Download the Java SE 5 JDK (Java Development Kit), or greater, from http://java.sun.com.
   You will need the JDK installed, and the %JAVA_HOME%\bin directory included
   on your command path.  To test this, issue a "java -version" command from your
   shell and verify that the Java version is 1.5 or greater.

2. Download the Apache Ant binary distribution (1.7.0 or greater) from http://ant.apache.org.
   You will need Ant installed and the %ANT_HOME%\bin directory included on your
   command path.  To test this, issue a "ant -version" command from your
   shell and verify that Ant is available.

3. If you want to build the site documents, check out, build, and install Apache Forrest
   version 0.9-dev.
   
4. In a shell, change to the root directory of the source (where you find the outermost
   build.xml file), and type "ant" for directions.


Some Files Included In Apache ManifoldCF Distributions
----------------------------------------------------

dist/doc/index.html
  The Apache ManifoldCF documentation root, which includes the Javadoc.

dist/doc/how-to-build-and-deploy.html
  The Apache ManifoldCF documentation page describing building and deployment.

framework
  The sources for the Apache ManifoldCF framework.
  
connectors
  The sources for the Apache ManifoldCF connectors.

site
  The sources for the Apache ManifoldCF documentation.
  
build.xml
  The root ant build script for Apache ManifoldCF.

Running the example
--------------------

After you build, the example can be found under dist/example.  Start the example
by going to that directory and typing "<path_to_java> -jar start.jar".  Full documentation
can also be found in "dist/doc".

Licensing
---------

ManifoldCF is licensed under the
Apache License 2.0. See the files called LICENSE.txt and NOTICE.txt
for more information.

Cryptographic Software Notice
-----------------------------

This distribution may include software that has been designed for use
with cryptographic software. The country in which you currently reside
may have restrictions on the import, possession, use, and/or re-export
to another country, of encryption software. BEFORE using any encryption
software, please check your country's laws, regulations and policies
concerning the import, possession, or use, and re-export of encryption
software, to see if this is permitted. See <http://www.wassenaar.org/>
for more information.

The U.S. Government Department of Commerce, Bureau of Industry and
Security (BIS), has classified this software as Export Commodity
Control Number (ECCN) 5D002.C.1, which includes information security
software using or performing cryptographic functions with asymmetric
algorithms. The form and manner of this Apache Software Foundation
distribution makes it eligible for export under the License Exception
ENC Technology Software Unrestricted (TSU) exception (see the BIS
Export Administration Regulations, Section 740.13) for both object
code and source code.

The following provides more details on the included software that
may be subject to export controls on cryptographic software:

  ManifoldCF interfaces with the
  Java Secure Socket Extension (JSSE) API to provide

    - HTTPS support

  ManifoldCF interfaces with the
  Java Cryptography Extension (JCE) API to provide

    - NTLM authentication

  ManifoldCF does not include any
  implementation of JSSE or JCE.

Contact
-------

  o For general information visit the main project site at
    http://incubator.apache.org/connectors


=========================================================================
==     Jetty Notice                                                    ==
=========================================================================
==============================================================
 Jetty Web Container 
 Copyright 1995-2006 Mort Bay Consulting Pty Ltd
==============================================================

This product includes some software developed at The Apache Software 
Foundation (http://www.apache.org/).

The javax.servlet package used by Jetty is copyright 
Sun Microsystems, Inc and Apache Software Foundation. It is 
distributed under the Common Development and Distribution License.
You can obtain a copy of the license at 
https://glassfish.dev.java.net/public/CDDLv1.0.html.

The UnixCrypt.java code ~Implements the one way cryptography used by
Unix systems for simple password protection.  Copyright 1996 Aki Yoshida,
modified April 2001  by Iris Van den Broeke, Daniel Deville.

The default JSP implementation is provided by the Glassfish JSP engine
from project Glassfish http://glassfish.dev.java.net.  Copyright 2005
Sun Microsystems, Inc. and portions Copyright Apache Software Foundation.

Some portions of the code are Copyright:
  2006 Tim Vernum 
  1999 Jason Gilbert.

The jboss integration module contains some LGPL code.

=========================================================================
==     HSQLDB Notice                                                   ==
=========================================================================

For content, code, and products originally developed by Thomas Mueller and the Hypersonic SQL Group:

Copyright (c) 1995-2000 by the Hypersonic SQL Group.
All rights reserved.

Redistribution and use in source and binary forms, with or without
modification, are permitted provided that the following conditions are met:

Redistributions of source code must retain the above copyright notice, this
list of conditions and the following disclaimer.

Redistributions in binary form must reproduce the above copyright notice,
this list of conditions and the following disclaimer in the documentation
and/or other materials provided with the distribution.

Neither the name of the Hypersonic SQL Group nor the names of its
contributors may be used to endorse or promote products derived from this
software without specific prior written permission.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
ARE DISCLAIMED. IN NO EVENT SHALL THE HYPERSONIC SQL GROUP,
OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
(INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.

This software consists of voluntary contributions made by many individuals on behalf of the
Hypersonic SQL Group.

For work added by the HSQL Development Group (a.k.a. hsqldb_lic.txt):

Copyright (c) 2001-2005, The HSQL Development Group
All rights reserved.

Redistribution and use in source and binary forms, with or without
modification, are permitted provided that the following conditions are met:

Redistributions of source code must retain the above copyright notice, this
list of conditions and the following disclaimer.

Redistributions in binary form must reproduce the above copyright notice,
this list of conditions and the following disclaimer in the documentation
and/or other materials provided with the distribution.

Neither the name of the HSQL Development Group nor the names of its
contributors may be used to endorse or promote products derived from this
software without specific prior written permission.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
ARE DISCLAIMED. IN NO EVENT SHALL HSQL DEVELOPMENT GROUP, HSQLDB.ORG,
OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
(INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
