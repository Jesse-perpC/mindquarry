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
