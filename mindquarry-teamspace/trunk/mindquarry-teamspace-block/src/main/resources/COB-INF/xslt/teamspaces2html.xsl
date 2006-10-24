<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

	<xsl:import
		href="block:/xslt/contextpath.xsl" />

	<xsl:template match="/teamspaces">
		<html>
			<head>
				<title>Teamspaces</title>
				<link rel="stylesheet"
					href="{$pathToBlock}css/teamspace.css" type="text/css" />
				<link rel="stylesheet" href="css/screen.css" 
					media="screen,projection" type="text/css" />
				<link rel="stylesheet" 
					href="{$pathToBlock}css/edit-members.css" type="text/css" />
				<link rel="stylesheet" 
					href="{$pathToBlock}css/create-teamspace.css" type="text/css" />
				<link rel="stylesheet" 
					href="{$pathToBlock}css/lightbox.css" type="text/css" />
				<link rel="stylesheet" 
					href="{$pathToBlock}css/nifty.css" type="text/css" />
				
				<script type="text/javascript" 
					src="{$pathToBlock}scripts/lightbox.js" >//</script>
				<script type="text/javascript" 
					src="{$pathToBlock}scripts/slider.js" >//</script>
				<script type="text/javascript" 
					src="{$pathToBlock}scripts/teamspace.js" >//</script>
				<script type="text/javascript">
					dojo.require("cocoon.forms");
				</script>
			</head>
			<body>
				<h1>Manage Your Teams</h1>
				
				<a class="create_teamspace_button" href="createTeamspace/" rel="lightbox">
					New Team</a>
				<a class="create_user_button" href="createUser/" rel="lightbox">
					New User</a>
				
				<ul class="teamspace-list">
					<xsl:apply-templates>
						<xsl:sort select="name" />
					</xsl:apply-templates>
				</ul>
			</body>
		</html>
	</xsl:template>

	<xsl:template match="teamspace">
		<li>
			<div class="nifty">
			
			<a class="details-collapsed" href="#" title="Click here to show project details">more</a>
			
			<div style="margin-left:24px">
			
			<ul class="members">
				<xsl:if test="users/user">
					<xsl:apply-templates select="users" />
				</xsl:if>
			</ul>
			
			<div class="edit-buttons">
				<a href="{normalize-space(id)}/editMembers/" class="edit_members_button" rel="lightbox">
						Edit Members
				</a>
				
				<a href="#" class="edit_subprojects_button">
						Edit Related Teams
				</a>
			</div>
			
			<div class="name">
				<img class="icon" src="{$pathToBlock}teams/goshaky.png" />
				<h2 class="name"><xsl:value-of select="name" /></h2>
				<span class="description"><xsl:value-of select="description" /></span>
			</div>
			
			<ul class="tags">
				<li><a href="#">docbook</a></li>
				<li><a href="#">techdoc</a></li>
				<li><a href="#">xml</a></li>
				<li><a href="#">source</a></li>
				<li><a href="#">open</a></li>
			</ul>
		
			<div class="details" style="display:none;">
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
			
			<div class="details" style="display:none;">
				<h3>Related Teams</h3>
				not implemented yet.
			</div>
			</div>
			</div>
		</li>
	</xsl:template>
	
	<xsl:template match="user" mode="detail">
		<div class="member-details">
			<img src="{$pathToRoot}teamspace/users/{normalize-space(id)}.png" />
			<h4>
				<xsl:value-of select="name" />
				<xsl:value-of select="surname" />
			</h4>
			<xsl:value-of select="skills" />
			<div class="role">Undefined Role <a href="#">more...</a></div>
			
		</div>
	</xsl:template>

	<xsl:template match="user">
		<li style="background:url({$pathToRoot}teamspace/users/{normalize-space(id)}.png);background-repeat:no-repeat;background-position:1px 0px;">
			<xsl:value-of select="name" />
			<xsl:value-of select="surname" />
		</li>
	</xsl:template>
</xsl:stylesheet>
