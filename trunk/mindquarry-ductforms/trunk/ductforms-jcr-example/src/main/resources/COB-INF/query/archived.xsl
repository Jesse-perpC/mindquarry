<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	xmlns:i="http://apache.org/cocoon/include/1.0"
	xmlns:db="http://apache.org/cocoon/xmldb/1.0"
	xmlns:df="http://mindquarry.com/ns/xml/ductforms">
	<df:title path="archived">Archived Documents</df:title>

	<xsl:template match="included[.//archived='true']">
		<li>
			<a href="{@path}">
				<xsl:apply-templates mode="link" />
			</a>
		</li>
	</xsl:template>

	<xsl:template match="included" />
	
	<xsl:template match="*" mode="link">
		<xsl:value-of select="title" />
		<xsl:if test="normalize-space(title)=''">
			&lt;untitled&gt;
		</xsl:if>
	</xsl:template>

</xsl:stylesheet>