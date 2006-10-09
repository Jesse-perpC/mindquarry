<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0"
        xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
        xmlns:map="http://apache.org/cocoon/sitemap/1.0">

    <!-- the dot format is text not xml -->
    <xsl:output method="text"/>
    <xsl:strip-space elements="*"/>

    <!-- don't write text nodes by default -->
    <xsl:template match="text()" mode="buildPipelines"/>
    <xsl:template match="text()" mode="connectFileSources"/>
    <xsl:template match="text()" mode="connectPipelines"/>

    <!-- the root matcher -->
    <xsl:template match="/">
digraph CocoonSitemap {
	compound = true;
    <xsl:apply-templates select="map:sitemap" mode="connectFileSources"/>
    <xsl:apply-templates select="map:sitemap" mode="buildPipelines"/>
    <xsl:apply-templates select="map:sitemap" mode="connectPipelines"/>
}
    </xsl:template>

	<!--
    <xsl:template match="map:pipeline">
		compound=true;
        <xsl:apply-templates />
		<xsl:for-each select="map:match">
			dispatcher -> <xsl:value-of select="generate-id(.)"/>;
        </xsl:for-each>
    </xsl:template>
	-->
    
    <xsl:template match="map:generate[@src] | map:transform[@src]" mode="connectPipelines">
		<xsl:choose>
			<xsl:when test="contains(@src, 'cocoon:/{1}')">
	"*<xsl:value-of select="substring(@src, 12)"/>_outputter" -> <xsl:value-of select="generate-id(.)"/> [style=filled,fillcolor=white];
			</xsl:when>
			<xsl:when test="contains(@src, 'cocoon:/')">
	"<xsl:value-of select="substring(@src, 9)"/>_outputter" -> <xsl:value-of select="generate-id(.)"/> [style=filled,fillcolor=white];
			</xsl:when>
			<xsl:otherwise>
	<!--"<xsl:value-of select="@src"/>" -> <xsl:value-of select="generate-id(.)"/>;-->
			</xsl:otherwise>
		</xsl:choose>
    </xsl:template>
	
    <xsl:template match="map:generate[@src] | map:transform[@src]" mode="connectFileSources">
		<xsl:choose>
			<xsl:when test="contains(@src, 'cocoon:/')">
	<!--"<xsl:value-of select="substring(@src, 9)"/>_outputter" -> <xsl:value-of select="generate-id(.)"/>;-->
			</xsl:when>
			<xsl:otherwise>
	"<xsl:value-of select="@src"/>" -> <xsl:value-of select="generate-id(.)"/> [style=filled,fillcolor=white];
			</xsl:otherwise>
		</xsl:choose>
    </xsl:template>

    <xsl:template match="map:match" mode="buildPipelines">
    subgraph "cluster_<xsl:value-of select="@pattern"/>" {
	<!--<xsl:value-of select="generate-id(.)"/>-->
        label = "<xsl:value-of select="@pattern"/>";
		style = filled;
		fillcolor = lightgrey;

       	<xsl:apply-templates mode="defineNodes"/>
        <!--<xsl:for-each select="map:generate | map:transform | map:serialize">-->
        <xsl:for-each select="*">
			<xsl:choose>
				<xsl:when test="position() != last()"><xsl:value-of select="generate-id(.)"/> -> </xsl:when>
				<xsl:otherwise>"<xsl:value-of select="../@pattern"/>_outputter";</xsl:otherwise>
			</xsl:choose>
        </xsl:for-each>
        <!--<xsl:for-each select="map:call | map:redirect-to">
			<xsl:value-of select="generate-id(.)"/>
			<xsl:choose>
				<xsl:when test="position() = last()">;</xsl:when>
				<xsl:otherwise>-></xsl:otherwise>
			</xsl:choose>
        </xsl:for-each>-->
    }
    </xsl:template>
    
	<!--
    <xsl:template match="map:when">
        subgraph cluster_<xsl:value-of select="generate-id(.)"/> {
            label = "<xsl:value-of select="@test"/>";
			
			<xsl:apply-templates mode="defineNodes"/>
			<xsl:for-each select="map:generate | map:transform | map:serialize | map:call | map:redirect-to">
				<xsl:value-of select="generate-id(.)"/>
				<xsl:choose>
					<xsl:when test="position() = last()">;</xsl:when>
					<xsl:otherwise>-></xsl:otherwise>
				</xsl:choose>
			</xsl:for-each>
    </xsl:template>
    
    <xsl:template match="map:otherwise">
        subgraph cluster_<xsl:value-of select="generate-id(.)"/> {
			
			<xsl:apply-templates mode="defineNodes"/>
			<xsl:for-each select="map:generate | map:transform | map:serialize | map:call | map:redirect-to">
				<xsl:value-of select="generate-id(.)"/>
				<xsl:choose>
					<xsl:when test="position() = last()">;</xsl:when>
					<xsl:otherwise>-></xsl:otherwise>
				</xsl:choose>
			</xsl:for-each>
        }
    </xsl:template>
	-->
    
    <xsl:template match="map:generate" mode="defineNodes">
        <xsl:value-of select="generate-id(.)"/> [shape=house,label="<xsl:choose><xsl:when test="@type"><xsl:value-of select="@type" /></xsl:when><xsl:otherwise>File</xsl:otherwise></xsl:choose> Generator",style=filled,fillcolor=white];
    </xsl:template>
    
    <xsl:template match="map:transform" mode="defineNodes">
        <xsl:value-of select="generate-id(.)"/> [shape=box,label="<xsl:choose><xsl:when test="@type"><xsl:value-of select="@type" /></xsl:when><xsl:otherwise>XSL</xsl:otherwise></xsl:choose> Transformer",style=filled,fillcolor=white];
    </xsl:template>
    
	<!-- there can be only one serializer and we must be able to identify it -->
    <xsl:template match="map:serialize" mode="defineNodes">
        "<xsl:value-of select="../@pattern"/>_outputter" [shape=invhouse,label="<xsl:value-of select="@type"/> Serializer",style=filled,fillcolor=white];
    </xsl:template>

    <xsl:template match="map:call" mode="defineNodes">
        "<xsl:value-of select="../@pattern"/>_outputter" [shape=parallelogram,label="Call: <xsl:value-of select="@function"/>",style=filled,fillcolor=white];
    </xsl:template>
    
    <xsl:template match="map:call[@continuation]" mode="defineNodes">
        <xsl:value-of select="generate-id(.)"/> [shape=parallelogram,label="Call Continuation: <xsl:value-of select="@continuation"/>",style=filled,fillcolor=white];
    </xsl:template>

    <xsl:template match="map:redirect-to" mode="defineNodes">
        <xsl:value-of select="generate-id(.)"/> [shape=parallelogram,label="Redirect to: <xsl:value-of select="@uri"/>",style=filled,fillcolor=white];
    </xsl:template>

</xsl:stylesheet>
