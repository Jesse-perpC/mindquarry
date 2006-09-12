<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	xmlns:collection="http://apache.org/cocoon/collection/1.0">
	
	<xsl:template match="/collection:collection">
		<html>
			<head>
				<title>Team Workspaces</title>
			</head>
			<body>
				<ul>
					<xsl:apply-templates select="collection:collection" />
				</ul>
			</body>
		</html>
	</xsl:template> 
	
	<xsl:template match="collection:collection">
		<li>
			<a href="{@name}/"><xsl:value-of select="@name" /></a> (<xsl:value-of select="@date" />)
		</li>
	</xsl:template>
</xsl:stylesheet>