<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	
	<xsl:template match="/teamspaces">
		<teamspaces>
			<xsl:apply-templates select="teamspace/id" />
		</teamspaces>
	</xsl:template>
	
	<xsl:template match="teamspace/id">
		<teamspace><xsl:value-of select="normalize-space(.)" /></teamspace>
	</xsl:template>
</xsl:stylesheet>
