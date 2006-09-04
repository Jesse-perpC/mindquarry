/*
 * Copyright (C) 2006, Mindquarry GmbH 
 */
package com.mindquarry.conversation.matchers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.jcr.NodeIterator;
import javax.jcr.RepositoryException;
import javax.jcr.query.Query;
import javax.jcr.query.QueryManager;
import javax.jcr.query.QueryResult;
import javax.mail.MessagingException;

import org.apache.mailet.Mail;
import org.apache.mailet.MailAddress;
import org.apache.mailet.MailetContext;

/**
 * @author <a hef="mailto:alexander(dot)saar(at)mindquarry(dot)com</a>
 */
public class ConversationMatcher extends AbstractRepositoryMatcher {
	/**
	 * @see org.apache.mailet.GenericMatcher#init()
	 */
	@Override
	public void init() throws MessagingException {
		super.init();
	}

	/**
	 * @see org.apache.mailet.GenericMatcher#destroy()
	 */
	@Override
	public void destroy() {
		super.destroy();
	}

	/**
	 * @see org.apache.mailet.GenericMatcher#match(org.apache.mailet.Mail)
	 */
	@Override
	public Collection match(Mail mail) throws MessagingException {
		List<MailAddress> recipients = null;
		try {
			recipients = checkConversations(mail);
		} catch (RepositoryException e) {
			getSession().logout();
			throw (new MessagingException("Error while checking projects.", e));
		}

		if (recipients.size() > 0) {
			MailetContext ctx = getMailetContext();
			ctx.setAttribute(AbstractRepositoryMatcher.LOGIN, getLogin());
			ctx.setAttribute(AbstractRepositoryMatcher.PASSWORD, getPassword());
			ctx.setAttribute(AbstractRepositoryMatcher.REPOSITORY,
					getRepository());
			ctx.setAttribute(AbstractRepositoryMatcher.WORKSPACE,
					getWorkspace());
		}
		return (recipients);
	}

	private List<MailAddress> checkConversations(Mail mail)
			throws RepositoryException, MessagingException {
		Collection recipients = mail.getRecipients();

		List<MailAddress> result = new ArrayList<MailAddress>();
		for (Object object : recipients) {
			MailAddress recipient = (MailAddress) object;
			String recipientName = recipient.getUser();

			// check conversation address pattern
			String[] parts = recipientName.split("-");
			if (parts.length != 2) {
				continue;
			} else {
				try {
					Integer.valueOf(parts[1]);
				} catch (NumberFormatException e) {
					continue;
				}
			}
			// query the repository for a conversation with the given identifier
			QueryManager qm = getSession().getWorkspace().getQueryManager();
			Query query = qm.createQuery(
					"//projects/project/talk/conversation[@id='"
							+ recipientName + "']", Query.XPATH);
			QueryResult qResult = query.execute();

			// check if the given conversation exists. if a conversation was not
			// found, inform the sender, otherwise check if the given
			// conversation exist
			NodeIterator nit = qResult.getNodes();
			if (nit.hasNext()) {
				result.add(recipient);
			} else {
				log("Conversation " + recipientName + "does not exist.");
				getMailetContext().bounce(mail,
						"Conversation '" + recipientName + "' does not exist.");
			}
		}
		return (result);
	}
}
