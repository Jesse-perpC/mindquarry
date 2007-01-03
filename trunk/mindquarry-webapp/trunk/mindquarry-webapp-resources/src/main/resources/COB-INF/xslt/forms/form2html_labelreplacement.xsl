<?xml version="1.0"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
  xmlns:fi="http://apache.org/cocoon/forms/1.0#instance"
  xmlns:xhtml="http://www.w3.org/1999/xhtml"
  xmlns:bu="http://apache.org/cocoon/browser-update/1.0" exclude-result-prefixes="fi">
  <xsl:import href="cocoon:/xslt/contextpath.xsl"/>
  
  <!-- resources directory for Dojo js, css and the like -->
  <xsl:param name="resources-uri">
    <xsl:value-of select="$pathToRoot"/>
    <xsl:text>resources/_cocoon/resources</xsl:text>
  </xsl:param>

  <xsl:include href="form2html.xsl"/>

  <!-- standard styling container for label and widget -->
  <xsl:template match="fi:*|fi:items/*" mode="default">
    <dl id="{@id}">
      <dt>
        <xsl:apply-templates select="." mode="label"/>
      </dt>
      <!--+
          | class: look for a descendant with a state attribute and use it
          | because the fi: widget might be wrapped in other html elements
  	      |
  	      | id: copy the cforms id that unfortunately includes dots that
          | cannot be used in css selectors
          +-->
      <dd class="{./descendant-or-self::fi:*[@state][1]/@state}" id="{translate(@id, '.', '_')}">
        <xsl:apply-templates select="."/>
      </dd>
    </dl>
  </xsl:template>

  <!-- hide labels that are marked as hidden, not in label mode so that it
       will only match inside the dd above -->
  <xsl:template match="fi:label[@class='hidden']"/>

  <!-- do not add a dl/dt/dd for actions (or non-visible actions represented as placeholder) -->
  <xsl:template match="bu:replace[./fi:action | ./fi:placeholder]">
    <xsl:copy>
      <xsl:copy-of select="@id"/>
      <xsl:apply-templates/>
    </xsl:copy>
  </xsl:template>
    
  <xsl:template match="bu:replace">
    <xsl:copy>
      <xsl:copy-of select="@id"/>
      <dl id="{@id}">
        <dt>
          <xsl:choose>
            <!-- people and dependencies (repeater-based) are wrapped in a div -->
            <xsl:when test="div/fi:label">
              <!-- some copy-of magic: the fi:label will end as an html label,
                   don't know exactly why, maybe its just the ns-prefix removal... -->
              <xsl:copy-of select="div/fi:label"/>
            </xsl:when>
            <xsl:otherwise>
              <xsl:apply-templates select="fi:*" mode="label"/>
            </xsl:otherwise>
          </xsl:choose>
        </dt>
        <dd class="{./descendant-or-self::fi:*[@state][1]/@state}" id="{translate(@id, '.', '_')}">
          <xsl:apply-templates/>
        </dd>
      </dl>
    </xsl:copy>
  </xsl:template>

  <xsl:template match="node() | @*" mode="default">
    <xsl:copy>
      <xsl:apply-templates select="node() | @*" mode="default"/>
    </xsl:copy>
  </xsl:template>  
  
</xsl:stylesheet>
