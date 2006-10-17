<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

	<xsl:import
		href="resource://com/mindquarry/webapp/xsl/contextpath.xsl" />

	<xsl:template match="/teamspaces">
		<html>
			<head>
				<title>Teamspaces</title>
				<link rel="stylesheet"
					href="resources/css/teamspace.css" type="text/css" />
				<link rel="stylesheet" href="resources/css/screen.css" 
					media="screen,projection" type="text/css" />
					
				<script type="text/javascript" 
					src="resources/scripts/prototype.js">//</script>
				<script type="text/javascript" 
					src="resources/scripts/lightbox.js" >//</script>
				<script type="text/javascript" 
					src="resources/scripts/slider.js" >//</script>
				<script type="text/javascript" 
					src="resources/scripts/teamspace.js" >//</script>
			</head>
			<body>
				<h1>Manage Your Teams</h1>
				
				<a class="create_teamspace_button" href="createTeamspace/" rel="lightbox">
					New Team</a>
				
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
				
				<li style="background:url(users/arnaud.png);background-repeat:no-repeat;background-position:1px 0px;">dummy 1</li>
				<li style="background:url(users/arnaud.png);background-repeat:no-repeat;background-position:1px 0px;">dummy 2</li>
				<li style="background:url(users/arnaud.png);background-repeat:no-repeat;background-position:1px 0px;">dummy 3</li>
				<li style="background:url(users/arnaud.png);background-repeat:no-repeat;background-position:1px 0px;">dummy 4</li>
				<li style="background:url(users/arnaud.png);background-repeat:no-repeat;background-position:1px 0px;">dummy 5</li>
				<li style="background:url(users/arnaud.png);background-repeat:no-repeat;background-position:1px 0px;">dummy 6</li>
				<li style="background:url(users/arnaud.png);background-repeat:no-repeat;background-position:1px 0px;">dummy 7</li>
			</ul>
			<div class="name">
				<img class="icon" src="teams/goshaky.png" />
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
		
			<a href="{normalize-space(id)}/editMembers/" class="add_members_button" rel="lightbox">
					Edit Members
			</a>
			
			<a href="#" class="edit_subprojects_button">
					Edit Related Teams
			</a>
		
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
		<div class="detail">
			<img src="users/{normalize-space(id)}.png" />
			<h4>
				<xsl:value-of select="name" />
				<xsl:value-of select="surname" />
			</h4>
			Description of the user in a few words
			<div class="role">Undefined Role <a href="#">more...</a></div>
			
		</div>
	</xsl:template>

	<xsl:template match="user">
		<li style="background:url(users/{normalize-space(id)}.png);background-repeat:no-repeat;background-position:1px 0px;">
			<xsl:value-of select="name" />
			<xsl:value-of select="surname" />
		</li>
	</xsl:template>
</xsl:stylesheet>
