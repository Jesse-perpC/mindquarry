/**
 * Copyright (C) 2005 MindQuarry GmbH, All Rights Reserved
 */
package com.mindquarry.common.init;

/**
 * This exception is used to indicate something went horribly wrong during
 * initialization of a mindquarry component.
 * 
 * @author 
 * <a href="bastian(dot)steinert(at)mindquarry(dot)com">Bastian Steinert</a>
 */
public class InitializationException extends RuntimeException {

	private static final long serialVersionUID = 7476174092079563106L;

	public InitializationException(String message) {
		super(message);
	}
	
	public InitializationException(String message, Throwable cause) {
		super(message, cause);
	}
}
