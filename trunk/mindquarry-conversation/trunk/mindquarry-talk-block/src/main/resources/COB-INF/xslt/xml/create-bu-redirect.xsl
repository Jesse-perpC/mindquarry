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
    xmlns:source="http://apache.org/cocoon/source/1.0"
    xmlns:bu="http://apache.org/cocoon/browser-update/1.0">

    <!-- replace the entire document with a redirect action using
         the result of the sourcewriting to get the name of the new source -->
    <xsl:template match="/">
        <bu:document>
            <!-- NOTE: this repeats the jcr path structure (DRY-mistake) -->
            <bu:redirect uri="{substring-before(substring-after(//source, '/talk/'), '.xml')}" />
        </bu:document>    
    </xsl:template>
    
</xsl:stylesheet>
