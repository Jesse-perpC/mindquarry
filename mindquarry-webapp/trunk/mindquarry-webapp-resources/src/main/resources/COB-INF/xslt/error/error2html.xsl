<?xml version="1.0"?>
<!--
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
-->

<!-- CVS $Id: error2html.xslt,v 1.18 2004/06/22 02:41:14 crossley Exp $ -->

<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
  xmlns:error="http://apache.org/cocoon/error/2.1">

  <!-- let sitemap override default page title -->
  <xsl:param name="pageTitle" select="//error:notify/error:title"/>
  <xsl:param name="httpStatus" select="''"/>
  <xsl:param name="showSupportContact" select="'false'"/>
  <xsl:param name="version" select="'undefined'"/>
  
  <xsl:variable name="title">
    <xsl:value-of select="$pageTitle"/>
    <xsl:if test="string-length($httpStatus) > 0">
      <xsl:text> [Error </xsl:text><xsl:value-of select="$httpStatus"/><xsl:text>]</xsl:text>
    </xsl:if>
  </xsl:variable>
  
  <xsl:template match="error:notify">
    <html>
      <head>
        <title>
          <xsl:value-of select="$title"/>
        </title>
        <!-- have css styles and javascript self-contained -->
        <style>
          
          p {
            font-size: 10pt;
          }
          
          h2 {
            margin-top: 0px;
            padding-top: 10px;
            font-size: 16pt;
          }
          
          .technical {
            padding-top: 1em;
            border-top: 1px solid grey;
          }
          
          #technical_details_switch {
            background-image: url(../buttons/collapsed_blue.gif);
            background-repeat: no-repeat;
            background-position = "0px 2px";
            padding-left: 20px;
            padding-bottom:5px;
          }
          
          .version {
            margin-top: 2em;
            color: grey;
          }
        </style>
        <script type="text/javascript">
          function toggle(id) {
              var element = document.getElementById(id);
              var switch_link = document.getElementById(id + "_switch");
              //var text = switch_link.firstChild;
              
              if (element.style.display == "none") {
                  element.style.display = "";
                  switch_link.style.backgroundImage = "url(../buttons/open.gif)";
                  switch_link.style.backgroundPosition = "0px 4px";
                  //text.nodeValue = "[Hide technical details]";
              } else{
                  element.style.display = "none";
                  switch_link.style.backgroundImage = "url(../buttons/collapsed_blue.gif)";
                  switch_link.style.backgroundPosition = "0px 2px";
                  //text.nodeValue = "[Show technical details]";
              }
          }          
        </script>
      </head>
      <body>

        <h1 id="title">
          <xsl:value-of select="$title"/>
        </h1>

        <div class="nifty">
          <h2 id="errormessage">
            <xsl:value-of select="error:message"/>
          </h2>

          <xsl:if test="$showSupportContact = 'true'">
            <p>
              If you need technical support go to the
              <a href="http://www.mindquarry.com/support">Mindquarry Support page</a>.
              Please refer to the detailed technical information below.
            </p>            
          </xsl:if>
          
          <p class="version">Mindquarry Version <xsl:value-of select="$version"/></p>
          
          <div class="technical">
            <a id="technical_details_switch" href="#" onclick="toggle('technical_details')">Technical details</a>
            
            <!-- the display of this div will be toggled by the link above -->
            <div id="technical_details" style="display: none">
              
              <h4>description:</h4>
              <pre>
                <xsl:value-of select="error:description"/>
              </pre>
              
              <xsl:apply-templates select="error:extra"/>
              
              <!--
                
                <ul>
                <li>specify the version of Cocoon you're using, or we'll assume that you are talking
                about the latest released version;</li>
                <li>specify the platform-operating system-version-servlet container version;</li>
                <li>send any pertinent error message;</li>
                <li>send pertinent log snippets;</li>
                <li>send pertinent sitemap snippets;</li>
                <li>send pertinent parts of the page that give you problems.</li>
                </ul>
                
                <p>For more detailed technical information, take a look at the log files in the log
                directory of Cocoon, which is placed by default in the <code>WEB-INF/logs/</code>
                folder of your cocoon webapp context.<br/> If the logs don't give you enough
                information, you might want to increase the log level by changing the Logging
                configuration which is by default the <code>WEB-INF/log4j.xconf</code> file. </p>
                
                <p>If you think you found a bug, please report it to <a
                href="http://issues.apache.org/jira/browse/COCOON">Apache Cocoon issue tracker</a>;
                a message will automatically be sent to the developer mailing list and you'll be kept
                in contact automatically with the further progress on that bug. </p>
                
                <p>Thanks, and sorry for the trouble if this is our fault. </p>
                
                <p>The <a href="http://cocoon.apache.org/">Apache Cocoon</a> Project</p>
              -->
            </div>
          </div>

        </div>
        
      </body>
    </html>
  </xsl:template>

  <xsl:template match="error:extra">
    <h4><xsl:value-of select="@error:description"/>:&#160;</h4>
    <pre>
      <xsl:value-of select="translate(.,'&#13;','')"/>
    </pre>
  </xsl:template>

</xsl:stylesheet>
