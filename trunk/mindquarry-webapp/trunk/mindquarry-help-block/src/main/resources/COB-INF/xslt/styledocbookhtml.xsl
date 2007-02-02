<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:saxon="http://icl.com/saxon" xmlns:lxslt="http://xml.apache.org/xslt" xmlns:redirect="http://xml.apache.org/xalan/redirect" xmlns:exsl="http://exslt.org/common" xmlns:doc="http://nwalsh.com/xsl/documentation/1.0" xmlns:xhtml="http://www.w3.org/1999/xhtml" version="1.0" exclude-result-prefixes="doc" extension-element-prefixes="saxon redirect lxslt exsl xhtml">

  
	<xsl:template match="@*|node()">
		<xsl:copy>
			<xsl:apply-templates select="@*|node()" />
		</xsl:copy>
	</xsl:template>
  
  <xsl:template match="xhtml:body/xhtml:div">
    <div class="nifty">
      <xsl:apply-templates />
    </div>
  </xsl:template>
  
  <xsl:template match="xhtml:p">
    <xsl:if test="ancestor::xhtml:div">
      <xsl:copy>
        <xsl:apply-templates select="@*|node()" />
      </xsl:copy>
    </xsl:if>
  </xsl:template>
  
  <xsl:template match="xhtml:div[@class='book']/xhtml:p" />
</xsl:stylesheet>
