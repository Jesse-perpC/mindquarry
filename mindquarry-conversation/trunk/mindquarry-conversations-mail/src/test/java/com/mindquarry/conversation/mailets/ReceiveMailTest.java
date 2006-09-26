/*
 * Copyright (C) 2006, Mindquarry GmbH 
 */
package com.mindquarry.conversation.mailets;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Store;

import junit.framework.TestCase;

/**
 * @author <a hef="mailto:alexander(dot)saar(at)mindquarry(dot)com</a>
 */
public class ReceiveMailTest extends TestCase {
	private static final String P_FILE_NAME = "src/test/resources/mail.properties";

	public void testReceiveMail() throws IOException, MessagingException {
		File pFile = new File(P_FILE_NAME);
		InputStream pis = new FileInputStream(pFile);

		Properties props = new Properties();
		props.load(pis);

		System.out.println("Using following properties:\n");
		props.store(System.out, null);
		System.out.println("\n");
		
		Session session = Session.getDefaultInstance(props, null);
		Store store = session.getStore("pop3");
		store.connect(props.getProperty("mail.pop.host"), props
				.getProperty("login.name"), props.getProperty("login.pwd"));

		// Get folder
		Folder folder = store.getFolder("INBOX");
		folder.open(Folder.READ_WRITE);

		// Get directory
		Message messages[] = folder.getMessages();
		for (Message message : messages) {
			System.out.println(message.getFrom()[0] + "\t"
					+ message.getSubject()+ "\t"
					+ message.getContent());
			
			// delete the received msg
			message.setFlag(Flags.Flag.DELETED, true);
		}
		// Close connection
		folder.close(true);
		store.close();
	}
}
