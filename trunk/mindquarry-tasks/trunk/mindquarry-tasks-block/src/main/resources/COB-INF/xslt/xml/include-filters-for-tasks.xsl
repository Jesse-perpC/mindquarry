<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	xmlns:xlink="http://www.w3.org/1999/xlink"
	xmlns:xi="http://www.w3.org/2001/XInclude">

	<xsl:template match="@*|node()">
		<xsl:copy>
			<xsl:apply-templates select="@*|node()" />
		</xsl:copy>
	</xsl:template>

	<xsl:template match="/tasks">
		<tasks xlink:href="{@xlink:href}">
			<xsl:apply-templates />
			<xi:include href="cocoon:/internal/pipe/filters/{@xlink:href}/list.xml" />
		</tasks>
	</xsl:template>

</xsl:stylesheet>