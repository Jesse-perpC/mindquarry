package com.mindquarry.jcr.changes;

import java.io.FileInputStream;

import javax.jcr.ImportUUIDBehavior;
import javax.jcr.Repository;
import javax.jcr.Session;
import javax.jcr.SimpleCredentials;

import junit.framework.TestCase;

import org.apache.jackrabbit.rmi.client.ClientRepositoryFactory;

public class TestImport extends TestCase {
    public void testImport() throws Exception {
        ClientRepositoryFactory factory = new ClientRepositoryFactory();
        Repository repo = factory
                .getRepository("rmi://localhost:1099/jackrabbit");
        Session session = repo.login(new SimpleCredentials("admin", "admin"
                .toCharArray()));
        
//        NodeIterator it = session.getRootNode().getNodes();
//        while(it.hasNext()) {
//            Node node = it.nextNode();
//            if(!node.getName().equals("jcr:system")) {
//                node.remove();
//            }
//        }
//        session.save();
        
        session.importXML(
                "/", new FileInputStream("changed-content.xml"),
                ImportUUIDBehavior.IMPORT_UUID_CREATE_NEW);
        session.save();
    }
}
