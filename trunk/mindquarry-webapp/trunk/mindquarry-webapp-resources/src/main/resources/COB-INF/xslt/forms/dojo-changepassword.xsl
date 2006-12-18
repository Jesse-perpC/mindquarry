<?xml version="1.0"?>
<xsl:stylesheet version="1.0"
                xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:fi="http://apache.org/cocoon/forms/1.0#instance"
                exclude-result-prefixes="fi">
  <xsl:template match="head" mode="forms-dojospan">
    <script type="text/javascript">
    	dojo.require("mindquarry.widget.ChangePassword");
    </script>
  </xsl:template>
  
  <xsl:template match="body" mode="forms-dojospan"/>


  <xsl:template match="fi:output[fi:styling/@type='pwdChanged']">
    <span id="{@id}" dojoType="ChangePassword">
      <xsl:apply-templates select="fi:value/node()"/>
    </span>
  </xsl:template>
  
  <!--+
      | standard span-based output mode
      +-->
  <xsl:template match="fi:field[@state='output'][./fi:styling[@autoactive='true']]" priority="2">
    <span dojoType="autoactivefield" id="{@id}" name="{@id}" title="{fi:hint}" class="forms field output">
	  <xsl:apply-templates select="fi:value/node()"/>
  	</span>
  </xsl:template>
  
  <!--+
      | textarea styling
      +-->
  <xsl:template match="fi:field[@state='output'][./fi:styling[@autoactive='true'][@type='textarea']]" priority="2">
    <div dojoType="autoactivefield" id="{@id}" name="{@id}" title="{fi:hint}" class="forms field output">
      <xsl:apply-templates select="fi:value/node()"/>
    </div>
  </xsl:template>

  <!--+
      | override standard styling for fi:booleanfield with @state 'output'
      | (checkbox) to include dojoType="autoactive"
      +-->
  <xsl:template match="fi:booleanfield[@state='output'][./fi:styling[@autoactive='true']]" priority="3">
    <input dojoType="autoactivefield" id="{@id}" type="checkbox" title="{fi:hint}" disabled="disabled" value="{@true-value}" name="{@id}">
      <xsl:apply-templates select="." mode="css"/>
      <xsl:if test="fi:value != 'false'">
        <xsl:attribute name="checked">checked</xsl:attribute>
      </xsl:if>
    </input>
  </xsl:template>
  
</xsl:stylesheet>