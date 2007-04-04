package com.mindquarry.persistence.jcr.query;

import com.mindquarry.persistence.api.Configuration;
import com.mindquarry.persistence.jcr.JcrNodeIterator;
import com.mindquarry.persistence.jcr.JcrSession;

public interface QueryResolver {
    
    void initialize(Configuration configuration);
    JcrNodeIterator resolve(
            JcrSession session, String queryName, Object[] queryParameters);
}
