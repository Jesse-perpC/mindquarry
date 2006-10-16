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
			</head>
			<body>
				<h1>Manage Your Teams</h1>
				<a href="createTeamspace/">create new teamspace</a>
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
			<a href="#">more</a>
			<ul class="members">
				<li style="background:url(users/arnaud.png);background-repeat:no-repeat;background-position:1px 0px;">First Margand</li>
				<li style="background:url(users/arnaud.png);background-repeat:no-repeat;background-position:1px 0px;">Second Margand</li>
				<li style="background:url(users/arnaud.png);background-repeat:no-repeat;background-position:1px 0px;">Third Margand</li>
				<li style="background:url(users/arnaud.png);background-repeat:no-repeat;background-position:1px 0px;">Arnaud Margand</li>
				<li style="background:url(users/arnaud.png);background-repeat:no-repeat;background-position:1px 0px;">Hans-Werner Sowieso</li>
				
				<li style="display:none;background:url(users/arnaud.png);background-repeat:no-repeat;background-position:1px 0px;">Superman</li>
				<li style="display:none;background:url(users/arnaud.png);background-repeat:no-repeat;background-position:1px 0px;">Zorro</li>
				<li style="display:none;background:url(users/arnaud.png);background-repeat:no-repeat;background-position:1px 0px;">Robin Hood</li>
			</ul>
			<div class="name">
				<img class="icon" src="teams/goshaky.png" />
				<h2 class="name"><xsl:value-of select="name" /></h2>
				<span class="description">Description of the project in plain enlish
				letters and words. And I add so much words here to test if the
				description can overflow nicely.</span>
			</div>
			
			<ul class="tags">
				<li><a href="#">docbook</a></li>
				<li><a href="#">techdoc</a></li>
				<li><a href="#">xml</a></li>
				<li><a href="#">source</a></li>
				<li><a href="#">open</a></li>
			</ul>
			<!-- 
				<a
					href="{$context.path}blocks/mindquarry-workspace-block/browser/{normalize-space(id)}/">
					<xsl:value-of select="name" />
				</a>
				<xsl:if test="users/user">
					<ul>
						<xsl:apply-templates select="users" />
					</ul>
				</xsl:if>
				<a href="{normalize-space(id)}/editMembers/" rel="lightbox" class="add_member_button">
					Edit Members
				</a>
				 -->
			</div>
		</li>
	</xsl:template>

	<xsl:template match="user">
		<li>
			<p>
				<xsl:value-of select="name" />
			</p>
		</li>
	</xsl:template>
</xsl:stylesheet>
