<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	xmlns:i="http://apache.org/cocoon/include/1.0"
	xmlns:db="http://apache.org/cocoon/xmldb/1.0"
	xmlns:x="http://apache.org/cocoon/xmldb/1.0">

	<xsl:template match="/">
		<html>
			<head>
				<title>Saved Wiki Documents</title>
			</head>
			<body>
				<ol>
					<xsl:apply-templates />
				</ol>
				<ul>
					<li>
						<a href="./{//db:new/@name}.xml">create</a> a new document
					</li>
				</ul>
			</body>
		</html>
	</xsl:template>
	
	<xsl:template match="included">
		<li>
			<a href="{@path}">
				<xsl:apply-templates mode="link" />
			</a>
		</li>
	</xsl:template>
	
	<xsl:template match="included[.//archived='true']" />

	<xsl:template match="*" mode="link">
		<xsl:value-of select="title" />
	</xsl:template>


</xsl:stylesheet>