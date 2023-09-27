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
import monty.solr.util.MontySolrSetup;
import monty.solr.util.SolrTestSetup;
import org.apache.solr.request.SolrQueryRequest;
import org.apache.solr.util.RefCounted;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.PosixFilePermissions;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;

@SuppressWarnings({"rawtypes", "unchecked"})
public class TestCitationCacheReaderWriter extends MontySolrAbstractTestCase {

    @BeforeClass
    public static void beforeClass() throws Exception {
        schemaString = "solr/collection1/conf/schema-citations-transformer.xml";

        configString = "solr/collection1/conf/citation-cache-solrconfig.xml";

        SolrTestSetup.initCore(configString, schemaString);
    }

    private CitationLRUCache cache;
    private Path tmpdir;


    public void createCache() throws Exception {
		
		/*
		 	0 refs: [3, 4, 2] cits: []
			1 refs: [2, 3, 4] cits: []
			2 refs: [2, 3, 4] cits: [0, 1, 2, 3, 4, 5, 6, 7, 8, 8, 9, 10]
			3 refs: [2, 3, 4] cits: [0, 1, 2, 3, 4, 5, 6, 7, 9, 10]
			4 refs: [2, 3, 4] cits: [0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10]
			5 refs: [3, 4, 2] cits: []
			6 refs: [2, 3, 4] cits: []
			7 refs: [2, 3, 4] cits: []
			8 refs: [4, 2, 2] cits: []
			9 refs: [2, 3, 4] cits: []
			10 refs: [2, 3, 4] cits: []
		 */

        cache = new CitationLRUCache<>();
        HashMap<String, String> m = new HashMap<String, String>();
        m.put("identifierFields", "bibcode");
        m.put("referenceFields", "reference");
        m.put("citationFields", "citation");

        cache.initializeCitationCache(10 + 1);

        for (int i = 0; i < 11; i++) {
            cache.put("b" + i, i);
        }

        for (int i = 0; i < 8; i++) {
            cache.insertCitation(2, i);
            cache.insertCitation(3, i);
            cache.insertCitation(4, i);
        }

        cache.insertCitation(2, 8);
        cache.insertCitation(2, 9);
        cache.insertCitation(2, 10);

        cache.insertCitation(3, 9);
        cache.insertCitation(3, 10);

        cache.insertCitation(4, 8);
        cache.insertCitation(4, 9);
        cache.insertCitation(4, 10);

        for (int i = 0; i < 11; i++) {
            cache.insertReference(i, 2);
            if (i != 8)
                cache.insertReference(i, 3);
            cache.insertReference(i, 4);
        }
    }


    @Override
    public void setUp() throws Exception {
        super.setUp();
        tmpdir = createTempDir();
        createCache();
    }

    @Override
    public void tearDown() throws Exception {
//		File cfg = new File(h.getCore().getResourceLoader().getConfigDir());
//		for (File c: cfg.listFiles()) {
//			if (c.getPath().contains("citation_cache"))
//				c.delete();
//		}
        for (File f : tmpdir.toFile().listFiles()) {
            f.delete();
        }
//		tmpdir.toFile().delete();
        super.tearDown();

    }


    @Test
    public void test() throws Exception {
        //System.out.println(formatCache(cache));

        CitationCacheReaderWriter rw = new CitationCacheReaderWriter(tmpdir.toFile());
        rw.persist(cache, 1);


        File td = tmpdir.toFile();
        for (File f : td.listFiles()) {
            assertTrue(f.length() > 0);
        }


        CitationLRUCache cache2 = new CitationLRUCache<>();
        assertEquals(0, cache2.getHighestDocid());
        rw.load(cache2);
        //System.out.println(formatCache(cache2));
        assertEquals(formatCache(cache), formatCache(cache2));
    }

    @Test
    public void testCacheLoad() throws IOException {


        RefCounted<SolrIndexSearcher> searchref = null;
        try {
            searchref = h.getCore().getSearcher();
            SolrIndexSearcher searcher = searchref.get();
            File confDir = new File(searcher.getCore().getResourceLoader().getConfigDir());

            // persist our known cache
            CitationCacheReaderWriter ccrw = new CitationCacheReaderWriter(confDir);
            ccrw.persist(cache, 2);

            // commit; it is configured to load the dump (index is empty)
            assertU(adoc("id", "10", "bibcode", "b10",
                    "reference", "b2", "reference", "b3", "reference", "b4"));
            assertU(commit("waitSearcher", "true"));

            // must reopen req to obtain the new searcher
            SolrQueryRequest r = req("test");
            SolrIndexSearcher searcherx = r.getSearcher();
            r.close();

            CitationCache cache2 = (CitationCache) searcherx.getCache("citations-cache-from-dump");
            CitationCache cache3 = (CitationCache) searcherx.getCache("citations-cache-from-both");

            assertEquals(formatCache(cache), formatCache(cache2));
            assertFalse(formatCache(cache3).equals(formatCache(cache2)));

            // commit; it is configured to load the dump (but generation is off now)
            assertU(adoc("id", "10", "bibcode", "b10",
                    "reference", "b2", "reference", "b3", "reference", "b4"));
            assertU(commit("waitSearcher", "true"));

            // must reopen req to obtain the new searcher
            r = req("test");
            searcherx = r.getSearcher();
            r.close();

            CitationCache cache4 = (CitationCache) searcherx.getCache("citations-cache-from-dump");
            CitationCache cache5 = (CitationCache) searcherx.getCache("citations-cache-from-both");

            assertEquals(formatCache(cache4), formatCache(cache5));
            assertFalse(formatCache(cache).equals(formatCache(cache4)));

        } finally {
            searchref.decref();
        }

    }

    private String formatCache(CitationCache cache) {
        Iterator cci = cache.getCitationGraph();
        StringBuilder out = new StringBuilder();

        int ii = 0;
        while (cci.hasNext()) {
            int[][] dta = (int[][]) cci.next();
            out.append(ii + " refs: " + Arrays.toString(dta[0]) + " cits: " + Arrays.toString(dta[1]) + "\n");
            ii++;
        }
        return out.toString();
    }

}


