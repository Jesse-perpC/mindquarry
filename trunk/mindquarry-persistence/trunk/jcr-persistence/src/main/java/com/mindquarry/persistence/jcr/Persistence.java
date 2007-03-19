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

import java.util.LinkedList;
import java.util.List;

import javax.jcr.Repository;

import com.mindquarry.common.persistence.Session;
import com.mindquarry.persistence.jcr.cmds.CommandManager;
import com.mindquarry.persistence.jcr.model.Model;
import com.mindquarry.persistence.jcr.query.DefaultQueryResolver;
import com.mindquarry.persistence.jcr.query.QueryResolver;
import com.mindquarry.persistence.jcr.session.SessionFactory;
import com.mindquarry.persistence.jcr.trafo.TransformationManager;

/**
 * Add summary documentation here.
 *
 * @author
 * <a href="mailto:bastian.steinert(at)mindquarry.com">Bastian Steinert</a>
 */
public class Persistence implements Configuration, 
             com.mindquarry.common.persistence.SessionFactory {

    private List<Class<?>> entityClazzes_;
    private SessionFactory sessionFactory_;
    private Repository repository_;
    
    private Model model_;
    private QueryResolver queryResolver_;
    private CommandManager commandManager_;    
    private TransformationManager transformationManager_;
    
    public Persistence() {
        entityClazzes_ = new LinkedList<Class<?>>();
    }
    
    public void setRepository(Repository repository) {
        repository_ = repository;
    }

    public Session currentSession() {
        return sessionFactory_.currentSession();
    }
    
    public void addClass(Class<?> clazz) {
        entityClazzes_.add(clazz);
    }
    
    public void configure() {        
        model_ = Model.buildFromClazzes(entityClazzes_);
        
        transformationManager_ = new TransformationManager(model_, this);        
        transformationManager_.initialize();
        
        queryResolver_ = new DefaultQueryResolver(model_);
        queryResolver_.initialize();
        
        commandManager_ = new CommandManager(this);        
        
        
        sessionFactory_ = new SessionFactory(repository_, this);        
    }
    
    public QueryResolver getQueryResolver() {
        return queryResolver_;
    }
    
    public CommandManager getCommandManager() {
        return commandManager_;
    }
    
    public TransformationManager getTransformationManager() {
        return transformationManager_;
    }
}
