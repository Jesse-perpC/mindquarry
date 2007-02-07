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

<xsl:stylesheet version="1.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	xmlns:xhtml="http://www.w3.org/1999/xhtml"
	xmlns:wa="http://www.mindquarry.com/ns/schema/webapp"
	xmlns:us="http://www.mindquarry.com/ns/schema/userswitch"
	xmlns:bu="http://apache.org/cocoon/browser-update/1.0"
	exclude-result-prefixes="#default xhtml wa us">
	
<!-- 
	NEVER! NEVER! use alt-f to re-format this code.
 -->
	<xsl:import href="contextpath.xsl"/>
	
	<xsl:output indent="no"/>

	<!-- external parameters -->
	<xsl:param name="user.agent" select="''"/>
	<xsl:param name="username" select="''"/>
	<xsl:param name="version" select="'undefined'"/>
	<xsl:param name="serverTitle" select="'Mindquarry'"/>
	
	<xsl:param name="cssPath" select="'css/'" />
	<xsl:param name="scriptPath" select="'scripts/'" />

	<!-- resources directory for Dojo js, css and the like -->
	<xsl:param name="resources-uri">
		<xsl:value-of select="$pathToRoot"/>
		<xsl:text>resources/_cocoon/resources</xsl:text>
	</xsl:param>
	
	<xsl:template match="@*|node()">
		<xsl:copy>
			<xsl:apply-templates select="@*|node()" />
		</xsl:copy>
	</xsl:template>
	
	<!-- do not remove the prefix for the cocoon AJAX Browser Update namespace -->
	<xsl:template match="bu:*">
		<xsl:copy>
			<xsl:apply-templates select="@*|node()" />
		</xsl:copy>
	</xsl:template>
	
	<xsl:template match="*">
		<xsl:element name="{local-name(.)}">
			<xsl:apply-templates select="@*|node()" />
		</xsl:element>
	</xsl:template>

	<!-- 
		NEVER! NEVER! use alt-f to re-format this code.
	-->
	<xsl:template match="us:text">
		<xsl:choose>
			<xsl:when test="contains($user.agent, @value)">
				<xsl:value-of select="@value"/>
				<xsl:apply-templates select="us:value/node()" />
			</xsl:when>
			<xsl:otherwise>
				<xsl:value-of select="@default"/>
				<xsl:apply-templates select="us:default/node()" />
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>

	<!-- 
		NEVER! NEVER! use alt-f to re-format this code.
	-->
	<xsl:template match="us:attribute">
		<xsl:attribute name="{@name}">
			<xsl:choose>
				<xsl:when test="contains($user.agent, @value)">
					<xsl:value-of select="@value"/>
				</xsl:when>
				<xsl:otherwise>
					<xsl:value-of select="@default"/>
				</xsl:otherwise>
			</xsl:choose>
		</xsl:attribute>
	</xsl:template>

	<!-- 
		NEVER! NEVER! use alt-f to re-format this code.
	-->
	<xsl:template match="xhtml:html|html">
		<html>
			<xsl:apply-templates />
		</html>
	</xsl:template>
	

	<xsl:template match="xhtml:head|head">
		<head>
			<xsl:apply-templates select="." mode="nifty" />
			<!-- dojo baselib: must be the first js include -->
			<script type="text/javascript">djConfig = { isDebug: false };</script>
			<script type="text/javascript" src="{$pathToRoot}resources/_cocoon/resources/dojo/dojo.js" >//</script>
			
			<!-- basic styling -->
			<link rel="stylesheet" href="{$pathToBlock}{$cssPath}screen.css" media="screen,projection" type="text/css" />
			<link rel="stylesheet" href="{$pathToBlock}{$cssPath}headerandlines.css" media="screen,projection" type="text/css" />
			<link rel="icon" href="{$pathToRoot}resources/icons/mindquarry-icon.ico" type="image/x-icon" />

			<!-- lightbox -->
			<link rel="stylesheet" type="text/css" href="{$pathToBlock}{$cssPath}lightbox.css" />
			<link rel="stylesheet" type="text/css" href="{$pathToBlock}{$cssPath}lightbox-forms.css" />
			<script type="text/javascript" src="{$pathToBlock}{$scriptPath}lightbox.js" >//</script>

			<!-- forms -->			
			<script src="{$resources-uri}/forms/js/forms-lib.js" type="text/javascript"/>
			<script type="text/javascript">
				dojo.addOnLoad(forms_onload);
				dojo.require("cocoon");
				dojo.require("cocoon.forms.*");
			</script>
			<link rel="stylesheet" type="text/css" href="{$resources-uri}/forms/css/forms.css"/>
			
			<!-- general scripts/dojo widgets -->
			<script type="text/javascript" src="{$pathToBlock}{$scriptPath}bugreport.js" >//</script>
			<script type="text/javascript" src="{$pathToBlock}{$scriptPath}login.js" >//</script>
			<script type="text/javascript" src="{$pathToBlock}{$scriptPath}dojoutils.js" >//</script>
			<script type="text/javascript">
				dojo.require("mindquarry.widget.QuickSearch");
				dojo.require("mindquarry.widget.TeamSwitcher");
				dojo.require("mindquarry.widget.ChangePassword");
			</script>

			<xsl:apply-templates />
			
		</head>
	</xsl:template>

	<xsl:template match="xhtml:head|head" mode="nifty">
		<!-- only include the nifty.css if there are actually
		nifty divs in the html to niftify -->
		<xsl:if test="//xhtml:div[@class='nifty']|//div[@class='nifty']">
			<link rel="stylesheet" type="text/css" href="{$pathToBlock}{$cssPath}nifty.css" />
		</xsl:if>
	</xsl:template>	

	<!-- 
		NEVER! NEVER! use alt-f to re-format this code.
	-->
	<xsl:template match="xhtml:script[normalize-space(.)='']|script[normalize-space(.)='']">
		<xsl:copy>
			<xsl:copy-of select="@*" />
			<xsl:text>//</xsl:text>
		</xsl:copy>
	</xsl:template>
	
	<xsl:template match="xhtml:textarea[normalize-space(.)='']|textarea[normalize-space(.)='']">
		<xsl:copy>
			<xsl:copy-of select="@*" />
				<xsl:text></xsl:text>
		</xsl:copy>
	</xsl:template>

	<!-- 
		NEVER! NEVER! use alt-f to re-format this code.
	-->
	<xsl:template match="xhtml:title|title">
		<title><xsl:value-of select="." /> - <xsl:value-of select="$serverTitle"/></title>
	</xsl:template>

	<xsl:template match="xhtml:body|body">
		<body>
			<div class="body">
				<!-- layouting the header -->
				<div id="webapp-header">
					<ul id="webapp-sections">
						<li><a class="navTeams" href="{$pathToRoot}teams/team/{$teamspaceNameWithSlash}"><b>Teams</b></a></li>
						<li><a class="navFiles" href="{$pathToRoot}files/{$teamspaceNameWithBrowse}"><b>Files</b></a></li>
						<li><a class="navWiki" href="{$pathToRoot}wiki/{$teamspaceNameWithSlash}"><b>Wiki</b></a></li>
						<li><a class="navTasks" href="{$pathToRoot}tasks/{$teamspaceNameWithSlash}"><b>Tasks</b></a></li>
						<!--li><a class="navTalk" href="{$pathToRoot}talk/">Talk</a></li-->
					</ul>

					<!-- test if user is logged in -->				
					<xsl:if test="string-length($username) > 0">
						<!-- make logging status widget -->
						<div id="user-status-background">
							<xsl:text> </xsl:text>
						</div>
						<div id="user-status">
							<div id="http-logout-hint"><b>(Close browser to logout)</b></div>
							<a href="{$pathToRoot}" id="path-to-webapp-root" title="logout"/>
							<div class="username-display">
								<a href="{$pathToRoot}teams/editUser/?targetUri={$pathToRoot}" id="edit-user-link" title="Edit your user profile ({$username})" rel="lightbox"><b><xsl:value-of select="$username" /></b></a>
							</div>
							<a href="{$pathToRoot}help/" id="head-help-link" title="Show help"><b>Help</b></a>													
						</div>
					</xsl:if>
				</div>
				
				<!-- test if user is logged in -->
				<xsl:if test="string-length($username) > 0">
					<!-- teamspace switcher -->
					<div title="{$teamspaceName}" dojoType="TeamSwitcher">
						<a href="{$pathToTeamspaceBase}" id="teamspace-base-link">Teamspace Base</a>
						<include src="block:teams:/user/{$username}/info" xmlns="http://apache.org/cocoon/include/1.0"/>
					</div>	
					
					<!-- make quick search widget -->
					<div class="quicksearch" dojoType="QuickSearch" url="/solr-select/" size="20" maxheight="300">&#160;</div>
				</xsl:if>

				<!-- layouting the content -->
				<div id="webapp-content">
					<div id="sidebar">
						<!-- TODO: sidebar -->
					</div>
					
					<div id="innercontent">
						<xsl:apply-templates />
						
						<xsl:call-template name="alternate" />
					</div>
				</div>
				
				<!-- layouting the footer -->
				<div id="webapp-footer">
					<ul id="webapp-footer-sections">
						<li><a href="{$pathToRoot}">Home</a></li>
						<li><a href="{$pathToRoot}teams/">Teams</a></li>
						<li><a href="{$pathToRoot}files/">Files</a></li>
						<li><a href="{$pathToRoot}wiki/">Wiki</a></li>
						<li><a href="{$pathToRoot}tasks/">Tasks</a></li>
						<!--li><a href="{$pathToRoot}talk/">Talk</a></li-->

						<!-- test if user is logged in -->
						<xsl:if test="string-length($username) > 0">
							<li><a href="{$pathToRoot}resources/client/client.jnlp">Client</a></li>
						</xsl:if>

						<!--<li><a href="{$pathToRoot}search/">Search</a></li>-->
						<li><a href="{$pathToRoot}help/">Help</a></li>
						<!--<li><a href="http://www.mindquarry.com">Mindquarry Version <xsl:value-of select="$version"/></a></li>-->
						<li><a href="http://www.mindquarry.com/support/">Get Mindquarry Support</a></li>
						<li><a id="bugreport" 
							href="mailto:support@mindquarry.com?subject=Bug%20Report">Report a Bug</a>
						</li>
					</ul>
					<span class="version-footer">Mindquarry Version <xsl:value-of select="$version"/></span>
				</div>
			</div>
		</body>
	</xsl:template>
	
	<!-- generate links for alternate content versions -->
	<xsl:template name="alternate">
		<xsl:if test="//link[@rel='alternate']|//xhtml:link[@rel='alternate']">
			<div class="nifty">
				<b class="rtop">
					<b class="r1"><xsl:comment>t</xsl:comment></b>
					<b class="rleft"><xsl:comment>tr</xsl:comment></b>
					<b class="rright"><xsl:comment>tl</xsl:comment></b>
				</b>
				
				<div class="niftycontent">
					<h3 class="bottom-header">Alternative Versions</h3>
					<xsl:apply-templates select="//link[@rel='alternate']|//xhtml:link[@rel='alternate']" mode="body"/>
					<br style="clear:both"/>
				</div>
				
				<b class="rbottom" >
					<b class="r1"><xsl:comment>b</xsl:comment></b>
					<b class="rleft"><xsl:comment>bl</xsl:comment></b>
					<b class="rright"><xsl:comment>br</xsl:comment></b>
				</b>
			</div>
		</xsl:if>
	</xsl:template>
	
	<xsl:template match="//link[@rel='alternate']|//xhtml:link[@rel='alternate']">
		<link rel="alternate" href="{@href}?http-accept-header={@type}">
			<xsl:copy-of select="@*"/>
		</link>
	</xsl:template>
	
	<xsl:template match="//link[@rel='alternate']|//xhtml:link[@rel='alternate']" mode="body">
		<xsl:variable name="icon">
			<xsl:call-template name="iconname">
				<xsl:with-param name="mimetype" select="@type"></xsl:with-param>
			</xsl:call-template>
		</xsl:variable>
		<div class="mimetype"><img src="{$pathToRoot}resources/tango-icons/22/mimetypes/{$icon}.png" class="mimetype" /><a href="{@href}?http-accept-header={@type}"><xsl:value-of select="@title"/></a></div>
	</xsl:template>
	
	<xsl:template name="iconname">
		<xsl:param name="mimetype"/>
		<xsl:choose>
			<xsl:when test="$mimetype='application/pdf'">
				<xsl:text>x-office-document</xsl:text>
			</xsl:when>
			<xsl:when test="$mimetype='text/calendar'">
				<xsl:text>x-office-calendar</xsl:text>
			</xsl:when>
			<xsl:otherwise>
				<xsl:value-of select="translate(translate($mimetype, '/', '-'),'+','-')"/>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>

	<!-- 
		NEVER! NEVER! use alt-f to re-format this code.
	-->
	<xsl:template match="xhtml:div[@class='nifty' or contains(@class, 'nifty ') or contains(@class, ' nifty')]|div[@class='nifty' or contains(@class, 'nifty ') or contains(@class, ' nifty')]">
		<div class="nifty">
			<b class="rtop">
				<b class="r1"><xsl:comment>t</xsl:comment></b>
				<b class="rleft"><xsl:comment>tr</xsl:comment></b>
				<b class="rright"><xsl:comment>tl</xsl:comment></b>
			</b>

			<div class="niftycontent">
				<xsl:apply-templates />
				<br style="clear:both"/>
			</div>

			<b class="rbottom" >
				<b class="r1"><xsl:comment>b</xsl:comment></b>
				<b class="rleft"><xsl:comment>bl</xsl:comment></b>
				<b class="rright"><xsl:comment>br</xsl:comment></b>
			</b>
		</div>
	</xsl:template>

	<!-- look for attributes whose parent element has an
		 attribute us:context with a value that equals the 
		 local name of the processed attribute -->
	<xsl:template match="@*[../@us:rootcontext=local-name(.)]">
		<xsl:attribute name="{local-name(.)}">
			<xsl:value-of select="$pathToRoot" />
			<xsl:value-of select="." />
		</xsl:attribute>
	</xsl:template>

<!-- 
	NEVER! NEVER! use alt-f to re-format this code.
 -->
	<xsl:template match="@*[../@us:context=local-name(.)]">
		<xsl:attribute name="{local-name(.)}">
			<xsl:value-of select="$pathToBlock" />
			<xsl:value-of select="." />
		</xsl:attribute>
	</xsl:template>
	
	<xsl:template match="//div[@hint='replaceWithUserPhoto']">
		<!-- if the size is provided, we must append a / for the correct path name -->
		<xsl:variable name="size">
			<xsl:if test="@size">
				<xsl:value-of select="@size"/><xsl:text>/</xsl:text>
			</xsl:if>
		</xsl:variable>
		<!-- TODO: add real contextPath before /teams  -->
		<img id="{@id}" class="{@class}" src="/teams/users/{$size}{normalize-space($username)}.png" />
	</xsl:template>	
	
</xsl:stylesheet>
