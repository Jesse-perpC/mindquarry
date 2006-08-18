<?xml version="1.0" encoding="ISO-8859-1"?>
<xsl:stylesheet version="1.0"
xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

<xsl:template match="/">
	<html>
		<head><title>Demo</title></head>
		<body>
			<xsl:apply-templates />
		</body>
	</html>
</xsl:template>

<xsl:template match="module">
	<h1>This is <xsl:apply-templates/> speaking:</h1>
</xsl:template>

<xsl:template match="spring">
<blockquote><xsl:apply-templates /> times.</blockquote>
</xsl:template>
</xsl:stylesheet>
