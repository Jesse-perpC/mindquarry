<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	xmlns:xhtml="http://www.w3.org/1999/xhtml"
	xmlns="http://www.w3.org/1999/xhtml"
	xmlns:wa="http://www.mindquarry.com/ns/schema/webapp"
	xmlns:us="http://www.mindquarry.com/ns/schema/userswitch">

<!-- 
	NEVER! NEVER! use alt-f to re-format this code.
 -->
	<xsl:import href="contextpath.xsl"/>
	<xsl:import href="niftify.xsl"/>
	<xsl:import href="lightbox.xsl"/>
	
	<xsl:param name="user.agent" select="''"/>

	<xsl:param name="cssPath" select="'css/'" />
	<xsl:param name="scriptPath" select="'scripts/'" />

	<xsl:template match="@*|node()">
		<xsl:copy>
			<xsl:apply-templates select="@*|node()" />
		</xsl:copy>
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
		<html xmlns="http://www.w3.org/1999/xhtml">
			<xsl:apply-templates />
		</html>
	</xsl:template>

	<xsl:template match="xhtml:head|head">
		<head>
			<xsl:apply-templates select="." mode="nifty" />
			
			<link rel="stylesheet" href="{$pathToBlock}{$cssPath}screen.css" media="screen,projection" type="text/css" />
			<link rel="stylesheet" href="{$pathToBlock}{$cssPath}headerandlines.css" media="screen,projection" type="text/css" />

			<xsl:apply-templates />
			
			<xsl:apply-templates select="." mode="lightbox" />

		</head>
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
                                <xsl:text>
</xsl:text>
	              </xsl:copy>
	</xsl:template>

<!-- 
	NEVER! NEVER! use alt-f to re-format this code.
 -->
	<xsl:template match="xhtml:title|title">
		<title>Mindquarry: <xsl:value-of select="." /></title>
	</xsl:template>

	<xsl:template match="xhtml:body|body">
		<body>
			<div class="body">
				<!-- layouting the header -->
				<div id="webapp-header">
        			<ul id="webapp-sections">
						<li><a class="navTalk" href="{$pathToRoot}talk/">Talk</a></li>
						<li><a class="navTasks" href="{$pathToRoot}tasks/">Tasks</a></li>
						<li><a class="navWiki" href="{$pathToRoot}wiki/">Wiki</a></li>
						<li><a class="navFiles" href="{$pathToRoot}workspace/">Files</a></li>
						<li><a class="navTeams" href="{$pathToRoot}teamspace/">Teams</a></li>
					</ul>
					
					<div id="beta-comment">Alpha 1</div>
				</div>
				
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
						<li><a href="{$pathToRoot}talk/">Talk</a></li>
						<li><a href="{$pathToRoot}help/">Help</a></li>
						<li><a href="http://www.mindquarry.com">Visit Mindquarry.com</a></li>
						<li><a href="http://www.mindquarry.com/support/">Get Support</a></li>
					</ul>
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
			<div class="content">
				<xsl:apply-templates />
			</div>
			<b class="rbottom">
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
