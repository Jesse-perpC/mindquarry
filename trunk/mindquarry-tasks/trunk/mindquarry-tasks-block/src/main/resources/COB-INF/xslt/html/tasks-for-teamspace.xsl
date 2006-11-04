<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0"
        xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
        xmlns:xlink="http://www.w3.org/1999/xlink">

	<xsl:import href="block:/xslt/contextpath.xsl" />
		
	<xsl:param name="teamspaceID" />    

    <xsl:template match="/tasks">
		<html>
			<head>
				<title>Tasks for <xsl:value-of select="$teamspaceID" /></title>
				<link rel="stylesheet" 
					href="{$pathToBlock}css/tasks.css" type="text/css" />
			</head>
			<body>
				<h1>Manage Tasks for <xsl:value-of select="$teamspaceID" /></h1>
				
				<div class="task-area">				
					<ul class="task-list">
						<xsl:apply-templates>
							<xsl:sort select="id" />
						</xsl:apply-templates>
					</ul>
					
			        <br/>
		        	<a class="create_task_button" href="task{count(*)+1}">Create task</a>
			        <br/>
			        <br/>
					<a href="..">Back to overview</a>				
			        <br/>
			        <br/>
			    </div>
			</body>
		</html>
    </xsl:template>

    <xsl:template match="task">
		<li>
			<a href="{@xlink:href}"><xsl:value-of select="title" /></a> [<xsl:value-of select="status" />]
		</li>
    </xsl:template>

</xsl:stylesheet>