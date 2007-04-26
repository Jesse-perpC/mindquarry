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
	
	<xsl:template name="content-actions" />
  
	<xsl:template match="/" mode="title">
		<title>Teams</title>
	</xsl:template>
  
	<xsl:template match="/" mode="heading">
		<h1>Manage Your Teams</h1>
    
		<xsl:if test="$username = 'admin'" >
		     <a class="button22 button create_teamspace_button" href="createTeamspace/" rel="lightbox">New Team</a>
		     <xsl:call-template name="create_user_button" />
		</xsl:if>
		
  	</xsl:template>
  
	<xsl:template match="teamspaces">
		<ul class="teamspace-list">
			<xsl:apply-templates select="teamspace">
				<xsl:sort select="name" />
			</xsl:apply-templates>
		</ul>
	</xsl:template>
    
	<xsl:template match="teamspace">
	<li>
		<xsl:apply-imports />
	</li>
	</xsl:template>
  
	<xsl:template match="teamspace/name">
		<h2 class="name">
			<a href="team/{normalize-space(../id)}/">
				<xsl:value-of select="." />
			</a>
		</h2>
	</xsl:template>
  
	<xsl:template name="create_user_button">
	  <a class="create_user_button" href="createUser/" rel="lightbox">New User</a>
	</xsl:template>
  
	<xsl:template name="create_edit_buttons">
		<a href="{normalize-space(id)}/editMembers/" class="edit_members_button" 
			rel="lightbox" title="Add or remove team members">
			Team Members
		</a>
		
		<a href="{normalize-space(id)}/edit/" class="edit_settings_button" 
			rel="lightbox">
			Edit Settings
		</a>
	</xsl:template>
</xsl:stylesheet>
