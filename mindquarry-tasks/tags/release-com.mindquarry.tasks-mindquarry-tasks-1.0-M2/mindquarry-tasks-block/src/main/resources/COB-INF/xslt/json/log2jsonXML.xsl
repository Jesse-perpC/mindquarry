<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet 
    xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:log="http://mindquarry.com/ns/schema/changelog" 
    xmlns:date="http://exslt.org/dates-and-times"
    extension-element-prefixes="date is-date"
    xmlns:team="http://mindquarry.com/ns/schema/teamtransform"
    xmlns:is-date="http://www.intelligentstreaming.com/xsl/date-time"
    version="1.0">
    
    <xsl:import href="servlet:resources:/xslt/date-time.xsl"/>
    <xsl:param name="baselink" />
    
    <xsl:template match="/">
        <object>
            <dateTimeFormat>iso8601</dateTimeFormat>
            <xsl:apply-templates />
        </object>
    </xsl:template>
    
    <xsl:template match="log:changelog">
        <events>
            <array>
                <xsl:apply-templates />
            </array>
        </events>
    </xsl:template>
    
    <xsl:template match="log:change">
        <object>
            <start><xsl:value-of select="is-date:iso-from-unix(@timestamp div 1000)" /></start>
            <link><xsl:value-of select="$baselink"/>?revision=<xsl:value-of select="@revision" /></link>
            <xsl:if test="@author">
                <title>Revision <xsl:value-of select="@revision" /></title>
            </xsl:if>
            <description><xsl:value-of select="@message" /></description>
        </object>
    </xsl:template>
</xsl:stylesheet>