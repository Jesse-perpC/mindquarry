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
package com.mindquarry.jcr.jackrabbit;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;

import javax.jcr.RepositoryException;
import javax.jcr.Session;

import org.springmodules.jcr.JcrSessionFactory;
import org.springmodules.jcr.SessionFactory;
import org.springmodules.jcr.SessionHolder;

/**
 * @author 
 * <a href="mailto:bastian.steinert(at)mindquarry.com">Bastian Steinert</a>
 */
public class AttributableSessionFactory implements SessionFactory {

    private JcrSessionFactory sessionFactory_;

    /**
     * Set the target JCR Repository that this proxy should
     * delegate to wrapped in a JcrSessionFactory object along
     * with the credentials and workspace.
     * 
     */
    public void setTargetFactory(JcrSessionFactory target) {
        sessionFactory_ = target;
    }
    
    /**
     * @see org.springmodules.jcr.SessionFactory#getSession()
     */
    public AttributableSession getSession() throws RepositoryException {
        
        return (AttributableSession) Proxy.newProxyInstance(
                AttributableSession.class.getClassLoader(),
                new Class[] { AttributableSession.class }, 
                newProxyInvocationHandler());
    }
    
    private InvocationHandler newProxyInvocationHandler() 
        throws RepositoryException {
        
        return new AttributableSessionInvocationHandler(proxyTarget());
    }
    
    private Session proxyTarget() throws RepositoryException {
        return sessionFactory_.getSession();
    }

    /**
     * @see org.springmodules.jcr.SessionFactory#getSessionHolder(javax.jcr.Session)
     */
    public SessionHolder getSessionHolder(Session session) {
        return sessionFactory_.getSessionHolder(session);
    }
}
