<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	xmlns:xlink="http://www.w3.org/1999/xlink"
	xmlns:xi="http://www.w3.org/2001/XInclude">

	<xsl:param name="base" />

	<!-- This stylesheet will replace the root element
		named "teamspaces" with a new root element "tasks"
		and add includes for the list of tasks per teamspace -->

	<xsl:template match="/teamspaces">
		<tasks xml:base="{$base}">
			<xsl:apply-templates />
		</tasks>
	</xsl:template>

	<xsl:template match="teamspace">
		<teamspace xlink:href="{id}">
			<name><xsl:value-of select="name" /></name>
			<description><xsl:value-of select="description" /></description>
			<xi:include href="cocoon:/internal/pipe/{id}/list.xml" />
		</teamspace>
	</xsl:template>

</xsl:stylesheet>