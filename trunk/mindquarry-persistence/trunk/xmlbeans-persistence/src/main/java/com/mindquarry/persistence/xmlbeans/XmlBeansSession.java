/**
 * Copyright (C) 2005 MindQuarry GmbH, All Rights Reserved
 */
package com.mindquarry.persistence.xmlbeans;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.apache.avalon.framework.service.ServiceException;
import org.apache.avalon.framework.service.ServiceManager;
import org.apache.commons.io.IOUtils;
import org.apache.excalibur.source.Source;
import org.apache.excalibur.source.SourceNotFoundException;
import org.apache.excalibur.source.SourceResolver;
import org.apache.xmlbeans.XmlBeans;
import org.apache.xmlbeans.XmlObject;
import org.xml.sax.SAXException;

import com.mindquarry.common.init.InitializationException;
import com.mindquarry.common.persistence.Session;
import com.mindquarry.jcr.xml.source.JCRNodeWrapperSource;
import com.mindquarry.persistence.xmlbeans.config.Entity;


/**
 * 
 *
 * @author <a href="bastian(dot)steinert(at)mindquarry(dot)com">Bastian Steinert</a>
 */
class XmlBeansSession implements Session {

    private static final String JCR_SOURCE_PREFIX = "jcr://";
    
    private Map<Class, Entity> entityMap_;
    private ServiceManager serviceManager_;
    
    private XmlBeansDocumentCreator documentCreator_;
    private XmlBeansEntityCreator entityCreator_;
    
    XmlBeansSession(ServiceManager serviceManager, 
            Map<Class, Entity> entityMap) {
        
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
        
        JCRNodeWrapperSource source = resolveJcrSource(entityPath);
        
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

    public List query(String queryKey, Object[] params) {
        XmlBeans.getBuiltinTypeSystem();
        Query query = new Query("/users/:userId", params);
        Source source = resolveJcrSource(query.prepare());
        
        SAXParser sourceParser = createSaxParser();
        
        try {
            IOUtils.copy(source.getInputStream(), System.out);
        } catch (SourceNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
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
    
    private JCRNodeWrapperSource resolveJcrSource(String path) {
        String uri = JCR_SOURCE_PREFIX + path;
        SourceResolver sourceResolver = lookupSourceResolver();
        try {
            return (JCRNodeWrapperSource) sourceResolver.resolveURI(uri);
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
