<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	xmlns:df="http://mindquarry.com/ns/xml/ductforms"
	xmlns:fd="http://apache.org/cocoon/forms/1.0#definition"
	xmlns:ft="http://apache.org/cocoon/forms/1.0#template">

	<xsl:key name="datatypes" match="df:datatype" use="@id" />

	<xsl:template match="/df:model">
		<fd:form id="ductform">
			<fd:widgets>
				<fd:multivaluefield id="ductforms">
					<fd:label>Fields</fd:label>
					<fd:datatype base="string" />
					<fd:selection-list>
						<xsl:apply-templates select="df:datatype" mode="ductforms_add"/>
					</fd:selection-list>
					<fd:on-value-changed>
						<fd:javascript>upd(event);</fd:javascript>
					</fd:on-value-changed>
				</fd:multivaluefield>

				<xsl:apply-templates select="df:datatype" />

				<fd:submit id="ductforms_save" validate="true">
					<fd:label>Save</fd:label>
					<fd:help>Save the data you entered</fd:help>
				</fd:submit>
			</fd:widgets>
		</fd:form>
	</xsl:template>

	<!-- the three supported widget types (field, boolean, repeater) -->
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
	
	 
	<xsl:template match="df:datatype[fd:repeater]">
		<xsl:variable name="id" select="@id"/>
		<fd:repeater id="{@id}">
			<xsl:copy-of select="fd:repeater/*|fd:repeater/@*" />
		</fd:repeater>
		<xsl:for-each select="fd:repeater-action">
			<xsl:copy>
				<xsl:attribute name="id"><xsl:value-of select="$id" /><xsl:value-of select="@id" /></xsl:attribute>
				<xsl:attribute name="repeater"><xsl:value-of select="$id" /></xsl:attribute>
				<xsl:copy-of select="@*[not(local-name(.)='id' or local-name(.)='repeater')]|node()" />
			</xsl:copy>
		</xsl:for-each>
	</xsl:template>

	<xsl:template match="df:datatype" mode="ductforms_add">
		<fd:item value="{@id}">
			<xsl:copy-of select="(.//fd:label)[1]" />
		</fd:item>
	</xsl:template>

</xsl:stylesheet>