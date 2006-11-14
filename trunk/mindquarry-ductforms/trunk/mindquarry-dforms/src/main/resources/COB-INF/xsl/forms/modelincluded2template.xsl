<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	xmlns:df="http://mindquarry.com/ns/xml/ductforms"
	xmlns:fd="http://apache.org/cocoon/forms/1.0#definition"
	xmlns:ft="http://apache.org/cocoon/forms/1.0#template"
	xmlns:fi="http://apache.org/cocoon/forms/1.0#instance"
	xmlns:jx="http://apache.org/cocoon/templates/jx/1.0"
	xmlns:xhtml="http://www.w3.org/1999/xhtml"
	exclude-result-prefixes="df fd ft fi jx xhtml">
	
	<xsl:param name="widgetArrangement" />
	
	<xsl:variable name="widgetArrangementOrDefault">
		<xsl:choose>
			<xsl:when test="string-length(normalize-space($widgetArrangement)) > 0">
				<xsl:value-of select="normalize-space($widgetArrangement)" />
			</xsl:when>
			<xsl:otherwise>default</xsl:otherwise>
		</xsl:choose>
	</xsl:variable>

	<xsl:template match="/df:model">
		<xhtml:html>
			<xhtml:head>
				<xhtml:title>DForm</xhtml:title>
				
				<jx:import
					uri="resource://org/apache/cocoon/forms/generation/jx-macros.xml" />	
			</xhtml:head>
			<xhtml:body>
				<!-- action must be modified in sub-blocks afterwards, because only
					 they know the correct URL -->
				<ft:form method="POST" action="">
					<ft:continuation-id>#{$cocoon/continuation/id}</ft:continuation-id>
				
					<!-- the switch button -->
					<xhtml:div class="form_block" id="block_ductform_switch">
						<ft:widget id="ductforms_switch" />
						<!-- the save button -->
							<ft:widget id="ductforms_save" />
					</xhtml:div>

					<!-- all fields/widgets grouped for nice layouting -->
					<fi:group>
						<fi:styling layout="{$widgetArrangementOrDefault}" />
						<fi:items>
							<xsl:apply-templates select="df:datatype" />
							
							<ft:widget id="ductforms">
								<fi:styling list-type="buttons" />
							</ft:widget>
						</fi:items>
					</fi:group>

					<!-- field chooser 
					<dl class="ductform" id="contentselector">
						<dt><label>Select Content</label></dt>
						<dd>
							<ft:widget id="ductforms">
								<fi:styling list-type="buttons" />
							</ft:widget>
						</dd>
					</dl>
					-->
				</ft:form>
			</xhtml:body>
		</xhtml:html>
	</xsl:template>

	<xsl:template match="df:datatype">
		<xsl:apply-templates select="*[namespace-uri(.)='http://apache.org/cocoon/forms/1.0#template']" />
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

</xsl:stylesheet>