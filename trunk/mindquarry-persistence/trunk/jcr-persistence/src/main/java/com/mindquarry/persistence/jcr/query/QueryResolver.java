package com.mindquarry.persistence.jcr.query;

import com.mindquarry.persistence.jcr.Configuration;
import com.mindquarry.persistence.jcr.api.JcrNodeIterator;
import com.mindquarry.persistence.jcr.api.JcrSession;

public interface QueryResolver {
    
    void initialize(Configuration configuration);
    JcrNodeIterator resolve(
            JcrSession jcrSession, String queryName, Object[] queryParameters);
}
