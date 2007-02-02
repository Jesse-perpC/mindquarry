<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:bu="http://apache.org/cocoon/browser-update/1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:saxon="http://icl.com/saxon" xmlns:lxslt="http://xml.apache.org/xslt" xmlns:redirect="http://xml.apache.org/xalan/redirect" xmlns:exsl="http://exslt.org/common" xmlns:doc="http://nwalsh.com/xsl/documentation/1.0" version="1.0" exclude-result-prefixes="doc" extension-element-prefixes="saxon redirect lxslt exsl">
  
  <xsl:import href="docbook2html.xsl"/>

  <xsl:param name="linkend" select="''" />
  <xsl:param name="document" select="''" />
  <xsl:param name="basepath" select="''" />
  
  <xsl:template match="/">
    <xsl:param name="targets" select="key('id',$linkend)"/>
    <xsl:param name="target" select="$targets[1]"/>
    <bu:redirect>
      <xsl:attribute name="uri">
        <xsl:value-of select="$basepath" />
        <xsl:value-of select="$document" />
        <xsl:text>/</xsl:text>
        <xsl:call-template name="href.target">
            <xsl:with-param name="object" select="$target"/>
          </xsl:call-template>
      </xsl:attribute>
    </bu:redirect>
  </xsl:template>
  
</xsl:stylesheet>
