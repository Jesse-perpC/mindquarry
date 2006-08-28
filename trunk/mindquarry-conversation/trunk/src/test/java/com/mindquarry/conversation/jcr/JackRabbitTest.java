package com.mindquarry.conversation.jcr;

import java.io.IOException;

import javax.jcr.LoginException;
import javax.jcr.Repository;
import javax.jcr.RepositoryException;
import javax.jcr.Session;

import junit.framework.TestCase;

import org.apache.jackrabbit.core.TransientRepository;

public class JackRabbitTest extends TestCase {
	public void testConnection() throws IOException, LoginException,
			RepositoryException {
		Repository repository = new TransientRepository();
		Session session = repository.login();
		try {
			String user = session.getUserID();
			String name = repository.getDescriptor(Repository.REP_NAME_DESC);
			System.out.println("Logged in as " + user + " to a " + name
					+ " repository.");
		} finally {
			session.logout();
		}
	}
}
