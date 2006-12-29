package com.mindquarry.persistence.castor;

import java.net.URL;

import org.apache.avalon.framework.activity.Initializable;
import org.apache.avalon.framework.logger.AbstractLogEnabled;
import org.exolab.castor.mapping.Mapping;

import com.mindquarry.common.init.InitializationException;
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
            if (url != null)
                mapping_.loadMapping(url);
            else
                throw new InitializationException("could not load " +
                        "castor-mapping file for entity class: " +
                        entityClazz.getName() + "." +
                        "The file is expected to find within " +
                        "the classpath at: " + 
                        entityClazz.getPackage().getName() + ".");
        }
    }

    public abstract Session currentSession();
    
    public abstract PersistenceConfiguration makeConfiguration()
        throws Exception;
}