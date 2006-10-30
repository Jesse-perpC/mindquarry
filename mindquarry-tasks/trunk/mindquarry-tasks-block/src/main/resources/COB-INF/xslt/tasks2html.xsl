<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

	<xsl:import href="block:/xslt/contextpath.xsl" />
		
	<xsl:template match="/tasks">
		<html>
			<head>
				<title>Tasks</title>
				<link rel="stylesheet" 
					href="{$pathToBlock}css/tasks.css" type="text/css" />
			</head>
			<body>
				<h1>Manage Your Tasks</h1>
				
				<ul class="tasks-list">
					<xsl:apply-templates>
						<xsl:sort select="name" />
					</xsl:apply-templates>
				</ul>				
			</body>
		</html>
	</xsl:template>
				 
	<xsl:template match="teamspace" >
		<li>
			<div class="nifty">
				<div class="name">
					<img class="icon">
						<xsl:attribute name="src">
							<xsl:value-of select="$pathToRoot"/>							
							<xsl:text>teamspace/</xsl:text>
							<xsl:value-of select="id"/>
							<xsl:text>.png</xsl:text>
						</xsl:attribute>
					</img>
					<h2 class="name"><xsl:value-of select="name" /></h2>
					<a href="{id}/">List all tasks</a>
					<span class="description"><xsl:value-of select="description" /></span>
				</div>
			</div>
		</li>
	</xsl:template>
</xsl:stylesheet>
