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
			</head>
			<body>
				<h1>Manage Tasks for <xsl:value-of select="$teamspaceID" /></h1>
				
				<ul class="tasks-list">
					<xsl:apply-templates>
						<xsl:sort select="id" />
					</xsl:apply-templates>
				</ul>
				
		        <a href="task{count(*)+1}">Create new task (task<xsl:value-of select="count(*)+1"/>)</a>
		        <br/>
		        <br/>
				<a href="..">Overview for all your teamspaces</a>				
			</body>
		</html>
    </xsl:template>

    <xsl:template match="task">
		<li>
			<a href="{@xlink:href}"><xsl:value-of select="title" /></a> [<xsl:value-of select="status" />]
		</li>
    </xsl:template>

</xsl:stylesheet>