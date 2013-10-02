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
package org.apache.solr.response.transform;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.lucene.search.DictionaryRecIdCache;
import org.apache.lucene.search.FieldCache;
import org.apache.lucene.search.DictionaryRecIdCache.Str2LuceneId;
import org.apache.lucene.search.DictionaryRecIdCache.UnInvertedMap;
import org.apache.lucene.search.FieldCache.DocTerms;
import org.apache.lucene.util.BytesRef;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrException;
import org.apache.solr.common.SolrException.ErrorCode;
import org.apache.solr.common.params.SolrParams;
import org.apache.solr.common.util.NamedList;
import org.apache.solr.request.SolrQueryRequest;
import org.apache.solr.search.SolrCache;
import org.apache.solr.search.SolrIndexSearcher;

/**
 *
 * @since solr 4.0
 */
public class CitationsTransformerFactory extends TransformerFactory
{
	private String cacheName;
	private String resolutionField;

	@Override
	public void init(NamedList args) {
    defaultUserArgs = (String)args.get( "args" );
    cacheName = (String)args.get( "cache-name" );
    resolutionField = (String)args.get( "resolution-field" );
    
    assert cacheName != null;
    assert resolutionField != null;
    
  }
	
  @Override
  public DocTransformer create(String field, SolrParams params, SolrQueryRequest req) {
  	
    // TODO: once i have SolrCache implementation, just call the public 
  	// methods of the cache and remove this
  	
  	SolrIndexSearcher searcher = req.getSearcher();
  	Map<Integer, List<Integer>> references = null;
  	int[][] citations = null;
  	
		try {
	    references = DictionaryRecIdCache.INSTANCE.getCache(UnInvertedMap.MULTIVALUED, 
		    		searcher, 
		    		new String[] {"bibcode", "alternate_bibcode"}, 
		    		"reference");
	    citations = DictionaryRecIdCache.INSTANCE.getCache(DictionaryRecIdCache.UnInvertedArray.MULTIVALUED_STRING, 
						searcher, 
						new String[] {"bibcode", "alternate_bibcode"},
						"reference");
    } catch (IOException e) {
    	throw new SolrException(ErrorCode.SERVER_ERROR, "Cannot get citation/references data", e);
    }
  	
    DocTerms idMapping = null;
    if (params.getBool("resolve", false)) {
    	try {
	      idMapping = FieldCache.DEFAULT.getTerms(searcher.getAtomicReader(), resolutionField);
      } catch (IOException e) {
	      throw new SolrException(ErrorCode.SERVER_ERROR, "Cannot get data for resolving field: " + resolutionField, e);
      }
    }
    
    return new CitationsTransform( field,
    		//req.getSearcher().getCache(cacheName); TODO: use this in the future
    		references, 
    		citations,
    		params.get("counts", "citations,references"),
    		params.get("values", ""),
    		idMapping
    		);
  }
}

class CitationsTransform extends DocTransformer
{
  final String name;
	private String[] counts;
	private String[] values;
	private DocTerms idMapping;
	private SolrCache cache;
	private Map<Integer, List<Integer>> references;
	private int[][] citations;

  public CitationsTransform( String display,
  		//SolrCache cache,
  		Map<Integer, List<Integer>> references,
  		int[][] citations,
  		String counts, String values, 
  		DocTerms idMapping )
  {
    this.name = display;
    this.cache = cache;
    this.counts = counts.split("\\s*,\\s*");
    this.values = values.split("\\s*,\\s*");
    this.idMapping = idMapping;
    
    this.references = references;
    this.citations = citations;
    
  }

  @Override
  public String getName()
  {
    return name;
  }

  @Override
  public void transform(SolrDocument doc, int docid) {
    if( docid >= 0) {
      doc.setField( name, generate(doc, docid) );
    }
  }

	private Map generate(SolrDocument doc, int docid) {
	  HashMap<String, Object> data = new HashMap<String, Object>();
	  
	  if (counts.length > 0) {
	  	for (String f: counts) {
	  		if (f.equals("citations")) {
	  			data.put("num_citations", getCitationCount(doc, docid));
	  		}
	  		else if (f.equals("references")) {
	  			data.put("num_references", getReferenceCount(doc, docid));
	  		}
	  	}
	  	
	  	for (String f: values) {
	  		if (f.equals("citations")) {
	  			data.put("citations", getCitationValues(doc, docid));
	  		}
	  		else if (f.equals("references")) {
	  			data.put("references", getReferenceValues(doc, docid));
	  		}
	  	}
	  }
	  
	  
	  return data;
  }

	
	
	private List<String> getCitationValues(SolrDocument doc, int docid) {
		ArrayList<String> data = new ArrayList<String>();
		BytesRef ret = new BytesRef();
		if (citations.length >= docid) {
			for (Integer v: citations[docid]) {
				if (idMapping != null) {
					ret = idMapping.getTerm(v, ret);
					data.add(ret.utf8ToString());
				}
				else {
					data.add(Integer.toString(v));
				}
			}
		}
	  return data;
  }

	private List<String> getReferenceValues(SolrDocument doc, int docid) {
		ArrayList<String> data = new ArrayList<String>();
		BytesRef ret = new BytesRef();
		if (references.containsKey(docid)) {
			for (Integer v: references.get(docid)) {
				if (idMapping != null) {
					ret = idMapping.getTerm(v, ret);
					data.add(ret.utf8ToString());
				}
				else {
					data.add(Integer.toString(v));
				}
			}
		}
	  return data;
  }

	private int getCitationCount(SolrDocument doc, int docid) {
	  if (citations.length >= docid) {
	  	return citations[docid].length;
	  }
	  else {
	  	return 0;
	  }
  }

	private int getReferenceCount(SolrDocument doc, int docid) {
		if (references.containsKey(docid)) {
			return references.get(docid).size();
		}
		else {
			return 0;
		}
  }
  
  
  
}


