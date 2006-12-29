<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	xmlns:source="http://apache.org/cocoon/source/1.0">

	<xsl:param name="path" />

	<!-- the root element should be wrapped into a source:write directive -->
	<xsl:template match="/">
		<source:write create="true">
			<source:source><xsl:value-of select="$path" /></source:source>
			<source:fragment>
				<xsl:apply-templates />
			</source:fragment>
		</source:write>
	</xsl:template>

	<xsl:template match="@*|node()">
		<xsl:copy>
			<xsl:apply-templates select="@*|node()" />
		</xsl:copy>
	</xsl:template>

</xsl:stylesheet>