/**
 * Copyright (C) 2006 Mindquarry GmbH, All Rights Reserved
 */
package com.mindquarry.persistence.xmlbeans.xml;

/**
 * Add summary documentation here.
 *
 * @author
 * <a href="mailto:bastian.steinert(at)mindquarry.com">Bastian Steinert</a>
 */
class XmlBeansEntityXmlizerException extends RuntimeException {

    private static final long serialVersionUID = 1734787949797718546L;

    /**
     * @param arg0
     */
    public XmlBeansEntityXmlizerException(String arg0) {
        super(arg0);
    }

    /**
     * @param arg0
     */
    public XmlBeansEntityXmlizerException(Throwable arg0) {
        super(arg0);
    }

    /**
     * @param arg0
     * @param arg1
     */
    public XmlBeansEntityXmlizerException(String arg0, Throwable arg1) {
        super(arg0, arg1);
    }

}
