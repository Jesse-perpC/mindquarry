<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	xmlns:df="http://mindquarry.com/ns/xml/ductforms"
	xmlns:fd="http://apache.org/cocoon/forms/1.0#definition"
	xmlns:ft="http://apache.org/cocoon/forms/1.0#template"
	xmlns:fi="http://apache.org/cocoon/forms/1.0#instance">

	<!--+ 
		| This Stylesheet receives the XML representation of
		| a form and cleans up all unneccessary fields, as
		| the current form implementation adds all possible
		| fields to the form, not only the really used
	 	+-->
	<xsl:template match="@*|node()">
		<xsl:copy>
			<xsl:apply-templates select="@*|node()" />
		</xsl:copy>
	</xsl:template>

	<xsl:template match="/ductform/*">
		<xsl:variable name="myname" select="local-name(.)" />
		<xsl:if test="/ductform/ductforms/item[normalize-space(.)=$myname]">
			<xsl:copy>
				<xsl:apply-templates select="@*|node()" />
			</xsl:copy>
		</xsl:if>
	</xsl:template>

	<xsl:template match="/ductform/ductforms" />
</xsl:stylesheet>