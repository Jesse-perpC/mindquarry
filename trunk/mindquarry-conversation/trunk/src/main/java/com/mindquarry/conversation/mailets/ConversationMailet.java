/*
 * Copyright (C) 2006, Mindquarry GmbH 
 */
package com.mindquarry.conversation.mailets;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.PathNotFoundException;
import javax.jcr.Repository;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.SimpleCredentials;
import javax.jcr.query.Query;
import javax.jcr.query.QueryManager;
import javax.jcr.query.QueryResult;
import javax.mail.MessagingException;
import javax.mail.internet.MimePart;

import org.apache.jackrabbit.rmi.client.ClientRepositoryFactory;
import org.apache.mailet.GenericMailet;
import org.apache.mailet.Mail;
import org.apache.mailet.MailAddress;
import org.apache.mailet.MailetException;

/**
 * @author <a hef="mailto:alexander(dot)saar(at)mindquarry(dot)com</a>
 */
public class ConversationMailet extends GenericMailet {
	private String login;

	private String password;

	private String workspace;

	private String repository;

	private Session session;

	/**
	 * Initialize the conversation mailet.
	 * 
	 * @throws MailetException
	 *             Thrown if a required parameter is missing.
	 */
	@Override
	public void init() throws MailetException {
		login = getInitParameter("login");
		if (login == null) {
			throw new MailetException("login parameter is required");
		}
		password = getInitParameter("password");
		if (password == null) {
			throw new MailetException("password parameter is required");
		}
		workspace = getInitParameter("workspace");
		if (workspace == null) {
			throw new MailetException("workspace parameter is required");
		}
		repository = getInitParameter("repository");
		if (repository == null) {
			throw new MailetException("repository parameter is required");
		}
	}

	/**
	 * @see org.apache.mailet.GenericMailet#service(org.apache.mailet.Mail)
	 */
	@Override
	public void service(Mail mail) throws MessagingException {
		System.out.println("Start processing of received mail...");

		try {
			// get connection to JCR repository
			ClientRepositoryFactory factory = new ClientRepositoryFactory();
			Repository repo = factory.getRepository(repository);
			session = repo.login(new SimpleCredentials(login, password
					.toCharArray()), workspace);

			// check if the specified project(s) exists
			List<String> projects = getProjects(mail);
			if (projects.size() == 0) {
				mail.setState(Mail.GHOST);
				return;
			}
			// process the mail for each project
			for (String project : projects) {
				processMail(project, mail);
			}
			attachFooter(mail);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			session.logout();
		}
	}

	private List<String> getProjects(Mail mail) throws RepositoryException,
			MessagingException {
		Collection recipients = mail.getRecipients();
		List<String> projectNames = new ArrayList<String>();
		for (Object object : recipients) {
			MailAddress recipient = (MailAddress) object;
			String recipientName = recipient.getUser();

			log("Checking recipient " + recipientName + "...");
			String projectName = recipientName.split("-")[0];

			// TODO use query manager instead of direct access for searching
			// projects

			// QueryManager qm = session.getWorkspace().getQueryManager();
			// Query query = qm.createQuery("", Query.XPATH);
			// QueryResult result = query.execute();

			// check if the given project exists
			boolean found = false;
			Node projectsNode = session.getRootNode().getNode("projects");
			NodeIterator nit = projectsNode.getNodes();
			while (nit.hasNext()) {
				Node projectNode = (Node) nit.next();
				if (projectNode.getProperty("name").equals(projectName)) {
					projectNames.add(projectNode.getName());
					break;
				}
			}
			// if a project was not found, inform the sender
			if (!found) {
				getMailetContext().bounce(mail,
						"Project '" + projectName + "' does not exist.");
			}
		}
		return (projectNames);
	}

	private void processMail(String projectName, Mail mail)
			throws PathNotFoundException, RepositoryException {
		Collection allRecipients = mail.getRecipients();

		// get recipients for the given project
		List<String> recipients = new ArrayList<String>();
		for (Object object : recipients) {
			MailAddress recipient = (MailAddress) object;
			String recipientName = recipient.getUser();
			String tmpProjectName = recipientName.split("-")[0];
			if (tmpProjectName.equals(projectName)) {
				recipients.add(recipientName);
			}
		}
		// processing project tags
		for (String recipient : recipients) {
			String tagName = recipient.split("-")[1];

			Node tagsNode = session.getRootNode().getNode(
					"projects/" + projectName + "/tags");

			// check if the tag already exist
			NodeIterator nit = tagsNode.getNodes();
			while (nit.hasNext()) {
				Node tagNode = (Node) nit.next();
				if (tagNode.getProperty("name").equals(tagName)) {
					
				}
			}
		}
	}

	private void attachFooter(Mail mail) throws MessagingException, IOException {
		MimePart part = mail.getMessage();
		if (part.isMimeType("text/plain")) {
			addToText(part);
		} else if (part.isMimeType("text/html")) {
			addToHTML(part);
		}
	}

	private void addToText(MimePart part) throws IOException,
			MessagingException {
		String content = part.getContent().toString();
		content += "\r\n\r\n" + getFooterText();
		part.setText(content);
	}

	private void addToHTML(MimePart part) throws MessagingException,
			IOException {
		String content = part.getContent().toString();

		/*
		 * This HTML part may have a closing <BODY> tag. If so, we want to
		 * insert out footer immediately prior to that tag.
		 */
		int index = content.lastIndexOf("</body>");
		if (index == -1)
			index = content.lastIndexOf("</body>");

		String footer = "<br/><br/>" + getFooterText();
		content = index == -1 ? content + footer : content.substring(0, index)
				+ footer + content.substring(index);
		part.setContent(content, part.getContentType());
	}

	private String getFooterText() {
		return "Track this conversation at ...";
	}
}
