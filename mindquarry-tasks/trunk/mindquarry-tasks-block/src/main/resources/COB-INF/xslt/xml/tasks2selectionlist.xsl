<!--

Converts a tasks list :

<tasks xlink:href="mindquarry" xml:base="http://172.16.5.138:8888/tasks/mindquarry/">

	<task xlink:href="task3">
		<title>Make Mindquarry cool</title>
		<status>running</status>
		<summary />
	</task>

	. . .
	
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
	xmlns:fd="http://apache.org/cocoon/forms/1.0#definition"
	>

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