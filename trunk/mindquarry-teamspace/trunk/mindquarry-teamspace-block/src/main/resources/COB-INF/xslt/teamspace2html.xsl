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

	<xsl:import href="block:/xslt/contextpath.xsl"/>

	<xsl:param name="username" select="''"/>

	<xsl:template match="teamspace">
		<html>
			<head>
				<title>Manage Teamspace <xsl:value-of select="name"/></title>
				<link rel="stylesheet" href="{$pathToBlock}css/teamspace-details.css"
					type="text/css"/>
				<link rel="stylesheet" href="{$pathToBlock}css/edit-members.css"
					type="text/css"/>

				
				<script type="text/javascript">
					dojo.require("cocoon.forms");
				</script>
			</head>
			<body>
				<h1>Manage Teamspace <xsl:value-of select="name"/></h1>
				<div class="nifty">
					<div class="name">
						<img class="icon">
							<xsl:attribute name="src">
								<xsl:value-of select="$pathToBlock"/>
								<xsl:value-of select="normalize-space(id)"/>
								<xsl:text>.png</xsl:text>
							</xsl:attribute>
						</img>
						<h2 class="name">
							<xsl:value-of select="name"/>
						</h2>
						<span class="description">
							<xsl:value-of select="description"/>
						</span>
					</div>

					<div class="edit-buttons">
						<a href="editMembers/"
							class="edit_members_button" rel="lightbox"
							title="Add or remove team members">Team Members</a>

						<a href="edit/"
							class="edit_settings_button" rel="lightbox"
							title="Edit teamspace settings">Edit Settings</a>
					</div>

					<div class="details">
						<h3>Team Members</h3>
						<xsl:choose>
							<xsl:when test="users/user">
								<xsl:apply-templates select="users/user"
									mode="detail"/>
							</xsl:when>
							<xsl:otherwise>No team members assigned.</xsl:otherwise>
						</xsl:choose>
					</div>
				</div>
			</body>
		</html>
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
