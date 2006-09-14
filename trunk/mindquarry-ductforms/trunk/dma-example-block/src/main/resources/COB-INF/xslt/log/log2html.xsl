<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	xmlns:log="http://mindquarry.com/ns/schema/changelog"
	xmlns="http://www.w3.org/2005/Atom">
	
	<xsl:param name="projectname"/>
	<xsl:param name="baselink"/>
	
	<xsl:template match="/log:changelog">
		<html>
			<head>
				<title>Changes for Project <xsl:value-of select="$projectname" /></title>
			</head>
			<body>
				<dl>
					<xsl:apply-templates select="log:change"/> 
				</dl>
			</body>
		</html>
	</xsl:template>
	
	<xsl:template match="log:change">
		<dd>Revision <xsl:value-of select="@revision" /> at <xsl:value-of select="@date" /></dd>
		<dt>
			<xsl:value-of select="@author" />:
			<blockquote>
				<xsl:value-of select="@message" />
			</blockquote>
			<ul title="changed paths">
				<xsl:apply-templates select="log:path" />
			</ul>
		</dt>
	</xsl:template>
	
	<xsl:template match="log:path">
		<li><xsl:value-of select="@src" /></li>
	</xsl:template>
</xsl:stylesheet>