<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns="http://www.w3.org/1999/xhtml"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

	<xsl:import
		href="resource://com/mindquarry/webapp/xsl/contextpath.xsl" />

	<xsl:template match="/teamspaces">
		<html xmlns="http://www.w3.org/1999/xhtml">
			<head>
				<title>Teamspaces</title>
				<link rel="stylesheet"
					href="resources/css/teamspace.css" type="text/css" />
			</head>
			<body>
				<h1>Manage Your Teams</h1>
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
				<a
					href="{$context.path}blocks/mindquarry-workspace-block/browser/{normalize-space(id)}/">
					<xsl:value-of select="name" />
				</a>
				<xsl:if test="users/user">
					<ul>
						<xsl:apply-templates select="users" />
					</ul>
				</xsl:if>
				<a href="{normalize-space(id)}/editMembers/" class="add_member_button">
					Add Members
				</a>
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
