/**
 * Copyright (C) 2006 Mindquarry GmbH, All Rights Reserved
 */
package com.mindquarry.common.index;

import java.io.ByteArrayOutputStream;
import java.util.List;

import org.apache.commons.httpclient.Credentials;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;
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
public class SolrIndexClient extends AbstractAsyncIndexClient {
    private String solrLogin;

    private String solrPassword;

    private String solrEndpoint;

    private HttpClient httpClient;

    /**
     * Used to initialize the HTTP client.
     */
    public void initialize() throws Exception {
        MultiThreadedHttpConnectionManager connectionManager = new MultiThreadedHttpConnectionManager();
        httpClient = new HttpClient(connectionManager);

        // enable Preemptive authentication to save one round trip
        httpClient.getParams().setAuthenticationPreemptive(true);

        Credentials solrCreds = new UsernamePasswordCredentials(solrLogin,
                solrPassword);
        AuthScope anyAuthScope = new AuthScope(AuthScope.ANY_HOST,
                AuthScope.ANY_PORT, AuthScope.ANY_REALM);

        httpClient.getState().setCredentials(anyAuthScope, solrCreds);

        // register events by calling base class initialization
        super.initialize();
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
        PostMethod pMethod = new PostMethod(solrEndpoint);
        pMethod.setDoAuthentication(true);
        pMethod.addRequestHeader("accept", "text/xml"); //$NON-NLS-1$ //$NON-NLS-2$
        pMethod.setRequestEntity(new ByteArrayRequestEntity(content));

        try {
            httpClient.executeMethod(pMethod);
        } finally {
            pMethod.releaseConnection();
        }

        if (pMethod.getStatusCode() == 200) {
            // everything worked fine, nothing more to do
        } else if (pMethod.getStatusCode() == 401) {
            getLogger()
                    .warn(
                            "Authorization problem. Could not connect to index updater.");
        } else {
            System.out.println("Unknown error");
            System.out.println("STATUS: " + pMethod.getStatusCode());
            System.out
                    .println("RESPONSE: " + pMethod.getResponseBodyAsString());
        }
        pMethod.releaseConnection();
    }
}
