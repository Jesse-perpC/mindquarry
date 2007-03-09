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

import com.mindquarry.common.persistence.Session;
import com.mindquarry.common.persistence.SessionFactory;
import com.mindquarry.persistence.jcr.mapping.MappingManager;
import com.mindquarry.persistence.jcr.session.JcrSessionFactory;

/**
 * Add summary documentation here.
 *
 * @author
 * <a href="mailto:bastian.steinert(at)mindquarry.com">Bastian Steinert</a>
 */
public class JcrPersistence implements SessionFactory, Configuration {

    private List<Class<?>> entityClazzes_;
    private SessionFactory sessionFactory_;
    
    public JcrPersistence() {
        entityClazzes_ = new LinkedList<Class<?>>();
    }
    
    public void addClass(Class<?> clazz) {
        entityClazzes_.add(clazz);
    }
    
    public void configure() {
        MappingManager mappingManager = 
            MappingManager.buildFromClazzes(entityClazzes_);
        sessionFactory_ = new JcrSessionFactory(mappingManager);
    }

    public Session currentSession() {
        return sessionFactory_.currentSession();
    }
}
