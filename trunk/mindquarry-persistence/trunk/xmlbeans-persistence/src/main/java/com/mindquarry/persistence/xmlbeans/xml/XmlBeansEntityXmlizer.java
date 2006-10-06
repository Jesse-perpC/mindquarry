/**
 * Copyright (C) 2006 Mindquarry GmbH, All Rights Reserved
 */
package com.mindquarry.persistence.xmlbeans.xml;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;

import org.apache.excalibur.xml.sax.XMLizable;
import org.apache.xmlbeans.XmlObject;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;

import com.mindquarry.common.xml.EntityXmlizer;

/**
 * Add summary documentation here.
 *
 * @author
 * <a href="mailto:bastian.steinert(at)mindquarry.com">Bastian Steinert</a>
 */
class XmlBeansEntityXmlizer implements EntityXmlizer {

    private XmlObject entity_;
    private XMLizable xmlizableChild_;
    private Constructor contentHandlerProxyConstructor_;
    
    XmlBeansEntityXmlizer(
            XmlObject entity, Constructor contentHandlerProxyConstructor) {
        
        entity_ = entity;
        contentHandlerProxyConstructor_ = contentHandlerProxyConstructor;
    }
    
    /**
     * @see com.mindquarry.common.xml.EntityXmlizer#beforeEndEntityElement(com.mindquarry.common.xml.EntityXmlizer)
     */
    public void beforeEndEntityElement(XMLizable xmlizableChild) {
        xmlizableChild_ = xmlizableChild;
    }

    /**
     * @see com.mindquarry.common.xml.EntityXmlizer#toSax(org.xml.sax.ContentHandler)
     */
    public void toSAX(ContentHandler contentHandler) {
        InvocationHandler handler = new ContentHandlerInvocationHandler(
                contentHandler, xmlizableChild_);
        
        ContentHandler proxyHandler = createProxyContentHandler(handler);
        try {
            entity_.save(proxyHandler, null);
        } catch (SAXException e) {
            throw new XmlBeansEntityXmlizerException(
                    "could not parse content of entity: " + entity_, e);
        }
    }

    private ContentHandler createProxyContentHandler(
            InvocationHandler handler) {
        
        Object[] params = new Object[] {handler};
        try {
            Object proxy = contentHandlerProxyConstructor_.newInstance(params);
            return (ContentHandler) proxy;
        } catch (IllegalArgumentException e) {
            throw new XmlBeansEntityXmlizerException(
                    "could not create ContentHandler proxy", e);
        } catch (InstantiationException e) {
            throw new XmlBeansEntityXmlizerException(
                    "could not create ContentHandler proxy", e);
        } catch (IllegalAccessException e) {
            throw new XmlBeansEntityXmlizerException(
                    "could not create ContentHandler proxy", e);
        } catch (InvocationTargetException e) {
            throw new XmlBeansEntityXmlizerException(
                    "could not create ContentHandler proxy", e);
        }
    }
}
