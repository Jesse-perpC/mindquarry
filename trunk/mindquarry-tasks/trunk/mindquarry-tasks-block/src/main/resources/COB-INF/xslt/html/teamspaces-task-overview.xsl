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

	<xsl:import href="block:/xslt/contextpath.xsl" />
		
	<xsl:template match="/tasks">
		<html>
			<head>
				<title>Tasks</title>
				<link rel="stylesheet" 
					href="{$pathToBlock}css/tasks.css" type="text/css" />
        <link rel="alternate" type="text/x-opml" title="reading list of tasks" />
      </head>
			<body>
				<h1>Manage Your Tasks</h1>
				
				<ul class="list">
					<xsl:apply-templates>
						<xsl:sort select="name" />
					</xsl:apply-templates>
				</ul>
			</body>
		</html>
	</xsl:template>
				 
	<xsl:template match="teamspace" >
		<li>
			<div class="nifty">
			<div  style="margin-left:5px">
				<div class="links">
					<ul>
	        			<li><a class="create_task_button" href="{@xlink:href}/new">Create task</a></li>
	        			<li><a class="create_filter_button" href="{@xlink:href}/filters/new">Create filter</a></li>
					</ul>
				</div>
			<div class="name">
					<img class="icon">
						<xsl:attribute name="src">
							<xsl:value-of select="$pathToRoot"/>							
							<xsl:text>teams/</xsl:text>
							<xsl:value-of select="@xlink:href"/>
							<xsl:text>.png</xsl:text>
						</xsl:attribute>
					</img>
					<h2 class="name"><a href="{$pathToBlock}{@xlink:href}/"><xsl:value-of select="name" /> Tasks</a></h2>
					<span class="description"><xsl:value-of select="description" /></span>
					
					<div class="summary">
						Teamspace <b><xsl:value-of select="name" /></b> contains 
						<a href="{$pathToBlock}{@xlink:href}/"><xsl:value-of select="count(task)" /> Tasks</a>
						(<xsl:value-of select="count(task[status='new'])" /> New,
						<xsl:value-of select="count(task[status='running'])" /> Running,
						<xsl:value-of select="count(task[status='paused'])" /> Paused and
						<xsl:value-of select="count(task[status='done'])" /> Done)
					</div>
				</div>			
			</div>
				<xsl:if test="count(filter) > 0">
					<div class="filters">
						<h3>Saved Filters</h3>
						<ul>
							<xsl:call-template name="filters" />
						</ul>
					</div>
				</xsl:if>
			</div>
		</li>
	</xsl:template>
	
	<xsl:template name="filters">
		<xsl:for-each select="filter">
			<li>
				<a href="{../@xlink:href}/{@xlink:href}"><xsl:value-of select="title"/></a>
			</li>
		</xsl:for-each>
	</xsl:template>
</xsl:stylesheet>
