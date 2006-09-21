<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	
	<xsl:import href="resource://com/mindquarry/webapp/xsl/contextpath.xsl"/>

	<xsl:template match="/teamspaces">
		<html>
			<head>
				<title>Teamspaces</title>
				<link rel="stylesheet" href="resources/css/teamspace.css" ype="text/css" />
			</head>
			<body>
				<ul>
					<xsl:apply-templates>
						<xsl:sort select="name" />
					</xsl:apply-templates>
				</ul>				
			</body>
		</html>
	</xsl:template>
				 
	<xsl:template match="teamspace" >
		<li>
			<a href="{$context.path}blocks/mindquarry-workspace-block/browser/{name}/trunk/"><xsl:value-of select="name" /></a>
		</li>
	</xsl:template>
</xsl:stylesheet>