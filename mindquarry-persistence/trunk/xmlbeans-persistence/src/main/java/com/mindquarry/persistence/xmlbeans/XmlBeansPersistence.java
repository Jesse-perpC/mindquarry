/**
 * Copyright (C) 2005 MindQuarry GmbH, All Rights Reserved
 */
package com.mindquarry.persistence.xmlbeans;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.xmlbeans.XmlObject;

import com.mindquarry.common.persistence.Persistence;


/**
 * 
 *
 * @author <a href="bastian(dot)steinert(at)mindquarry(dot)com">Bastian Steinert</a>
 */
public class XmlBeansPersistence implements Persistence {

    private XmlBeansDocumentCreator documentCreator_;
    private XmlBeansEntityCreator entityCreator_;
    
    public XmlBeansPersistence() {
        documentCreator_ = new XmlBeansDocumentCreator();
        entityCreator_ = new XmlBeansEntityCreator();
    }
    
    public Object newInstance(Class entityClazz) {
        validateEntityClass(entityClazz);
        XmlObject document = documentCreator_.newDocumentFor(entityClazz);
        return entityCreator_.newEntityFor(entityClazz, document);
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
}
