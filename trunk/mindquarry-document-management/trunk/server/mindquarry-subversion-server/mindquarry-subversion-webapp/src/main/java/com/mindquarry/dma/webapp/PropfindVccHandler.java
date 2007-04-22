package com.mindquarry.dma.webapp;

import javax.servlet.http.HttpServletRequest;

public class PropfindVccHandler extends SubversionHandler implements
		RequestHandler {

	@Override
	boolean canHandle(HttpServletRequest request) {
		return (request.getContextPath()+request.getServletPath()+request.getPathInfo()).equals(getVcc());
	}

}
