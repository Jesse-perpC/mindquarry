<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	xmlns:df="http://mindquarry.com/ns/xml/ductforms"
	xmlns:dir="http://apache.org/cocoon/directory/2.0"
	xmlns:i="http://apache.org/cocoon/include/1.0">
	
	<!-- create a root element of type df:model -->
	<xsl:template match="/">
		<xsl:element name="df:model" namespace="http://mindquarry.com/ns/xml/ductforms">
			<xsl:apply-templates />
		</xsl:element>
	</xsl:template>

	<!-- copy all include statements -->	
	<xsl:template match="i:include">
		<xsl:copy-of select="." />
	</xsl:template>

</xsl:stylesheet>