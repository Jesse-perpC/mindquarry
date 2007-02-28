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
package com.mindquarry.jcr.jackrabbit.xpath;

import javax.jcr.NodeIterator;
import javax.jcr.RepositoryException;
import javax.jcr.query.QueryResult;
import javax.jcr.query.RowIterator;

import org.apache.jackrabbit.core.SessionImpl;
import org.apache.jackrabbit.core.query.ExecutableQuery;
import org.jaxen.BaseXPath;
import org.jaxen.JaxenException;

public class JackrabbitXPath extends BaseXPath implements ExecutableQuery, QueryResult {
	
    private static final long serialVersionUID = -4394464481180580466L;
    
    private SessionImpl session;
	
	public JackrabbitXPath(String expression, SessionImpl session) throws JaxenException {
		super(expression, new JackrabbitNavigator(session.getNamespaceResolver()));
		this.session = session;
	}

	public QueryResult execute() throws RepositoryException {
		return this;
	}

	public String[] getColumnNames() throws RepositoryException {
		return null;
	}

	public NodeIterator getNodes() throws RepositoryException {
		try {
			return new JaxenNodeIterator(this.evaluate(this.session.getRootNode()));
		} catch (JaxenException e) {
			throw new RepositoryException(e);
		}
	}

	public RowIterator getRows() throws RepositoryException {
		return null;
	}
}
