/**
 * Copyright (C) 2006 Mindquarry GmbH, All Rights Reserved
 */
package com.mindquarry.persistence.jcr;

import java.util.LinkedList;
import java.util.List;

import com.mindquarry.common.persistence.Session;
import com.mindquarry.common.persistence.SessionFactory;

/**
 * Add summary documentation here.
 *
 * @author
 * <a href="mailto:bastian.steinert(at)mindquarry.com">Bastian Steinert</a>
 */
public class JcrPersistence implements SessionFactory, Configuration {

    private List<Class<?>> modelClazzes_;
    
    public JcrPersistence() {
        modelClazzes_ = new LinkedList<Class<?>>();
    }
    
    public void addClass(Class<?> clazz) {
        modelClazzes_.add(clazz);
    }
    
    public void configure() {
    }

    public Session currentSession() {
        // TODO Auto-generated method stub
        return null;
    }
}
