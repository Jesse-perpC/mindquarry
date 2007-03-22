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
package com.mindquarry.jcr.client;

import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

import javax.jcr.LoginException;
import javax.jcr.Repository;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.SimpleCredentials;

import org.apache.avalon.framework.configuration.Configurable;
import org.apache.avalon.framework.configuration.Configuration;
import org.apache.avalon.framework.configuration.ConfigurationException;
import org.apache.avalon.framework.logger.AbstractLogEnabled;
import org.apache.avalon.framework.service.ServiceException;
import org.apache.avalon.framework.service.ServiceManager;
import org.apache.avalon.framework.service.Serviceable;
import org.apache.jackrabbit.rmi.client.ClientRepositoryFactory;

import com.mindquarry.common.init.InitializationException;

/**
 * Component that has everything to act as a client for a JCR repository. It is
 * configured with the credentials (username and password) and the optional URL
 * for an RMI remote repository (if none is specified, a local JCR will be used
 * via a lookup for a component under the javax.jcr.Repository name).
 * 
 * @author <a href="mailto:alexander(dot)klimetschek(at)mindquarry(dot)com">
 *         Alexander Klimetschek</a>
 * @author <a href="mailto:alexander(dot)saar(at)mindquarry(dot)com">
 *         Alexander Saar</a>
 * 
 */
public class JCRClient extends AbstractLogEnabled implements Configurable,
        Serviceable {

    /**
     * The reference to the JCR Repository to use as interface.
     */
    protected Repository repo;

    /**
     * Used to resolve other components.
     */
    protected ServiceManager manager;

    /**
     * Configuration: the url of an remote RMI repository (optional)
     */
    private String remoteRepoUrl;

    /**
     * Configuration: the username to log into the repository
     */
    private String username;

    /**
     * Configuration: the password to log into the repository
     */
    private String password;

    public void configure(Configuration config) throws ConfigurationException {
        this.username = config.getAttribute("login"); //$NON-NLS-1$
        this.password = config.getAttribute("password"); //$NON-NLS-1$
        this.remoteRepoUrl = config.getAttribute("rmi", null); //$NON-NLS-1$
    }

    public void service(ServiceManager manager) throws ServiceException {
        this.manager = manager;
    }

    // =========================================================================
    // Public interface
    // =========================================================================

    /**
     * Logs into the repository and returns a JCR session if successfull.
     * 
     * @throws RepositoryException
     * @throws LoginException
     */
    public Session getSession() throws LoginException, RepositoryException {
        Session session = null;

        boolean isRemoteRepositoryConfigured = null != this.remoteRepoUrl;
        if (isRemoteRepositoryConfigured) {
            // check if already initialized
            if (repo == null) {
                // otherwise try to lookup the repository
                repo = createClientRepository(remoteRepoUrl);

                if (getLogger().isInfoEnabled()) {
                    getLogger().info(
                            "Connected to JCR on '" + remoteRepoUrl + "'."); //$NON-NLS-2$
                }
            }
            return repo.login(new SimpleCredentials(username, password
                    .toCharArray()));
        } else {
            session = lookupSession();
            if (getLogger().isInfoEnabled()) {
                getLogger().info("Opened local JCR repository.");
            }
        }
        return session;
    }

    // =========================================================================
    // Internal methods
    // =========================================================================

    private Session lookupSession() {
        try {
            return (Session) manager.lookup("jcrSession"); //$NON-NLS-1$
        } catch (ServiceException e) {
            throw new InitializationException("Cannot lookup jcr session.", e);
        }
    }

    private Repository createClientRepository(String remoteRepoUrl) {
        ClientRepositoryFactory factory = new ClientRepositoryFactory();
        try {
            return factory.getRepository(remoteRepoUrl);
        } catch (ClassCastException e) {
            throw new InitializationException("could not create client "
                    + "jcr repository for repository URL:" + remoteRepoUrl, e);
        } catch (MalformedURLException e) {
            throw new InitializationException("could not create client "
                    + "repository for repository URL:" + remoteRepoUrl, e);
        } catch (RemoteException e) {
            throw new InitializationException("could not create client "
                    + "repository for repository URL:" + remoteRepoUrl, e);
        } catch (NotBoundException e) {
            throw new InitializationException("could not create client "
                    + "repository for repository URL:" + remoteRepoUrl, e);
        }
    }
}