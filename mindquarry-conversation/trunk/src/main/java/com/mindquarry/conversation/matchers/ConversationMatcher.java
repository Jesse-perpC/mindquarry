/*
 * Copyright (C) 2006, Mindquarry GmbH 
 */
package com.mindquarry.conversation.matchers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.mail.MessagingException;

import org.apache.mailet.GenericMatcher;
import org.apache.mailet.Mail;
import org.apache.mailet.MailAddress;

/**
 * @author <a hef="mailto:alexander(dot)saar(at)mindquarry(dot)com</a>
 */
public class ConversationMatcher extends GenericMatcher {
	private static List<String> projects;
	
	private static List<String> tags;
	
	private static List<String> user;
	
	static {
		projects = new ArrayList<String>();
		projects.add("Mindquarry");
		projects.add("Cyclr");
		
		tags = new ArrayList<String>();
		tags.add("dev");
		tags.add("support");
		
		user = new ArrayList<String>();
		user.add("test");
	}
	
	/**
	 * @see org.apache.mailet.GenericMatcher#match(org.apache.mailet.Mail)
	 */
	@Override
	public Collection match(Mail mail) throws MessagingException {
		log("Start processing of received mail...");
		
		Collection recipients = mail.getRecipients();
		for (Object object : recipients) {
			MailAddress recipient = (MailAddress)object;
			String toID = recipient.getUser();
			
		}
		return (mail.getRecipients());
	}
}
