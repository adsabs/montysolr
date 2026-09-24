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
package org.apache.solr.search;

import monty.solr.util.MontySolrAbstractTestCase;
import monty.solr.util.SolrTestSetup;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.Term;
import org.apache.solr.request.SolrQueryRequest;
import org.apache.solr.schema.SchemaField;
import org.apache.solr.util.RefCounted;
import org.junit.BeforeClass;
import org.junit.Test;

@SuppressWarnings({"rawtypes", "unchecked"})
public class TestCitationCacheDocValues extends MontySolrAbstractTestCase {

    @BeforeClass
    public static void beforeClass() throws Exception {
        schemaString = "solr/collection1/conf/schema-citations-docvalues.xml";
        configString = "solr/collection1/conf/citation-cache-docvalues-solrconfig.xml";
        SolrTestSetup.initCore(configString, schemaString);
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();
        clearIndex();
        assertU(commit("waitSearcher", "true"));
    }

    @Test
    public void testDocValuesOnlyUpdateRebuildsIncrementalCache() throws Exception {
        assertU(adoc("id", "1", "cache_id", "1"));
        assertU(adoc("id", "2", "cache_id", "2"));
        assertU(commit("waitSearcher", "true"));

        SolrQueryRequest initialRequest = req("test");
        try {
            CitationLRUCache cache = (CitationLRUCache) initialRequest.getSearcher()
                    .getCache("citations-cache-from-id-docvalues");
            assertEquals("Cache contains both identifiers", 2, cache.size());
            assertEquals("Initial cache identifier", 1, cache.get("2"));

            RefCounted<IndexWriter> indexWriter = h.getCore().getUpdateHandler().getSolrCoreState()
                    .getIndexWriter(h.getCore());
            try {
                SchemaField idField = h.getCore().getLatestSchema().getField("id");
                String idTerm = idField.getType().readableToIndexed("2");
                indexWriter.get().updateNumericDocValue(new Term("id", idTerm), "cache_id", 22L);
            } finally {
                indexWriter.decref();
            }
            assertU(commit("waitSearcher", "true"));

            SolrQueryRequest updatedRequest = req("test");
            try {
                CitationLRUCache updatedCache = (CitationLRUCache) updatedRequest.getSearcher()
                        .getCache("citations-cache-from-id-docvalues");
                assertEquals("Updated cache identifier", 1, updatedCache.get("22"));
                assertNull(updatedCache.get("2"));
            } finally {
                updatedRequest.close();
            }
        } finally {
            initialRequest.close();
        }
    }
}
