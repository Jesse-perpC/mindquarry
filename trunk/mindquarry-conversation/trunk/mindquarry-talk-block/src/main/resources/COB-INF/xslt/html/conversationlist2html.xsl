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
	xmlns:source="http://apache.org/cocoon/source/1.0">
  
  <xsl:template match="/conversations">
    <html>
      <head>
        <title>Talk for <xsl:value-of select="/conversations[1]/teamspace[1]/name[1]"/></title>
        <xsl:apply-templates select="block" mode="headlinks"/>
      </head>
      <body>
        <ul class="pagination">
          <xsl:apply-templates select="block" mode="pagination"/>
        </ul>
        <xsl:apply-templates select="block[node()]"/>
      </body>
    </html>
  </xsl:template>
  
  <xsl:template match="block">
    <ul class="conversations">
       <xsl:apply-templates />
    </ul>
  </xsl:template>
  
  <xsl:template match="conversation">
    <li>
      <h2><a href="{@id}/"><xsl:apply-templates select="title" /></a></h2>
      <xsl:apply-templates select="message[position()=1]" />
    </li>
  </xsl:template>
  
  
  
  <xsl:template match="block" mode="pagination">
    <li><a href="{@id}"><xsl:value-of select="@id" /></a></li>
  </xsl:template>
  
  <xsl:template match="block[node()]" mode="pagination">
    <li><xsl:value-of select="@id" /></li>
  </xsl:template>
  
  <xsl:template match="block[position()=last()]" mode="headlinks">
    <link rel="last" href="{@id}">
      <xsl:attribute name="title">
        <xsl:value-of select="position()"/>
      </xsl:attribute>
    </link>
  </xsl:template>
  
  <xsl:template match="block" mode="headlinks">
    <link rel="following" href="{@id}">
      <xsl:attribute name="title">
        <xsl:value-of select="position()"/>
      </xsl:attribute>
    </link>
  </xsl:template>
  
  <xsl:template match="block[position()=1]" mode="headlinks">
    <link rel="start" href="{@id}">
      <xsl:attribute name="title">
        <xsl:value-of select="position()"/>
      </xsl:attribute>
    </link>
  </xsl:template>
  
  <xsl:template match="block[preceding-sibling::block[1][node()]]" mode="headlinks">
    <link rel="next" href="{@id}">
      <xsl:attribute name="title">
        <xsl:value-of select="position()"/>
      </xsl:attribute>
    </link>
  </xsl:template>
  
  <xsl:template match="block[following-sibling::block[1][node()]]" mode="headlinks">
    <link rel="prev" href="{@id}">
      <xsl:attribute name="title">
        <xsl:value-of select="position()"/>
      </xsl:attribute>
    </link>
  </xsl:template>
  
  </xsl:stylesheet>
