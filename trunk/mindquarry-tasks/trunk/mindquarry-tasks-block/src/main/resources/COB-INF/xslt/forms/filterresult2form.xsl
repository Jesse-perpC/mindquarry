<?xml version="1.0"?>
<xsl:stylesheet version="1.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

	<xsl:param name="taskID" select="''" />
	<xsl:param name="teamspaceID" select="''" />

	<xsl:template match="@*|node()"/>

	<xsl:template match="task">
		<item>
			<resultLink>
				../<xsl:value-of select="$taskID" />
			</resultLink>
			<xsl:apply-templates />
		</item>
	</xsl:template>

	<xsl:template match="title">
		<resultTitle>
			<xsl:value-of select="normalize-space(.)" />
		</resultTitle>
	</xsl:template>

	<xsl:template match="status">
		<resultStatus>
			<xsl:value-of select="normalize-space(.)" />
		</resultStatus>
	</xsl:template>

	<xsl:template match="summary">
		<resultSummary>
			<xsl:value-of select="normalize-space(.)" />
		</resultSummary>
	</xsl:template>
</xsl:stylesheet>
