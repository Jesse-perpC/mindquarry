/**
 * Copyright (C) 2006 Mindquarry GmbH, All Rights Reserved
 */
package com.mindquarry.persistence.xmlbeans.reflection;

import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.apache.xmlbeans.XmlOptions;

/**
 * Add summary documentation here.
 *
 * @author
 * <a href="mailto:bastian.steinert(at)mindquarry.com">Bastian Steinert</a>
 */
public class DocumentReflectionData {
    
    private Map<Class, Method> documentFactoryMethods_;
    private Map<Class, Method> documentParseMethods_;
    private Map<Class, Method> setEntityMethods_;
    
    public DocumentReflectionData(Set<Class> entityClazzes) {
        
        documentFactoryMethods_ = new HashMap<Class, Method>();
        loadDocumentFactoryMethods(entityClazzes);
        
        documentParseMethods_ = new HashMap<Class, Method>();
        loadDocumentParseMethods(entityClazzes);
        
        setEntityMethods_ = new HashMap<Class, Method>();
        loadSetEntityMethods(entityClazzes);
    }
    
    public Method documentFactoryMethod(Class entityClazz) {
        return documentFactoryMethods_.get(entityClazz);
    }
    
    private void loadDocumentFactoryMethods(Set<Class> entityClazzes) {
        for (Class entityClazz : entityClazzes) {
            Method factoryMethod = loadDocumentFactoryMethod(entityClazz);
            documentFactoryMethods_.put(entityClazz, factoryMethod);
        }
    }
    
    private Method loadDocumentFactoryMethod(Class entityClazz) {       
        Class documentFactoryClazz = documentFactoryClazz(entityClazz);
        try {
            return documentFactoryClazz.getMethod(ReflectionConstants.FACTORY_METHOD, new Class[0]);
        } catch (SecurityException e) {
            throw XmlBeansPersistenceReflectionException.gotNoFactoryMethod(
                    documentFactoryClazz, e);
        } catch (NoSuchMethodException e) {
            throw XmlBeansPersistenceReflectionException.gotNoFactoryMethod(
                    documentFactoryClazz, e);
        }
    }
    
    public Method documentParseMethod(Class entityClazz) {
        return documentParseMethods_.get(entityClazz);
    }
    
    private void loadDocumentParseMethods(Set<Class> entityClazzes) {
        for (Class entityClazz : entityClazzes) {
            Method parseMethod = loadDocumentParseMethod(entityClazz);
            documentParseMethods_.put(entityClazz, parseMethod);
        }
    }
    
    private Method loadDocumentParseMethod(Class entityClazz) {
        Class documentFactoryClazz = documentFactoryClazz(entityClazz);
        try {
            Class[] paramTypes = new Class[2];
            paramTypes[0] = InputStream.class;
            paramTypes[1] = XmlOptions.class;
            return documentFactoryClazz.getMethod(
                    ReflectionConstants.PARSE_METHOD, paramTypes);
        } catch (SecurityException e) {
            throw XmlBeansPersistenceReflectionException.gotNoFactoryMethod(
                    documentFactoryClazz, e);
        } catch (NoSuchMethodException e) {
            throw XmlBeansPersistenceReflectionException.gotNoFactoryMethod(
                    documentFactoryClazz, e);
        }
    }
    
    private Class documentFactoryClazz(Class entityClazz) {
        Class documentClazz = loadDocumentClazz(entityClazz);
        assert isValidDocumentClass(documentClazz);        
        return documentClazz.getClasses()[0];
    }
    
    public Method setEntityMethod(Class entityClazz) {
        return setEntityMethods_.get(entityClazz);
    }
    
    private void loadSetEntityMethods(Set<Class> entityClazzes) {
        for (Class entityClazz : entityClazzes) {
            Method setEntityMethod = loadSetEntityMethod(entityClazz);
            setEntityMethods_.put(entityClazz, setEntityMethod);
        }
    }
    
    private Method loadSetEntityMethod(Class entityClazz) {
        // load Methods like setUser(User user)
        String methodName = "set" + entityClazz.getSimpleName();
        Class documentClazz = loadDocumentClazz(entityClazz);
        try {            
            Class[] paramTypes = new Class[] {entityClazz};
            return documentClazz.getMethod(methodName, paramTypes);
        } catch (SecurityException e) {
            throw XmlBeansPersistenceReflectionException.gotNoSetEntityMethod(
                    methodName, documentClazz, e);
        } catch (NoSuchMethodException e) {
            throw XmlBeansPersistenceReflectionException.gotNoSetEntityMethod(
                    methodName, documentClazz, e);
        }
    }
    
    private boolean isValidDocumentClass(Class documentClazz) {
        Class[] innerClasses = documentClazz.getClasses();
        if (1 != innerClasses.length)
            return false;
        
        String firstInnerClassName = innerClasses[0].getSimpleName();
        if (! ReflectionConstants.FACTORY_CLASS.equals(firstInnerClassName))
            return false;
        
        return true;
    }
    
    private Class loadDocumentClazz(Class entityClazz) {        
        String documentClazzName = 
            entityClazz.getName() + ReflectionConstants.DOCUMENT_CLASS_SUFFIX;
        try {
            return Class.forName(documentClazzName);
        } catch (ClassNotFoundException e) {
            throw XmlBeansPersistenceReflectionException.classNotFound(e);
        }
    }
}
