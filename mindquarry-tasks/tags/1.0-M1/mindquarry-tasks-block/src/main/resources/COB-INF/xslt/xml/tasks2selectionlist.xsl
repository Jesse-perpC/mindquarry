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

<!--

Converts a tasks list :

<tasks xlink:href="mindquarry" xml:base="http://172.16.5.138:8888/tasks/mindquarry/">

	<task xlink:href="task3">
		<title>Make Mindquarry cool</title>
		<status>running</status>
		<summary />
	</task>

	...
</tasks>

To a CForms selectionlist :

	<fd:selection-list>
		<fd:item value="the href"><fd:label>the title</fd:label></fd:item>
		. . .
	</fd:selection-list>

-->

<xsl:stylesheet version="1.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	xmlns:xlink="http://www.w3.org/1999/xlink"
	xmlns:fd="http://apache.org/cocoon/forms/1.0#definition">

	<xsl:template match="tasks">
		<fd:selection-list>
			<xsl:apply-templates select="task">
				<xsl:sort select="title"/>
			</xsl:apply-templates>
		</fd:selection-list>
	</xsl:template>
	
	<xsl:template match="task">
		<fd:item value="{@xlink:href}">
			<fd:label><xsl:apply-templates select="title"/></fd:label>
		</fd:item>
	</xsl:template>

</xsl:stylesheet>