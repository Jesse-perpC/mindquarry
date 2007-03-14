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

import static com.mindquarry.model.source.ReflectionUtil.invokeMethod;

import java.lang.reflect.Method;
import java.util.Collection;

/**
 *
 * @author 
 * <a href="mailto:bastian(dot)steinert(at)mindquarry(dot)com">Bastian Steinert</a>
 */
class ModelSourceInterpreter {

	private String componentName_;
	private Collection<String> statements_;
	
	private ContainerUtil containerUtil_;
	
	ModelSourceInterpreter(ContainerUtil containerUtil,
			String componentName, Collection<String> statements) {

        containerUtil_ = containerUtil;
		
		componentName_ = componentName;
		statements_ = statements;
	}
	
	Object interpret() {
		Object context = containerUtil_.lookupComponent(componentName_);
    	
    	for (String statement : statements_) {
    		
        	String methodName = statement.substring(0, statement.indexOf('('));
    		
    		int argsBegin = statement.indexOf('(') + 1;
    		int argsEnd = statement.indexOf(')');
    		String[] args;
    		
    		if (argsBegin + 1 == argsEnd) {
    			args = new String[0];
    		}
    		else {
    			String argsString = statement.substring(argsBegin, argsEnd);
    			args = argsString.split(",");
    		}
    		

    		Method method = findMethod(methodName, args.length, context.getClass());
    		
    		Object[] typedArgs = new Object[args.length];
    		for (int i = 0; i < args.length; i++) {
    			Class<?> parameterType = method.getParameterTypes()[i];
    			
    			if (parameterType.equals(int.class)) {
    				typedArgs[i] = Integer.parseInt(args[i]);
    			}
    			else if (parameterType.equals(boolean.class)) {
    				typedArgs[i] = Boolean.parseBoolean(args[i]);
    			}
    			else if (parameterType.equals(String.class)) {
    				typedArgs[i] = args[i].trim();
    			}
    			else {
    				throw new ModelSourceException(
						"non primitve parameter types are not supported.");
    			}
    		}
    		
			context = invokeMethod(method, context, typedArgs);
    	}
    	
    	return context;
	}
	
	private Method findMethod(String name, int nParams, Class clazz) {
		Method result = lookForMethod(name, nParams, clazz);
		if (result == null) {
			throw new ModelSourceException(
					"no method " + "with name: " + name + " and " + 
					nParams + " parameters " + "in class " + clazz);
		}
		return result;
	}
	
	private Method lookForMethod(String name, int nParams, Class clazz) {
		Method result = null;
		for (Method method : clazz.getMethods()) {
			if (method.getName().equals(name)
					&& method.getParameterTypes().length == nParams) {
				
				result = method;
				break;
			}
		}
		return result;
	}
}
