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
package com.mindquarry.jcr.jackrabbit;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import javax.jcr.Session;

/**
 * @author 
 * <a href="mailto:bastian.steinert(at)mindquarry.com">Bastian Steinert</a>
 */
class AttributableSessionInvocationHandler implements InvocationHandler {

    private Method getAttributeMethod_;
    
    private Method setAttributeMethod_;
    
    private final Session target_;
    
    private final Map<String, Object> attributeMap_;

    public AttributableSessionInvocationHandler(Session target) {
        target_ = target;
        attributeMap_ = new HashMap<String, Object>();
    }
    
    private Method getAttributeMethod() 
        throws SecurityException, NoSuchMethodException {
        
        if (getAttributeMethod_ == null) {
            Class[] paramTypes = new Class[] {String.class};
            getAttributeMethod_ = 
                AttributableSession.class.getMethod("getAttribute", paramTypes);
        }
        return getAttributeMethod_;
    }
    
    private Method setAttributeMethod() 
        throws SecurityException, NoSuchMethodException {
        
        if (setAttributeMethod_ == null) {
            Class[] paramTypes = new Class[] {String.class, Object.class};
            setAttributeMethod_ = 
                AttributableSession.class.getMethod("setAttribute", paramTypes);
        }
        return setAttributeMethod_;
    }

    public Object getAttribute(String key) {
        return attributeMap_.get(key);
    }

    public void setAttribute(String key, Object value) {
        attributeMap_.put(key, value);
    }

    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        // Invocation on Session interface coming in...

        if (method.getName().equals("equals")) {
            // Only consider equal when proxies are identical.
            return (proxy == args[0] ? Boolean.TRUE : Boolean.FALSE);
        }
        else if (method.getName().equals("hashCode")) {
            // Use hashCode of Session proxy.
            return new Integer(hashCode());
        }
        else if (method.equals(getAttributeMethod())) {
            return getAttribute((String) args[0]); 
        }
        else if (method.equals(setAttributeMethod())) {
            setAttribute((String) args[0], args[1]);
            return null;
        }

        // Invoke method on target Session.
        try {
            return method.invoke(target_, args);
        }
        catch (InvocationTargetException e) {
            throw e.getTargetException();
        }
    }
}
