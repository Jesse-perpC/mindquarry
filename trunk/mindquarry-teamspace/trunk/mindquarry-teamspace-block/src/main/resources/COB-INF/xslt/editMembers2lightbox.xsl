<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns="http://www.w3.org/1999/xhtml"
	xmlns:xhtml="http://www.w3.org/1999/xhtml"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

	<xsl:template match="/html">
		<xsl:copy-of select="//div[@class='edit-members']" />
	</xsl:template>
	
	<xsl:template match="/xhtml:html">
		<xsl:copy-of select="//xhtml:div[@class='edit-members']" />
	</xsl:template>
</xsl:stylesheet>
