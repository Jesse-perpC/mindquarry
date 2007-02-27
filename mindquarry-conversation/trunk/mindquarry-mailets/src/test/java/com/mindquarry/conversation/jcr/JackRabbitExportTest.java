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
package com.mindquarry.conversation.jcr;

import java.io.FileOutputStream;

import javax.jcr.Repository;
import javax.jcr.Session;
import javax.jcr.SimpleCredentials;

import junit.framework.TestCase;

import org.apache.jackrabbit.rmi.client.ClientRepositoryFactory;

public class JackRabbitExportTest extends TestCase {
	/**
	 * Dump the entire repository.
	 */
	public void testDumpRepositoryData() {
		Session session = null;
		try {
			ClientRepositoryFactory factory = new ClientRepositoryFactory();
			Repository repo = factory
					.getRepository("rmi://localhost:1100/jackrabbit");
			session = repo.login(new SimpleCredentials("alexander.saar",
					"mypwd".toCharArray()), "default");

			FileOutputStream fos = new FileOutputStream(
					"content-repo-projects.xml");
			session.exportDocumentView("/projects", fos, true, false);
			fos.flush();
			fos.close();

			fos = new FileOutputStream("content-repo-users.xml");
			session.exportDocumentView("/users", fos, true, false);
			fos.flush();
			fos.close();

			System.out.println("Export finished!!!");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			session.logout();
		}
	}
}
