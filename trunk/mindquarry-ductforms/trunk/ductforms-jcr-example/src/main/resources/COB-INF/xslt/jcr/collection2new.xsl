<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0"
        xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
        xmlns:i="http://apache.org/cocoon/include/1.0"
		xmlns:collection="http://apache.org/cocoon/collection/1.0">
    
    <xsl:template match="@*|node()">
        <xsl:copy>
            <xsl:apply-templates select="@*|node()" />
        </xsl:copy>
    </xsl:template>

    <xsl:template match="collection:resource">
        <included path="{@name}">
            <i:include src="{@uri}" />
        </included>
    </xsl:template>

    <xsl:template match="/collection:collection">
        <xsl:copy>
            <xsl:apply-templates select="@*|node()" />
            <collection:new name="wiki{count(*)}" />
        </xsl:copy>
    </xsl:template>

</xsl:stylesheet>