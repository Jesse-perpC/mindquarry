<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:xi="http://www.w3.org/2001/XInclude" version="1.0">
    
    <xsl:template match="changes">
        <update>
            <add>
                <xsl:apply-templates select="modified/path"/>
            </add>
    
            <delete>
                <xsl:apply-templates select="deleted/path"/>
            </delete>
    
            <commit/>
        </update>
    </xsl:template>

    <xsl:template match="deleted/path">
        <id>
            <xsl:value-of select="normalize-space(.)"/>
        </id>
    </xsl:template>

    <xsl:template match="modified/path">
        <source id="{normalize-space(.)}">
            <xi:include href="cocoon:/extract?url={normalize-space(.)}"/>
            <xi:include href="cocoon:/meta?url={normalize-space(.)}"/>
        </source>
    </xsl:template>
</xsl:stylesheet>
