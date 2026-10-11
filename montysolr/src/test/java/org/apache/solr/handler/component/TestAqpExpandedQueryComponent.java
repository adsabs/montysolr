package org.apache.solr.handler.component;

import monty.solr.util.MontySolrQueryTestCase;
import monty.solr.util.SolrTestSetup;
import org.apache.solr.SolrTestCaseJ4;
import org.apache.solr.common.util.Utils;
import org.junit.BeforeClass;

import java.util.Map;

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

/**
 * Tests the {@code parseOnly=true} parameter on /select: it must return the
 * fully expanded query (equal to debug.parsedquery) without running a search,
 * and must not NPE when combined with debugQuery / wordcloud.
 */
public class TestAqpExpandedQueryComponent extends MontySolrQueryTestCase {

    @BeforeClass
    public static void beforeClass() throws Exception {
        // Reuse the real deploy schema/synonyms and solrconfig.xml (which wires
        // the expandedQuery component into /select's first-components).
        schemaString = TestAqpAdsabsSolrSearchSchema();
        configString = "solrconfig.xml";
        SolrTestSetup.initCore(configString, schemaString);
    }

    private static String TestAqpAdsabsSolrSearchSchema() {
        return org.apache.solr.search.TestAqpAdsabsSolrSearch.getSchemaFile();
    }

    @SuppressWarnings("unchecked")
    private static String extractParsedQuery(String jsonResponse) {
        Map<String, Object> resp = (Map<String, Object>) Utils.fromJSONString(jsonResponse);
        Object top = resp.get("parsedquery");
        if (top != null) {
            return top.toString();
        }
        Object debug = resp.get("debug");
        if (debug instanceof Map) {
            Object pq = ((Map<String, Object>) debug).get("parsedquery");
            return pq == null ? null : pq.toString();
        }
        return null;
    }

    public void testPrintPotato() throws Exception {
        String bare = h.query(SolrTestCaseJ4.req(
                "qt", "/select", "q", "potato", "parseOnly", "true", "wt", "json"));
        System.out.println("=== POTATO (bare /select defaults) ===");
        System.out.println(extractParsedQuery(bare));

        String boosted = h.query(SolrTestCaseJ4.req(
                "qt", "/select", "q", "potato", "parseOnly", "true", "wt", "json",
                "boost", "float(general_final_boost)",
                "boost", "sum(float(cite_read_boost),const(0.5))",
                "boost", "float(general_final_boost)"));
        System.out.println("=== POTATO (with 3 boost params) RAW ===");
        System.out.println(boosted);
    }

    public void testReturnsExpandedQueryWithoutSearching() throws Exception {
        assertU(adoc("id", "1", "bibcode", "b1", "title", "hubble space telescope"));
        assertU(commit("waitSearcher", "true"));

        String response = h.query(SolrTestCaseJ4.req(
                "qt", "/select", "q", "title:foo", "parseOnly", "true", "wt", "json"));

        // The expanded query is present ...
        assertTrue("expected parsedquery in: " + response,
                response.contains("\"parsedquery\""));
        // ... and no search was executed (no result docs / numFound section).
        assertFalse("expected no result docs in: " + response,
                response.contains("\"numFound\""));
        assertFalse("expected no error in: " + response,
                response.contains("\"parsedqueryError\""));
    }

    public void testMatchesDebugParsedQuery() throws Exception {
        // A query whose expansion is non-trivial (multi-token synonym + unfielded).
        String q = "hubble space telescope";

        String debugResp = h.query(SolrTestCaseJ4.req(
                "qt", "/select", "q", q, "debugQuery", "true", "wt", "json"));
        String parseOnlyResp = h.query(SolrTestCaseJ4.req(
                "qt", "/select", "q", q, "parseOnly", "true", "wt", "json"));

        String fromDebug = extractParsedQuery(debugResp);
        String fromParseOnly = extractParsedQuery(parseOnlyResp);

        assertNotNull("debug.parsedquery missing: " + debugResp, fromDebug);
        assertNotNull("parsedquery missing: " + parseOnlyResp, fromParseOnly);
        assertTrue("expanded query should be non-trivial: " + fromParseOnly,
                fromParseOnly.trim().length() > q.length());
        assertEquals("parseOnly must return the same string as debug.parsedquery",
                fromDebug, fromParseOnly);
    }

    public void testNoSearchWithoutParam() throws Exception {
        assertU(adoc("id", "2", "bibcode", "b2", "title", "regular search"));
        assertU(commit("waitSearcher", "true"));

        String response = h.query(SolrTestCaseJ4.req(
                "qt", "/select", "q", "title:regular", "wt", "json"));

        // Normal search runs; there is no top-level parsedquery key (debug is off).
        assertTrue("expected result section in: " + response,
                response.contains("\"numFound\""));
    }

    public void testParseErrorReportedAndSearchSkipped() throws Exception {
        String response = h.query(SolrTestCaseJ4.req(
                "qt", "/select", "q", "title:(unbalanced", "parseOnly", "true", "wt", "json"));

        assertTrue("expected parsedqueryError in: " + response,
                response.contains("\"parsedqueryError\""));
        assertFalse("expected no result docs in: " + response,
                response.contains("\"numFound\""));
    }

    public void testNoNpeWithDebugQuery() throws Exception {
        // The exact combination that previously NPEd: query is skipped, but
        // debug must be disabled on the ResponseBuilder so DebugComponent does
        // not dereference the null query.
        String response = h.query(SolrTestCaseJ4.req(
                "qt", "/select", "q", "title:foo", "parseOnly", "true",
                "debugQuery", "true", "wt", "json"));

        assertTrue("expected parsedquery in: " + response,
                response.contains("\"parsedquery\""));
    }

    public void testNoNpeWithWordcloud() throws Exception {
        // wordcloud is a last-component that reads rb.getResults(); it must be
        // disabled so it does not NPE when the search is skipped.
        String response = h.query(SolrTestCaseJ4.req(
                "qt", "/select", "q", "title:foo", "parseOnly", "true",
                "wordcloud.fl", "title", "wt", "json"));

        assertTrue("expected parsedquery in: " + response,
                response.contains("\"parsedquery\""));
    }
}
