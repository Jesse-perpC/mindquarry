/*
 * Copyright (C) 2006, Mindquarry GmbH 
 */
package com.mindquarry.conversation.matchers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.Repository;
import javax.jcr.Session;
import javax.jcr.SimpleCredentials;
import javax.mail.MessagingException;

import org.apache.jackrabbit.core.TransientRepository;
import org.apache.mailet.GenericMatcher;
import org.apache.mailet.Mail;

/**
 * @author <a hef="mailto:alexander(dot)saar(at)mindquarry(dot)com</a>
 */
public class ConversationMatcher extends GenericMatcher {
	private static List<String> projects;

	private static List<String> tags;

	private static List<String> users;

	static {
		projects = new ArrayList<String>();
		projects.add("Mindquarry");
		projects.add("Cyclr");

		tags = new ArrayList<String>();
		tags.add("dev");
		tags.add("support");

		users = new ArrayList<String>();
		users.add("test");
	}

	private static final String LOGIN = "alexander.saar";

	private static final String PWD = "mypwd";

	private static final String WORKSPACE = "default";

	/**
	 * @see org.apache.mailet.GenericMatcher#match(org.apache.mailet.Mail)
	 */
	@Override
	public Collection match(Mail mail) throws MessagingException {
		System.out.println("Start processing of received mail...");
		
		Repository repo = null;
		Session session = null;
		try {
			
			session = repo.login(new SimpleCredentials(LOGIN, PWD.toCharArray()),
					WORKSPACE);
			
			Node root = session.getRootNode();
			
			// check if there is already some data in the repository and if so
			// remove it
			NodeIterator nit = root.getNodes();
			while(nit.hasNext()) {
				Node node = (Node)nit.next();
				
				// remove only users and projects nodes
				if((node.getName().equals("users")) || 
						(node.getName().equals("projects"))) {
					node.remove();
				}
			}
			// store user data
			Node membersNode = root.addNode("users");
			for (String user : users) {
				Node node = membersNode.addNode("user");
				node.setProperty("name", user);
			}
			// store project data
			Node projectsNode = root.addNode("projects");
			for (String project : projects) {
				Node node = projectsNode.addNode("project");
				node.setProperty("name", project);
				
				// store tag data
				Node tagsNode = root.addNode("tags");
				for (String tag : tags) {
					node = tagsNode.addNode("project");
					node.setProperty("name", tag);
				}
			}
			session.save();
		} catch (Throwable e) {
			e.printStackTrace();
		} finally {
			session.logout();
		}
		return (mail.getRecipients());
	}
}
