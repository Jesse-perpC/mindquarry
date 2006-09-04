/*
 * Copyright (C) 2006, Mindquarry GmbH 
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
 * @author <a hef="mailto:alexander(dot)saar(at)mindquarry(dot)com</a>
 */
public abstract class AbstractConversationMailet extends GenericMailet {
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

	protected void attachFooter(Mail mail) throws MessagingException,
			IOException {
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

	/**
	 * Getter for login.
	 *
	 * @return the login
	 */
	public String getLogin() {
		return login;
	}

	/**
	 * Setter for login.
	 *
	 * @param login the login to set
	 */
	public void setLogin(String login) {
		this.login = login;
	}

	/**
	 * Getter for password.
	 *
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * Setter for password.
	 *
	 * @param password the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * Getter for repository.
	 *
	 * @return the repository
	 */
	public String getRepository() {
		return repository;
	}

	/**
	 * Setter for repository.
	 *
	 * @param repository the repository to set
	 */
	public void setRepository(String repository) {
		this.repository = repository;
	}

	/**
	 * Getter for session.
	 *
	 * @return the session
	 */
	public Session getSession() {
		return session;
	}

	/**
	 * Setter for session.
	 *
	 * @param session the session to set
	 */
	public void setSession(Session session) {
		this.session = session;
	}

	/**
	 * Getter for workspace.
	 *
	 * @return the workspace
	 */
	public String getWorkspace() {
		return workspace;
	}

	/**
	 * Setter for workspace.
	 *
	 * @param workspace the workspace to set
	 */
	public void setWorkspace(String workspace) {
		this.workspace = workspace;
	}
}
