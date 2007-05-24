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
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0"
    xmlns:team="http://mindquarry.com/ns/schema/teamtransform"
    xmlns:java="http://xml.apache.org/xslt/java">
    
    <xsl:import href="servlet:/xslt/html/paging.xsl" />
    
    <xsl:param name="team" />
    
    <xsl:template match="/changes">
        <html>
            <head>
                <title>Changed tasks for <team:team><xsl:value-of select="$team"/></team:team></title>
                <xsl:apply-templates select="block" mode="headlinks"/>
                <link rel="breadcrumb" title="Tasks" href="."/>
                <link rel="breadcrumb" title="Recent changes" />
            </head>
            <body>
                
                <div class="list">
                    <ul>
                        <xsl:apply-templates select="block/entry"/>
                    </ul>
                </div>
                
            </body>
        </html>
    </xsl:template>
        
    <xsl:template match="entry">
        <li>
            <img class="icon" src="/resources/tango-icons/48/apps/mindquarry-tasks.png" />
            <h2><a>
                <xsl:variable name="lastpart"><xsl:value-of select="substring-after(substring-after(substring-after(substring-after(@src, '/'), '/'), '/'), '/')" /></xsl:variable>
                <!-- cut off ".xml": -->
                <xsl:variable name="lastpart2NoXml"><xsl:value-of select="substring($lastpart, 0, string-length($lastpart)-3)" /></xsl:variable>
                <xsl:attribute name="href">
                    <xsl:text>/tasks/</xsl:text>
                    <xsl:value-of select="$team" />
                    <xsl:value-of select="$lastpart2NoXml" />
                    <xsl:text>?revision=</xsl:text>
                    <xsl:value-of select="@revision" />
                </xsl:attribute>
                <xsl:value-of select="$lastpart2NoXml"/>
             </a>
            </h2>
            <p><xsl:value-of select="@date"/> to revision <xsl:value-of select="@revision"/></p>
        </li>    
    </xsl:template>
    
</xsl:stylesheet>
