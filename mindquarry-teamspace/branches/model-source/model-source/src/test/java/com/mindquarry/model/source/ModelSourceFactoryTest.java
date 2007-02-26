/*
 * Copyright (C) 2006-2007 Mindquarry GmbH, All Rights Reserved
 * 
 * The contents of this file are subject to the Mozilla Public License
 * Version 1.1 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * http://www.mozilla.org/MPL/
 *
 * Software distributed under the License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
 * License for the specific language governing rights and limitations
 * under the License.
 */
package com.mindquarry.model.source;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;

import org.apache.commons.io.IOUtils;
import org.apache.excalibur.source.Source;
import org.apache.excalibur.source.SourceResolver;

import com.mindquarry.model.source.ModelSource;
import com.mindquarry.teamspace.TeamspaceAdmin;
import com.mindquarry.teamspace.TeamspaceRO;
import com.mindquarry.user.User;
import com.mindquarry.user.UserAdmin;

/**
 * @author <a href="bastian(dot)steinert(at)mindquarry(dot)com">Bastian Steinert</a>
 */
public class ModelSourceFactoryTest extends ModelSourceTestBase {
	
	SourceResolver resolver_;

	protected void setUp() throws Exception {
		super.setUp();
		resolver_ = (SourceResolver) lookup(SourceResolver.ROLE);
	}

	public void testQueryTeamById() throws Exception {
		
		TeamspaceAdmin teamsAdmin = lookupTeamspaceAdmin();
		UserAdmin userAdmin = lookupUserAdmin();
		
        User mqUser = userAdmin.createUser("mindquarry-user", "aSecretPassword",
                "Mindquarry User", "surname", "an email", "the skills");
        
        TeamspaceRO team = teamsAdmin.createTeamspace("mindquarry-teamspace",
                "Mindquarry Teamspace", "a greate description", mqUser);
        
        String resourceUrl = "model://TeamQuery#teamspaceById(" + team.getId() + ")";
        Source source = resolveSource(resourceUrl, ModelSource.class);
        InputStream inputStream = source.getInputStream();
        assertNotNull(inputStream);
        assertTrue(inputStream.available() > 0);
        IOUtils.copy(inputStream, System.out);
	}

	public void testQueryMembersForTeam() throws Exception {
		
		TeamspaceAdmin teamsAdmin = lookupTeamspaceAdmin();
		UserAdmin userAdmin = lookupUserAdmin();
		
        User mqUser = userAdmin.createUser("mindquarry-user", "aSecretPassword",
                "Mindquarry User", "surname", "an email", "the skills");
        
        TeamspaceRO team = teamsAdmin.createTeamspace("mindquarry-teamspace",
                "Mindquarry Teamspace", "a greate description", mqUser);
        
        String resourceUrl = "model://UserQuery#membersForTeamspace(" + team.getId() + ")";
        Source source = resolveSource(resourceUrl, ModelSource.class);
        InputStream inputStream = source.getInputStream();
        assertNotNull(inputStream);
        assertTrue(inputStream.available() > 0);
        IOUtils.copy(inputStream, System.out);
	}

	private Source resolveSource(String url, Class expectedSourceClass) throws MalformedURLException, IOException {
		Source source = resolver_.resolveURI(url);
		assertNotNull(source);
		assertEquals(expectedSourceClass, source.getClass());
		return source;
	}

}
