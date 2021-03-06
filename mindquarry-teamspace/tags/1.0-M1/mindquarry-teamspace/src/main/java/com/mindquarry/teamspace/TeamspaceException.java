/*
 * Copyright (C) 2006-2007 Mindquarry GmbH, All Rights Reserved
 * 
 * The contents of this file are subject to the Mozilla Public License
 * Version 1.1 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * http://www.mozilla.org/MPL/
 *
 * Software distributed under the License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
 * License for the specific language governing rights and limitations
 * under the License.
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
