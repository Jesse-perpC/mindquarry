/*
 * Copyright (C) 2006, Mindquarry GmbH 
 */
package com.mindquarry.conversation.mailets;

import java.io.IOException;

import javax.mail.MessagingException;
import javax.mail.internet.MimePart;

import org.apache.mailet.GenericMailet;
import org.apache.mailet.Mail;

/**
 * @author <a hef="mailto:alexander(dot)saar(at)mindquarry(dot)com</a>
 */
public abstract class AbstractConversationMailet extends GenericMailet {
	protected void attachFooter(Mail mail) throws MessagingException,
			IOException {
		MimePart part = mail.getMessage();
		if (part.isMimeType("text/plain")) {
			addToText(part);
		} else if (part.isMimeType("text/html")) {
			addToHTML(part);
		}
	}

	private void addToText(MimePart part) throws IOException,
			MessagingException {
		String content = part.getContent().toString();
		content += "\r\n\r\n" + getFooterText();
		part.setText(content);
	}

	private void addToHTML(MimePart part) throws MessagingException,
			IOException {
		String content = part.getContent().toString();

		/*
		 * This HTML part may have a closing <BODY> tag. If so, we want to
		 * insert out footer immediately prior to that tag.
		 */
		int index = content.lastIndexOf("</body>");
		if (index == -1)
			index = content.lastIndexOf("</body>");

		String footer = "<br/><br/>" + getFooterText();
		content = index == -1 ? content + footer : content.substring(0, index)
				+ footer + content.substring(index);
		part.setContent(content, part.getContentType());
	}

	private String getFooterText() {
		return "Track this conversation at ...";
	}
}
