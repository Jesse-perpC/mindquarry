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

import org.apache.avalon.framework.logger.ConsoleLogger;
import org.apache.avalon.framework.logger.Logger;
import org.apache.commons.io.IOUtils;
import org.apache.jackrabbit.core.TransientRepository;
import org.apache.jackrabbit.rmi.remote.RemoteRepository;
import org.apache.jackrabbit.rmi.server.ServerAdapterFactory;

import com.mindquarry.common.index.IndexClient;
import com.mindquarry.common.index.SolrIndexClient;

/**
 * Abstract base classes for all JCR XML source test cases.
 * 
 * @author <a href="mailto:alexander(dot)saar(at)mindquarry(dot)com">Alexander
 *         Saar</a>
 */
public abstract class JCRTestBaseStandalone extends TestCase {
    public static final String SCHEME = "jcr"; //$NON-NLS-1$

    public static final String BASE_URL = SCHEME + ":///"; //$NON-NLS-1$

    public static final String LOGIN = "alexander.saar"; //$NON-NLS-1$

    public static final String PWD = "mypwd"; //$NON-NLS-1$

    public static final String WORKSPACE = "default"; //$NON-NLS-1$

    public static final String REMOTE_REPO_NAME = "jackrabbit"; //$NON-NLS-1$

    public static final String REPO_CONFIG_FILE = "/com/mindquarry/jcr/jackrabbit/repository.xml"; //$NON-NLS-1$

    public static final String MQ_JCR_XML_NODETYPES_FILE = "/com/mindquarry/jcr/jackrabbit/node-types.txt"; //$NON-NLS-1$

    protected Repository repo;

    protected Registry reg;

    protected Session session;

    private Logger logger;

    protected Logger getLogger() {
        return logger;
    }

    @Override
    protected void setUp() throws Exception {

        this.logger = new ConsoleLogger(ConsoleLogger.LEVEL_WARN);

        // remove old repository
        File repoFolder = new File("target/repository"); //$NON-NLS-1$
        removeRepository(repoFolder);

        InputStream repoConfigIn = getClass().getResourceAsStream(
                REPO_CONFIG_FILE);

        File tempRepoConfigFile = File.createTempFile("repository", "xml"); //$NON-NLS-1$ //$NON-NLS-2$
        tempRepoConfigFile.deleteOnExit();

        OutputStream tempRepoConfigOut = new FileOutputStream(
                tempRepoConfigFile);

        try {
            IOUtils.copy(repoConfigIn, tempRepoConfigOut);
        } finally {
            repoConfigIn.close();
            tempRepoConfigOut.close();
        }

        Repository repo = new TransientRepository(tempRepoConfigFile
                .getAbsolutePath(), "target/repository"); //$NON-NLS-1$

        ServerAdapterFactory factory = new ServerAdapterFactory();
        RemoteRepository remoteRepo = factory.getRemoteRepository(repo);

        reg = LocateRegistry.createRegistry(Registry.REGISTRY_PORT);
        reg.rebind(REMOTE_REPO_NAME, remoteRepo);

        session = repo.login(new SimpleCredentials(LOGIN, PWD.toCharArray()),
                WORKSPACE);

        InputStream nodeTypeDefIn = getClass().getResourceAsStream(
                MQ_JCR_XML_NODETYPES_FILE);

        IndexClient iClient = new SolrIndexClient();
        JackrabbitInitializerHelper.setupRepository(session,
                new InputStreamReader(nodeTypeDefIn), "", iClient); //$NON-NLS-1$
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
