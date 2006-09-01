<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	xmlns:df="http://mindquarry.com/ns/xml/ductforms"
	xmlns:fd="http://apache.org/cocoon/forms/1.0#definition"
	xmlns:ft="http://apache.org/cocoon/forms/1.0#template"
	xmlns:fi="http://apache.org/cocoon/forms/1.0#instance">

	<xsl:key name="datatypes" match="df:datatype" use="@id" />
	<xsl:key name="usedfields" match="df:instance/*"
		use="local-name(.)" />

	<xsl:template match="/df:model">
		<fd:form id="ductform">
			<fd:widgets>
				<xsl:apply-templates select="df:instance" />
				<xsl:call-template name="addfield" />
				<xsl:call-template name="extra" />
			</fd:widgets>
		</fd:form>
	</xsl:template>

	<xsl:template match="df:instance">
		<xsl:apply-templates select="*" />
	</xsl:template>

	<xsl:template match="df:instance/*">
		<xsl:apply-templates
			select="key('datatypes', normalize-space(local-name(.)))" />
	</xsl:template>

	<xsl:template match="df:instance/ductforms_add">
		<xsl:apply-templates
			select="key('datatypes', normalize-space(.))" />
	</xsl:template>


	<xsl:template match="df:datatype">
		<fd:field id="{@id}">
			<xsl:copy-of select="fd:field/*|fd:field/@*" />
		</fd:field>
	</xsl:template>

	<xsl:template name="extra">
		<fd:submit id="ductforms_save" validate="true">
			<fd:label>Save</fd:label>
			<fd:help>Save the data you entered</fd:help>
		</fd:submit>
	</xsl:template>

	<xsl:template name="addfield">
		<fd:field id="ductforms_add">
			<fd:datatype base="string" />
			<fd:selection-list>
				<fd:item value="ductforms_none">
					<fd:label>None</fd:label>
				</fd:item>
				<xsl:apply-templates select="df:datatype"
					mode="ductforms_add" />
			</fd:selection-list>
			<fd:label>Additional information</fd:label>
		</fd:field>
	</xsl:template>

	<xsl:template match="df:datatype" mode="ductforms_add">
		<xsl:if test="not(key('usedfields',@id) or normalize-space(/df:model/df:instance/ductforms_add)=@id)">
			<fd:item value="{@id}">
				<xsl:copy-of select="./fd:field/fd:label" />
			</fd:item>
		</xsl:if>
	</xsl:template>

</xsl:stylesheet>