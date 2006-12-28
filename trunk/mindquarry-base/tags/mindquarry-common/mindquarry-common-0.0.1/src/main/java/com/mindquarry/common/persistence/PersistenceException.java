/**
 * Copyright (C) 2005 MindQuarry GmbH, All Rights Reserved
 */
package com.mindquarry.common.persistence;

/**
 * This exception is used to indicate something went horribly wrong during 
 * initialization of a mindquarry component.
 * 
 * @author 
 * <a href="bastian(dot)steinert(at)mindquarry(dot)com">Bastian Steinert</a>
 */
public class PersistenceException extends RuntimeException {

	private static final long serialVersionUID = 7476174092079563106L;

	public PersistenceException(String message) {
		super(message);
	}
	
	public PersistenceException(String message, Throwable cause) {
		super(message, cause);
	}
}
