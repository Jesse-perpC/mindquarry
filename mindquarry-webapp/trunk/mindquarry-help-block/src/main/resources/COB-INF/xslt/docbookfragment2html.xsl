<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:saxon="http://icl.com/saxon" xmlns:lxslt="http://xml.apache.org/xslt" xmlns:redirect="http://xml.apache.org/xalan/redirect" xmlns:exsl="http://exslt.org/common" xmlns:doc="http://nwalsh.com/xsl/documentation/1.0" xmlns="http://www.w3.org/1999/xhtml" version="1.0" exclude-result-prefixes="doc" extension-element-prefixes="saxon redirect lxslt exsl">
  
  <xsl:import href="docbook2html.xsl"/>

  <xsl:param name="desiredfilename" select="'index.html'" />
  <xsl:param name="writerealfiles" select="0" />
  <xsl:param name="chunk.fast" select="1"/>
  <xsl:param name="admon.graphics" select="1"/>
  <xsl:param name="admon.graphics.path">/help/images/docbook/</xsl:param>
  <xsl:param name="footer.rule" select="0"/>
  <xsl:param name="header.rule" select="0"/>
  <xsl:param name="navig.graphics.extension" select="'.png'"/>
  <xsl:param name="navig.graphics" select="1"/>
  <xsl:param name="navig.graphics.path">/help/images/docbook/</xsl:param>
  <xsl:param name="use.id.as.filename" select="'1'"/>
  
  <xsl:param name="suppress.header.navigation">1</xsl:param>
  
</xsl:stylesheet>
