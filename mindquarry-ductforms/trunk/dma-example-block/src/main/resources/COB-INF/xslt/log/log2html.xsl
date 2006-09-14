<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	xmlns:log="http://mindquarry.com/ns/schema/changelog"
	xmlns="http://www.w3.org/2005/Atom">
	<xsl:import href="../dma/common.xsl" />
	
	<xsl:template match="/log:changelog">
		<html>
			<head>
				<title>Changes for Project <xsl:value-of select="$repo" /></title>
			</head>
			<body>
				<h1>Changes: <a href="{$reversepath}{$repo}/"><xsl:value-of select="$repo"/></a> - <a href="{$reversepath}{$repo}{$path}"><xsl:value-of select="$path"/></a> - Revision <a href="{$reversepath}{$myrevision}/{$repo}{$path}"><xsl:value-of select="$myrevision"/></a> - <xsl:value-of select="./log:change[1]/@date"/></h1>
				<xsl:if test="$myrevision&gt;0">
					<a href="{$reversepath}{$myrevision - 1}/{$repo}{$path}?show=changes">earlier</a> 
				-
				</xsl:if>
				<a href="{$reversepath}{$myrevision + 1}/{$repo}{$path}?show=changes">later</a>
				
				<dl>
					<xsl:apply-templates select="log:change"/> 
				</dl>
			</body>
		</html>
	</xsl:template>
	
	<xsl:template match="log:change[@revision=0]">
		<dt>Revision <xsl:value-of select="@revision" /> at <xsl:value-of select="@date" /></dt>
		<dd>Repository created</dd>
	</xsl:template>
	
	<xsl:template match="log:change">
		<dt>Revision <xsl:value-of select="@revision" /> at <xsl:value-of select="@date" /></dt>
		<dd>
			<xsl:value-of select="@author" />:
			<blockquote>
				<xsl:value-of select="@message" />
			</blockquote>
			<ul title="changed paths">
				<xsl:apply-templates  />
			</ul>
		</dd>
	</xsl:template>
	
	<xsl:template match="log:path">
		<li><xsl:value-of select="@src" /></li>
	</xsl:template>
	
	<xsl:template match="log:replacement">
		<li>replaced: <a href="{@src}"><xsl:value-of select="@src"/></a></li>
	</xsl:template>
	
	<xsl:template match="log:deletion">
		<li>deleted: <xsl:value-of select="@src" /></li>
	</xsl:template>
	
	<xsl:template match="log:addition">
		<li>added: <a href="{$reversepath}{../@revision}/{$repo}{@src}"><xsl:value-of select="@src"/></a></li>
	</xsl:template>
	
	<xsl:template match="log:modification">
		<li>modified: <a href="{$reversepath}{../@revision}/{$repo}{@src}"><xsl:value-of select="@src"/></a></li>
	</xsl:template>
	
	<xsl:template match="*" />
</xsl:stylesheet>