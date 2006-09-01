<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	xmlns:df="http://mindquarry.com/ns/xml/ductforms"
	xmlns:fd="http://apache.org/cocoon/forms/1.0#definition"
	xmlns:ft="http://apache.org/cocoon/forms/1.0#template"
	xmlns:fi="http://apache.org/cocoon/forms/1.0#instance">

	<xsl:template match="/df:model">
		<ductform>
			<xsl:apply-templates select="df:datatype[@required='true']" />
			<ductforms_add/>
		</ductform>
	</xsl:template>
	
	<xsl:template match="df:datatype">
		<xsl:element name="{@id}"></xsl:element>
	</xsl:template>

</xsl:stylesheet>