<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	xmlns:xlink="http://www.w3.org/1999/xlink">

	<xsl:param name="reposURI" select="''" />
	
	<xsl:template match="/teamspace">
		<teamspace>
			<xsl:apply-templates select="id|name" />
		</teamspace>
	</xsl:template>

	<xsl:template match="id">
			<id><xsl:value-of select="normalize-space(.)" /></id>
			<workspace><xsl:value-of select="$reposURI" />/<xsl:value-of select="normalize-space(.)" />/trunk</workspace>
	</xsl:template>
	
	<xsl:template match="name">
		<name><xsl:value-of select="normalize-space(.)" /></name>
	</xsl:template>
</xsl:stylesheet>
