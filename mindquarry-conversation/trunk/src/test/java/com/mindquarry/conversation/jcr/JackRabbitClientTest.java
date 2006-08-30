package com.mindquarry.conversation.jcr;

import java.io.IOException;
import java.rmi.NotBoundException;

import javax.jcr.LoginException;
import javax.jcr.Node;
import javax.jcr.Repository;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.SimpleCredentials;
import javax.naming.NamingException;

import junit.framework.TestCase;

import org.apache.jackrabbit.rmi.client.ClientRepositoryFactory;

public class JackRabbitClientTest extends TestCase {
	public void testConnection() throws IOException, LoginException,
			RepositoryException, NamingException, ClassCastException,
			NotBoundException {
		ClientRepositoryFactory factory = new ClientRepositoryFactory();
		Repository repo = factory
				.getRepository("rmi://localhost:1100/jackrabbit");

		Session session = repo.login(new SimpleCredentials("test", "test"
				.toCharArray()), "default");
		try {
			String user = session.getUserID();
			String name = repo.getDescriptor(Repository.REP_NAME_DESC);
			System.out.println("Logged in as " + user + " to a " + name
					+ " repository.");

			Node root = session.getRootNode();
			Node newUserNode = root.addNode("user");
			newUserNode.setProperty("name", "Alexander Saar");
			session.save();

			Node userNode = root.getNode("user[1]");
			System.out.println(userNode.getProperty("name").getString());

			userNode.remove();
			session.save();
		} finally {
			session.logout();
		}
	}
}
