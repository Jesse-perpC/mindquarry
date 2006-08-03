<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

	<xsl:template match="/repository">
		<html>
			<head>
				<title>
					<xsl:value-of select="url" />
				</title>
			</head>
			<body>
				<xsl:apply-templates />
			</body>
		</html>
	</xsl:template>

	<xsl:template match="entries">
		<ul>
			<xsl:apply-templates />
		</ul>
	</xsl:template>

	<xsl:template match="entry">
		<li>
			<xsl:value-of select="name" />
			(
			<xsl:value-of select="author" />
			:
			<xsl:value-of select="revision" />
			)
		</li>
	</xsl:template>
</xsl:stylesheet>
