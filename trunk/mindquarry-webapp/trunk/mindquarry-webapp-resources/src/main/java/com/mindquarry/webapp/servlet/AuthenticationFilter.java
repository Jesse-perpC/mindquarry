/**
 * Copyright (C) 2006 Mindquarry GmbH, All Rights Reserved
 */
package com.mindquarry.webapp.servlet;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.avalon.framework.logger.Logger;
import org.apache.cocoon.ProcessingUtil;
import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.mindquarry.teamspace.Authentication;

/**
 * Add summary documentation here.
 *
 * @author 
 * <a href="mailto:bastian.steinert(at)mindquarry.com">your full name</a>
 */
public class AuthenticationFilter implements Filter {

    /** The logger. */
    private Logger log_;

    /** Root Cocoon Bean Factory. */
    private BeanFactory beanFactory_;
    
    private String realm_;
    
    /**
     * @see javax.servlet.Filter#init(javax.servlet.FilterConfig)
     */
    public void init(FilterConfig config) throws ServletException {
        ServletContext servletContext = config.getServletContext();
        beanFactory_ = WebApplicationContextUtils
                .getRequiredWebApplicationContext(servletContext);
        
        log_ = (Logger) beanFactory_.getBean(ProcessingUtil.LOGGER_ROLE);
        realm_ = "Protected Resource";
        

        String authenticationBeanName = Authentication.class.getName();
        if (! beanFactory_.containsBean(authenticationBeanName)) {
            throw new ServletException("there is no spring bean with name: " + 
                    authenticationBeanName + " available.");
        }
    }
    
    /**
     * @see javax.servlet.Filter#destroy()
     */
    public void destroy() {
        // nothing to do
    }

    /**
     * @see javax.servlet.Filter#doFilter(javax.servlet.ServletRequest, javax.servlet.ServletResponse, javax.servlet.FilterChain)
     */
    public void doFilter(ServletRequest servletRequest, 
            ServletResponse servletResponse, FilterChain chain) 
            throws IOException, ServletException {

        // cast to http request and response due to read and set http headers
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        
        
        // always send an authentication request 
        // in order to avoid one round trip 
        response.setHeader("WWW-Authenticate", "BASIC realm=\""+ realm_ +"\"");
        
        String targetUri = getTargetURI(request);
        
        if (isProtected(targetUri)) {            
            String authenticatedUser = authenticateUser(request);
            if (authenticatedUser == null) {
                // not authenticated.
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
                //response.sendRedirect("/login?targetUri=" + targetUri);
                // break this request, 
                // leave filter chain and don't go to the servlet
                return;
            } else {
                // authenticated. make username available as request attribute
                request.setAttribute("username", authenticatedUser);
            }
        }
        chain.doFilter(servletRequest, servletResponse);
    }
    
    private boolean isProtected(String targetUri) {
        if (targetUri.equals("/login"))
            return false;
        if (targetUri.endsWith(".css"))
            return false;
        if (targetUri.endsWith(".png"))
            return false;
        if (targetUri.endsWith(".jpg"))
            return false;
        if (targetUri.endsWith(".gif"))
            return false;
        if (targetUri.endsWith(".js"))
            return false;
        
        return true;
    }
    
    private String[] authTupleFromAuthHeader(String authHeader) {
        
        String encodedAuthRequest = authHeader.substring(6);
        byte[] authRequest = Base64.decodeBase64(
                encodedAuthRequest.getBytes());
        return new String(authRequest).split(":");
    }
    
    private String authenticateUser(HttpServletRequest request) {
        
        String authHeader = request.getHeader("Authorization");
        
        String authenticatedUser = null;
        if ((authHeader != null) && 
                authHeader.toUpperCase().startsWith("BASIC")) {
            
            String[] authTuple = authTupleFromAuthHeader(authHeader);
            if (authTuple.length == 2) {
                String username = authTuple[0];
                String password = authTuple[1];
                
                if (lookupAuthentication().authenticate(username, password)) {
                    authenticatedUser = username;
                } else {
                    log_.info("failed authentication" +
                            " from host: " + request.getRemoteAddr() +
                            " with username: " + username);
                }
            }
        }
        return authenticatedUser;
    }

    private Authentication lookupAuthentication() {
        String lookupName = Authentication.class.getName();
        return (Authentication) beanFactory_.getBean(lookupName);
    }

    protected String getTargetURI(HttpServletRequest request) {

        // get the part after http://host:port
        // without query string / parameter
        String currentURI = request.getRequestURI();

        // search index of a second slash
        // if present the context is not empty or "/"
        int lastSlash = currentURI.lastIndexOf("/", 1); // search index of "/",

        // target URI is the part after context root name and following
        // slash, e.g. everything after /dancewear/ or /
        String targetURI;
        if (lastSlash != -1) {
            // get everything after the context root
            targetURI = currentURI
                    .substring(lastSlash, currentURI.length());
        } else {
            // context root is empty ("/")
            targetURI = currentURI; 
        }
        return targetURI;
    }
}
