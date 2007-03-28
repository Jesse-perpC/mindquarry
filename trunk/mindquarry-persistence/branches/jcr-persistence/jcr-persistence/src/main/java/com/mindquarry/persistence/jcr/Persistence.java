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

import java.util.List;

import javax.jcr.RepositoryException;

import org.springmodules.jcr.JcrSessionFactory;

import com.mindquarry.common.init.InitializationException;
import com.mindquarry.persistence.api.Configuration;
import com.mindquarry.persistence.api.SessionFactory;
import com.mindquarry.persistence.jcr.cmds.CommandProcessor;
import com.mindquarry.persistence.jcr.model.Model;
import com.mindquarry.persistence.jcr.query.DefaultQueryResolver;
import com.mindquarry.persistence.jcr.query.QueryResolver;
import com.mindquarry.persistence.jcr.trafo.TransformationManager;

/**
 * Add summary documentation here.
 *
 * @author
 * <a href="mailto:bastian.steinert(at)mindquarry.com">Bastian Steinert</a>
 */
public class Persistence implements SessionFactory {

    private List<Class<?>> entityClazzes_;
    private JcrSessionFactory jcrSessionFactory_;
    
    private Model model_;
    private QueryResolver queryResolver_;
    private CommandProcessor commandProcessor_;    
    private TransformationManager transformationManager_;
    
    private ThreadLocal<Session> currentSession_;
    
    public Persistence() {
        currentSession_ = new ThreadLocal<Session>();
    }
    
    public void setJcrSessionFactory(JcrSessionFactory jcrSessionFactory) {
        jcrSessionFactory_ = jcrSessionFactory;
    }
    
    public void addClass(Class<?> clazz) {
        entityClazzes_.add(clazz);
    }
    
    public void configure(Configuration configuration) {        
        model_ = Model.buildFromClazzes(configuration.getClasses());
        
        transformationManager_ = new TransformationManager(model_, this);        
        transformationManager_.initialize();
        
        queryResolver_ = new DefaultQueryResolver();
        queryResolver_.initialize(configuration);
        
        commandProcessor_ = new CommandProcessor(this);    
    }
    
    public QueryResolver getQueryResolver() {
        if (queryResolver_ == null) 
            throw new InitializationException("query resolver is requested " +
                    "though persistence component is not yet configured");
        
        return queryResolver_;
    }
    
    public CommandProcessor getCommandProcessor() {
        if (commandProcessor_ == null) 
            throw new InitializationException("CommandProcessor is requested " +
                    "though persistence component is not yet configured");
        
        return commandProcessor_;
    }
    
    public TransformationManager getTransformationManager() {
        if (transformationManager_ == null) 
            throw new InitializationException("TransformationManager is " +
                    "requested though persistence component " +
                    "is not yet configured");
        
        return transformationManager_;
    }
    
    public Model getModel() {
        if (model_ == null) 
            throw new InitializationException("Model is " +
                    "requested though persistence component " +
                    "is not yet configured");
        
        return model_;
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
        return new Session(this, createJcrSession());
    }
    
    private javax.jcr.Session createJcrSession() {
        try {
            return jcrSessionFactory_.getSession();
        } catch (RepositoryException e) {
            throw new JcrPersistenceInternalException(e);
        }
    }
}
