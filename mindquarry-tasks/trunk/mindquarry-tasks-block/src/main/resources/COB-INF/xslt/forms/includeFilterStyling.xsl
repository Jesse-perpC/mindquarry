<?xml version="1.0"?>
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
	xmlns:bu="http://apache.org/cocoon/browser-update/1.0"
	xmlns:xhtml="http://www.w3.org/1999/xhtml"
	xmlns:fd="http://apache.org/cocoon/forms/1.0#definition">
	
	<xsl:import href="servlet:/xslt/contextpath.xsl" />
	
	<xsl:param name="teamspaceID"/>
	
	<xsl:variable name="teamspaceUsers"
		select="document(concat('servlet:teams:/', $teamspaceID, '/members/asFormsSelectionlist'))"/>
	
	<xsl:template match="@*|node()">
		<xsl:copy>
			<xsl:apply-templates select="@*|node()" />
		</xsl:copy>
	</xsl:template>
	
	<!-- do not remove the prefix for the cocoon AJAX Browser Update namespace -->
	<xsl:template match="bu:*">
		<xsl:copy>
			<xsl:apply-templates select="@*|node()" />
		</xsl:copy>
	</xsl:template>
	
	<xsl:template match="*">
		<xsl:element name="{local-name(.)}">
			<xsl:apply-templates select="@*|node()" />
		</xsl:element>
	</xsl:template>
		
	<xsl:template match="xhtml:head|head">
		<head>
			<xsl:apply-templates />
			
			<link rel="stylesheet"
				href="{$pathToBlock}css/filter.css" type="text/css" />
		</head>
	</xsl:template>
	
	<xsl:template match="td[./span[starts-with(./@id,'results') and contains(./@id,'status')]]">
		<xsl:copy>
			<xsl:attribute name="sortValue">
				<xsl:choose>
					<xsl:when test="./span='new'">1</xsl:when>
					<xsl:when test="./span='running'">2</xsl:when>
					<xsl:when test="./span='paused'">3</xsl:when>
					<xsl:when test="./span='done'">4</xsl:when>
				</xsl:choose>
			</xsl:attribute>
			
			<img class="task_status">
				<xsl:attribute name="src">
					<xsl:choose>
						<xsl:when test="./span='new'">
							<xsl:value-of select="$pathToBlock" />images/status/new.png</xsl:when>
						<xsl:when test="./span='running'">
							<xsl:value-of select="$pathToBlock" />images/status/running.png</xsl:when>
						<xsl:when test="./span='paused'">
							<xsl:value-of select="$pathToBlock" />images/status/paused.png</xsl:when>
						<xsl:when test="./span='done'">
							<xsl:value-of select="$pathToBlock" />images/status/done.png</xsl:when>
					</xsl:choose>
				</xsl:attribute>
				<xsl:attribute name="alt"><xsl:value-of select="normalize-space(./span)"/></xsl:attribute>
			</img>
		</xsl:copy>
	</xsl:template>
		
	<xsl:template match="td[./a/span[starts-with(./@id,'results') and contains(./@id,'title')]]">
		<xsl:copy>
			<xsl:attribute name="sortValue"><xsl:copy-of select="./a/span"/></xsl:attribute>
			<xsl:copy-of select="./*"/>
		</xsl:copy>
	</xsl:template>

	<xsl:template match="td[./span[starts-with(./@id,'results') and contains(./@id,'date')]]">
		<xsl:copy>
			<xsl:value-of select="./span"/>
		</xsl:copy>
	</xsl:template>
	
	<xsl:template match="td[./span[starts-with(./@id,'results') and contains(./@id,'people')]]">
		<xsl:copy>
			<xsl:attribute name="sortValue"><xsl:value-of select="./span"/></xsl:attribute>
			<ul class="members">
				<xsl:call-template name="splitNamesIntoImages">
					<xsl:with-param name="allPersonsString" select="normalize-space(./span)"/>
				</xsl:call-template>
			</ul>
		</xsl:copy>
	</xsl:template>
	
	<!-- makes this
		
			<li><img src='...admin'/></li>
			<li><img src='...frank'/></li>
			<li><img src='...peter'/></li>
			<li><img src='...lisa'/></li>
			
		 out of
		 
		 	'admin|frank|peter|lisa|'
	-->
	<xsl:template name="splitNamesIntoImages">
		<!-- example: 'admin|frank|peter|lisa|' -->
		<xsl:param name="allPersonsString"/>
		<xsl:choose>
			<!-- stops when the last '|' is reached -->
			<xsl:when test="string-length($allPersonsString) > 1">
				<xsl:variable name="person" select="substring-before($allPersonsString, '|')"/>
				<xsl:variable name="personFullName" select="$teamspaceUsers/fd:selection-list/fd:item[@value=$person]/fd:label"/>
				<li title="{$personFullName}">
					<img src="{$pathToRoot}teams/users/48/{$person}.png" />
				</li>
				<xsl:call-template name="splitNamesIntoImages">
					<xsl:with-param name="allPersonsString" select="substring-after($allPersonsString, '|')"/>
				</xsl:call-template>
			</xsl:when>
		</xsl:choose>
	</xsl:template>
	
</xsl:stylesheet>
