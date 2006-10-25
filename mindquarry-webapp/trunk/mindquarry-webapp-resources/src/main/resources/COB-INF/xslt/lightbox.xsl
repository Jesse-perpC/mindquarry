<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	xmlns:xhtml="http://www.w3.org/1999/xhtml"
	xmlns="http://www.w3.org/1999/xhtml"
	xmlns:us="http://www.mindquarry.com/ns/schema/webapp">
	
	<xsl:template match="xhtml:head|head" mode="lightbox">
		<!-- only include the lightbox.(css|js) if there are actually
		lightbox links in the page -->
		<xsl:if test="//xhtml:a[@rel='lightbox']|//a[@rel='lightbox']">
			<link rel="stylesheet" type="text/css" href="{$pathToBlock}{$cssPath}lightbox.css" />
			<script type="text/javascript" src="{$pathToBlock}{$scriptPath}lightbox.js" >//</script>
		</xsl:if>
	</xsl:template>

</xsl:stylesheet>