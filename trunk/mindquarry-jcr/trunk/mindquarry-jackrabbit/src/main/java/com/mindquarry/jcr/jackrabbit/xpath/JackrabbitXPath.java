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
