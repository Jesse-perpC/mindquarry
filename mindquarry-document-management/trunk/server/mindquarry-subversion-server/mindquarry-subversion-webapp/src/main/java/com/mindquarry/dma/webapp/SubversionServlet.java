package com.mindquarry.dma.webapp;

import java.io.IOException;
import java.util.List;
import java.util.Vector;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class SubversionServlet extends HttpServlet {
	private List<RequestHandler> handlers;

	@Override
	protected void service(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		for (RequestHandler handler : handlers) {
			try {
				if (handler.handle(request, response)) {
					return;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void init() throws ServletException {
		this.handlers = new Vector<RequestHandler>();
		this.handlers.add(new PropfindHandler());
		this.handlers.add(new FallbackHandler());
	}

}
