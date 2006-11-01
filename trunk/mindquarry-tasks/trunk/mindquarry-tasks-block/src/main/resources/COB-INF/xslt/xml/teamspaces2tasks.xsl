<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0"
        xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
        xmlns:xlink="http://www.w3.org/1999/xlink">
        
    <xsl:param name="base" />
        
    <!-- This stylesheet will simply replace the root element
    	 named "teamspaces" with a new root element "tasks-overview" -->

    <xsl:template match="/teamspaces">
    	<tasks xml:base="{$base}">
			<xsl:apply-templates />
    	</tasks>
    </xsl:template>

    <xsl:template match="teamspace">
    	<teamspace xlink:href="{id}" />
    </xsl:template>

	<!-- <xsl:template match="@*|node()">
		<xsl:copy>
			<xsl:apply-templates select="@*|node()" />
		</xsl:copy>
	</xsl:template> -->

</xsl:stylesheet>