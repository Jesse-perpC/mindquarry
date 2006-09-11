/*
 * Copyright (C) 2006, Mindquarry GmbH 
 */
package com.mindquarry.conversation.mailets;

import java.io.IOException;
import java.rmi.NotBoundException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.StringTokenizer;

import javax.jcr.ItemExistsException;
import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.PathNotFoundException;
import javax.jcr.RepositoryException;
import javax.jcr.ValueFormatException;
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
	 * @see com.mindquarry.conversation.mailets.AbstractConversationMailet#init()
	 */
	@Override
	public void init() throws MessagingException {
		super.init();
	}

	/**
	 * @see com.mindquarry.conversation.mailets.AbstractConversationMailet#destroy()
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
		Hashtable<String, Collection<Node>> newConversations = 
			new Hashtable<String, Collection<Node>>();

		// loop all recipients and process the mail according to them
		for (Object object : recipients) {
			MailAddress address = (MailAddress) object;

			// check if the recipient pattern is correct, if not continue with
			// the next one
			StringTokenizer tokenizer = new StringTokenizer(address.getUser(),
					"-");
			if (tokenizer.countTokens() != 2) {
				getMailetContext().bounce(mail, 
						"Unsupported conversation address pattern: "
								+ address.toString());
				continue;
			}

			// check if the mail is a contribution to an existing conversation
			Node conversationNode = checkConversationExists(address.getUser());
			if (conversationNode != null) {
				// a conversation that matches the address pattern was found,
				// thus we process the contribution and continue with the next
				// recipient
				handleContribution(mail, address, conversationNode);
				continue;
			}
			// this mail is no contribution to an existing conversation or
			// the conversation does not exist, check if a tag exists that
			// matches the given address pattern
			String projectName = tokenizer.nextToken();
			String tagName = tokenizer.nextToken();
			Node tagNode = checkTagExistsInProject(projectName, tagName);
			if (tagNode != null) {
				Collection<Node> entries = newConversations.get(projectName);
				if(entries == null) {
					entries = new ArrayList<Node>();
				}
				entries.add(tagNode);
				newConversations.put(projectName, entries);
				continue;
			}
			// if we reached this point no processing of the contribution was
			// possible. thus inform the user and continue with the next
			// recipient
			getMailetContext().bounce(mail, "Cannot process contribution!! "
							+ "Please check if the recipient exist.");
		}
		// if there are unprocessed contributions that are assigned to valid 
		// projects and tags, process them now 
		if(!newConversations.isEmpty()) {
			handleNewConversation(newConversations, mail);
		}
		getSession().save();
	}
	
	private void handleNewConversation(Hashtable<String, Collection<Node>> items, 
			Mail mail) throws RepositoryException, IOException, 
			MessagingException {
		Enumeration<String> keys = items.keys();
		while(keys.hasMoreElements()) {
			String projectName = keys.nextElement();
			Collection<Node> tags = items.get(projectName);
			
			// get project node
			Node projectNode = checkProjectExist(projectName);
			if(projectNode == null) {
				continue;
			}
			// create tagged conversation
			Node talkNode = projectNode.getNode("talk");
			
			// get greatest conversation ID
			Node conversationNode = createNewConversation(talkNode, tags, 
					projectName);
			addContributionToConversation(mail.getMessage(), conversationNode);

			// get tag subscribers and add them as conversation subscriber
			Collection<MailAddress> subscriber = new ArrayList<MailAddress>();
			for (Node tagNode : tags) {
				QueryManager qm = getSession().getWorkspace().getQueryManager();
				Query query = qm.createQuery("//users/user/tags/tag[@reference='" 
						+ tagNode.getUUID() + "']", Query.XPATH);
				QueryResult qResult = query.execute();
				
				NodeIterator nit = qResult.getNodes();
				while(nit.hasNext()) {
					Node userNode = nit.nextNode().getParent().getParent();
					addSubscriberToConversation(conversationNode, userNode);
					
					MailAddress userAddress = new MailAddress(
							userNode.getProperty("mail").getString());
					
					if(!subscriber.contains(userAddress)) {
						subscriber.add(userAddress);
					}
				}
			}
			// send mail to subscriber
			MailAddress conversationAddress = new MailAddress(
					conversationNode.getProperty("id").getString() + "@" + 
					mail.getSender().getHost());
			forwardContribution(mail, conversationAddress, subscriber);
		}	
	}
	
	private Node createNewConversation(Node talkNode, Collection<Node> tags, 
			String projectName) throws RepositoryException {
		int id = 0;
		NodeIterator nit = talkNode.getNodes("conversation");
		while(nit.hasNext()) {
			Node tmp = nit.nextNode();
			int cid = Integer.valueOf(
					tmp.getProperty("id").getString().split("-")[1]);
			
			if(cid >= id) {
				id = cid;
			}
		}
		id++;
		Node conversationNode = talkNode.addNode("conversation");
		conversationNode.setProperty("id", projectName + "-" + id);
		conversationNode.addNode("subscribers");
		conversationNode.addNode("contributions");
		
		Node tagsNode = conversationNode.addNode("tags");
		for (Node tag : tags) {
			Node tagRef = tagsNode.addNode("tag");
			tagRef.setProperty("reference", tag);
		}
		getSession().save();
		return conversationNode;
	}

	private void handleContribution(Mail mail, MailAddress address,
			Node conversationNode) throws RepositoryException, IOException,
			MessagingException, ItemExistsException, PathNotFoundException,
			VersionException, ConstraintViolationException, LockException,
			ParseException, AddressException {
		// check if the sender is a valid user
		Node userNode = checkUserExists(mail.getSender().toString());
		if (userNode == null) {
			getMailetContext().bounce(mail, "You are not a member and " + 
				"thus you are not allowed to send mails to this list.");
			return;
		}
		// add contribution to conversation
		addContributionToConversation(mail.getMessage(), conversationNode);
		
		// get subscriber and add the sender, if he is no subscriber
		Collection<MailAddress> subscriber = getConversationSubscriber(conversationNode);
		if(!subscriber.contains(mail.getSender())) {
			addSubscriberToConversation(conversationNode, userNode);
			
			subscriber.add(mail.getSender());
		}
		forwardContribution(mail, address, subscriber);
	}

	private void addSubscriberToConversation(Node conversationNode, 
			Node userNode) throws ItemExistsException, PathNotFoundException, 
			VersionException, ConstraintViolationException, LockException, 
			RepositoryException, ValueFormatException {
		Node subscribersNode = conversationNode.getNode("subscribers");
		Node subscriberNode = subscribersNode.addNode("subscriber");
		subscriberNode.setProperty("reference", userNode);
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
	}

	/**
	 * Get all subscriber of the given conversation.
	 * 
	 * @param conversationNode the node containing the conversation
	 * @return all users that are subscribers of the given conversation
	 */
	private Collection<MailAddress> getConversationSubscriber(
			Node conversationNode) throws PathNotFoundException, 
			RepositoryException, ParseException {
		// retrieve subscribers and add them to the result list
		Collection<MailAddress> subscriber = new ArrayList<MailAddress>();
		Node subscribersNode = conversationNode.getNode("subscribers");
		NodeIterator subNit = subscribersNode.getNodes("subscriber");

		while (subNit.hasNext()) {
			Node subscriberNode = subNit.nextNode();
			String reference = subscriberNode.getProperty("reference")
					.getString();

			Node userNode = getSession().getNodeByUUID(reference);
			String mailAddr = userNode.getProperty("mail").getString();
			subscriber.add(new MailAddress(mailAddr));
		}
		return subscriber;
	}
	
	private void forwardContribution(Mail mail, MailAddress address, 
			Collection<MailAddress> subscriber) throws AddressException, 
			MessagingException, IOException {
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

	/**
	 * Query the repository for a tag under the given project identifier.
	 * 
	 * @param projectName
	 *            the name of the project to be searched
	 * @param tagName
	 *            the name of the tag to find
	 * @return a node containing the tag if one exist, otherwise null
	 * @throws RepositoryException
	 */
	private Node checkTagExistsInProject(String projectName, String tagName)
			throws RepositoryException {
		QueryManager qm = getSession().getWorkspace().getQueryManager();
		Query query = qm.createQuery("//projects/project[@name='" + projectName
				+ "']/tags/tag[@name='" + tagName + "']", Query.XPATH);
		return executeQuery(query);
	}
	
	/**
	 * Query the repository for a project with the given project name.
	 * 
	 * @param projectName
	 *            the name of the project to be searched
	 * @return a node containing the project if one exist, otherwise null
	 * @throws RepositoryException
	 */
	private Node checkProjectExist(String projectName) 
		throws RepositoryException {
		QueryManager qm = getSession().getWorkspace().getQueryManager();
		Query query = qm.createQuery("//projects/project[@name='" + projectName
				+ "']", Query.XPATH);
		return executeQuery(query);
	}

	/**
	 * Query the repository for a conversation with the given identifier.
	 * 
	 * @param conversationID
	 *            the ID of the conversation to find
	 * @return a node containing the conversation if one exist, otherwise null
	 * @throws RepositoryException
	 *             thrown if something with the repository goes wrong
	 */
	private Node checkConversationExists(String conversationID)
			throws RepositoryException {
		QueryManager qm = getSession().getWorkspace().getQueryManager();
		Query query = qm.createQuery(
				"//projects/project/talk/conversation[@id='" + conversationID
						+ "']", Query.XPATH);
		return executeQuery(query);
	}

	/**
	 * Query the repository for a user with the given mail address.
	 * 
	 * @param mailAddress
	 *            the mail address of the user to find
	 * @return a node containing the user if one exist, otherwise null
	 * @throws RepositoryException
	 *             thrown if something with the repository goes wrong
	 */
	private Node checkUserExists(String mailAddress) throws RepositoryException {
		QueryManager qm = getSession().getWorkspace().getQueryManager();
		Query query = qm.createQuery("//users/user[@mail='" + mailAddress
				+ "']", Query.XPATH);
		return executeQuery(query);
	}

	/**
	 * Execute a query and return the resulting node, if one exist.
	 */
	private Node executeQuery(Query query) throws RepositoryException {
		QueryResult qResult = query.execute();

		NodeIterator nit = qResult.getNodes();
		if (nit.hasNext()) {
			return nit.nextNode();
		} else {
			return null;
		}
	}
}
