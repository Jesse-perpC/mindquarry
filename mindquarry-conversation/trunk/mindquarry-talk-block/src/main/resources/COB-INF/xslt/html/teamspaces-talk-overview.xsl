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
		
	<xsl:template match="/">
		<html>
			<head>
				<title>Talk</title>
				<link rel="stylesheet" 
					href="{$pathToBlock}css/wiki.css" type="text/css" />
			</head>
			<body>
				<h1>Talk to your teams</h1>
				
				<ul class="wiki-list">
					<xsl:apply-templates select="*/teamspace">
						<xsl:sort select="name" />
					</xsl:apply-templates>
				</ul>
			</body>
		</html>
	</xsl:template>

	<xsl:template match="user"/>
		
	<xsl:template match="teamspace">
		<li>
			<div class="nifty">
				<div class="name">
					<img class="icon">
						<xsl:attribute name="src">
							<xsl:value-of select="$pathToRoot"/>							
							<xsl:text>teams/</xsl:text>
							<xsl:value-of select="id"/>
							<xsl:text>.png</xsl:text>
						</xsl:attribute>
					</img>
					<h2 class="name"><a href="{$pathToBlock}{normalize-space(id)}/"><xsl:value-of select="name" /> Talks</a></h2>
					<span class="description"><xsl:value-of select="description" /></span>
				</div>
				
				<div class="links">
					<ul>
						<li><a class="create_talk_button" href="{normalize-space(id)}/new">Start conversation</a></li>
					</ul>
				</div>

				<div class="summary">
					<a href="{$pathToBlock}{normalize-space(id)}/">List</a> of all <xsl:value-of select="name" /> conversations
				</div>
			</div>
		</li>
	</xsl:template>
</xsl:stylesheet>
