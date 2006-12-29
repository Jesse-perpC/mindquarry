<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	xmlns:df="http://mindquarry.com/ns/xml/ductforms"
	xmlns:i="http://apache.org/cocoon/include/1.0">
	<xsl:template match="/df:model">
		<xsl:copy>
			<xsl:apply-templates select="df:datatype">
				<xsl:sort select="@position" />
			</xsl:apply-templates>
		</xsl:copy>
	</xsl:template>
	
	<xsl:template match="df:datatype">
		<xsl:copy-of select="."/>
	</xsl:template>
</xsl:stylesheet>