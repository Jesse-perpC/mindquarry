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

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.mindquarry.persistence.api.Configuration;


/**
 * Add summary documentation here.
 *
 * @author
 * <a href="mailto:bastian.steinert(at)mindquarry.com">Bastian Steinert</a>
 */
public class JavaConfiguration implements Configuration {
    
    private List<Class<?>> classes_;
    private Map<String, String> namedQueries_;
    
    public JavaConfiguration() {
        classes_ = new LinkedList<Class<?>>();
        namedQueries_ = new HashMap<String, String>(); 
    }
    
    public void addClass(Class<?> clazz) {
        classes_.add(clazz);
    }
    
    public void addNamedQuery(String name, String query) {
        namedQueries_.put(name, query);
    }

    public Collection<Class<?>> getClasses() {
        return classes_;
    }

    public Map<String, String> getNamedQueries() {
        return namedQueries_;
    }
}
