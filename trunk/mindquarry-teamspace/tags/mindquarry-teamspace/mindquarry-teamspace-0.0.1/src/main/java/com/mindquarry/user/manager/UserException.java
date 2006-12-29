/**
 * Copyright (C) 2005 MindQuarry GmbH, All Rights Reserved
 */
package com.mindquarry.user.manager;

/**
 * 
 *
 * @author 
 * <a href="bastian.steinert(at)mindquarry.com">Bastian Steinert</a>
 */
class UserException extends RuntimeException {

	private static final long serialVersionUID = -83840116191701053L;

	/**
	 * 
	 */
	public UserException() {
	}

	/**
	 * @param message
	 */
	public UserException(String message) {
		super(message);
	}

	/**
	 * @param cause
	 */
	public UserException(Throwable cause) {
		super(cause);
	}

	/**
	 * @param message
	 * @param cause
	 */
	public UserException(String message, Throwable cause) {
		super(message, cause);
	}
}
