package org.apache.solr.update;

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


import invenio.montysolr.jni.PythonMessage;
import invenio.montysolr.jni.MontySolrVM;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.apache.lucene.document.Field;
import org.apache.solr.common.SolrException;
import org.apache.solr.common.SolrInputDocument;
import org.apache.solr.common.params.SolrParams;
import org.apache.solr.core.SolrCore;
import org.apache.solr.handler.RequestHandlerBase;
import org.apache.solr.request.SolrQueryRequest;
import org.apache.solr.response.SolrQueryResponse;
import org.apache.solr.schema.IndexSchema;
import org.apache.solr.util.DictionaryCache;



/**
 * Ping solr core
 *
 * @since solr 1.3
 */
public class InvenioKeepRecidUpdated extends RequestHandlerBase
{
  @Override
  public void handleRequestBody(SolrQueryRequest req, SolrQueryResponse rsp) throws Exception
  {
    SolrParams params = req.getParams();
    SolrParams required = params.required();
    SolrCore core = req.getCore();
    IndexSchema schema = req.getSchema();

    UpdateHandler updateHandler = core.getUpdateHandler();


    long start = System.currentTimeMillis();

    AddUpdateCommand addCmd = new AddUpdateCommand();
    addCmd.allowDups = false;
    addCmd.overwriteCommitted = false;
    addCmd.overwritePending = false;



    int last_recid = -1; // -1 means get the first created doc

    if (params.getInt("last_recid") != null) {
    	last_recid = params.getInt("last_recid");
    }
    else {
    	int[] ids = DictionaryCache.INSTANCE.getLuceneCache(req.getSearcher().getReader(), schema.getUniqueKeyField().getName());
	    for(int m: ids) {
	    	if (m > last_recid) {
	    		last_recid = m;
	    	}
	    }
    }

    rsp.add("last_recid", last_recid);


	Map<String, int[]> dictData;

	if (params.getBool("generate", false)) {
		Integer max_recid = params.getInt("max_recid", 0);
		if (max_recid == 0 || max_recid < last_recid) {
			throw new SolrException(SolrException.ErrorCode.BAD_REQUEST, "The max_recid parameter missing!");
		}

		dictData = new HashMap<String, int[]>();
		int[] a = new int[max_recid-last_recid];
		for (int i=0, ii=last_recid+1;ii<max_recid+1;i++,ii++) {
			a[i] = ii;
		}
		dictData.put("ADDED", a);
	}
	else {
		// get recids from Invenio
	    // TODO: get updates by a timestamp
	    PythonMessage message = MontySolrVM.INSTANCE
			.createMessage("get_recids_changes").setSender(this.getClass().getSimpleName())
			.setSolrQueryRequest(req).setSolrQueryResponse(rsp)
			.setParam("last_recid", last_recid);
		MontySolrVM.INSTANCE.sendMessage(message);

		Object results = message.getResults();
		if (results == null ) {
			rsp.add("message", "Invenio returned null. We cannot update anything.");
			return;
		}
		dictData = (HashMap<String, int[]>) results;
	}


	if (dictData.containsKey("ADDED")) {
    int[] recids = dictData.get("ADDED");
	    // create new documentns, they will have only recids, but that's OK (for some
	    // people), sigh...
	    if (recids.length > 0) {
		    SolrInputDocument doc = null;
		    for (int i=0; i<recids.length; i++) {
		    	doc = new SolrInputDocument();
		    	doc.addField(schema.getUniqueKeyField().getName(), recids[i]);
		    	addCmd.doc = DocumentBuilder.toDocument(doc, schema);
		        updateHandler.addDoc(addCmd);
		    }
	    }
	    CommitUpdateCommand updateCmd = new CommitUpdateCommand(false);
	    updateHandler.commit(updateCmd);
	    rsp.add("added", recids.length);
  	}

    long end = System.currentTimeMillis();



    rsp.add( "status", "OK" );
    rsp.add( "QTime", end-start);
  }


  //////////////////////// SolrInfoMBeans methods //////////////////////

  @Override
  public String getVersion() {
    return "";
  }

  @Override
  public String getDescription() {
    return "Updates the Invenio recid with the missing/new docs (if any)";
  }

  @Override
  public String getSourceId() {
    return "";
  }

  @Override
  public String getSource() {
    return "";
  }
}
