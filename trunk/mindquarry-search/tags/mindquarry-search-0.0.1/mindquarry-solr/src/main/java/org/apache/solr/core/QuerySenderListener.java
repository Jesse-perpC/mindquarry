/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.solr.core;

import org.apache.solr.search.SolrIndexSearcher;
import org.apache.solr.search.DocList;
import org.apache.solr.search.DocIterator;
import org.apache.solr.request.LocalSolrQueryRequest;
import org.apache.solr.request.SolrQueryResponse;
import org.apache.solr.util.NamedList;

import java.util.List;

/**
 * @author yonik
 * @version $Id: QuerySenderListener.java 472574 2006-11-08 18:25:52Z yonik $
 */
class QuerySenderListener extends AbstractSolrEventListener {

  public void newSearcher(SolrIndexSearcher newSearcher, SolrIndexSearcher currentSearcher) {
    final SolrIndexSearcher searcher = newSearcher;
    SolrCore core = SolrCore.getSolrCore();
    log.info("QuerySenderListener sending requests to " + newSearcher);
    for (NamedList nlst : (List<NamedList>)args.get("queries")) {
      try {
        // bind the request to a particular searcher (the newSearcher)
        LocalSolrQueryRequest req = new LocalSolrQueryRequest(core,nlst) {
          public SolrIndexSearcher getSearcher() {
            return searcher;
          }
          public void close() {
          }
        };

        SolrQueryResponse rsp = new SolrQueryResponse();
        core.execute(req,rsp);

        // Retrieve the Document instances (not just the ids) to warm
        // the OS disk cache, and any Solr document cache.  Only the top
        // level values in the NamedList are checked for DocLists.
        NamedList values = rsp.getValues();
        for (int i=0; i<values.size(); i++) {
          Object o = values.getVal(i);
          if (o instanceof DocList) {
            DocList docs = (DocList)o;
            for (DocIterator iter = docs.iterator(); iter.hasNext();) {
              newSearcher.doc(iter.nextDoc());
            }
          }
        }

        req.close();

      } catch (Exception e) {
        // do nothing... we want to continue with the other requests.
        // the failure should have already been logged.
      }
      log.info("QuerySenderListener done.");
    }
  }



}
