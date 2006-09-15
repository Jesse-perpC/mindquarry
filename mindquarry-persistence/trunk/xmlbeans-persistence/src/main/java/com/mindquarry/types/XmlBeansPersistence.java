/**
 * Copyright (C) 2005 MindQuarry GmbH, All Rights Reserved
 */
package com.mindquarry.types;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.xmlbeans.XmlObject;

import com.mindquarry.common.persistence.Persistence;
import com.mindquarry.common.persistence.PersistenceException;


/**
 * This exception is used to indicate something went horribly wrong 
 * in usage of persistence component. 
 *
 * @author <a href="bastian(dot)steinert(at)mindquarry(dot)com">Bastian Steinert</a>
 */
public class XmlBeansPersistence implements Persistence {

    private static final String DOCUMENT_CLASS_SUFFIX = "Document";
    private static final String DOCUMENT_FACTORY_CLASS = "Factory";
    private static final String FACTORY_METHOD = "newInstance";
    
    /* a map holding method objects 
     * to create xmlbean documents for genereated types (used as key */
    private Map<Class, Method> documentFactoryMethods_;
    
    public XmlBeansPersistence() {
        documentFactoryMethods_ = new HashMap<Class, Method>();
    }
    
    public Object newInstance(Class clazz) {
        validateEntityClass(clazz);        
        XmlObject document = createDocument(clazz);
        
        Method addNewMethod;
        try {
            addNewMethod = document.getClass().getMethod("addNew" + clazz.getSimpleName(), null);
        } catch (SecurityException e) {
            throw new RuntimeException(e);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
        
        Object result;
        try {
            result = addNewMethod.invoke(document, null);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        }
        
        return result;
    }
    
    private void validateEntityClass(Class clazz) {
    	if (! clazz.getInterfaces()[0].equals(XmlObject.class)) 
    		throw new PersistenceException(
    				"the class: " + clazz.getName() + 
                    "is not a valid XML Beans type; " +
                    "it does not extend the XmlObject interface.");
    	
    	if (clazz.getSimpleName().endsWith(DOCUMENT_CLASS_SUFFIX))
            throw new PersistenceException(
                    "you must not use the xmlbeans '*Document' classe. " +
                    "Please, find the persistence framework usage " +
                    "documentation in the Mindquarry wiki.");
    }
    
    private XmlObject createDocument(Class clazz) {
        Method factoryMethod = documentFactoryMethod(clazz);
        try {
            return (XmlObject) factoryMethod.invoke(null, null);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }
    
    private Method documentFactoryMethod(Class clazz) {
        Method result = documentFactoryMethods_.get(clazz);
        if (null == result) {
            result = loadDocumentFactoryMethod(clazz);
            synchronized (documentFactoryMethods_) {
                documentFactoryMethods_.put(clazz, result);
            }
        }
        return result;
    }
    
    private Method loadDocumentFactoryMethod(Class clazz) {
        Class factoryClazz = loadDocumentFactoryClazz(clazz);
        try {
            return factoryClazz.getMethod(FACTORY_METHOD, null);
        } catch (SecurityException e) {
            throw new PersistenceException(
                    "was not allowed not introspect/reflect the class: " +
                    factoryClazz, e);
        } catch (NoSuchMethodException e) {
            throw new PersistenceException(
                    "the found document factory class: " + factoryClazz +
                    " has unexpected content. no method " + FACTORY_METHOD + 
                    " could be found.");
        }
    }
    
    private Class loadDocumentFactoryClazz(Class clazz) {
        Class documentClazz = loadDocumentClazz(clazz);
        Class result = null;
        if (1 == documentClazz.getClasses().length) {
            Class innerClass = documentClazz.getClasses()[0];
            if (DOCUMENT_FACTORY_CLASS.equals(innerClass.getSimpleName())) {
                result = innerClass;
            }
        }
        if (null == result) {
            throw new PersistenceException(
                    "the found document class: " + documentClazz +
                    " has unexpected content. no inner " + 
                    DOCUMENT_FACTORY_CLASS + " class could be found.");
        }
        return result;
    }
    
    private Class loadDocumentClazz(Class clazz) {
        String documentClazzName = clazz.getName() + "Document";
        try {
            return classLoader().loadClass(documentClazzName);
        } catch (ClassNotFoundException e) {
            throw new PersistenceException(
                    "could not load xmlbeans generated class needed " +
                    "to handle persistence of class: " + clazz, e);
        }
    }
    
    private ClassLoader classLoader() {
        return getClass().getClassLoader();
    }

    public void persist(Object transientInstance) {
        XmlObject xmlTransientInstance = (XmlObject) transientInstance;
        try {
            xmlTransientInstance.save(new File("test.xml"));
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }        
    }

    public List query(String arg0, Object[] arg1) {
        // TODO Auto-generated method stub
        return null;
    }

}
