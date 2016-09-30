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

import org.apache.lucene.index.BinaryDocValues;
import org.apache.lucene.util.BytesRef;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrException;
import org.apache.solr.common.SolrException.ErrorCode;
import org.apache.solr.common.params.SolrParams;
import org.apache.solr.common.util.NamedList;
import org.apache.solr.request.SolrQueryRequest;
import org.apache.solr.search.CitationLRUCache;
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
  	
  	SolrIndexSearcher searcher = req.getSearcher();
  	CitationLRUCache<Object,Integer> cache = (CitationLRUCache<Object,Integer>) searcher.getCache(cacheName);
  	
  	if (cache == null) {
  		throw new SolrException(ErrorCode.SERVER_ERROR, "Cannot find cache: " + cacheName);
  	}
  	
  	Map<Integer, List<Integer>> references = null;
  	int[][] citations = null;
  	
  	
    BinaryDocValues idMapping = null;
    if (params.getBool("resolve", false)) {
    	try {
	      idMapping = req.getSearcher().getLeafReader().getSortedDocValues(resolutionField);
      } catch (IOException e) {
	      throw new SolrException(ErrorCode.SERVER_ERROR, "Cannot get data for resolving field: " + resolutionField, e);
      }
    }
    
    return new CitationsTransform( field,
    		cache,
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
	private BinaryDocValues idMapping;
	private CitationLRUCache<Object,Integer> cache;

  public CitationsTransform( String display,
  		CitationLRUCache<Object,Integer> cache,
  		String counts, String values, 
  		BinaryDocValues idMapping )
  {
    this.name = display;
    this.cache = cache;
    this.counts = counts.split("\\s*,\\s*");
    this.values = values.split("\\s*,\\s*");
    this.idMapping = idMapping;
    
    
  }

  @Override
  public String getName()
  {
    return name;
  }

  @Override
  public void transform(SolrDocument doc, int docid, float score) {
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
		int[] citations = cache.getCitations(docid);
		
		if (citations != null) {
			for (int i=0;i<citations.length;i++) {
				if (citations[i] < 0) // unresolved refs = -1
					continue;
				if (idMapping != null) {
					ret = idMapping.get(citations[i]);
					data.add(ret.utf8ToString());
				}
				else {
					data.add(Integer.toString(citations[i]));
				}
			}
		}
	  return data;
  }

	private List<String> getReferenceValues(SolrDocument doc, int docid) {
		ArrayList<String> data = new ArrayList<String>();
		BytesRef ret = new BytesRef();
		int[] references = cache.getReferences(docid);
		
		if (references != null) {
			for (int i=0;i<references.length;i++) {
				if (references[i] < 0) // unresolved refs = -1
					continue;
				if (idMapping != null) {
					ret = idMapping.get(references[i]);
					data.add(ret.utf8ToString());
				}
				else {
					data.add(Integer.toString(references[i]));
				}
			}
		}
	  return data;
  }

	private int getCitationCount(SolrDocument doc, int docid) {
		int[] v = cache.getCitations(docid);
		if (v != null) {
			return _count(v);
		}
		else {
			return 0;
		}
  }

	private int _count(int[] v) {
		int res = 0;
		for (int x: v) {
			if (x >= 0)
				res++;
		}
		return res;
  }

	private int getReferenceCount(SolrDocument doc, int docid) {
		int[] v = cache.getReferences(docid);
		if (v != null) {
			return _count(v);
		}
		else {
			return 0;
		}
  }
  
  
  
}


