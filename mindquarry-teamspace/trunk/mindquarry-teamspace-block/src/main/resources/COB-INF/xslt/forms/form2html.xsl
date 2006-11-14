<?xml version="1.0"?>
<xsl:stylesheet version="1.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	xmlns:fi="http://apache.org/cocoon/forms/1.0#instance"
	xmlns:xhtml="http://www.w3.org/1999/xhtml"
	exclude-result-prefixes="fi">

	<xsl:import
		href="cocoon:/xslt/contextpath.xsl" />

	<!-- styling of the page -->
	<xsl:import
		href="resource://org/apache/cocoon/forms/resources/forms-page-styling.xsl" />
	<!-- styling of the widgets -->

	<xsl:import
		href="resource://org/apache/cocoon/forms/resources/forms-advanced-field-styling.xsl" />

	<!-- resources directory for Dojo js, css and the like -->
	<xsl:param name="resources-uri">
		<xsl:value-of select="$pathToRoot" />
		<xsl:text>resources/_cocoon/resources</xsl:text>
	</xsl:param>

	<xsl:template match="head">
		<xsl:copy>
			<xsl:apply-templates select="." mode="forms-page" />
			<xsl:apply-templates select="." mode="forms-field-mindquarry" />
			<xsl:apply-templates />
		</xsl:copy>
	</xsl:template>
	
  <xsl:template match="head" mode="forms-field-mindquarry">
    <!-- copy any pre-initialization code which can be used e.g. to setup dojo debugging with
         <script> djConfig = {isDebug: true} </script> -->
    <xsl:copy-of select="fi:init/node()"/>
    <script src="{$resources-uri}/ajax/cocoon.js" type="text/javascript"/>
    <script src="{$resources-uri}/forms/js/forms-lib.js" type="text/javascript"/>
    <script type="text/javascript">
        dojo.addOnLoad(forms_onload);
        dojo.require("cocoon.forms.*");
    </script>
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
    			<div class="member-details">
	    			<xsl:apply-templates select="fi:action" mode="javascript"/>
    				<xsl:apply-templates select="fi:output|fi:action|text()" />
    			</div>
    		</div>
    	</li>
  </xsl:template>
  
  <xsl:template match="fi:output[fi:styling/@type='full-name']">
  	<xsl:if test="count(preceding-sibling::fi:output[fi:styling/@type='full-name'])=0">
  		<h4>
  			<xsl:value-of select="fi:value" /><xsl:text> </xsl:text>
  			<xsl:for-each select="following-sibling::fi:output[fi:styling/@type='full-name']">
  				<xsl:value-of select="fi:value" />
  			</xsl:for-each>
  		</h4>
  	</xsl:if>
  </xsl:template>
  
  <xsl:template match="fi:action" mode="javascript">
	<xsl:attribute name="onclick">
		<xsl:text>forms_submitForm(this, '</xsl:text>
		<xsl:value-of select="@id"/>
		<xsl:text>'); return false;</xsl:text>
	</xsl:attribute>
  </xsl:template>
  
  <xsl:template match="fi:output[fi:styling/@type='user-image']">
  	<img alt="image of the user with id {normalize-space(fi:value)}" src="{$pathToRoot}teamspace/users/{normalize-space(fi:value)}.png"/>
  </xsl:template>
  
</xsl:stylesheet>