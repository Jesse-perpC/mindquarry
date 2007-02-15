/*
 * Copyright (C) 2006-2007 Mindquarry GmbH, All Rights Reserved
 *
 * The contents of this file are subject to the Mozilla Public License
 * Version 1.1 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * http://www.mozilla.org/MPL/
 *
 * Software distributed under the License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
 * License for the specific language governing rights and limitations
 * under the License.
 */
package com.mindquarry.jcr.xml.source;

import org.apache.excalibur.source.SourceValidity;

/**
 * @author <a href="mailto:alexander(dot)klimetschek(at)mindquarry(dot)com">
 *         Alexander Klimetschek</a>
 *
 */
public class JCRNodeLastModifiedSourceValidity implements SourceValidity {
    
    private static final long serialVersionUID = -7312896132020153744L;

    private long lastModified;
    
    public JCRNodeLastModifiedSourceValidity(long lastModified) {
        this.lastModified = lastModified;
    }

    /**
     * Always returns SourceValidity.UNKNOWN = 0, since it needs another
     * SourceValidity to check if the lastModified has changed.
     */
    public int isValid() {
        return SourceValidity.UNKNOWN;
    }

    /* (non-Javadoc)
     * @see org.apache.excalibur.source.SourceValidity#isValid(org.apache.excalibur.source.SourceValidity)
     */
    public int isValid(SourceValidity newValidity) {
        if (newValidity instanceof JCRNodeLastModifiedSourceValidity) {
            // compare the two values
            if (((JCRNodeLastModifiedSourceValidity) newValidity).lastModified == this.lastModified) {
                return SourceValidity.VALID;
            } else {
                return SourceValidity.INVALID;
            }
        } else {
            return SourceValidity.INVALID;
        }
    }

}
