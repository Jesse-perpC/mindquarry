<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0"
        xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
        
    <!-- This stylesheet will simply replace the root element
    	 named "teamspaces" with a new root element "tasks" -->

    <xsl:template match="/teamspaces">
    	<tasks>
			<xsl:apply-templates />
    	</tasks>
    </xsl:template>

	<xsl:template match="@*|node()">
		<xsl:copy>
			<xsl:apply-templates select="@*|node()" />
		</xsl:copy>
	</xsl:template>

</xsl:stylesheet>