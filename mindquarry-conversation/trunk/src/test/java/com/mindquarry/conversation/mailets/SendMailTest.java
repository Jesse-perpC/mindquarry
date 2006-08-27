/*
 * Copyright (C) 2006, Mindquarry GmbH 
 */
package com.mindquarry.conversation.mailets;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import javax.mail.Address;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import junit.framework.TestCase;

/**
 * @author <a hef="mailto:alexander(dot)saar(at)mindquarry(dot)com</a>
 */
public class SendMailTest extends TestCase {
	private static final String P_FILE_NAME = "src/test/resources/mail.properties";

	public void testSendMail() throws IOException, MessagingException {
		File pFile = new File(P_FILE_NAME);
		InputStream pis = new FileInputStream(pFile);

		Properties props = new Properties();
		props.load(pis);

		System.out.println("Using following properties:\n");
		props.store(System.out, null);

		Address from = new InternetAddress("test@example.org", "Test User");
		Address to = new InternetAddress("test@example.org", "Test User");

		Session session = Session.getDefaultInstance(props, null);
		MimeMessage msg = new MimeMessage(session);

		msg.setFrom(from);
		msg.addRecipient(Message.RecipientType.TO, to);

		msg.setSubject("test");
		msg.setText("this is a test...");

		Transport.send(msg);
	}
}
