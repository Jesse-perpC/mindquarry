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

/**
 * @author <a hef="mailto:alexander(dot)saar(at)mindquarry(dot)com</a>
 */
public class ConversationMailet extends GenericMailet {
	private static final String LOGIN = "alexander.saar";

	private static final String PWD = "mypwd";

	private static final String WORKSPACE = "default";

	private static final String REMOTE_REPO_NAME = "jackrabbit";

	/**
	 * @see org.apache.mailet.GenericMailet#service(org.apache.mailet.Mail)
	 */
	@Override
	public void service(Mail mail) throws MessagingException {
		System.out.println("Start processing of received mail...");

		Session session = null;
		try {
			ClientRepositoryFactory factory = new ClientRepositoryFactory();
			Repository repo = factory.getRepository("rmi://localhost:1100/"
					+ REMOTE_REPO_NAME);
			session = repo.login(
					new SimpleCredentials(LOGIN, PWD.toCharArray()), WORKSPACE);

			// Node root = session.getRootNode();
			attachFooter(mail);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			session.logout();
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
		if (!content.endsWith("\n")) {
			content += "\r\n";
		}
		content += "Track this conversation at ...";
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
		String footer = "<br/>" + "Track this conversation at ...";
		content = index == -1 ? content + footer : content.substring(0, index)
				+ footer + content.substring(index);

		part.setContent(content, part.getContentType());
	}
}
