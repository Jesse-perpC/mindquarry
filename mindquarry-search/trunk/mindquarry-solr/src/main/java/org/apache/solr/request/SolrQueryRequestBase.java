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

package org.apache.solr.request;

import org.apache.solr.search.SolrIndexSearcher;
import org.apache.solr.util.RefCounted;
import org.apache.solr.schema.IndexSchema;
import org.apache.solr.core.SolrCore;
import org.apache.solr.core.SolrException;

import java.util.Map;
import java.util.HashMap;

/**
 * Base implementation of <code>SolrQueryRequest</code> that provides some
 * convenience methods for accessing parameters, and manages an IndexSearcher
 * reference.
 *
 * <p>
 * The <code>close()</code> method must be called on any instance of this
 * class once it is no longer in use.
 * </p>
 *
 *
 * @author yonik
 * @version $Id: SolrQueryRequestBase.java 472574 2006-11-08 18:25:52Z yonik $
 */
public abstract class SolrQueryRequestBase implements SolrQueryRequest {
  @Deprecated
  public static final String QUERY_NAME="q";
  @Deprecated
  public static final String START_NAME="start";
  @Deprecated
  public static final String ROWS_NAME="rows";
  @Deprecated
  public static final String XSL_NAME="xsl";
  @Deprecated
  public static final String QUERYTYPE_NAME="qt";

  protected final SolrCore core;
  protected final SolrParams origParams;
  protected SolrParams params;
  protected Map<Object,Object> context;

  public SolrQueryRequestBase(SolrCore core, SolrParams params) {
    this.core = core;
    this.params = this.origParams = params;
  }

  public Map<Object,Object> getContext() {
    // SolrQueryRequest as a whole isn't thread safe, and this isn't either.
    if (context==null) context = new HashMap<Object,Object>();
    return context;
  }

  public SolrParams getParams() {
    return params;
  }

  public SolrParams getOriginalParams() {
    return origParams;
  }

  public void setParams(SolrParams params) {
    this.params = params;
  }

  public String getParam(String name) {
    return params.get(name);
  }

  public String[] getParams(String name) {
    return params.getParams(name);
  }


  public int getIntParam(String name) {
    String s = getParam(name);
    if (s==null) {
      throw new SolrException(500,"Missing required parameter '"+name+"' from " + this);
    }
    return Integer.parseInt(s);
  }

  public int getIntParam(String name, int defval) {
    String s = getParam(name);
    return s==null ? defval : Integer.parseInt(s);
  }

  public String getStrParam(String name) {
    String s = getParam(name);
    if (s==null) {
      throw new SolrException(500,"Missing required parameter '"+name+"' from " + this);
    }
    return s;
  }

  public String getStrParam(String name, String defval) {
    String s = getParam(name);
    return s==null ? defval : s;
  }

  public String getQueryString() {
    return params.get(SolrParams.Q);
  }

  public String getQueryType() {
    return params.get(SolrParams.QT);
  }

  // starting position in matches to return to client
  public int getStart() {
    return params.getInt(SolrParams.START, 0);
  }

  // number of matching documents to return
  @Deprecated
  public int getLimit() {
    return params.getInt(SolrParams.ROWS, 10);
  }


  protected final long startTime=System.currentTimeMillis();
  // Get the start time of this request in milliseconds
  public long getStartTime() {
    return startTime;
  }

  // The index searcher associated with this request
  protected RefCounted<SolrIndexSearcher> searcherHolder;
  public SolrIndexSearcher getSearcher() {
    // should this reach out and get a searcher from the core singleton, or
    // should the core populate one in a factory method to create requests?
    // or there could be a setSearcher() method that Solr calls

    if (searcherHolder==null) {
      searcherHolder = core.getSearcher();
    }

    return searcherHolder.get();
  }

  // The solr core (coordinator, etc) associated with this request
  public SolrCore getCore() {
    return core;
  }

  // The index schema associated with this request
  public IndexSchema getSchema() {
    return core.getSchema();
  }

  /**
   * Frees resources associated with this request, this method <b>must</b>
   * be called when the object is no longer in use.
   */
  public void close() {
    if (searcherHolder!=null) {
      searcherHolder.decref();
    }
  }


  public String getParamString() {
    return origParams.toString();
  }

  public String toString() {
    return this.getClass().getSimpleName() + '{' + params + '}';
  }

}
