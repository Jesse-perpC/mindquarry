<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0"
        xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
		xmlns:collection="http://apache.org/cocoon/collection/1.0">

    <xsl:template match="/collection:collection">
    	<tasks>
    		<xsl:apply-templates />
    	</tasks>
    </xsl:template>

    <xsl:template match="collection:resource">
		<task>
			<id><xsl:value-of select="substring-before(@name, '.xml')" /></id>
		</task>
    </xsl:template>

</xsl:stylesheet>