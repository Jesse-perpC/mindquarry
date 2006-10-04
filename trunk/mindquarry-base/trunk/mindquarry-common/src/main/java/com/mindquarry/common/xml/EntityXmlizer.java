/**
 * Copyright (C) 2006 Mindquarry GmbH, All Rights Reserved
 */
package com.mindquarry.common.xml;

import org.xml.sax.ContentHandler;

/**
 * Add summary documentation here.
 *
 * @author
 * <a href="mailto:bastian.steinert(at)mindquarry.com">Bastian Steinert</a>
 */
public interface EntityXmlizer {

    void beforeEndEntityElement(EntityXmlizer childEntityXmlizer);
    
    void toSax(ContentHandler contentHandler);
}
