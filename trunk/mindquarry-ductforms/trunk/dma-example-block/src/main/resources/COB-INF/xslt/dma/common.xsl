<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	xmlns:mindquarry="mindquarry"
	xmlns:collection="http://apache.org/cocoon/collection/1.0">
	<xsl:param name="repo" select="''"/>
	<xsl:param name="path" select="''"/>
	<xsl:param name="revision" select="false()"/>
	
	<xsl:param name="myrevision">
		<xsl:choose>
			<xsl:when test="$revision">
				<xsl:value-of select="$revision" />
			</xsl:when>
			<xsl:otherwise>
				<xsl:value-of select="/collection:collection/@revision" />
			</xsl:otherwise>
		</xsl:choose>
	</xsl:param>
	
	<xsl:param name="reversepath">
		<xsl:call-template name="reversepath">
			<xsl:with-param name="path">
				<xsl:if test="$revision">
					<xsl:value-of select="$revision" /><xsl:text>/</xsl:text>
				</xsl:if>
				<xsl:value-of select="$repo"/>
				<xsl:value-of select="$path"/>
			</xsl:with-param>
		</xsl:call-template>
	</xsl:param>
	
	<xsl:template name="reversepath">
		<xsl:param name="path" />
		<xsl:choose>
			<xsl:when test="contains($path, '/')">
				<xsl:text>../</xsl:text>
				<xsl:call-template name="reversepath">
					<xsl:with-param name="path" select="substring-after($path, '/')"/>
				</xsl:call-template>
			</xsl:when>
		</xsl:choose>
	</xsl:template>
	
</xsl:stylesheet>