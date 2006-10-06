/**
 * Copyright (C) 2006 Mindquarry GmbH, All Rights Reserved
 */
package com.mindquarry.common.xml;

import org.apache.excalibur.xml.sax.XMLizable;

/**
 * Add summary documentation here.
 *
 * @author
 * <a href="mailto:bastian.steinert(at)mindquarry.com">Bastian Steinert</a>
 */
public interface EntityXmlizer extends XMLizable {

    void beforeEndEntityElement(XMLizable xmlizableChild);
}
