/**
 * Copyright (C) 2005 MindQuarry GmbH, All Rights Reserved
 */
package com.mindquarry.persistence.xmlbeans;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.apache.avalon.framework.service.ServiceException;
import org.apache.avalon.framework.service.ServiceManager;
import org.apache.commons.io.IOUtils;
import org.apache.excalibur.source.ModifiableSource;
import org.apache.excalibur.source.Source;
import org.apache.excalibur.source.SourceNotFoundException;
import org.apache.excalibur.source.SourceResolver;
import org.apache.xmlbeans.XmlObject;
import org.xml.sax.SAXException;

import com.mindquarry.common.init.InitializationException;
import com.mindquarry.common.persistence.Session;
import com.mindquarry.persistence.xmlbeans.config.Entity;
import com.mindquarry.persistence.xmlbeans.config.QueryInfo;


/**
 * 
 *
 * @author <a href="bastian(dot)steinert(at)mindquarry(dot)com">Bastian Steinert</a>
 */
class XmlBeansSession implements Session {

    private static final String JCR_SOURCE_PREFIX = "jcr://";
    
    private Map<Class, Entity> entityMap_;
    private Map<String, QueryInfo> queryInfoMap_;
    private ServiceManager serviceManager_;
    
    private XmlBeansDocumentCreator documentCreator_;
    private XmlBeansEntityCreator entityCreator_;
    
    XmlBeansSession(ServiceManager serviceManager, 
            Map<Class, Entity> entityMap, Map<String, QueryInfo> queryMap) {
        
        queryInfoMap_ = queryMap;
        entityMap_ = entityMap;
        serviceManager_ = serviceManager;
        documentCreator_ = new XmlBeansDocumentCreator();
        entityCreator_ = new XmlBeansEntityCreator();
    }
    
    public Object newEntity(Class entityClazz) {
        validateEntityClass(entityClazz);
        return entityCreator_.newEntityFor(entityClazz);
    }

    public void persist(Object transientInstance) {
        
        Class entityClazz = entityClazz(transientInstance);        
        XmlObject xmlTransientInstance = (XmlObject) transientInstance;
        
        String id = entityId(xmlTransientInstance);
        String basePath = entityBasePath(entityClazz);
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
    
    private String entityBasePath(Class entityClazz) {
        return entityMap_.get(entityClazz).getPath();
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
        
        QueryInfo queryInfo = queryInfoMap_.get(queryKey);
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
        
        XmlObject document = documentCreator_.newDocumentFor(
                sourceIn, entityClazz);
        
        
        Method getEntityMethod;
        try {
            getEntityMethod = document.getClass().getMethod(
                    "get" + entityClazz.getSimpleName(), new Class[0]);
        } catch (SecurityException e1) {
            throw new RuntimeException(e1);
        } catch (NoSuchMethodException e1) {
            throw new RuntimeException(e1);
        }
        
        XmlObject entity;
        try {
            entity = (XmlObject) getEntityMethod.invoke(document, new Object[0]);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        }
        
        List<Object> result = new LinkedList<Object>();
        result.add(entity);
        return result;
    }
    
    private String prepareQuery(QueryInfo queryInfo, Object[] params) {
        return new QueryPreparer(queryInfo.getQuery(), params).prepare();
    }
    
    private SAXParser createSaxParser() {
        SAXParserFactory parserFactory = SAXParserFactory.newInstance();
        parserFactory.setNamespaceAware(true);
        try {
            return parserFactory.newSAXParser();
        } catch (ParserConfigurationException e) {
            throw new InitializationException("could not create sax parser", e);
        } catch (SAXException e) {
            throw new InitializationException("could not create sax parser", e);
        }
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
        String uri = JCR_SOURCE_PREFIX + path;
        SourceResolver sourceResolver = lookupSourceResolver();
        try {
            return (ModifiableSource) sourceResolver.resolveURI(uri);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    
    private SourceResolver lookupSourceResolver() {
        try {
            return (SourceResolver) serviceManager_.lookup(SourceResolver.ROLE);
        } catch (ServiceException e) {
            throw new InitializationException(
                    "lookup of SourceResolver failed");
        }
    }
}
