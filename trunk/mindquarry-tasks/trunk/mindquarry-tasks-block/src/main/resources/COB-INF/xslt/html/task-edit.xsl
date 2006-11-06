<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0"
        xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
		xmlns:xhtml="http://www.w3.org/1999/xhtml"
        xmlns:xlink="http://www.w3.org/1999/xlink">
        
	<xsl:import href="block:/xslt/contextpath.xsl" />
		
	<xsl:template match="@*|node()">
		<xsl:copy>
			<xsl:apply-templates select="@*|node()" />
		</xsl:copy>
	</xsl:template>
	
	<xsl:template match="xhtml:head|head">
		<head>
			<link rel="stylesheet" href="{$pathToBlock}css/tasks-edit.css" media="screen,projection" type="text/css" />
		</head>
	</xsl:template>

</xsl:stylesheet>