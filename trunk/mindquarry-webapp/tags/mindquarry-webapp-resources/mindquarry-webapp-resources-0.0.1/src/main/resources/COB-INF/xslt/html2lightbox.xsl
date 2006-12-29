<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	xmlns:xhtml="http://www.w3.org/1999/xhtml"
	xmlns="http://www.w3.org/1999/xhtml"
	xmlns:us="http://www.mindquarry.com/ns/schema/webapp">

	<xsl:template match="@*|node()">
		<xsl:copy>
			<xsl:apply-templates select="@*|node()" />
		</xsl:copy>
	</xsl:template>

	<xsl:template match="xhtml:html|html">
		<div id="lightbox-content">
			<xsl:apply-templates select="//head/script" />
			<xsl:choose>
				<xsl:when test="//div[@id='lightbox-content']">
					<xsl:apply-templates select="//div[@id='lightbox-content']/node()" />
				</xsl:when>
				<xsl:otherwise>
					<xsl:apply-templates select="//body/node()" />				
				</xsl:otherwise>
			</xsl:choose>
		</div>
	</xsl:template>
	
	<xsl:template match="xhtml:script[normalize-space(.)='']|script[normalize-space(.)='']">
	    <xsl:copy>
           <xsl:copy-of select="@*" />
           <xsl:text>//</xsl:text>
	    </xsl:copy>
	</xsl:template>
	
</xsl:stylesheet>
