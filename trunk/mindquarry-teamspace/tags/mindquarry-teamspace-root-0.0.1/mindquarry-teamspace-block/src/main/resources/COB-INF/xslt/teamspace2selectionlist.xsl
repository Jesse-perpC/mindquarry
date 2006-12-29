<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet 
    xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0"
    xmlns:fd="http://apache.org/cocoon/forms/1.0#definition">
    <xsl:template match="teamspace">
        <fd:selection-list>
            <xsl:apply-templates select="//user"></xsl:apply-templates>
        </fd:selection-list>
    </xsl:template>
    
    <xsl:template match="user">
        <fd:item value="{id}">
            <xsl:if test="normalize-space(name) or normalize-space(surname)">
                <fd:label>
                    <xsl:value-of select="normalize-space(name)"/>
                    <xsl:text> </xsl:text>
                    <xsl:value-of select="normalize-space(surname)"/>
                </fd:label>
            </xsl:if>
        </fd:item>
    </xsl:template>
</xsl:stylesheet>