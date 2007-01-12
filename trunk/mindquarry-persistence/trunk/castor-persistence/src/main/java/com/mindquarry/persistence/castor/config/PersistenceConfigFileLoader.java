/*
 * Copyright (C) 2006-2007 MindQuarry GmbH, All Rights Reserved
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
package com.mindquarry.persistence.castor.config;

import java.io.InputStream;

/**
 * Add summary documentation here.
 *
 * @author <a href="mailto:your-email-address">your full name</a>
 */
public class PersistenceConfigFileLoader extends PersistenceConfigLoader {
 
    private static final String CONFIG_FILE = "/com/mindquarry/persistence/mindquarry-persistence.xml";
    
    protected InputStream resolveConfig() {
        getLogger().info("lookup xmlbeans persistence " +
                "configuration file at: " + CONFIG_FILE);
        return getClass().getResourceAsStream(CONFIG_FILE);
    }
}
