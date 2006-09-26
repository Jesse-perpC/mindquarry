/**
 * Copyright (C) 2005 MindQuarry GmbH, All Rights Reserved
 */
package com.mindquarry.persistence.xmlbeans;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.util.List;
import java.util.Map;

import org.apache.avalon.framework.service.ServiceException;
import org.apache.avalon.framework.service.ServiceManager;
import org.apache.excalibur.source.SourceResolver;
import org.apache.xmlbeans.XmlObject;

import com.mindquarry.common.init.InitializationException;
import com.mindquarry.common.persistence.Session;
import com.mindquarry.jcr.xml.source.JCRNodeWrapperSource;
import com.mindquarry.persistence.xmlbeans.config.Entity;
import com.mindquarry.types.user.User;
import com.mindquarry.types.user.UserDocument;


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
        XmlObject document = documentCreator_.newDocumentFor(entityClazz);
        return entityCreator_.newEntityFor(entityClazz, document);
    }

    public void persist(Object transientInstance) {
        XmlObject xmlTransientInstance = (XmlObject) transientInstance;
        String id = entityId(xmlTransientInstance);
        String basePath = entityBasePath(xmlTransientInstance);
        String entityPath = basePath + "/" + id;
        try {
            UserDocument doc = UserDocument.Factory.newInstance();
            doc.setUser(((User) xmlTransientInstance)); 
            JCRNodeWrapperSource source = resolveJcrSource(entityPath);
            OutputStream out = source.getOutputStream();
            doc.save(out, null);
            out.flush();
            out.close();
            //xmlTransientInstance.save(new File(entityPath.substring(1)));
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }        
    }
    
    private String entityBasePath(XmlObject entity) {
        Class clazz = entity.getClass().getInterfaces()[0];
        return entityMap_.get(clazz).getPath();
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

    public List query(String query, Object[] params) {
        StringBuilder querySB = new StringBuilder(query);
        // TODO Auto-generated method stub
        return null;
    }
    
    private void validateEntityClass(Class entityClazz) {
        assert isValidXmlBeanClass(entityClazz) : 
                    "the class: " + entityClazz + " seems not to be a valid " +
                    "xmlbeans type, it does not extend " + XmlObject.class; 
        
        String classSimpleName = entityClazz.getSimpleName(); 
        if (classSimpleName.endsWith(Constants.DOCUMENT_CLASS_SUFFIX))
            throw XmlBeansPersistenceException.documentSuffix(entityClazz);
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
            throw new RuntimeException();
        } catch (IOException e) {
            throw new RuntimeException();
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
