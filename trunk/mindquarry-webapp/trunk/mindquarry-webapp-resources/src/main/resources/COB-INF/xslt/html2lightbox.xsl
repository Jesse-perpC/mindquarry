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
	xmlns:xhtml="http://www.w3.org/1999/xhtml"
	xmlns="http://www.w3.org/1999/xhtml"
	xmlns:us="http://www.mindquarry.com/ns/schema/webapp">

	<!--<xsl:import href="contextpath.xsl"/>-->
	
	<xsl:param name="username" select="''" />
	
	<xsl:template match="@*|node()">
		<xsl:copy>
			<xsl:apply-templates select="@*|node()" />
		</xsl:copy>
	</xsl:template>

	<xsl:template match="xhtml:html|html">
		<div id="lightbox-content">
			<xsl:apply-templates select="//head/script" />
			<xsl:choose>
				<xsl:when test="//div[@id='lightbox-content']">
					<xsl:apply-templates select="//div[@id='lightbox-content']/node()" />
				</xsl:when>
				<xsl:otherwise>
					<xsl:apply-templates select="//body/node()" />				
				</xsl:otherwise>
			</xsl:choose>
		</div>
	</xsl:template>
  
  <xsl:template match="xhtml:html[.//xhtml:tbody[@id='lightbox-content']]|html[.//tbody[@id='lightbox-content']]">
    <div>
       <xsl:apply-templates select=".//xhtml:tbody[@id='lightbox-content']/xhtml:tr|.//tbody[@id='lightbox-content']/tr" />
    </div>
  </xsl:template>
  
  <xsl:template match="xhtml:tbody[@id='lightbox-content']/xhtml:tr|tbody[@id='lightbox-content']/tr">
    <div class="pseudotr {@class}">
      <xsl:apply-templates />
    </div>
  </xsl:template>
  
  <xsl:template match="xhtml:tbody[@id='lightbox-content']/xhtml:tr/td|tbody[@id='lightbox-content']/tr/td">
    <div class="pseudotd {@class}">
      <xsl:apply-templates />
    </div>
  </xsl:template>
  
  <xsl:template match="*[@class='stripfromlightbox' or contains(@class, 'stripfromlightbox ') or contains(@class, ' stripfromlightbox')]">
  
  </xsl:template>
	
	<xsl:template match="xhtml:script[normalize-space(.)='']|script[normalize-space(.)='']">
	    <xsl:copy>
           <xsl:copy-of select="@*" />
           <xsl:text>//</xsl:text>
	    </xsl:copy>
	</xsl:template>
	
	<xsl:template match="//div[@hint='replaceWithUserPhoto']">
		<!-- TODO: add real contextPath before /teamspace  -->
		<img id="{@id}" class="{@class}" src="/teamspace/users/70/{normalize-space($username)}.png" />
	</xsl:template>	
	
</xsl:stylesheet>
