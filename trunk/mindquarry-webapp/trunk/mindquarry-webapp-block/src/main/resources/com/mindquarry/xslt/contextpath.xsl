<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:param name="pathInfo" select="''" />
	
	<xsl:param name="context.path">
		<xsl:call-template name="generate.contextpath">
			<xsl:with-param name="path">
				<xsl:value-of select="$pathInfo"/>
			</xsl:with-param>
		</xsl:call-template>
	</xsl:param>
	
	<xsl:template name="generate.contextpath">
		<xsl:param name="path" />
		<xsl:choose>
			<xsl:when test="contains($path, '/')">
				<xsl:text>../</xsl:text>
				<xsl:call-template name="generate.contextpath">
					<xsl:with-param name="path" select="substring-after($path, '/')"/>
				</xsl:call-template>
			</xsl:when>
		</xsl:choose>
	</xsl:template>
</xsl:stylesheet>