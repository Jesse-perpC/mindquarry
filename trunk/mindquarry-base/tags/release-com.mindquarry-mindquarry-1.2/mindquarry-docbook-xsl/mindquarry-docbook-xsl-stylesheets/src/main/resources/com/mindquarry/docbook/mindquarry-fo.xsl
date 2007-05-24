<?xml version="1.0"?>
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
    xmlns:fo="http://www.w3.org/1999/XSL/Format" 
    xmlns:xlink='http://www.w3.org/1999/xlink'>
  
  <xsl:import href="resource://net/sourceforge/docbook/fo/profile-docbook.xsl"/>
	
  <!-- this is a very basic stylesheet customization layer -->
  <xsl:param name="body.font.family" select="'Helvetica'"/>
  <xsl:param name="title.font.family" select="'Helvetica'"/>
  <xsl:param name="body.font-master" select="12"/>
  
  <xsl:param name="fop.extensions" select="0"/>
  <xsl:param name="paper.type" select="'A4'"/>

  <xsl:param name="admon.graphics" select="1"/>
  <xsl:param name="admon.graphics.path">resource://net/sourceforge/docbook/images/</xsl:param>
  <xsl:param name="callout.graphics.path" select="'resource://net/sourceforge/docbook/images/callouts/'"/>
  <xsl:param name="ulink.footnotes" select="0"/>
  <xsl:param name="ulink.show" select="1"/>
  
  <xsl:attribute-set name="verbatim.properties">
    <xsl:attribute name="space-before.minimum">0.8em</xsl:attribute>
    <xsl:attribute name="space-before.optimum">1em</xsl:attribute>
    <xsl:attribute name="space-before.maximum">1.2em</xsl:attribute>
    <xsl:attribute name="space-after.minimum">0.8em</xsl:attribute>
    <xsl:attribute name="space-after.optimum">1em</xsl:attribute>
    <xsl:attribute name="space-after.maximum">1.2em</xsl:attribute>
    <xsl:attribute name="hyphenate">false</xsl:attribute>
    <xsl:attribute name="wrap-option">no-wrap</xsl:attribute>
    <xsl:attribute name="white-space-collapse">false</xsl:attribute>
    <xsl:attribute name="white-space-treatment">preserve</xsl:attribute>
    <xsl:attribute name="linefeed-treatment">preserve</xsl:attribute>
    <xsl:attribute name="text-align">start</xsl:attribute>
    <xsl:attribute name="background-color">#eeeeee</xsl:attribute>
    <xsl:attribute name="border-color">#dddddd</xsl:attribute>
    <xsl:attribute name="border-width">0.5pt</xsl:attribute>
  </xsl:attribute-set>

</xsl:stylesheet>
