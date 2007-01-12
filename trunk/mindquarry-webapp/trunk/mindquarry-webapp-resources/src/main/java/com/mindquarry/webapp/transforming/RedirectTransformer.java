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
package com.mindquarry.webapp.transforming;

import java.io.IOException;
import java.util.Map;

import org.apache.avalon.framework.parameters.Parameters;
import org.apache.cocoon.ProcessingException;
import org.apache.cocoon.environment.Environment;
import org.apache.cocoon.environment.SourceResolver;
import org.apache.cocoon.environment.internal.EnvironmentHelper;
import org.apache.cocoon.transformation.AbstractTransformer;
import org.apache.cocoon.xml.AbstractXMLProducer;
import org.apache.cocoon.xml.DefaultLexicalHandler;
import org.xml.sax.SAXException;

/**
 * <p>
 * Simply redirect the browser to the location given by the <code>src</code>
 * attribute. This transformer will ignore the XML input and won't output any
 * XML to avoid any serializer after it starting to write content in the output
 * stream since this would conflict with the redirect action. To have a
 * syntactically correct sitemap, you still need to put a serializer after
 * this transformer.
 * </p>
 * 
 * Example Usage:
 * <pre>
 * &lt;transform type="redirect" src="foobar" /&gt;
 * </pre>
 * 
 * Declaration:
 * <pre>
 * &lt;transformer name="redirect"
 *     src="com.mindquarry.webapp.transforming.RedirectTransformer" /&gt;
 * </pre>
 * 
 * @author <a href="mailto:alexander(dot)klimetschek(at)mindquarry(dot)com">
 *         Alexander Klimetschek</a>
 * 
 */
public class RedirectTransformer extends AbstractTransformer {

    public void setup(SourceResolver sourceResolver, Map objectModel,
            String src, Parameters params) throws ProcessingException,
            SAXException, IOException {
        
        // disconnect us from the following element in the pipe
        setContentHandler(AbstractXMLProducer.EMPTY_CONTENT_HANDLER);
        setLexicalHandler(DefaultLexicalHandler.NULL_HANDLER);
        
        // do the redirect
        Environment env = EnvironmentHelper.getCurrentEnvironment();
        env.tryResetResponse();
        env.redirect(src, false, false);
    }

}
