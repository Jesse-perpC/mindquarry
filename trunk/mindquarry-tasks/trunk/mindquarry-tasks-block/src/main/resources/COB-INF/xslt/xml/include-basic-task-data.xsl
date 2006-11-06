<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	xmlns:xlink="http://www.w3.org/1999/xlink">

	<xsl:param name="basePath" />

	<xsl:template match="tasks">
		<xsl:copy>
			<xsl:apply-templates />
		</xsl:copy>
	</xsl:template>

	<!-- access the JCR (via path param) and get the title -->	
	<xsl:template match="task">
		<task xlink:href="{@id}">
			<!-- "../@xlink:href" contains the teamspace id -->
			<xsl:variable name="doc" select="document(concat($basePath, ../@xlink:href, '/tasks/', @id, '.xml'))" />
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
