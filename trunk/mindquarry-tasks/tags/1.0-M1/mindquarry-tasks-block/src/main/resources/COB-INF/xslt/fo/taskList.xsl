<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	xmlns:xhtml="http://www.w3.org/1999/xhtml"
	xmlns:fo="http://www.w3.org/1999/XSL/Format"
	xmlns:xlink="http://www.w3.org/1999/xlink">

    <xsl:import href="block:/xslt/fo/xhtml2fo.xsl" />
  
    <xsl:param name="teamspaceID" />

	<xsl:variable name="pageTitle">
		<xsl:text>Tasks for </xsl:text><xsl:value-of select="$teamspaceID" />
	</xsl:variable>
	
	<xsl:template match="/tasks">
		<fo:root xsl:use-attribute-sets="root">
			<xsl:call-template name="process-common-attributes"/>
			<xsl:call-template name="make-layout-master-set"/>
			<fo:page-sequence master-reference="all-pages">
        <fo:title>
          <xsl:value-of select="$pageTitle" />
        </fo:title>
        <fo:static-content flow-name="page-header">
          <fo:block space-before.conditionality="retain"
            space-before="{$page-header-margin}"
            xsl:use-attribute-sets="page-header">
            <xsl:if test="$title-print-in-header = 'true'">
              <xsl:value-of select="$pageTitle" />
            </xsl:if>
          </fo:block>
        </fo:static-content>
        <fo:static-content flow-name="page-footer">
          <fo:block space-after.conditionality="retain"
            space-after="{$page-footer-margin}"
            xsl:use-attribute-sets="page-footer">
            <xsl:if test="$page-number-print-in-footer = 'true'">
              <xsl:text>- </xsl:text>
              <fo:page-number/>
              <xsl:text> -</xsl:text>
            </xsl:if>
          </fo:block>
        </fo:static-content>
        <fo:flow flow-name="xsl-region-body">
          <fo:block xsl:use-attribute-sets="body">
            <fo:table table-layout="auto" width="100%">
              <fo:table-column column-number="1" column-width="20mm"/>
              <fo:table-column column-number="2" column-width="85mm"/>
              <fo:table-column column-number="3" column-width="25mm"/>
              <fo:table-column column-number="4" column-width="25mm"/>
              <fo:table-header>
                <fo:table-row text-align="center">
                  <fo:table-cell xsl:use-attribute-sets="th">
                    <fo:block>
                      ID
                    </fo:block>
                  </fo:table-cell>
                  <fo:table-cell xsl:use-attribute-sets="th">
                    <fo:block>
                      Task
                    </fo:block>
                  </fo:table-cell>
                  <fo:table-cell xsl:use-attribute-sets="th">
                    <fo:block>
                      Status
                    </fo:block>
                  </fo:table-cell>
                  <fo:table-cell xsl:use-attribute-sets="th">
                    <fo:block>
                      Due Date
                    </fo:block>
                  </fo:table-cell>
                </fo:table-row>
              </fo:table-header>
              <fo:table-body>
                <xsl:apply-templates />
              </fo:table-body>
            </fo:table>
          </fo:block>
        </fo:flow>
      </fo:page-sequence>
		</fo:root>
	</xsl:template>
	
	<xsl:template match="task">
    <fo:table-row>
      <xsl:if test="status='done'">
        <xsl:attribute name="text-decoration">line-through</xsl:attribute>
      </xsl:if>
      <fo:table-cell xsl:use-attribute-sets="td">
        <fo:block><xsl:value-of select="@xlink:href" /></fo:block>
      </fo:table-cell>
      
      <fo:table-cell xsl:use-attribute-sets="td">
        <fo:block><xsl:value-of select="title" /></fo:block>
        <fo:block font-size="80%" color="#cccccc"><xsl:value-of select="summary"/></fo:block>
      </fo:table-cell>
      
      <fo:table-cell xsl:use-attribute-sets="td">
        <fo:block><xsl:value-of select="status" /></fo:block>
      </fo:table-cell>
      
      <fo:table-cell xsl:use-attribute-sets="td">
        <fo:block><xsl:value-of select="date"/></fo:block>
      </fo:table-cell>
    </fo:table-row>
	</xsl:template>
	
	<xsl:template match="div[@class='ductform']/dl[@id='ductform.title']|xhtml:div[@class='ductform']/xhtml:dl[@id='ductform.title']" priority="7">
		<fo:block xsl:use-attribute-sets="h1">
			<xsl:apply-templates select="dd/span|xhtml:dd/xhtml:span"/>
		</fo:block>
	</xsl:template>
	
	<xsl:template match="div[@class='ductform']/dl[@id='ductform.content']|xhtml:div[@class='ductform']/xhtml:dl[@id='ductform.content']" priority="7">
		<fo:block>
			<xsl:apply-templates select="dd/*|xhtml:dd/*"/>
		</fo:block>
	</xsl:template>
	
	<xsl:template match="div[@class='ductform']/dl|xhtml:div[@class='ductform']/xhtml:dl">
		<fo:block xsl:use-attribute-sets="h2">
			<xsl:apply-templates select="dt/label/text()"></xsl:apply-templates>
		</fo:block>
		<fo:block>
			<xsl:apply-templates select="dd/*"></xsl:apply-templates>
		</fo:block>
	</xsl:template>
		
	<xsl:template match="div[@class='ductform']/dl[@id='ductform.ductforms']|xhtml:div[@class='ductform']/xhtml:dl[@id='ductform.ductforms']" priority="7"/>
	
</xsl:stylesheet>
