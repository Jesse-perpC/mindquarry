<?xml version="1.0"?>

<!--
  Copyright (C) 2006-2007 Mindquarry GmbH, All Rights Reserved
  
  The contents of this file are subject to the Mozilla Public License
  Version 1.1 (the "License"); you may not use this file except in
  compliance with the License. You may obtain a copy of the License at
  http://www.mozilla.org/MPL/
  
  Software distributed under the License is distributed on an "AS IS"
  basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
  License for the specific language governing rights and limitations
  under the License.
--> 

<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
  xmlns:fi="http://apache.org/cocoon/forms/1.0#instance"
  xmlns:xhtml="http://www.w3.org/1999/xhtml"
  xmlns:bu="http://apache.org/cocoon/browser-update/1.0"
  exclude-result-prefixes="fi">
  
  <xsl:import href="cocoon:/xslt/contextpath.xsl"/>

  <!-- resources directory for Dojo js, css and the like -->
  <xsl:param name="resources-uri">
    <xsl:value-of select="$pathToRoot"/>
    <xsl:text>resources/_cocoon/resources</xsl:text>
  </xsl:param>

  <!-- styling of the page -->
  <xsl:include href="resource://org/apache/cocoon/forms/resources/forms-page-styling.xsl"/>
  <!-- styling of the widgets -->

  <xsl:include href="resource://org/apache/cocoon/forms/resources/forms-advanced-field-styling.xsl"/>

  <!--  styling of dojo editor (from lenya) -->
  <xsl:include href="dojo-editor.xsl"/>
  <!--  styling of dojo auto activated fields  -->
  <xsl:include href="dojo-autoactive.xsl"/>
  <!-- styling of toggle buttons -->
  <xsl:include href="dojo-buttons.xsl"/>
  <!-- styling of calendar -->
  <xsl:include href="dojo-calendar.xsl"/>
  <!-- styling of ChangePassword -->
  <xsl:include href="dojo-changepassword.xsl"/>
  <!-- styling of IconSelect -->
  <xsl:include href="dojo-iconselect.xsl"/>
  
  <xsl:template match="head">
    <xsl:copy>
      <xsl:apply-templates select="." mode="forms-page"/>
      <xsl:apply-templates select="." mode="forms-field-mindquarry"/>
      <xsl:apply-templates select="." mode="forms-dojoarea"/>
      <xsl:apply-templates select="." mode="forms-dojoautoactive"/>
      <xsl:apply-templates select="." mode="forms-dojobuttons"/>
      <xsl:apply-templates select="." mode="forms-dojocalendar"/>
      <xsl:apply-templates select="." mode="forms-dojochangepassword"/>
      <xsl:apply-templates select="." mode="forms-dojoiconselect"/>
      <xsl:apply-templates/>
    </xsl:copy>
  </xsl:template>

  <xsl:template match="head" mode="forms-field-mindquarry">
    <xsl:copy-of select="fi:init/node()"/>
    
    <!-- moved to html2html to be available for lightbox-forms (eg. edit-user)
    <script src="{$resources-uri}/ajax/cocoon.js" type="text/javascript"/>
    <script src="{$resources-uri}/forms/js/forms-lib.js" type="text/javascript"/>
    <script type="text/javascript">
        dojo.addOnLoad(forms_onload);
        dojo.require("cocoon.forms.*");
    </script>
    -->
    <script type="text/javascript">
      dojo.require("dojo.widget.Tooltip");
    </script>
    
    <!-- <script src="{$resources-uri}/forms/mattkruse-lib/AnchorPosition.js" type="text/javascript"/> -->
    <!-- <script src="{$resources-uri}/forms/mattkruse-lib/PopupWindow.js" type="text/javascript"/>-->
    <!-- script src="{$resources-uri}/forms/mattkruse-lib/OptionTransfer.js" type="text/javascript"/-->
    <!-- script src="{$resources-uri}/forms/mattkruse-lib/selectbox.js" type="text/javascript"/-->
    <!-- xsl:apply-templates select="." mode="forms-calendar"/-->
    <!-- xsl:apply-templates select="." mode="forms-htmlarea"/-->

    <!-- googlemap-key -->
    <xsl:if test="/*/fi:googlemap">
      <script src="/*/fi:googlemap/fi:key" type="text/javascript"/>
    </xsl:if>

    <!-- moved to html2html to be available for lightbox-forms (eg. edit-user)
    <link rel="stylesheet" type="text/css" href="{$resources-uri}/forms/css/forms.css"/>
    -->
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
      <xsl:choose>
        <xsl:when test="/html/body//fi:field[@id='ductform.title']/fi:value">
          <xsl:value-of select="/html/body//fi:field[@id='ductform.title']/fi:value"/>  
        </xsl:when>
        <xsl:otherwise>
          <xsl:value-of select="." />
        </xsl:otherwise>
      </xsl:choose>
    </title>
  </xsl:template>

  <!-- remove invisible stuff (non-selected fields) -->
  <xsl:template match="div[@class='form_block'][not(fi:*)]"/>

  <xsl:template match="fi:group[fi:styling/@layout='default']" mode="group-layout">
    <div title="{normalize-space(fi:hint)}" class="ductform">
      <xsl:apply-templates select="fi:items/*" mode="default"/>
    </div>
  </xsl:template>

  <!-- hide actions with the type "hidden" -->
  <xsl:template match="fi:action[fi:styling/@type='hidden']">
    <input id="{@id}" type="submit" name="{@id}" title="{fi:hint}" style="display:none;"/>
  </xsl:template>

  <xsl:template match="fi:items//fi:action[@state='output']"/>
  
  <xsl:template match="bu:replace">
    <xsl:copy>
      <xsl:apply-templates select="node() | @*"/>
    </xsl:copy>
  </xsl:template>

  <!-- links -->
    
  <xsl:template match="fi:field[fi:styling/@styling='link']">
    <a href="{normalize-space(fi:value)}">
      <xsl:apply-templates select="following-sibling::fi:field[fi:styling/@styling='linkcontent']"
        mode="value"/>
    </a>
  </xsl:template>

  <xsl:template match="fi:field[fi:styling/@styling='linkcontent']"/>

  <xsl:template match="fi:field[fi:styling/@styling='linkcontent']" mode="value">
    <span id="{@id}">
      <xsl:value-of select="fi:value"/>
    </span>
  </xsl:template>

  <xsl:template match="fi:help">
    <xsl:variable name="id" select="concat(../@id, ':help')"/>
    <div class="forms-help forms help" id="{$id}" style="display:none" dojoType="tooltip" connectId="{$id}:a">
      <xsl:apply-templates select="node()"/>
    </div>
    <a id="{$id}:a" href="#{$id}" class="help">
      <!-- TODO: i18n key for helppopup -->
      <img src="{$pathToRoot}resources/icons/16x16/apps/help-browser.png" alt="helppopup"/>
    </a>
  </xsl:template>

  <!--
  <xsl:template match="fi:items/fi:label"/>
  -->
  <xsl:template match="fi:items/*" mode="label">
    <xsl:param name="id"/>

    <xsl:variable name="resolvedId">
      <xsl:choose>
        <xsl:when test="$id != ''">
          <xsl:value-of select="$id"/>
        </xsl:when>
        <xsl:otherwise>
          <xsl:value-of select="concat(@id, ':input')"/>
        </xsl:otherwise>
      </xsl:choose>
    </xsl:variable>

    <label for="{$resolvedId}" title="{fi:hint}">
      <xsl:apply-templates select="." mode="css"/>
      <xsl:copy-of select="fi:label/node()"/>
    </label>
  </xsl:template>

  <!-- teamspace stuff -->

  <xsl:template match="li[fi:styling/@type='member-entry']">
    <li id="{@id}">
      <div class="member-entry" style="cursor:pointer;">
        <div class="member-details">
          <xsl:apply-templates select="fi:action" mode="javascript"/>
          <xsl:apply-templates select="fi:output|fi:action|text()"/>
        </div>
      </div>
    </li>
  </xsl:template>

  <xsl:template match="fi:output[fi:styling/@type='full-name']">
    <xsl:if test="count(preceding-sibling::fi:output[fi:styling/@type='full-name'])=0">
      <h4>
        <xsl:value-of select="fi:value"/>
        <xsl:text> </xsl:text>
        <xsl:for-each select="following-sibling::fi:output[fi:styling/@type='full-name']">
          <xsl:value-of select="fi:value"/>
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
  
  <xsl:template match="fi:action[fi:styling/@type='blindlink']">
    <a href="#">
      <xsl:apply-templates/>
    </a>
  </xsl:template>

  <xsl:template match="fi:output[fi:styling/@type='user-image']">
    <img alt="image of the user with id {normalize-space(fi:value)}"
      src="{$pathToRoot}teamspace/users/{normalize-space(fi:value)}.png"/>
  </xsl:template>

</xsl:stylesheet>
