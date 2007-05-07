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
        <link rel="breadcrumb" title="Talks" href="."/>
        <link rel="breadcrumb" title="New" />
      </head>
      <body>
        <form id="startNewConversationForm" action="" method="POST">
          <h1>Start new conversation</h1>
          <div dojotype="mindquarry:AddResource">AddResource</div>
          <dl>
            <dt><label for="title">Subject</label></dt>
            <dd><input type="text" name="title" /></dd>
            
            <dt><label for="subscribers">Subscribers</label></dt>
            <dd dojoType="mindquarry:selectSubscribers">
              <select multiple="multiple" id="oldsubscribers" name="oldsubscribers">
					<xsl:apply-templates select="subscriber[@type='email']" />
              </select>
              <select multiple="multiple" id="subscribers" name="subscribers">
					<ci:include src="{$userlist}" />
              </select>
              <input type="submit" value="Add"/>
            </dd>
            <dt><label>Message</label></dt>
            <dd><input type="submit" value="Insert link"/><textarea cols="60" rows="20" name="body"></textarea></dd>
          </dl>
          <input type="submit" value="Start Conversation" />
          <span>Alternatively you can start a conversation through your mail client: <a href="mailto:{$email}"><xsl:value-of select="$email" /></a></span>
        </form>
      </body>
    </html>
  </xsl:template>
  
  <xsl:template match="subscriber">
    <option value="{normalize-space(.)}"><team:user><xsl:apply-templates /></team:user></option>
  </xsl:template>
  
  </xsl:stylesheet>
