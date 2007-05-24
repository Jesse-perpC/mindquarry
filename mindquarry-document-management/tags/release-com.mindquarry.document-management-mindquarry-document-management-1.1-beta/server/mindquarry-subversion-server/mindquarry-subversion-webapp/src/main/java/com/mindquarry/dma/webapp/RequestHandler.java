package com.mindquarry.dma.webapp;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface RequestHandler {

	public boolean handle(HttpServletRequest request,
			HttpServletResponse response) throws Exception;

}
