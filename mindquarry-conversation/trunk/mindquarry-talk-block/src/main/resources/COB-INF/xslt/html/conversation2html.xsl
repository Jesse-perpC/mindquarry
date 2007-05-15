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
    xmlns:team="http://mindquarry.com/ns/schema/teamtransform"
	xmlns:source="http://apache.org/cocoon/source/1.0">
  
  <xsl:import href="servlet:/xslt/html/paging.xsl" />
  
  <xsl:param name="now" />
  <xsl:param name="user" />
  <xsl:param name="email" />
  
  <xsl:template name="head-links">
	<link rel="section-global-action" href="new" title="New Conversation" class="button button22 new-conversation-action"/>
  </xsl:template>
  
  <xsl:template match="messages">
    <html>
      <head>
        <title><xsl:value-of select="conversation[1]/title"/></title>
		<link rel="up" href=".." title="All Talks"/>
	
		<link rel="breadcrumb" title="Talks" href=".."/>
		<link rel="breadcrumb" title="{conversation[1]/title}"/>
		<link rel="section-global-action" title="New Conversation" href="../new" />
        <xsl:apply-templates select="block" mode="headlinks"/>

      </head>
      <body>
	  
	      <xsl:apply-templates select="conversation/subscribers" />
        <div class="list">
			<ul class="conversations">
				<xsl:apply-templates select="block[node()]"/>
				<li>
					<img src="../../images/{normalize-space($user)}/web.png" title="You say:" class="icon"/>
					<p><a name="post"> </a> </p>
					<div>
						<form action="new" method="POST">
						  <textarea id="body" name="body" class="talk-textarea"/><br />
						  <input type="submit" value="Send Message" class="button button22 new-subscribe-action"/>
						   <span>For longer messages you can use your e-mail program: <a href="mailto:{$email}">
						  <xsl:value-of select="$email" /></a></span>
						  <!-- if you would like to add more link fields, add more
						  <input type="text" name="link" value="/foo"/>
						  <input type="text" name="link" value="/bar"/>
						  -->
						</form>
					</div>
				</li>
			</ul>

		
		</div>


	</body>
    </html>
  </xsl:template>
  
  <xsl:template match="subscribers[subscriber[@type='email'][normalize-space(.)=$user]]">
    <form action="meta" method="POST" class="button button22 new-unsubscribe-action">
      <input type="hidden" name="unsubscribe-email" value="{$user}"/>
      <input type="submit" value="Unsubscribe"/>
    </form>
  </xsl:template>
  
  <xsl:template match="subscribers">
    <form action="meta" method="POST" class="button button22 new-subscribe-action">
      <input type="hidden" name="subscribe-email" value="{$user}"/>
      <input type="submit" value="Subscribe"/>
    </form>
  </xsl:template>
  
  <xsl:template match="block">
           <xsl:apply-templates select="message/message"/>
      </xsl:template>
  
  <xsl:template match="message/message">
  	<xsl:call-template name="timestamp" />
    <li class="senderinfo {@via}">
      <img class="icon" src="../../images/{normalize-space(from)}/{@via}.png" title="{from}" height="48" width="48"/>	
		<div>
			<blockquote>
				<xsl:apply-templates select="body"/>
			</blockquote>
		</div>
	</li>
	</xsl:template>
  
  <xsl:template match="link">
    <a href="{normalize-space(.)}"><xsl:apply-templates /></a><br />
  </xsl:template>
  <xsl:template match="message">
    <xsl:call-template name="timestamp" />
      <xsl:apply-templates />
  </xsl:template>
  
  <xsl:template name="timestamp">
    <xsl:variable name="showtimestamp">
      <xsl:call-template name="showtimestamp">
        <xsl:with-param name="current" select="date"/>
      </xsl:call-template>
    </xsl:variable>
    <xsl:if test="normalize-space($showtimestamp)='true'">
        <xsl:apply-templates select="date"/>
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

  <xsl:template match="message/message/date">
    <xsl:text> </xsl:text>
    <xsl:variable name="diff" select="floor(($now - normalize-space(.)) div 1000)" />
    <li class="date"><p><xsl:text> </xsl:text></p><div><xsl:text> </xsl:text></div><div><span class="date" title="{.}">
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
    ago </span></div></li>
  </xsl:template>

</xsl:stylesheet>
