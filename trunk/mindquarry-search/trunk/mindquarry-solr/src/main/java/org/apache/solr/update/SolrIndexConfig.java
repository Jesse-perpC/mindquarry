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

package org.apache.solr.update;

import org.apache.solr.core.SolrConfig;
import org.apache.lucene.index.IndexWriter;

//
// For performance reasons, we don't want to re-read
// config params each time an index writer is created.
// This config object encapsulates IndexWriter config params.
//
/**
 * @author yonik
 * @version $Id: SolrIndexConfig.java 472574 2006-11-08 18:25:52Z yonik $
 */
public class SolrIndexConfig {
  public static final String defaultsName ="indexDefaults";

  //default values
  public static final boolean defUseCompoundFile=SolrConfig.config.getBool(defaultsName +"/useCompoundFile", true);
  public static final int defMaxBufferedDocs=SolrConfig.config.getInt(defaultsName +"/maxBufferedDocs", -1);
  public static final int defMaxMergeDocs=SolrConfig.config.getInt(defaultsName +"/maxMergeDocs", -1);
  public static final int defMergeFactor=SolrConfig.config.getInt(defaultsName +"/mergeFactor", -1);
  public static final int defMaxFieldLength=SolrConfig.config.getInt(defaultsName +"/maxFieldLength", -1);
  public static final int defWriteLockTimeout=SolrConfig.config.getInt(defaultsName +"/writeLockTimeout", -1);
  public static final int defCommitLockTimeout=SolrConfig.config.getInt(defaultsName +"/commitLockTimeout", -1);

  /*** These are "final" in lucene 1.9
  static {
    if (writeLockTimeout != -1) IndexWriter.WRITE_LOCK_TIMEOUT=writeLockTimeout;
    if (commitLockTimeout != -1) IndexWriter.COMMIT_LOCK_TIMEOUT=commitLockTimeout;
  }
  ***/
  
  public final boolean useCompoundFile;
  public final int maxBufferedDocs;
  public final int maxMergeDocs;
  public final int mergeFactor;
  public final int maxFieldLength;
  public final int writeLockTimeout;
  public final int commitLockTimeout;

  public SolrIndexConfig(String prefix)  {
    useCompoundFile=SolrConfig.config.getBool(prefix+"/useCompoundFile", defUseCompoundFile);
    maxBufferedDocs=SolrConfig.config.getInt(prefix+"/maxBufferedDocs",defMaxBufferedDocs);
    maxMergeDocs=SolrConfig.config.getInt(prefix+"/maxMergeDocs",defMaxMergeDocs);
    mergeFactor=SolrConfig.config.getInt(prefix+"/mergeFactor",defMergeFactor);
    maxFieldLength= SolrConfig.config.getInt(prefix+"/maxFieldLength",defMaxFieldLength);
    writeLockTimeout= SolrConfig.config.getInt(prefix+"/writeLockTimeout", defWriteLockTimeout);
    commitLockTimeout= SolrConfig.config.getInt(prefix+"/commitLockTimeout", defCommitLockTimeout);
  }
}