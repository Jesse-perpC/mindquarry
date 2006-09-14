<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	xmlns:mindquarry="mindquarry"
	xmlns:collection="http://apache.org/cocoon/collection/1.0">
	<xsl:import href="common.xsl" />
	
	<xsl:template match="/collection:collection">
		<html>
			<head>
				<title>Workspace Content</title>
			</head>
			<body>
				
				<h1><a href="{$reversepath}{$repo}/"><xsl:value-of select="$repo"/></a> - <a href="{$reversepath}{$repo}{$path}"><xsl:value-of select="$path"/></a> - Revision <a href="{$reversepath}{$myrevision}/{$repo}{$path}"><xsl:value-of select="@revision"/></a> by <xsl:value-of select="collection:properties/mindquarry:author" /> - <xsl:value-of select="@date"/></h1>
				<a href="{$reversepath}{$myrevision - 1}/{$repo}{$path}">earlier</a> - <a href="{$reversepath}{$myrevision + 1}/{$repo}{$path}">later</a>
				<ul>
					<xsl:choose>
						<xsl:when test="$path='/'">
							<li style="list-style-image:url({$reversepath}icons/22x22/actions/go-up.png);">
								<a href="{$reversepath}">repository list</a>
							</li>
						</xsl:when>
						<xsl:otherwise>
							<li style="list-style-image:url({$reversepath}icons/22x22/actions/go-up.png);">
								<a href="../">parent folder</a>
							</li>
						</xsl:otherwise>
					</xsl:choose>
					<li style="list-style-image:url({$reversepath}icons/22x22/feed.png);">
						<a href=".?show=changes">recent changes</a> (<a href=".?show=atom">feed</a>)
					</li>
					<xsl:apply-templates />
				</ul>
			</body>
		</html>
	</xsl:template> 
	
	<xsl:template match="collection:properties"/>
	
	<xsl:template match="collection:collection">
		<li style="list-style-image:url({$reversepath}icons/22x22/folder.png);">
			<a href="{@name}/"><xsl:value-of select="@name" /></a> (by <xsl:value-of select="collection:properties/mindquarry:author" /> at <xsl:value-of select="@date" />)
		</li>
	</xsl:template>
	
	<xsl:template match="collection:resource">
		<li style="list-style-image:url({$reversepath}icons/22x22/{@mimeType}.png);">
			<a href="{@name}"><xsl:value-of select="@name" /></a> (<xsl:value-of select="@mimeType" />, <xsl:value-of select="@size" /> bytes, by <xsl:value-of select="collection:properties/mindquarry:author" /> at <xsl:value-of select="@date" />)
		</li>
	</xsl:template>
</xsl:stylesheet>