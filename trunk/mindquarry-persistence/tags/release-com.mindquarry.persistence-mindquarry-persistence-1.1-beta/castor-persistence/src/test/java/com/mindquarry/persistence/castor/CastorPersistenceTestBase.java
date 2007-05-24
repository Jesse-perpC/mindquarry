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
package com.mindquarry.persistence.castor;

import java.net.URL;
import java.util.List;

import com.mindquarry.jcr.jackrabbit.JCRTestBase;

/**
 * Abstract base classes for all JCR XML source test cases.
 * 
 * @author <a href="mailto:alexander(dot)saar(at)mindquarry(dot)com">Alexander
 *         Saar</a>
 */
public abstract class CastorPersistenceTestBase extends JCRTestBase {
	
	protected List<String> springConfigClasspathResources() {
        List<String> result = super.springConfigClasspathResources();
        result.add("META-INF/cocoon/spring/castor-persistence-test-context.xml");
        return result;
    }
    
	/**
	 * Initializes the ComponentLocator
	 * 
	 * The configuration file is determined by the class name plus .xtest
	 * appended, all '.' replaced by '/' and loaded as a resource via classpath
	 */
	@Override
	protected void prepare() throws Exception {
		String className = CastorPersistenceTestBase.class.getName();
		String xtestResourceName = className.replace('.', '/') + ".xtest";

		URL xtestResource = getClass().getClassLoader().getResource(
				xtestResourceName);
		prepare(xtestResource.openStream());
	}
}
