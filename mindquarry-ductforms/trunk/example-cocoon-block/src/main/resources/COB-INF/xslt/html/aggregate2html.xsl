<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	xmlns:i="http://apache.org/cocoon/include/1.0"
	xmlns:db="http://apache.org/cocoon/xmldb/1.0"
	xmlns:x="http://apache.org/cocoon/xmldb/1.0">

	<xsl:template match="/">
		<html>
			<head>
				<title>The Job list</title>
			</head>
			<body>
				<ol>
					<xsl:apply-templates />
				</ol>
				<ul>
					<li>
						<a href="./{//db:new/@name}.xml">post</a>
						a job to the job board
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

	<xsl:template match="*" mode="link">
		<xsl:value-of select="title" />
	</xsl:template>


</xsl:stylesheet>