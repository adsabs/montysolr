package org.apache.solr.handler.component;

import invenio.montysolr.jni.PythonMessage;
import invenio.montysolr.jni.MontySolrVM;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.lucene.search.Query;
import org.apache.solr.common.SolrException;
import org.apache.solr.common.params.ModifiableSolrParams;
import org.apache.solr.common.params.SolrParams;
import org.apache.solr.common.util.NamedList;
import org.apache.solr.core.PluginInfo;
import org.apache.solr.core.SolrConfig;
import org.apache.solr.request.SolrQueryRequest;
import org.apache.solr.response.SolrQueryResponse;
import org.apache.solr.schema.IndexSchema;
import org.apache.solr.search.DocIterator;
import org.apache.solr.search.DocList;
import org.apache.solr.search.DocListAndSet;
import org.apache.solr.search.DocSlice;
import org.apache.solr.search.QParserPlugin;
import org.apache.solr.search.SolrIndexReader;
import org.apache.solr.search.SortSpec;
import org.apache.lucene.search.DictionaryRecIdCache;


public class InvenioFormatter extends SearchComponent
{
	public static final String COMPONENT_NAME = "invenio-formatter";
	private boolean activated = false;
	private Map<String, String> invParams = null;
	private String idField = null;

	@SuppressWarnings("unchecked")
	@Override
	public void prepare(ResponseBuilder rb) throws IOException {
		activated = false;
		SolrParams params = rb.req.getParams();
		Map<Object, Object> context = rb.req.getContext();

		if (context.containsKey("inv.params")) {
			Object par = context.get("inv.params");
			if (par != null) {
				invParams = (Map<String, String>) par;
				if (invParams.containsKey("of")) {
					String of = invParams.get("of");
					if (of.equals("hcs")) { // citation summary
						ModifiableSolrParams rawParams = new ModifiableSolrParams(rb.req.getParams());
						Integer old_limit = params.getInt("rows", 10);
						int max_len = params.getInt("inv.rows", 25000);
						rawParams.set("rows", max_len);
						rawParams.set("old_rows", old_limit);
						rb.req.setParams(rawParams);
						SortSpec sortSpec = rb.getSortSpec();
						SortSpec nss = new SortSpec(sortSpec.getSort(), sortSpec.getOffset(), max_len);
						rb.setSortSpec(nss);
						activated = true;
					}
					else if(invParams.containsKey("rm") && invParams.get("rm").length() > 0) {
						activated = true;
					}
					else if(invParams.containsKey("sf") && invParams.get("sf").length() > 0) {
						activated = true;
					}
				}
			}
		}

	}

	@Override
	public void process(ResponseBuilder rb) throws IOException {

		if ( activated ) {
			SolrParams params = rb.req.getParams();
			Integer original_limit = params.getInt("old_rows", params.getInt("rows"));

			SolrQueryRequest req = rb.req;
			SolrQueryResponse rsp = rb.rsp;

			DocListAndSet results = rb.getResults();
			DocList dl = results.docList;

			if (dl.size() < 1) {
				return;
			}

			int[] recids = new int[dl.size()];
			DocIterator it = dl.iterator();

			SolrIndexReader reader = rb.req.getSearcher().getReader();
			int[] docidMap = DictionaryRecIdCache.INSTANCE.getLuceneCache(reader, getIdField(rb.req));

			// translate into Invenio ID's
			for (int i=0;it.hasNext();i++) {
				recids[i] = docidMap[it.next()];
			}

			PythonMessage message = MontySolrVM.INSTANCE.createMessage("sort_and_format")
				.setSender("InvenioFormatter")
				.setSolrQueryRequest(req)
				.setSolrQueryResponse(rsp)
				.setParam("recids", recids)
				.setParam("kwargs", invParams);

			MontySolrVM.INSTANCE.sendMessage(message);

			Object result = message.getResults();
			String t = (String) message.getParam("rtype");
			if (result != null && t.contains("str")) {
				rb.rsp.add("inv_response", (String)result);

				// truncate the number of retrieved documents back into reasonable size
				int[] luceneIds = new int[original_limit>dl.size() ? dl.size() : original_limit];
				it = dl.iterator();
				for (int i=0;i<luceneIds.length && it.hasNext();i++) {
					luceneIds[i] = it.next();
				}
				DocListAndSet res = new DocListAndSet();
				res.docList = new DocSlice(results.docList.offset(), luceneIds.length, luceneIds, null, dl.matches(), results.docList.maxScore());
				rb.setResults( res );

				NamedList vals = rsp.getValues();
				vals.remove("response");
			    rsp.add("response",rb.getResults().docList);
			}
			else {
				Map<Integer, Integer> recidToDocid = DictionaryRecIdCache.INSTANCE
						.getTranslationCache(reader, rb.req.getSchema().getUniqueKeyField().getName());
				int[] recs = (int[]) result;

				// truncate the number of retrieved documents back into reasonable size
				int[] luceneIds = new int[original_limit>recs.length ? recs.length : original_limit];
				for (int i=0;i<luceneIds.length;i++) {
					luceneIds[i] = recidToDocid.get(recs[i]);
				}
				DocListAndSet res = new DocListAndSet();
				res.docList = new DocSlice(results.docList.offset(), luceneIds.length, luceneIds, null, dl.matches(), results.docList.maxScore());
				rb.setResults( res );

				NamedList vals = rsp.getValues();
				vals.remove("response");
			    rsp.add("response",rb.getResults().docList);
				}

			}

		rb.rsp.add("inv_query", rb.getQuery().toString());

	}
	
	private String getIdField(SolrQueryRequest req) {
		if (idField  != null) {
			return idField;
		}

		SolrConfig config = req.getCore().getSolrConfig();
		PluginInfo pluginInfo = null;
		for (PluginInfo info : config.getPluginInfos(SearchComponent.class.getCanonicalName())) {
			if (info.className.contains(this.getClass().getSimpleName())) {
				pluginInfo = info;
				break;
			}
		}
		if (pluginInfo == null) {
			throw new SolrException(SolrException.ErrorCode.SERVICE_UNAVAILABLE,
					"Configuration error, InvenioFormatter not found");
		}
		Object o = pluginInfo.initArgs.get("defaults");
		SolrParams defaults;
		if (o != null && o instanceof NamedList) {
			defaults = SolrParams.toSolrParams((NamedList) o);
			idField = defaults.get("idfield");
			
			IndexSchema schema = req.getSchema();
			if (idField == null || !schema.hasExplicitField(idField)) {
				throw new SolrException(SolrException.ErrorCode.SERVICE_UNAVAILABLE,
						"Wrong configuration, the required idfield is missing or not present in schema. "
					  + "We cannot map external ids onto lucene docids without it.");
			}
			
			return idField;
		} else {
			throw new SolrException(SolrException.ErrorCode.SERVER_ERROR,
					"Wrong configuration, missing idfield");
		}
	}

	@Override
	public String getDescription() {
		return "Invenio result list formatter";
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
