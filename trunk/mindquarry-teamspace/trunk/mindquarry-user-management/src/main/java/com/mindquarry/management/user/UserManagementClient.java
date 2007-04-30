/*
 * Copyright (C) 2006-2007 Mindquarry GmbH, All Rights Reserved
 * 
 * The contents of this file are subject to the Mozilla Public License
 * Version 1.1 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * http://www.mozilla.org/MPL/
 *
 * Software distributed under the License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
 * License for the specific language governing rights and limitations
 * under the License.
 */
package com.mindquarry.management.user;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

import javax.jcr.LoginException;
import javax.jcr.Node;
import javax.jcr.PathNotFoundException;
import javax.jcr.Property;
import javax.jcr.PropertyIterator;
import javax.jcr.Repository;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.SimpleCredentials;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.jackrabbit.rmi.client.ClientRepositoryFactory;

public class UserManagementClient {
	private static final String ADMIN_LOGIN = "admin"; //$NON-NLS-1$

	private static final String ADMIN_PWD = "admin"; //$NON-NLS-1$

	private static final String O_REPO = "r"; //$NON-NLS-1$

	private static final String O_DEL = "d"; //$NON-NLS-1$

	private static Log log;

	/**
	 * The options object for the command line applications.
	 */
	private static final Options options;

	// static initialization of command line options
	static {
		Option repo = new Option(O_REPO, "repository", true, //$NON-NLS-1$
				"The endpoint where the repository is available."); //$NON-NLS-1$
		repo.setRequired(true);

		Option del = new Option(O_DEL, "delete", false, //$NON-NLS-1$
				"Delete a user."); //$NON-NLS-1$
		del.setOptionalArg(true);

		options = new Options();
		options.addOption(repo);
		options.addOption(del);
	}

	public static void main(String[] args) {
		log = LogFactory.getLog(UserManagementClient.class);
		log.info("Starting user management client..."); //$NON-NLS-1$

		// parse command line arguments
		CommandLine line = null;
		CommandLineParser parser = new GnuParser();
		try {
			// parse the command line arguments
			line = parser.parse(options, args);
		} catch (ParseException e) {
			// oops, something went wrong
			log.error("Parsing of command line failed."); //$NON-NLS-1$
			printUsage();
			return;
		}
		// retrieve login data
		System.out.print("Please enter your login ID: "); //$NON-NLS-1$

		String user = readString();
		user = user.trim();

		System.out.print("Please enter your new password: "); //$NON-NLS-1$

		String password = readString();
		password = password.trim();
		password = new String(DigestUtils.md5(password));
		password = new String(Base64.encodeBase64(password.getBytes()));

		// start PWD change client
		UserManagementClient manager = new UserManagementClient();
		try {
			if (line.hasOption(O_DEL)) {
				manager.deleteUser(line.getOptionValue(O_REPO), ADMIN_LOGIN,
						ADMIN_PWD, user, password);
			} else {
				manager.changePwd(line.getOptionValue(O_REPO), ADMIN_LOGIN,
						ADMIN_PWD, user, password);
			}
		} catch (Exception e) {
			log.error("Error while applying password changes.", e); //$NON-NLS-1$
		}
		log.info("User management client finished successfully."); //$NON-NLS-1$
	}

	private static String readString() {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

		String user = null;
		try {
			user = br.readLine();
		} catch (IOException e) {
			log.error("IO error trying to read from command line!", e); //$NON-NLS-1$
			System.exit(1);
		}
		return user;
	}

	private void deleteUser(String repoLocation, String adminID,
			String adminPwd, String user, String pwd) throws Exception {
		Session session = login(repoLocation, adminID, adminPwd);
		Node root = session.getRootNode();
		Node userNode;
		try {
			userNode = root.getNode("users/" + user); //$NON-NLS-1$

			PropertyIterator allRefs = userNode.getReferences();
			while (allRefs.hasNext()) {
				Property ref = allRefs.nextProperty();
				ref.getParent().remove();
			}
			userNode.remove();
		} catch (PathNotFoundException e) {
			log.error("The requested user was not found."); //$NON-NLS-1$
			System.exit(-1);
			return;
		}
		session.save();
	}

	private void changePwd(String repoLocation, String adminID,
			String adminPwd, String user, String pwd) throws Exception {
		Session session = login(repoLocation, adminID, adminPwd);
		Node root = session.getRootNode();
		Node pwdNode;
		try {
			pwdNode = root.getNode("users/" + user //$NON-NLS-1$
					+ "/jcr:content/password/text"); //$NON-NLS-1$
		} catch (PathNotFoundException e) {
			log.error("The requested user was not found."); //$NON-NLS-1$
			System.exit(-1);
			return;
		}
		pwdNode.setProperty("xt:characters", pwd); //$NON-NLS-1$
		session.save();
		log.info("Password changed."); //$NON-NLS-1$
	}

	private Session login(String repoLocation, String login, String pwd)
			throws MalformedURLException, NotBoundException, RemoteException,
			LoginException, RepositoryException {
		ClientRepositoryFactory factory = new ClientRepositoryFactory();
		Repository repo = factory.getRepository(repoLocation);
		Session session = repo.login(new SimpleCredentials(login, pwd
				.toCharArray()));
		return session;
	}

	/**
	 * Automatically generate and print the help statement.
	 */
	private static void printUsage() {
		HelpFormatter formatter = new HelpFormatter();
		formatter.printHelp("java -jar mindquarry-pwd-change-<version>.jar", //$NON-NLS-1$
				options, true);
	}
}
