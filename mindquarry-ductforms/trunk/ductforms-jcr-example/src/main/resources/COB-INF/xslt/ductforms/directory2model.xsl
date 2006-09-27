<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	xmlns:df="http://mindquarry.com/ns/xml/ductforms"
	xmlns:dir="http://apache.org/cocoon/directory/2.0"
	xmlns:xi="http://www.w3.org/2001/XInclude">
	
	<xsl:template match="/*">
		<xsl:element name="df:{@name}" namespace="http://mindquarry.com/ns/xml/ductforms">
			<xsl:apply-templates select="dir:file">
				<xsl:with-param name="path"><xsl:value-of select="@name" /></xsl:with-param>
			</xsl:apply-templates>
		</xsl:element>
	</xsl:template>
	
	<xsl:template match="dir:file">
		<xsl:param name="path" />
		<xi:include href="{$path}/{@name}" />
	</xsl:template>
	
</xsl:stylesheet>