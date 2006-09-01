<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	xmlns:df="http://mindquarry.com/ns/xml/ductforms"
	xmlns:html="http://www.w3.org/1999/xhtml"
	xmlns:fi="http://apache.org/cocoon/forms/1.0#instance"
	exclude-result-prefixes="#default html">
	<xsl:param name="page" />

	<xsl:template match="@*|node()">
		<xsl:copy>
			<xsl:apply-templates select="@*|node()" />
		</xsl:copy>
	</xsl:template>

	<xsl:template match="html:*">
		<xsl:element name="{local-name(.)}">
			<xsl:apply-templates select="@*|node()" />
		</xsl:element>
	</xsl:template>

	<xsl:template match="html:title">
		<title>
			<xsl:if test="//fi:form-template/@state='active'">
				<xsl:text>Editing &quot;</xsl:text>
			</xsl:if>
			<xsl:apply-templates select="@*|node()" />
			<xsl:if test="//fi:form-template/@state='active'">
				<xsl:text>&quot;</xsl:text>
			</xsl:if>
		</title>
	</xsl:template>

	<xsl:template match="fi:form-template[@state='active']">
		<xsl:copy>
			<xsl:apply-templates select="@*|node()" />
		</xsl:copy>
	</xsl:template>

	<xsl:template match="html:div[@class='form_block'][fi:action[@state='output']]">
	</xsl:template>
	
	<xsl:template match="fi:form-template[@state='output']">
		<xsl:copy>
			<xsl:apply-templates select="@*|node()" />
			<a href="{$page}.xml.edit">edit</a>
		</xsl:copy>	
	</xsl:template>


</xsl:stylesheet>