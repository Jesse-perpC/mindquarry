/**
 * Copyright (C) 2005 MindQuarry GmbH, All Rights Reserved
 */
package com.mindquarry.project;

/**
 * 
 *
 * @author <a href="bastian(dot)steinert(at)mindquarry(dot)com">Bastian Steinert</a>
 */
public class ProjectException extends RuntimeException {

	private static final long serialVersionUID = -83840116191701053L;

	/**
	 * 
	 */
	public ProjectException() {
	}

	/**
	 * @param message
	 */
	public ProjectException(String message) {
		super(message);
	}

	/**
	 * @param cause
	 */
	public ProjectException(Throwable cause) {
		super(cause);
	}

	/**
	 * @param message
	 * @param cause
	 */
	public ProjectException(String message, Throwable cause) {
		super(message, cause);
	}
}
