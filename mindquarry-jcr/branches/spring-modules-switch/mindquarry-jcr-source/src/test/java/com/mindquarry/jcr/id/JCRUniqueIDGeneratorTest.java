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
package com.mindquarry.jcr.id;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.apache.avalon.framework.service.ServiceException;

import com.mindquarry.jcr.xml.source.JCRNodeSource;
import com.mindquarry.jcr.xml.source.JCRSourceTestBase;

/**
 * @author <a href="mailto:alexander(dot)klimetschek(at)mindquarry(dot)com">
 *         Alexander Klimetschek</a>
 */
public class JCRUniqueIDGeneratorTest extends JCRSourceTestBase {
    private final static String jcrPath = "/idfolder"; //$NON-NLS-1$

    private List<Long> results;

    private synchronized void addResult(long value) {
        results.add(value);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        results = new ArrayList<Long>();

        JCRNodeSource source = (JCRNodeSource) resolveSource("jcr://"//$NON-NLS-1$ 
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
        final int nThread = 5;

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
            assertEquals(value + 1, current);
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
