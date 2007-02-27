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
    xmlns:collection="http://apache.org/cocoon/collection/1.0"
    xmlns:xhtml="http://www.w3.org/1999/xhtml"
    exclude-result-prefixes="#default xhtml">

    <xsl:template match="@*|node()">
        <xsl:copy>
            <xsl:apply-templates select="@*|node()" />
        </xsl:copy>
    </xsl:template>

    <xsl:template match="/collection:collection">
        <html>
            <head>
                <title>Sample Textfilters</title>
            </head>
            <body>
                <h1>Sample Textfilters</h1>
                <p>
                    This shows examples of using the TextFilter with various
                    file formats, including Word, PDF, XML etc.
                </p>
                <ul>
                    <xsl:apply-templates/>
                </ul>
            </body>
        </html>
    </xsl:template>
    
    <xsl:template match="collection:resource">
        <li>
            <a href="{@name}"><xsl:value-of select="@name"/></a>
            &gt;
            <a href="{@name}.filtered">Filtered XML Output</a>
        </li>        
    </xsl:template>
</xsl:stylesheet>
