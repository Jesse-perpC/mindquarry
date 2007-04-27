<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
    
    <xsl:template match="teamspaces">
        <body>
            <div class="teamlist" dojoType="mindquarry:TeamList">
                <ul>
                    <xsl:apply-templates />
                </ul>
            </div>
        </body>
    </xsl:template>
    
    <xsl:template match="teamspace">
        <li><a href="/teams/team/{normalize-space(id)}/" title="{normalize-space(id)}"><xsl:apply-templates select="name"></xsl:apply-templates></a></li>
    </xsl:template>
    
    <xsl:template match="name">
        <xsl:apply-templates />
    </xsl:template>
    
    <xsl:template match="node() | @*">
        <xsl:copy>
            <xsl:apply-templates select="node() | @*"/>
        </xsl:copy>
    </xsl:template>
    
</xsl:stylesheet>
