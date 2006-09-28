<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	xmlns:i="http://apache.org/cocoon/include/1.0"
	xmlns:df="http://mindquarry.com/ns/xml/ductforms"
	xmlns:collection="http://apache.org/cocoon/collection/1.0">
	<df:title path="index">All Documents</df:title>
	
	<xsl:template match="included">
		<li>
			<a href="{@path}">
				<xsl:apply-templates mode="link" />
			</a>
		</li>
	</xsl:template>
	
	<xsl:template match="included[.//archived='true']" />

	<xsl:template match="*" mode="link">
		<xsl:value-of select="title" />
	</xsl:template>
	
</xsl:stylesheet>