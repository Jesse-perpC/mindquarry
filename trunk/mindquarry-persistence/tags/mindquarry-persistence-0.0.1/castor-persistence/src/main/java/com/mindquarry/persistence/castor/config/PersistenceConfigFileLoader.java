/**
 * Copyright (C) 2006 Mindquarry GmbH, All Rights Reserved
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
