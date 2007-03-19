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
package com.mindquarry.persistence.jcr.session;

import javax.jcr.Credentials;
import javax.jcr.LoginException;
import javax.jcr.Repository;
import javax.jcr.RepositoryException;
import javax.jcr.SimpleCredentials;

import com.mindquarry.persistence.jcr.JcrPersistenceInternalException;
import com.mindquarry.persistence.jcr.Persistence;
import com.mindquarry.persistence.jcr.api.JcrSession;

/**
 * Add summary documentation here.
 *
 * @author
 * <a href="mailto:bastian.steinert(at)mindquarry.com">Bastian Steinert</a>
 */
public class SessionFactory implements com.mindquarry.common.persistence.SessionFactory {

    private ThreadLocal<Session> currentSession_;
    
    private Repository repository_;
    private Persistence persistence_;
    
    public SessionFactory(Repository repository, Persistence persistence) {        
        currentSession_ = new ThreadLocal<Session>();
        repository_ = repository;
        persistence_ = persistence;
    }
    
    /**
     * @see com.mindquarry.common.persistence.SessionFactory#currentSession()
     */
    public Session currentSession() {
        if (currentSession_.get() == null) {
            currentSession_.set(buildJcrSession());
        }
        return currentSession_.get();
    }
    
    private Session buildJcrSession() {
        return new Session(persistence_, createJcrSession());
    }
    
    private JcrSession createJcrSession() {        
        Credentials credentials = 
            new SimpleCredentials("foo", "bar".toCharArray());
        try {
            return new JcrSession(repository_.login(credentials));
        } catch (LoginException e) {
            throw new JcrPersistenceInternalException(e);
        } catch (RepositoryException e) {
            throw new JcrPersistenceInternalException(e);
        }
    }
}
