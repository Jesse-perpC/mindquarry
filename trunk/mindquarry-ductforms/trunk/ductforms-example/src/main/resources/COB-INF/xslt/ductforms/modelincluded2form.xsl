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
				<xsl:apply-templates select="df:instance/ductforms" />
				<xsl:apply-templates select="df:datatype" />
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

	<xsl:template match="df:datatype[fd:field]">
		<fd:field id="{@id}">
			<xsl:copy-of select="fd:field/*|fd:field/@*" />
		</fd:field>
	</xsl:template>

	<xsl:template match="df:datatype[fd:booleanfield]">
		<fd:booleanfield id="{@id}">
			<xsl:copy-of select="fd:booleanfield/*|fd:booleanfield/@*" />
		</fd:booleanfield>
	</xsl:template>

	<xsl:template name="extra">
		<fd:submit id="ductforms_save" validate="true">
			<fd:label>Save</fd:label>
			<fd:help>Save the data you entered</fd:help>
		</fd:submit>
	</xsl:template>

	<xsl:template match="df:instance/ductforms">
		<fd:multivaluefield id="ductforms">
			<fd:label>Fields</fd:label>
			<fd:datatype base="string" />
			<fd:selection-list>
				<xsl:apply-templates select="/df:model/df:datatype" mode="ductforms_add"/>
			</fd:selection-list>
			<fd:on-value-changed>
				<fd:javascript>upd(event);</fd:javascript>
			</fd:on-value-changed>
		</fd:multivaluefield>
	</xsl:template>

	<xsl:template match="df:datatype" mode="ductforms_add">
		<fd:item value="{@id}">
			<xsl:copy-of select=".//fd:label[1]" />
		</fd:item>
	</xsl:template>

</xsl:stylesheet>