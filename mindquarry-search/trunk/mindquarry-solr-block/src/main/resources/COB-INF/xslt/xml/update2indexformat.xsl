<?xml version="1.0" encoding="UTF-8"?>
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
        </doc>
    </xsl:template>

    <xsl:template match="delete|commit">
        <xsl:copy-of select="."/>
    </xsl:template>

</xsl:stylesheet>
