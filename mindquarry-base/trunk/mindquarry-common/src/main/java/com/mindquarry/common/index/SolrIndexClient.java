/**
 * Copyright (C) 2006 Mindquarry GmbH, All Rights Reserved
 */
package com.mindquarry.common.index;

import java.io.ByteArrayOutputStream;
import java.util.List;

import org.apache.avalon.framework.activity.Initializable;
import org.apache.avalon.framework.configuration.Configurable;
import org.apache.avalon.framework.configuration.Configuration;
import org.apache.avalon.framework.configuration.ConfigurationException;
import org.apache.avalon.framework.service.ServiceException;
import org.apache.avalon.framework.service.ServiceManager;
import org.apache.avalon.framework.service.Serviceable;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.UsernamePasswordCredentials;
import org.apache.commons.httpclient.auth.AuthScope;
import org.apache.commons.httpclient.methods.ByteArrayRequestEntity;
import org.apache.commons.httpclient.methods.PostMethod;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

/**
 * Add summary documentation here.
 * 
 * @author <a href="mailto:alexander(dot)saar(at)mindquarry(dot)com">Alexander
 *         Saar</a>
 */
public class SolrIndexClient extends AbstractAsyncIndexClient implements
        Configurable, Serviceable, Initializable {
    /**
     * Used to resolve other components.
     */
    protected ServiceManager manager;

    /**
     * Configuration for this component.
     */
    protected Configuration config;

    private String solrLogin;

    private String solrPassword;

    private String solrEndpoint;

    /**
     * @see com.mindquarry.common.index.AbstractAsyncIndexer#indexInternal(java.io.InputStream,
     *      java.lang.String, java.lang.String, java.lang.String)
     */
    @Override
    protected void indexInternal(List<String> changedPaths,
            List<String> deletedPaths) throws Exception {

        Element chngEl = new org.jdom.Element("changes");
        Element delEl = new org.jdom.Element("deleted");
        chngEl.addContent(delEl);
        Element modEl = new org.jdom.Element("modified");
        chngEl.addContent(modEl);

        for (String path : deletedPaths) {
            Element pathEl = new Element("path");
            pathEl.addContent(path);
            delEl.addContent(pathEl);
        }
        for (String path : changedPaths) {
            Element pathEl = new Element("path");
            pathEl.addContent(path);
            modEl.addContent(pathEl);
        }
        Document doc = new Document(chngEl);
        ByteArrayOutputStream os = new ByteArrayOutputStream();

        XMLOutputter op = new XMLOutputter(Format.getPrettyFormat());
        op.output(doc, os);
        // op.output(doc, System.out);

        // send content
        sendToIndexer(os.toByteArray());
    }

    private void sendToIndexer(byte[] content) throws Exception {
        HttpClient httpClient = new HttpClient();
        httpClient.getState().setCredentials(
                new AuthScope(AuthScope.ANY_HOST, AuthScope.ANY_PORT,
                        AuthScope.ANY_REALM),
                new UsernamePasswordCredentials(solrLogin, solrPassword));

        PostMethod pMethod = new PostMethod(solrEndpoint);
        pMethod.setDoAuthentication(true);
        pMethod.setRequestEntity(new ByteArrayRequestEntity(content));
        httpClient.executeMethod(pMethod);

        if (pMethod.getStatusCode() == 200) {
            System.out.println(pMethod.getResponseBodyAsString());
        } else if (pMethod.getStatusCode() == 401) {
            getLogger().warn("Authorization problem. Could not connect to index updater.");
        } else {
            System.out.println("Unknown error");
            System.out.println("STATUS: "+ pMethod.getStatusCode());
            System.out.println("RESPONSE: " + pMethod.getResponseBodyAsString());
        }
        pMethod.releaseConnection();
    }

    /**
     * @see org.apache.avalon.framework.configuration.Configurable#configure(org.apache.avalon.framework.configuration.Configuration)
     */
    public void configure(Configuration config) throws ConfigurationException {
        this.config = config;
    }

    /**
     * @see org.apache.avalon.framework.service.Serviceable#service(org.apache.avalon.framework.service.ServiceManager)
     */
    public void service(ServiceManager manager) throws ServiceException {
        this.manager = manager;
    }

    /**
     * @see org.apache.avalon.framework.activity.Initializable#initialize()
     */
    public void initialize() throws Exception {
        // load configuration
        Configuration cfg = config.getChild("solr-credentials", false); //$NON-NLS-1$
        solrLogin = cfg.getAttribute("login"); //$NON-NLS-1$
        solrPassword = cfg.getAttribute("password"); //$NON-NLS-1$

        cfg = config.getChild("solr-endpoint", false); //$NON-NLS-1$
        solrEndpoint = cfg.getAttribute("url"); //$NON-NLS-1$
    }
}
