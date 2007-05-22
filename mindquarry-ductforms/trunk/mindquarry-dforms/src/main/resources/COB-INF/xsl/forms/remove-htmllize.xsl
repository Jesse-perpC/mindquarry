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
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

	<xsl:template match="@*|node()">
		<xsl:copy>
			<xsl:apply-templates select="@*|node()" />
		</xsl:copy>
	</xsl:template>

	<!-- flatten the htmllize element, which contains a full html document -->	
	<xsl:template match="htmllize">
		<!--<xsl:apply-templates/>-->
		<xsl:apply-templates select="html/body/node()" />
	</xsl:template>	
  
  
  <xsl:template match="htmllize//*">
    <xsl:apply-templates />
  </xsl:template>
  
  <xsl:template match="htmllize//b">
    <strong><xsl:apply-templates /></strong>
  </xsl:template>
  
  <xsl:template match="htmllize//i">
    <em><xsl:apply-templates /></em>
  </xsl:template>
  
  <xsl:template match="htmllize//strike">
    <strike><xsl:apply-templates /></strike>
  </xsl:template>
  
  <xsl:template match="htmllize//img">
    <img><xsl:apply-templates select="@src|@height|@width|@title|@alt"/></img>
  </xsl:template>
  
  <xsl:template match="htmllize//a">
    <a>
      <xsl:apply-templates select="@name|@id|@href|@rel|node()"/>
    </a>
  </xsl:template>
  
  <xsl:template match="htmllize//div">
    <div>
      <xsl:apply-templates select="@align|@class|@id|node()" />
    </div>
  </xsl:template>
  
  <xsl:template match="htmllize//br|htmllize//ol|htmllize//ul|htmllize//li|htmllize//caption|htmllize//thead|htmllize//tbody|htmllize//tfooter|htmllize//tr|htmllize//td|htmllize//th|htmllize//p|htmllize//em|htmllize//strong">
    <xsl:copy>
      <xsl:apply-templates select="@class|@id|node()"/>
    </xsl:copy>
  </xsl:template>
  
  <xsl:template match="htmllize//table">
    <xsl:copy>
      <xsl:apply-templates select="@class|@id|@summary|@border|@cellpadding|@cellspacing|@height|@width|caption"/>
      <xsl:choose>
        <xsl:when test="tbody|thead|tfooter">
          <xsl:apply-templates select="tbody|thead|tfooter" />
        </xsl:when>
        <xsl:otherwise>
          <tbody>
            <xsl:apply-templates select="tr" />
          </tbody>
        </xsl:otherwise>
      </xsl:choose>
    </xsl:copy>
  </xsl:template>
</xsl:stylesheet>