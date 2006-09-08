/*
 * Copyright (C) 2006, Mindquarry GmbH 
 */
package com.mindquarry.conversation.mailets;

import java.io.IOException;
import java.rmi.NotBoundException;
import java.util.ArrayList;
import java.util.Collection;

import javax.jcr.ItemExistsException;
import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.PathNotFoundException;
import javax.jcr.RepositoryException;
import javax.jcr.lock.LockException;
import javax.jcr.nodetype.ConstraintViolationException;
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
			processMail(mail);
		} catch (Exception e) {
			throw (new MessagingException(
					"Error while processing contribution.", e));
		}
	}

	private void processMail(Mail mail) throws RepositoryException,
			MessagingException, IOException, ClassCastException,
			NotBoundException {
		Collection recipients = mail.getRecipients();
		for (Object object : recipients) {
			MailAddress address = (MailAddress) object;
			String id = address.getUser();

			// check if the mail is a contribution to an existing conversation
			Node conversationNode = checkConversationExists(id);
			if (conversationNode != null) {
				handleContribution(mail, address, conversationNode, id);
			} else {
				// this mail is no contribution to an existing conversation or
				// the conversatio does not exist. check if the pattern is
				// correct or if we must create a new conversation
				String[] parts = id.split("-");
				if (parts.length == 2) {
					Node tagNode = checkTagExistsInProject(parts[0], parts[1]);
					if(tagNode != null) {
						
					}
				} else {
					getMailetContext().bounce(mail,
							"Unsupported conversation address pattern.");
				}
			}
		}
	}

	private Node checkTagExistsInProject(String projectName, String tagName)
			throws RepositoryException {
		// query the repository for a conversation with the given identifier
		QueryManager qm = getSession().getWorkspace().getQueryManager();
		Query query = qm.createQuery("//projects/project[@name='" + projectName
				+ "']/tags/tag[@name='" + tagName + "']", Query.XPATH);
		QueryResult qResult = query.execute();

		// add contribution to the conversation, if one was found
		NodeIterator nit = qResult.getNodes();
		if (nit.hasNext()) {
			return (Node) nit.next();
		} else {
			return null;
		}
	}

	private Node checkConversationExists(String conversationID)
			throws RepositoryException {
		// query the repository for a conversation with the given identifier
		QueryManager qm = getSession().getWorkspace().getQueryManager();
		Query query = qm.createQuery(
				"//projects/project/talk/conversation[@id='" + conversationID
						+ "']", Query.XPATH);
		QueryResult qResult = query.execute();

		// add contribution to the conversation, if one was found
		NodeIterator nit = qResult.getNodes();
		if (nit.hasNext()) {
			return (Node) nit.next();
		} else {
			return null;
		}
	}

	private void handleContribution(Mail mail, MailAddress address,
			Node conversationNode, String recipient)
			throws RepositoryException, IOException, MessagingException,
			ItemExistsException, PathNotFoundException, VersionException,
			ConstraintViolationException, LockException, ParseException,
			AddressException {
		addContributionToConversation(mail.getMessage(), conversationNode);

		Collection<MailAddress> subscriber = getSubscriber(conversationNode);

		// replace ReplyTo header
		InternetAddress replyTo = new InternetAddress(address.toString());
		mail.getMessage().setReplyTo(new InternetAddress[] { replyTo });

		// set footer
		attachFooter(mail, "Track this conversation at " + address.toString()
				+ ".");

		// send mail to subscribers
		getMailetContext().sendMail(mail.getSender(), subscriber,
				mail.getMessage());

		// we send the mail, so further processing is not necessary
		mail.setState(Mail.GHOST);
	}

	private void addContributionToConversation(MimePart part,
			Node conversationNode) throws IOException, MessagingException,
			ItemExistsException, PathNotFoundException, VersionException,
			ConstraintViolationException, LockException, RepositoryException {
		String content = part.getContent().toString();

		// add content to conversation
		Node contributionNode = conversationNode
				.addNode("contributions/contribution");
		contributionNode.setProperty("content", content);
		getSession().save();
	}

	private Collection<MailAddress> getSubscriber(Node conversationNode)
			throws PathNotFoundException, RepositoryException, ParseException {
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
