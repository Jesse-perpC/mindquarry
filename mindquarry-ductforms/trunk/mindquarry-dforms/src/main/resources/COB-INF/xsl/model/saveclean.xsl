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
	xmlns:df="http://mindquarry.com/ns/xml/ductforms"
	xmlns:fd="http://apache.org/cocoon/forms/1.0#definition"
	xmlns:ft="http://apache.org/cocoon/forms/1.0#template"
	xmlns:fi="http://apache.org/cocoon/forms/1.0#instance">
	
	<xsl:param name="rootElement" select="'ductform'"/>

	<!--+ 
		| This Stylesheet receives the XML representation of
		| a form and cleans up all unneccessary fields, as
		| the current form implementation adds all possible
		| fields to the form, not only the really used
	 	+-->
	<xsl:template match="@*|node()">
		<xsl:copy>
			<xsl:apply-templates select="@*|node()" />
		</xsl:copy>
	</xsl:template>
	
	<!-- rename the root element from 'ductform' to a specific one -->
	<xsl:template match="/ductform">
		<xsl:element name="{$rootElement}">
			<xsl:apply-templates/>
		</xsl:element>
	</xsl:template>

	<xsl:template match="/ductform/*">
		<xsl:variable name="myname" select="local-name(.)" />
		<xsl:if test="/ductform/ductforms/item[normalize-space(.)=$myname]">
			<xsl:copy>
				<xsl:apply-templates select="@*|node()" />
			</xsl:copy>
		</xsl:if>
	</xsl:template>

	<xsl:template match="/ductform/ductforms" />
</xsl:stylesheet>