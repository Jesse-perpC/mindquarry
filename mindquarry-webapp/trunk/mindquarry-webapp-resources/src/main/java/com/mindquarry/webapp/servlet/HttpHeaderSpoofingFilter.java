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
package com.mindquarry.webapp.servlet;

import java.io.IOException;
import java.util.Enumeration;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;

public class HttpHeaderSpoofingFilter implements Filter {

	/**
	 * @see javax.servlet.Filter#destroy()
	 */
	public void destroy() {
		;
	}

	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse,
			FilterChain chain) throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        
        HttpHeaderSpoofingRequestWrapper wrapped = new HttpHeaderSpoofingRequestWrapper(request);
        boolean changed = false;
        
        Map parameters = request.getParameterMap();
        for (Object parameter : parameters.keySet()) {
        	if ((parameter instanceof String)&&(((String)parameter).matches("^http-.+-header$"))) {
				String headerValue = (String) parameters.get(parameter);
				String headerName = ((String) parameter).replaceFirst("^http-", "").replaceFirst("-header$", "");
				wrapped.setHeader(headerName, headerValue);
				changed = true;
			}
        }
        if (changed) {
        	chain.doFilter(wrapped, response);
        } else {
        	chain.doFilter(request, response);
        }
	}
	/**
	 * @see javax.servlet.Filter#init(javax.servlet.FilterConfig)
	 */
	public void init(FilterConfig filterConfig) throws ServletException {
		;
	}

}
