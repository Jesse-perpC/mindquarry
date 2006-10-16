<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	xmlns:xhtml="http://www.w3.org/1999/xhtml"
	xmlns="http://www.w3.org/1999/xhtml"
	xmlns:us="http://www.mindquarry.com/ns/schema/userswitch">

	<xsl:import href="contextpath.xsl"/>
	<xsl:import href="niftify.xsl"/>
	
	<xsl:param name="user.agent" select="''"/>

	<xsl:template match="@*|node()">
		<xsl:copy>
			<xsl:apply-templates select="@*|node()" />
		</xsl:copy>
	</xsl:template>

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

	<xsl:template match="xhtml:html|html">
		<html xmlns="http://www.w3.org/1999/xhtml">
			<xsl:apply-templates />
		</html>
	</xsl:template>

	<xsl:template match="xhtml:head|head">
		<head>
        		<xsl:apply-templates />
                        <link rel="stylesheet" href="{$context.path}blocks/mindquarry-webapp-resources/resources/css/screen.css" media="screen,projection" type="text/css" />
		</head>
	</xsl:template>

	<xsl:template match="xhtml:script[normalize-space(.)='']|script[normalize-space(.)='']">
	              <xsl:copy>
                                <xsl:copy-of select="@*" />
                                <xsl:text>//</xsl:text>
	              </xsl:copy>
	</xsl:template>

	<xsl:template match="xhtml:title|title">
		<title>Mindquarry: <xsl:value-of select="." /></title>
	</xsl:template>

	<xsl:template match="xhtml:body|body">
		<body>
			<div class="body">
				<div id="webapp-header">
        				<ul id="webapp-sections">
						<li><a class="navTalk" href="{$context.path}">Talk</a></li>
						<li><a class="navTasks" href="{$context.path}">Tasks</a></li>
						<li><a class="navWiki" href="{$context.path}">Wiki</a></li>
						<li><a class="navFiles" href="{$context.path}blocks/mindquarry-workspace-block/">Files</a></li>
						<li><a class="navTeams" href="{$context.path}blocks/mindquarry-teamspace-block/">Teams</a></li>
					</ul>
				</div>
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
				<div id="webapp-footer">
					<ul id="webapp-footer-sections">
						<li><a href="{$context.path}">Home</a></li>
						<li><a href="{$context.path}blocks/mindquarry-teamspace-block/">Teams</a></li>
						<li><a href="{$context.path}blocks/mindquarry-workspace-block/">Files</a></li>
						<li><a href="{$context.path}">Wiki</a></li>
						<li><a href="{$context.path}">Tasks</a></li>
						<li><a href="{$context.path}">Talk</a></li>
						<li><a href="{$context.path}/blocks/mindquarry-help-block/">Help</a></li>
						<li><a href="http://www.mindquarry.com">Visit Mindquarry.com</a></li>
						<li><a href="http://www.mindquarry.com/support/">Get Support</a></li>
					</ul>
				</div>
			</div>
		</body>
	</xsl:template>

</xsl:stylesheet>
