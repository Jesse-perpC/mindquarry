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
package com.mindquarry.webapp.modules;

import java.util.Map;

import org.apache.avalon.framework.configuration.Configuration;
import org.apache.avalon.framework.configuration.ConfigurationException;
import org.apache.cocoon.components.modules.input.AbstractInputModule;

/**
 * 
 * @author <a href="mailto:lars.trieloff@mindquarry.com">
 *         Lars Trieloff</a>
 *
 */
public class DigestStringModule extends AbstractInputModule {
    
    @Override
    public Object getAttribute(final String name, Configuration config, Map objectModel) throws ConfigurationException {
      
        int colonindex = name.indexOf(":");
        
        String before = name.substring(0, colonindex);
        
        int hash = Math.abs(name.hashCode()) % Integer.parseInt(before);
        
        return "" + hash;
    }

}
