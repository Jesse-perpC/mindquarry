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
	
	<!-- xmldb style
	<xsl:template match="db:resource">
		<included path="{@name}">
			<i:include src="{/*/@base}/{@name}" />
		</included>
	</xsl:template>
	-->

	<xsl:template match="collection:resource">
		<included path="{@name}">
			<i:include src="{/*/@base}/{@name}" />
		</included>
	</xsl:template>


</xsl:stylesheet>