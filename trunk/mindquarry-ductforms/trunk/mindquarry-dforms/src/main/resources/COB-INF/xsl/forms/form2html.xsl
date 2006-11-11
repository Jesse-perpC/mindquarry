<?xml version="1.0"?>
<xsl:stylesheet version="1.0" 
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform" 
	xmlns:fi="http://apache.org/cocoon/forms/1.0#instance"
	exclude-result-prefixes="fi">

  <xsl:import href="block:resources:/xslt/contextpath.xsl" />

  <!-- resources directory for Dojo js, css and the like -->
  <xsl:param name="resources-uri">
		<xsl:value-of select="$pathToRoot" />
		<xsl:text>resources/_cocoon/resources</xsl:text>
  </xsl:param>

  <!-- styling of the page -->
  <xsl:include href="resource://org/apache/cocoon/forms/resources/forms-page-styling.xsl"/>
  <!-- styling of the widgets -->
  
  <xsl:include href="resource://org/apache/cocoon/forms/resources/forms-advanced-field-styling.xsl"/>

  <!--  styling of dojo editor (from lenya) -->
  <xsl:include href="dojo-editor.xsl"/>
  <!--  styling of dojo span  -->
  <xsl:include href="dojo-span.xsl" />

  <xsl:template match="head">
  	<xsl:copy>
      <xsl:apply-templates select="." mode="forms-page"/>
      <xsl:apply-templates select="." mode="forms-field-mindquarry"/>
      <!-- script type="text/javascript" src="{$pathToBlock}scripts/dforms.js">//</script-->
      <xsl:apply-templates select="." mode="forms-dojoarea"/>
      <xsl:apply-templates select="." mode="forms-dojospan"/>
      <xsl:apply-templates/>
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
    
    <!-- googlemap-key -->
    <xsl:if test="/*/fi:googlemap">
      <script src="/*/fi:googlemap/fi:key" type="text/javascript"/>
    </xsl:if>

    
    <link rel="stylesheet" type="text/css" href="{$resources-uri}/forms/css/forms.css"/>
  </xsl:template>

  <xsl:template match="body">
  	<xsl:copy>
      <xsl:apply-templates select="." mode="forms-page"/>
      <xsl:apply-templates select="." mode="forms-field"/>
      <xsl:apply-templates/> 
      </xsl:copy>
  </xsl:template>
  
  <!-- put the required ductform field title into the html title -->
  <xsl:template match="title">
  	<title>
  		<xsl:value-of select="/html/body//fi:field[@id='ductform.title']/fi:value" />
  	</title>
  </xsl:template>
  
  <!-- remove invisible stuff (non-selected fields) -->
  <xsl:template match="div[@class='form_block'][not(fi:*)]" />

</xsl:stylesheet>