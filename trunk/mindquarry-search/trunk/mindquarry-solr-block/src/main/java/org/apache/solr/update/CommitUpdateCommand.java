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
/**
 * @author yonik
 * @version $Id: CommitUpdateCommand.java 472574 2006-11-08 18:25:52Z yonik $
 */
public class CommitUpdateCommand extends UpdateCommand {
  public boolean optimize;
  public boolean waitFlush;
  public boolean waitSearcher=true;

  public CommitUpdateCommand(boolean optimize) {
    super("commit");
    this.optimize=optimize;
  }
  public String toString() {
    return "commit(optimize="+optimize
            +",waitFlush="+waitFlush
            +",waitSearcher="+waitSearcher
            +')';
  }
}
