/**
 * Copyright (C) 2006 Mindquarry GmbH, All Rights Reserved
 */
package com.mindquarry.jcr.jackrabbit;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import org.apache.avalon.framework.configuration.Configuration;
import org.apache.avalon.framework.configuration.ConfigurationException;
import org.apache.jackrabbit.rmi.remote.RemoteRepository;
import org.apache.jackrabbit.rmi.server.ServerAdapterFactory;

/**
 * This extension of the Cocoon internal Jackrabbit repository enables remote
 * access to the repository via RMI.
 * 
 * @author <a href="mailto:alexander(dot)saar(at)mindquarry(dot)com">Alexander
 *         Saar</a>
 */
public class JackrabbitRMIRepository extends
        org.apache.cocoon.jcr.JackrabbitRepository {
    /**
     * The name of the remote repository.
     */
    public static final String REMOTE_REPO_NAME = "jackrabbit";

    /**
     * The RMI registry used for publishing the remote repository stubs.
     */
    private Registry reg;

    /**
     * @see org.apache.cocoon.jcr.JackrabbitRepository#configure(org.apache.avalon.framework.configuration.Configuration)
     */
    @Override
    public void configure(Configuration config) throws ConfigurationException {
        super.configure(config);

        // register RMI repository
        ServerAdapterFactory factory = new ServerAdapterFactory();
        try {
            RemoteRepository remoteRepo = factory.getRemoteRepository(this);

            reg = LocateRegistry.createRegistry(Registry.REGISTRY_PORT);
            reg.rebind(REMOTE_REPO_NAME, remoteRepo);
        } catch (RemoteException e) {
            throw new ConfigurationException(
                    "An error occured during RMI repository setup.", config, e);
        }
    }

    /**
     * @see org.apache.cocoon.jcr.JackrabbitRepository#dispose()
     */
    @Override
    public void dispose() {
        // shutdown RMI repository
        try {
            reg.unbind(REMOTE_REPO_NAME);
        } catch (Exception e) {
            // nothing to do
        } finally {
            reg = null;
        }
        super.dispose();
    }
}
