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
	<xsl:import href="lightbox.xsl"/>
	
	<xsl:output indent="no"/>

	<!-- external parameters -->
	<xsl:param name="user.agent" select="''"/>
	<xsl:param name="username" select="''"/>
	<xsl:param name="version" select="'undefined'"/>
	
	<xsl:param name="cssPath" select="'css/'" />
	<xsl:param name="scriptPath" select="'scripts/'" />

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
			<script type="text/javascript">djConfig = { isDebug: false };</script>
			<script type="text/javascript" src="{$pathToRoot}resources/_cocoon/resources/dojo/dojo.js" >//</script>
			<link rel="stylesheet" href="{$pathToBlock}{$cssPath}screen.css" media="screen,projection" type="text/css" />
			<link rel="stylesheet" href="{$pathToBlock}{$cssPath}headerandlines.css" media="screen,projection" type="text/css" />
			<link rel="icon" href="{$pathToRoot}resources/icons/logo-red-gradient-256-colors.ico" type="image/x-icon" />

			<xsl:apply-templates />
			<xsl:apply-templates select="." mode="lightbox" />
			
			<script type="text/javascript" src="{$pathToBlock}{$scriptPath}login.js" >//</script>
			<script type="text/javascript" src="{$pathToBlock}{$scriptPath}dojoutils.js" >//</script>
			<script type="text/javascript">
				dojo.require("mindquarry.widget.QuickSearch");
				dojo.require("mindquarry.widget.TeamSwitcher");
			</script>
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
		<title><xsl:value-of select="." /> - Mindquarry</title>
	</xsl:template>

	<xsl:template match="xhtml:body|body">
		<body>
			<div class="body">
				<!-- layouting the header -->
				<div id="webapp-header">
					<ul id="webapp-sections">
						<li><a class="navTeams" href="{$pathToRoot}teamspace/">Teams</a></li>
						<li><a class="navFiles" href="{$pathToRoot}workspace/{$teamspaceNameWithBrowse}">Files</a></li>
						<li><a class="navWiki" href="{$pathToRoot}wiki/{$teamspaceNameWithSlash}">Wiki</a></li>
						<li><a class="navTasks" href="{$pathToRoot}tasks/{$teamspaceNameWithSlash}">Tasks</a></li>
						<!--li><a class="navTalk" href="{$pathToRoot}talk/">Talk</a></li-->
					</ul>

					<!-- test if user is logged in -->				
					<xsl:if test="string-length($username) > 0">
						<!-- make logging status widget -->
						<div id="user-status-background">
							<xsl:text> </xsl:text>
						</div>
						<div id="user-status">
							<div id="http-logout-hint">(Close browser to logout)</div>
							<a href="{$pathToRoot}" id="path-to-webapp-root" title="logout"/>
							<div class="username-display">
								<a href="{$pathToRoot}teamspace/editUser/?targetUri={$pathToRoot}" id="edit-user-link" title="edit settings for {$username}" rel="lightbox"><xsl:value-of select="$username" /></a>
							</div>
							<a href="{$pathToRoot}/help/" id="head-help-link">Help</a>													
						</div>
					</xsl:if>
				</div>
				
				<div title="{$teamspaceName}" dojoType="TeamSwitcher">
					<a href="{$pathToTeamspaceBase}" id="teamspace-base-link">Teamspace Base</a>
					<include src="block:teams:/user/{$username}/info" xmlns="http://apache.org/cocoon/include/1.0"/>
				</div>	
				
				<!-- test if user is logged in -->
				<xsl:if test="string-length($username) > 0">
					<!-- make quick search widget -->
					<div class="quicksearch" dojoType="QuickSearch" url="/solr-select/" size="20" maxheight="300">&#160;</div>
				</xsl:if>

				<!-- layouting the content -->
				<div id="webapp-content">
					<div id="background-repeater">
						<div id="background-lines">
							<div id="background-n">
								<div id="background-s">
									<div id="background-w">
										<div id="background-e">
											<div id="background-nw">
												<div id="background-ne">
													<div id="background-sw">
														<div id="background-se">
															<div id="innercontent">
																<xsl:apply-templates />
															</div>
														</div>
													</div>
												</div>
											</div>
										</div>
									</div>
								</div>
							</div>
						</div>
					</div>
				</div>
				
				<!-- layouting the footer -->
				<div id="webapp-footer">
					<ul id="webapp-footer-sections">
						<li><a href="{$pathToRoot}">Home</a></li>
						<li><a href="{$pathToRoot}teamspace/">Teams</a></li>
						<li><a href="{$pathToRoot}workspace/">Files</a></li>
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
					</ul>
					<span class="version-footer">Mindquarry Version <xsl:value-of select="$version"/></span>
				</div>
			</div>
		</body>
	</xsl:template>

	<!-- 
		NEVER! NEVER! use alt-f to re-format this code.
	-->
	<xsl:template match="xhtml:div[@class='nifty']|div[@class='nifty']">
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
</xsl:stylesheet>
