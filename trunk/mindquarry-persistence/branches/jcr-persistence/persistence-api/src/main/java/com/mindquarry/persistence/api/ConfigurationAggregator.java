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
package com.mindquarry.persistence.api;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;



/**
 * Add summary documentation here.
 *
 * @author
 * <a href="mailto:bastian.steinert(at)mindquarry.com">Bastian Steinert</a>
 */
public class ConfigurationAggregator implements Configuration {
    
    private Collection<Configuration> configurations_;
    
    public ConfigurationAggregator() {
        configurations_ = new LinkedList<Configuration>(); 
    }
    
    public void addConfiguration(Configuration configuration) {
        configurations_.add(configuration);
    }
    
    public void setConfigurations(Collection<Configuration> configurations) {
        configurations_ = configurations;
    }

    public Collection<Class<?>> getClasses() {
        Collection<Class<?>> result = new LinkedList<Class<?>>();
        for (Configuration configuration : configurations_) {
            result.addAll(configuration.getClasses());
        }
        return result;
    }

    public Map<String, String> getNamedQueries() {
        Map<String, String> result = new HashMap<String, String>();
        for (Configuration configuration : configurations_) {
            result.putAll(configuration.getNamedQueries());
        }
        return result;
    }
}
