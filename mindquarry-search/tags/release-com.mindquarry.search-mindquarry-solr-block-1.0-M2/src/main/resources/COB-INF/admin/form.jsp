<%@ page contentType="text/html; charset=utf-8" pageEncoding="UTF-8"%>
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
<%@include file="header.jsp" %>

<br clear="all">
<h2>/select mode</h2>

<form name=queryForm method="GET" action="../select">
<table>
<tr>
  <td>
	<strong>Solr/Lucene Statement</strong>
  </td>
  <td>
	<textarea rows="5" cols="60" name="q"></textarea>
  </td>
</tr>
<tr>
  <td>
	<strong>Protocol Version</strong>
  </td>
  <td>
	<input name="version" type="text" value="2.1">
  </td>
</tr>
<tr>
  <td>
	<strong>Start Row</strong>
  </td>
  <td>
	<input name="start" type="text" value="0">
  </td>
</tr>
<tr>
  <td>
	<strong>Maximum Rows Returned</strong>
  </td>
  <td>
	<input name="rows" type="text" value="10">
  </td>
</tr>
<tr>
  <td>
	<strong>Fields to Return</strong>
  </td>
  <td>
	<input name="fl" type="text" value="">
  </td>
</tr>
<tr>
  <td>
	<strong>Query Type</strong>
  </td>
  <td>
	<input name="qt" type="text" value="standard">
  </td>
</tr>
<tr>
  <td>
	<strong>Style Sheet</strong>
  </td>
  <td>
	<input name="stylesheet" type="text" value="">
  </td>
</tr>
<tr>
  <td>
	<strong>Indent XML</strong>
  </td>
  <td>
	<input name="indent" type="checkbox" checked="true">
  </td>
</tr>
<tr>
  <td>
	<strong>Debug: enable</strong>
  </td>
  <td>
	<input name="debugQuery" type="checkbox" >
  <em><font size="-1">  Note: do "view source" in your browser to see explain() correctly indented</font></em>
  </td>
</tr>
<tr>
  <td>
	<strong>Debug: explain others</strong>
  </td>
  <td>
	<input name="explainOther" type="text" >
  <em><font size="-1">  apply original query scoring to matches of this query</font></em>
  </td>
</tr>
<tr>
  <td>
	<strong>Enable Highlighting</strong>
  </td>
  <td>
	<input name="hl" type="checkbox" >
  </td>
</tr>
<tr>
  <td>
	<strong>Fields to Highlight</strong>
  </td>
  <td>
	<input name="hl.fl" type="text" >
  </td>
</tr>
<tr>
  <td>
  </td>
  <td>
	<input class="stdbutton" type="button" value="search" onclick="if (queryForm.q.value.length==0) alert('no empty queries, please'); else queryForm.submit();">
  </td>
</tr>
</table>
</form>


</body>
</html>
