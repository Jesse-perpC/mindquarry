<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0"
    xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:xlink="http://www.w3.org/1999/xlink"
    xmlns:team="http://mindquarry.com/ns/schema/teamtransform"
    xmlns:fd="http://apache.org/cocoon/forms/1.0#definition">
    
    <xsl:import href="servlet:/xslt/contextpath.xsl" />
    <xsl:import href="servlet:/xslt/html/paging.xsl" />
    
    <xsl:param name="team" select="''" />
    
    <xsl:template match="/filters">
        <html>
            <head>
                <title>Task Filters for <team:team><xsl:value-of select="$team" /></team:team></title>
                <link rel="section-global-action" class="add-action" href="../new" title="New Task"/>
                <link rel="section-global-action" class="new-filter-action" href="new" title="New filter" />
                <link rel="breadcrumb" href=".." title="Tasks" />
                <link rel="breadcrumb" title="All Filters" />
                <xsl:apply-templates select="block" mode="headlinks"/>
            </head>
            <body>
                <div class="list">
				    <ul>
                        <xsl:apply-templates select="block" />
				    </ul>
                </div>
            </body>
        </html>
    </xsl:template>

    <xsl:template match="filter">
        <li>
            <img src="/resources/tango-icons/22/actions/system-search.png" class="icon"/>
            <h2><a href="{substring-after(@xlink:href,'/')}"><xsl:apply-templates /></a></h2>
        </li>
    </xsl:template>

</xsl:stylesheet>