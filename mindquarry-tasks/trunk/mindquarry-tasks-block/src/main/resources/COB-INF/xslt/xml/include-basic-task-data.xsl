<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	xmlns:xlink="http://www.w3.org/1999/xlink">

	<xsl:param name="path" />

	<!-- access the JCR (via path param) and get the title -->	
	<xsl:template match="task">
		<task xlink:href="{@xlink:href}">
			<xsl:variable name="doc" select="document(concat($path, @xlink:href, '.xml'))" />
			<title><xsl:value-of select="$doc//title" /></title>
			<status><xsl:value-of select="$doc//status" /></status>
		</task>
	</xsl:template>

	<xsl:template match="@*|node()">
		<xsl:copy>
			<xsl:apply-templates select="@*|node()" />
		</xsl:copy>
	</xsl:template>

</xsl:stylesheet>