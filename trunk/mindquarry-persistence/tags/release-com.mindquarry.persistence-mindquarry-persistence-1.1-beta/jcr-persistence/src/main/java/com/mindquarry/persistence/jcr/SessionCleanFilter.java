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
package com.mindquarry.persistence.jcr;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.web.context.support.WebApplicationContextUtils;

/**
 * Add summary documentation here.
 *
 * @author
 * <a href="mailto:bastian.steinert(at)mindquarry.com">Bastian Steinert</a>
 */
public class SessionCleanFilter implements Filter {

    /** Root Cocoon Bean Factory. */
    private BeanFactory beanFactory_;

    /**
     * @see javax.servlet.Filter#init(javax.servlet.FilterConfig)
     */
    public void init(FilterConfig config) throws ServletException {
        ServletContext servletContext = config.getServletContext();
        beanFactory_ = WebApplicationContextUtils
            .getRequiredWebApplicationContext(servletContext);
    
        if (!beanFactory_.containsBean(Persistence.ROLE)) {
            throw new ServletException("there is no spring bean with name: "
                + Persistence.ROLE + " available.");
        }
    }

    /**
     * @see javax.servlet.Filter#doFilter(javax.servlet.ServletRequest,
     *      javax.servlet.ServletResponse, javax.servlet.FilterChain)
     */
    public void doFilter(ServletRequest servletRequest,
        ServletResponse servletResponse, FilterChain chain)
        throws IOException, ServletException {
        
        try {
            chain.doFilter(servletRequest, servletResponse);
        }
        finally {
            lookupPersistence().clear();
        }
    }
    
    private Persistence lookupPersistence() {
        return (Persistence) beanFactory_.getBean(Persistence.ROLE);
    }

    public void destroy() {
        // TODO Auto-generated method stub
    }    
}
