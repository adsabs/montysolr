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

package org.apache.solr.handler.component;

import org.apache.solr.common.util.ContentStream;
import org.apache.solr.request.SolrQueryRequest;
import org.apache.solr.request.SolrQueryRequestBase;
import org.apache.solr.response.SolrQueryResponse;

/**
 *
 * Since SOLR 4.8 got this [booooo]terrific[/booooo] idea
 * of disallowing content streams for search handlers, 
 * we have to have this new handler which essentially
 * tricks {@link SearchHandler} into obedience
 *
 */
public class BigQuerySearchHandler extends SearchHandler
{

  @Override
  public void handleRequestBody(SolrQueryRequest req, SolrQueryResponse rsp) throws Exception
  {
  	Iterable<ContentStream> csms = req.getContentStreams();
  	if (csms != null) {
  		IgnoreFirstIteratorArrayList<ContentStream> il = new IgnoreFirstIteratorArrayList<ContentStream>();
  		for (ContentStream cs: csms) {
  			il.add(cs);
  		}
  		((SolrQueryRequestBase) req).setContentStreams(il);
  	}
  	super.handleRequestBody(req, rsp);
  }
}


