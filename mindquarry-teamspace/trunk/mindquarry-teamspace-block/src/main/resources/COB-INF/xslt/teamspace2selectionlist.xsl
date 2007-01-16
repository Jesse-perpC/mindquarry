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
<xsl:stylesheet 
    xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0"
    xmlns:fd="http://apache.org/cocoon/forms/1.0#definition">
    <xsl:template match="teamspace">
        <fd:selection-list>
            <xsl:apply-templates select="//user"></xsl:apply-templates>
        </fd:selection-list>
    </xsl:template>
    
    <xsl:template match="user">
        <fd:item value="{id}">
            <xsl:if test="normalize-space(name) or normalize-space(surname)">
                <fd:label>
                    <xsl:value-of select="normalize-space(name)"/>
                    <xsl:text> </xsl:text>
                    <xsl:value-of select="normalize-space(surname)"/>
                </fd:label>
            </xsl:if>
        </fd:item>
    </xsl:template>
</xsl:stylesheet>