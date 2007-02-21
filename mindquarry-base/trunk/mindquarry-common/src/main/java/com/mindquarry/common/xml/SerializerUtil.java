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
package com.mindquarry.common.xml;

import java.io.IOException;
import java.io.OutputStream;

import org.apache.avalon.framework.configuration.Configuration;
import org.apache.avalon.framework.configuration.ConfigurationException;
import org.apache.avalon.framework.configuration.DefaultConfiguration;
import org.apache.avalon.framework.logger.NullLogger;
import org.apache.cocoon.serialization.Serializer;
import org.apache.cocoon.serialization.XMLSerializer;

import com.mindquarry.common.init.InitializationException;

/**
 *
 * @author 
 * <a href="mailto:bastian(dot)steinert(at)mindquarry(dot)com">Bastian Steinert</a>
 */
public class SerializerUtil {

	private static final DefaultConfiguration serializerConfiguration;
	
	static {
		serializerConfiguration = new DefaultConfiguration("serializer");
		serializerConfiguration.setAttribute(
				"class", "org.apache.cocoon.serialization.XMLSerializer");
		serializerConfiguration.addChild(transformerConfiguration());
	}
	
	private static Configuration transformerConfiguration() {
		DefaultConfiguration result = 
			new DefaultConfiguration("transformer-factory");
		result.setValue("org.apache.xalan.xsltc.trax.TransformerFactoryImpl");
		return result;
	}
    
    public static Serializer makeSerializer(OutputStream outputStream) 
    		throws IOException {
    	
        // we use Cocoons XMLSerializer here which is configurable in terms
        // of the Transformer Implementation to use (some are buggy, so we
        // want to ensure which one is used)
        
        XMLSerializer serializer = new XMLSerializer();
        serializer.enableLogging(new NullLogger());
        try {
            serializer.configure(serializerConfiguration);
            serializer.setOutputStream(outputStream);
        } catch (ConfigurationException e) {
            throw new InitializationException(
                    "could not confiure XMLSerializer", e);
        }
        
        return serializer;
    }
}
