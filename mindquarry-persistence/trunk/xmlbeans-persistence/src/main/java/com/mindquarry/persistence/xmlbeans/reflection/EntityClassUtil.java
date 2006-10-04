/**
 * Copyright (C) 2006 Mindquarry GmbH, All Rights Reserved
 */
package com.mindquarry.persistence.xmlbeans.reflection;

import org.apache.xmlbeans.XmlObject;

import com.mindquarry.persistence.xmlbeans.config.PersistenceConfiguration;

/**
 * Add summary documentation here.
 *
 * @author
 * <a href="mailto:bastian.steinert(at)mindquarry.com">Bastian Steinert</a>
 */
public class EntityClassUtil {
    
    static public Class entityClazz(
            Object transientInstance, PersistenceConfiguration configuration) {
        Class entityImplClazz = transientInstance.getClass();
        validateEntityImplClass(entityImplClazz);
        
        Class entityClazz = entityImplClazz.getInterfaces()[0];
        validateEntityClass(entityClazz, configuration);
        
        return entityClazz;
    }
    
    static public void validateEntityClass(
            Class entityClazz, PersistenceConfiguration configuration) {
        
        String classSimpleName = entityClazz.getSimpleName(); 
        if (classSimpleName.endsWith(ReflectionConstants.DOCUMENT_CLASS_SUFFIX))
            throw XmlBeansPersistenceReflectionException.documentSuffix(
                    entityClazz);
        
        assert isValidXmlBeanClass(entityClazz, configuration) : 
                    "the class: " + entityClazz + " seems not to be a valid " +
                    "xmlbeans type, it does not extend " + XmlObject.class;       
    }
    
    static private void validateEntityImplClass(Class entityImplClazz) {        
        assert isValidXmlBeanImplClass(entityImplClazz) : 
                    "the class: " + entityImplClazz + " seems not to be a valid " +
                    "xmlbeans implementation type, " +
                    "it does not implement any interfaces.";
    }
    
    static private boolean isValidXmlBeanImplClass(Class clazz) {
        Class[] extendedInterfaces = clazz.getInterfaces();
        if (1 != extendedInterfaces.length)
            return false;
        
        return true;
    }
    
    static private boolean isValidXmlBeanClass(
            Class clazz, PersistenceConfiguration configuration) {
        
        Class[] extendedInterfaces = clazz.getInterfaces();
        if (1 != extendedInterfaces.length)
            return false;
        
        if (! XmlObject.class.equals(extendedInterfaces[0]))
            return false;
        
        if (! configuration.existsEntity(clazz))
            return false;
        
        return true;
    }
}
