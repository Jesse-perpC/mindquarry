<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	xmlns:df="http://mindquarry.com/ns/xml/ductforms"
	xmlns:fd="http://apache.org/cocoon/forms/1.0#definition"
	xmlns:ft="http://apache.org/cocoon/forms/1.0#template"
	xmlns:fi="http://apache.org/cocoon/forms/1.0#instance">

	<xsl:param name="required"
		select="document('cocoon:/model.xml')/df:model/df:datatype[@required='true']" />

	<xsl:param name="existing" select="/ductform/*" />

	<xsl:template match="@*|node()">
		<xsl:copy>
			<xsl:apply-templates select="@*|node()" />
		</xsl:copy>
	</xsl:template>

	<xsl:template match="/ductform">
		<ductform>
			<!-- existing fields first, but strip out deleted fields -->
			<xsl:apply-templates select="*" mode="strip-deleted" />
			<!-- required fields next -->
			<xsl:for-each select="$required">
				<xsl:variable name="myname" select="@id" />
				<xsl:if test="not($existing[local-name(.)=$myname])">
					<xsl:element name="{$myname}">
						<xsl:attribute name="required">
							true
						</xsl:attribute>
					</xsl:element>
				</xsl:if>
			</xsl:for-each>
			<!-- other added fields last -->
			<xsl:apply-templates select="ductforms/*"
				mode="add-missing" />
			<xsl:apply-templates select="ductforms" />
			<xsl:if test="not(ductforms)">
				<ductforms/>
			</xsl:if>
		</ductform>
	</xsl:template>

	<xsl:template match="*" mode="strip-deleted">
		<xsl:variable name="myname" select="local-name(.)" />
		<!-- include all existing elements that are either listed in the
			list of elements to be kept or that are required -->
		<xsl:if
			test="/ductform/ductforms/item[normalize-space(.)=$myname] or $required/@id=$myname">
			<xsl:element name="{$myname}">
				<xsl:attribute name="existing">true</xsl:attribute>
				<xsl:apply-templates />
			</xsl:element>
		</xsl:if>
	</xsl:template>

	<xsl:template match="ductforms/*" mode="add-missing">
		<xsl:variable name="myname" select="normalize-space(.)" />
		<!-- filter out existing and required -->
		<xsl:if test="not(/ductform/*[local-name(.)=$myname]) and not($required/@id=$myname)">
			<xsl:element name="{$myname}">
				<xsl:attribute name="missing">true</xsl:attribute>
			</xsl:element>
		</xsl:if>
	</xsl:template>

	<xsl:template match="ductforms">
		<xsl:copy>
			<xsl:for-each select="$required">
				<item required="true">
					<xsl:value-of select="@id" />
				</item>
			</xsl:for-each>
			<xsl:apply-templates />
		</xsl:copy>
	</xsl:template>

	<xsl:template match="ductforms/item">
		<xsl:variable name="myname" select="normalize-space(.)" />
		<!-- do not include item that are already required -->
		<xsl:if test="not($required/@id=$myname)">
			<item duct="true">
				<xsl:apply-templates />
			</item>
		</xsl:if>
	</xsl:template>

</xsl:stylesheet>