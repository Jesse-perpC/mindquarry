/*
 * Copyright 2006 Mindquarry GmbH
 */
package com.mindquarry.jcr.source.xml.sources;

import javax.jcr.Value;

import org.apache.excalibur.source.SourceValidity;

/**
 * Validity of a {@link JCRNodeSource}. It's a wrapper around a JCR
 * <code>Value</code>.
 *
 * @author alexander.klimetschek@mindquarry.com
 */
public class JCRNodeSourceValidity implements SourceValidity {

    private static final long serialVersionUID = 1L;

    private Value value;

    public JCRNodeSourceValidity(Value value) {
        this.value = value;
    }

    public int isValid() {
        // Don't know, need another validity to compare with
        return 0;
    }

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
