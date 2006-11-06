<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0"
        xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
		xmlns:xhtml="http://www.w3.org/1999/xhtml"
        xmlns:xlink="http://www.w3.org/1999/xlink">
        
	<xsl:import href="block:/xslt/contextpath.xsl" />
		
    <xsl:param name="viewDocumentLink" />

	<xsl:template match="@*|node()">
		<xsl:copy>
			<xsl:apply-templates select="@*|node()" />
		</xsl:copy>
	</xsl:template>
	
	<xsl:template match="xhtml:head|head">
		<head>
			<link rel="stylesheet" href="{$pathToBlock}css/tasks-edit.css" media="screen,projection" type="text/css" />
		</head>
	</xsl:template>

	<xsl:template match="form[@id='ductform' and @state='active']">
		<h1>Editing Task "<xsl:value-of select="//div[@id='block.ductform.title']/span/input/@value" />"</h1>
		<div class="nifty">
			<xsl:copy-of select="." />
		</div>
		<a href="{$viewDocumentLink}">Cancel</a>
	</xsl:template>
</xsl:stylesheet>