<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	xmlns:i="http://apache.org/cocoon/include/1.0"
	xmlns:collection="http://apache.org/cocoon/collection/1.0">

	<xsl:template match="/">
		<xsl:element name="resources">
			<xsl:apply-templates />
		</xsl:element>
	</xsl:template>	

	<!-- convert all file entries into an include statement for the content of that file -->
	<xsl:template match="collection:resource">
		<!-- <xi:include href="block:/{$path}/{@name}.model" /> -->
		<i:include src="block:/resource/{../@name}/{@name}" />
	</xsl:template>

</xsl:stylesheet>