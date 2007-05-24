<?xml version="1.0" encoding="UTF-8"?>
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
<xsl:stylesheet version="1.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	xmlns:ci="http://apache.org/cocoon/include/1.0"
	xmlns:xlink="http://www.w3.org/1999/xlink"
	xmlns:collection="http://apache.org/cocoon/collection/1.0"
	xmlns:team="http://mindquarry.com/ns/schema/teamtransform"
	xmlns:source="http://apache.org/cocoon/source/1.0">
  
  <xsl:param name="userlist" />
  <xsl:param name="email" />
  
  <xsl:template match="/team">
    <html>
      <head>
        <title>Start new conversation</title>
        <xsl:apply-templates select="block" mode="headlinks"/>
        <link rel="breadcrumb" title="Talks" href="."/>
        <link rel="breadcrumb" title="New" />
		<link rel="section-global-action" href="new" title="New Conversation" class="new-conversation-action"/>		
      </head>
      <body>
	  <div class="content">
        <form id="startNewConversationForm" action="" method="POST">
          <h2>Start new conversation</h2>
          <div dojotype="mindquarry:AddResource">AddResource</div>
          <dl>
            <dt><label for="title">Subject</label></dt>
            <dd><div id="subject-box"><input type="text" name="title" /></div></dd>
            
            <dt><label for="subscribers">Subscribers</label></dt>
            <dd dojoType="mindquarry:selectSubscribers">
				<div id="subscribers-box">
				  <select multiple="multiple" id="oldsubscribers" name="oldsubscribers">
						<xsl:apply-templates select="subscriber[@type='email']" />
				  </select>
				  <select multiple="multiple" id="subscribers" name="subscribers">
						<ci:include src="{$userlist}" />
				  </select>
				  <input class="button" type="submit" value="Add"/>
				</div>
            </dd>
            <dt><label>Message</label></dt>
            <dd><div id="message-box"><input type="submit" value="Insert link" class="button"/><br /><textarea cols="60" rows="10" name="body"></textarea></div></dd>
          </dl>
		  <div class="conclusion">
		  		<dl>
          		<dt></dt>
				<dd><input type="submit" value="Start Conversation" /><span>Alternatively you can start a conversation through your mail client: <a href="mailto:{$email}"><xsl:value-of select="$email" /></a></span></dd>
				</dl>
		  </div>
        </form>
	</div>	
      </body>
    </html>
  </xsl:template>
  

  
  <xsl:template match="subscriber">
    <option value="{normalize-space(.)}"><team:user><xsl:apply-templates /></team:user></option>
  </xsl:template>
  
  </xsl:stylesheet>
