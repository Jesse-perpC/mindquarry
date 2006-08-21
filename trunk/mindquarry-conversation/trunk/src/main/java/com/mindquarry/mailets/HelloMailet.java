/*
 * Copyright (C) 2006, Mindquarry GmbH 
 */
package com.mindquarry.mailets;

import java.util.Enumeration;

import javax.mail.MessagingException;

import org.apache.mailet.GenericMailet;
import org.apache.mailet.Mail;

/**
 * @author <a hef="mailto:alexander(dot)saar(at)mindquarry(dot)com</a>
 */
public class HelloMailet extends GenericMailet {
	/**
	 * @see org.apache.mailet.GenericMailet#service(org.apache.mailet.Mail)
	 */
	@Override
	public void service(Mail mail) throws MessagingException {
		System.out.println("Processed mail with following header:");

		Enumeration headerLines = mail.getMessage().getAllHeaderLines();
		while (headerLines.hasMoreElements()) {
			System.out.println(headerLines.nextElement());
		}
	}
}
