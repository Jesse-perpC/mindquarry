<%--
 Licensed to the Apache Software Foundation (ASF) under one or more
 contributor license agreements.  See the NOTICE file distributed with
 this work for additional information regarding copyright ownership.
 The ASF licenses this file to You under the Apache License, Version 2.0
 (the "License"); you may not use this file except in compliance with
 the License.  You may obtain a copy of the License at

     http://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.
--%>
<%@ page import="org.apache.solr.core.SolrConfig,
                 org.apache.solr.core.SolrCore,
                 org.apache.solr.schema.IndexSchema,
                 java.io.File"%>
<%@ page import="java.net.InetAddress"%>
<%@ page import="org.apache.solr.core.Config"%>

<%
  SolrCore core = SolrCore.getSolrCore();
  int port = request.getServerPort();
  IndexSchema schema = core.getSchema();

  // enabled/disabled is purely from the point of a load-balancer
  // and has no effect on local server function.  If there is no healthcheck
  // configured, don't put any status on the admin pages.
  String enabledStatus = null;
  String enabledFile = SolrConfig.config.get("admin/healthcheck/text()",null);
  boolean isEnabled = false;
  if (enabledFile!=null) {
    isEnabled = new File(enabledFile).exists();
  }

  String collectionName = schema!=null ? schema.getName():"unknown";
  InetAddress addr = InetAddress.getLocalHost();
  String hostname = addr.getCanonicalHostName();

  String defaultSearch = SolrConfig.config.get("admin/defaultQuery/text()",null);
  String cwd=System.getProperty("user.dir");
  String solrHome= Config.getInstanceDir();
%>
