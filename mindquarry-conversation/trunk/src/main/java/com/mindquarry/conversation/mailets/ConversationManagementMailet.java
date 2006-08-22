/*
 * Copyright (C) 2006, Mindquarry GmbH 
 */
package com.mindquarry.conversation.mailets;

import javax.mail.MessagingException;

import org.apache.mailet.GenericMailet;
import org.apache.mailet.Mail;
import org.apache.mailet.MailAddress;

/**
 * @author <a hef="mailto:alexander(dot)saar(at)mindquarry(dot)com</a>
 */
public class ConversationManagementMailet extends GenericMailet {
	/**
	 * Initialize the mailet
	 */
	public void init() {
		// ComponentManager compMgr = (ComponentManager) getMailetContext()
		// .getAttribute(Constants.AVALON_COMPONENT_MANAGER);
		// try {
		// UsersStore usersStore = (UsersStore) compMgr
		// .lookup("org.apache.james.services.UsersStore");
		// String repName = getInitParameter("repositoryName");
		//
		// members = (UsersRepository) usersStore.getRepository(repName);
		// } catch (ComponentException cnfe) {
		// log("Failed to retrieve Store component:" + cnfe.getMessage());
		// } catch (Exception e) {
		// log("Failed to retrieve Store component:" + e.getMessage());
		// }
	}

	/**
	 * @see org.apache.mailet.GenericMailet#service(org.apache.mailet.Mail)
	 */
	@Override
	public void service(Mail mail) throws MessagingException {
		if (mail.getRecipients().size() != 1) {
			getMailetContext()
					.bounce(mail,
							"You can only send one command at a time to this conversation manager.");
			return;
		}
		MailAddress address = (MailAddress) mail.getRecipients().iterator()
				.next();
		if (address.getUser().endsWith("-off")) {
			if (existsAddress(mail.getSender())) {
				if (removeAddress(mail.getSender())) {
					getMailetContext().bounce(mail,
							"Successfully removed from listserv.");
				} else {
					getMailetContext()
							.bounce(mail,
									"Unable to remove you from the conversation for some reason.");
				}
			} else {
				getMailetContext().bounce(mail,
						"You are not subscribed to this conversation.");
			}
		} else if (address.getUser().endsWith("-on")) {
			if (existsAddress(mail.getSender())) {
				getMailetContext().bounce(mail,
						"You are already subscribed to this conversation.");
			} else {
				if (addAddress(mail.getSender())) {
					getMailetContext().bounce(mail,
							"Successfully subscribed to conversation.");
				} else {
					getMailetContext()
							.bounce(mail,
									"Unable to add you to the conversation for some reason.");
				}
			}
		} else {
			getMailetContext()
					.bounce(
							mail,
							"Could not understand the command you sent to this conversation manager.\r\n"
									+ "Valid commands are <listserv>-on@domain.com and <listserv>-off@domain.com");
		}
		// Kill the command message
		mail.setState(Mail.GHOST);
	}

	/**
	 * Add an address to the list.
	 * 
	 * @param address
	 *            the address to add
	 * 
	 * @return true if successful, false otherwise
	 */
	public boolean addAddress(MailAddress address) {
		// members.addUser(address.toString(), "");
		return true;
	}

	/**
	 * Remove an address from the list.
	 * 
	 * @param address
	 *            the address to remove
	 * 
	 * @return true if successful, false otherwise
	 */
	public boolean removeAddress(MailAddress address) {
		// members.removeUser(address.toString());
		return true;
	}

	public boolean existsAddress(MailAddress address) {
		return true; // members.contains(address.toString());
	}

	/**
	 * Return a string describing this mailet.
	 * 
	 * @return a string describing this mailet
	 */
	public String getMailetInfo() {
		return "Conversation Manager Mailet";
	}
}
