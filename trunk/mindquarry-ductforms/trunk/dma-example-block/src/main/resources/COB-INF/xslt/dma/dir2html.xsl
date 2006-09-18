<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	xmlns:fn="http://www.w3.org/2005/02/xpath-functions"
	xmlns:mindquarry="mindquarry"
	xmlns:collection="http://apache.org/cocoon/collection/1.0">
	<xsl:import href="../common.xsl" />
	<xsl:param name="file_view_pattern" select="''"/>
	<xsl:param name="changelog_pattern" select="''"/>

	<xsl:template match="/collection:collection">
		<html>
			<head>
				<title>Workspace Content</title>
				<link rel="stylesheet" href="{$reversepath}css/mindquarry.css" type="text/css" />
				<link rel="stylesheet" href="{$reversepath}css/dma.css" type="text/css" />
			</head>
			<body>
				
				<h1><a href="{$reversepath}/{$project}/"><xsl:value-of select="$project"/></a> - <a href="{$reversepath}/{$project}{$path}"><xsl:value-of select="$path"/></a> - Revision <a href="{$reversepath}/{$myrevision}/{$project}{$path}"><xsl:value-of select="@revision"/></a> by <xsl:value-of select="collection:properties/mindquarry:author" /> - <xsl:value-of select="@date"/></h1>
				<xsl:if test="$myrevision&gt;0">
					<a href="{$reversepath}/{$myrevision - 1}/{$project}{$path}">earlier</a> 
				</xsl:if>
				
				<a href="{$reversepath}/{$myrevision + 1}/{$project}{$path}">later</a>
				<ul>
					<li style="list-style-image:url({$reversepath}/icons/22x22/feed.png);">
						<a href="{$reversepath}{$changelog_pattern}/{$project}{$path}">recent changes</a> (<a href=".?show=atom">feed</a>)
					</li>
					<li style="list-style-image:url({$reversepath}/icons/22x22/actions/go-up.png);">
						<a href="{$reversepath}">repository list</a>
					</li>
				</ul>
				<table class="listing" id="dirlist">
   					<thead>
    					<tr>
							<th class="name asc">
								<a title="Sort by name (descending)" 
     								href="/browser/trunk?order=name&amp;desc=1">Name</a>
							</th>
 							<th class="size">
 								<a title="Sort by size" 
 									href="/browser/trunk?order=size">Size</a>
 							</th>
 							<th class="date">
 								<a title="Sort by date" 
 									href="/browser/trunk?order=date">Age</a>
 							</th>
 							<th class="change">Last Change</th>
 						</tr>
 					</thead>
 					<tbody>
 						<xsl:if test="$path!=''">
 							<tr class="even">
 								<td class="name" colspan="5">
       								<a class="parent" title="Parent Directory" href="..">..</a>
								</td>
							</tr>
						</xsl:if>
     
     					<xsl:apply-templates />

     				</tbody>
     			</table>
			</body>
		</html>
	</xsl:template>
				 
	<xsl:template match="collection:properties"/>
	
	<xsl:template match="/collection:collection/collection:collection[position() mod 2 = 0]">
		<tr class="even">
			<xsl:call-template name="collection-columns" />
		</tr>
	</xsl:template>
	
	<xsl:template match="/collection:collection/collection:collection[position() mod 2 = 1]">
		<tr class="odd">
			<xsl:call-template name="collection-columns" />
		</tr>
	</xsl:template>
	
	<xsl:template match="/collection:collection/collection:resource[position() mod 2 = 0]">
		<tr class="even">
			<xsl:call-template name="resource-columns" />
		</tr>
	</xsl:template>
	
	<xsl:template match="/collection:collection/collection:resource[position() mod 2 = 1]">
		<tr class="odd">
			<xsl:call-template name="resource-columns" />
		</tr>
	</xsl:template>
	
	<xsl:template name="collection-columns">
		<td class="name">
			<a class="dir" title="Browse Directory" href="{@name}/">
				<xsl:value-of select="@name" />
			</a>
		</td>
		<td class="size"></td>
		<xsl:call-template name="common-columns" />
	</xsl:template>
	
	<xsl:template name="resource-columns">
		<td class="name">
			<a class="dir" title="View File" href="{$reversepath}{$file_view_pattern}/{$project}{$path}{@name}">
				<xsl:value-of select="@name" />
			</a>
		</td>
		<td class="size"><xsl:value-of select="@size" /></td>
		<xsl:call-template name="common-columns" />
	</xsl:template>
	
	<xsl:template name="common-columns">
		<td class="age"><xsl:value-of select="@date" /></td>
		<td class="change">
			<xsl:apply-templates select="collection:properties/mindquarry:author" />
			<xsl:apply-templates select="collection:properties/mindquarry:message" />
		</td>		
	</xsl:template>
	
	<xsl:template match="collection:properties/mindquarry:author">
		<span class="author"><xsl:value-of select="." />: </span>
	</xsl:template>
	
	<xsl:template match="collection:properties/mindquarry:message">
		<span class="change">
		<xsl:attribute name="title"><xsl:value-of select="." /></xsl:attribute>
			<xsl:choose>
				<xsl:when test="string-length(.)&gt;70">
					<xsl:value-of select="substring(., 1, 70)" /> &#x2026;
				</xsl:when>
				<xsl:otherwise>
					<xsl:value-of select="."/>
				</xsl:otherwise>
			</xsl:choose>
		</span>
	</xsl:template>
	
	<!--
	<xsl:template match="collection:collection">
		<li style="list-style-image:url({$reversepath}/icons/22x22/folder.png);">
			<a href="{@name}/"><xsl:value-of select="@name" /></a> (at <xsl:value-of select="@date" /> by <xsl:value-of select="collection:properties/mindquarry:author" /><xsl:apply-templates select="collection:properties/mindquarry:message" />)
		</li>
	</xsl:template>
	
	<xsl:template match="collection:resource">
		<li style="list-style-image:url({$reversepath}/icons/22x22/{@mimeType}.png);">
			<a href="{@name}"><xsl:value-of select="@name" /></a> (<xsl:value-of select="@mimeType" />, <xsl:value-of select="@size" /> bytes, at <xsl:value-of select="@date" /> by <xsl:value-of select="collection:properties/mindquarry:author" /><xsl:apply-templates select="collection:properties/mindquarry:message" />)
		</li>
	</xsl:template>
	
	<xsl:template match="collection:properties/mindquarry:message">
		<xsl:text> </xsl:text><q><xsl:value-of select="."/></q>
	</xsl:template> 
	-->
</xsl:stylesheet>