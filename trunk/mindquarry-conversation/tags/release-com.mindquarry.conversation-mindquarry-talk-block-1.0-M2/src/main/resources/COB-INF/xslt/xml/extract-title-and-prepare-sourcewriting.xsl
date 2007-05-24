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
	xmlns:source="http://apache.org/cocoon/source/1.0"
	xmlns:jx="http://apache.org/cocoon/templates/jx/1.0">

	<xsl:param name="basePath" />
  <xsl:param name="metaPath" />

	<!-- the root element should be wrapped into a source:write directive -->
	<xsl:template match="/">
		<source:write create="true">
			<!-- the jx will call the TalkManager component to calculate
				 a unique name for the wiki page, which needs the basePath
				 and most important the title of the wiki page as parameters -->
			<source:source>
				<xsl:value-of select="$basePath" />
				<jx:out>
					<xsl:attribute name="value">
						<xsl:text>${talkManager.getUniqueTalkId('</xsl:text>
						<xsl:value-of select="$basePath"/>
						<xsl:text>','</xsl:text>
						<xsl:value-of select="//title"/>
						<xsl:text>')}</xsl:text>
					</xsl:attribute>
				</jx:out>
				<xsl:text>/meta.xml</xsl:text>
			</source:source>
			<source:fragment>
				<xsl:apply-templates />
			</source:fragment>
		</source:write>
	</xsl:template>
  
  <xsl:template match="conversation[not(subscribers)]">
    <xsl:copy>
      <xsl:apply-templates />
      <subscribers>
        <xsl:call-template name="add-subscribers"/>
      </subscribers>
    </xsl:copy>
  </xsl:template>
  
  <xsl:template match="subscribers">
    <xsl:copy>
      <xsl:apply-templates />
      <xsl:call-template name="add-subscribers"/>
    </xsl:copy>
  </xsl:template>
  
  <xsl:template name="add-subscribers">
    <include xmlns="http://apache.org/cocoon/include/1.0" src="{$metaPath}"/>
  </xsl:template>

	<xsl:template match="@*|node()">
		<xsl:copy>
			<xsl:apply-templates select="@*|node()" />
		</xsl:copy>
	</xsl:template>

</xsl:stylesheet>