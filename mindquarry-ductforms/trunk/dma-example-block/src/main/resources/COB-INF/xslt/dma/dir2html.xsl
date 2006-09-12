<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	xmlns:collection="http://apache.org/cocoon/collection/1.0">
	<xsl:param name="repo" select="''"/>
	<xsl:param name="path" select="''"/>
	<xsl:param name="revision" select="false()"/>
	<xsl:param name="myrevision">
		<xsl:choose>
			<xsl:when test="$revision">
				<xsl:value-of select="$revision" />
			</xsl:when>
			<xsl:otherwise>
				<xsl:value-of select="/collection:collection/@revision" />
			</xsl:otherwise>
		</xsl:choose>
	</xsl:param>
	
	<xsl:param name="reversepath">
		<xsl:call-template name="reversepath">
			<xsl:with-param name="path">
				<xsl:if test="$revision">
					<xsl:value-of select="$revision" /><xsl:text>/</xsl:text>
				</xsl:if>
				<xsl:value-of select="$repo"/>
				<xsl:value-of select="$path"/>
				</xsl:with-param>
		</xsl:call-template>
	</xsl:param>
	
	<xsl:template match="/collection:collection">
		<html>
			<head>
				<title>Workspace Content</title>
			</head>
			<body>
				
				<h1><a href="{$reversepath}{$repo}/"><xsl:value-of select="$repo"/></a> - <a href="{$reversepath}{$repo}{$path}"><xsl:value-of select="$path"/></a> - Revision <a href="{$reversepath}{$myrevision}/{$repo}{$path}"><xsl:value-of select="@revision"/></a> - <xsl:value-of select="@date"/></h1>
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
					<xsl:apply-templates />
				</ul>
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
		<li style="list-style-image:url({$reversepath}icons/22x22/folder.png);">
			<a href="{@name}/"><xsl:value-of select="@name" /></a> (<xsl:value-of select="@date" />)
		</li>
	</xsl:template>
	
	<xsl:template match="collection:resource">
		<li style="list-style-image:url({$reversepath}icons/22x22/{@mimeType}.png);">
			<a href="{@name}"><xsl:value-of select="@name" /></a> (<xsl:value-of select="@mimeType" />, <xsl:value-of select="@size" /> bytes)
		</li>
	</xsl:template>
</xsl:stylesheet>