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
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:jcr="http://www.jcp.org/jcr/1.0"
    xmlns:xt="http://mindquarry.com/ns/cnd/xt">

    <!-- copy complete file content -->
    <xsl:template match="@*|node()">
        <xsl:copy>
            <xsl:apply-templates select="@*|node()"/>
        </xsl:copy>
    </xsl:template>

    <!-- rename team description file nodes -->
    <xsl:template match="/teamspaces/*/metadata.xml">
        <xsl:element name="{local-name(..)}.xml">
            <xsl:copy-of select="./@*"/>
            <xsl:apply-templates/>
        </xsl:element>
    </xsl:template>

    <!-- fix date formats to ISO8601 -->
    <xsl:template
        match="/teamspaces/*/tasks/*/jcr:content/task/date/text[@xt:characters]">
        <xsl:element name="text">
            <xsl:copy-of select="@*[local-name(.) != 'xt:characters']"/>
            
            <xsl:attribute name="xt:characters">
                <xsl:call-template name="AmericanDateFormatToISO8601">
                    <xsl:with-param name="americanDate" select="@xt:characters"
                    />
                </xsl:call-template>
            </xsl:attribute>
            <xsl:apply-templates/>
        </xsl:element>
    </xsl:template>

    <xsl:template name="AmericanDateFormatToISO8601">
        <xsl:param name="americanDate"/>
        
        <xsl:variable name="month" select="substring-before($americanDate, '/')"/>
        <xsl:variable name="afterMonth" select="substring-after($americanDate, '/')"/>
        
        <xsl:variable name="day" select="substring-before($afterMonth, '/')"/>
        <xsl:variable name="year" select="substring-after($afterMonth, '/')"/>
        
        <xsl:value-of select="$year"/>-<xsl:value-of select="$month"/>-<xsl:value-of select="$day"/>
    </xsl:template>
</xsl:stylesheet>
