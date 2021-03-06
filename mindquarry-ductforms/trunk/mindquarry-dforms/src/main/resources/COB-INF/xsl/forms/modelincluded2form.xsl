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
	xmlns:ft="http://apache.org/cocoon/forms/1.0#template">

	<xsl:param name="teamspace" select="''"/>
	<xsl:param name="username" select="''"/>
	
	<xsl:key name="datatypes" match="df:datatype" use="@id" />

	<xsl:template match="@*[contains(.,'}')]">
		<xsl:variable name="before"><xsl:value-of select="substring-before(.,'{')"/></xsl:variable>
		<xsl:variable name="after"><xsl:value-of select="substring-after(.,'}')"/></xsl:variable>
		<xsl:variable name="afterfirst"><xsl:value-of select="substring-after(.,'{')"/></xsl:variable>
		<xsl:variable name="beforesecond"><xsl:value-of select="substring-before($afterfirst,'}')"/></xsl:variable>
		<xsl:attribute name="{local-name(.)}">
			<xsl:value-of select="$before"/>
				<xsl:choose>
					<xsl:when test="$beforesecond='teamspace'"><xsl:value-of select="$teamspace"/></xsl:when>
					<xsl:when test="$beforesecond='username'"><xsl:value-of select="$username"/></xsl:when>
					<xsl:otherwise><xsl:value-of select="$beforesecond"/></xsl:otherwise>
				</xsl:choose>
			<xsl:value-of select="$after"/>
		</xsl:attribute>
	</xsl:template>
	
	<xsl:template match="df:text">
		<xsl:copy-of select="text()"/>
	</xsl:template>
	
	<xsl:template match="df:variable[normalize-space(.)='teamspace']">
		<xsl:value-of select="$teamspace"/>
	</xsl:template>
	
	<xsl:template match="df:variable[normalize-space(.)='username']">
		<xsl:value-of select="username"/>
	</xsl:template>

	<xsl:template match="/df:model">
		<fd:form id="ductform">
			<fd:widgets>
				<fd:multivaluefield id="ductforms">
					<fd:label>Fields</fd:label>
					<fd:datatype base="string" />
					<fd:selection-list>
						<xsl:apply-templates select="df:datatype" mode="addfields"/>
					</fd:selection-list>
					<fd:on-value-changed>
						<fd:javascript>fieldsChanged(event);</fd:javascript>
					</fd:on-value-changed>
				</fd:multivaluefield>

				<xsl:apply-templates select="df:datatype" />

				<fd:action id="ductforms_activate">
					<fd:on-action>
						<fd:javascript>activate(event);</fd:javascript>
					</fd:on-action>
				</fd:action>
				
				<fd:action id="ductforms_keepalive">
					<fd:on-action>
						<fd:javascript>keepAlive(event);</fd:javascript>
					</fd:on-action>
				</fd:action>
				
				<fd:action id="ductforms_editall">
					<fd:label>Edit All</fd:label>
					<fd:help>Edit all fields</fd:help>
					<fd:on-action>
						<fd:javascript>editAll(event);</fd:javascript>
					</fd:on-action>
				</fd:action>
				
				<fd:action id="ductforms_save">
					<fd:label>Save</fd:label>
					<fd:help>Save the data you entered</fd:help>
					<fd:on-action>
						<fd:javascript>save(event);</fd:javascript>
					</fd:on-action>
				</fd:action>

				<fd:action id="ductforms_cancel">
					<fd:label>Cancel</fd:label>
					<fd:help>Drop all changes</fd:help>
					<fd:on-action>
						<fd:javascript>cancel(event);</fd:javascript>
					</fd:on-action>
				</fd:action>
				
				<fd:action id="ductforms_delete">
					<fd:label>Delete</fd:label>
					<fd:help>Delete</fd:help>
					<fd:on-action>
						<fd:javascript>deleteIt(event);</fd:javascript>
					</fd:on-action>
				</fd:action>
			</fd:widgets>
		</fd:form>
	</xsl:template>

	<xsl:template match="node() | @*">
		<xsl:copy>
			<xsl:apply-templates select="node() | @*"/>
		</xsl:copy>
	</xsl:template>
	
	<!-- the three supported widget types (field, boolean, repeater) -->
	<xsl:template match="df:datatype[fd:field]">
		<fd:field id="{@id}">
			<xsl:apply-templates select="fd:field/*|fd:field/@*" />
		</fd:field>
	</xsl:template>

	<xsl:template match="df:datatype[fd:booleanfield]">
		<fd:booleanfield id="{@id}">
			<xsl:apply-templates select="fd:booleanfield/*|fd:booleanfield/@*" />
		</fd:booleanfield>
	</xsl:template>
	
	 
	<xsl:template match="df:datatype[fd:repeater]">
		<xsl:variable name="id" select="@id"/>
		<fd:repeater id="{@id}">
			<xsl:apply-templates select="fd:repeater/*|fd:repeater/@*" />
		</fd:repeater>
		<xsl:for-each select="fd:repeater-action">
			<xsl:copy>
				<xsl:attribute name="id"><xsl:value-of select="$id" /><xsl:value-of select="@id" /></xsl:attribute>
				<xsl:attribute name="repeater"><xsl:value-of select="$id" /></xsl:attribute>
				<xsl:apply-templates select="@*[not(local-name(.)='id' or local-name(.)='repeater')]|node()" />
			</xsl:copy>
		</xsl:for-each>
	</xsl:template>

	<xsl:template match="df:datatype" mode="addfields">
		<fd:item value="{@id}">
			<xsl:apply-templates select="(.//fd:label)[1]" />
		</fd:item>
	</xsl:template>

</xsl:stylesheet>