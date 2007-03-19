package com.mindquarry.persistence.jcr.query;

import com.mindquarry.persistence.jcr.api.JcrNodeIterator;
import com.mindquarry.persistence.jcr.api.JcrSession;

public interface QueryResolver {
    
    void initialize();
    JcrNodeIterator resolve(
            JcrSession jcrSession, String queryName, Object[] queryParameters);
}
