/**
 * Copyright (C) 2006 MindQuarry GmbH, All Rights Reserved
 */
package com.mindquarry.jcr.xml.source;

import javax.jcr.Value;

import org.apache.excalibur.source.SourceValidity;

/**
 * Validity of a {@link JCRNodeSource}. It's a wrapper around a JCR
 * <code>Value</code>.
 *
 * @author <a
 *         href="mailto:alexander(dot)klimetschek(at)mindquarry(dot)com">Alexander
 *         Klimetschek</a>
 * @author <a href="mailto:alexander(dot)saar(at)mindquarry(dot)com">Alexander
 *         Saar</a>
 */
public class JCRNodeSourceValidity implements SourceValidity {
    /**
     * Generated serial version UID.
     */
    private static final long serialVersionUID = 6847020671450640424L;
    
    /**
     * The value object.
     */
    private Value value;

    /**
     * Default constructor.
     * 
     * @param value the value
     */
    public JCRNodeSourceValidity(Value value) {
        this.value = value;
    }

    /**
     * {@inheritDoc}
     * @see org.apache.excalibur.source.SourceValidity#isValid()
     */
    public int isValid() {
        // Don't know, need another validity to compare with
        return 0;
    }

    /**
     * {@inheritDoc}
     * @see org.apache.excalibur.source.SourceValidity#isValid(org.apache.excalibur.source.SourceValidity)
     */
    public int isValid(SourceValidity other) {
        if (other instanceof JCRNodeSourceValidity) {
            // compare the two values
            return ((JCRNodeSourceValidity) other).value.equals(this.value) ? 1 : -1;
        } else {
            // invalid
            return -1;
        }
    }
}
