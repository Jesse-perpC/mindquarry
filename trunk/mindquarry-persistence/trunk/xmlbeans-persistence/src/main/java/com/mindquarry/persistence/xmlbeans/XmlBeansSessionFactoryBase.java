package com.mindquarry.persistence.xmlbeans;

import org.apache.avalon.framework.activity.Initializable;
import org.apache.avalon.framework.logger.AbstractLogEnabled;

import com.mindquarry.common.persistence.Session;
import com.mindquarry.common.persistence.SessionFactory;
import com.mindquarry.persistence.xmlbeans.config.PersistenceConfigLoader;
import com.mindquarry.persistence.xmlbeans.config.PersistenceConfiguration;
import com.mindquarry.persistence.xmlbeans.creation.XmlBeansDocumentCreator;
import com.mindquarry.persistence.xmlbeans.creation.XmlBeansEntityCreator;
import com.mindquarry.persistence.xmlbeans.reflection.DocumentReflectionData;
import com.mindquarry.persistence.xmlbeans.reflection.EntityReflectionData;
import com.mindquarry.persistence.xmlbeans.source.JcrSourceResolverBase;

public abstract class XmlBeansSessionFactoryBase extends AbstractLogEnabled 
    implements SessionFactory, Initializable {

    protected PersistenceConfiguration configuration_;
    
    protected XmlBeansDocumentCreator documentCreator_;
    protected XmlBeansEntityCreator entityCreator_;
    
    protected JcrSourceResolverBase jcrSourceResolver_;
    

    /**
     * @see org.apache.avalon.framework.activity.Initializable#initialize()
     */
    public void initialize() throws Exception {
        configuration_ = new PersistenceConfiguration(
                getLogger(), makeConfigLoader());
        documentCreator_ = makeDocumentCreator(configuration_);
        entityCreator_ = makeEntityCreator(configuration_, documentCreator_);
    }
    
    private XmlBeansDocumentCreator makeDocumentCreator(
            PersistenceConfiguration configuration) {
        
        DocumentReflectionData reflectionData = 
            new DocumentReflectionData(configuration_.entityClazzes());
        return new XmlBeansDocumentCreator(reflectionData);
    }
    
    private XmlBeansEntityCreator makeEntityCreator(
            PersistenceConfiguration configuration, 
            XmlBeansDocumentCreator documentCreator) {
        
        EntityReflectionData reflectionData = 
            new EntityReflectionData(configuration_.entityClazzes());
        return new XmlBeansEntityCreator(reflectionData, documentCreator);
    }

    public abstract Session currentSession();
    
    protected abstract PersistenceConfigLoader makeConfigLoader();
}