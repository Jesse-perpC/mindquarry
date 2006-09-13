<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	xmlns:log="http://mindquarry.com/ns/schema/changelog"
	xmlns="http://www.w3.org/2005/Atom">
	
	<xsl:param name="projectname"/>
	<xsl:param name="baselink"/>
	
	<xsl:template match="/log:changelog">
		<feed>
			<title>Recent changes for documents in <xsl:value-of select="$projectname" /></title>
			<link href="{$baselink}" />
			<updated><xsl:value-of select="log:change[last()]/@date" /></updated>
			<id>feed:<xsl:value-of select="$baselink" />?show=atom</id>
			<xsl:apply-templates select="log:change" />
		</feed>
	</xsl:template>
	
	<xsl:template match="log:change">
		<entry>
			<title>Revision <xsl:value-of select="@revision" /> by <xsl:value-of select="@author" /></title>
			<!-- there should be a shortname to realname mapping -->
			<author><name><xsl:value-of select="@name" /></name></author>
			<id>feed:<xsl:value-of select="$baselink" />?show=atom&amp;revision<xsl:value-of select="@revision" /></id>
			<!-- no link yet, as there is no display of individual changesets, otherwise <link>...</link> -->
			<updated><xsl:value-of select="@date" /></updated>
			<summary><xsl:value-of select="@message" /></summary>
			<content type="xhtml">
				<xsl:value-of select="@author" />: <blockquote xmlns="http://www.w3.org/1999/xhtml"><xsl:value-of select="@message" /></blockquote>
				<ul  xmlns="http://www.w3.org/1999/xhtml" title="changed paths">
					<xsl:apply-templates select="log:path" />
				</ul>
			</content>
		</entry>
	</xsl:template>
	
	<xsl:template match="log:path">
		<li  xmlns="http://www.w3.org/1999/xhtml">
			<xsl:value-of select="@src" />
		</li>
	</xsl:template>
	
	<!-- 
	
<?xml version="1.0" encoding="utf-8"?>
<feed xmlns="http://www.w3.org/2005/Atom" xml:lang="de">

  <title>Name des Weblogs</title>
  <link href="http://example.org/"/>
  <updated>2003-12-13T18:30:02Z</updated>
  <author>
    <name>Autor des Weblogs</name>
  </author>
  <id>urn:uuid:60a76c80-d399-11d9-b93C-0003939e0af6</id>

  <entry>
    <title>Titel des Weblog-Eintrags</title>
    <link href="http://example.org/2003/12/13/atom-beispiel"/>
    <id>urn:uuid:1225c695-cfb8-4ebb-aaaa-80da344efa6a</id>
    <updated>2003-12-13T18:30:02Z</updated>
    <summary>Zusammenfassung des Weblog-Eintrags</summary>
    <content>Volltext des Weblog-Eintrags</content>
  </entry>

</feed>
	
	
	
	 -->
</xsl:stylesheet>