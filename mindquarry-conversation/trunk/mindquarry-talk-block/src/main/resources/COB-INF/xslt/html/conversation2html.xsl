<?xml version="1.0" encoding="UTF-8"?>
<!--
	Copyright (C) 2006-2007 Mindquarry GmbH, All Rights Reserved
	
	The contents of this file are subject to the Mozilla Public License
	Version 1.1 (the "License"); you may not use this file except in
	compliance with the License. You may obtain a copy of the License at
	http://www.mozilla.org/MPL/
	
	Software distributed under the License is distributed on an "AS IS"
	basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
	License for the specific language governing rights and limitations
	under the License.
-->
<xsl:stylesheet version="1.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
  xmlns:ci="http://apache.org/cocoon/include/1.0"
  xmlns:xlink="http://www.w3.org/1999/xlink"
  xmlns:collection="http://apache.org/cocoon/collection/1.0"
  xmlns:dateFormat="java:java.text.SimpleDateFormat"
	xmlns:source="http://apache.org/cocoon/source/1.0">
  
  <xsl:param name="now" />
  <xsl:param name="user" />
  <xsl:param name="email" />
  
  <xsl:template match="/messages">
    <html>
      <head>
        <title><xsl:value-of select="conversation[1]/title"/></title>
        <xsl:apply-templates select="block" mode="headlinks"/>
      </head>
      <xsl:apply-templates select="conversation/subscribers" />
      <body>
        
        <ul class="pagination">
          <xsl:apply-templates select="block" mode="pagination"/>
        </ul>
        <xsl:apply-templates select="block[node()]"/>
        
        <form action="new" method="POST">
          <textarea id="body" name="body"/>
          For longer messages you can use your e-mail program: <a href="mailto:{$email}"><xsl:value-of select="$email" /></a>
          <input type="submit" value="Send Message" />
          <!-- if you would like to add more link fields, add more
          <input type="text" name="link" value="/foo"/>
          <input type="text" name="link" value="/bar"/>
          -->
        </form>
      </body>
    </html>
  </xsl:template>
  
  <xsl:template match="subscribers[subscriber[@type='email'][normalize-space(.)=$user]]">
    <form action="meta" method="POST">
      <input type="hidden" name="unsubscribe-email" value="{$user}"/>
      <input type="submit" value="Unsubscribe"/>
    </form>
  </xsl:template>
  
  <xsl:template match="subscribers">
    <form action="meta" method="POST">
      <input type="hidden" name="subscribe-email" value="{$user}"/>
      <input type="submit" value="Subscribe"/>
    </form>
  </xsl:template>
  
  <xsl:template match="block">
    <ul class="messages">
       <xsl:apply-templates />
    </ul>
  </xsl:template>
  
  <xsl:template match="message">
    <xsl:call-template name="timestamp" />
    <li>
      <xsl:apply-templates />
    </li>
  </xsl:template>
  
  <xsl:template name="timestamp">
    <xsl:variable name="showtimestamp">
      <xsl:call-template name="showtimestamp">
        <xsl:with-param name="current" select="message/date"/>
      </xsl:call-template>
    </xsl:variable>
    <xsl:if test="normalize-space($showtimestamp)='true'">
      <li class="timestamp">
        <xsl:value-of select="message/date" />
      </li>
    </xsl:if>
  </xsl:template>
  
  <xsl:template name="showtimestamp">
    <xsl:param name="current" />
    <xsl:param name="preceding" select="$current/../../preceding-sibling::message/message/date"/>
    <xsl:choose>
      <!-- always show the first timestamp -->
      <xsl:when test="not($preceding)">
        true
      </xsl:when>
      <!-- every file messages -->
      <xsl:when test="count($preceding) mod 5=0">
        true
      </xsl:when>
      <xsl:otherwise>
        false
      </xsl:otherwise>
    </xsl:choose>
  </xsl:template>
  
  <xsl:template match="message/message">
    <div class="senderinfo {@via}">
      <img src="/teams/users/{normalize-space(from)}.png" alt="{from}" height="48" width="48"/>
      <span class="sender">
        <xsl:choose>
          <xsl:when test="from[text()]"><xsl:value-of select="from" /></xsl:when>
          <xsl:otherwise>unknown user</xsl:otherwise>
        </xsl:choose>
      </span>
    </div>
    <div class="body">
      <xsl:apply-templates select="body"/>
    </div>
    <xsl:if test="link">
      <ul class="links">
        <xsl:apply-templates select="link" />
      </ul>
    </xsl:if>
  </xsl:template>
  
  <xsl:template match="link">
    <li><a href="{normalize-space(.)}"><xsl:apply-templates /></a></li>
  </xsl:template>
  
  <xsl:template match="message/message/date">
    <xsl:text> </xsl:text>
    <xsl:variable name="diff" select="floor(($now - normalize-space(.)) div 1000)" />
    <span class="date" title="{.}">
    <xsl:choose>
      <xsl:when test="$diff &gt; 86400*2">
        <xsl:value-of select="floor($diff div 86400)" /> days
      </xsl:when>
      <xsl:when test="$diff &gt; 3600*2">
        <xsl:value-of select="floor($diff div 3600)" /> hours
      </xsl:when>
      <xsl:when test="$diff &gt; 60*2">
        <xsl:value-of select="floor($diff div 60)" /> minutes
      </xsl:when>
      <xsl:otherwise>
        <xsl:value-of select="$diff" /> seconds
      </xsl:otherwise>
    </xsl:choose>
    ago </span>
  </xsl:template>
  
  
  
  <xsl:template match="block" mode="pagination">
    <li><a href="?page={@id}"><xsl:value-of select="@id" /></a></li>
  </xsl:template>
  
  <xsl:template match="block[node()]" mode="pagination">
    <li><xsl:value-of select="@id" /></li>
  </xsl:template>
  
  <xsl:template match="block[position()=last()]" mode="headlinks">
    <link rel="last" href="?page={@id}">
      <xsl:attribute name="title">
        <xsl:value-of select="position()"/>
      </xsl:attribute>
    </link>
  </xsl:template>
  
  <xsl:template match="block" mode="headlinks">
    <link rel="following" href="?page={@id}">
      <xsl:attribute name="title">
        <xsl:value-of select="position()"/>
      </xsl:attribute>
    </link>
  </xsl:template>
  
  <xsl:template match="block[position()=1]" mode="headlinks">
    <link rel="start" href="?page={@id}">
      <xsl:attribute name="title">
        <xsl:value-of select="position()"/>
      </xsl:attribute>
    </link>
  </xsl:template>
  
  <xsl:template match="block[preceding-sibling::block[1][node()]]" mode="headlinks">
    <link rel="next" href="?page={@id}">
      <xsl:attribute name="title">
        <xsl:value-of select="position()"/>
      </xsl:attribute>
    </link>
  </xsl:template>
  
  <xsl:template match="block[following-sibling::block[1][node()]]" mode="headlinks">
    <link rel="prev" href="?page={@id}">
      <xsl:attribute name="title">
        <xsl:value-of select="position()"/>
      </xsl:attribute>
    </link>
  </xsl:template>
  
  </xsl:stylesheet>
