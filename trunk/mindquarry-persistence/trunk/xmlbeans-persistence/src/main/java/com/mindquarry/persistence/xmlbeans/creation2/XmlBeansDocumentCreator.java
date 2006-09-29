/**
 * Copyright (C) 2005 MindQuarry GmbH, All Rights Reserved
 */
package com.mindquarry.persistence.xmlbeans.creation2;

import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.XmlOptions;

import com.mindquarry.persistence.xmlbeans.reflection.DocumentReflectionData;

/**
 * Add summary documentation here.
 *
 * @author
 * <a href="mailto:bastian.steinert(at)mindquarry.com">Bastian Steinert</a>
 */
public class XmlBeansDocumentCreator {

    private DocumentReflectionData documentReflectionData_;
    
    public XmlBeansDocumentCreator(
            DocumentReflectionData documentReflectionData) {
        documentReflectionData_ = documentReflectionData;
    }
    
    public XmlObject newDocumentFor(XmlObject entity, Class entityClazz) {
        XmlObject document = makeEmptyDocument(entityClazz);
        return setEntityInDocument(entity, entityClazz, document);
    }
    
    XmlObject newDocumentFor(InputStream contentIn, Class entityClazz) {
        Method parseMethod = documentParseMethod(entityClazz);
        return invokeParseMethod(parseMethod, contentIn);
    }
    
    private XmlObject invokeParseMethod(
            Method parseMethod, InputStream contentIn) {
        
        try {
            Object[] params = new Object[] {contentIn, makeParserXmlOptions()};
            return (XmlObject) parseMethod.invoke(null, params);
        } catch (IllegalArgumentException e) {
            throw XmlBeansPersistenceCreationException.parseMethodFailed(
                    parseMethod,e);
        } catch (IllegalAccessException e) {
            throw XmlBeansPersistenceCreationException.parseMethodFailed(
                    parseMethod,e);
        } catch (InvocationTargetException e) {
            throw XmlBeansPersistenceCreationException.parseMethodFailed(
                    parseMethod,e);
        }
    }
    
    private XmlOptions makeParserXmlOptions() {
        XmlOptions result = new XmlOptions();
        result.setCompileDownloadUrls();
        return result;
    }
    
    private Method documentParseMethod(Class entityClazz) {
        return documentReflectionData_.documentParseMethod(entityClazz);
    }
    
    private XmlObject setEntityInDocument(
            XmlObject entity, Class entityClazz, XmlObject document) {
        
        Method setEntityMethod = setEntityMethod(entityClazz);
        invokeSetEntityMethod(entity, document, setEntityMethod);
        return document;
    }

    private void invokeSetEntityMethod(
            XmlObject entity, XmlObject document, Method setEntityMethod) {
        try {
            Object[] params = new Object[] {entity};
            setEntityMethod.invoke(document, params);
        } catch (IllegalArgumentException e) {
            throw XmlBeansPersistenceCreationException.setEntityMethodFailed(
                    setEntityMethod.getName(), document.getClass(),e);
        } catch (IllegalAccessException e) {
            throw XmlBeansPersistenceCreationException.setEntityMethodFailed(
                    setEntityMethod.getName(), document.getClass(),e);
        } catch (InvocationTargetException e) {
            throw XmlBeansPersistenceCreationException.setEntityMethodFailed(
                    setEntityMethod.getName(), document.getClass(),e);
        }
    }
    
    private Method setEntityMethod(Class entityClazz) {
        return documentReflectionData_.setEntityMethod(entityClazz);
    }
    
    private XmlObject makeEmptyDocument(Class entityClazz) {
        Method factoryMethod = documentFactoryMethod(entityClazz);
        return invokeDocumentFactoryMethod(entityClazz, factoryMethod);
    }

    private XmlObject invokeDocumentFactoryMethod(
            Class clazz, Method factoryMethod) {
        try {
            return (XmlObject) factoryMethod.invoke(null, new Object[0]);
        } catch (IllegalArgumentException e) {
            throw XmlBeansPersistenceCreationException.factoryMethodFailed(clazz, e);
        } catch (IllegalAccessException e) {
            throw XmlBeansPersistenceCreationException.factoryMethodFailed(clazz, e);
        } catch (InvocationTargetException e) {
            throw XmlBeansPersistenceCreationException.factoryMethodFailed(clazz, e);
        }
    }
    
    private Method documentFactoryMethod(Class entityClazz) {
        return documentReflectionData_.documentFactoryMethod(entityClazz);
    }
}
