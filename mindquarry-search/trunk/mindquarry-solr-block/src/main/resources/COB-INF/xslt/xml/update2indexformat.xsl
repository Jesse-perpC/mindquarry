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
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:collection="http://apache.org/cocoon/collection/1.0"
    xmlns:xi="http://www.w3.org/2001/XInclude" version="1.0">
    <xsl:param name="action" select="''"/>

    <xsl:template match="update">
        <post action="{$action}">
            <xsl:apply-templates/>
        </post>
    </xsl:template>

    <xsl:template match="add">
        <add>
            <xsl:apply-templates/>
        </add>
    </xsl:template>

    <xsl:template match="source">
        <doc>
            <field name="id">
                <xsl:value-of select="@id"/>
            </field>
            <field name="name">
                <xsl:value-of select="collection:resource/@name"/>
            </field>
            <field name="location">
                <xsl:value-of select="collection:resource/@uri"/>
            </field>
            <field name="type">
                <xsl:value-of
                    select="substring-before(substring-after(substring-after(collection:resource/@uri,'/teamspaces/'),'/'),'/')"
                />
            </field>
            <field name="content">
                <xsl:value-of select="document/content"/>
            </field>
            <field name="title">
                <xsl:value-of select="document/title"/>
            </field>
        </doc>
    </xsl:template>

    <xsl:template match="delete|commit">
        <xsl:copy-of select="."/>
    </xsl:template>

</xsl:stylesheet>
