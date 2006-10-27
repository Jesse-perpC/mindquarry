<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0"
        xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
		xmlns:collection="http://apache.org/cocoon/collection/1.0">

	<xsl:param name="teamspaceID" />    

    <xsl:template match="/collection:collection">
		<html>
			<head>
				<title>Tasks for <xsl:value-of select="$teamspaceID" /></title>
			</head>
			<body>
				<h1>Manage Tasks for <xsl:value-of select="$teamspaceID" /></h1>
				
				<ul class="tasks-list">
					<xsl:apply-templates>
						<xsl:sort select="name" />
					</xsl:apply-templates>
				</ul>
				
		        <a href="task{count(*)+1}.xml">Create new task (task<xsl:value-of select="count(*)+1"/>)</a>
		        <br/>
		        <br/>
				<a href="..">Overview for all your teamspaces</a>				
			</body>
		</html>
    </xsl:template>

    <xsl:template match="collection:resource">
		<li>
			<a href="{@name}"><xsl:value-of select="substring-before(@name, '.xml')" /></a>
		</li>
    </xsl:template>

</xsl:stylesheet>