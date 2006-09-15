/**
 * Copyright (C) 2005 MindQuarry GmbH, All Rights Reserved
 */
package com.mindquarry.types;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

import org.apache.xmlbeans.XmlObject;

import com.mindquarry.common.persistence.Persistence;


/**
 * 
 *
 * @author <a href="bastian(dot)steinert(at)mindquarry(dot)com">Bastian Steinert</a>
 */
public class XmlBeansPersistence implements Persistence {

    public Object newInstance(Class clazz) {
        String entityClassName = clazz.getName();
        Class entityDocumentClass;
        try {
            entityDocumentClass = getClass().getClassLoader().loadClass(entityClassName + "Document");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        Class entityDocumentFactoryClass = entityDocumentClass.getClasses()[0];
        Method factoryMethod;
        try {
            factoryMethod = entityDocumentFactoryClass.getMethod("newInstance", null);
        } catch (SecurityException e) {
            throw new RuntimeException(e);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
        
        Object documentFactory;
        try {
            documentFactory = factoryMethod.invoke(null, null);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        }
        
        Method addNewMethod;
        try {
            addNewMethod = documentFactory.getClass().getMethod("addNew" + clazz.getSimpleName(), null);
        } catch (SecurityException e) {
            throw new RuntimeException(e);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
        
        Object result;
        try {
            result = addNewMethod.invoke(documentFactory, null);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        }
        
        return result;
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
