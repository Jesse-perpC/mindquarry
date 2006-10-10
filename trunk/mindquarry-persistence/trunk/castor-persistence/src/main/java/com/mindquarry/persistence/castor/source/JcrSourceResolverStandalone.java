/**
 * Copyright (C) 2006 Mindquarry GmbH, All Rights Reserved
 */
package com.mindquarry.persistence.castor.source;

import java.io.IOException;
import java.net.MalformedURLException;

import org.apache.avalon.framework.configuration.Configuration;
import org.apache.avalon.framework.configuration.ConfigurationException;
import org.apache.excalibur.source.ModifiableTraversableSource;

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
    protected ModifiableTraversableSource 
                        resolveJcrSourceInternal(String jcrPath) {
        try {
            Object sourceObj = jcrSourceFactory_.getSource(jcrPath, null);
            return (ModifiableTraversableSource) sourceObj;
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
