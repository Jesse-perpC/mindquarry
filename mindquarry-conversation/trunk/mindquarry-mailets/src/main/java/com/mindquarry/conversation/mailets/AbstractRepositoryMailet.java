/**
 * Copyright (C) 2006 Mindquarry GmbH, All Rights Reserved
 */
package com.mindquarry.conversation.mailets;

import java.io.IOException;

import javax.jcr.Repository;
import javax.jcr.Session;
import javax.jcr.SimpleCredentials;
import javax.mail.MessagingException;
import javax.mail.internet.MimePart;

import org.apache.jackrabbit.rmi.client.ClientRepositoryFactory;
import org.apache.mailet.GenericMailet;
import org.apache.mailet.Mail;
import org.apache.mailet.MailetException;

/**
 * Abstract base class for all mailets that want ot work with an JCR repository.
 * 
 * @author <a hef="mailto:alexander(dot)saar(at)mindquarry(dot)com</a>
 */
public abstract class AbstractRepositoryMailet extends GenericMailet {
	private String login;

	private String password;

	private String workspace;

	private String repository;

	private Session session;
	
	/**
	 * @see org.apache.mailet.GenericMailet#init()
	 */
	@Override
	public void init() throws MessagingException {
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
		
		try {
			// get connection to JCR repository
			ClientRepositoryFactory factory = new ClientRepositoryFactory();
			Repository repo = factory.getRepository(repository);
			session = repo.login(new SimpleCredentials(login, password
					.toCharArray()), workspace);
		} catch (Exception e) {
			throw (new MessagingException("Cannot connect to repository.", e));
		}
	}
	
	/**
	 * @see org.apache.mailet.GenericMailet#destroy()
	 */
	@Override
	public void destroy() {
		session.logout();
	}

	protected void attachFooter(Mail mail, String text) throws MessagingException,
			IOException {
		MimePart part = mail.getMessage();
		if (part.isMimeType("text/plain")) {
			addToText(part, text);
		} else if (part.isMimeType("text/html")) {
			addToHTML(part, text);
		}
	}

	private void addToText(MimePart part, String text) throws IOException,
			MessagingException {
		String content = part.getContent().toString();
		content += "\r\n\r\n" + text;
		part.setText(content);
	}

	private void addToHTML(MimePart part, String text) throws MessagingException,
			IOException {
		String content = part.getContent().toString();

		/*
		 * This HTML part may have a closing <BODY> tag. If so, we want to
		 * insert out footer immediately prior to that tag.
		 */
		int index = content.lastIndexOf("</body>");
		if (index == -1)
			index = content.lastIndexOf("</body>");

		String footer = "<br/><br/>" + text;
		content = index == -1 ? content + footer : content.substring(0, index)
				+ footer + content.substring(index);
		part.setContent(content, part.getContentType());
	}

	/**
	 * Getter for the JCR session.
	 *
	 * @return the session
	 */
	protected Session getSession() {
		return session;
	}
}
