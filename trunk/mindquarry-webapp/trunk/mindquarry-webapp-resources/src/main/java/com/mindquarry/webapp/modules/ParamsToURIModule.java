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

import java.io.UnsupportedEncodingException;
import java.util.Iterator;
import java.util.Map;
import java.util.Vector;

import org.apache.avalon.framework.configuration.Configuration;
import org.apache.avalon.framework.configuration.ConfigurationException;
import org.apache.cocoon.components.modules.input.AbstractMetaModule;
import org.apache.cocoon.components.modules.input.InputModule;
import org.apache.cocoon.util.NetUtils;

/**
 * @author <a href="mailto:alexander(dot)klimetschek(at)mindquarry(dot)com">
 *         Alexander Klimetschek</a>
 * 
 */
public class ParamsToURIModule extends AbstractMetaModule {

    public Object getAttribute(String name, Configuration modeConf,
            Map objectModel) throws ConfigurationException {

        if (name == null) {
            return null;
        }

        lazy_initialize();
        
        StringBuffer uri = new StringBuffer();

        InputModule module = obtainModule(name);
        if (module != null) {

            final String encoding = (String) this.settings.get("encoding",
                    "utf-8");

            try {
                // for each attribute name, create a name=value pair in the uri
                Iterator names = module
                        .getAttributeNames(modeConf, objectModel);
                while (names.hasNext()) {
                    String n = (String) names.next();
                    Object[] values = module
                            .getAttributeValues(n, modeConf, objectModel);
                    
                    if (values != null) {
                        for (int i = 0; i < values.length; i++) {
                            if (values[i] != null) {
                                String strValue = values[i].toString();
                                
                                uri.append(n);
                                uri.append("=");
                                
                                // encode URI
                                uri.append(NetUtils.encode(strValue, encoding));
                                
                                uri.append("&");
                            }
                        }
                    } else {
                        try {
                            Object value = module.getAttribute(n, modeConf, objectModel);
                            if (value != null) {
                                String strValue = value.toString();
                                
                                uri.append(n);
                                uri.append("=");
                                
                                // encode URI
                                uri.append(NetUtils.encode(strValue, encoding));
                                
                                uri.append("&");
                            }
                        } catch (Exception e) {
                            // attribute could not be resolved
                        }
                    }
                }
            } catch (UnsupportedEncodingException e) {
                throw new ConfigurationException(
                        "ParamsToURIModule, invalid encoding: " + encoding);
            } finally {
                releaseModule(module);
                
                // remove the last "&"
                if (uri.length() > 0) {
                    uri.deleteCharAt(uri.length()-1);
                }
            }
        }

        return uri.toString();
    }

    public Iterator getAttributeNames(Configuration modeConf, Map objectModel)
            throws ConfigurationException {

        return new Vector().iterator();
    }

    public Object[] getAttributeValues(String name, Configuration modeConf,
            Map objectModel) throws ConfigurationException {

        return new Object[0];
    }

}
