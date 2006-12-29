<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	xmlns:xlink="http://www.w3.org/1999/xlink">
	
	<xsl:template match="/teamspaces">
		<teamspaces>
			<xsl:apply-templates select="teamspace" />
		</teamspaces>
	</xsl:template>

	<xsl:template match="teamspace">
		<teamspace xlink:href="{normalize-space(id)}">
			<xsl:apply-templates select="name" />
		</teamspace>
	</xsl:template>
	
	<xsl:template match="name">
		<xsl:value-of select="normalize-space(.)" />
	</xsl:template>
</xsl:stylesheet>
