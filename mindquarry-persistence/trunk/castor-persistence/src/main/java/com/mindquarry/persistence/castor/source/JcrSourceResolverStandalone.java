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
package com.mindquarry.persistence.castor.source;

import java.io.IOException;
import java.net.MalformedURLException;

import org.apache.avalon.framework.configuration.Configuration;
import org.apache.avalon.framework.configuration.ConfigurationException;
import org.apache.excalibur.source.Source;

import com.mindquarry.common.init.InitializationException;
import com.mindquarry.jcr.xml.source.JCRSourceFactory;

/**
 * Add summary documentation here.
 *
 * @author <a href="mailto:your-email-address">your full name</a>
 */
public class JcrSourceResolverStandalone extends JcrSourceResolverBase {
     
    private JCRSourceFactory jcrSourceFactory_;
    
    public JcrSourceResolverStandalone(Configuration configuration) {
        jcrSourceFactory_ = new JCRSourceFactory();        
        try {
            jcrSourceFactory_.configure(configuration);
        } catch (ConfigurationException e) {
            throw new InitializationException(
                    "error in configuring jcr source factory", e);
        }
    }
    
    @Override
    protected Source resolveJcrSourceInternal(String jcrPath) {
        try {
            return jcrSourceFactory_.getSource(jcrPath, null);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void releaseJcrSource(Source source) {
        jcrSourceFactory_.release(source);
    }
}
