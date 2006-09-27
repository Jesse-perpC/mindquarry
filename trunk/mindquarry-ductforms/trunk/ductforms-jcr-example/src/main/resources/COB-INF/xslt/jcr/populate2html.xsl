<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

	<xsl:template match="@*|node()">
		<xsl:copy>
			<xsl:apply-templates select="@*|node()" />
		</xsl:copy>
	</xsl:template>

	<!-- xmldb style
	<xsl:template match="x:query[@result='failure'][@type='create']">
		<p style="color:red;">
			Failure creating resource:
			<xsl:value-of select="@oid" />
		</p>
	</xsl:template>

	<xsl:template match="x:query[@result='success'][@type='create']">
		<p style="color:green;">
			Success creating resource:
			<a href="./show/{@oid}"><xsl:value-of select="@oid" /></a>
		</p>
	</xsl:template>
	-->


</xsl:stylesheet>