<?xml version="1.0"?>
<xsl:stylesheet version="1.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	xmlns:fi="http://apache.org/cocoon/forms/1.0#instance"
	xmlns:xhtml="http://www.w3.org/1999/xhtml"
	exclude-result-prefixes="fi">

	<xsl:import
		href="resource://com/mindquarry/webapp/xsl/contextpath.xsl" />

	<!-- styling of the page -->
	<xsl:import
		href="resource://org/apache/cocoon/forms/resources/forms-page-styling.xsl" />
	<!-- styling of the widgets -->

	<xsl:import
		href="resource://org/apache/cocoon/forms/resources/forms-advanced-field-styling.xsl" />

	<!-- resources directory for Dojo js, css and the like -->
	<xsl:param name="resources-uri">
		<xsl:value-of select="$context.path" />
		<xsl:text>_cocoon/resources</xsl:text>
	</xsl:param>

	<xsl:template match="head">
		<xsl:copy>
			<xsl:apply-templates select="." mode="forms-page" />
			<xsl:apply-templates select="." mode="forms-field" />
			<xsl:apply-templates />
		</xsl:copy>
	</xsl:template>

	<xsl:template match="body">
		<xsl:copy>
			<xsl:apply-templates select="." mode="forms-page" />
			<xsl:apply-templates select="." mode="forms-field" />
			<xsl:apply-templates />
		</xsl:copy>
	</xsl:template>


	<xsl:template match="li[fi:styling/@type='member-entry']" >
    	<li id="{@id}">
    		<div class="member-entry" style="cursor:pointer;">
    			<xsl:apply-templates select="fi:action" mode="javascript"/>
    			<xsl:apply-templates select="fi:output" />
    		</div>
    	</li>
  </xsl:template>
  
  <xsl:template match="fi:action" mode="javascript">
	<xsl:attribute name="onclick">
		<xsl:text>forms_submitForm(this, '</xsl:text>
		<xsl:value-of select="@id"/>
		<xsl:text>'); return false;</xsl:text>
	</xsl:attribute>
  </xsl:template>
  
  <xsl:template match="fi:output[fi:styling/@type='user-image']">
  	<img alt="{normalize-space(fi:value)}" src="users/{normalize-space(fi:value)}.png"/>
  </xsl:template>
  
</xsl:stylesheet>