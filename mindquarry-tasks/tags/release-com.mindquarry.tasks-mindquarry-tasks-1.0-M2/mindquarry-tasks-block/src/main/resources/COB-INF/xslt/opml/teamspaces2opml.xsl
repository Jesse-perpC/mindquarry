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
	xmlns:xlink="http://www.w3.org/1999/xlink">

	<xsl:import href="servlet:/xslt/contextpath.xsl" />
    <xsl:param name="serverTitle" />
    <xsl:param name="base" />
		
	<xsl:template match="/teamspaces">
    <opml version="1.0">
      <head>
        <title>Tasks for <xsl:value-of select="$serverTitle" /></title>
      </head>
      <body>
        <xsl:apply-templates select="teamspace">
          <xsl:sort select="name" />
        </xsl:apply-templates>
      </body>
    </opml>
	</xsl:template>
				 
	<xsl:template match="teamspace" >
    <outline text="{normalize-space(name)}" 
      htmlUrl="{$base}{normalize-space(id)}/all.changes/" 
      xmlUrl="{$base}{normalize-space(id)}/all.changes/?http-accept-header=application/atom+xml"/>
	</xsl:template>
  
</xsl:stylesheet>
