/**
 * Copyright (C) 2005 MindQuarry GmbH, All Rights Reserved
 */
package com.mindquarry.persistence.xmlbeans.creation;

import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.apache.xmlbeans.XmlObject;

import com.mindquarry.persistence.xmlbeans.reflection.EntityReflectionData;

/**
 * Add summary documentation here.
 *
 * @author
 * <a href="mailto:bastian.steinert(at)mindquarry.com">Bastian Steinert</a>
 */
public class XmlBeansEntityCreator {

    final private EntityReflectionData entityReflectionData_;
    final private XmlBeansDocumentCreator documentCreator_;
    
    public XmlBeansEntityCreator(EntityReflectionData entityReflectionData,
            XmlBeansDocumentCreator documentCreator) {
        
        documentCreator_ = documentCreator;
        entityReflectionData_ = entityReflectionData;
    }
    
    public XmlObject newEntityFor(Class entityClazz) {
        Method factoryMethod = entityFactoryMethod(entityClazz);
        return invokeEntityFactoryMethod(entityClazz, factoryMethod);
    }
    
    public XmlObject newEntityFrom(
            InputStream contentIn, String entityClazzName) {
        
        Class entityClazz = entityReflectionData_.classForName(entityClazzName);
        
        XmlObject document = documentCreator_.newDocumentFor(
                contentIn, entityClazz);
        
        Method getEntityMethod = getEntityMethod(entityClazz);
        return invokeGetEntityMethod(document, getEntityMethod);
    }
    
    private Method getEntityMethod(Class entityClazz) {
        return entityReflectionData_.getEntityMethod(entityClazz);
    }

    private XmlObject invokeGetEntityMethod(
            XmlObject document, Method getEntityMethod) {
        
        try {
            return (XmlObject) getEntityMethod.invoke(document, new Object[0]);
        } catch (IllegalArgumentException e) {
            throw XmlBeansPersistenceCreationException.getEntityMethodFailed(
                    getEntityMethod.getName(), document.getClass(),e);
        } catch (IllegalAccessException e) {
            throw XmlBeansPersistenceCreationException.getEntityMethodFailed(
                    getEntityMethod.getName(), document.getClass(),e);
        } catch (InvocationTargetException e) {
            throw XmlBeansPersistenceCreationException.getEntityMethodFailed(
                    getEntityMethod.getName(), document.getClass(),e);
        }
    }

    private XmlObject invokeEntityFactoryMethod(
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
    
    private Method entityFactoryMethod(Class entityClazz) {
        return entityReflectionData_.entityFactoryMethod(entityClazz);
    }
}
