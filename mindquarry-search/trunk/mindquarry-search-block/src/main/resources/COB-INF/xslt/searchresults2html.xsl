<?xml version="1.0" encoding="UTF-8"?>
<!--
	Copyright (C) 2006-2007 Mindquarry GmbH, All Rights Reserved
	
	The contents of this file are subject to the Mozilla Public License
	Version 1.1 (the "License"); you may not use this file except in
	compliance with the License. You may obtain a copy of the License at
	http://www.mozilla.org/MPL/
	
	Software distributed under the License is distributed on an "AS IS"
	basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
	License for the specific language governing rights and limitations
	under the License.
-->
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0"
	xmlns:java="http://xml.apache.org/xslt/java">

	<xsl:param name="query" />
	<xsl:param name="start" />
	<xsl:param name="fq" />
	
	<xsl:param name="hitsPerPage">10</xsl:param>

	<xsl:template match="/">
		<html>
			<head>
				<title>Search results for: <xsl:value-of select="$query" />
					<xsl:if test="$fq">
						(<xsl:value-of select="$fq"/>)
					</xsl:if>
				</title>
				<link rel="breadcrumb" title="Search for: {$query}"/>
				<!--				
				<xsl:if test="number($start)-$hitsPerPage &gt;= 0">
					<link rel="start" title="start" href="/search/query?q={$query}&amp;start=0&amp;fq={$fq}"/>
				</xsl:if>
				-->
				<xsl:if test="number($start)-$hitsPerPage &gt;= 0">
					<link rel="prev" title="previous" href="/search/query?q={$query}&amp;start={number($start)-$hitsPerPage}&amp;fq={$fq}"/>
				</xsl:if>
				<xsl:if test="number($start)+$hitsPerPage &lt; number(/response/result/@numFound)">
					<link rel="next" title="next" href="/search/query?q={$query}&amp;start={number($start)+$hitsPerPage}&amp;fq={$fq}"/>
				</xsl:if>
				<xsl:choose>
					<xsl:when test="$fq">
						<link rel="facet" title="show all" href="/search/query?q={$query}&amp;start=0"/>
					</xsl:when>
					<xsl:otherwise>
						<xsl:apply-templates select="/response/lst/lst[@name='facet_fields']/lst[@name='type']"/>
					</xsl:otherwise>
				</xsl:choose>
			</head>
			<body>
				<form action="/search/query" method="GET" class="button search-action">
					<input type="hidden" name="start" value="0" />
				
					<input type="text" name="q">
						<xsl:attribute name="value"><xsl:value-of select="$query" /></xsl:attribute>
					</input>
					<input type="submit" value="Search" />

				</form>
				
				<div class="list">
					<ul>
						<xsl:choose>
							<xsl:when test="number(/response/result/@numFound) = 0">
								<li><h2>No matches</h2></li>
							</xsl:when>
							<xsl:otherwise>
								<xsl:apply-templates select="/response/result/doc"/>
							</xsl:otherwise>							
						</xsl:choose>
					</ul>
				</div>
			</body>
		</html>
	</xsl:template>

    <xsl:template match="doc">
    		<xsl:variable name="link"><xsl:value-of select="substring(str[@name='location'], 19)" /></xsl:variable>
    		<xsl:variable name="link2"><xsl:value-of select="substring($link, 0, string-length($link)-3)" /></xsl:variable>
    		<xsl:variable name="teamname"><xsl:value-of select="substring-before($link, '/')" /></xsl:variable>
    		<xsl:variable name="typename"><xsl:value-of select="substring-before(substring-after($link, '/'), '/')" /></xsl:variable>
    		<xsl:variable name="lastpart"></xsl:variable>
    		<xsl:choose>
	    		<xsl:when test="contains(str[@name='location'],'/wiki/')">
		    		<xsl:variable name="lastpart2"><xsl:value-of select="substring-before(substring-after(substring-after(substring-after($link, '/'), '/'), '/'), '/')" /></xsl:variable>
					<xsl:attribute name="href">/<xsl:value-of select="$typename" />/<xsl:value-of select="$teamname" />/<xsl:value-of select="$lastpart2" /></xsl:attribute>
		    	</xsl:when>
	    		<xsl:when test="contains(str[@name='location'],'/talk/')">
		    		<xsl:variable name="lastpart3"><xsl:value-of select="substring-before(substring-after(substring-after(substring-after($link, '/'), '/'), '/'), '/')" /></xsl:variable>
					<xsl:attribute name="href">/<xsl:value-of select="$typename" />/<xsl:value-of select="$teamname" />/<xsl:value-of select="$lastpart3" />/</xsl:attribute>
		    	</xsl:when>
	    		<xsl:when test="contains(str[@name='location'],'/tasks/')">
		    		<xsl:variable name="lastpart4"><xsl:value-of select="substring-after(substring-after($link, '/'), '/')" /></xsl:variable>
					<xsl:attribute name="href">/<xsl:value-of select="$typename" />/<xsl:value-of select="$teamname" />/<xsl:value-of select="$lastpart4" /></xsl:attribute>
		    	</xsl:when>
    		</xsl:choose>
    			
    	<li>
    		<img class="icon">
    			<xsl:attribute name="src">
    				<xsl:choose>
    					<xsl:when test="contains(str[@name='location'],'/wiki/')">
    						<xsl:text>/resources/tango-icons/48/apps/mindquarry-wiki.png</xsl:text>
    					</xsl:when>
    					<xsl:when test="contains(str[@name='location'],'/talk/')">
    						<xsl:text>/resources/tango-icons/48/apps/mindquarry-talk.png</xsl:text>
    					</xsl:when>
    					<xsl:when test="contains(str[@name='location'],'/tasks/')">
    						<xsl:text>/resources/tango-icons/48/apps/mindquarry-tasks.png</xsl:text>
    					</xsl:when>
    					<xsl:otherwise>
    						<xsl:text>/resources/tango-icons/22/places/folder.png</xsl:text>
    					</xsl:otherwise>
    				</xsl:choose>
    			</xsl:attribute>
    			
    		</img>
   			<h2><a>
   			<xsl:choose>
    			<xsl:when test="contains(str[@name='location'],'/wiki/')">
	    			<xsl:variable name="lastpart2"><xsl:value-of select="substring-before(substring-after(substring-after(substring-after($link, '/'), '/'), '/'), '/')" /></xsl:variable>
					<xsl:attribute name="href">/<xsl:value-of select="$typename" />/<xsl:value-of select="$teamname" />/<xsl:value-of select="$lastpart2" /></xsl:attribute>
	    		</xsl:when>
    			<xsl:when test="contains(str[@name='location'],'/talk/')">
	    			<xsl:variable name="lastpart3"><xsl:value-of select="substring-before(substring-after(substring-after(substring-after($link, '/'), '/'), '/'), '/')" /></xsl:variable>
					<xsl:attribute name="href">/<xsl:value-of select="$typename" />/<xsl:value-of select="$teamname" />/<xsl:value-of select="$lastpart3" />/</xsl:attribute>
	    		</xsl:when>
    			<xsl:when test="contains(str[@name='location'],'/tasks/')">
	    			<xsl:variable name="lastpart4"><xsl:value-of select="substring-after(substring-after($link, '/'), '/')" /></xsl:variable>
					<xsl:attribute name="href">/<xsl:value-of select="$typename" />/<xsl:value-of select="$teamname" />/<xsl:value-of select="$lastpart4" /></xsl:attribute>
	    		</xsl:when>
    		</xsl:choose>
			
    		<xsl:choose>
    			<xsl:when test="str[@name='title'] = ''">
    				untitled
    			</xsl:when>
    			<xsl:otherwise>
	    			<xsl:value-of select="str[@name='title']" />
    			</xsl:otherwise>
    		</xsl:choose>
    		
    		</a></h2>
    		<p>Result <xsl:value-of select="position()+$start" /> of <xsl:value-of select="/response/result/@numFound" /></p>
    		<!--
    		<div>
    			hier mehr content in weiteren spalten
    		</div>
    		-->
    		</li>
    </xsl:template>

    <xsl:template match="int">
    	<xsl:param name="hits"><xsl:value-of select="." /></xsl:param>
    	<xsl:choose>
    		<xsl:when test="$hits > 0">
    			<link rel="facet" title="{@name} ({.})" href="/search/query?q={$query}&amp;fq=type:{@name}&amp;start=0"/>
    		</xsl:when>
    		<xsl:otherwise>
    			<link rel="facet" title="{@name} ({.})"/>
    		</xsl:otherwise>
    	</xsl:choose>
    	<br />
    </xsl:template>

</xsl:stylesheet>
