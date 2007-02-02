<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:saxon="http://icl.com/saxon" xmlns:lxslt="http://xml.apache.org/xslt" xmlns:redirect="http://xml.apache.org/xalan/redirect" xmlns:exsl="http://exslt.org/common" xmlns:doc="http://nwalsh.com/xsl/documentation/1.0" xmlns="http://www.w3.org/1999/xhtml" version="1.0" exclude-result-prefixes="doc" extension-element-prefixes="saxon redirect lxslt exsl">
  <xsl:import href="docbook/xhtml/docbook.xsl"/>
  <xsl:import href="docbook/xhtml/chunk-common.xsl"/>
  <xsl:include href="docbook/xhtml/manifest.xsl"/>
  
  <!-- Why is chunk-code now xsl:included?
  
  Suppose you want to customize *both* the chunking algorithm used *and* the
  presentation of some elements that may be chunks. In order to do that, you
  must get the order of imports "just right". The answer is to make your own
  copy of this file, where you replace the initial import of "docbook.xsl"
  with an import of your own base.xsl (that does its own import of docbook.xsl).
  
  Put the templates for changing the presentation of elements in your base.xsl.
  
  Put the templates that control chunking after the include of chunk-code.xsl.
  
  Voila! (Man I hope we can do this better in XSLT 2.0)
  
  -->
  
  <xsl:include href="docbook/xhtml/chunk-code.xsl"/>

  <xsl:param name="desiredfilename" select="'index.html'" />
  <xsl:param name="writerealfiles" select="0" />
  <xsl:param name="chunk.fast" select="1"/>
  <xsl:param name="chunk.section.depth" select="1"/>
  <xsl:param name="chunk.first.sections" select="1"/>
  <xsl:param name="admon.graphics" select="1"/>
  <xsl:param name="admon.graphics.path">../../images/docbook/</xsl:param>
  <xsl:param name="footer.rule" select="0"/>
  <xsl:param name="header.rule" select="0"/>
  <xsl:param name="navig.graphics.extension" select="'.png'"/>
  <xsl:param name="navig.graphics" select="1"/>
  <xsl:param name="navig.graphics.path">../../images/docbook/</xsl:param>
  <xsl:param name="use.id.as.filename" select="'1'"/>
  
<xsl:param name="suppress.header.navigation">1</xsl:param>
  
  <xsl:template match="set|book|part|preface|chapter|appendix|article|reference|refentry|book/glossary|article/glossary|part/glossary|book/bibliography|article/bibliography|part/bibliography|colophon">
  <xsl:variable name="chunkfn">
    <xsl:apply-templates mode="chunk-filename" select="."/>
  </xsl:variable>
    <xsl:choose>
      <xsl:when test="$onechunk != 0 and parent::*">
        <xsl:apply-imports/>
      </xsl:when>
      <xsl:when test="$desiredfilename=$chunkfn">
          <xsl:call-template name="process-chunk-element"/>
      </xsl:when>
      <xsl:otherwise>
        <xsl:apply-templates />
      </xsl:otherwise>
    </xsl:choose>
</xsl:template>

<xsl:template match="sect1|sect2|sect3|sect4|sect5|section">
  <xsl:variable name="ischunk">
    <xsl:call-template name="chunk"/>
  </xsl:variable>
  <xsl:variable name="chunkfn">
        <xsl:apply-templates mode="chunk-filename" select="."/>
      </xsl:variable>
  <xsl:choose>
    <xsl:when test="not(parent::*)">
      <xsl:call-template name="process-chunk-element"/>
    </xsl:when>
    <xsl:when test="$ischunk = 0">
      <xsl:if test="$desiredfilename=$chunkfn">
        <xsl:apply-imports/>
      </xsl:if>
    </xsl:when>
    <xsl:otherwise>
      <xsl:if test="$desiredfilename=$chunkfn">
        <xsl:call-template name="process-chunk-element"/>
      </xsl:if>
    </xsl:otherwise>
  </xsl:choose>
</xsl:template>

  
<xsl:template name="write.chunk">
  <xsl:param name="filename" select="''"/>
  <xsl:param name="quiet" select="$chunker.output.quiet"/>
  <xsl:param name="suppress-context-node-name" select="0"/>
  <xsl:param name="message-prolog"/>
  <xsl:param name="message-epilog"/>

  <xsl:param name="method" select="$chunker.output.method"/>
  <xsl:param name="encoding" select="$chunker.output.encoding"/>
  <xsl:param name="indent" select="$chunker.output.indent"/>
  <xsl:param name="omit-xml-declaration" select="$chunker.output.omit-xml-declaration"/>
  <xsl:param name="standalone" select="$chunker.output.standalone"/>
  <xsl:param name="doctype-public" select="$chunker.output.doctype-public"/>
  <xsl:param name="doctype-system" select="$chunker.output.doctype-system"/>
  <xsl:param name="media-type" select="$chunker.output.media-type"/>
  <xsl:param name="cdata-section-elements" select="$chunker.output.cdata-section-elements"/>

  <xsl:param name="content"/>
  <xsl:copy-of select="$content" />
</xsl:template>
  
</xsl:stylesheet>
