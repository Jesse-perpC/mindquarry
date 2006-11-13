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

  <xsl:template match="head" mode="forms-dojocalendar">
  
    <script type="text/javascript">
        dojo.require("dojo.widget.DropdownDatePicker");
    </script>
  </xsl:template>

  <xsl:template match="body" mode="forms-dojocalendar"/>
    
  <xsl:template match="fi:field[fi:styling/@type='date'] |
                       fi:field[fi:datatype[@type='date'][fi:convertor/@variant='date']][not(fi:styling/@type)][not(fi:selection-list)] |
                       fi:aggregatefield[fi:datatype[@type='date'][fi:convertor/@variant='date']][not(fi:styling/@type)][not(fi:selection-list)]
                       ">
    <xsl:variable name="id" select="concat(@id, ':cal')"/>
    
    <xsl:variable name="format">
      <xsl:choose>
        <xsl:when test="fi:datatype[@type='date']/fi:convertor/@pattern">
          <xsl:value-of select="fi:datatype[@type='date']/fi:convertor/@pattern"/>
        </xsl:when>
        <xsl:otherwise>yyyy-MM-dd</xsl:otherwise>
      </xsl:choose>
    </xsl:variable>
    
    <span id="{@id}">
      <xsl:choose>
        <xsl:when test="@state = 'output'">
          <xsl:value-of select="fi:value"/>
        </xsl:when>
        <xsl:otherwise>
	      <!-- regular input -->
	      <!-- 
	      <input id="{@id}:input" name="{@id}" value="{fi:value}" title="{normalize-space(fi:hint)}" type="text">
	        <xsl:apply-templates select="." mode="styling"/>
	      </input>
	      -->
	      <!-- input name is the name of the shielded dojo widget -->
	      <div inputName="{@id}" dojoType="DropdownDatePicker" widgetId="{@id}:input" id="{@id}:input" name="{@id}" value="{fi:value}" title="{normalize-space(fi:hint)}" type="text"/>
	      <!-- calendar popup -->
	      <!-- <xsl:choose>
	        <xsl:when test="@state = 'disabled'">
	          <img src="{$resources-uri}/forms/img/cal.gif" alt="forms:calendar.alt" i18n:attr="alt"/>
	        </xsl:when>
	        <xsl:otherwise>
	          <a href="#" name="{$id}" id="{$id}"
	             onclick="forms_calendar.select(forms_getForm(this)['{@id}'],'{$id}','{$format}'); return false;">
	            <img src="{$resources-uri}/forms/img/cal.gif" alt="forms:calendar.alt" i18n:attr="alt"/>
	          </a>
	        </xsl:otherwise>
	      </xsl:choose> -->
	
	      <!-- common stuff -->
	      <xsl:apply-templates select="." mode="common"/>
	    </xsl:otherwise>
	  </xsl:choose>
    </span>
  </xsl:template>
</xsl:stylesheet>