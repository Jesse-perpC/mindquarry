<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	xmlns:df="http://mindquarry.com/ns/xml/ductforms"
	xmlns:fd="http://apache.org/cocoon/forms/1.0#definition"
	xmlns:ft="http://apache.org/cocoon/forms/1.0#template"
	xmlns:fi="http://apache.org/cocoon/forms/1.0#instance"
	xmlns:jx="http://apache.org/cocoon/templates/jx/1.0"
	xmlns:html="http://www.w3.org/1999/xhtml">

	<xsl:key name="usedfields" match="df:instance/*"
		use="local-name(.)" />
	<xsl:key name="datatypes" match="df:datatype" use="@id" />


	<xsl:template match="/df:model">
		<html:html>
			<html:head>
				<html:title>
					<xsl:value-of select="df:instance/title" />
				</html:title>
				<jx:import
					uri="resource://org/apache/cocoon/forms/generation/jx-macros.xml" />
			</html:head>
			<html:body>
				<xsl:apply-templates select="df:instance" />
			</html:body>
		</html:html>
	</xsl:template>

	<xsl:template match="df:instance">
		<ft:form method="POST">
			<xsl:attribute name="action">
				<xsl:text>#{$cocoon/continuation/id}.continue</xsl:text>
			</xsl:attribute>
			
			<xsl:apply-templates select="*" />

			<xsl:call-template name="extra">
				<xsl:with-param name="suffix">save</xsl:with-param>
			</xsl:call-template>
		</ft:form>
	</xsl:template>

	<xsl:template match="*">
		<html:div class="form_block" style="border:1px solid lightgreen;margin:2px;">
			<html:label for="ductform.{local-name(.)}">
				<xsl:apply-templates select="key('datatypes',local-name(.))//fd:hint[1]" />
				<ft:widget-label id="{local-name(.)}" />
			</html:label>
			
			<ft:widget id="{local-name(.)}">
				<xsl:copy-of
					select="key('datatypes',local-name(.))/ft:widget/@*" />
				<xsl:copy-of
					select="key('datatypes',local-name(.))/ft:widget/node()" />
					
				<xsl:apply-templates select="." mode="styling"/>
			</ft:widget>
		</html:div>
	</xsl:template>
	
	<xsl:template match="*" mode="styling"/>
	
	<xsl:template match="ductforms_add|ductforms_delete" mode="styling">
 		<fi:styling submit-on-change="true"/>
	</xsl:template>

	<xsl:template match="fd:hint">
		<xsl:attribute name="title">
			<xsl:value-of select="." />
		</xsl:attribute>
	</xsl:template>	

	<xsl:template name="extra">
		<xsl:param name="suffix" />
		<html:div class="form_block">
			<html:label for="ductforms_{$suffix}:input">
				<ft:widget-label id="ductforms_{$suffix}" />
			</html:label>
			<ft:widget id="ductforms_{$suffix}" />
		</html:div>
	</xsl:template>
</xsl:stylesheet>