<?xml version="1.0" encoding="UTF-8"?>
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
<xsl:stylesheet version="1.0"
        xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
        xmlns:xlink="http://www.w3.org/1999/xlink"
        xmlns:team="http://mindquarry.com/ns/schema/teamtransform"
		xmlns:fd="http://apache.org/cocoon/forms/1.0#definition">

	<xsl:import href="servlet:/xslt/contextpath.xsl" />
		
	<xsl:param name="teamspaceID" />    
	<xsl:param name="username" />
	
	<xsl:variable name="teamspaceUsers"
		select="document(concat('servlet:teams:/', $teamspaceID, '/members/asFormsSelectionlist'))"/>
	
	<xsl:template match="/tasks">
		<html>
			<head>
				<title>Tasks for <team:team><xsl:value-of select="$teamspaceID" /></team:team></title>
				<link rel="stylesheet" 
					href="{$pathToBlock}css/tasks.css" type="text/css" />
				<!--<script type="text/javascript">
					dojo.require("mindquarry.widget.SortableHTMLTable");
				</script>-->
				<link rel="alternate" href="" type="application/atom+xml" title="Feed of tasks" />
				<link rel="alternate" href="" type="text/calendar" title="Web Calendar (iCal)" />
				<link rel="alternate" href="" type="application/pdf" title="PDF for print" />
				
				<link rel="breadcrumb" title="Tasks"/>
			</head>
			<body>
				<h1>
					Manage Tasks for 
					<team:team><xsl:value-of select="$teamspaceID" /></team:team>
				</h1>
				
				<div class="nifty">
					<div class="firstlinks">
						<ul>
							<li><a class="create_task_button" href="new">Create task</a></li>
							<li><a class="create_filter_button" href="filters/new">Create filter</a></li>
						</ul>
					</div>
					<div class="task-area">
						<!--<table class="task-list" dojoType="SortableHTMLTable" id="taskList">-->
						<table class="task-list" id="taskList">
							<thead>
								<tr>
									<th contentType="html" dataType="Number"                       valign="top"></th>
									<th contentType="html"                   id="title-col-header" valign="top">Task</th>
									<th                    dataType="Date"   style="width:70px;"   valign="top">Due Date</th>
									<th contentType="html"                                         valign="top">People</th>
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
						<div class="filters">
							<h3>Saved Filters</h3>
							<ul>
								<xsl:call-template name="filters" />
							</ul>
						</div>
					</xsl:if>
				</div>
				<!-- replaced by breadcrumbs-->
				<!--div class="nifty">
					<a href=".." id="back">Back to overview</a>				
				</div-->
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
    		
    		<xsl:variable name="meOrFirstPerson">
    			<xsl:choose>
    				<xsl:when test="people/*">
    					<xsl:choose>
    						<!-- have the user itself at the top when sorting ascendingly -->
    						<xsl:when test="people//item[person=$username]">
    							<xsl:text>000</xsl:text><xsl:value-of select="$username"/>
		    				</xsl:when>
		    				<xsl:otherwise>
		    					<xsl:value-of select="people//item[position()=1]/person"/>
		    				</xsl:otherwise>
    					</xsl:choose>
    				</xsl:when>
    				<!-- in the case when no user is assigned put them at the bottom -->
    				<xsl:otherwise>
    					<xsl:text>zzz_no_people</xsl:text>
    				</xsl:otherwise>
    			</xsl:choose>
    		</xsl:variable>
    		
    		<td sortValue="{$meOrFirstPerson}">
    			<ul class="members">
    				<xsl:apply-templates select="people/*"/>
    			</ul>
    		</td>
    	</tr>
    </xsl:template>
	
	<xsl:template match="people/item">
		<xsl:variable name="person" select="normalize-space(person)"/>
		<xsl:variable name="role" select="normalize-space(role)"/>
		<xsl:variable name="personFullName" select="$teamspaceUsers/fd:selection-list/fd:item[@value=$person]/fd:label"/>
		<li title="{$personFullName} has role {$role}">
			<img src="{$pathToRoot}teams/users/48/{$person}.png" />
			<!--<xsl:copy-of select="$personFullName"/>-->
		</li>		
	</xsl:template>

	<xsl:template name="filters">
		<xsl:for-each select="filter">
			<li>
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
			</li>
		</xsl:for-each>
	</xsl:template>
	
</xsl:stylesheet>
