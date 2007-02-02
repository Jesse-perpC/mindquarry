<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:saxon="http://icl.com/saxon" xmlns:lxslt="http://xml.apache.org/xslt" xmlns:redirect="http://xml.apache.org/xalan/redirect" xmlns:exsl="http://exslt.org/common" xmlns:doc="http://nwalsh.com/xsl/documentation/1.0" xmlns="http://www.w3.org/1999/xhtml" version="1.0" exclude-result-prefixes="doc" extension-element-prefixes="saxon redirect lxslt exsl">
  
  <xsl:import href="docbook2html.xsl"/>
  
  <xsl:param name="document" select="''" />
  <xsl:param name="basepath" select="''" />

  <xsl:param name="admon.graphics.path">/help/images/docbook/</xsl:param>
  <xsl:param name="navig.graphics.path">/help/images/docbook/</xsl:param>
  <xsl:param name="use.id.as.filename" select="'1'"/>
  <xsl:template name="href.target">
    <xsl:param name="context" select="."/>
    <xsl:param name="object" select="."/>
  
    <xsl:variable name="href.to.uri">
      <xsl:call-template name="href.target.uri">
        <xsl:with-param name="object" select="$object"/>
      </xsl:call-template>
    </xsl:variable>
  
    <xsl:variable name="href.from.uri">
      <xsl:call-template name="href.target.uri">
        <xsl:with-param name="object" select="$context"/>
      </xsl:call-template>
    </xsl:variable>
    
    <!--
    <xsl:message>href.to.uri: <xsl:value-of select="$href.to.uri"/></xsl:message>
    <xsl:message>href.from.uri: <xsl:value-of select="$href.from.uri"/></xsl:message>
    -->
  
    <xsl:variable name="href.to">
      <xsl:call-template name="trim.common.uri.paths">
        <xsl:with-param name="uriA" select="$href.to.uri"/>
        <xsl:with-param name="uriB" select="$href.from.uri"/>
        <xsl:with-param name="return" select="'A'"/>
      </xsl:call-template>
    </xsl:variable>
  
    <xsl:variable name="href.from">
      <xsl:call-template name="trim.common.uri.paths">
        <xsl:with-param name="uriA" select="$href.to.uri"/>
        <xsl:with-param name="uriB" select="$href.from.uri"/>
        <xsl:with-param name="return" select="'B'"/>
      </xsl:call-template>
    </xsl:variable>
  
    <xsl:variable name="depth">
      <xsl:call-template name="count.uri.path.depth">
        <xsl:with-param name="filename" select="$href.from"/>
      </xsl:call-template>
    </xsl:variable>
  
    <xsl:variable name="href">
      <xsl:call-template name="copy-string">
        <xsl:with-param name="string" select="'../'"/>
        <xsl:with-param name="count" select="$depth"/>
      </xsl:call-template>
      <xsl:value-of select="$href.to"/>
    </xsl:variable>
  
    <!--
    <xsl:message>
      <xsl:text>In </xsl:text>
      <xsl:value-of select="name(.)"/>
      <xsl:text> (</xsl:text>
      <xsl:value-of select="$href.from"/>
      <xsl:text>,</xsl:text>
      <xsl:value-of select="$depth"/>
      <xsl:text>) </xsl:text>
      <xsl:value-of select="name($object)"/>
      <xsl:text> href=</xsl:text>
      <xsl:value-of select="$href"/>
    </xsl:message>
    -->
    <xsl:value-of select="$basepath" />
    <xsl:value-of select="$document" />
    <xsl:text>/</xsl:text>
    <xsl:value-of select="$href"/>
  </xsl:template>
  
</xsl:stylesheet>
