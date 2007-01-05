<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

	<xsl:import href="block:/xslt/contextpath.xsl" />
	
	<xsl:param name="username" select="''" />
	
	<xsl:template match="@*|node()">
		<xsl:copy>
			<xsl:apply-templates select="@*|node()" />
		</xsl:copy>
	</xsl:template>
	
	<xsl:template match="//div[@id='replaceWithCurrentPhoto']">
		<img src="{$pathToRoot}teamspace/users/{normalize-space(id)}.png" />
	</xsl:template>
</xsl:stylesheet>
