<?xml version="1.0"?>
<xsl:stylesheet version="1.0" 
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform" 
	xmlns:fi="http://apache.org/cocoon/forms/1.0#instance"
	xmlns:html="http://www.w3.org/1999/xhtml"
	exclude-result-prefixes="fi #default html">

  	<xsl:template match="@*|node()">
		<xsl:copy>
			<xsl:apply-templates select="@*|node()" />
		</xsl:copy>
	</xsl:template>
	

	<xsl:template match="htmllize">
		<xsl:copy-of select="html/body/node()" />
	</xsl:template>
	
	
  

</xsl:stylesheet>