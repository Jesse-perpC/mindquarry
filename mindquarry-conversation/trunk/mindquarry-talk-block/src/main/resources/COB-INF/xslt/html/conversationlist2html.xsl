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
  xmlns:team="http://mindquarry.com/ns/schema/teamtransform"
	xmlns:source="http://apache.org/cocoon/source/1.0">
  
  <xsl:param name="now" />
  <xsl:param name="user" />
  
  <xsl:template match="/conversations">
    <html>
      <head>
        <title>Talk for <xsl:value-of select="teamspace[1]/name[1]"/></title>
        <xsl:apply-templates select="block" mode="headlinks"/>
        <link rel="up" href=".." title="All Teams"/>
      </head>
      <body>
        <xsl:apply-templates select="team" />
        <a href="new">new conversation</a>
        <ul class="pagination">
          <xsl:apply-templates select="block" mode="pagination"/>
        </ul>
        <xsl:apply-templates select="block[node()]"/>
      </body>
    </html>
  </xsl:template>
  
  <xsl:template match="team[subscriber[@type='email'][normalize-space(.)=$user]]">
    <form action="meta" method="POST">
      <input type="hidden" name="unsubscribe-email" value="{$user}"/>
      <input type="submit" value="Unsubscribe from new"/>
    </form>
  </xsl:template>
  
  <xsl:template match="team">
    <form action="meta" method="POST">
      <input type="hidden" name="subscribe-email" value="{$user}"/>
      <input type="submit" value="Subscribe to new"/>
    </form>
  </xsl:template>
  
  <xsl:template match="block">
    <ul class="conversations">
       <xsl:apply-templates />
    </ul>
  </xsl:template>
  
  <xsl:template match="conversation">
    <li>
      <h2><a href="{@id}/"><xsl:apply-templates select="title" /></a></h2>
      <xsl:apply-templates select="message[position()=1]/message" mode="first"/>
      <xsl:apply-templates select="message[position()=last()]/message" mode="last"/>
    </li>
  </xsl:template>
  
  <xsl:template match="message[position()=1]" mode="first">
    <div class="firstmessage message">
      Started by <xsl:apply-templates select="from"/>
      <xsl:apply-templates select="date" />
    </div>
  </xsl:template>
  
  <xsl:template match="message[position()=last()]" mode="last">
    <div class="firstmessage message">
      Last comment by <xsl:apply-templates select="from"/>
      <xsl:apply-templates select="date" />
      <div class="content">
        <xsl:value-of select="substring(body,1,60)" />
      </div>
    </div>
  </xsl:template>
  
  <xsl:template match="message/message/from">
    <span class="from"><team:user><xsl:value-of select="." /></team:user></span>
  </xsl:template>
  
  <xsl:template match="message/message/from[not(node())]">
    <span class="from">unknown user</span>
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
