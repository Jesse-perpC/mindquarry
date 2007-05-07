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
	
	<xsl:param name="username" />
	
	<xsl:param name="blockPath" />
	<xsl:param name="serverBaseUrl" />
	
	<xsl:variable name="pathToRoot"><xsl:value-of select="$serverBaseUrl" />/</xsl:variable>
	<xsl:variable name="pathToBlock"><xsl:value-of select="$serverBaseUrl" /><xsl:value-of select="$blockPath" />/</xsl:variable>

	<xsl:template match="/">
		<html>
			<head>
				<xsl:apply-templates select="." mode="title" />
				
				<link rel="stylesheet" href="{$pathToBlock}css/edit-members.css" type="text/css" />
				
				<xsl:call-template name="head-links" />
		         
				<xsl:if test="$username = 'admin'" >
					<link rel="section-global-action" type="new-user-action" linkrel="lightbox" href="{$pathToBlock}createUser/" title="New User"/>
					<link rel="section-global-action" type="new-team-action" linkrel="lightbox" href="{$pathToBlock}createTeamspace/" title="New Team"/>
				</xsl:if> 

				<script type="text/javascript"
				    src="{$pathToBlock}scripts/slider.js">//</script>
				<script type="text/javascript"
				    src="{$pathToBlock}scripts/teamspace.js">//</script>
				<script type="text/javascript">dojo.require("cocoon.forms");</script>			  
			</head>
			<body>
				<xsl:apply-templates />
			</body>
		</html>
	</xsl:template>
  
  
  <xsl:template match="user">
    <li style="background:url({$pathToRoot}teams/users/{normalize-space(id)}.png);background-repeat:no-repeat;background-position:1px 0px;">
      <xsl:value-of select="name" />
      <xsl:value-of select="surname" />
    </li>
  </xsl:template>

  <xsl:template match="user" mode="detail">
    <div class="member-details">
      <img src="{$pathToRoot}teams/users/{normalize-space(id)}.png"/>
      <h4>
        <xsl:value-of select="name"/>
        <xsl:value-of select="surname"/>
      </h4>
      <xsl:value-of select="skills"/>
    </div>
  </xsl:template>
</xsl:stylesheet>
