<?xml version="1.0"?>
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

	<xsl:param name="taskID" select="''" />
	<xsl:param name="teamspaceID" select="''" />

	<xsl:template match="@*|node()"/>

	<xsl:template match="task">
		<item>
			<link>
				../<xsl:value-of select="$taskID" />
			</link>
			<xsl:apply-templates />
		</item>
	</xsl:template>

	<xsl:template match="title">
		<title>
			<xsl:value-of select="normalize-space(.)" />
		</title>
	</xsl:template>

	<xsl:template match="status">
		<status>
			<xsl:value-of select="normalize-space(.)" />
		</status>
	</xsl:template>
	
	<xsl:template match="date">
		<date>
			<xsl:value-of select="normalize-space(.)" />
		</date>
	</xsl:template>

	<xsl:template match="summary">
		<summary>
			<xsl:value-of select="normalize-space(.)" />
		</summary>
	</xsl:template>
</xsl:stylesheet>
