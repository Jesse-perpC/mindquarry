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
 * @author <a href="mailto:bastian(dot)steinert(at)mindquarry(dot)com">Bastian
 *         Steinert</a>
 */
class ModelSourceInterpreter {

    private String componentName_;
    private Collection<String> statements_;
    
    private ContainerUtil containerUtil_;
    
    ModelSourceInterpreter(ContainerUtil containerUtil, String componentName,
        Collection<String> statements) {
    
        containerUtil_ = containerUtil;
        
        componentName_ = componentName;
        statements_ = statements;
    }

    Object interpret() {
    	Object context = containerUtil_.lookupComponent(componentName_);
    
    	for (String statement : statements_) {
    
    	    String methodName = methodName(statement);
    	    String[] parameters = parameters(statement);
    
    	    Method method = findMethod(context.getClass(), 
                    methodName, parameters.length);
    
    	    Object[] parsedParameters = parseParameters(method, parameters);
    
    	    context = invokeMethod(method, context, parsedParameters);
    	}
    
    	return context;
    }
    
    private String methodName(String statement) {
        return statement.substring(0, statement.indexOf('('));
    }
    
    private String[] parameters(String statement) {
        int argsBegin = statement.indexOf('(') + 1;
        int argsEnd = statement.indexOf(')');

        if (argsBegin == argsEnd)
            return new String[0];
        else {
            String argsString = statement.substring(argsBegin, argsEnd);
            return argsString.split(",");
        }
    }
    
    private Object[] parseParameters(Method method, String[] parameters) {
        Object[] result = new Object[parameters.length];
        Class<?>[] parameterTypes = method.getParameterTypes();
        for (int i = 0; i < parameters.length; i++) {
            result[i] = parseParameter(parameterTypes[i], parameters[i]);
        }
        return result;
    }
    
    private Object parseParameter(Class<?> clazz, String parameter) {
        Object result;
        if (clazz.equals(int.class)) {
            result = Integer.parseInt(parameter);
        } else if (clazz.equals(boolean.class)) {
            result = Boolean.parseBoolean(parameter);
        } else if (clazz.equals(String.class)) {
            result = parameter.trim();
        } else {
            throw new ModelSourceException(
                "non primitve parameter types are not supported.");
        }
        return result;
    }

    private Method findMethod(Class clazz, String name, int nParams) {
    	Method result = lookForMethod(name, nParams, clazz);
    	if (result == null) {
    	    throw new ModelSourceException("no method " + "with name: " + name
    		    + " and " + nParams + " parameters " + "in class " + clazz);
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
