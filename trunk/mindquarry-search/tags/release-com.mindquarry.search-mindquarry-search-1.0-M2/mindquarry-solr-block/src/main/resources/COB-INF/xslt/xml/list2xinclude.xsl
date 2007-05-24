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
    xmlns:xi="http://www.w3.org/2001/XInclude" version="1.0">
    
    
    <xsl:template match="changes">
        <update>
            <xsl:if test="count(modified/path)>0">
                <add>
                    <xsl:apply-templates select="modified/path"/>
                </add>
            </xsl:if>
    
            <xsl:if test="count(deleted/path)>0">
                <delete>
                    <xsl:apply-templates select="deleted/path"/>
                </delete>
            </xsl:if>
    
            <commit/>
        </update>
    </xsl:template>

    <xsl:template match="deleted/path">
        <id>
            <xsl:value-of select="normalize-space(.)"/>
        </id>
    </xsl:template>

    <xsl:template match="modified/path">
        <source id="{normalize-space(.)}">
            <xi:include href="cocoon:/extract?url={normalize-space(.)}"/>
            <xi:include href="cocoon:/meta?url={normalize-space(.)}"/>
        </source>
    </xsl:template>
</xsl:stylesheet>
