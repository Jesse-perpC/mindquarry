/*
 * Copyright (C) 2006, Mindquarry GmbH 
 */
package com.mindquarry.conversation.mailets;

import java.io.IOException;
import java.rmi.NotBoundException;
import java.util.ArrayList;
import java.util.Collection;

import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.Repository;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.SimpleCredentials;
import javax.jcr.query.Query;
import javax.jcr.query.QueryManager;
import javax.jcr.query.QueryResult;
import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimePart;

import org.apache.jackrabbit.rmi.client.ClientRepositoryFactory;
import org.apache.mailet.Mail;
import org.apache.mailet.MailAddress;

import com.mindquarry.conversation.matchers.AbstractRepositoryMatcher;

/**
 * @author <a hef="mailto:alexander(dot)saar(at)mindquarry(dot)com</a>
 */
public class ConversationMailet extends AbstractConversationMailet {
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
			String recipientName = recipient.getUser();

			log("Processing contribution for conversation '" + recipientName
					+ "'...");

			// FIXME dont know why we cant pas the session object via mail
			// context to the mailet. this results to a ClassCastException
			ClientRepositoryFactory factory = new ClientRepositoryFactory();
			Repository repo = factory.getRepository(getMailetContext()
					.getAttribute(AbstractRepositoryMatcher.REPOSITORY)
					.toString());
			Session session = repo.login(new SimpleCredentials(
					getMailetContext().getAttribute(
							AbstractRepositoryMatcher.LOGIN).toString(),
					getMailetContext().getAttribute(
							AbstractRepositoryMatcher.PASSWORD).toString()
							.toCharArray()), getMailetContext().getAttribute(
					AbstractRepositoryMatcher.WORKSPACE).toString());

			// query the repository for a conversation with the given identifier
			QueryManager qm = session.getWorkspace().getQueryManager();
			Query query = qm.createQuery(
					"//projects/project/talk/conversation[@id='"
							+ recipientName + "']", Query.XPATH);
			QueryResult qResult = query.execute();

			// add contribution t the conversation
			NodeIterator nit = qResult.getNodes();
			if (nit.hasNext()) {
				// get mail content
				MimePart part = mail.getMessage();
				String content = part.getContent().toString();

				// add content to conversation
				Node conversationNode = (Node) nit.next();
				Node contributionNode = conversationNode
						.addNode("contribution");
				contributionNode.setProperty("content", content);
				session.save();

				// replace ReplyTo header
				InternetAddress[] replyTo = (InternetAddress[]) mail
						.getMessage().getReplyTo();
				replyTo[0].setAddress(recipientName + "@localhost");

				// retrieve subscribers and forward mail
				Collection<MailAddress> subscriber = new ArrayList<MailAddress>();
				NodeIterator subNit = conversationNode.getNodes("subscriber");
				while (subNit.hasNext()) {
					Node subscriberNode = (Node) subNit.next();
					String reference = subscriberNode.getProperty("reference")
							.getString();

					Node userNode = session.getNodeByUUID(reference);
					String mailAddr = userNode.getProperty("mail").getString();
					subscriber.add(new MailAddress(mailAddr));
				}
				getMailetContext().sendMail(mail.getSender(), subscriber,
						mail.getMessage());
			} else {
				log("Error: Conversation " + recipientName + "not found.");
				getMailetContext().bounce(
						mail,
						"Error while processing contribution for conversation '"
								+ recipientName + "'.");
			}
			session.logout();
		}
	}
}
