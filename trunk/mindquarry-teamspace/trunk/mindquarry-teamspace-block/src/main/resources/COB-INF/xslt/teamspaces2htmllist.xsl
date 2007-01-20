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
	
	<xsl:template match="/teamspaces">
		<div id="user-and-teamspace-info">
			<xsl:apply-templates select="user" />
			<ul class="teamspacelist">
				<xsl:apply-templates select="teamspace"/>
			</ul>
		</div>
	</xsl:template>
	
	<xsl:template match="user">
		<p class="userinfo">
			<span class="username"><xsl:value-of select="@id"/></span>
			<span>Logged in as</span>
			<span class="userid"><xsl:apply-templates/></span>
		</p>
	</xsl:template>
	
	<xsl:template match="teamspace">
		<li class="team"><a href="/{normalize-space(id)}" title="{normalize-space(id)}"><xsl:apply-templates select="name" /></a></li>
	</xsl:template>
</xsl:stylesheet>
