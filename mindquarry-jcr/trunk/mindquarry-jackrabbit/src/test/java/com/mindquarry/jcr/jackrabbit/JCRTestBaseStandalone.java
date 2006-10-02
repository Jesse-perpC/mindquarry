/**
 * Copyright (C) 2006 MindQuarry GmbH, All Rights Reserved
 */
package com.mindquarry.jcr.jackrabbit;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import javax.jcr.Repository;
import javax.jcr.Session;
import javax.jcr.SimpleCredentials;

import junit.framework.TestCase;

import org.apache.commons.io.IOUtils;
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

    public static final String REPO_CONFIG_FILE = "/com/mindquarry/jcr/jackrabbit/repository.xml";
    
	public static final String MQ_JCR_XML_NODETYPES_FILE = "/com/mindquarry/jcr/jackrabbit/node-types.txt";

	protected Repository repo;

	protected Registry reg;

	protected Session session;

	@Override
	protected void setUp() throws Exception {
		// remove old repository
		File repoFolder = new File("target/repository");
		removeRepository(repoFolder);

        InputStream repoConfigIn = getClass().getResourceAsStream(REPO_CONFIG_FILE);
        
        File tempRepoConfigFile = File.createTempFile("repository", "xml");
        tempRepoConfigFile.deleteOnExit();
        
        OutputStream tempRepoConfigOut = new FileOutputStream(tempRepoConfigFile);
        
        try {
            IOUtils.copy(repoConfigIn, tempRepoConfigOut);
        } finally {
            repoConfigIn.close();
            tempRepoConfigOut.close();
        }
        
        Repository repo = new TransientRepository(
                tempRepoConfigFile.getAbsolutePath(), "target/repository");
        
		ServerAdapterFactory factory = new ServerAdapterFactory();
		RemoteRepository remoteRepo = factory.getRemoteRepository(repo);

		reg = LocateRegistry.createRegistry(Registry.REGISTRY_PORT);
		reg.rebind(REMOTE_REPO_NAME, remoteRepo);

		session = repo.login(new SimpleCredentials(LOGIN, PWD.toCharArray()),
				WORKSPACE);
        
        InputStream nodeTypeDefIn = getClass().getResourceAsStream(MQ_JCR_XML_NODETYPES_FILE);
		JackrabbitInitializerHelper.setupRepository(session,
				new InputStreamReader(nodeTypeDefIn), "");
	}

	/**
	 * Shutdown Jackrabbit repository.
	 */
	@Override
	protected void tearDown() throws Exception {
		// shutting down RMI repository
		reg.unbind(REMOTE_REPO_NAME);
		session.logout();
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
