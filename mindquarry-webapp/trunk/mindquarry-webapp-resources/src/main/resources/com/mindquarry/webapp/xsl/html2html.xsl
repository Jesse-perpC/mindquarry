<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	xmlns="http://www.w3.org/1999/xhtml">
	
	<xsl:import href="contextpath.xsl"/>
	
	<xsl:template match="@*|node()">
		<xsl:copy>
			<xsl:apply-templates select="@*|node()" />
		</xsl:copy>
	</xsl:template>

	<xsl:template match="html">
		<html xmlns="http://www.w3.org/1999/xhtml">
			<xsl:apply-templates />	
		</html>
	</xsl:template>
	
	<xsl:template match="head">
		<head>
			<xsl:apply-templates select="*"/>
			<link rel="stylesheet" href="{$context.path}blocks/mindquarry-webapp-resources/css/screen.css" media="screen,projection" type="text/css" />
		</head>
	</xsl:template>
	
	<xsl:template match="title">
		<title>Mindquarry: <xsl:value-of select="." /></title>
	</xsl:template>
	
	<xsl:template match="body">
		<body>
			<div class="body">
				<div id="header">
        			<ul id="sections">
						<li><a class="navTalk" href="content.html">Talk</a></li>
						<li><a class="navTasks" href="content.html">Tasks</a></li>
						<li><a class="navWiki" href="content.html">Wiki</a></li>
						<li><a class="navFiles" href="content.html">Files</a></li>
						<li><a class="navTeams" href="content.html">Teams</a></li>
					</ul>
				</div>
				<xsl:apply-templates />
			</div>
		</body>
	</xsl:template>
	
</xsl:stylesheet>