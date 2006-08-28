/*
 * Copyright (C) 2006, Mindquarry GmbH 
 */
package com.mindquarry.conversation.matcher;

import java.util.Collection;

import javax.mail.MessagingException;

import org.apache.mailet.GenericMatcher;
import org.apache.mailet.Mail;

/**
 * @author <a hef="mailto:alexander(dot)saar(at)mindquarry(dot)com</a>
 */
public class ConversationMatcher extends GenericMatcher {
	/**
	 * @see org.apache.mailet.GenericMatcher#match(org.apache.mailet.Mail)
	 */
	@Override
	public Collection match(Mail mail) throws MessagingException {
		System.out.println(getCondition());
		
		return (mail.getRecipients());
	}
}
