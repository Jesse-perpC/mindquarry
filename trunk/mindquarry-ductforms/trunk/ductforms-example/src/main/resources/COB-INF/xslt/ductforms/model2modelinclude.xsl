<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	xmlns="http://mindquarry.com/ns/xml/ductforms">

	<!-- resource name of the model instance, i.e. the concrete document -->
	<xsl:param name="instance" />

	<xsl:template match="@*|node()">
		<xsl:copy>
			<xsl:apply-templates select="@*|node()" />
		</xsl:copy>
	</xsl:template>

	<xsl:template match="/*">
		<xsl:copy>
			<instance>
				<!-- simply copy the document instance into a combined document -->
				<xsl:copy-of select="document($instance)/*/*" />
			</instance>
			<xsl:apply-templates select="@*|node()" />
		</xsl:copy>
	</xsl:template>

</xsl:stylesheet>