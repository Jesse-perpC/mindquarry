<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

	<xsl:template match="/teamspaces">
		<html>
			<head>
				<title>Mindquarry Teamspaces</title>
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
			<a href="../mindquarry-workspace-block/browser/{name}"><xsl:value-of select="name" /></a>
		</li>
	</xsl:template>
</xsl:stylesheet>