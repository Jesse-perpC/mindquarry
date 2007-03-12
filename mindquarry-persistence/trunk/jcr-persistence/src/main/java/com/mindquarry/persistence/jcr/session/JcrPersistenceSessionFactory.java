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

import javax.jcr.Repository;

import com.mindquarry.common.persistence.Session;
import com.mindquarry.common.persistence.SessionFactory;
import com.mindquarry.persistence.jcr.mapping.MappingManager;

/**
 * Add summary documentation here.
 *
 * @author
 * <a href="mailto:bastian.steinert(at)mindquarry.com">Bastian Steinert</a>
 */
public class JcrPersistenceSessionFactory implements SessionFactory {

    private Repository repository_;
    private MappingManager mappingManager_;
    
    public JcrPersistenceSessionFactory(MappingManager mappingManager, 
            Repository repository) {
        
        repository_ = repository;
        mappingManager_ = mappingManager;
    }
    
    /**
     * @see com.mindquarry.common.persistence.SessionFactory#currentSession()
     */
    public Session currentSession() {
        return buildJcrSession();
    }
    
    private Session buildJcrSession() {
        return new JcrPersistenceSession(mappingManager_, repository_);
    }
}
