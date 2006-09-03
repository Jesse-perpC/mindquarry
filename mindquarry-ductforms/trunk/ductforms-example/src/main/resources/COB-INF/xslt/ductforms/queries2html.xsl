<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	xmlns:df="http://mindquarry.com/ns/xml/ductforms"
	xmlns:dir="http://apache.org/cocoon/directory/2.0"
	xmlns:xi="http://www.w3.org/2001/XInclude">

	<xsl:template match="/df:query">
		<html>
			<head>
				<title>Available Queries</title>
			</head>
			<body>
				<ul>
					<xsl:apply-templates select="xsl:stylesheet/df:title" />
				</ul>
			</body>
		</html>
	</xsl:template>

	<xsl:template match="df:title">
		<li>
			<a href="{@path}.query">
				<xsl:apply-templates />
			</a>
		</li>
	</xsl:template>

</xsl:stylesheet>