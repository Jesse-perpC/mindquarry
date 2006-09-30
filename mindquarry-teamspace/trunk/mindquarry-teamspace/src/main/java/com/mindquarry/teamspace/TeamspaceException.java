/**
 * Copyright (C) 2005 MindQuarry GmbH, All Rights Reserved
 */
package com.mindquarry.teamspace;

/**
 * 
 *
 * @author 
 * <a href="bastian.steinert(at)mindquarry.com">Bastian Steinert</a>
 */
public class TeamspaceException extends RuntimeException {

	private static final long serialVersionUID = -83840116191701053L;

	/**
	 * 
	 */
	public TeamspaceException() {
	}

	/**
	 * @param message
	 */
	public TeamspaceException(String message) {
		super(message);
	}

	/**
	 * @param cause
	 */
	public TeamspaceException(Throwable cause) {
		super(cause);
	}

	/**
	 * @param message
	 * @param cause
	 */
	public TeamspaceException(String message, Throwable cause) {
		super(message, cause);
	}
}
