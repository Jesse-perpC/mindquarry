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
  xmlns:D="DAV:" 
  xmlns:ci="http://apache.org/cocoon/include/1.0"
  xmlns:svndav="http://subversion.tigris.org/xmlns/dav/">
  <xsl:param name="resource" />
  
  <xsl:template match="/D:propfind">
    <D:multistatus>
      <D:response>
        <D:href><xsl:value-of select="$resource"/></D:href>
        <xsl:apply-templates />
      </D:response>
    </D:multistatus>
  </xsl:template>
  
  <xsl:template match="D:prop">
    <D:propstat>
      <D:prop>
        <xsl:apply-templates />
      </D:prop>
      <D:status>HTTP/1.1 200 OK</D:status>
    </D:propstat>
  </xsl:template>
  
	<xsl:template match="@*|node()">
		<xsl:copy>
			<xsl:apply-templates select="@*|node()" />
		</xsl:copy>
	</xsl:template>
</xsl:stylesheet>
