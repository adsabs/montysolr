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
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.File;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Objects;

@SuppressWarnings({"rawtypes", "unchecked"})
public class TestCitationMapDBCache extends MontySolrAbstractTestCase {

    @BeforeClass
    public static void beforeClass() throws Exception {
        schemaString = "solr/collection1/conf/schema-citations-transformer.xml";

        configString = "solr/collection1/conf/citation-cache-solrconfig.xml";

        SolrTestSetup.initCore(configString, schemaString);
    }

    private CitationMapDBCache cache;
    private Path tmpdir;

    public void createCache() {
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

        cache = new CitationMapDBCache<>();
        HashMap<String, String> m = new HashMap<>();
        m.put("identifierFields", "bibcode");
        m.put("referenceFields", "reference");
        m.put("citationFields", "citation");
        m.put("dbPath", tmpdir.toString() + "/mapdb-test");

        // Initialize the cache
        cache.init(m, null, null);
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
        if (cache != null) {
            cache.close();
        }

        for (File f : Objects.requireNonNull(tmpdir.toFile().listFiles())) {
            if (!f.delete()) {
                System.err.println("Failed to delete file: " + f.getAbsolutePath());
            }
        }

        super.tearDown();
    }

    @Test
    public void testBasicOperations() {
        // Test ID mapping function
        assertEquals(0, cache.get("b0"));
        assertEquals(1, cache.get("b1"));
        assertEquals(2, cache.get("b2"));

        // Test references
        compare("References", new int[]{2, 3, 4}, cache.getReferences(1));
        compare("References", new int[]{2, 3, 4}, cache.getReferences(2));
        compare("References", new int[]{2, 3, 4}, cache.getReferences(3));

        // Test citations
        compare("Citations", null, cache.getCitations(0));
        compare("Citations", null, cache.getCitations(1));
        compare("Citations", new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 8, 9, 10}, cache.getCitations(2));
        compare("Citations", new int[]{0, 1, 2, 3, 4, 5, 6, 7, 9, 10}, cache.getCitations(3));

        // Test with string keys
        compare("References", new int[]{2, 3, 4}, cache.getReferences("b1"));
        compare("Citations", new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 8, 9, 10}, cache.getCitations("b2"));
    }

    @Test
    public void testCacheModifications() {
        // Test initial state
        assertEquals(2, cache.get("b2"));
        compare("Citations", new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 8, 9, 10}, cache.getCitations(2));

        // Remove an item
        cache.remove("b2");
        assertNull(cache.get("b2"));

        // Add it back
        cache.put("b2", 2);
        assertEquals(2, cache.get("b2"));

        // Test that citations and references need to be rebuilt
        assertNull(cache.getCitations(2));

        // Reinsert citations
        for (int i = 0; i < 8; i++) {
            cache.insertCitation(2, i);
        }
        cache.insertCitation(2, 8);
        cache.insertCitation(2, 9);
        cache.insertCitation(2, 10);

        compare("Citations", new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10}, cache.getCitations(2));
    }

    @Test
    public void testCachePersistence() {
        // Close the existing cache to avoid file lock conflicts
        cache.close();
        cache = null;

        // Create a new cache that will load the data from the MapDB files
        CitationMapDBCache<String, Integer> cache2 = new CitationMapDBCache<>();
        HashMap<String, String> m = new HashMap<>();
        m.put("identifierFields", "bibcode");
        m.put("referenceFields", "reference");
        m.put("citationFields", "citation");
        m.put("dbPath", tmpdir.toString() + "/mapdb-test");

        try {
            // Initialize the cache (should load from existing MapDB files)
            cache2.init(m, null, null);

            // Test that the data was loaded
            assertEquals(0, (int)cache2.get("b0"));
            assertEquals(1, (int)cache2.get("b1"));
            assertEquals(2, (int)cache2.get("b2"));

            compare("References", new int[]{2, 3, 4}, cache2.getReferences(1));
            compare("Citations", new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 8, 9, 10}, cache2.getCitations(2));
        } finally {
            // Close the second cache
            cache2.close();
        }
    }

    private void compare(String msg, int[] exp, int[] res) {
        if (exp != null)
            Arrays.sort(exp);
        if (res != null)
            Arrays.sort(res);

        // Print debug info
        System.out.println("EXPECTED: " + (exp == null ? "null" : Arrays.toString(exp)));
        System.out.println("ACTUAL  : " + (res == null ? "null" : Arrays.toString(res)));

        assertArrayEquals(msg, exp, res);
    }
}
