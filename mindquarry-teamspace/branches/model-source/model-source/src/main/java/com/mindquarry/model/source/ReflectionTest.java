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

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author 
 * <a href="mailto:bastian(dot)steinert(at)mindquarry(dot)com">Bastian Steinert</a>
 */
public class ReflectionTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		Method m = ReflectionTest.class.getMethod("getFoos");
		Type t = m.getGenericReturnType();
		if (t instanceof ParameterizedType) {
			ParameterizedType pt = (ParameterizedType) t;
			System.out.println(pt.getActualTypeArguments()[0]);
		}
	}
	
	public List<String> getFoos() {
		return new LinkedList<String>();
	}
}
