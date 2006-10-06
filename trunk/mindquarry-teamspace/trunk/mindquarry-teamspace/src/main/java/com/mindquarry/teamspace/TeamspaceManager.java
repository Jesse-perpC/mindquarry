/**
 * Copyright (C) 2006 Mindquarry GmbH, All Rights Reserved
 */
package com.mindquarry.teamspace;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

import org.apache.avalon.framework.activity.Initializable;
import org.apache.avalon.framework.configuration.Configurable;
import org.apache.avalon.framework.configuration.Configuration;
import org.apache.avalon.framework.configuration.ConfigurationException;
import org.apache.avalon.framework.service.ServiceException;
import org.apache.avalon.framework.service.ServiceManager;
import org.apache.avalon.framework.service.Serviceable;

import com.mindquarry.common.init.InitializationException;
import com.mindquarry.common.persistence.Session;
import com.mindquarry.common.persistence.SessionFactory;
import com.mindquarry.types.teamspace.Teamspace;
import com.mindquarry.types.teamspace.TeamspaceReferences;
import com.mindquarry.types.user.User;

/**
 * Add summary documentation here.
 *
 * @author 
 * <a href="mailto:bastian.steinert(at)mindquarry.com">your full name</a>
 */
class TeamspaceManager implements TeamspaceAdmin,
    Serviceable, Configurable, Initializable {

    static final String REPOS_BASE_PATH_PROPERTY = "mindquarry.reposbasepath";
    
    private String reposBasePath_;
    
    private SessionFactory sessionFactory_;
    
    private File reposBaseDirectory_;
    
    /**
     * {@inheritDoc}
     * @see org.apache.avalon.framework.service.Serviceable#service(org.apache.avalon.framework.service.ServiceManager)
     */
    public void service(ServiceManager serviceManager) throws ServiceException {
        String name = SessionFactory.class.getName();
        sessionFactory_ = (SessionFactory) serviceManager.lookup(name);
    }

    /**
     * {@inheritDoc}
     * @see org.apache.avalon.framework.configuration.Configurable#configure(org.apache.avalon.framework.configuration.Configuration)
     */
    public void configure(Configuration configuration) 
        throws ConfigurationException {
        
        reposBasePath_ = configuration.getAttribute("reposbasepath", null);

        if (null != System.getProperty(REPOS_BASE_PATH_PROPERTY))
            reposBasePath_ = System.getProperty(REPOS_BASE_PATH_PROPERTY);
        
        if (null == reposBasePath_) 
            throw new ConfigurationException(
                    "'mindquarry.reposbasepath' is not set, whether as " +
                    "container configuration nor as system property. " +
                    "It must be set to a valid, " +
                    "existing base directory for repositories");
    }

    /**
     * @throws Exception
     */
    public void initialize() throws Exception {
        
        reposBaseDirectory_ = new File(reposBasePath_);       
        
        if (! reposBaseDirectory_.exists() 
                || ! reposBaseDirectory_.isDirectory()) 
            throw new InitializationException(
                    "'mindquarry.reposbasepath' is not set to a valid, " +
                    "existing base directory for repositories.");        
    }

    public void create(String id, String name, String description) {
        Session session = currentSession();
        Teamspace teamspace = (Teamspace) session.newEntity(Teamspace.class);
        teamspace.setId(id);
        teamspace.setName(name);
        teamspace.setDescription(description);
        session.commit();
    }

    public void remove(String id) {
        Session session = currentSession();
        Teamspace teamspace = queryTeamspaceById(session, id);
        session.delete(teamspace);
        session.commit();
    }

    public List<Object> allTeamspaces() {
        Session session = currentSession();
        List queryResult = session.query("getAllTeamspaces", new Object[0]);
        
        List<Object> result = new LinkedList<Object>();
        for (Object teamspace : queryResult)
            result.add(teamspace);
        
        session.commit();
        return result;
    }

    public List<Object> teamspacesForUser(String userId) {
        Session session = currentSession();
        User user = queryUserById(session, userId);
        
        List<Object> result = new LinkedList<Object>();
        
        TeamspaceReferences teamRefs = user.getTeamspaces();
        for (String teamRef : teamRefs.getTeamspaceReferenceArray()) {
            Teamspace teamspace = queryTeamspaceById(session, teamRef);
            result.add(teamspace);
        }
        
        session.commit();
        return result;
    }
    
    private Teamspace queryTeamspaceById(Session session, String id) {
        List queryResult = session.query("getTeamspaceById", new Object[] {id});
        return (Teamspace) queryResult.get(0);
    }
    
    private User queryUserById(Session session, String id) {
        List queryResult = session.query("getUserById", new Object[] {id});
        return (User) queryResult.get(0);
    }

    public String workspaceUri(String id) {
        Session session = currentSession();
        Teamspace teamspace = queryTeamspaceById(session, id);
        String result = teamspace.getWorkspaceUri();
        session.commit();
        return result;
    }
    
    private Session currentSession() {
        return sessionFactory_.currentSession();
    }

}
