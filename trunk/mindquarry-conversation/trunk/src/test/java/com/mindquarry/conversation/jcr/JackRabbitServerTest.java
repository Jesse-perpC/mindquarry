package com.mindquarry.conversation.jcr;

import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import javax.jcr.Repository;

import junit.framework.TestCase;

import org.apache.jackrabbit.core.TransientRepository;
import org.apache.jackrabbit.rmi.remote.RemoteRepository;
import org.apache.jackrabbit.rmi.server.ServerAdapterFactory;

public class JackRabbitServerTest extends TestCase {
	public void testJackRabbitServer() throws IOException, NotBoundException {
		Repository repository = new TransientRepository();
		ServerAdapterFactory factory = new ServerAdapterFactory();
		RemoteRepository remote = factory.getRemoteRepository(repository);
		
		Registry reg = LocateRegistry.createRegistry(1100);
		reg.rebind("jackrabbit", remote);
		
		System.out.println("Please press <enter> to shutdown the server!!");
		System.in.read();
		
		reg.unbind("jackrabbit");
	}
}
