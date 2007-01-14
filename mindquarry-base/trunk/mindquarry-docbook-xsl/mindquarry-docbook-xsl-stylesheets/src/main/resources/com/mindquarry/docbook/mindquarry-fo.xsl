<?xml version="1.0"?>
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
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:fo="http://www.w3.org/1999/XSL/Format">
	<xsl:import href="resource://net/sourceforge/docbook/fo/docbook.xsl"/>
	
	<xsl:param name="body.font.family" select="'Helvetica'"/>
	<xsl:param name="title.font.family" select="'Helvetica'"/>
	<xsl:param name="body.font-master" select="12"/>
	<xsl:param name="monospace.font.family" select="'BitstreamVeraSansMono'"/>
	
<xsl:template name="setup.pagemasters">
  <fo:layout-master-set>
	
		<fo:simple-page-master master-name="mindquarry-firstpage"
                           page-height="297mm"
                           page-width="208mm"
                           margin-top="0pt"
                           margin-bottom="0pt"
                           margin-left="0pt"
                           margin-right="0pt">
      <fo:region-body margin-bottom="25.31mm"
                      margin-top="77.04mm"
											margin-left="0mm"
											margin-right="0mm"
											padding-left="60mm"
											padding-top="25mm"
											padding-right="5mm"
											padding-bottom="7mm"
											 background-image="url(first-body-background.png)"
                      column-count="1">
      </fo:region-body>
			
      <fo:region-before region-name="mindquarry-title"
                        extent="77.04mm"
												padding-left="60mm"
												padding-top="30mm"
												padding-bottom="2mm"
													background-image="url(first-header-background.png)"
                        display-align="after"/>
												
      <fo:region-after region-name="mindquarry-nofooter"
                       extent="25.31mm"
											 background-image="url(first-footer-background.png)"
											 
                        display-align="before"/>
    </fo:simple-page-master>
		
		<fo:simple-page-master master-name="mindquarry-chapterpage"
                           page-height="297mm"
                           page-width="208mm"
                           margin-top="0pt"
                           margin-bottom="0pt"
                           margin-left="0pt"
                           margin-right="0pt">
      <fo:region-body margin-bottom="0mm"
                      margin-top="77.89mm"
											margin-left="52.83mm"
											margin-right="0mm"
											padding-left="5mm"
											padding-top="15mm"
											padding-right="5mm"
											padding-bottom="25mm"
											 background-image="url(chapter-body-background.png)"
                      column-count="1">
      </fo:region-body>
			
      <fo:region-before region-name="mindquarry-title"
                        extent="77.89mm"
												padding-top="30mm"
												padding-left="5mm"
												padding-bottom="2mm"
													background-image="url(chapter-title-background.png)"
                        display-align="after"/>
												
      <fo:region-start region-name="mindquarry-chapter-title"
                       extent="52.83mm"
											 padding-bottom="30mm"
											 padding-left="5mm"
											 padding-right="2mm"
											 reference-orientation="90"
											 background-image="url(chapter-start-background.png)"
                        display-align="after"/>
    </fo:simple-page-master>
		
		<fo:simple-page-master master-name="mindquarry-page"
                           page-height="297mm"
                           page-width="208mm"
                           margin-top="0pt"
                           margin-bottom="0pt"
                           margin-left="0pt"
                           margin-right="0pt">
													 
      <fo:region-body margin-bottom="25.31mm"
                      margin-top="25.31mm"
											margin-left="0mm"
											margin-right="0mm"
											padding-left="60mm"
											padding-top="5mm"
											padding-right="5mm"
											padding-bottom="7mm"
											 background-image="url(normal-body-background.png)"
                      column-count="1">
      </fo:region-body>
			
      <fo:region-before region-name="mindquarry-notitle"
                        extent="25.31mm"
													background-image="url(first-footer-background.png)"
                        display-align="before"/>
												
      <fo:region-after region-name="mindquarry-footer"
                       extent="25.31mm"
											 background-image="url(first-footer-background.png)"
											 padding-right="10mm"
											 padding-top="2mm"
                        display-align="before"/>
    </fo:simple-page-master>
		
		<fo:page-sequence-master master-name="mindquarry-sequence">
			<fo:repeatable-page-master-alternatives>
				<fo:conditional-page-master-reference master-reference="mindquarry-firstpage"
																							page-position="first"/>
				<fo:conditional-page-master-reference master-reference="mindquarry-page"
																							odd-or-even="odd"/>
				<fo:conditional-page-master-reference master-reference="mindquarry-page"
																							odd-or-even="even">
				</fo:conditional-page-master-reference>
			</fo:repeatable-page-master-alternatives>
    </fo:page-sequence-master>
		
		<fo:page-sequence-master master-name="mindquarry-chapter-sequence">
			<fo:repeatable-page-master-alternatives>
				<fo:conditional-page-master-reference master-reference="mindquarry-chapterpage"
																							page-position="first"/>
				<fo:conditional-page-master-reference master-reference="mindquarry-page"
																							odd-or-even="odd"/>
				<fo:conditional-page-master-reference master-reference="mindquarry-page"
																							odd-or-even="even">
				</fo:conditional-page-master-reference>
			</fo:repeatable-page-master-alternatives>
    </fo:page-sequence-master>
		
		</fo:layout-master-set>
	</xsl:template>
	
	<xsl:template name="select.user.pagemaster">
		<xsl:param name="element"/>
		<xsl:param name="pageclass"/>
		<xsl:param name="default-pagemaster"/>
		<xsl:choose>
			<xsl:when test="ancestor::book">
				<xsl:value-of select="'mindquarry-chapter-sequence'"/>
			</xsl:when>
			<xsl:otherwise>
				<xsl:value-of select="'mindquarry-sequence'"/>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
	
	<xsl:template name="header.table"/>
	<xsl:template name="footer.table"/>
	
	<xsl:template match="*" mode="running.foot.mode">
		<fo:static-content flow-name="mindquarry-footer">
			<fo:block text-align="right">
				<fo:page-number/>
			</fo:block>
		</fo:static-content>
	</xsl:template>
	
	<xsl:template match="*" mode="running.head.mode">
	
	<xsl:param name="master-reference" select="'unknown'"/>
  <xsl:param name="gentext-key" select="name(.)"/>

  <!-- remove -draft from reference -->
  <xsl:variable name="pageclass">
    <xsl:choose>
      <xsl:when test="contains($master-reference, '-draft')">
        <xsl:value-of select="substring-before($master-reference, '-draft')"/>
      </xsl:when>
      <xsl:otherwise>
        <xsl:value-of select="$master-reference"/>
      </xsl:otherwise>
    </xsl:choose>
  </xsl:variable>

  <fo:static-content flow-name="mindquarry-title">
    <fo:block font-weight="bolder" font-size="32pt" text-align="left">
      <xsl:apply-templates select="." mode="titleabbrev.markup"/>
    </fo:block>
  </fo:static-content>
	
	<fo:static-content flow-name="mindquarry-chapter-title">
    <fo:block color="#ffffff" font-size="48pt" font-weight="bolder">
      <xsl:apply-templates select="." mode="titleabbrev.markup"/>
    </fo:block>
  </fo:static-content>
	</xsl:template>
	
	<xsl:template name="article.titlepage.recto" />
	<xsl:template name="book.titlepage.recto" />
	<xsl:template name="chapter.titlepage.recto" />
	<xsl:template name="preface.titlepage.recto" />
	<xsl:template name="appendix.titlepage.recto" />

</xsl:stylesheet>
