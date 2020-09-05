/*
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

package org.apache.solr.search;

import org.apache.solr.common.params.ModifiableSolrParams;
import org.apache.solr.common.params.SolrParams;
import org.apache.solr.common.util.NamedList;
import org.apache.solr.request.SolrQueryRequest;

/**
 * After an extended effort of working around the edismax limitations,
 * i finally gave up and am modifying the logic. The future should see
 * our own AQP implementation of edismax...
 */
public class AqpExtendedDismaxQParserPlugin extends QParserPlugin {
  public static final String NAME = "adismax";
  NamedList defaults = null;

  @Override
  public void init(NamedList args) {
    if (args.get("defaults", 0) != null) {
      NamedList defs = (NamedList) args.get("defaults");
      defaults = defs.clone();
    }
  }

  @Override
  public QParser createParser(String qstr, SolrParams localParams, SolrParams params, SolrQueryRequest req) {
    if (params.get("df", null) == null && defaults.get("df") != null) {
      params = new ModifiableSolrParams(params).set("df", (String) defaults.get("df"));
    }
    return new AqpExtendedDismaxQParser(qstr, localParams, params, req);
  }
}