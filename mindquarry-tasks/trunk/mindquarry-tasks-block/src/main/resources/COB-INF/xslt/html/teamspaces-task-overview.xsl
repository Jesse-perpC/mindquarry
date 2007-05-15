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
	xmlns:xlink="http://www.w3.org/1999/xlink">

	<xsl:import href="servlet:/xslt/contextpath.xsl" />
	<xsl:template match="/">
		<html>
			<head>
				<title>Manage Your Tasks</title>
				<link rel="stylesheet" href="{$pathToBlock}css/tasks.css" type="text/css" />
				<link rel="alternate" type="text/x-opml" title="reading list of tasks" />
				<link rel="breadcrumb" title="Tasks"/>
			</head>
			<body>
				<div class="list">
					<ul class="workspace-list">
						<xsl:apply-templates select="*/teamspace">
							<xsl:sort select="name" />
						</xsl:apply-templates>
					</ul>
				</div>
			</body>
		</html>
	</xsl:template>

	<xsl:template match="teamspace" >
		<li>
			<img class="icon">
				<xsl:attribute name="src">
					<xsl:value-of select="$pathToRoot"/>							
					<xsl:text>teams/</xsl:text>
					<xsl:value-of select="@xlink:href"/>
					<xsl:text>.png</xsl:text>
				</xsl:attribute>
			</img>
			<h2 class="name"><a href="{$pathToBlock}{@xlink:href}/"><xsl:value-of select="name" /> Tasks</a></h2>
			<p class="description"><xsl:value-of select="description" /></p>
			<div class="summary">
				Teamspace <b><xsl:value-of select="name" /></b> contains 
				<a href="{$pathToBlock}{@xlink:href}/"><xsl:value-of select="count(task)" /> Tasks</a>
				(<xsl:value-of select="count(task[status='new'])" /> New,
				<xsl:value-of select="count(task[status='running'])" /> Running,
				<xsl:value-of select="count(task[status='paused'])" /> Paused and
				<xsl:value-of select="count(task[status='done'])" /> Done)
			</div>
			<div class="summary">
				<ul>
					<xsl:call-template name="filters" />
					<xsl:if test="count(filter) = 0">
						<li>no filters</li>
					</xsl:if>
				</ul>
			</div>
				

		</li>
	</xsl:template>
	
	<xsl:template name="filters">
		<xsl:for-each select="filter[position()&lt;5]">
			<li>
				<a href="{../@xlink:href}/{@xlink:href}">
					<xsl:choose>
						<xsl:when test="string-length(title) > 0">
							<xsl:value-of select="title" />
						</xsl:when>
						<xsl:otherwise>
							&lt;no title&gt;
						</xsl:otherwise>
					</xsl:choose>
				</a>
			</li>
		</xsl:for-each>
	</xsl:template>
</xsl:stylesheet>
