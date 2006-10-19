<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	xmlns:dir="http://apache.org/cocoon/directory/2.0"
	xmlns:i="http://apache.org/cocoon/include/1.0">

	<!-- <xsl:template match="@*|node()">
		<xsl:copy>
			<xsl:apply-templates select="@*|node()" />
		</xsl:copy>
	</xsl:template> -->
	
	<xsl:template match="/">
		<xsl:element name="resources">
			<xsl:apply-templates />
		</xsl:element>
	</xsl:template>	

	<!-- convert all file entries into an include statement for the content of that file -->
	<xsl:template match="dir:file">
		<!-- <xi:include href="block:/{$path}/{@name}.model" /> -->
		<i:include src="block:/resource/{../@name}/{@name}" />
	</xsl:template>

</xsl:stylesheet>