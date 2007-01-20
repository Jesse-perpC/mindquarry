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
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

	<xsl:import href="teamspace2htmlutils.xsl"/>
  
  <xsl:template match="teamspace" mode="title">
    <title><xsl:value-of select="name"/></title>
  </xsl:template>
  
  <xsl:template match="teamspace" mode="heading">
    <h1>Manage Teamspace <xsl:value-of select="name"/></h1>
    
    <xsl:if test="$username = 'admin'" >
      <xsl:call-template name="create_user_button" />
    </xsl:if>
  </xsl:template>
  
  <xsl:template match="name">
    <h2 class="name">
      <xsl:apply-templates />
    </h2>
  </xsl:template>
</xsl:stylesheet>
