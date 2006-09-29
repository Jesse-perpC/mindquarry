/**
 * Copyright (C) 2006 MindQuarry GmbH, All Rights Reserved
 */
package com.mindquarry.persistence.xmlbeans;

import java.net.URL;

import com.mindquarry.jcr.jackrabbit.JCRTestBase;

/**
 * Abstract base classes for all JCR XML source test cases.
 * 
 * @author <a href="mailto:alexander(dot)saar(at)mindquarry(dot)com">Alexander
 *         Saar</a>
 */
public abstract class XmlBeansPersistenceTestBase extends JCRTestBase {
	/**
	 * Initializes the ComponentLocator
	 * 
	 * The configuration file is determined by the class name plus .xtest
	 * appended, all '.' replaced by '/' and loaded as a resource via classpath
	 */
	@Override
	protected void prepare() throws Exception {
		String className = XmlBeansPersistenceTestBase.class.getName();
		String xtestResourceName = className.replace('.', '/') + ".xtest";

		URL xtestResource = getClass().getClassLoader().getResource(
				xtestResourceName);
		prepare(xtestResource.openStream());
	}
}
