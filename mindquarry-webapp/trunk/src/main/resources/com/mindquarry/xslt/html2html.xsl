<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	xmlns="http://www.w3.org/1999/xhtml">
	
	<xsl:import href="contextpath.xsl"/>
	
	<xsl:template match="@*|node()">
		<xsl:copy>
			<xsl:apply-templates select="@*|node()" />
		</xsl:copy>
	</xsl:template>

	<xsl:template match="html">
		<html xmlns="http://www.w3.org/1999/xhtml">
			<xsl:apply-templates />	
		</html>
	</xsl:template>
	
	<xsl:template match="head">
		<head>
			<xsl:apply-templates select="*"/>
			<link rel="stylesheet" type="text/css" href="mindquarry.css" />
		</head>
	</xsl:template>
	
	<xsl:template match="title">
		<title>Mindquarry: <xsl:value-of select="." /></title>
	</xsl:template>
	
	<xsl:template match="body">
		<body>
			<a href="{$context.path}/mindquarry.css">mindquarry.css</a>
			<xsl:apply-templates select="@*" />
			<div class="body">
				<xsl:apply-templates />
			</div>
		</body>
	</xsl:template>
	
</xsl:stylesheet>