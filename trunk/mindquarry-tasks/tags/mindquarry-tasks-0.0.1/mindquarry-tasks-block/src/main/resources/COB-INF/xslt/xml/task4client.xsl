<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

	<xsl:param name="base" />

	<!-- the root element should be task with a xml:base -->
	<xsl:template match="/task" priority="3">
		<task xml:base="{$base}">
			<xsl:apply-templates />
		</task>
	</xsl:template>


	<!-- Remove the unused namespace prefix decls
		 from ductforms (fi, df, etc.) - exclude-result-prefixes
		 does not work, because these prefixes are from the input
		 document -->

	<!-- match elements and manually copy them to remove the ns attribute -->		 
	<xsl:template match="*" priority="2">
		<xsl:element name="{local-name(.)}">
			<xsl:apply-templates select="@*|node()" />
		</xsl:element>
	</xsl:template>

	<!-- copy the rest (atts, text, comments) -->
	<xsl:template match="@*|node()" priority="1">
		<xsl:copy>
			<xsl:apply-templates select="@*|node()" />
		</xsl:copy>
	</xsl:template>

</xsl:stylesheet>