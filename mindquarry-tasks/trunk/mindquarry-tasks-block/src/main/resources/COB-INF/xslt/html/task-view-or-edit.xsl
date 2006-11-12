<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	xmlns:xlink="http://www.w3.org/1999/xlink">

	<xsl:import href="block:/xslt/contextpath.xsl" />

	<xsl:param name="viewDocumentLink" />
	<xsl:param name="editDocumentLink" />
	
	<xsl:variable name="taskTitle">
		<xsl:choose>
			<xsl:when test="/html/body/form/@state='output'">
				Details for task: 
			</xsl:when>
			<xsl:otherwise>
				Editing task: 
			</xsl:otherwise>
		</xsl:choose>
		<xsl:value-of select="/html/head/title" />
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
			<xsl:choose>
				<xsl:when test="/html/body/form/@state='output'">
					<link rel="stylesheet"
						href="{$pathToBlock}css/task-view.css" media="screen,projection"
						type="text/css" />
				</xsl:when>
				<xsl:otherwise>
					<link rel="stylesheet"
						href="{$pathToBlock}css/task-edit.css" media="screen,projection"
						type="text/css" />
				</xsl:otherwise>
			</xsl:choose>
		</head>
	</xsl:template>

	<xsl:template match="title">
		<title>
			<xsl:value-of select="$taskTitle" />
		</title>
	</xsl:template>

	<xsl:template match="body">
		<body>
			<xsl:choose>
				<xsl:when test="/html/body/form/@state='output'">
					<h1>
						<xsl:value-of select="$taskTitle" />
					</h1>
						<div id="actions" class="nifty">
							<a class="edit_task_button"
								href="{$editDocumentLink}">
								Edit Task
							</a>
						</div>
						<div class="nifty">
						<xsl:apply-templates/>
						</div>
						<div id="footbar" class="nifty">
							<a id="back" href="./"
								title="go back to task overview">
								Back to tasks list
							</a>
						</div>
				</xsl:when>
				<xsl:otherwise>
					<h1>
						<xsl:value-of select="$taskTitle" />
					</h1>

					<div class="nifty">
						<xsl:apply-templates select="form" />
					</div>
					
					<div class="nifty">
						<a href="." id="back" title="back to teamspace overview">
							Back to overview</a>				
					</div>
				</xsl:otherwise>
			</xsl:choose>
		</body>
	</xsl:template>

	<xsl:template match="form/@action">
		<xsl:attribute name="action">
			<xsl:value-of select="$viewDocumentLink" />
		</xsl:attribute>
	</xsl:template>

</xsl:stylesheet>
