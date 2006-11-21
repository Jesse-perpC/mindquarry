/**
 * Copyright (C) 2006 Mindquarry GmbH, All Rights Reserved
 */
package com.mindquarry.search.index;

import java.io.InputStream;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.UsernamePasswordCredentials;
import org.apache.commons.httpclient.auth.AuthScope;
import org.apache.commons.httpclient.methods.PostMethod;

import com.mindquarry.common.index.AbstractAsyncIndexClient;

/**
 * Add summary documentation here.
 * 
 * @author <a href="mailto:alexander(dot)saar(at)mindquarry(dot)com">Alexander
 *         Saar</a>
 */
public class SolrIndexClient extends AbstractAsyncIndexClient {
    private String indexEndpoint;

    private String login;

    private String pwd;

    /**
     * @see com.mindquarry.common.index.AbstractAsyncIndexer#indexInternal(java.io.InputStream,
     *      java.lang.String, java.lang.String, java.lang.String)
     */
    @Override
    public void indexInternal(final InputStream content, final String name,
            final String location, final String type) {
        HttpClient httpClient = new HttpClient();
        httpClient.getState().setCredentials(
                new AuthScope(AuthScope.ANY_HOST, AuthScope.ANY_PORT,
                        AuthScope.ANY_REALM),
                new UsernamePasswordCredentials(login, pwd));

        PostMethod postMethod = new PostMethod("address");
    }

    public void setIndexEndpoint(String indexEndpoint) {
        this.indexEndpoint = indexEndpoint;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }
}
