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

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.StringTokenizer;

import org.apache.avalon.framework.configuration.Configurable;
import org.apache.avalon.framework.configuration.Configuration;
import org.apache.avalon.framework.configuration.ConfigurationException;
import org.apache.avalon.framework.parameters.Parameters;
import org.apache.cocoon.ProcessingException;
import org.apache.cocoon.environment.SourceResolver;
import org.apache.cocoon.transformation.AbstractSAXTransformer;
import org.apache.cocoon.xml.IncludeXMLConsumer;
import org.apache.cocoon.xml.dom.DOMBuilder;
import org.apache.excalibur.source.Source;
import org.apache.xerces.parsers.AbstractSAXParser;
import org.cyberneko.html.HTMLConfiguration;
import org.w3c.dom.Document;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 * Converts (escaped) HTML snippets into tidied HTML using the NekoHTML library.
 * This transformer expects a list of elements, passed as comma separated values
 * of the "tags" parameter. It records the text enclosed in such elements and
 * pass it thru Neko to obtain valid XHTML.
 * 
 * Automatically sets the encoding of the local system as default-encoding
 * parameter for nekohtml to work properly
 * 
 * @author <a href="mailto:alexander(dot)klimetschek(at)mindquarry(dot)com">
 *         Alexander Klimetschek</a>
 */
public class NekoHTMLTransformer
    extends AbstractSAXTransformer
    implements Configurable {

    /**
     * Properties for Neko format
     */
    protected Properties properties;

    /**
     * Tags that must be normalized
     */
    private Map<String, String> tags;

    /**
     * React on endElement calls that contain a tag to be
     * tidied and run Neko on it, otherwise passthru.
     *
     * @see org.xml.sax.ContentHandler#endElement(java.lang.String, java.lang.String, java.lang.String)
     */
    public void endElement(String uri, String name, String raw)
        throws SAXException {
        if (this.tags.containsKey(name)) {
            String toBeNormalized = this.endTextRecording();
            try {
                this.normalize(toBeNormalized);
            } catch (ProcessingException e) {
                e.printStackTrace();
            }
        }
        super.endElement(uri, name, raw);
    }

    /**
     * Start buffering text if inside a tag to be normalized,
     * passthru otherwise.
     *
     * @see org.xml.sax.ContentHandler#startElement(java.lang.String, java.lang.String, java.lang.String, org.xml.sax.Attributes)
     */
    public void startElement(
        String uri,
        String name,
        String raw,
        Attributes attr)
        throws SAXException {
        super.startElement(uri, name, raw, attr);
        if (this.tags.containsKey(name)) {
            this.startTextRecording();
        }
    }

    /**
     * Configure this transformer, possibly passing to it
     * a jtidy configuration file location.
     */
    public void configure(Configuration config) throws ConfigurationException {
        super.configure(config);

        String configUrl = config.getChild("neko-config").getValue(null);
        if (configUrl != null) {
            org.apache.excalibur.source.SourceResolver resolver = null;
            Source configSource = null;
            try {
                resolver = (org.apache.excalibur.source.SourceResolver)
                           this.manager.lookup(org.apache.excalibur.source.SourceResolver.ROLE);
                configSource = resolver.resolveURI(configUrl);
                if (getLogger().isDebugEnabled()) {
                    getLogger().debug(
                        "Loading configuration from " + configSource.getURI());
                }
                this.properties = new Properties();
                this.properties.load(configSource.getInputStream());

            } catch (Exception e) {
                getLogger().warn("Cannot load configuration from " + configUrl);
                throw new ConfigurationException(
                    "Cannot load configuration from " + configUrl,
                    e);
            } finally {
                if (null != resolver) {
                    this.manager.release(resolver);
                    resolver.release(configSource);
                }
            }
        }
    }

    /**
     * The beef: run Neko on the buffered text and stream
     * the result
     *
     * @param text the string to be tidied
     */
    protected void normalize(String text) throws ProcessingException {
        try {
            HtmlSaxParser parser = new HtmlSaxParser(this.properties);

            ByteArrayInputStream bais =
                new ByteArrayInputStream(text.getBytes());

            DOMBuilder builder = new DOMBuilder();
            parser.setContentHandler(builder);
            parser.parse(new InputSource(bais));
            Document doc = builder.getDocument();

            IncludeXMLConsumer.includeNode(doc, this.contentHandler, this.lexicalHandler);
        } catch (Exception e) {
            throw new ProcessingException(
                "Exception in NekoHTMLTransformer.normalize()",
                e);
        }
    }

    /**
     * Setup this component, passing the tag names to be tidied.
     */

    public void setup(
        SourceResolver resolver,
        Map objectModel,
        String src,
        Parameters par)
        throws ProcessingException, SAXException, IOException {
        super.setup(resolver, objectModel, src, par);
        String tagsParam = par.getParameter("tags", "");        
        if (getLogger().isDebugEnabled()) {
            getLogger().debug("tags: " + tagsParam);
        }        
        this.tags = new HashMap<String, String>();
        StringTokenizer tokenizer = new StringTokenizer(tagsParam, ",");
        while (tokenizer.hasMoreElements()) {
            String tok = tokenizer.nextToken().trim();
            this.tags.put(tok, tok);
        }
    }

    public static class HtmlSaxParser extends AbstractSAXParser {

        public HtmlSaxParser(Properties properties) {
            super(getConfig(properties));
        }

        private static HTMLConfiguration getConfig(Properties properties) {
            HTMLConfiguration config = new HTMLConfiguration();
            config.setProperty("http://cyberneko.org/html/properties/names/elems", "lower");
            //System.out.println("Default Locale: " + Charset.defaultCharset().name());
            config.setProperty("http://cyberneko.org/html/properties/default-encoding", Charset.defaultCharset().name());
            if (properties != null) {
                for (Iterator i = properties.keySet().iterator();i.hasNext();) {
                    String name = (String) i.next();
                    if (name.indexOf("/features/") > -1) {
                        config.setFeature(name, Boolean.getBoolean(properties.getProperty(name)));
                    } else if (name.indexOf("/properties/") > -1) {
                        config.setProperty(name, properties.getProperty(name));
                    }
                }
            }
            return config;
        }
    }
}
