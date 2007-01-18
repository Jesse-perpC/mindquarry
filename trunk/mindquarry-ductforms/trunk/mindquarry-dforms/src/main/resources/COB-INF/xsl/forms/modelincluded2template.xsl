<?xml version="1.0" encoding="UTF-8"?>

<!--
	Copyright (C) 2006-2007 Mindquarry GmbH, All Rights Reserved
	
	The contents of this file are subject to the Mozilla Public License
	Version 1.1 (the "License"); you may not use this file except in
	compliance with the License. You may obtain a copy of the License at
	http://www.mozilla.org/MPL/
	
	Software distributed under the License is distributed on an "AS IS"
	basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
	License for the specific language governing rights and limitations
	under the License.
-->

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
				<!-- empty title needed for 'new document' case -->
				<xhtml:title></xhtml:title>
				
				<jx:import
					uri="resource://org/apache/cocoon/forms/generation/jx-macros.xml" />	
			</xhtml:head>
			<xhtml:body>
				<!-- action must be modified in sub-blocks afterwards, because only
					 they know the correct URL -->
				<!-- enctype="application/x-www-form-urlencoded;charset=ISO-8859-1" accept-charset="ISO-8859-1"  -->
				<ft:form method="POST" action="" ajax="true" >
					<ft:continuation-id>#{$cocoon/continuation/id}</ft:continuation-id>

					<!-- hide the activate action (only used for communication) -->					
					<ft:widget id="ductforms_activate">
						<fi:styling type="hidden" />
					</ft:widget>
					
					<!-- the buttons -->
					<ft:widget id="ductforms_editall">
						<fi:styling class="ductform_button editall_button" dojoType="AutoActiveButton"/>
					</ft:widget>
					<ft:widget id="ductforms_save">
						<fi:styling class="ductform_button save_button" dojoType="AutoActiveButton"/>						
					</ft:widget>
					<ft:widget id="ductforms_cancel">
						<fi:styling class="ductform_button cancel_button" dojoType="AutoActiveButton"/>						
					</ft:widget>
					
					<!-- all fields/widgets grouped for nice layouting -->
					<fi:group>
						<fi:styling layout="{$widgetArrangementOrDefault}" />
						<fi:items>
							<!-- the fields -->
							<xsl:apply-templates select="df:datatype" />
							
							<!-- the field chooser -->
							<ft:widget id="ductforms">
								<fi:styling list-type="buttons" />
							</ft:widget>
						</fi:items>
					</fi:group>

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