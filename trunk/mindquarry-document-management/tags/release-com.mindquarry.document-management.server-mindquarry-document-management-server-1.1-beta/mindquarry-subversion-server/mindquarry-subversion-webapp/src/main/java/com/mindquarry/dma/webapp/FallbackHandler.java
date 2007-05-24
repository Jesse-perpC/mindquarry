package com.mindquarry.dma.webapp;

import java.io.Writer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class FallbackHandler implements RequestHandler {

	public boolean handle(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		//response.sendError(500);
		Writer writer = response.getWriter();
		writer.write("No handler configured.");
		writer.close();
		return true;
	}

}
