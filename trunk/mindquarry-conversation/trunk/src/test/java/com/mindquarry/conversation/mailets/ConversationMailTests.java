/*
 * Copyright (C) 2006, Mindquarry GmbH 
 */
package com.mindquarry.conversation.mailets;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * @author <a hef="mailto:alexander(dot)saar(at)mindquarry(dot)com</a>
 */
public class ConversationMailTests {
	public static Test suite() {
		TestSuite suite = new TestSuite(
				"Test cases for the conversation mail functionality.");
		
		//$JUnit-BEGIN$
		suite.addTestSuite(SendMailTest.class);
		suite.addTestSuite(ReceiveMailTest.class);
		//$JUnit-END$
		return suite;
	}
}
