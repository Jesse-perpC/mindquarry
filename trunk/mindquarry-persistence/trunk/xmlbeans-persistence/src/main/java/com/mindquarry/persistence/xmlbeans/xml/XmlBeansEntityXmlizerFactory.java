package com.mindquarry.persistence.xmlbeans.xml;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;

import org.apache.avalon.framework.activity.Initializable;
import org.apache.avalon.framework.service.ServiceException;
import org.apache.avalon.framework.service.ServiceManager;
import org.apache.avalon.framework.service.Serviceable;
import org.apache.xmlbeans.XmlObject;
import org.xml.sax.ContentHandler;

import com.mindquarry.common.xml.EntityXmlizer;
import com.mindquarry.common.xml.EntityXmlizerFactory;
import com.mindquarry.persistence.xmlbeans.config.PersistenceConfiguration;
import com.mindquarry.persistence.xmlbeans.creation.XmlBeansDocumentCreator;
import com.mindquarry.persistence.xmlbeans.reflection.EntityClassUtil;

/**
 * Copyright (C) 2006 Mindquarry GmbH, All Rights Reserved
 */

/**
 * Add summary documentation here.
 *
 * @author
 * <a href="mailto:bastian.steinert(at)mindquarry.com">Bastian Steinert</a>
 */
class XmlBeansEntityXmlizerFactory implements 
    Serviceable, Initializable, EntityXmlizerFactory {

    private ServiceManager serviceManager_;
    private PersistenceConfiguration configuration_;
    
    private Constructor contentHandlerProxyConstructor_;

    public void service(ServiceManager serviceManager) throws ServiceException {
        serviceManager_ = serviceManager;
    }

    public void initialize() throws Exception {
        String name = PersistenceConfiguration.class.getName();
        configuration_ = 
            (PersistenceConfiguration) serviceManager_.lookup(name);
        
        contentHandlerProxyConstructor_ = contentHandlerProxyConstructor();
    }
    
    /**
     * @see com.mindquarry.common.xml.EntityXmlizerFactory#newEntityXmlizer(java.lang.Object)
     */
    public EntityXmlizer newEntityXmlizer(Object entity) {
        Class entityClazz = entityClazz(entity);
        XmlObject xmlEntity = (XmlObject) entity;
        
        XmlObject xmlEntityDocument = 
            documentCreator().newDocumentFor(xmlEntity, entityClazz);
        
        return new XmlBeansEntityXmlizer(
                xmlEntityDocument, contentHandlerProxyConstructor_);
    }
    
    private XmlBeansDocumentCreator documentCreator() {
        return configuration_.getDocumentCreator();
    }
    
    private Class entityClazz(Object entity) {
        return EntityClassUtil.entityClazz(entity, configuration_);
    }
    
    private Constructor contentHandlerProxyConstructor() {
        Class handlerClazz = ContentHandler.class;
        Class proxyClass = Proxy.getProxyClass(
                handlerClazz.getClassLoader(), new Class[] {handlerClazz});
        
        Class[] constrParamType = new Class[] {InvocationHandler.class};
        try {
            return proxyClass.getConstructor(constrParamType);
        } catch (SecurityException e) {
            throw new XmlBeansEntityXmlizerException(
                    "could not get constructor method from dynamic proxy", e);
        } catch (NoSuchMethodException e) {
            throw new XmlBeansEntityXmlizerException(
                    "could not get constructor method from dynamic proxy", e);
        }
    }
}
