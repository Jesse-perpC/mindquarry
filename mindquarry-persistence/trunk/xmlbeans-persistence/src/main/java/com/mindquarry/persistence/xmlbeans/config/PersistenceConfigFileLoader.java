/**
 * Copyright (C) 2006 Mindquarry GmbH, All Rights Reserved
 */
package com.mindquarry.persistence.xmlbeans.config;

import java.io.InputStream;

/**
 * Add summary documentation here.
 *
 * @author <a href="mailto:your-email-address">your full name</a>
 */
public class PersistenceConfigFileLoader extends PersistenceConfigLoader {
 
    private static final String CONFIG_FILE = "/com/mindquarry/persistence/xmlbeans/mindquarry-persistence.xml";
    
    protected InputStream resolveConfig() {
        return getClass().getResourceAsStream(CONFIG_FILE);
    }
}
