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

	<xsl:param name="required"
		select="document('cocoon:/complete-model.xml')/df:model/df:datatype[@required='true']" />

	<xsl:param name="existing" select="/*[0]/*" />

	<xsl:template match="@*|node()">
		<xsl:copy>
			<xsl:apply-templates select="@*|node()" />
		</xsl:copy>
	</xsl:template>

	<xsl:template match="/*">
		<ductform>
			<!-- existing fields first, but strip out deleted fields -->
			<xsl:apply-templates select="*" />
			<!-- required fields next -->
			<xsl:for-each select="$required">
				<xsl:variable name="myname" select="@id" />
				<xsl:if test="not($existing[local-name(.)=$myname])">
					<xsl:element name="{$myname}">
						<xsl:attribute name="required">true</xsl:attribute>
					</xsl:element>
				</xsl:if>
			</xsl:for-each>
			<!-- other added fields last -->
			<ductforms>
				<xsl:apply-templates select="*" mode="ductforms-item" />
				<xsl:for-each select="$required">
					<xsl:variable name="myname" select="@id" />
					<xsl:if
						test="not($existing[local-name(.)=$myname])">
						<item>
							<xsl:attribute name="required">true</xsl:attribute>
							<xsl:value-of select="$myname" />
						</item>
					</xsl:if>
				</xsl:for-each>
			</ductforms>

		</ductform>
	</xsl:template>

	<xsl:template match="*" mode="ductforms-item">
		<item>
			<xsl:value-of select="local-name(.)" />
		</item>
	</xsl:template>
	
	<xsl:template match="ductforms"/>
	<xsl:template match="ductforms" mode="ductforms-item"/>
	
</xsl:stylesheet>