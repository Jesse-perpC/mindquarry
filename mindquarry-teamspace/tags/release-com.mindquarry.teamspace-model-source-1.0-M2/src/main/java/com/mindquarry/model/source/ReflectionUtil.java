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
package com.mindquarry.model.source;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 *
 * @author 
 * <a href="mailto:bastian(dot)steinert(at)mindquarry(dot)com">Bastian Steinert</a>
 */
class ReflectionUtil {

	static Object invokeMethod(Method method, Object obj, Object[] args) {
		try {
			return method.invoke(obj, args);
		} catch (IllegalArgumentException e) {
			throw new ModelSourceException(e);
		} catch (IllegalAccessException e) {
			throw new ModelSourceException(e);
		} catch (InvocationTargetException e) {
			throw new ModelSourceException(e);
		}
	}
	
	static Object invokeGetter(Method method, Object obj) {
		return invokeMethod(method, obj, new Object[0]);
	}
	
	static Field fieldForName(Class clazz, String name) {
		try {
			return clazz.getField(name);
		} catch (SecurityException e) {
			throw new ModelSourceException(e);
		} catch (NoSuchFieldException e) {
			return null;
		}
	}
}
