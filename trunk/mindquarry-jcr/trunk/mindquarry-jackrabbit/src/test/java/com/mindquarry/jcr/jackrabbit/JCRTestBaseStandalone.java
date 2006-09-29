/**
 * Copyright (C) 2006 MindQuarry GmbH, All Rights Reserved
 */
package com.mindquarry.jcr.jackrabbit;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import javax.jcr.Repository;
import javax.jcr.Session;
import javax.jcr.SimpleCredentials;

import junit.framework.TestCase;

import org.apache.jackrabbit.core.TransientRepository;
import org.apache.jackrabbit.rmi.remote.RemoteRepository;
import org.apache.jackrabbit.rmi.server.ServerAdapterFactory;

/**
 * Abstract base classes for all JCR XML source test cases.
 * 
 * @author <a href="mailto:alexander(dot)saar(at)mindquarry(dot)com">Alexander
 *         Saar</a>
 */
public abstract class JCRTestBaseStandalone extends TestCase {
	public static final String SCHEME = "jcr";

	public static final String BASE_URL = SCHEME + ":///";

	public static final String LOGIN = "alexander.saar";

	public static final String PWD = "mypwd";

	public static final String WORKSPACE = "default";

	public static final String REMOTE_REPO_NAME = "jackrabbit";

	public static final String MINDQUARRY_JCR_XML_NAMESPACE_PREFIX = "xt";

	public static final String MINDQUARRY_JCR_XML_NAMESPACE_URI = "http://mindquarry.com/ns/cnd/xt";

	public static final String MINDQUARRY_JCR_XML_NODETYPES_FILE = "src/test/resources/node-types.txt";

	protected Repository repo;

	protected Registry reg;

	protected Session session;

	@Override
	protected void setUp() throws Exception {
		// remove old repository
		File repoFolder = new File("target/repository");
		removeRepository(repoFolder);

		Repository repo = new TransientRepository(
				"src/test/resources/repository.xml", "target/repository");
		ServerAdapterFactory factory = new ServerAdapterFactory();
		RemoteRepository remoteRepo = factory.getRemoteRepository(repo);

		reg = LocateRegistry.createRegistry(1100);
		reg.rebind(REMOTE_REPO_NAME, remoteRepo);

		session = repo.login(new SimpleCredentials(LOGIN, PWD.toCharArray()),
				WORKSPACE);
		session = repo.login(new SimpleCredentials("alexander.saar", "mypwd"
				.toCharArray()));
		JackrabbitInitializerHelper.setupRepository(session,
				new InputStreamReader(new FileInputStream(
						MINDQUARRY_JCR_XML_NODETYPES_FILE)), "");
	}

	/**
	 * Shutdown Jackrabbit repository.
	 */
	@Override
	protected void tearDown() throws Exception {
		// uncomment this for shutting down RMI repository
		reg.unbind(REMOTE_REPO_NAME);
	}

	private void removeRepository(File file) {
		// check if the file exists
		if (!file.exists()) {
			return;
		}
		// check if it is a file or a folder
		if (!file.isDirectory()) {
			file.delete();
			return;
		}
		// if it is a folder, remove the childs before removing the folder
		for (File tmp : file.listFiles()) {
			removeRepository(tmp);
		}
		file.delete();
	}
}
