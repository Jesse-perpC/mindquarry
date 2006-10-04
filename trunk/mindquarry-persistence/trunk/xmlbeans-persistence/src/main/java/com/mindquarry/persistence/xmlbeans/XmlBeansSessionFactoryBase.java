package com.mindquarry.persistence.xmlbeans;

import org.apache.avalon.framework.activity.Initializable;
import org.apache.avalon.framework.logger.AbstractLogEnabled;

import com.mindquarry.common.persistence.Session;
import com.mindquarry.common.persistence.SessionFactory;
import com.mindquarry.persistence.xmlbeans.config.PersistenceConfiguration;
import com.mindquarry.persistence.xmlbeans.source.JcrSourceResolverBase;

public abstract class XmlBeansSessionFactoryBase extends AbstractLogEnabled 
    implements SessionFactory, Initializable {

    protected PersistenceConfiguration configuration_;
    
    protected JcrSourceResolverBase jcrSourceResolver_;
    

    /**
     * @see org.apache.avalon.framework.activity.Initializable#initialize()
     */
    public void initialize() throws Exception {
        configuration_ = makeConfiguration();
    }

    public abstract Session currentSession();
    
    public abstract PersistenceConfiguration makeConfiguration()
        throws Exception;
}