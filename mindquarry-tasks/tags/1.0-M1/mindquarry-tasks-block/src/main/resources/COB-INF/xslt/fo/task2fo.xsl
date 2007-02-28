<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	xmlns:xhtml="http://www.w3.org/1999/xhtml"
	xmlns:fo="http://www.w3.org/1999/XSL/Format"
	xmlns:xlink="http://www.w3.org/1999/xlink">

	<xsl:import href="block:/xslt/fo/xhtml2fo.xsl" />

	<xsl:variable name="pageTitle">
		<xsl:choose>
			<xsl:when test="string-length(normalize-space(/html/head/title)) = 0">
				Create New Task
			</xsl:when>
			<xsl:otherwise>
				Task: <xsl:value-of select="/html/head/title" />	
			</xsl:otherwise>
		</xsl:choose>
	</xsl:variable>
	
	<xsl:template match="xhtml:html|html">
		<fo:root xsl:use-attribute-sets="root">
			<xsl:call-template name="process-common-attributes"/>
			<xsl:call-template name="make-layout-master-set"/>
			<xsl:apply-templates/>
		</fo:root>
	</xsl:template>
	
	<xsl:template match="xhtml:head|head|xhtml:script|script"/>
	
	<xsl:template match="xhtml:body|body">
		<fo:page-sequence master-reference="all-pages">
			<fo:title>
				<xsl:value-of select="$pageTitle" />
			</fo:title>
			<fo:static-content flow-name="page-header">
				<fo:block space-before.conditionality="retain"
					space-before="{$page-header-margin}"
					xsl:use-attribute-sets="page-header">
					<xsl:if test="$title-print-in-header = 'true'">
						<xsl:value-of select="$pageTitle" />
					</xsl:if>
				</fo:block>
			</fo:static-content>
			<fo:static-content flow-name="page-footer">
				<fo:block space-after.conditionality="retain"
					space-after="{$page-footer-margin}"
					xsl:use-attribute-sets="page-footer">
					<xsl:if test="$page-number-print-in-footer = 'true'">
						<xsl:text>- </xsl:text>
						<fo:page-number/>
						<xsl:text> -</xsl:text>
					</xsl:if>
				</fo:block>
			</fo:static-content>
			<fo:flow flow-name="xsl-region-body">
				<fo:block xsl:use-attribute-sets="body">
					<xsl:call-template name="process-common-attributes"/>
					<xsl:apply-templates select="form/div/div[@class='ductform']"/>
				</fo:block>
			</fo:flow>
		</fo:page-sequence>
	</xsl:template>
	
	<xsl:template match="div[@class='ductform']/dl[@id='ductform.title']|xhtml:div[@class='ductform']/xhtml:dl[@id='ductform.title']" priority="7">
		<fo:block xsl:use-attribute-sets="h1">
			<xsl:apply-templates select="dd/span|xhtml:dd/xhtml:span"/>
		</fo:block>
	</xsl:template>
	
	<xsl:template match="div[@class='ductform']/dl[@id='ductform.content']|xhtml:div[@class='ductform']/xhtml:dl[@id='ductform.content']" priority="7">
		<fo:block>
			<xsl:apply-templates select="dd/*|xhtml:dd/*"/>
		</fo:block>
	</xsl:template>
	
	<xsl:template match="div[@class='ductform']/dl|xhtml:div[@class='ductform']/xhtml:dl">
		<fo:block xsl:use-attribute-sets="h2">
			<xsl:apply-templates select="dt/label/text()"></xsl:apply-templates>
		</fo:block>
		<fo:block>
			<xsl:apply-templates select="dd/*"></xsl:apply-templates>
		</fo:block>
	</xsl:template>

	<xsl:template match="xhtml:table|table" priority="7">
		<fo:table xsl:use-attribute-sets="table">
			<xsl:attribute name="table-layout">fixed</xsl:attribute>
			<!-- give fixed width for columns to avoid table stretching across the page border -->
			<xsl:variable name="nColumns" select="count(tbody/tr/th)"/>
			<xsl:for-each select="tbody/tr/th">
				<fo:table-column column-width="{100 div $nColumns}%"/>
			</xsl:for-each>
			<xsl:call-template name="process-table"/>
		</fo:table>
	</xsl:template>
	
	<xsl:template match="div[@class='ductform']/dl[@id='ductform.ductforms']|xhtml:div[@class='ductform']/xhtml:dl[@id='ductform.ductforms']" priority="7"/>
	
</xsl:stylesheet>
