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
	xmlns:source="http://apache.org/cocoon/source/1.0">

	<xsl:param name="path" />
	<xsl:param name="subscribe-email" />
	<xsl:param name="subscribe-jabber" />
	<xsl:param name="unsubscribe-email" />
	<xsl:param name="unsubscribe-jabber" />
	<xsl:param name="unsubscribe-all" />

	<!-- the root element should be wrapped into a source:write directive -->
	<xsl:template match="/">
		<source:write create="true">
			<source:source><xsl:value-of select="$path" /></source:source>
			<source:fragment>
				<team>
					<xsl:apply-templates select="team/*"/>
					<xsl:if test="$subscribe-email">
						<subscriber type="email"><xsl:value-of select="$subscribe-email"></xsl:value-of></subscriber>
					</xsl:if>
					<xsl:if test="$subscribe-jabber">
						<subscriber type="jabber"><xsl:value-of select="$subscribe-jabber"></xsl:value-of></subscriber>
					</xsl:if>
				</team>
			</source:fragment>
		</source:write>
	</xsl:template>
	
	<xsl:template match="subscriber[@type='email'][text()=$subscribe-email]" />
	<xsl:template match="subscriber[@type='email'][text()=$unsubscribe-email]" />

	<xsl:template match="subscriber[@type='jabber'][text()=$subscribe-jabber]" />
	<xsl:template match="subscriber[@type='jabber'][text()=$unsubscribe-jabber]" />

	<xsl:template match="subscriber[text()=$unsubscribe-all]" />

	<xsl:template match="@*|node()">
		<xsl:copy>
			<xsl:apply-templates select="@*|node()" />
		</xsl:copy>
	</xsl:template>
</xsl:stylesheet>
