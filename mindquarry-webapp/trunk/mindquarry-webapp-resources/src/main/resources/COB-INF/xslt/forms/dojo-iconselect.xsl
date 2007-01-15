<?xml version="1.0"?>
<xsl:stylesheet version="1.0"
                xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:fi="http://apache.org/cocoon/forms/1.0#instance"
                exclude-result-prefixes="fi">

  <!--+
    | fi:field with a selection list (not 'radio' style)
    | Rendering depends on the attributes of fi:styling :
    | - if @list-type is "listbox" : produce a list box with @listbox-size visible
    |   items (default 5)
    | - otherwise, produce a dropdown menu
    +-->
  <xsl:template match="head" mode="forms-dojoiconselect">
    <script type="text/javascript">
      dojo.require("mindquarry.widget.IconSelect");
    </script>
  </xsl:template>
  
  <xsl:template
    match="fi:field[@state='active'][fi:selection-list][fi:styling/@list-type = 'iconSelect']"
    priority="1">
    <!--<script type="text/javascript">dojo.require("mindquarry.widget.IconSelect");</script>-->
    <xsl:variable name="value" select="fi:value"/>
    <!-- dropdown or listbox -->
    <span id="{@id}">
      <select title="{fi:hint}" id="{@id}:input" name="{@id}" dojoType="IconSelect"
        iconprefix="{$pathToBlock}resource/icons/">
        <xsl:apply-templates select="." mode="styling"/>
        <xsl:for-each select="fi:selection-list/fi:item">
          <option value="{@value}">
            <xsl:if test="@value = $value">
              <xsl:attribute name="selected">selected</xsl:attribute>
            </xsl:if>
            <xsl:copy-of select="fi:label/node()"/>
          </option>
        </xsl:for-each>
      </select>
      <xsl:apply-templates select="." mode="common"/>
    </span>
  </xsl:template>
  
  <!-- 
       the xml we match against looks like that:
       
       <fi:field id="..." state="output" required="false">
         <fi:label>Person</fi:label>
         
         <fi:value>max</fi:value>
         <fi:selection-list>
           <fi:item value="admin">
             <fi:label>Administrator</fi:label>
           </fi:item>
           <fi:item value="max">
             <fi:label>Max Mustermann</fi:label>
           </fi:item>
         </fi:selection-list>
       </fi:field>
       
       -->
  <xsl:template match="fi:field[@state='output'][fi:selection-list][fi:styling/@list-type = 'iconSelect']">
    <xsl:variable name="value" select="normalize-space(fi:value)"/>

    <img alt="image for {$value}" src="{$pathToBlock}resource/icons/44/{@id}/{$value}.png"/>
    <span class="iconLabel" id="{@id}:iconLabel">
      <xsl:value-of select="fi:selection-list/fi:item[@value=$value]/fi:label"/>
    </span>    
  </xsl:template>
  
</xsl:stylesheet>