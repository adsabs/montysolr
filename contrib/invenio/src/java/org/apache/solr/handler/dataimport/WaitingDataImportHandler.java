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
package org.apache.solr.handler.dataimport;

import org.apache.solr.common.params.ModifiableSolrParams;
import org.apache.solr.common.params.SolrParams;
import org.apache.solr.common.util.NamedList;
import org.apache.solr.request.SolrQueryRequest;
import org.apache.solr.response.SolrQueryResponse;

/**
 * Just a small class to make sure the request is always in a synchronized mode
 * and also that we use the NoRollbackWriter.
 *
 * If the SOLR-3671 is accepted, we can subclass DataImportHandler
 */
 
public class WaitingDataImportHandler extends FixedDataImportHandler {
  
  private String writerImpl;
  
  @Override
  @SuppressWarnings("unchecked")
  public void init(NamedList args) {
    super.init(args);
    
    writerImpl = NoRollbackWriter.class.getName();
    
    if (args.get("defaults") == null) {
      return;
    }
    NamedList defs = (NamedList) args.get("defaults");
    if (defs.get("writerImpl") != null) {
      writerImpl = (String) defs.get("writerImpl");
    }
    
  }
  
  @Override
  @SuppressWarnings("unchecked")
  public void handleRequestBody(SolrQueryRequest req, SolrQueryResponse rsp)
          throws Exception {
    
    SolrParams params = req.getParams();
    ModifiableSolrParams newParams = new ModifiableSolrParams(params);
    newParams.set("writerImpl", newParams.get("writerImpl", writerImpl));
    newParams.set("synchronous", true);
    req.setParams(newParams);
    
    super.handleRequestBody(req, rsp);      
    
  }
  
}
