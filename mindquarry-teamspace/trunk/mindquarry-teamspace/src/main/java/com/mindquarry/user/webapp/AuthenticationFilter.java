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
package com.mindquarry.user.webapp;

import static com.mindquarry.user.manager.DefaultUsers.isAnonymousUser;
import static com.mindquarry.user.manager.DefaultUsers.isAnonymousDisabled;
import static com.mindquarry.user.manager.DefaultUsers.isIndexUser;

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
import javax.swing.text.html.HTMLDocument.HTMLReader.IsindexAction;

import org.apache.avalon.framework.logger.Logger;
import org.apache.cocoon.core.container.spring.avalon.AvalonUtils;
import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.mindquarry.user.Authentication;

/**
 * Servlet filter that handles HTTP authentication.
 * 
 * <p>
 * The basic principle ist a RESTful authentication, ie. fully stateless. The
 * second feature is to avoid the ugly standard HTTP login form in most popular
 * GUI browsers by having a special login page that uses Javascript and
 * XMLHttpRequests to do the login in the background. Because Javascript may not
 * be available (because users are paranoid, use something like lynx or it is
 * actually a REST client application), the process of using the standard way is
 * also supported.
 * </p>
 * 
 * <p>
 * This filter needs appropriate client-side Javascript and login pages to work
 * properly. The most important requirement is to provide a login page under
 * LOGIN_PAGE that includes either a link to
 * LOGIN_REQUEST_URI?LOGIN_REQUEST_PARAM=LOGIN_REQUEST_VALUE&TARGET_URI_PARAM=foobar
 * (where foobar is passed to the login page as parameter with the same name,
 * TARGET_URI_PARAM) or some Javascript code that uses XMLHttpRequest to first
 * call LOGIN_REQUEST_URI?LOGIN_REQUEST_PARAM=LOGIN_REQUEST_VALUE and then
 * (on success) redirecting the browser to the value of TARGET_URI_PARAM (eg.
 * foobar).
 * </p>
 * 
 * @author <a href="mailto:bastian(dot)steinert(at)mindquarry(dot)com">
 *         Bastian Steinert</a>
 * @author <a href="mailto:alexander(dot)klimetschek(at)mindquarry(dot)com">
 *         Alexander Klimetschek</a>
 */
public class AuthenticationFilter implements Filter {

    /**
     * The name of the request attribute that will hold the name of the
     * authenticated user when the authentication was successful. Can then be
     * used in Servlets and Cocoon Sitemaps via "{request-attr:username}".
     */
    public static final String USERNAME_ATTR = "username";

    /**
     * URI against which the actual first (hopefully successful) authentication
     * is done, with ?LOGIN_REQUEST_PARAM=LOGIN_REQUEST_VALUE. Must be used in
     * the login page.
     */
    public static final String LOGIN_REQUEST_URI = "";

    /**
     * Name of the request parameter that is used to indicate an actual first
     * login request. The only value used is LOGIN_REQUEST_VALUE.
     */
    public static final String LOGIN_REQUEST_PARAM = "request";

    /**
     * Standard value for LOGIN_REQUEST_PARAM that indicates a login request.
     */
    public static final String LOGIN_REQUEST_VALUE = "login";

    /**
     * URI of the login page to which any unauthorized GUI HTML browsers are
     * sent to. Must display a link to the LOGIN_REQUEST_URI (including the
     * param) or a form that uses Javascript XMLHttpRequest to call that URI.
     * This is an unprotected page.
     */
    public static final String LOGIN_PAGE = "loginpage";

    /**
     * URI of the logout page that should simply display that the logout was
     * successful (and my be a link to re-login). This is an unprotected page.
     */
    public static final String LOGOUT_PAGE = "logoutpage";

    /**
     * Name of the GET request parameter which holds the actual URI when
     * the login page is shown to forward to that URI upon successful login. 
     */
    public static final String TARGET_URI_PARAM = "targetUri";

    /**
     * This is a) the challenge for the authentication and b) the string that
     * is displayed in the standard HTTP login dialog. Configurable. 
     */
    private String realm_;
    
    /** The logger. */
    private Logger log_;

    /** Root Cocoon Bean Factory. */
    private BeanFactory beanFactory_;
    
