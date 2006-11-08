<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0"
        xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
        xmlns:xlink="http://www.w3.org/1999/xlink">

	<xsl:import href="block:/xslt/contextpath.xsl" />
		
	<xsl:param name="teamspaceID" />    

    <xsl:template match="/tasks">
		<html>
			<head>
				<title>Tasks for <xsl:value-of select="$teamspaceID" /></title>
				<link rel="stylesheet" 
					href="{$pathToBlock}css/tasks.css" type="text/css" />
			</head>
			<body>
				<h1>Manage Tasks for 
					<xsl:value-of 
						select="document(concat('jcr:///teamspaces/', $teamspaceID, '/metadata.xml'))//name" />
				</h1>
				
				<div class="nifty">
					<div class="firstlinks">
						<ul>
		        			<li><a class="create_task_button" href="task{count(*)+1}">Create task</a></li>
						</ul>
					</div>
				
					<div class="task-area">
						<table class="task-list">
							<tr>
								<th>Task</th>
								<th>Status</th>
							</tr>
							<xsl:apply-templates>
								<xsl:sort select="id" />
							</xsl:apply-templates>
						</table>
					</div>

					<div class="footbar">
						<a href=".." id="back" title="back to teamspace overview">
							Back to overview</a>				
					</div>
				</div>
			</body>
		</html>
    </xsl:template>

    <xsl:template match="task">
		<tr class="highlight">
			<td>
				<img src="{$pathToBlock}images/status/{normalize-space(status)}.png" class="task_status"/>
				<a href="{@xlink:href}">
				<xsl:choose>
					<xsl:when test="string-length(title) > 0">
						<xsl:value-of select="title" />
					</xsl:when>
					<xsl:otherwise>
						&lt;no title&gt;
					</xsl:otherwise>
				</xsl:choose>
				</a>
				<br/>
				<xsl:value-of select="summary" /></td>
			<td class="task_status">
				<xsl:choose>
					<xsl:when test="string-length(status) > 0">
						<xsl:value-of select="status" />
					</xsl:when>
					<xsl:otherwise>
						&lt;no status&gt;
					</xsl:otherwise>
				</xsl:choose>
			</td>
		</tr>
    </xsl:template>
</xsl:stylesheet>
