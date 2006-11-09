<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	xmlns:df="http://mindquarry.com/ns/xml/ductforms"
	xmlns:fd="http://apache.org/cocoon/forms/1.0#definition"
	xmlns:ft="http://apache.org/cocoon/forms/1.0#template"
	xmlns:fi="http://apache.org/cocoon/forms/1.0#instance"
	xmlns:jx="http://apache.org/cocoon/templates/jx/1.0"
	xmlns:html="http://www.w3.org/1999/xhtml"
	exclude-result-prefixes="df fd ft fi jx html">

	<xsl:key name="usedfields" match="df:instance/*"
		use="local-name(.)" />
	<xsl:key name="datatypes" match="df:datatype" use="@id" />


	<xsl:template match="/df:model">
		<html:html>
			<html:head>
				<html:title>
					Editing Thing
				</html:title>
				<jx:import
					uri="resource://org/apache/cocoon/forms/generation/jx-macros.xml" />
			</html:head>
			<html:body>
				<!-- action must be modified in sub-blocks afterwards, because only
			 they know the correct URL -->
				<ft:form method="POST" action="">
					<ft:continuation-id>#{$cocoon/continuation/id}</ft:continuation-id>
				
					<xsl:apply-templates select="df:datatype" />


					<xsl:call-template name="extra">
						<xsl:with-param name="suffix">save</xsl:with-param>
					</xsl:call-template>
				</ft:form>
			</html:body>
		</html:html>
	</xsl:template>

	<xsl:template match="df:datatype">
		<html:div class="form_block" id="block_ductform_{@id}">
			<html:label for="ductform.{@id}">
				<xsl:apply-templates select="(fd:hint)[1]" />
				<ft:widget-label id="{@id}" />
			</html:label>
			<xsl:apply-templates select="*[namespace-uri(.)='http://apache.org/cocoon/forms/1.0#template']" />
		</html:div>
	</xsl:template>
	
	<xsl:template match="df:datatype/*[namespace-uri(.)='http://apache.org/cocoon/forms/1.0#template']">
		<xsl:copy>
			<xsl:attribute name="id">
				<xsl:value-of select="../@id" />
			</xsl:attribute>
			<xsl:copy-of select="node()|@*" />
		</xsl:copy>
	</xsl:template>

	<xsl:template match="fd:hint">
		<xsl:attribute name="title">
			<xsl:value-of select="." />
		</xsl:attribute>
	</xsl:template>

	<xsl:template name="extra">
		<xsl:param name="suffix" />
		<html:div class="form_block" id="block_ductform_ductforms">
			<html:label for="ductform.ductforms">
				<xsl:apply-templates select="(fd:hint)[1]" />
				<ft:widget-label id="ductforms" />
			</html:label>
			<ft:widget id="ductforms"/>
		</html:div>
		<html:div class="form_block" id="block_ductform_{$suffix}">
			<!-- <html:label for="ductforms_{$suffix}:input">
				<ft:widget-label id="ductforms_{$suffix}" />
			</html:label> -->
			<ft:widget id="ductforms_{$suffix}" />
		</html:div>
	</xsl:template>
</xsl:stylesheet>