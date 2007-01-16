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

	<xsl:param name="base" />

	<!-- the root element should be task with a xml:base -->
	<xsl:template match="/task" priority="3">
		<task xml:base="{$base}">
			<xsl:apply-templates />
		</task>
	</xsl:template>


	<!-- Remove the unused namespace prefix decls
		 from ductforms (fi, df, etc.) - exclude-result-prefixes
		 does not work, because these prefixes are from the input
		 document -->

	<!-- match elements and manually copy them to remove the ns attribute -->		 
	<xsl:template match="*" priority="2">
		<xsl:element name="{local-name(.)}">
			<xsl:apply-templates select="@*|node()" />
		</xsl:element>
	</xsl:template>

	<!-- copy the rest (atts, text, comments) -->
	<xsl:template match="@*|node()" priority="1">
		<xsl:copy>
			<xsl:apply-templates select="@*|node()" />
		</xsl:copy>
	</xsl:template>

</xsl:stylesheet>