<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	xmlns:df="http://mindquarry.com/ns/xml/ductforms"
	xmlns:dir="http://apache.org/cocoon/directory/2.0"
	xmlns:xi="http://www.w3.org/2001/XInclude"
	xmlns:collection="http://apache.org/cocoon/collection/1.0">

	<xsl:template match="@*|node()">
		<xsl:copy>
			<xsl:apply-templates select="@*|node()" />
		</xsl:copy>
	</xsl:template>

	<xsl:template match="/xsl:stylesheet/df:title" />


	<xsl:template match="/xsl:stylesheet">
		<xsl:copy>
			<xsl:apply-templates select="@*|node()" />

			<xsl:element name="xsl:param">
				<xsl:attribute name="name">queries</xsl:attribute>
				<xsl:attribute name="select">
					document('cocoon:/query.xml')
				</xsl:attribute>
			</xsl:element>

			<xsl:element name="xsl:template">
				<xsl:attribute name="match">/</xsl:attribute>
				<html>
					<head>
						<title>
							<xsl:value-of
								select="/xsl:stylesheet/df:title" />
						</title>
					</head>
					<body>
						<ol class="documents">
							<xsl:element name="xsl:apply-templates" />
						</ol>

						<ul class="queries">
							<xsl:element name="xsl:apply-templates">
								<xsl:attribute name="select">
									$queries//df:title
								</xsl:attribute>
							</xsl:element>
						</ul>

						<ul class="actions">
							<li>
								<a>
									<xsl:attribute name="href">
										<xsl:text>./{//collection:new/@name}.xml.edit</xsl:text>
									</xsl:attribute>
									create
								</a>
								a new document
							</li>
						</ul>
					</body>
				</html>
			</xsl:element>

			<xsl:element name="xsl:template">
				<xsl:attribute name="match">df:title</xsl:attribute>
				<li>
					<a>
						<xsl:attribute name="href">
							<xsl:text>{@path}.query</xsl:text>
						</xsl:attribute>
						<xsl:element name="xsl:apply-templates"></xsl:element>
					</a>
				</li>
			</xsl:element>

			<xsl:element name="">

			</xsl:element>

		</xsl:copy>
	</xsl:template>

</xsl:stylesheet>