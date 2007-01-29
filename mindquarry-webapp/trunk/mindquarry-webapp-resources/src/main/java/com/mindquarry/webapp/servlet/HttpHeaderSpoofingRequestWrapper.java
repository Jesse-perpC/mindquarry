package com.mindquarry.webapp.servlet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class HttpHeaderSpoofingRequestWrapper extends HttpServletRequestWrapper {
	private Map<String, String> headers;
	public HttpHeaderSpoofingRequestWrapper(HttpServletRequest request) {
		super(request);
		this.headers = new HashMap<String, String>();
	}
	@Override
	public String getHeader(String name) {
		String header = (String) this.headers.get(name);
		if (header==null) {
			header = super.getHeader(name);
		}
		return header;
	}
	@Override
	public Enumeration getHeaderNames() {
		Set<String> names = this.headers.keySet();
		Collection<String> othernames = Collections.list(super.getHeaderNames());
		names.addAll(othernames);
		
		return Collections.enumeration(names);
	}
	@Override
	public Enumeration getHeaders(String name) {
		if (this.headers.get(name)!=null) {
			return Collections.enumeration(Collections.singleton(this.headers.get(name)));
		} else {
			return super.getHeaders(name);
		}
	}
	@Override
	public int getIntHeader(String name) {
		return Integer.parseInt(this.getHeader(name));
	}
	
	public void setHeader(String headerName, String headerValue) {
		this.headers.put(headerName, headerValue);
	}

}
