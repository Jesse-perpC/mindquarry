<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

	<xsl:template match="@*|node()">
		<xsl:copy>
			<xsl:apply-templates select="@*|node()" />
		</xsl:copy>
	</xsl:template>

	<xsl:template match="body">
		<xsl:copy>
			<xsl:apply-templates select="//title[1]" mode="header" />
			<xsl:apply-templates select="@*|node()" />
		</xsl:copy>
	</xsl:template>

	<xsl:template match="title" mode="header">
		<h1>
			<xsl:apply-templates />
		</h1>
	</xsl:template>

	<xsl:template match="head">
		<xsl:copy>
			<xsl:apply-templates select="@*|node()" />
			<link rel="stylesheet" type="text/css"
				href="resources/css/style.css" />
		</xsl:copy>
	</xsl:template>


</xsl:stylesheet>