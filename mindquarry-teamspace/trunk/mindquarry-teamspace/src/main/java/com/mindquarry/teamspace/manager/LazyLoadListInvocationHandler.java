/**
 * Copyright (C) 2006 Mindquarry GmbH, All Rights Reserved
 */
package com.mindquarry.teamspace.manager;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.List;

/**
 * Add summary documentation here.
 *
 * @author 
 * <a href="mailto:bastian.steinert(at)mindquarry.com">your full name</a>
 */
public class LazyLoadListInvocationHandler<T> implements InvocationHandler {

    private List<T> proxiedList_;
    
    private ListLoading<T> listLoader_;
    
    public LazyLoadListInvocationHandler(ListLoading<T> listLoader) {
        listLoader_ = listLoader;
    }
    
    /**
     * @see java.lang.reflect.InvocationHandler#invoke(java.lang.Object, java.lang.reflect.Method, java.lang.Object[])
     */
    public Object invoke(Object proxy, Method method, Object[] args)
            throws Throwable {
        if (null == proxiedList_) {
            proxiedList_ = listLoader_.load();
        }
        return method.invoke(proxiedList_, args);
    }

}
