/*
 * Copyright (C) 2006, Mindquarry GmbH 
 */
package com.mindquarry.conversation.mailets;

import java.io.IOException;
import java.rmi.NotBoundException;
import java.util.ArrayList;
import java.util.Collection;

import javax.jcr.AccessDeniedException;
import javax.jcr.InvalidItemStateException;
import javax.jcr.ItemExistsException;
import javax.jcr.ItemNotFoundException;
import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.PathNotFoundException;
import javax.jcr.RepositoryException;
import javax.jcr.ValueFormatException;
import javax.jcr.lock.LockException;
import javax.jcr.nodetype.ConstraintViolationException;
import javax.jcr.nodetype.NoSuchNodeTypeException;
import javax.jcr.query.Query;
import javax.jcr.query.QueryManager;
import javax.jcr.query.QueryResult;
import javax.jcr.version.VersionException;
import javax.mail.MessagingException;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimePart;
import javax.mail.internet.ParseException;

import org.apache.mailet.Mail;
import org.apache.mailet.MailAddress;

/**
 * @author <a hef="mailto:alexander(dot)saar(at)mindquarry(dot)com</a>
 */
public class ConversationManager extends AbstractConversationMailet {
	/**
	 * @see org.apache.mailet.GenericMailet#init()
	 */
	@Override
	public void init() throws MessagingException {
		super.init();
	}

	/**
	 * @see org.apache.mailet.GenericMailet#destroy()
	 */
	@Override
	public void destroy() {
		super.destroy();
	}

	/**
	 * @see org.apache.mailet.GenericMailet#service(org.apache.mailet.Mail)
	 */
	@Override
	public void service(Mail mail) throws MessagingException {
		try {
			processContribution(mail);
		} catch (Exception e) {
			throw (new MessagingException(
					"Error while processing contribution.", e));
		}
	}

	private void processContribution(Mail mail) throws RepositoryException,
			MessagingException, IOException, ClassCastException,
			NotBoundException {
		Collection recipients = mail.getRecipients();
		for (Object object : recipients) {
			MailAddress recipient = (MailAddress) object;
			String conversationID = recipient.getUser();

			// query the repository for a conversation with the given identifier
			QueryManager qm = getSession().getWorkspace().getQueryManager();
			Query query = qm.createQuery(
					"//projects/project/talk/conversation[@id='"
							+ conversationID + "']", Query.XPATH);
			QueryResult qResult = query.execute();

			// add contribution to the conversation, if one was found
			NodeIterator nit = qResult.getNodes();
			if (nit.hasNext()) {
				Collection<MailAddress> subscriber = processConversationContribution(
						mail.getMessage(), conversationID, (Node) nit.next());

				// replace ReplyTo header
				InternetAddress replyTo = new InternetAddress(recipient
						.toString());
				mail.getMessage().setReplyTo(new InternetAddress[] { replyTo });

				// set footer
				attachFooter(mail, "Track this conversation at "
						+ recipient.toString() + ".");

				// send mail to subscribers
				getMailetContext().sendMail(mail.getSender(), subscriber,
						mail.getMessage());

				// we send the mail, so further processing is not necessary
				mail.setState(Mail.GHOST);
			} else {
				log("No conversation with the given name was found.");
			}
		}
	}

	private Collection<MailAddress> processConversationContribution(
			MimePart part, String conversationID, Node conversationNode)
			throws MessagingException, IOException, ItemExistsException,
			PathNotFoundException, VersionException,
			ConstraintViolationException, LockException, RepositoryException,
			ValueFormatException, AccessDeniedException,
			InvalidItemStateException, NoSuchNodeTypeException,
			AddressException, ItemNotFoundException, ParseException {
		String content = part.getContent().toString();

		// add content to conversation
		Node contributionNode = conversationNode
				.addNode("contributions/contribution");
		contributionNode.setProperty("content", content);
		getSession().save();

		// retrieve subscribers and add them to the rsult list
		Collection<MailAddress> subscriber = new ArrayList<MailAddress>();
		Node subscribersNode = conversationNode.getNode("subscribers");
		NodeIterator subNit = subscribersNode.getNodes("subscriber");

		while (subNit.hasNext()) {
			Node subscriberNode = (Node) subNit.next();
			String reference = subscriberNode.getProperty("reference")
					.getString();

			Node userNode = getSession().getNodeByUUID(reference);
			String mailAddr = userNode.getProperty("mail").getString();
			subscriber.add(new MailAddress(mailAddr));
		}
		return subscriber;
	}
}
