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
        xmlns="http://www.w3.org/2005/Atom"
        xmlns:date="http://exslt.org/dates-and-times"
        extension-element-prefixes="date"
        xmlns:xlink="http://www.w3.org/1999/xlink">

	<xsl:import href="servlet:/xslt/contextpath.xsl" />
		
	<xsl:param name="teamspaceID" />   
  <xsl:param name="hostname" />
  <xsl:param name="baselink" />
  
<xsl:template match="/tasks">
BEGIN:VCALENDAR
VERSION: 2.0
PRODID:-//hacksw/handcal//NONSGML v1.0//EN
<xsl:apply-templates select="task" />
END:VCALENDAR
</xsl:template>
  
<xsl:template match="task">
BEGIN:VTODO
UID:<xsl:value-of select="$teamspaceID" />/<xsl:value-of select="@xlink:href" />@<xsl:value-of select="$hostname" />
URL:<xsl:value-of select="$baselink"/><xsl:value-of select="@xlink:href" />
<xsl:apply-templates />
END:VTODO
</xsl:template>

<xsl:template match="title">
SUMMARY: <xsl:value-of select="normalize-space(.)" />
</xsl:template>

<xsl:template match="summary[string-length(.)!=0]">
DESCRIPTION: <xsl:value-of select="normalize-space(.)" />
</xsl:template>

<xsl:template match="date[string-length(.)!=0]">
DUE: <xsl:value-of select="substring(.,7)" /><xsl:value-of select="substring(.,4,2)" /><xsl:value-of select="substring(.,1,2)" /><xsl:text>T0000Z</xsl:text>
</xsl:template>

<xsl:template match="*" />
<!--
    <xsl:template match="/tasks">
    <feed xmlns="http://www.w3.org/2005/Atom">
      <updated><xsl:value-of select="date:date-time()"/></updated>
      <title>Tasks for <xsl:value-of select="$teamspaceID" /></title>
      <link rel="self" href="{$baselink}?http-accept-header=application/atom+xml" />
      <id>feed:<xsl:value-of select="$baselink" />?http-accept-header=application/atom+xml</id>
			<xsl:apply-templates select="task">
        <xsl:sort select="id" />
      </xsl:apply-templates>
    </feed>
    </xsl:template>

    <xsl:template match="task">
      <entry>
        <title>
          <xsl:choose>
            <xsl:when test="string-length(title) > 0">
              <xsl:value-of select="title" />
            </xsl:when>
            <xsl:otherwise>
              &lt;no title&gt;
            </xsl:otherwise>
          </xsl:choose>
        </title>
        <author>
          <name>Mindquarry</name>
        </author>
        <xsl:if test="summary//text()">
          <summary>
            <xsl:value-of select="summary//node()" />
          </summary>
        </xsl:if>
        <content type="xhtml"><div xmlns="http://www.w3.org/1999/xhtml"> <xsl:value-of select="summary//node()" /></div></content>
        <link href="{$baselink}{@xlink:href}" />
        <updated><xsl:value-of select="date:date-time()"/></updated>
        <id>feed:<xsl:value-of select="$baselink" /><xsl:value-of select="@xlink:href"/></id>
      </entry>
    </xsl:template>
-->
</xsl:stylesheet>
