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
   
  <xsl:template match="teamspace" mode="title">
    <title><xsl:value-of select="name"/></title>
  </xsl:template>
  
  <xsl:template name="head-links">
	<link rel="breadcrumb" text="Details"/>
		
	<link rel="action" linkrel="lightbox" type="changemembers" href="../../{/teamspace/id}/editMembers/" text="Team Members"/>
	<link rel="action" linkrel="lightbox" type="editteamsettings" href="../../{/teamspace/id}/editUser/" text="Edit Settings"/>
  </xsl:template>
    
  <xsl:template match="teamspace">
  	<div class="content">
		<h2 class="name">
			<xsl:apply-templates select="name" />
		</h2>
		
		<img class="icon" src="{$pathToBlock}{normalize-space(id)}.png"/>
		
		<span class="description">
		  <xsl:apply-templates select="description"/>
		</span>
		
        <div class="details">
          <h3>Team Members</h3>
          <xsl:choose>
            <xsl:when test="users/user">
              <xsl:apply-templates select="users/user" mode="detail" />
            </xsl:when>
            <xsl:otherwise>
              No team members assigned.
			</xsl:otherwise>
          </xsl:choose>
        </div>
        <div class="details">
          <h3>Tools for your Teamwork</h3>
          <div class="member-details">
            <img src="{$pathToRoot}resources/tango-icons/48/apps/mindquarry-documents.png" class="teamblock"/>
            <h4>
              Team Documents
            </h4>
            Share documents with your team members. Browse <a href="{$pathToRoot}files/browser/{normalize-space(id)}/">shared files</a> 
            or view <a href="{$pathToRoot}files/changes/{normalize-space(id)}/">recent changes</a> to stay up to date.
          </div>
          <div class="member-details">
            <img src="{$pathToRoot}resources/tango-icons/48/apps/mindquarry-wiki.png" class="teamblock"/>
            <h4>
              Team Wiki
            </h4>
            Share ideas, thoughts and know-how with your team members. Visit <a href="{$pathToRoot}/wiki/{normalize-space(id)}/">the Wiki</a> 
            or create <a href="{$pathToRoot}/wiki/{normalize-space(id)}/new">a new page</a>.
          </div>
          <div class="member-details">
            <img src="{$pathToRoot}resources/tango-icons/48/apps/mindquarry-tasks.png" class="teamblock"/>
            <h4>
              Team Tasks
            </h4>
            Collaboratively manage your tasks and assignments. Take the <a href="{$pathToRoot}/tasks/{normalize-space(id)}/">task overview</a> 
            or create <a href="{$pathToRoot}/tasks/{normalize-space(id)}/new">a new task</a>.
          </div>
        </div>
    </div>
  </xsl:template>
  
</xsl:stylesheet>
