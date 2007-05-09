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
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	 
	<xsl:import href="teamspace2htmlutils.xsl"/>
	
	<!-- all the templates below are called by teamspace2htmlutils.xsl which
		 already provides the general structure for the raw html -->

	<xsl:template name="content-actions" />

	<xsl:template name="head-links">
		<link rel="breadcrumb" title="Teams"/>
	</xsl:template>
  
	<xsl:template match="/" mode="title">
		<title>Manage Your Teams</title>
	</xsl:template>
  
	<xsl:template match="teamspaces|no-data-available">
		<div class="list">
			
			<ul class="teamspace-list">
				<xsl:apply-templates select="teamspace">
					<xsl:sort select="name" />
				</xsl:apply-templates>
			</ul>
		</div>
	</xsl:template>
    
	<xsl:template match="teamspace">
		<li>
			<img class="icon" src="{$pathToBlock}{normalize-space(id)}.png"/>

			<h2 class="name">
				<a href="team/{normalize-space(id)}/">
					<xsl:value-of select="name" />
				</a>
			</h2>
			
			<p>
				<xsl:value-of select="description" />
			</p>
	<!--
			<div>
				<ul class="members">
					<xsl:if test="users/user">
					  <xsl:apply-templates select="users" />
					</xsl:if>
				</ul>
			</div>
	-->
		</li>
	</xsl:template>
  
</xsl:stylesheet>
