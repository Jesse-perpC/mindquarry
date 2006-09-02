<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	xmlns:df="http://mindquarry.com/ns/xml/ductforms"
	xmlns:fd="http://apache.org/cocoon/forms/1.0#definition"
	xmlns:ft="http://apache.org/cocoon/forms/1.0#template"
	xmlns:fi="http://apache.org/cocoon/forms/1.0#instance">

	<xsl:template match="@*|node()">
		<xsl:copy>
			<xsl:apply-templates select="@*|node()" />
		</xsl:copy>
	</xsl:template>

	<xsl:template match="*[local-name(.)=normalize-space(/*/ductforms_delete)]" />

	<xsl:template match="ductforms_add">
		<xsl:if test="normalize-space(.)!='' and normalize-space(.)!='ductforms_none'">
			<xsl:element name="{normalize-space(.)}">
			</xsl:element>
		</xsl:if>
		<ductforms_add/>
	</xsl:template>
	
	<xsl:template match="/*">
		<ductform>
			<xsl:apply-templates />
			<xsl:if test="not(ductforms_add)">
				<ductforms_add />
			</xsl:if>
			<xsl:if test="not(ductforms_delete)">
				<ductforms_delete />
			</xsl:if>
		</ductform>
	</xsl:template>

</xsl:stylesheet>