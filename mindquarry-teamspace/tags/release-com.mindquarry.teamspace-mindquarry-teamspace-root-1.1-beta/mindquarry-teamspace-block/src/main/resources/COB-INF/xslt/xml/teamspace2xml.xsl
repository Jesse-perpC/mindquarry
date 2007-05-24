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

	<xsl:param name="reposURI" select="''" />
	
	<xsl:param name="basePath" />
	
	<xsl:template match="/teamspace">
		<teamspace xml:base="{$basePath}">
			<xsl:apply-templates select="id|name" />
		</teamspace>
	</xsl:template>

	<xsl:template match="id">
			<id><xsl:value-of select="normalize-space(.)" /></id>
			<workspace><xsl:value-of select="$reposURI" />/<xsl:value-of select="normalize-space(.)" />/trunk</workspace>
	</xsl:template>
	
	<xsl:template match="name">
		<name><xsl:value-of select="normalize-space(.)" /></name>
	</xsl:template>
</xsl:stylesheet>
