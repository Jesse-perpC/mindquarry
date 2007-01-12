/*
 * Copyright (C) 2006-2007 MindQuarry GmbH, All Rights Reserved
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
