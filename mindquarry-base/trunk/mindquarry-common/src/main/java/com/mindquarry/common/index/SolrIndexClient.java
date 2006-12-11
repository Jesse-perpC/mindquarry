/**
 * Copyright (C) 2006 Mindquarry GmbH, All Rights Reserved
 */
package com.mindquarry.common.index;

import java.io.ByteArrayOutputStream;
import java.net.URI;
import java.util.List;

import org.apache.avalon.framework.activity.Initializable;
import org.apache.avalon.framework.configuration.Configurable;
import org.apache.avalon.framework.configuration.Configuration;
import org.apache.avalon.framework.configuration.ConfigurationException;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.UsernamePasswordCredentials;
import org.apache.commons.httpclient.auth.AuthScope;
import org.apache.commons.httpclient.methods.ByteArrayRequestEntity;
import org.apache.commons.httpclient.methods.PostMethod;
import org.jdom.Comment;
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
        Configurable, Initializable {
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

        Element delEl = new Element("deleted"); //$NON-NLS-1$
        Element modEl = new Element("modified"); //$NON-NLS-1$
        
        Element chngEl = new Element("changes"); //$NON-NLS-1$
        chngEl.addContent(delEl);
        chngEl.addContent(modEl);

        Comment comment = new Comment("list of deleted paths"); //$NON-NLS-1$
        delEl.addContent(comment);
        for (String path : deletedPaths) {
            Element pathEl = new Element("path"); //$NON-NLS-1$
            pathEl.addContent(path);
            delEl.addContent(pathEl);
        }
        comment = new Comment("list of modified paths"); //$NON-NLS-1$
        modEl.addContent(comment);
        for (String path : changedPaths) {
            Element pathEl = new Element("path"); //$NON-NLS-1$
            pathEl.addContent(path);
            modEl.addContent(pathEl);
        }
        Document doc = new Document(chngEl);
        ByteArrayOutputStream os = new ByteArrayOutputStream();

        XMLOutputter op = new XMLOutputter(Format.getPrettyFormat());
        op.output(doc, os);

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
        pMethod.addRequestHeader("accept", "text/xml"); //$NON-NLS-1$ //$NON-NLS-2$
        pMethod.setRequestEntity(new ByteArrayRequestEntity(content));

        httpClient.executeMethod(pMethod);

        if (pMethod.getStatusCode() == 200) {
            System.out.println(pMethod.getResponseBodyAsString());
        } else if (pMethod.getStatusCode() == 401) {
            getLogger().warn(
                            "Authorization problem. Could not connect to index updater.");
        } else if (pMethod.getStatusCode() == 302) {
            URI redirectLocation = new URI(pMethod
                    .getResponseHeader("location").getValue()); //$NON-NLS-1$
            System.out.println(redirectLocation.toString());
            
        } else {
            System.out.println("Unknown error");
            System.out.println("STATUS: " + pMethod.getStatusCode());
            System.out
                    .println("RESPONSE: " + pMethod.getResponseBodyAsString());
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

    public String getSolrEndpoint() {
        return solrEndpoint;
    }

    public void setSolrEndpoint(String solrEndpoint) {
        this.solrEndpoint = solrEndpoint;
    }

    public String getSolrLogin() {
        return solrLogin;
    }

    public void setSolrLogin(String solrLogin) {
        this.solrLogin = solrLogin;
    }

    public String getSolrPassword() {
        return solrPassword;
    }

    public void setSolrPassword(String solrPassword) {
        this.solrPassword = solrPassword;
    }
}
