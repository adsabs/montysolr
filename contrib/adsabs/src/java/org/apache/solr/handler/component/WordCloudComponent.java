package org.apache.solr.handler.component;

import java.io.IOException;
import java.io.StringReader;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.LeafReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.Query;
import org.apache.lucene.util.BytesRef;
import org.apache.solr.common.params.SolrParams;
import org.apache.solr.common.util.StrUtils;
import org.apache.solr.request.SolrQueryRequest;
import org.apache.solr.schema.FieldType;
import org.apache.solr.schema.IndexSchema;
import org.apache.solr.schema.SchemaField;
import org.apache.solr.search.DocIterator;
import org.apache.solr.search.DocList;
import org.apache.solr.search.SolrIndexSearcher;

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

public class WordCloudComponent extends SearchComponent {

  public static final String COMPONENT_NAME = "wordcloud";
  
  @Override
  public void prepare(ResponseBuilder rb) throws IOException {
    SolrQueryRequest req = rb.req;
    SolrParams params = req.getParams();
    if (!params.getBool(COMPONENT_NAME, true)) {
      return;
    }
    
    Query query = rb.getQuery();
    if (query == null) return;
    
  }

  @Override
  public void process(ResponseBuilder rb) throws IOException {
    SolrQueryRequest req = rb.req;
    SolrParams params = req.getParams();
    if (!params.getBool(COMPONENT_NAME, true)) {
      return;
    }
    
    String wcFields = null;
    if ((wcFields = params.get("wordcloud.fl", null)) == null) {
      return;
    }
    
    Set<String> flds = new HashSet<String>(StrUtils.splitSmart(wcFields,','));
    DocList ids = rb.getResults().docList;
    
    SolrIndexSearcher searcher = rb.req.getSearcher();
    IndexSchema schema = rb.req.getCore().getLatestSchema();
    
    final Analyzer analyzer = rb.req.getCore().getLatestSchema().getIndexAnalyzer();
    final HashMap<String, FieldType> fieldsToLoad = new HashMap<String, FieldType>();
    
    CharTermAttribute termAtt;
    Map<String, Map<String, Integer>>tokens = new HashMap<String, Map<String, Integer>>();
    
    for (String f: flds) {
      SchemaField field = schema.getFieldOrNull(f);
      if (field==null || !field.stored()) {
        continue; // ignore this field
      }
      fieldsToLoad.put(f, field.getType());
      tokens.put(f, new HashMap<String, Integer>());
    }
    
    
      
    DocIterator iterator = ids.iterator();
    String w; Integer v;
    int sz = ids.size();
    for (int i=0; i<sz; i++) {
      int id = iterator.nextDoc();
      Document doc = searcher.doc(id, fieldsToLoad.keySet());
      for (Entry<String,FieldType> en: fieldsToLoad.entrySet()) {
        Map<String,Integer> toks = tokens.get(en.getKey());
        String[] vals = doc.getValues(en.getKey());
        FieldType fType = en.getValue();
        
        if (vals != null) {
          for (String s: vals) {
            TokenStream buffer = analyzer.tokenStream(en.getKey(), new StringReader(fType.indexedToReadable(s)));
            
            if (!buffer.hasAttribute(CharTermAttribute.class)) {
              continue; // empty stream
            }

            termAtt = buffer.getAttribute(CharTermAttribute.class);
            buffer.reset();

            while (buffer.incrementToken()) {
              w = termAtt.toString();
              v = toks.get(w);
              if (v == null) v = 0;
              toks.put(w, ++v);
            }
            
            buffer.close();
          }
        }
      }
    }
    
    // TODO: filter out the tokens (use some sort of a range 0.1-0.9 by frequency)
    
    LeafReader reader = searcher.getSlowAtomicReader();
    BytesRef term;
    int df;
    String f;
    
    Map<String, Map<String, Double>>docFreqs = new HashMap<String, Map<String, Double>>();
    for (Entry<String, Map<String, Integer>>field: tokens.entrySet()) {
      HashMap<String,Double> idfs = new HashMap<String, Double>();
      f = field.getKey();
      docFreqs.put(f, idfs);
      int N = reader.getDocCount(f);
      
      for (Entry<String, Integer>token: field.getValue().entrySet()) {
        w = token.getKey();
        df = reader.docFreq(new Term(f, new BytesRef(w)));
        if (df != 0) {
          idfs.put(w, Math.log10(N/df));
        }
      }
    }
    
    HashMap<String,Object> ret = new HashMap<String, Object>();
    for (String fi: fieldsToLoad.keySet()) {
      HashMap<String, Object> va = new HashMap<String, Object>();
      va.put("tf", tokens.get(fi));
      va.put("idf", docFreqs.get(fi));
      ret.put(fi, va);
    }
    rb.rsp.add("wordcloud", ret);
    
  }

  @Override
  public String getDescription() {
    return "return tokens with TF and IDF for wordcloud";
  }

  @Override
  public String getSource() {
    return null;
  }
  
}
