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

	<xsl:param name="viewDocumentLink" />
	
	<xsl:variable name="taskTitle">
		<xsl:choose>
			<xsl:when test="string-length(normalize-space(/html/head/title)) = 0">
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
			<link rel="alternate" type="application/pdf" title="PDF for print" />
			
			<link rel="breadcrumb" text="Tasks" href="."/>
			<link rel="breadcrumb" text="{/html/head/title}"/>
		</head>
	</xsl:template>

	<xsl:template match="title">
		<title>
			<xsl:value-of select="$taskTitle" />
		</title>
	</xsl:template>

	<xsl:template match="body">
		<body>
			<h1>
				<xsl:value-of select="$taskTitle" />
			</h1>

			<div class="nifty">
				<xsl:apply-templates select="form" />
			</div>
			
			<div class="nifty">
				<div class="btm-link">
				<a href="." id="back">Back to tasks list</a></div>				
			</div>
		</body>
	</xsl:template>

	<xsl:template match="form/@action">
		<xsl:attribute name="action">
			<xsl:value-of select="$viewDocumentLink" />
		</xsl:attribute>
	</xsl:template>

</xsl:stylesheet>
