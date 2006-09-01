/*
 * Copyright (C) 2006, Mindquarry GmbH 
 */
package com.mindquarry.conversation.matchers;

import java.util.ArrayList;
import java.util.Collection;

import javax.mail.MessagingException;

import org.apache.mailet.GenericMatcher;
import org.apache.mailet.Mail;
import org.apache.mailet.MailAddress;

/**
 * @author <a hef="mailto:alexander(dot)saar(at)mindquarry(dot)com</a>
 */
public class ConversationMatcher extends GenericMatcher {
	/**
	 * @see org.apache.mailet.GenericMatcher#match(org.apache.mailet.Mail)
	 */
	@Override
	public Collection match(Mail mail) throws MessagingException {
		log("Start processing of mail...");

		Collection recipients = mail.getRecipients();
		ArrayList<MailAddress> targets = new ArrayList<MailAddress>();
		for (Object object : recipients) {
			MailAddress recipient = (MailAddress) object;
			String name = recipient.getUser();

			log("Checking recipient " + name + "...");
			String[] parts = name.split("-");
			if (parts.length != 2) {
				log("Unsupported conversation address pattern detected.");
			} else {
				targets.add(recipient);
			}
		}
		return (targets);
	}
}
