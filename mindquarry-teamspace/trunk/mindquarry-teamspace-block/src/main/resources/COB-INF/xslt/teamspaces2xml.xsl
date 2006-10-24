<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

	<xsl:param name="reposURI" select="''" />
	
	<xsl:template match="/teamspaces">
		<teamspaces>
			<xsl:apply-templates select="teamspace/id" />
		</teamspaces>
	</xsl:template>

	<xsl:template match="teamspace/id">
		<teamspace>
			<id><xsl:value-of select="normalize-space(.)" /></id>
			<workspace><xsl:value-of select="$reposURI" />/<xsl:value-of select="normalize-space(.)" />/trunk</workspace>
		</teamspace>
	</xsl:template>
</xsl:stylesheet>
