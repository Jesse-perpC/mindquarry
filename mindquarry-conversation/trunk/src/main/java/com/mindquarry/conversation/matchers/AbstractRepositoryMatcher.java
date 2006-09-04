/*
 * Copyright (C) 2006, Mindquarry GmbH 
 */
package com.mindquarry.conversation.matchers;

import java.util.Collection;

import javax.jcr.Repository;
import javax.jcr.Session;
import javax.jcr.SimpleCredentials;
import javax.mail.MessagingException;

import org.apache.jackrabbit.rmi.client.ClientRepositoryFactory;
import org.apache.mailet.GenericMatcher;
import org.apache.mailet.Mail;

/**
 * Abstract matcher that acts as base class for all matchers working with a
 * JCR/RMI interface. It parses the connection string given as condition in the
 * form of
 * 
 * <pre>
 *  rmi://name:pwd@host:port/repository/workspace
 * </pre>
 * 
 * and initializes the properties that are necessary for connecting to the
 * repository.
 * 
 * An example condition for a local jackrabbit installation with RMI interface
 * might look like:
 * 
 * <pre>
 *  rmi://anonymous:myPWD@localhost:1100/jackrabbit/default
 * </pre>
 * 
 * @author <a hef="mailto:alexander(dot)saar(at)mindquarry(dot)com</a>
 */
public abstract class AbstractRepositoryMatcher extends GenericMatcher {
	public static final String LOGIN = "login";
	
	public static final String PASSWORD = "password";
	
	public static final String REPOSITORY = "repository";
	
	public static final String WORKSPACE = "workspace";
	
	private String login;

	private String password;

	private String workspace;

	private String repository;

	private Session session;

	/**
	 * @see org.apache.mailet.GenericMatcher#init()
	 */
	@Override
	public void init() throws MessagingException {
		String condition = getCondition();

		String[] parts = condition.split("//");
		repository = parts[0] + "//";

		parts = parts[1].split("@");
		String account = parts[0];
		login = account.split(":")[0];
		password = account.split(":")[1];

		repository += parts[1].substring(0, parts[1].lastIndexOf("/"));
		workspace = parts[1].substring(parts[1].lastIndexOf("/") + 1, parts[1]
				.length());

		try {
			// get connection to JCR repository
			ClientRepositoryFactory factory = new ClientRepositoryFactory();
			Repository repo = factory.getRepository(getRepository());
			session = repo.login(new SimpleCredentials(getLogin(),
					getPassword().toCharArray()), getWorkspace());
		} catch (Exception e) {
			throw (new MessagingException("Cannot connect to repository.", e));
		}
	}

	/**
	 * @see org.apache.mailet.GenericMatcher#destroy()
	 */
	@Override
	public void destroy() {
		session.logout();
	}

	/* (non-Javadoc)
	 * @see org.apache.mailet.GenericMatcher#match(org.apache.mailet.Mail)
	 */
	@Override
	public Collection match(Mail arg0) throws MessagingException {
		// TODO Auto-generated method stub
		return null;
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
	 * @param login
	 *            the login to set
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
	 * @param password
	 *            the password to set
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
	 * @param repository
	 *            the repository to set
	 */
	public void setRepository(String repository) {
		this.repository = repository;
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
	 * @param workspace
	 *            the workspace to set
	 */
	public void setWorkspace(String workspace) {
		this.workspace = workspace;
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
}
