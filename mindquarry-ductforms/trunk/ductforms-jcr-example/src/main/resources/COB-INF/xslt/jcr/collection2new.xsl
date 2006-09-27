<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	xmlns:i="http://apache.org/cocoon/include/1.0">

	<xsl:template match="@*|node()">
		<xsl:copy>
			<xsl:apply-templates select="@*|node()" />
		</xsl:copy>
	</xsl:template>

	<!-- 
		<xsl:template match="db:resource">
		<included path="{@name}">
		<i:include src="{/*/@base}/{@name}" />
		</included>
		</xsl:template>
	-->

	<!-- xmldb style
	<xsl:template match="/db:collections">
		<xsl:copy>
			<xsl:apply-templates select="@*|node()" />
			<db:new name="wiki{count(*)}" />
		</xsl:copy>
	</xsl:template>
	-->


</xsl:stylesheet>