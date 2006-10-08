package com.mindquarry.persistence.castor;

import java.net.URL;

import org.apache.avalon.framework.activity.Initializable;
import org.apache.avalon.framework.logger.AbstractLogEnabled;
import org.exolab.castor.mapping.Mapping;

import com.mindquarry.common.persistence.Session;
import com.mindquarry.common.persistence.SessionFactory;
import com.mindquarry.persistence.castor.config.PersistenceConfiguration;
import com.mindquarry.persistence.castor.source.JcrSourceResolverBase;

public abstract class CastorSessionFactoryBase extends AbstractLogEnabled 
    implements SessionFactory, Initializable {

    protected PersistenceConfiguration configuration_;
    
    protected JcrSourceResolverBase jcrSourceResolver_;
    
    protected Mapping mapping_;

    /**
     * @see org.apache.avalon.framework.activity.Initializable#initialize()
     */
    public void initialize() throws Exception {
        configuration_ = makeConfiguration();
        
        mapping_ = new Mapping();
        
        for (Class entityClazz : configuration_.entityClazzes()) {
            URL url = entityClazz.getResource("castor-mapping.xml");
            mapping_.loadMapping(url);
        }
    }

    public abstract Session currentSession();
    
    public abstract PersistenceConfiguration makeConfiguration()
        throws Exception;
}