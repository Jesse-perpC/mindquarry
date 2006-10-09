package com.mindquarry.jcr.jackrabbit.xpath;

import java.io.IOException;

import javax.jcr.RepositoryException;
import javax.jcr.query.InvalidQueryException;

import org.apache.jackrabbit.core.ItemManager;
import org.apache.jackrabbit.core.NodeId;
import org.apache.jackrabbit.core.SessionImpl;
import org.apache.jackrabbit.core.query.AbstractQueryHandler;
import org.apache.jackrabbit.core.query.ExecutableQuery;
import org.apache.jackrabbit.core.state.NodeState;
import org.jaxen.JaxenException;

public class JaxenQueryHandler extends AbstractQueryHandler {
	
	
	protected void doInit() throws IOException {
		//no initialization neccessary
	}

	public void addNode(NodeState node) throws RepositoryException, IOException {
		throw new UnsupportedOperationException("addNode");
	}

	public void close() throws IOException {
		//no finishing cleanup neccessary
	}

	public ExecutableQuery createExecutableQuery(SessionImpl session,
			ItemManager itemMgr, String statement, String language)
			throws InvalidQueryException {
		try {
			return new JackrabbitXPath(statement, session);
		} catch (JaxenException je) {
			throw new InvalidQueryException(je);
		}
	}

	public void deleteNode(NodeId id) throws IOException {
		throw new UnsupportedOperationException("deleteNode");
	}

}
