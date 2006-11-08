<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	xmlns:xhtml="http://www.w3.org/1999/xhtml"
	xmlns:xlink="http://www.w3.org/1999/xlink">

	<xsl:import href="block:/xslt/contextpath.xsl" />

	<xsl:param name="viewDocumentLink" />
	<xsl:param name="editDocumentLink" />

	<xsl:template match="@*|node()">
		<xsl:copy>
			<xsl:apply-templates select="@*|node()" />
		</xsl:copy>
	</xsl:template>

	<xsl:template match="xhtml:head|head">
		<head>
			<!-- copy existing link/script stuff -->
			<xsl:apply-templates />

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

	<xsl:template match="xhtml:body|body">
		<body>
			<xsl:choose>
				<xsl:when test="/html/body/form/@state='output'">
					<h1>
						Details for Task:
						<xsl:value-of select="/html/head/title" />
					</h1>
					<div class="nifty">
						<div id="actions">
							<a class="edit_task_button"
								href="{$editDocumentLink}">
								Edit Task
							</a>
						</div>
			
						<table>
							<xsl:apply-templates select="form//div[@class='form_block']" mode="output"/>
						</table>
			
						<div id="footbar">
							<a id="back" href="./"
								title="go back to task overview">
								Back to tasks list
							</a>
						</div>
					</div>
				</xsl:when>
				<xsl:otherwise>
					<h1>Editing Task "<xsl:value-of select="form//div[@id='block_ductform_title']/span/input/@value" />"</h1>
					<div class="nifty">
						<xsl:apply-templates select="form" />
					</div>
					<a href="{$viewDocumentLink}">Cancel</a>
				</xsl:otherwise>
			</xsl:choose>
		</body>
	</xsl:template>

	<xsl:template match="xhtml:form/@action|form/@action">
		<xsl:attribute name="action">
			<xsl:value-of select="$editDocumentLink" />
		</xsl:attribute>
	</xsl:template>

	<!-- state=output only -->
	<xsl:template
		match="div[@class='form_block' and not(@id='block_ductform_ductforms')]" mode="output">
		<tr>
			<th>
				<xsl:value-of select="label" />
			</th>
			<td>
				<xsl:value-of select="span" />
				<xsl:apply-templates select="div" mode="output"/>
				<xsl:apply-templates select="table" mode="output" />
			</td>
		</tr>
	</xsl:template>
	
	<xsl:template
		match="div[@class='form_block' and (@id='block_ductform_ductforms')]" mode="output" />

</xsl:stylesheet>
