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
  
  <xsl:template match="conversation[not(subscribers)]">
    <xsl:copy>
      <xsl:apply-templates />
      <subscribers>
        <xsl:apply-templates select="../team/subscriber[@type='email']"/>
      </subscribers>
    </xsl:copy>
  </xsl:template>
  
  <xsl:template match="subscribers">
    <xsl:copy>
      <xsl:apply-templates select="subscriber[@type='email']" />
      <xsl:apply-templates select="../../team/subscriber[@type='email']"/>
    </xsl:copy>
  </xsl:template>
  
  <xsl:template match="team/subscriber">
    <xsl:variable name="subscriber" select="normalize-space(.)" />
    <xsl:if test="0=count(../../conversation/subscribers/subscriber[@type='email'][normalize-space(.)=$subscriber])+count(../../conversation/subscribers/unsubscriber[@type='email'][normalize-space(.)=$subscriber])">
      <xsl:copy-of select="."/>
    </xsl:if>
  </xsl:template>
  
  <xsl:template match="team" />
  
	<xsl:template match="@*|node()">
		<xsl:copy>
			<xsl:apply-templates select="@*|node()" />
		</xsl:copy>
	</xsl:template>
</xsl:stylesheet>
