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

/**
 * @author yonik
 * @version $Id$
 */
public class DefaultSolrParams extends SolrParams {
  protected final SolrParams params;
  protected final SolrParams defaults;

  public DefaultSolrParams(SolrParams params, SolrParams defaults) {
    this.params = params;
    this.defaults = defaults;
  }

  public String get(String param) {
    String val = params.get(param);
    return val!=null ? val : defaults.get(param);
  }

  public String[] getParams(String param) {
    String[] vals = params.getParams(param);
    return vals!=null ? vals : defaults.getParams(param);
  }

  public String toString() {
    return "{params("+params+"),defaults("+defaults+")}";
  }
}
