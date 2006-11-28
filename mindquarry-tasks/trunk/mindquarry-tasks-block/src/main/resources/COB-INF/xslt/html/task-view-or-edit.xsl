<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	xmlns:xlink="http://www.w3.org/1999/xlink">

	<xsl:import href="block:/xslt/contextpath.xsl" />

	<xsl:param name="viewDocumentLink" />
	
	<xsl:variable name="taskTitle">
		Task: <xsl:value-of select="/html/head/title" />
	</xsl:variable>

	<xsl:template match="@*|node()">
		<xsl:copy>
			<xsl:apply-templates select="@*|node()" />
		</xsl:copy>
	</xsl:template>

	<xsl:template match="head">
		<head>			
			<!-- copy existing link/script stuff -->
			<xsl:apply-templates />

			<link rel="stylesheet"
				href="{$pathToBlock}css/tasks.css" media="screen,projection"
				type="text/css" />
			<link rel="stylesheet"
				href="{$pathToBlock}css/task-edit.css" media="screen,projection"
				type="text/css" />
		</head>
	</xsl:template>

	<xsl:template match="title">
		<title>
			<xsl:value-of select="$taskTitle" />
		</title>
	</xsl:template>

	<xsl:template match="body">
		<body>
			<h1>
				<xsl:value-of select="$taskTitle" />
			</h1>

			<div class="nifty">
				<xsl:apply-templates select="form" />
			</div>
			
			<div class="nifty">
				<a href="." id="back" title="back to teamspace overview">
					Back to tasks list</a>				
			</div>
		</body>
	</xsl:template>

	<xsl:template match="form/@action">
		<xsl:attribute name="action">
			<xsl:value-of select="$viewDocumentLink" />
		</xsl:attribute>
	</xsl:template>

</xsl:stylesheet>
