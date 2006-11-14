<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	xmlns:xlink="http://www.w3.org/1999/xlink">

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
				
				<ul class="list">
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
							<xsl:value-of select="@xlink:href"/>
							<xsl:text>.png</xsl:text>
						</xsl:attribute>
					</img>
					<h2 class="name"><xsl:value-of select="name" /></h2>
					<span class="description"><xsl:value-of select="description" /></span>
					
					<div class="summary">
						Teamspace <b><xsl:value-of select="name" /></b> contains 
						<a href="{$pathToBlock}{@xlink:href}/"><xsl:value-of select="count(task)" /> Tasks</a>
						(<xsl:value-of select="count(task[status='new'])" /> New,
						<xsl:value-of select="count(task[status='running'])" /> Running,
						<xsl:value-of select="count(task[status='paused'])" /> Paused and
						<xsl:value-of select="count(task[status='done'])" /> Done)
					</div>
				</div>
				
				<div class="links">
					<ul>
	        			<li><a class="create_task_button" href="{@xlink:href}/task{count(*)+1}">Create task</a></li>
	        			<li><a class="create_filter_button" href="{@xlink:href}/query">Create filter</a></li>
					</ul>
				</div>
				
				<div class="queries">
					<h3>Saved Filters</h3>
					<ul>
						<xsl:call-template name="filters" />
						<li>test</li>
					</ul>
				</div>
			</div>
		</li>
	</xsl:template>
	
	<xsl:template name="filters">
		
	</xsl:template>
</xsl:stylesheet>
