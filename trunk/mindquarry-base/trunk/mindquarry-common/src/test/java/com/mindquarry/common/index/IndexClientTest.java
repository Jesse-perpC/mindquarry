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
package com.mindquarry.common.index;

import java.util.ArrayList;
import java.util.List;

import org.apache.avalon.framework.service.ServiceException;

import com.mindquarry.common.test.AvalonSpringContainerTestBase;

/**
 * Add summary documentation here.
 * 
 * @author <a href="mailto:alexander(dot)saar(at)mindquarry(dot)com">Alexander
 *         Saar</a>
 */
public class IndexClientTest extends AvalonSpringContainerTestBase {
    public void testIndexUpdate() throws ServiceException {
        List<String> deletedPaths = new ArrayList<String>();
        //deletedPaths.add("jcr:///teamspaces/mindquarry/tasks/task3.xml");
        List<String> changedPaths = new ArrayList<String>();
        changedPaths.add("jcr:///teamspaces/mindquarry/tasks/task3.xml");

        SolrIndexClient iClient = new SolrIndexClient();
        iClient.setSolrLogin("admin");
        iClient.setSolrPassword("admin");
        iClient.setSolrEndpoint("http://localhost:8888/solr/update");

        iClient.indexSynch(changedPaths, deletedPaths);
                
        SolrIndexClient indexClient = (SolrIndexClient) lookup(IndexClient.ROLE);
        assertEquals("admin", indexClient.getSolrLogin());
    }
}
