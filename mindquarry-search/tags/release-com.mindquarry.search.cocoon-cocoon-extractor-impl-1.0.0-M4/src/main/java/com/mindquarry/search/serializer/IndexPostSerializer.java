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
package com.mindquarry.search.serializer;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMResult;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.sax.TransformerHandler;
import javax.xml.transform.stream.StreamResult;

import org.apache.avalon.framework.configuration.Configuration;
import org.apache.avalon.framework.configuration.ConfigurationException;
import org.apache.avalon.framework.service.ServiceException;
import org.apache.avalon.framework.service.ServiceManager;
import org.apache.cocoon.CascadingIOException;
import org.apache.cocoon.serialization.AbstractTextSerializer;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.UsernamePasswordCredentials;
import org.apache.commons.httpclient.auth.AuthScope;
import org.apache.commons.httpclient.methods.ByteArrayRequestEntity;
import org.apache.commons.httpclient.methods.PostMethod;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.mindquarry.common.index.SolrIndexClient;

/**
 * Add summary documentation here.
 * 
 * @author <a href="mailto:alexander(dot)saar(at)mindquarry(dot)com">Alexander
 *         Saar</a>
 */
public class IndexPostSerializer extends AbstractTextSerializer {
    DOMResult res = new DOMResult();

    private ServiceManager servicemanager;

    private String login;

    private String password;

    /**
     * Set the configurations for this serializer.
     */
    public void configure(Configuration conf) throws ConfigurationException {
        super.configure(conf);
        this.format.put(OutputKeys.METHOD, "xml");
    }

    /**
     * Set the {@link OutputStream} where the requested resource should be
     * serialized.
     */
    public void setOutputStream(OutputStream out) throws IOException {
        super.setOutputStream(out);
        try {
            TransformerHandler handler = this.getTransformerHandler();
            handler.getTransformer().setOutputProperties(this.format);
            res = new DOMResult();
            handler.setResult(res);
            // handler.setResult(new StreamResult(this.output));
            this.setContentHandler(handler);
            this.setLexicalHandler(handler);
        } catch (Exception e) {
            final String message = "Cannot set XMLSerializer outputstream";
            throw new CascadingIOException(message, e);
        }
    }

    @Override
    public void endDocument() throws SAXException {
        super.endDocument();

        Node node = this.res.getNode();
        Element root = (Element) ((Document) node).getFirstChild();
        String action = root.getAttribute("action"); //$NON-NLS-1$

        NodeList children = root.getChildNodes();

        if (true) { // should resolve the action and find out wether it is a
            // cycle through all children of the root element
            for (int i = 0; i < children.getLength(); i++) {
                Node child = children.item(i);
                // for every child element...
                if (child instanceof Element) {

                    // block source which can be posted by creating a new
                    // servlet request
                    URL url = null;
                    try {
                        url = new URL(action);
                    } catch (MalformedURLException e1) {
                        e1.printStackTrace();
                    }
                    HttpClient client = new HttpClient();
                    client.getState().setCredentials(
                            new AuthScope(url.getHost(), url.getPort(),
                                    AuthScope.ANY_REALM),
                            new UsernamePasswordCredentials(login, password));

                    PostMethod pMethod = new PostMethod(action);
                    pMethod.setDoAuthentication(true);
                    pMethod.addRequestHeader("accept", "text/xml"); //$NON-NLS-1$ //$NON-NLS-2$

                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    TransformerFactory tf = TransformerFactory.newInstance();
                    try {
                        tf.newTransformer().transform(new DOMSource(child),
                                new StreamResult(baos));
                        pMethod.setRequestEntity(new ByteArrayRequestEntity(
                                baos.toByteArray()));
                        client.executeMethod(pMethod);
                    } catch (TransformerConfigurationException e) {
                        getLogger()
                                .error(
                                        "Failed to configure transformer prior to posting",
                                        e);
                    } catch (TransformerException e) {
                        getLogger().error(
                                "Failed to transform prior to posting", e);
                    } catch (HttpException e) {
                        getLogger().error("Failed to post", e);
                    } catch (IOException e) {
                        getLogger().error("Something went wrong", e);
                    }
                }
            }
        }
    }

    @Override
    public void service(ServiceManager manager) throws ServiceException {
        super.service(manager);
        this.servicemanager = manager;

        SolrIndexClient ic = (SolrIndexClient) this.servicemanager
                .lookup("com.mindquarry.common.index.IndexClient");
        login = ic.getSolrLogin();
        password = ic.getSolrPassword();
    }

    @Override
    public String getMimeType() {
        return "text/xml";
    }
}
