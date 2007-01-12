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
package com.mindquarry.jcr.jackrabbit;

import org.apache.avalon.framework.configuration.Configuration;
import org.apache.avalon.framework.configuration.ConfigurationException;
import org.apache.avalon.framework.service.ServiceException;

/**
 * This extension of the Cocoon internal Jackrabbit repository component ensures
 * the {@link JackrabbitInitializer} component runs and initializes the
 * repository.
 * 
 * @author <a href="mailto:alexander(dot)klimetschek(at)mindquarry(dot)com">
 *         Alexander Klimetschek</a>
 */
public class JackrabbitRepository extends
        org.apache.cocoon.jcr.JackrabbitRepository {
    
    /**
     * @see org.apache.cocoon.jcr.JackrabbitRepository#configure(org.apache.avalon.framework.configuration.Configuration)
     */
    @Override
    public void configure(Configuration config) throws ConfigurationException {
        super.configure(config);

        // must run the initializer on repository startup
        try {
            this.manager.lookup(JackrabbitInitializer.ROLE);
        } catch (ServiceException e) {
            getLogger().error("Cannot initializes Jackrabbit repository.", e);
        }
    }
}