    /**
     * @see javax.servlet.Filter#init(javax.servlet.FilterConfig)
     */
    public void init(FilterConfig config) throws ServletException {
        ServletContext servletContext = config.getServletContext();
        beanFactory_ = WebApplicationContextUtils
                .getRequiredWebApplicationContext(servletContext);
        
        log_ = (Logger) beanFactory_.getBean(AvalonUtils.LOGGER_ROLE);        
        realm_ = config.getInitParameter("realm");        

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
     * Filter method that implements the actual logic and decides whether to
     * process by calling the next filter in the chain (another filter or the
     * servlet) or stopping here and sending a special response (authentication
     * request or redirect).
     *  
     * @see javax.servlet.Filter#doFilter(javax.servlet.ServletRequest, javax.servlet.ServletResponse, javax.servlet.FilterChain)
     */
    public void doFilter(ServletRequest servletRequest, 
            ServletResponse servletResponse, FilterChain chain) 
            throws IOException, ServletException {

        // cast to http request and response due to read and set http headers
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        
        // always send an authentication request in order to avoid one round trip 
        response.setHeader("WWW-Authenticate", "BASIC realm=\""+ realm_ +"\"");
        
        // only do authentication for protected URIs, eg. excluded login page
        if (isProtected(request)) {
            String authenticatedUser = authenticateUser(request);
            
            // the special login request is done to actually perform the
            // authentication, this is typically the second step initiated by
            // the login page
            if (isLoginRequest(request)) {
                if (authenticatedUser == null) {
                    // not authenticated. trigger auth. with username / password
                    // either by the HTTP auth dialog in the browser or
                    // automatically by Javascript XMLHttpRequest
                    response.sendError(HttpServletResponse.SC_UNAUTHORIZED);                    
                } else {
                    // authenticated. redirect to original target
                    String originalUrl = request.getParameter(TARGET_URI_PARAM);
                    String redirectUrl = response.encodeRedirectURL(
                            (originalUrl != null ? originalUrl : "."));
                    response.sendRedirect(redirectUrl);
                }
                
                // no further servlet processing.
                return;
                
            }
            else {
                // 99 percent of all pages, not the special login request.
                
                // here we either have the first request to the server, ie.
                // not yet authenticated, or some client that does not send the
                // authorization data preemptively (although he already did
                // authenticate) -> see isGuiBrowserRequest()
                if (authenticatedUser == null) {
                    // not authenticated.
                    
                    // standard browser with preemptive sending auth data, thus
                    // it must be the first request -> go to login page
                    if (isGuiBrowserRequest(request)) {
                        String loginUrl = buildLoginUrlForRequest(request);                        
                        String redirectUrl = response.encodeRedirectURL(loginUrl);
                        response.sendRedirect(redirectUrl);
                    } else {
                        // trigger simple client auth. or re-authentication
                        response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
                    }

                    // no further servlet processing.
                    return;
                    
                } else {
                    // authenticated. make username available as request attribute
                    request.setAttribute(USERNAME_ATTR, authenticatedUser);
                }
            }
        }
        
        // access granted, proceed with the servlet
        chain.doFilter(servletRequest, servletResponse);
    }
    
    /**
     * builds an url to the loginpage - 
     * the requestUri of the current request is appended 
     * as 'targetUri' parameter. hence we can send an redirect
     * to the orginial request uri after successful login 
     */
    private String buildLoginUrlForRequest(HttpServletRequest request) {
        StringBuilder loginUrlSB = new StringBuilder();
        loginUrlSB.append(request.getContextPath());
        loginUrlSB.append('/');
        loginUrlSB.append(LOGIN_PAGE);
        loginUrlSB.append('?');
        loginUrlSB.append(TARGET_URI_PARAM);
        loginUrlSB.append('=');
        loginUrlSB.append(requestAsRedirectUri(request));
        return loginUrlSB.toString();
    }
    
    /**
     * Tests if the request is a special login request, used by the
     * XMLHttpRequest of the Java-based login form.
     */
    private boolean isLoginRequest(HttpServletRequest request) {
        String targetUri = servletRelativeUri(request);
        return targetUri.equals(LOGIN_REQUEST_URI) && 
            LOGIN_REQUEST_VALUE.equals(request.getParameter(LOGIN_REQUEST_PARAM));
    }
    
    /**
     * Tests if the request comes from a popular GUI browser.
     * 
     * <p>
     * This includes all the standard ones (Internet Explorer, Firefox, Safari,
     * Opera, and all Mozilla-based) but excludes the non-popular and more
     * strictly-following-the-RFC ones (like lynx or link). Those strict ones do
     * not sent the Authorization header preemptively to all pages on the same
     * server once there was an authorization. Thus it is not possible to decide
     * whether that request is the first one (not yet logged in) and the login
     * page should be displayed or if it is from that strict browser, that
     * actually holds the credentials but does not send them yet without
     * explicitly being asked for by sending yet another authorization
     * challenge.
     * </p>
     * 
     * <p>
     * The check uses the Accept and the User-Agent header and is for most
     * cases somewhat verbose, since the check for the User-Agent is sufficient.
     * </p>
     */
    private boolean isGuiBrowserRequest(HttpServletRequest request) {
        boolean result = true;
        String accept = request.getHeader("Accept");
        String userAgent = request.getHeader("User-Agent");
        
        if (accept != null) {
            // first check for human-based browser content requests
            result = accept.contains("text/html") 
                || accept.contains("application/xhtml+xml")
                || accept.contains("*/*"); // e.g. for IE 6
            
            if (result && userAgent != null) {
                // second check for popular browsers
                result = userAgent.contains("MSIE")
                    || userAgent.contains("Gecko")
                    || userAgent.contains("Opera")
                    || userAgent.contains("Safari");
            }
        }
        return result;
    }
    
    private static final String ANY_CHAR = "(.)*";

    private static final String EOL = "$";
    
    private static final String NO_SLASH = "[^/]+";
    
    private static final String DOT = "\\.";
    
    /**
     * Checks if the requested URI is actually protected. Non-protected URIs are
     * the login and logout pages as well as stylesheets, scripts and images for
     * those pages.
     */
    private boolean isProtected(HttpServletRequest request) {
        String targetUri = servletRelativeUri(request);
        // (1) The login page is allowed.
        
        // (2) The logout page is allowed.

        // (3) All css stylesheets and scripts are allowed.
        
        // (4) Images with the pattern "/header.png" are allowed, since they
        //     belong to the login/logout page.
        
        // (5) Images under the folders "images", "icons" and "buttons" are
        //     considered non-protected image resources.
        
        // Other images might contain protected content (photos or diagrams)
        
        return !(targetUri.matches(
                LOGIN_PAGE + ANY_CHAR + EOL + "|" +
                LOGOUT_PAGE + ANY_CHAR + EOL + "|" +
                ANY_CHAR + DOT + "(css|js|ico)" + EOL + "|" +
                NO_SLASH + DOT + "(png|jpg|gif|bmp|jpeg)" + EOL + "|" +
                ANY_CHAR + "(images|icons|buttons)/" + ANY_CHAR + DOT + "(png|jpg|gif|bmp|jpeg)" + EOL));
    }
    
    /**
     * Returns the decoded authorization data from the HTTP header.
     */
    private String[] authTupleFromAuthHeader(String authHeader) {
        
        String encodedAuthRequest = authHeader.substring(6);
        byte[] authRequest = Base64.decodeBase64(
                encodedAuthRequest.getBytes());
        return new String(authRequest).split(":");
    }
    
    /**
     * This does the actual authentication using the teamspace authentication
     * component.
     */
    private String authenticateUser(HttpServletRequest request) {
        
        String authHeader = request.getHeader("Authorization");
        
        String authenticatedUser = null;
        if ((authHeader != null) && 
                authHeader.toUpperCase().startsWith("BASIC")) {
            
            String[] authTuple = authTupleFromAuthHeader(authHeader);
            if (authTuple.length == 2) {
                String username = authTuple[0];
                String password = authTuple[1];
                
                if (authenticate(username, password) 
                        && isUserAllowed(username)) {
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
    
    private boolean isUserAllowed(String username) {
        if (isAnonymousUser(username) && isAnonymousDisabled()) {
            return false;
        }
        else if (isIndexUser(username)) {
            return false;
        }
        else { 
            return true;
        }
    }

    private boolean authenticate(String username, String password) {
        String lookupName = Authentication.class.getName();
        Authentication authentication = 
            (Authentication) beanFactory_.getBean(lookupName);
        return authentication.authenticate(username, password);
    }

    private String servletRelativeUri(HttpServletRequest request) {

        String servletRequestUri = request.getPathInfo();
        
        if (servletRequestUri.startsWith("/"))
            return servletRequestUri.substring(1);
        else
            return servletRequestUri;
    }

    private String requestAsRedirectUri(HttpServletRequest request) {
        // get the part after http://host:port
        // without query string / parameter
        
        // TODO: make this relative, use one of the servlet methods, AFAIK:
        // /mindquarry-webapplication/servlet/dosomething?param=value
        // getContextPath() + getServletPath() + getPathInfo() + getQueryString()
        // (path info is empty)
        
        return request.getRequestURI();
    }
}
