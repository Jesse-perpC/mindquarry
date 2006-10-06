/**
 * Copyright (C) 2006 Mindquarry GmbH, All Rights Reserved
 */
package com.mindquarry.persistence.xmlbeans.xml;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Arrays;

import org.apache.excalibur.xml.sax.XMLizable;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;

import com.mindquarry.common.init.InitializationException;

/**
 * Add summary documentation here.
 *
 * @author
 * <a href="mailto:bastian.steinert(at)mindquarry.com">Bastian Steinert</a>
 */
class ContentHandlerInvocationHandler implements InvocationHandler {
    
    static private Method startElementMethod_;
    static private Method endElementMethod_;
    
    static {
        Class[] paramTypes = new Class[4];
        Arrays.fill(paramTypes, String.class);
        paramTypes[3] = Attributes.class;
        
        startElementMethod_ = getStartElementMethod();
        endElementMethod_ = getEndElementMethod();
    }
    
    static private Method getStartElementMethod() {        
        Class[] paramTypes = new Class[4];
        Arrays.fill(paramTypes, String.class);
        paramTypes[3] = Attributes.class;
        return getContentHandlerMethod("startElement", paramTypes);
    }
    
    static private Method getEndElementMethod() {        
        Class[] paramTypes = new Class[3];
        Arrays.fill(paramTypes, String.class);
        return getContentHandlerMethod("endElement", paramTypes);
    }
    
    static private Method getContentHandlerMethod(
            String name, Class[] paramTypes) {
        
        try {
            return ContentHandler.class.getMethod(name, paramTypes);
        } catch (SecurityException e) {
            throw new InitializationException( "could not get method: " + 
                    name + " from class: " + ContentHandler.class);
        } catch (NoSuchMethodException e) {
            throw new InitializationException( "could not get method: " + 
                    name + " from class: " + ContentHandler.class);
        }
    }
    
    private int elementDepth_;
    private ContentHandler contentHandler_;
    private XMLizable xmlizableChild_;
    
    ContentHandlerInvocationHandler(ContentHandler contentHandler,
            XMLizable xmlizableChild) {
        
        elementDepth_ = 0;
        contentHandler_ = contentHandler;
        xmlizableChild_ = xmlizableChild;
    }
    
    public Object invoke(Object proxy, Method method, Object[] args) 
        throws Throwable {
        
        if (startElementMethod_.equals(method)) {
            elementDepth_++;
        }
        
        if (endElementMethod_.equals(method)) {
            if (1 == elementDepth_ && null != xmlizableChild_) {
                xmlizableChild_.toSAX(contentHandler_);
            }
            elementDepth_--;
        }
        
        return method.invoke(contentHandler_, args);
    }
}
