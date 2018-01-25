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

import static org.apache.solr.core.RequestParams.USEPARAM;

import java.lang.invoke.MethodHandles;
import java.util.concurrent.atomic.AtomicLong;

import org.apache.solr.common.SolrException;
import org.apache.solr.common.util.ContentStream;
import org.apache.solr.common.util.NamedList;
import org.apache.solr.core.PluginInfo;
import org.apache.solr.request.SolrQueryRequest;
import org.apache.solr.request.SolrQueryRequestBase;
import org.apache.solr.response.SolrQueryResponse;
import org.apache.solr.search.SyntaxError;
import org.apache.solr.util.SolrPluginUtils;
import org.apache.solr.util.stats.Timer;
import org.apache.solr.util.stats.TimerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

 //Statistics
 private final AtomicLong numRequests = new AtomicLong();
 private final AtomicLong numServerErrors = new AtomicLong();
 private final AtomicLong numClientErrors = new AtomicLong();
 private final AtomicLong numTimeouts = new AtomicLong();
 private final Timer requestTimes = new Timer();
 private static final Logger log = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
 
 
  
  @Override
  public void handleRequest(SolrQueryRequest req, SolrQueryResponse rsp) {
    PluginInfo pluginInfo = this.getPluginInfo();
    numRequests.incrementAndGet();
    TimerContext timer = requestTimes.time();
    try {
      if(pluginInfo != null && pluginInfo.attributes.containsKey(USEPARAM)) req.getContext().put(USEPARAM,pluginInfo.attributes.get(USEPARAM));
      
      Iterable<ContentStream> csms = req.getContentStreams();
      if (csms != null) {
        IgnoreFirstIteratorArrayList<ContentStream> il = new IgnoreFirstIteratorArrayList<ContentStream>();
        for (ContentStream cs: csms) {
          il.add(cs);
          log.debug("processing content stream:" + cs.getContentType() + " " + cs.getSize());
        }
        ((SolrQueryRequestBase) req).setContentStreams(il);
      }
      
      SolrPluginUtils.setDefaults(this, req, defaults, appends, invariants);
      
      req.getContext().remove(USEPARAM);
      rsp.setHttpCaching(httpCaching);
      handleRequestBody( req, rsp );
      // count timeouts
      NamedList header = rsp.getResponseHeader();
      if(header != null) {
        Object partialResults = header.get(SolrQueryResponse.RESPONSE_HEADER_PARTIAL_RESULTS_KEY);
        boolean timedOut = partialResults == null ? false : (Boolean)partialResults;
        if( timedOut ) {
          numTimeouts.incrementAndGet();
          rsp.setHttpCaching(false);
        }
      }
    } catch (Exception e) {
      boolean incrementErrors = true;
      boolean isServerError = true;
      if (e instanceof SolrException) {
        SolrException se = (SolrException)e;
        if (se.code() == SolrException.ErrorCode.CONFLICT.code) {
          incrementErrors = false;
        } else if (se.code() >= 400 && se.code() < 500) {
          isServerError = false;
        }
      } else {
        if (e instanceof SyntaxError) {
          isServerError = false;
          e = new SolrException(SolrException.ErrorCode.BAD_REQUEST, e);
        }
      }

      rsp.setException(e);

      if (incrementErrors) {
        SolrException.log(log, e);

        if (isServerError) {
          numServerErrors.incrementAndGet();
        } else {
          numClientErrors.incrementAndGet();
        }
      }
    }
    finally {
      timer.stop();
    }
  }
}


