<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	xmlns:xhtml="http://www.w3.org/1999/xhtml"
	exclude-result-prefixes="xhtml">
	<xsl:param name="page" />
	
	<xsl:template match="/*">
		<html>
			<head>
				<title>
					<xsl:value-of select="title" />
				</title>
			</head>
			<body>
				<xsl:apply-templates select="description" />

				<ul>
					<li>
						<a href="./">back</a>
						to the list of posted jobs
					</li>
					<li>
						<a href="{$page}.xml.edit">edit</a>
						this page
					</li>
				</ul>
			</body>
		</html>
	</xsl:template>

	<xsl:template match="description[.//body/*]">
		<div>
			<xsl:copy-of select=".//body/node()" />
		</div>
	</xsl:template>

	<xsl:template match="description">
		<pre>
			<xsl:apply-templates />
		</pre>
	</xsl:template>
</xsl:stylesheet>