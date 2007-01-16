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
	xmlns:xlink="http://www.w3.org/1999/xlink">

	<xsl:param name="basePath" />

	<!--<xsl:template match="filters">
		<xsl:copy>
			<xsl:apply-templates />
		</xsl:copy>
	</xsl:template>-->

	<!-- access the JCR (via path param) and get the title -->	
	<xsl:template match="filter">
		<filter xlink:href="filters/{@id}">
			<!-- "../@xlink:href" contains the teamspace id -->
			<xsl:variable name="doc" select="document(concat($basePath, ../@xlink:href, '/tasks/filters/', @id))" />
			<title><xsl:value-of select="$doc/filterBuilder/title" /></title>
		</filter>
	</xsl:template>

	<xsl:template match="@*|node()">
		<xsl:copy>
			<xsl:apply-templates select="@*|node()" />
		</xsl:copy>
	</xsl:template>
</xsl:stylesheet>
