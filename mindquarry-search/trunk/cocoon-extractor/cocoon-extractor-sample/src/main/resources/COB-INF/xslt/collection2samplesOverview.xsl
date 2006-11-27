<?xml version="1.0" encoding="UTF-8"?>
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
