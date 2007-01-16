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
				<script type="text/javascript">
					//dojo.require("dojo.widget.SortableTable");
					dojo.require("mindquarry.widget.SortableHTMLTable");
				</script>
			</head>
			<body>
				<h1>Manage Tasks for 
					<xsl:value-of 
						select="document(concat('jcr:///teamspaces/', $teamspaceID, '/metadata.xml'))//name" />
				</h1>
				
				<div class="nifty">
					<div class="firstlinks">
						<ul>
		        			<li><a class="create_task_button" href="new">Create task</a></li>
	        				<li><a class="create_filter_button" href="filters/new">Create filter</a></li>
						</ul>
					</div>
				
					<div class="task-area">
						<table class="task-list" dojoType="SortableHTMLTable" id="taskList">
							<thead>
								<tr>
									<th contentType="html" dataType="int" valign="top">Status</th>
									<th contentType="html" id="title-col-header">Task</th>
									<th dataType="date">Date</th>
								</tr>
							</thead>
							<tbody>
								<xsl:apply-templates select="task">
									<xsl:sort select="id" />
								</xsl:apply-templates>
							</tbody>
						</table>
					</div>

					<xsl:if test="count(filter) > 0">
						<div class="filter-area">
							<table class="filter-list">
								<thead>
									<tr>
										<th>Filter</th>
									</tr>
								</thead>
								<tbody>
									<xsl:apply-templates select="filter" />
								</tbody>
							</table>
						</div>
					</xsl:if>

					<div class="footbar">
						<a href=".." id="back" title="back to teamspace overview">
							Back to overview</a>				
					</div>
				</div>
			</body>
		</html>
    </xsl:template>

    <xsl:template match="task">
    	<tr class="highlight" value="{@xlink:href}">
    		<!-- Note: attributes here (e.g. class) will get lost due to SortableHTMLTable -->
    		<td>
    			<!-- convert status value into number for sortin -->
    			<xsl:attribute name="sortValue">
    				<xsl:choose>
    					<xsl:when test="status='new'">1</xsl:when>
    					<xsl:when test="status='running'">2</xsl:when>
    					<xsl:when test="status='paused'">3</xsl:when>
    					<xsl:when test="status='done'">4</xsl:when>
    				</xsl:choose>
    			</xsl:attribute>
    			<div class="task_status">
    				<img src="{$pathToBlock}images/status/{normalize-space(status)}.png" alt="{status}" class="task_status"/>
    			</div>
    		</td>
    		<td sortValue="{title}">
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
				<div class="summary">
					<xsl:value-of select="summary" />
				</div>
			</td>
    		<td>
    			<xsl:value-of select="date" />
    		</td>
    	</tr>
    </xsl:template>

	<xsl:template match="filter">
		<tr>
			<td>
				<a href="{@xlink:href}"><xsl:value-of select="title"/></a>
			</td>
		</tr>
	</xsl:template>
	
</xsl:stylesheet>
