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
package com.mindquarry.persistence.jcr;

import static com.mindquarry.common.lang.StringUtil.concat;

import java.util.LinkedList;
import java.util.List;

import javax.jcr.Node;
import javax.jcr.PathNotFoundException;
import javax.jcr.RepositoryException;
import javax.jcr.ValueFormatException;

import org.apache.avalon.framework.service.ServiceException;
import org.springmodules.jcr.JcrSessionFactory;

import com.mindquarry.persistence.api.JavaConfiguration;
import com.mindquarry.persistence.api.Session;
import com.mindquarry.persistence.api.SessionFactory;
import com.mindquarry.persistence.jcr.query.QueryException;


public class JcrPersistenceTest extends JcrPersistenceTestBase {
    
    private static final String TEST_USER_ID = "testUser";
    
    private SessionFactory sessionFactory_;
    
    protected void setUp() throws Exception {
        super.setUp();
        
        JavaConfiguration configuration = new JavaConfiguration();
        configuration.addClass(User.class);
        configuration.addClass(Group.class);
        configuration.addClass(Team.class);
        
        Persistence persistence = (Persistence) lookup(Persistence.ROLE);
        persistence.configure(configuration);
        
        sessionFactory_ = persistence;    
    }
    
    private Session currentSession() {
        return sessionFactory_.currentSession();
    }
    
    private User makeTestUser() {
        User user = new User();
        user.setLogin(TEST_USER_ID);
        user.setPwd("pwd");
        user.firstname = "test";
        user.lastname = "test";
        
        return user;
    }
    
    /*
    public void testPersistUser() throws Exception {
        currentSession().persist(makeTestUser());
        
        String contentNodePath = concat(User.PARENT_FOLDER, "/", 
                TEST_USER_ID, "/", "jcr:content");
        
        javax.jcr.Session jcrSession = getJcrSession(); 
        Node rootNode = jcrSession.getRootNode();
        assertTrue(rootNode.hasNode(contentNodePath));
        
        Node contentNode = rootNode.getNode(contentNodePath);
        assertEquals("pwd", textValueOfChild(contentNode, "pwd"));
        assertEquals("test", textValueOfChild(contentNode, "firstname"));
        assertEquals("test", textValueOfChild(contentNode, "lastname"));
        
        rootNode.getNode(User.PARENT_FOLDER).remove();
        jcrSession.save();
    }
    
    private String textValueOfChild(Node contentNode, String childName) 
        throws Exception {
        
        Node textNode = contentNode.getNode(childName + "/text");
        return textNode.getProperty("xt:characters").getString();
    }
    
    /*
    public void testPersistCollectionProperties() throws Exception {        
        User user = new User();
        user.setLogin("testUser");
        user.setPwd("pwd");
        user.firstname = "test";
        user.lastname = "test";        
        currentSession().persist(user);
    }
    */
    public void testUpdateUser() {
        User user = new User();
        user.setLogin("testUser");
        user.setPwd("pwd");
        user.firstname = "test";
        user.lastname = "test";        
        currentSession().persist(user);
        
        List<String> skills = new LinkedList<String>(); 
        skills.add("foo");
        skills.add("bar");        
        
        user.setSkills(skills);        
        user.setSkillsArray(skills.toArray(new String[skills.size()]));
        
        Team team = new Team();
        team.name = "jcr-persistence";
        team.title = "Jcr Persistence";
        team.description = "bla blub";
        
        List<String> bars = new LinkedList<String>();
        bars.add("bla");
        bars.add("blub");        
        team.fooMap.put("foo", bars);
        currentSession().persist(team);
        
        List<Team> teams = new LinkedList<Team>();
        teams.add(team);
        user.setTeams(teams);
        
        currentSession().update(user);
    }
    
    public void testPersistUserReferencingTeams() {
        
    }
    
    public void testQueryUsersForTeam() throws ServiceException {
        
        Session session = sessionFactory_.currentSession();
        List<Object> queryResult = session.query("getUsersForTeam", "jcr-persistence");
        
        assertEquals(1, queryResult.size());
    }
    
    public void testMapProperties() throws ServiceException {
        
        Session session = sessionFactory_.currentSession();
        List<Object> queryResult = session.query("allTeams");
        
        assertEquals(1, queryResult.size());
        
        Team team = (Team) queryResult.get(0);
        assertEquals("blub", team.fooMap.get("foo").get(1));
    }
    
    public void testInvalidQuery() throws ServiceException {
        
        Session session = sessionFactory_.currentSession();
        try {
            session.query("userByLogin");
        }
        catch (QueryException e) {
            return;
        }
        fail();
    }
    
    public void testQueryAllUsers() throws ServiceException {
        
        Session session = sessionFactory_.currentSession();
        List<Object> queryResult = session.query("allUsers");
        
        assertEquals(1, queryResult.size());
    }
    
    public void testQueryAndUpdateUser() throws ServiceException {
        
        Session session = sessionFactory_.currentSession();
        List<Object> queryResult = session.query(
                "userByLogin", "testUser");
        
        User user = (User) queryResult.get(0);
        assertEquals("testUser", user.getLogin());
        assertEquals("test", user.lastname);
        assertEquals(2, user.getSkills().size());
        assertEquals(2, user.getSkillsArray().length);
        assertEquals("jcr-persistence", user.getTeams().get(0).name);
        
        user.lastname = "lastName";
        user.getSkills().remove(0);
        user.setSkillsArray(new String[] {user.getSkillsArray()[0]});
        session.update(user);
        
        
        session = sessionFactory_.currentSession();
        queryResult = session.query(
                "userByLogin", "testUser");
        
        user = (User) queryResult.get(0);
        assertEquals("lastName", user.lastname);
        assertEquals(1, user.getSkills().size());
        assertEquals(1, user.getSkillsArray().length);
    }
    
    public void testDynamicTypedCollection() throws ServiceException {
        
        Session session = sessionFactory_.currentSession();
        List<Object> queryResult = session.query(
                "userByLogin", "testUser");
        
        User user = (User) queryResult.get(0);
        Group group = new Group();
        group.id = "foogroup";
        group.members.add(user);
        
        session.persist(group);
    }
    
    public void testDeleteGroup() throws ServiceException {
        
        Session session = sessionFactory_.currentSession();
        List<Object> queryResult = session.query(
                "groupById", "foogroup");
        
        Group group = (Group) queryResult.get(0);
        assertEquals(1, group.members.size());
        
        session.delete(group);
    }
    
    public void testDeleteUser() throws ServiceException {
        
        Session session = sessionFactory_.currentSession();
        List<Object> queryResult = session.query(
                "userByLogin", "testUser");
        
        User user = (User) queryResult.get(0);
        session.delete(user.getTeams().get(0));
        session.delete(user);
    }
}
