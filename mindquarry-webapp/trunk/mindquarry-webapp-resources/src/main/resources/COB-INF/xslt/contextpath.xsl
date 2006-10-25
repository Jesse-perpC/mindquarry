<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	
	<!-- this must be the complete path from the Cocoon servlet root
		 to the current called matcher,
		 eg. /tasks/createTask/25 -->
	<xsl:param name="fullPath" select="''" />

	<!-- this must be the path called inside the current BlockServlet
		 eg. /createTask/25 -->
	<xsl:param name="sitemapPath" select="''" />
	
	<!-- this param will be available for stylesheets including this one,
		 and will be the relative path to the Cocoon servlet root,
		 eg. ../../../ -->
	<xsl:param name="pathToRoot">
		<xsl:call-template name="generate.contextpath.without.beginning.slash">
			<xsl:with-param name="path">
				<xsl:value-of select="$fullPath"/>
			</xsl:with-param>
		</xsl:call-template>
	</xsl:param>
	
	<!-- this param will be available for stylesheets including this one,
		 and will be the relative path to the BlockServlet root,
		 eg. ../../ -->
	<xsl:param name="pathToBlock">
		<xsl:call-template name="generate.contextpath.without.beginning.slash">
			<xsl:with-param name="path">
				<xsl:value-of select="$sitemapPath"/>
			</xsl:with-param>
		</xsl:call-template>
	</xsl:param>
	
	<!-- removes a starting slash before calling generate.contextpath -->	
	<xsl:template name="generate.contextpath.without.beginning.slash">
		<xsl:param name="path" />
		<xsl:choose>
			<xsl:when test="starts-with($path, '/')">
				<xsl:call-template name="generate.contextpath">
					<xsl:with-param name="path" select="substring($path, 2)"/>
				</xsl:call-template>
			</xsl:when>
			<xsl:otherwise>
				<xsl:call-template name="generate.contextpath">
					<xsl:with-param name="path" select="$path"/>
				</xsl:call-template>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>

	<!-- recursive template that generates this ../../ from path=/foo/bar -->
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