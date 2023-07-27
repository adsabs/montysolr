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

import org.apache.lucene.index.LeafReaderContext;
import org.apache.lucene.index.ReaderUtil;
import org.apache.lucene.queries.function.FunctionValues;
import org.apache.lucene.queries.function.ValueSource;
import org.apache.lucene.search.IndexSearcher;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrException;
import org.apache.solr.common.SolrException.ErrorCode;
import org.apache.solr.common.params.SolrParams;
import org.apache.solr.common.util.NamedList;
import org.apache.solr.request.SolrQueryRequest;
import org.apache.solr.schema.SchemaField;
import org.apache.solr.search.CitationCache;
import org.apache.solr.search.SolrIndexSearcher;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @since solr 4.0
 */
public class CitationsTransformerFactory extends TransformerFactory {
    private String cacheName;
    private String resolutionField;

    @Override
    public void init(NamedList args) {
        defaultUserArgs = (String) args.get("args");
        cacheName = (String) args.get("cache-name");
        resolutionField = (String) args.get("resolution-field");

        assert cacheName != null;
        assert resolutionField != null;

    }

    @Override
    public DocTransformer create(String field, SolrParams params, SolrQueryRequest req) {

        SolrIndexSearcher searcher = req.getSearcher();
        CitationCache<Object, Integer> cache = (CitationCache<Object, Integer>) searcher.getCache(cacheName);

        if (cache == null) {
            throw new SolrException(ErrorCode.SERVER_ERROR, "Cannot find cache: " + cacheName);
        }

        return new CitationsTransform(field,
                cache,
                params.get("counts", "citations,references"),
                params.get("values", ""),
                params.getBool("resolve", false) ? searcher : null
        );
    }
}

class CitationsTransform extends DocTransformer {
    final String name;
    private final String[] counts;
    private final String[] values;
    private final CitationCache<Object, Integer> cache;
    private final SolrIndexSearcher searcher;
    private ValueSourceAccessor vs;

    public CitationsTransform(String display,
                              CitationCache<Object, Integer> cache,
                              String counts, String values,
                              SolrIndexSearcher searcher
    ) {
        this.name = display;
        this.cache = cache;
        this.counts = counts.split("\\s*,\\s*");
        this.values = values.split("\\s*,\\s*");
        this.searcher = searcher;


    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void transform(SolrDocument doc, int docid) {
        if (docid >= 0) {
            try {
                doc.setField(name, generate(doc, docid));
            } catch (IOException e) {
                throw new SolrException(SolrException.ErrorCode.SERVER_ERROR, e);
            }
        }
    }

    private Map generate(SolrDocument doc, int docid) throws IOException {
        HashMap<String, Object> data = new HashMap<String, Object>();

        if (counts.length > 0) {
            for (String f : counts) {
                if (f.equals("citations")) {
                    data.put("num_citations", getCitationCount(doc, docid));
                } else if (f.equals("references")) {
                    data.put("num_references", getReferenceCount(doc, docid));
                }
            }

            for (String f : values) {
                if (f.equals("citations")) {
                    data.put("citations", getCitationValues(doc, docid));
                } else if (f.equals("references")) {
                    data.put("references", getReferenceValues(doc, docid));
                }
            }
        }


        return data;
    }


    private List<String> getCitationValues(SolrDocument doc, int docid) throws IOException {
        ArrayList<String> data = new ArrayList<String>();
        int[] citations = cache.getCitations(docid);

        if (citations != null) {
            for (int i = 0; i < citations.length; i++) {
                if (citations[i] < 0) // unresolved refs = -1
                    continue;
                data.add(getValue(citations[i]));
            }
        }
        return data;
    }

    private String getValue(int docId) throws IOException {

        if (searcher != null) {
            if (vs == null) {
                SchemaField idSchemaField = searcher.getSchema().getField("bibcode");
                vs = new ValueSourceAccessor(searcher, idSchemaField.getType().getValueSource(idSchemaField, null));
            }
            return vs.objectVal(docId).toString();
        } else {
            return Integer.toString(docId);
        }

    }

    private List<String> getReferenceValues(SolrDocument doc, int docid) throws IOException {
        ArrayList<String> data = new ArrayList<String>();
        int[] references = cache.getReferences(docid);

        if (references != null) {
            for (int i = 0; i < references.length; i++) {
                if (references[i] < 0) // unresolved refs = -1
                    continue;
                data.add(getValue(references[i]));
            }
        }
        return data;
    }

    private int getCitationCount(SolrDocument doc, int docid) {
        int[] v = cache.getCitations(docid);
        if (v != null) {
            return _count(v);
        } else {
            return 0;
        }
    }

    private int _count(int[] v) {
        int res = 0;
        for (int x : v) {
            if (x >= 0)
                res++;
        }
        return res;
    }

    private int getReferenceCount(SolrDocument doc, int docid) {
        int[] v = cache.getReferences(docid);
        if (v != null) {
            return _count(v);
        } else {
            return 0;
        }
    }


    static class ValueSourceAccessor {
        private final List<LeafReaderContext> readerContexts;
        private final ValueSource valueSource;
        private final Map fContext;
        private final FunctionValues[] functionValuesPerSeg;
        private final int[] functionValuesDocIdPerSeg;
        private final Map<Integer, Object> cache;

        ValueSourceAccessor(IndexSearcher searcher, ValueSource valueSource) {
            readerContexts = searcher.getIndexReader().leaves();
            this.valueSource = valueSource;
            fContext = ValueSource.newContext(searcher);
            functionValuesPerSeg = new FunctionValues[readerContexts.size()];
            functionValuesDocIdPerSeg = new int[readerContexts.size()];
            cache = new HashMap<Integer, Object>();
        }

        Object objectVal(int topDocId) throws IOException {
            if (cache.containsKey(topDocId))
                return cache.get(topDocId);

            // lookup segment level stuff:
            int segIdx = ReaderUtil.subIndex(topDocId, readerContexts);
            LeafReaderContext rcontext = readerContexts.get(segIdx);
            int segDocId = topDocId - rcontext.docBase;
            // unfortunately Lucene 7.0 requires forward only traversal (with no reset method).
            //   So we need to track our last docId (per segment) and re-fetch the FunctionValues. :-(
            FunctionValues functionValues = functionValuesPerSeg[segIdx];
            if (functionValues == null || segDocId < functionValuesDocIdPerSeg[segIdx]) {
                functionValues = functionValuesPerSeg[segIdx] = valueSource.getValues(fContext, rcontext);
            }
            functionValuesDocIdPerSeg[segIdx] = segDocId;

            // get value:
            Object val = functionValues.objectVal(segDocId);
            cache.put(topDocId, val);
            return val;
        }
    }


}


