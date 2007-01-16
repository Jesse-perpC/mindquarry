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
	xmlns:xhtml="http://www.w3.org/1999/xhtml"
	xmlns="http://www.w3.org/1999/xhtml"
	xmlns:us="http://www.mindquarry.com/ns/schema/webapp">
	
	<xsl:template match="xhtml:head|head" mode="lightbox">
		<!-- only include the lightbox.(css|js) if there are actually
		lightbox links in the page -->
		<xsl:if test="//xhtml:a[@rel='lightbox']|//a[@rel='lightbox']">
			<link rel="stylesheet" type="text/css" href="{$pathToBlock}{$cssPath}lightbox.css" />
			<script type="text/javascript" src="{$pathToBlock}{$scriptPath}lightbox.js" >//</script>
		</xsl:if>
	</xsl:template>

</xsl:stylesheet>