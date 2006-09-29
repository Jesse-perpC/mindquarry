/**
 * Copyright (C) 2006 Mindquarry GmbH, All Rights Reserved
 */
package com.mindquarry.persistence.xmlbeans.source;

import java.io.IOException;
import java.net.MalformedURLException;

import org.apache.avalon.framework.configuration.ConfigurationException;
import org.apache.avalon.framework.configuration.DefaultConfiguration;
import org.apache.excalibur.source.ModifiableSource;

import com.mindquarry.common.init.InitializationException;
import com.mindquarry.jcr.xml.source.JCRSourceFactory;

/**
 * Add summary documentation here.
 *
 * @author <a href="mailto:your-email-address">your full name</a>
 */
class JcrSourceResolverStandalone extends JcrSourceResolverBase {
     
    private JCRSourceFactory jcrSourceFactory_;
    
    JcrSourceResolverStandalone() {
        jcrSourceFactory_ = new JCRSourceFactory();
        
        DefaultConfiguration jcrCredentialsConfig = new DefaultConfiguration("credentials");
        jcrCredentialsConfig.setAttribute("login", "alexander.saar");
        jcrCredentialsConfig.setAttribute("password", "myPwd");
        jcrCredentialsConfig.setAttribute("rmi", "rmi://localhost:1099/jackrabbit");
        
        try {
            jcrSourceFactory_.configure(jcrCredentialsConfig);
        } catch (ConfigurationException e) {
            throw new InitializationException(
                    "error in configuring jcr source factory", e);
        }
    }
    
    @Override
    protected ModifiableSource resolveJcrSourceInternal(String jcrPath) {
        try {
            return jcrSourceFactory_.getSource(jcrPath, null);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
