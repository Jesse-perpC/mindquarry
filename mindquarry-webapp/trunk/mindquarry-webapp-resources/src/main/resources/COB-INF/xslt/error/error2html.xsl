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
  <xsl:param name="showSupportContact" select="'true'"/>
  <xsl:param name="artifactId" select="'undefined'"/>
  <xsl:param name="version" select="'undefined'"/>
  <xsl:param name="timeStamp" select="'undefined'"/>
  
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
          
          .niftycontent {
            overflow: visible;
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
          
          // for bugreport.js
          var g_mindquarryErrorMessage = "<xsl:value-of select="translate(error:message, '&#10;&#13;','  ')"/>";
          var g_mindquarryStacktrace = "<xsl:value-of select="substring(normalize-space(error:extra[@error:description='stacktrace']), 0, 6774)"/>";
          var g_mindquarryFullStacktrace = "<xsl:value-of select="substring(normalize-space(error:extra[@error:description='full exception chain stacktrace']), 0, 6774)"/>";
          
        </script>
      </head>
      <body onload="createBugReport(); /* not called when dojo is active */">

        <h1 id="title">
          <xsl:value-of select="$title"/>
        </h1>

        <div class="nifty">
          <h2 id="errormessage">
            <xsl:value-of select="error:message"/>
          </h2>

          <!--<xsl:if test="$showSupportContact = 'true'">-->
            <p>
              <!-- href is replaced with detailed information when javascript is turned on.
                   this is the no-js fallback link -->
              <!-- [nn] at the end : first is javascript enabled, second is error page (n=no, y=yes) -->
              <a id="bugreport-onerror"
                href="mailto:support@mindquarry.com?subject=[Bug%20Report] {error:message} - {$artifactId} ({$version} {$timeStamp}) [ny]">
                Report this error
              </a>
              (via mail) or visit the 
              <a href="http://www.mindquarry.com/support">Mindquarry Support page</a>.
            </p>
            <p>
              Please refer to the detailed technical information below (it will
              be included in your bugreport automatically).
            </p>            
          <!--</xsl:if>-->
          
          <p class="version">Mindquarry Version <xsl:value-of select="$version"/> (<xsl:value-of select="$artifactId"/> - <xsl:value-of select="$timeStamp"/>)</p>
          
          <div class="technical">
            <a id="technical_details_switch" href="#" onclick="toggle('technical_details')">Technical details</a>
            
            <!-- the display of this div will be toggled by the link above -->
            <div id="technical_details" style="display: none">
              
              <h4>description:</h4>
              <pre>
                <xsl:value-of select="error:description"/>
              </pre>
              
              <xsl:apply-templates select="error:extra"/>
              
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
