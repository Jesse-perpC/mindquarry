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
  xmlns:ci="http://apache.org/cocoon/include/1.0"
  xmlns:email="http://apache.org/cocoon/transformation/sendmail"
	xmlns:source="http://apache.org/cocoon/source/1.0">

	<xsl:param name="path" />
  <xsl:param name="metapath" />
  <xsl:param name="queryroot" />
  <xsl:param name="fromemail" />
  <xsl:param name="smtphost" />

	<!-- the root element should be wrapped into a source:write directive -->
	<xsl:template match="/">
    <data>
      <source:write create="true">
        <source:source><xsl:value-of select="$path" /></source:source>
        <source:fragment>
          <xsl:apply-templates />
        </source:fragment>
      </source:write>
      <email:sendmail>
        <ci:include src="{$metapath}" />
        <ci:include src="{$queryroot}" />
        <email:from><xsl:value-of select="$fromemail" /></email:from>
        <email:smtphost><xsl:value-of select="$smtphost" /></email:smtphost>
        <email:smtpport>25</email:smtpport>
        <email:body mime-type="text/plain"><xsl:apply-templates select="message/body/text()" /></email:body>
      </email:sendmail>
    </data>
	</xsl:template>
  
	<xsl:template match="@*|node()">
		<xsl:copy>
			<xsl:apply-templates select="@*|node()" />
		</xsl:copy>
	</xsl:template>
</xsl:stylesheet>
