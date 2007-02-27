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
package com.mindquarry.persistence.castor;

import java.net.URL;

import org.apache.avalon.framework.activity.Initializable;
import org.apache.avalon.framework.logger.AbstractLogEnabled;
import org.exolab.castor.mapping.Mapping;

import com.mindquarry.common.init.InitializationException;
import com.mindquarry.common.persistence.Session;
import com.mindquarry.common.persistence.SessionFactory;
import com.mindquarry.persistence.config.PersistenceConfiguration;
import com.mindquarry.persistence.castor.source.JcrSourceResolverBase;

public abstract class CastorSessionFactoryBase extends AbstractLogEnabled 
    implements SessionFactory, Initializable {

    protected PersistenceConfiguration configuration_;
    
    protected JcrSourceResolverBase jcrSourceResolver_;
    
    protected Mapping mapping_;

    /**
     * @see org.apache.avalon.framework.activity.Initializable#initialize()
     */
    public void initialize() throws Exception {
        configuration_ = makeConfiguration();
        
        mapping_ = new Mapping();
        
        for (Class entityClazz : configuration_.entityClazzes()) {
            URL url = entityClazz.getResource("castor-mapping.xml");
            if (url != null)
                mapping_.loadMapping(url);
            else
                throw new InitializationException("could not load " +
                        "castor-mapping file for entity class: " +
                        entityClazz.getName() + "." +
                        "The file is expected to find within " +
                        "the classpath at: " + 
                        entityClazz.getPackage().getName() + ".");
        }
    }

    public abstract Session currentSession();
    
    public abstract PersistenceConfiguration makeConfiguration()
        throws Exception;
}