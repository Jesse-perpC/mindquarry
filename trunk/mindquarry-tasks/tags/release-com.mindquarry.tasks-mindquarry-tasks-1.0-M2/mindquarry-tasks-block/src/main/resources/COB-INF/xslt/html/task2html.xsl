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

	<xsl:param name="document" />
	<xsl:variable name="dformParams" select="/html/head/dformParams"/>
	<xsl:template match="dformParams"/>
	
	<xsl:variable name="taskTitle">
		<xsl:choose>
			<xsl:when test="$dformParams/documentIsNew = 'true'">
				Create New Task				
			</xsl:when>
			<xsl:otherwise>
				Task: <xsl:value-of select="/html/head/title" />				
			</xsl:otherwise>
		</xsl:choose>
	</xsl:variable>

	<xsl:template match="@*|node()">
		<xsl:copy>
			<xsl:apply-templates select="@*|node()" />
		</xsl:copy>
	</xsl:template>

	<xsl:template match="head">
		<head>			
			<!-- copy existing link/script stuff -->
			<xsl:apply-templates />

			<link rel="stylesheet"
				href="{$pathToBlock}css/tasks.css" media="screen,projection"
				type="text/css" />
			<link rel="stylesheet"
				href="{$pathToBlock}css/task-edit.css" media="screen,projection"
				type="text/css" />
			
			<xsl:if test="$dformParams/documentIsNew = 'false'">
				<link rel="alternate" type="application/pdf" title="PDF for print" />
			
				<link rel="section-global-action" class="add-action" href="new" title="New Task"/>
				<link rel="section-global-action" class="new-filter-action" href="filters/new" title="New filter" />
			</xsl:if>
			
			<link rel="breadcrumb" title="Tasks" href="."/>
			<xsl:choose>
				<xsl:when test="$dformParams/documentIsNew = 'true'">
					<link rel="breadcrumb" title="New"/>
				</xsl:when>
				<xsl:otherwise>
					<link rel="breadcrumb" title="{/html/head/title}"/>
				</xsl:otherwise>
			</xsl:choose>
		</head>
	</xsl:template>

	<xsl:template match="title">
		<title>
			<xsl:value-of select="$taskTitle" />
		</title>
	</xsl:template>

	<xsl:template match="body">
		<body>
			<div class="content">
				<xsl:apply-templates select="form" />
			</div>
			
			<!-- do not show the history when creating a new task -->
			<xsl:if test="$dformParams/documentIsNew = 'false'">
				<div class="nifty">
				<h3 class="bottom-header">Activity Timeline</h3>
				
					<div
						id="my-timeline"
						style="height: 150px; border: 1px solid #aaa" 
						linkText="Show task"
						dataUrl="{$fullPath}/changes?http-accept-header=application/json"
						dojoType="mindquarry:Timeline">placeholder for timeline</div>
					
					<p class="hint">Click the blue dots to view the task back at a certain point in time.</p>
				</div>
			</xsl:if>
		</body>
	</xsl:template>

	<xsl:template match="form/@action">
		<xsl:attribute name="action">
			<xsl:value-of select="$document" />
		</xsl:attribute>
	</xsl:template>

</xsl:stylesheet>
