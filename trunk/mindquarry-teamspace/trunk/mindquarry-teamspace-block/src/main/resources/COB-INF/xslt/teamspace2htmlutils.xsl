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
    xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	
  <xsl:import href="block:/xslt/contextpath.xsl" />
	
  <xsl:param name="username" select="''" />

  <xsl:template match="/">
    <html>
      <head>
        <xsl:apply-templates mode="title" />
        <link rel="stylesheet"
            href="{$pathToBlock}css/teamspace.css" type="text/css" />
        <link rel="stylesheet"
            href="{$pathToBlock}css/edit-members.css" type="text/css" />
        <link rel="stylesheet"
            href="{$pathToBlock}css/create-teamspace.css" type="text/css" />
				
        <script type="text/javascript"
            src="{$pathToBlock}scripts/slider.js">//</script>
        <script type="text/javascript"
            src="{$pathToBlock}scripts/teamspace.js">//</script>
        <script type="text/javascript">
					dojo.require("cocoon.forms");
				</script>
      </head>
      <body>
        <xsl:apply-templates mode="heading" />
        <xsl:apply-templates />
      </body>
    </html>
  </xsl:template>
  

  <xsl:template name="create_user_button">
    <a class="create_user_button" href="createUser/" rel="lightbox">
        New User</a>
  </xsl:template>

  <xsl:template match="id">
    <img class="icon">
      <xsl:attribute name="src">
        <xsl:value-of select="$pathToBlock"/>
        <xsl:value-of select="normalize-space(.)"/>
        <xsl:text>.png</xsl:text>
      </xsl:attribute>
    </img>
  </xsl:template>
  
  <xsl:template match="name">
    <h2 class="name">
      <a href="team/{normalize-space(../id)}/">
        <xsl:apply-templates />
      </a>
    </h2>
  </xsl:template>
  
  <xsl:template match="description">
    <span class="description">
      <xsl:apply-templates />
    </span>
  </xsl:template>
  
  <xsl:template match="teamspace">
    <div class="nifty">
      <xsl:if test="parent::teamspaces">
        <a class="details-collapsed" href="#" title="Click here to show project details">more</a>
      </xsl:if>
      <div>
        <xsl:if test="parent::teamspaces">
          <xsl:attribute name="style">margin-left:24px;</xsl:attribute>
        </xsl:if>
        
        <div class="name">
          <xsl:apply-templates select="id" />
          <xsl:apply-templates select="name" />
          <xsl:apply-templates select="description" />
        </div>

        <xsl:if test="parent::teamspaces">
          <ul class="members">
            <xsl:if test="users/user">
              <xsl:apply-templates select="users" />
            </xsl:if>
          </ul>
        </xsl:if>
				
					
        <div class="edit-buttons">
          <a href="{normalize-space(id)}/editMembers/" class="edit_members_button" rel="lightbox" title="Add or remove team members">
								Team Members
						</a>
						
          <a href="{normalize-space(id)}/edit/" class="edit_settings_button" rel="lightbox">
								Edit Settings
						</a>
						
						<!--a href="#" class="edit_subprojects_button">
								Edit Related Teams
						</a-->
        </div>

					<!--ul class="tags">
						<li><a href="#">docbook</a></li>
						<li><a href="#">techdoc</a></li>
						<li><a href="#">xml</a></li>
						<li><a href="#">source</a></li>
						<li><a href="#">open</a></li>
					</ul-->
					
        <div class="details">
          <xsl:if test="parent::teamspaces">
            <xsl:attribute name="style">display:none;</xsl:attribute>
          </xsl:if>
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
          <xsl:if test="parent::teamspaces">
            <xsl:attribute name="style">display:none;</xsl:attribute>
          </xsl:if>
          <h3>Tools for your Teamwork</h3>
          <div class="member-details">
            <img src="{$pathToRoot}resources/tango-icons/48/apps/mindquarry-documents.png" class="teamblock"/>
            <h4>
              Team Documents
            </h4>
            Share documents with your team members. Browse <a href="{$pathToRoot}/workspace/browser/{normalize-space(id)}/">shared files</a> 
            or view <a href="{$pathToRoot}/workspace/changes/{normalize-space(id)}">recent changes</a> to stay up to date.
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
					
					<!--div class="details" style="display:none;">
						<h3>Related Teams</h3>
						not implemented yet.
					</div-->
      </div>
    </div>
  </xsl:template>
	
  <xsl:template match="user">
    <li style="background:url({$pathToRoot}teamspace/users/{normalize-space(id)}.png);background-repeat:no-repeat;background-position:1px 0px;">
      <xsl:value-of select="name" />
      <xsl:value-of select="surname" />
    </li>
  </xsl:template>

  <xsl:template match="user" mode="detail">
    <div class="member-details">
      <img src="{$pathToRoot}teamspace/users/{normalize-space(id)}.png"/>
      <h4>
        <xsl:value-of select="name"/>
        <xsl:value-of select="surname"/>
      </h4>
      <xsl:value-of select="skills"/>
    </div>
  </xsl:template>

</xsl:stylesheet>
