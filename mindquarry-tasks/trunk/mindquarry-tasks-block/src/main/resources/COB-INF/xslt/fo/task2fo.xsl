<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	xmlns:xhtml="http://www.w3.org/1999/xhtml"
	xmlns:fo="http://www.w3.org/1999/XSL/Format"
	xmlns:xlink="http://www.w3.org/1999/xlink">

	<xsl:import href="block:/xslt/contextpath.xsl" />
	<xsl:import href="xhtml2fo.xsl" />

	<xsl:param name="viewDocumentLink" />

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
		
	<xsl:template match="div[@class='ductform']/dl[@id='ductform.ductforms']|xhtml:div[@class='ductform']/xhtml:dl[@id='ductform.ductforms']" priority="7"/>
	
	<xsl:template match="form/@action">
		<xsl:attribute name="action">
			<xsl:value-of select="$viewDocumentLink" />
		</xsl:attribute>
	</xsl:template>

	<!-- complete restructuring of the form fields:
		 put the title field at the beginning and wrap the rest inside
		 one large nifty box and the ductforms field chooser is removed -->
	
	<!-- the edit/save button is moved inside (see template below) -->
	<xsl:template match="div[@id='block_ductform_switch']" />
	
	<!-- include the edit/save button inside the nifty, have the title before -->
	<xsl:template match="div[@class='ductform']">
			<xsl:apply-templates select="dl[@id='ductform.title']"/>

			<xsl:apply-templates select="dl[@id!='ductform.title']"/>
	</xsl:template>
		
	<!-- Change 'Edit all' to 'Edit' (simpler for wiki) -->
	<xsl:template match="input[@id='ductform.ductforms_editall']/@value">
		<xsl:attribute name="value">Edit</xsl:attribute>
	</xsl:template>
	
</xsl:stylesheet>
