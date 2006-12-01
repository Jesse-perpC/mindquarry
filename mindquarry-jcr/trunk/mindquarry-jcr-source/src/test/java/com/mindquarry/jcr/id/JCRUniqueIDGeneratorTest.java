/*
 * Coypright (c) 2006 Mindquarry GmbH, Potsdam, Germany 
 */
package com.mindquarry.jcr.id;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.apache.avalon.framework.service.ServiceException;

import com.mindquarry.jcr.xml.source.JCRNodeWrapperSource;
import com.mindquarry.jcr.xml.source.JCRSourceTestBase;

/**
 * @author <a href="mailto:alexander(dot)klimetschek(at)mindquarry(dot)com">
 *         Alexander Klimetschek</a>
 * 
 */
public class JCRUniqueIDGeneratorTest extends JCRSourceTestBase {

    private final static String jcrPath = "/idfolder";

    private List<Long> results;

    private synchronized void addResult(long value) {
        results.add(value);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        results = new ArrayList<Long>();

        JCRNodeWrapperSource source = (JCRNodeWrapperSource) resolveSource("jcr://"
                + jcrPath);
        // ensure the path exists
        if (!source.exists()) {
            source.makeCollection();
        }
        // ensure the id node exists
        JCRUniqueIDGenerator generator = (JCRUniqueIDGenerator) lookup(JCRUniqueIDGenerator.class
                .getName());
        generator.initializePath(jcrPath);
    }

    public void testGenerateID() throws ServiceException, IOException,
            IDException {
        JCRUniqueIDGenerator generator = (JCRUniqueIDGenerator) lookup(JCRUniqueIDGenerator.class
                .getName());
        // check initial id
        long id = generator.getNextID(jcrPath);
        assertEquals(1, id);

        // check id increment
        id = generator.getNextID(jcrPath);
        assertEquals(2, id);
    }

    public void testParallelIDAccess() throws ServiceException, IDException {
        final int nThread = 100;
        
        List<Thread> workers = new ArrayList<Thread>();
        for (int i = 0; i < nThread; i++) {
            Thread thread = new Thread(new Worker());
            thread.start();
            workers.add(thread);
        }
        
        for (Iterator<Thread> iter = workers.iterator(); iter.hasNext();) {
            Thread thread = iter.next();
            try {
                thread.join();
            } catch (InterruptedException e) {
                fail(e.getMessage());
            }
        }
            
        assertEquals(nThread, results.size());
        
        Collections.sort(results);
        
        // we must have all values from 1 to 10
        long value = 0;
        for (Iterator<Long> iter = results.iterator(); iter.hasNext();) {
            long current = iter.next();
            System.out.println(current);
            assertEquals(value+1, current);
            value = current;
        }
    }

    class Worker implements Runnable {
        public void run() {
            JCRUniqueIDGenerator generator;
            try {
                generator = (JCRUniqueIDGenerator) lookup(JCRUniqueIDGenerator.class
                        .getName());

                long id = generator.getNextID(jcrPath);
                addResult(id);
            } catch (ServiceException e) {
                e.printStackTrace();
            } catch (IDException e) {
                e.printStackTrace();
            }
        }
    }
}
