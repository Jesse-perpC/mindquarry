<?xml version="1.0"?>
<!--
  Copyright 1999-2004 The Apache Software Foundation

  Licensed under the Apache License, Version 2.0 (the "License");
  you may not use this file except in compliance with the License.
  You may obtain a copy of the License at

      http://www.apache.org/licenses/LICENSE-2.0

  Unless required by applicable law or agreed to in writing, software
  distributed under the License is distributed on an "AS IS" BASIS,
  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  See the License for the specific language governing permissions and
  limitations under the License.
-->
<xsl:stylesheet version="1.0"
                xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:fi="http://apache.org/cocoon/forms/1.0#instance"
                exclude-result-prefixes="fi">

  <xsl:template match="head" mode="forms-dojobuttons">
  
    <script type="text/javascript">
        dojo.require("mindquarry.widget.ToggleButton");
    </script>
  </xsl:template>

  <xsl:template match="body" mode="forms-dojobuttons"/>

  <xsl:template match="fi:multivaluefield[fi:styling/@list-type='buttons']">
    <xsl:variable name="id" select="@id"/>
    <xsl:variable name="values" select="fi:values/fi:value/text()"/>
    <xsl:variable name="state" select="@state" />

    <div id="{@id}" title="{fi:hint}" class="togglebuttons">
      <xsl:for-each select="fi:selection-list/fi:item">
      	<div dojoType="ToggleButton">
	        <xsl:variable name="value" select="@value"/>
	        <xsl:variable name="item-id" select="concat($id, ':', position())"/>
      		<xsl:choose>
	      		<xsl:when test="$values[. = $value]">
		            <xsl:attribute name="class">togglebuttonpushed</xsl:attribute>
		         </xsl:when>
		         <xsl:otherwise>
		         	<xsl:attribute name="class">togglebutton</xsl:attribute>
		         </xsl:otherwise>
	         </xsl:choose>
	        <input id="{$item-id}" type="checkbox" value="{@value}" name="{$id}">
	          <xsl:apply-templates select="." mode="css"/>
	          <xsl:if test="$state = 'disabled'">
	            <xsl:attribute name="disabled">disabled</xsl:attribute>
	          </xsl:if>
	          <xsl:if test="$values[. = $value]">
	            <xsl:attribute name="checked">checked</xsl:attribute>
	          </xsl:if>
	        </input>
	        <xsl:apply-templates select="." mode="label">
	          <xsl:with-param name="id" select="$item-id"/>
	        </xsl:apply-templates>
        </div>
      </xsl:for-each>
      <xsl:apply-templates select="." mode="common"/>
    </div>
    <hr style="clear:both"/>
  </xsl:template>

</xsl:stylesheet>