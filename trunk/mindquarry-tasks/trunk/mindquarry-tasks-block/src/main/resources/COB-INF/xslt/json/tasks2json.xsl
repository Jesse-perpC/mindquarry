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
        
  <xsl:param name="query" select="''" />
  
<xsl:template match="/tasks">
{
  "responseHeader": {
    "status":0
  },
  "response":{
    "numFound":<xsl:value-of select="count(*)" />,
    "start":0,
    "maxScore":0,
    "docs":{
      "tasks":[
        <xsl:apply-templates select="task[contains(.,$query)]"/>
      ]
    }
  }
}
</xsl:template>

<xsl:template match="task"> {"location":"<xsl:value-of select="//@xml:base[1]" /><xsl:value-of select="@xlink:href" />", "title": "<xsl:call-template name="strip-quotes">
  <xsl:with-param name="text">
    <xsl:value-of select="title" />
  </xsl:with-param>
</xsl:call-template>", "description":"<xsl:call-template name="strip-quotes">
  <xsl:with-param name="text">
    <xsl:value-of select="summary" />
  </xsl:with-param>
</xsl:call-template>", "picture":"<xsl:value-of select="//@xml:base[1]" />../resource/icons/ductform.status/<xsl:value-of select="status" />.png  "}
  <xsl:if test="position()!=last()">,</xsl:if>
</xsl:template>

<xsl:template name="strip-quotes">
  <xsl:param name="text" />
  <xsl:if test="contains($text, '&#x22;')">
    <xsl:value-of select="substring-before($text, '&#x22;')" />
    <xsl:text>\"</xsl:text>
    <xsl:call-template name="strip-quotes">
      <xsl:with-param name="text">
        <xsl:value-of select="substring-after($text, '&#x22;')" />
      </xsl:with-param>
    </xsl:call-template>
  </xsl:if>
  <xsl:if test="not(contains($text, '&#x22;'))">
    <xsl:value-of select="$text" />
  </xsl:if>
</xsl:template>

</xsl:stylesheet>
