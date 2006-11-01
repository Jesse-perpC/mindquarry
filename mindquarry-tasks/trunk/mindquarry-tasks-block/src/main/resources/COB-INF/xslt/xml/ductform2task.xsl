<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0"
        xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
        xmlns:xlink="http://www.w3.org/1999/xlink">
        
    <xsl:param name="base" />
        
    <xsl:template match="/ductform">
    	<task xml:base="{$base}">
			<xsl:apply-templates />
    	</task>
    </xsl:template>

	<!-- clear the ductforms element hint -->
    <xsl:template match="ductforms" />

	<xsl:template match="@*|node()">
		<xsl:copy>
			<xsl:apply-templates select="@*|node()" />
		</xsl:copy>
	</xsl:template>

</xsl:stylesheet>