<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	xmlns:collection="http://apache.org/cocoon/collection/1.0">
	<xsl:param name="repo" select="''"/>
	<xsl:param name="path" select="''"/>
	
	<xsl:param name="reversepath">
		<xsl:call-template name="reversepath">
			<xsl:with-param name="path"><xsl:value-of select="$repo"/><xsl:value-of select="$path"/></xsl:with-param>
		</xsl:call-template>
	</xsl:param>
	
	<xsl:template match="/collection:collection">
		<html>
			<head>
				<title>Workspace Content</title>
			</head>
			<body>
				
				<h1><a href="{$reversepath}{$repo}/"><xsl:value-of select="$repo"/></a> - <a href="{$reversepath}{$repo}{$path}"><xsl:value-of select="$path"/></a> - Revision <a href="{$reversepath}{@revision}/{$repo}{$path}"><xsl:value-of select="@revision"/></a> - <xsl:value-of select="@date"/></h1>
				<a href="{$reversepath}{@revision - 1}/{$repo}{$path}">earlier</a> - <a href="{$reversepath}{@revision + 1}/{$repo}{$path}">later</a>
				<ul>
					<xsl:apply-templates />
				</ul>
				<a href="..">parent</a>
			</body>
		</html>
	</xsl:template> 
	
	<xsl:template name="reversepath">
		<xsl:param name="path" />
		<xsl:choose>
			<xsl:when test="contains($path, '/')">
				<xsl:text>../</xsl:text>
				<xsl:call-template name="reversepath">
					<xsl:with-param name="path" select="substring-after($path, '/')"/>
				</xsl:call-template>
			</xsl:when>
		</xsl:choose>
	</xsl:template>
	
	<xsl:template match="collection:collection">
		<li>
			<a href="{@name}/"><xsl:value-of select="@name" /></a> (<xsl:value-of select="@date" />)
		</li>
	</xsl:template>
	
	<xsl:template match="*">
		<li>
			<a href="{@name}"><xsl:value-of select="@name" /></a> (<xsl:value-of select="@mimeType" />, <xsl:value-of select="@size" /> bytes)
		</li>
	</xsl:template>
</xsl:stylesheet>