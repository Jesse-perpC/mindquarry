package com.mindquarry.webapp.servlet;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import junit.framework.TestCase;

import com.mockobjects.servlet.MockFilterChain;
import com.mockobjects.servlet.MockFilterConfig;
import com.mockobjects.servlet.MockHttpServletRequest;
import com.mockobjects.servlet.MockHttpServletResponse;


public class HttpHeaderSpoofingFilterTest extends TestCase {
	public void testDoFilter() throws ServletException, IOException {
		HttpHeaderSpoofingFilter filter = new HttpHeaderSpoofingFilter();
		
		MockFilterConfig filterConfig = new MockFilterConfig();
		MockFilterChain filterChain = new MockFilterChain() {

			@Override
			public void doFilter(ServletRequest req, ServletResponse resp) throws IOException, ServletException {
				HttpServletRequest hreq = (HttpServletRequest) req;
				assertEquals("Headers do not match", "text/plain", hreq.getHeader("Accept"));
				super.doFilter(req, resp);
			}
			
		};
		MockHttpServletRequest servletRequest = new MockHttpServletRequest() {

			@Override
			public Map getParameterMap() {
				Map<String, Object> parameters = new HashMap<String, Object>();
				parameters.put("hallo", "ballo");
				parameters.put("http-Accept-header", new String[] {"text/plain"});
				return parameters;
			}
			
			
			public String getParameter(String name) {
				return "text/plain";
			}
		};
		MockHttpServletResponse servletResponse = new MockHttpServletResponse();
		
		filter.init(filterConfig);
		filter.doFilter(servletRequest, servletResponse, filterChain);
		filter.destroy();
	}
}
