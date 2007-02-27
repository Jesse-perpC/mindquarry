/*
 * Copyright (C) 2006-2007 Mindquarry GmbH, All Rights Reserved
 * 
 * The contents of this file are subject to the Mozilla Public License
 * Version 1.1 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * http://www.mozilla.org/MPL/
 *
 * Software distributed under the License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
 * License for the specific language governing rights and limitations
 * under the License.
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
public class ReplyToConversationTest extends TestCase {
	private static final String P_FILE_NAME = "src/test/resources/mail.properties";

	public void testSendMail() throws IOException, MessagingException {
		File pFile = new File(P_FILE_NAME);
		InputStream pis = new FileInputStream(pFile);

		Properties props = new Properties();
		props.load(pis);

		Address from = new InternetAddress("lars.trieloff@mindquarry.com", "Alexander Saar");
		Address to = new InternetAddress("Mindquarry-1@conversation.mindquarry.com");

		Session session = Session.getDefaultInstance(props, null);
		MimeMessage msg = new MimeMessage(session);

		msg.setFrom(from);
		msg.addRecipient(Message.RecipientType.TO, to);

		msg.setSubject("RE: Mindquarry-1");
		msg.setText("a contribution to a conversation...");

		Transport.send(msg);
	}
}
