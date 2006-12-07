<?xml version="1.0"?>
<xsl:stylesheet version="1.0"
                xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:fi="http://apache.org/cocoon/forms/1.0#instance"
                exclude-result-prefixes="fi">
  <xsl:template match="head" mode="forms-dojospan">
    <script type="text/javascript">
    	dojo.require("mindquarry.widget.AutoActiveField");
    </script>
  </xsl:template>
  
  <xsl:template match="body" mode="forms-dojospan"/>
  
  <xsl:template match="fi:field[@state='output'][./fi:styling[@type='dojospan']]" priority="2">
  	<span id="{@id}" name="{@id}" title="{fi:hint}" dojoType="autoactivefield" class="forms field output">
		<xsl:apply-templates select="fi:value/node()"/>
  	</span>
  </xsl:template>
</xsl:stylesheet>