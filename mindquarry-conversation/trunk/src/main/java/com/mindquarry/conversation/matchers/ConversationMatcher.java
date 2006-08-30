/*
 * Copyright (C) 2006, Mindquarry GmbH 
 */
package com.mindquarry.conversation.matchers;

import java.util.Collection;

import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.Repository;
import javax.jcr.Session;
import javax.jcr.SimpleCredentials;
import javax.mail.MessagingException;

import org.apache.jackrabbit.rmi.client.ClientRepositoryFactory;
import org.apache.mailet.GenericMatcher;
import org.apache.mailet.Mail;

/**
 * @author <a hef="mailto:alexander(dot)saar(at)mindquarry(dot)com</a>
 */
public class ConversationMatcher extends GenericMatcher {
	private static final String LOGIN = "alexander.saar";

	private static final String PWD = "mypwd";

	private static final String WORKSPACE = "default";

	private static final String REMOTE_REPO_NAME = "jackrabbit";

	/**
	 * @see org.apache.mailet.GenericMatcher#match(org.apache.mailet.Mail)
	 */
	@Override
	public Collection match(Mail mail) throws MessagingException {
		System.out.println("Start processing of received mail...");

		Session session = null;
		try {
			ClientRepositoryFactory factory = new ClientRepositoryFactory();
			Repository repo = factory.getRepository("rmi://localhost:1100/"
					+ REMOTE_REPO_NAME);
			session = repo.login(
					new SimpleCredentials(LOGIN, PWD.toCharArray()), WORKSPACE);

			Node root = session.getRootNode();

			// check if there is already some data in the repository and if so
			// remove it
			NodeIterator nit = root.getNodes();
			while (nit.hasNext()) {
				Node node = (Node) nit.next();
				System.out.println(node.getName());
			}
		} catch (Throwable e) {
			e.printStackTrace();
		} finally {
			session.logout();
		}
		return (mail.getRecipients());
	}
}
