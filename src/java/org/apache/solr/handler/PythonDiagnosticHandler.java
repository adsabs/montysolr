package org.apache.solr.handler;

import invenio.montysolr.jni.PythonMessage;
import invenio.montysolr.jni.MontySolrVM;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Map;

import org.apache.solr.common.params.CommonParams;
import org.apache.solr.common.params.ModifiableSolrParams;
import org.apache.solr.common.params.SolrParams;
import org.apache.solr.common.util.NamedList;
import org.apache.solr.core.SolrCore;
import org.apache.solr.request.SolrQueryRequest;
import org.apache.solr.response.SolrQueryResponse;
import org.apache.solr.request.SolrRequestHandler;
import org.apache.solr.search.DocSlice;
import org.apache.solr.util.DictionaryCache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;




public class PythonDiagnosticHandler extends RequestHandlerBase {

	public static final Logger log = LoggerFactory
			.getLogger(PythonDiagnosticHandler.class);


	public void handleRequestBody(SolrQueryRequest req, SolrQueryResponse rsp)
			throws Exception {
		SolrParams params = req.getParams();
		String q = params.get(CommonParams.Q);

		log.info("======= start diagnostics =======");

		PythonMessage message = MontySolrVM.INSTANCE
			.createMessage("diagnostic_test")
			.setParam("query", q);

		try {
			MontySolrVM.INSTANCE.sendMessage(message);
		} catch (InterruptedException e) {
			e.printStackTrace();
			throw new IOException("Error searching Invenio!");
		}

		Object result = message.getResults();
		if (result != null) {
			String res = (String) result;
			rsp.add("diagnostic_message", res);
			log.info("Diagnostic message: \n" + res);
		}
		else {
			log.info("Diagnostic message: null");
		}

		// run invenio queries
		SolrCore core = req.getCore();
		SolrRequestHandler handler = core.getRequestHandler( "/invenio" );
		if (handler==null) {
			log.error("/invenio handler is not registered");
		}
		else {
			
		
			String[] queries = {"boson", "title:boson", "inv_title:boson", "a*", "{!iq iq.mode=maxinv}title:boson", "year:1->99999999"};
			
			
			String qu = null;
			Object pyresult = null;
			int[] recids = null;
			String r1 = null;
			String r2 = null;
	
			Map<Integer, Integer> recidToDocid = DictionaryCache.INSTANCE.getTranslationCache(req.getSearcher().getReader(),
					req.getSchema().getUniqueKeyField().getName());
	
			for (int i=0; i<queries.length; i++) {
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				PrintStream ps = new PrintStream(baos);
	
	
				try {
					qu = queries[i];
					ModifiableSolrParams new_params = new ModifiableSolrParams(req.getParams());
					SolrQueryResponse new_rsp = new SolrQueryResponse();
					new_params.set("q", qu);
					req.setParams(new_params);
					handler.handleRequest(req, new_rsp);
					NamedList values = new_rsp.getValues();
					r1 = "Calling InvenioHandler " +
						"query=" + qu + ", translated query=" + values.get("inv_query") + " number of hits=" + ((DocSlice) values.get("response")).matches();
	
	
					r2 = "Calling Invenio [directly through Python] " +
						"query=," + qu;
	
					message = MontySolrVM.INSTANCE
						.createMessage("perform_request_search_ints")
						.setSender("InvenioQuery").setParam("query", qu);
	
					try {
						MontySolrVM.INSTANCE.sendMessage(message);
					} catch (InterruptedException e) {
						e.printStackTrace(ps);
						r1 += baos.toString();
						log.error(baos.toString());
						baos.reset();
					}
	
					pyresult = message.getResults();
					if (pyresult != null) {
						recids = (int[]) pyresult;
						r2 += " retrieved=" + recids.length + " total hits=" + ((Integer) message.getParam("total"));
						try {
							for (int r: recids ) {
								recidToDocid.get(r);
							}
						}
						catch (Exception e) {
							r2 += "WARNING: Invenio returned recids that are not in Solr index! Go to /invenio_update to fix that";
						}
					}
					else {
						r2 += " retrieved=" + null + " total hits=" + null;
					}
	
					rsp.add("query=" + qu, r1);
					log.info("query=" + qu + "\n" + r1);
					rsp.add("python query=" + qu, r2);
					log.info("python query=" + qu + "\n" + r2);
				}
				catch (Exception e) {
					e.printStackTrace(ps);
					rsp.add("query=" + qu, baos.toString());
					log.error(baos.toString());
				}
				log.info("--");
			}
	
			String defaultField = req.getSchema().getUniqueKeyField().getName();
			int[] lCache = DictionaryCache.INSTANCE.getLuceneCache(req.getSearcher().getReader(),
					defaultField);
			String rinfo = "Lucene knows about " + recidToDocid.size() + " recids." + "[inside field \"" + defaultField + "\"]...";
			Integer d = (lCache.length / 2);
			if(lCache.length > 0) {
				rinfo += "[0]=" + lCache[0] + ", [" + d + "]=" + lCache[d] + ", [" + (lCache.length-1) + "]=" + lCache[lCache.length-1];
			}
			else {
				rinfo += " the cache is empty. You should visit /invenio_update";
			}
			rsp.add("recids", rinfo);
			log.info(rinfo);
		}
		
		log.info("======== end diagnostics ========");

	}




	@Override
	public String getDescription() {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public String getSourceId() {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public String getSource() {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public String getVersion() {
		// TODO Auto-generated method stub
		return null;
	}


}
