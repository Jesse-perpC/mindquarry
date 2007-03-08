/**
 * Copyright (C) 2006 Mindquarry GmbH, All Rights Reserved
 */
package com.mindquarry.persistence.jcr.session;

import com.mindquarry.common.persistence.Session;
import com.mindquarry.common.persistence.SessionFactory;

/**
 * Add summary documentation here.
 *
 * @author
 * <a href="mailto:bastian.steinert(at)mindquarry.com">Bastian Steinert</a>
 */
class JcrSessionFactory implements SessionFactory {
    
    /**
     * @see com.mindquarry.common.persistence.SessionFactory#currentSession()
     */
    public Session currentSession() {
        return buildJcrSession();
    }
    
    private Session buildJcrSession() {
        JcrSession result = new JcrSession();
        return result;
    }
}
