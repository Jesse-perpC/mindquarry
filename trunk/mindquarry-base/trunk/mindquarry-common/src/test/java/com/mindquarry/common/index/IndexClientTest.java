/**
 * Copyright (C) 2006 Mindquarry GmbH, All Rights Reserved
 */
package com.mindquarry.common.index;

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

/**
 * Add summary documentation here.
 * 
 * @author <a href="mailto:alexander(dot)saar(at)mindquarry(dot)com">Alexander
 *         Saar</a>
 */
public class IndexClientTest extends TestCase {
    public void testIndexUpdate() {
        List<String> deletedPaths = new ArrayList<String>();
        deletedPaths.add("jcr:///teamspaces/mindquarry/tasks/task3.xml");
        List<String> changedPaths = new ArrayList<String>();
        //changedPaths.add("jcr:///teamspaces/mindquarry/tasks/task3.xml");

        SolrIndexClient iClient = new SolrIndexClient();
        iClient.setSolrLogin("admin");
        iClient.setSolrPassword("admin");
        iClient.setSolrEndpoint("http://localhost:8888/solr/update");

        iClient.indexSynch(changedPaths, deletedPaths);
    }
}
