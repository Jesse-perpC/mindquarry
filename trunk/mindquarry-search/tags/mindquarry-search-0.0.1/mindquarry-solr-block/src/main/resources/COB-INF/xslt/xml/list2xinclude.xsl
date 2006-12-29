<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:xi="http://www.w3.org/2001/XInclude" version="1.0">
    
    
    <xsl:template match="changes">
        <update>
            <xsl:if test="count(modified/path)>0">
                <add>
                    <xsl:apply-templates select="modified/path"/>
                </add>
            </xsl:if>
    
            <xsl:if test="count(deleted/path)>0">
                <delete>
                    <xsl:apply-templates select="deleted/path"/>
                </delete>
            </xsl:if>
    
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
