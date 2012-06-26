package org.apache.solr.search;

import invenio.montysolr.jni.MontySolrVM;
import invenio.montysolr.jni.PythonMessage;

import java.io.IOException;
import java.util.BitSet;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.CitationQueryCites;
import org.apache.lucene.search.CitationQueryFilter;
import org.apache.lucene.search.CitedByCollector;
import org.apache.lucene.search.CollectorQuery;
import org.apache.lucene.search.DictionaryRecIdCache;
import org.apache.lucene.search.FieldCache;
import org.apache.lucene.search.Filter;
import org.apache.lucene.search.Query;
import org.apache.solr.common.params.CommonParams;
import org.apache.solr.common.params.SolrParams;
import org.apache.solr.request.SolrQueryRequest;

public class CitationQParser extends QParser {
	String sortStr;
	SolrQueryParser lparser;
	private String dictName = null;
	private String idField = null;
	
	public CitationQParser(String qstr, SolrParams localParams,
			SolrParams params, SolrQueryRequest req) {
		super(qstr, localParams, params, req);
	}

	public Query parse() throws ParseException {
		String qstr = getString();

		String defaultField = getParam(CommonParams.DF);
		if (defaultField == null) {
			defaultField = getReq().getSchema().getDefaultSearchFieldName();
		}
		lparser = new SolrQueryParser(this, defaultField);

		// these could either be checked & set here, or in the SolrQueryParser
		// constructor
		String opParam = getParam(QueryParsing.OP);
		if (opParam != null) {
			lparser.setDefaultOperator("AND".equals(opParam) ? QueryParser.Operator.AND
					: QueryParser.Operator.OR);
		} else {
			// try to get default operator from schema
			QueryParser.Operator operator = getReq().getSchema()
					.getSolrQueryParser(null).getDefaultOperator();
			lparser.setDefaultOperator(null == operator ? QueryParser.Operator.OR
					: operator);
		}

		Query mainq = lparser.parse(qstr);
		
		String rel = getParam("rel");
		if (rel == null) {
			rel = defaultField;
		}
		
		idField = params.get("idField");
		
		String type = params.get("rel");
		if (type.contains("refersto")) {
			dictName = "citationdict";
		}
		else if (type.contains("citedby")) {
			dictName = "reversedict";
		}
		
		
		if (rel.equals("citedby")) {
			Filter qfilter = new CitationQueryFilter();
			//TODO: the collector class doesn't work
			try {
				return new CollectorQuery(mainq, getReq().getSearcher().getReader(), 
						CollectorQuery.createCollector(CitedByCollector.class, qfilter, null));
			} catch (Exception e) {
				throw new ParseException("Meeeek, server error, lazy coders!!!");
			}
		}
		else if (rel.equals("cites")) {
			try {
				return new CitationQueryCites(mainq, getDictCache());
			} catch (IOException e) {
				throw new ParseException(e.getLocalizedMessage());
			}
		}
		else {
			throw new ParseException("Unknown relation: " + rel);
		}
		
		
	}

	public String[] getDefaultHighlightFields() {
		return new String[] { lparser.getField() };
	}
	
	
	public Map<Integer, int[]> getDictCache() throws IOException {
		try {
			return getDictCache(this.dictName, this.idField);
		} catch (IOException e) {
			e.printStackTrace();
			// return empty map, that is ok because it will affect only
			// this query, the next will get a new cache
			return new HashMap<Integer, int[]>();
		}
	}
	
	public Map<Integer, int[]> getDictCache(String dictName, String idField) throws IOException {


		Map<Integer, int[]> cache = DictionaryRecIdCache.INSTANCE.getCache(dictName);


		if (cache == null) {



			// Get mapping lucene_id->invenio_recid
			// The simplest would be to load the field with a cache (but the
			// field should be integer - and it is not now). The other reason
			// for doint this is that we don't create unnecessary cache

			/**

			TermDocs td = reader.termDocs(); //FIXME: .termDocs(new Term(idField)) works not?!
			String[] li =  {idField};
			MapFieldSelector fieldSelector = new MapFieldSelector(li);
			**/

			SolrIndexSearcher searcher = req.getSearcher();
			SolrIndexReader reader = searcher.getReader();
		    int[] idMapping = FieldCache.DEFAULT.getInts(reader, idField);

			Map<Integer, Integer> fromValueToDocid = new HashMap<Integer, Integer>(idMapping.length);
			int i = 0;
			for (int value: idMapping) {
				fromValueToDocid.put(value, i);
				i++;
			}

			/**
			//OpenBitSet bitSet = new OpenBitSet(reader.maxDoc());
			int i;
			while (td.next()) {
				i = td.doc();
				// not needed when term is null
				//if (reader.isDeleted(i)) {
				//	continue;
				//}
				Document doc = reader.document(i);

				try {
					//bitSet.set(Integer.parseInt(doc.get(idField)));
					idMap.put(i, Integer.parseInt(doc.get(idField)));
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			**/

			// now get the citation dictionary from Invenio
			HashMap<Integer, int[]> hm = new HashMap<Integer, int[]>();

			PythonMessage message = MontySolrVM.INSTANCE.createMessage("get_citation_dict")
						.setSender("CitationQuery")
						.setParam("dictname", dictName)
						.setParam("result", hm);
			MontySolrVM.INSTANCE.sendMessage(message);


			Map<Integer, int[]> citationDict = new HashMap<Integer, int[]>(0);
			if (message.containsKey("result")) {

				@SuppressWarnings("unchecked")
				Map<Integer, Object> result = (Map<Integer, Object>) message.getResults();
				citationDict = new HashMap<Integer, int[]>(result.size());
				for (Entry<Integer, Object> e: result.entrySet()) {
					Integer recid = e.getKey();
					if (fromValueToDocid.containsKey(recid)) {
						// translate recids into lucene-ids

			            int[] recIds = (int[]) e.getValue();
			            int[] lucIds = new int[recIds.length];
						for (int x=0;x<recIds.length;x++) {
							if (fromValueToDocid.containsKey(recIds[x]))
								lucIds[x] = fromValueToDocid.get(recIds[x]);
						}
						citationDict.put(recid, (int[]) e.getValue());
					}
				}
			}

			DictionaryRecIdCache.INSTANCE.setCache(dictName, citationDict);
			return citationDict;
		}
		return cache;
	}

	public Map<Integer, BitSet> getDictCacheX() {
    	HashMap<Integer, BitSet> hm = new HashMap<Integer, BitSet>();

    	int Min = 1;
    	int Max = 5000;
    	int r;
    	for (int i=0;i<5000; i++) {
    		r = Min + (int)(Math.random() * ((Max - Min) + 1));
    		BitSet bs = new BitSet(r);
    		int ii = 0;
    		while (ii < 20) {
    			r = Min + (int)(Math.random() * ((Max - Min) + 1));
    			bs.set(r);
    			ii += 1;
    		}
    		hm.put(i, bs);
    	}
    	return hm;
    }


}

