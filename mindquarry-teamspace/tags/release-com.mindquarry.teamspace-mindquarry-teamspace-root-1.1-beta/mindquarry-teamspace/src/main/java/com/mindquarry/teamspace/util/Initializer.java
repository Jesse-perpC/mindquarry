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
package com.mindquarry.teamspace.util;

import com.mindquarry.persistence.api.JavaConfiguration;
import com.mindquarry.persistence.api.SessionFactory;
import com.mindquarry.teamspace.manager.TeamspaceEntity;

/**
 * @author
 * <a href="mailto:bastian.steinert(at)mindquarry.com">Bastian Steinert</a>
 */
public class Initializer {
    
    public static final String ROLE = Initializer.class.getName();
    
    private SessionFactory sessionFactory_;

    /**
     * Setter for sessionFactory bean, set by spring at object creation
     */
    public void setSessionFactory(SessionFactory sessionFactory) {
        sessionFactory_ = sessionFactory;
    }
    
    public void initialize() {
        JavaConfiguration configuration = new JavaConfiguration();
        configuration.addClass(TeamspaceEntity.class);
        sessionFactory_.addConfiguration(configuration);
    }
}
