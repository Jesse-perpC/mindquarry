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

  <xsl:template match="head" mode="forms-dojoarea">
  
    <script type="text/javascript">
        dojo.require("mindquarry.widget.wikiLink");
        dojo.require("dojo.widget.Editor2");
        dojo.require("dojo.widget.Dialog");
        dojo.require("dojo.widget.ComboBox");
    </script>
  </xsl:template>

  <xsl:template match="body" mode="forms-dojoarea"/>

  <!--+
      | fi:field with @type 'wiki'
      +-->

  <!-- output view -->
  <xsl:template match="fi:field[@state='output'][fi:styling[@type='wiki']]">
  	<div id="{@id}" name="{@id}" title="{fi:hint}" style="{fi:styling/@style}">
  	  <htmllize>
	      <xsl:apply-templates select="fi:value/node()" mode="dojoarea-copy"/>  	
      </htmllize>
  	</div>
  </xsl:template>
 
  <!-- active view -->
  <xsl:template match="fi:field[@state='active'][fi:styling[@type='wiki']]">
		<xsl:variable name="id" select="concat(@id,':input')"/>
    <textarea  id="{$id}" name="{@id}" title="{fi:hint}" style="{fi:styling/@style}" dojoType="Editor2">
      <xsl:apply-templates select="fi:value/node()" mode="dojoarea-copy"/>
    </textarea>

		<div class="wiki-link-dialog" dojoType="Dialog" id="wikiLinkDialog" bgColor="black" bgOpacity="0.7" toggle="fade" toggleDuration="250">
			<form>
				<table style="background-color:white;opacity:1;padding:20px;">
					<tr>
						<td>Wiki Pages:</td>
						<td>
							<input dojoType="combobox" dataUrl="pages.js" style="width: 300px;" name="location" />
						</td>
					</tr>
					<tr>
						<td colspan="2" align="center">
							<input type="button" onClick="wikiLinkSet(this.form['location'].value);return false;" id="wikiLinkDialogOK" value="OK"/>
							&#160;
							<input type="button" onClick="return false;" id="wikiLinkDialogCancel" value="Cancel"/>
						</td>
					</tr>
				</table>
			</form>
		</div>
  </xsl:template>

  <xsl:template match="@*|*" mode="dojoarea-copy">
    <xsl:copy>
      <xsl:apply-templates select="@*|node()" mode="dojoarea-copy"/>
    </xsl:copy>
  </xsl:template>

  <xsl:template match="text()" mode="dojoarea-copy">
    <xsl:copy-of select="translate(., '&#13;', '')"/>
  </xsl:template>

</xsl:stylesheet>