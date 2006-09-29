/**
 * Copyright (C) 2005 MindQuarry GmbH, All Rights Reserved
 */
package com.mindquarry.persistence.xmlbeans;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.LinkedList;
import java.util.List;

import org.apache.excalibur.source.ModifiableSource;
import org.apache.excalibur.source.Source;
import org.apache.excalibur.source.SourceNotFoundException;
import org.apache.xmlbeans.XmlObject;

import com.mindquarry.common.persistence.Session;
import com.mindquarry.persistence.xmlbeans.config.PersistenceConfiguration;
import com.mindquarry.persistence.xmlbeans.config.QueryInfo;
import com.mindquarry.persistence.xmlbeans.creation.XmlBeansDocumentCreator;
import com.mindquarry.persistence.xmlbeans.creation.XmlBeansEntityCreator;
import com.mindquarry.persistence.xmlbeans.source.JcrSourceResolver;


/**
 * 
 *
 * @author <a href="bastian(dot)steinert(at)mindquarry(dot)com">Bastian Steinert</a>
 */
class XmlBeansSession implements Session {
    
    private final PersistenceConfiguration configuration_;
    private final XmlBeansDocumentCreator documentCreator_;
    private final XmlBeansEntityCreator entityCreator_;
    
    private final JcrSourceResolver jcrSourceResolver_;
    
    /**
     * @param configuration
     * @param documentCreator
     * @param entityCreator
     * @param jcrSourceResolver
     */
    XmlBeansSession(final PersistenceConfiguration configuration, 
            final XmlBeansDocumentCreator documentCreator, 
            final XmlBeansEntityCreator entityCreator, 
            final JcrSourceResolver jcrSourceResolver) {
        
        super();
        configuration_ = configuration;
        documentCreator_ = documentCreator;
        entityCreator_ = entityCreator;
        jcrSourceResolver_ = jcrSourceResolver;
    }

    public Object newEntity(Class entityClazz) {
        validateEntityClass(entityClazz);
        return entityCreator_.newEntityFor(entityClazz);
    }

    public void persist(Object transientInstance) {
        
        Class entityClazz = entityClazz(transientInstance);        
        XmlObject xmlTransientInstance = (XmlObject) transientInstance;
        
        String id = entityId(xmlTransientInstance);
        String basePath = configuration_.entityBasePath(entityClazz);
        String entityPath = basePath + "/" + id;
        
        ModifiableSource source = resolveJcrSource(entityPath);
        
        XmlObject entityDocument = documentCreator_.newDocumentFor(
                xmlTransientInstance, entityClazz);        
        try {
            OutputStream out = source.getOutputStream();
            entityDocument.save(out, null);
            out.flush();
            out.close();
        } catch (IOException e) {
            throw new XmlBeansPersistenceException(
                    "could not write xml content to jcr source", e);
        }        
    }
    
    private Class entityClazz(Object transientInstance) {
        Class entityImplClazz = transientInstance.getClass();
        validateEntityImplClass(entityImplClazz);
        Class entityClazz = entityImplClazz.getInterfaces()[0];
        validateEntityClass(entityClazz);
        return entityClazz;
    }
    
    private String entityId(XmlObject entity) {
        Class clazz = entity.getClass();
        try {
            Method getIdMethod = clazz.getMethod("getId", new Class[0]);
            return (String) getIdMethod.invoke(entity, new Object[0]);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Object> query(String queryKey, Object[] params) {
        
        QueryInfo queryInfo = configuration_.queryInfo(queryKey);
        String query = prepareQuery(queryInfo, params);
        Source source = resolveJcrSource(query);
        InputStream sourceIn;
        try {
            sourceIn = source.getInputStream();
        } catch (SourceNotFoundException e2) {
            throw new RuntimeException(e2);
        } catch (IOException e2) {
            throw new RuntimeException(e2);
        }
        
        Class entityClazz;
        try {
            entityClazz = Class.forName(queryInfo.getResultEntityClass());
        } catch (ClassNotFoundException e1) {
            throw new RuntimeException(e1);
        }
        
        XmlObject entity = entityCreator_.newEntityFrom(sourceIn, entityClazz);
                
        List<Object> result = new LinkedList<Object>();
        result.add(entity);
        return result;
    }
    
    private String prepareQuery(QueryInfo queryInfo, Object[] params) {
        return new QueryPreparer(queryInfo.getQuery(), params).prepare();
    }
    
    private void validateEntityImplClass(Class entityImplClazz) {        
        assert isValidXmlBeanImplClass(entityImplClazz) : 
                    "the class: " + entityImplClazz + " seems not to be a valid " +
                    "xmlbeans implementation type, " +
                    "it does not implement any interfaces.";
    }
    
    private boolean isValidXmlBeanImplClass(Class clazz) {
        Class[] extendedInterfaces = clazz.getInterfaces();
        if (1 != extendedInterfaces.length)
            return false;
        
        return true;
    }
    
    private void validateEntityClass(Class entityClazz) {
        String classSimpleName = entityClazz.getSimpleName(); 
        if (classSimpleName.endsWith(Constants.DOCUMENT_CLASS_SUFFIX))
            throw XmlBeansPersistenceException.documentSuffix(entityClazz);
        
        assert isValidXmlBeanClass(entityClazz) : 
                    "the class: " + entityClazz + " seems not to be a valid " +
                    "xmlbeans type, it does not extend " + XmlObject.class;       
    }
    
    private boolean isValidXmlBeanClass(Class clazz) {
        Class[] extendedInterfaces = clazz.getInterfaces();
        if (1 != extendedInterfaces.length)
            return false;
        
        if (! XmlObject.class.equals(extendedInterfaces[0]))
            return false;
        
        return true;
    }
    
    private ModifiableSource resolveJcrSource(String path) {
        return jcrSourceResolver_.resolveJcrSource(path);
    }
}
