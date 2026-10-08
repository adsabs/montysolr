package org.adsabs;

/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


import monty.solr.util.MontySolrQueryTestCase;
import monty.solr.util.SolrTestSetup;
import org.apache.lucene.queries.mlt.MoreLikeThisQuery;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.DisjunctionMaxQuery;
import org.apache.lucene.search.TermRangeQuery;
import org.apache.solr.client.solrj.embedded.EmbeddedSolrServer;
import org.apache.solr.common.util.ContentStream;
import org.apache.solr.common.util.ContentStreamBase;
import org.apache.solr.common.util.ContentStreamBase.StringStream;
import org.apache.solr.request.SolrQueryRequestBase;
import org.apache.solr.schema.IndexSchema;
import org.apache.solr.schema.IntPointField;
import org.apache.solr.schema.SchemaField;
import org.apache.solr.servlet.DirectSolrConnection;
import org.junit.BeforeClass;

import java.util.ArrayList;
import java.util.List;


/**
 * This test verifies all indexes are in place and the search against
 * them works. This is the main test for the whole ADS search.
 *
 * Exercesis both indexing and searching, as configured
 * for the ADS. The test does not need a working solr installation,
 * it is using both solr example config and the specific ads config.
 *
 *    KEEP IT FREE FROM DEPENDENCIES!!!
 *
 **/
public class TestJoinReferences extends MontySolrQueryTestCase {

    @BeforeClass
    public static void beforeClass() throws Exception {
        schemaString = "deploy/adsabs/server/solr/collection1/conf/schema.xml";

        configString = "deploy/adsabs/server/solr/collection1/conf/solrconfig.xml";

        SolrTestSetup.initCore(configString, schemaString);
    }


    public void test() throws Exception {

        DirectSolrConnection direct = getDirectServer();
        EmbeddedSolrServer embedded = getEmbeddedServer();

        // bibcode is deliberately mixed-case here: its DocValues (used by joincitations/
        // joinreferences) hold the raw value, while citation/reference postings hold the
        // value normalized by the identifier_string analyzer (lower-cased, dashes/underscores
        // stripped). citation/reference field content is otherwise unchanged from before, so
        // the citations()/references() RAM-cache-based oracles below stay valid.
        assertU(adoc("id", "0", "bibcode", "B1",
                "citation", "b2", "reference", "b2"));
        assertU(adoc("id", "3", "bibcode", "C1",
                "citation", "b2", "citation", "b3", "reference", "b2", "reference", "b3"));
        assertU(adoc("id", "1", "bibcode", "B2",
                "citation", "b3", "reference", "b3"));
        assertU(adoc("id", "2", "bibcode", "B3",
                "citation", "b4", "reference", "b4"));

        // Isolated pair covering the dash-stripping normalization step (PatternReplaceFilter),
        // kept separate from the docs above so it doesn't disturb the RAM-cache oracle counts.
        assertU(adoc("id", "4", "bibcode", "D-1",
                "citation", "e1", "reference", "e1"));
        assertU(adoc("id", "5", "bibcode", "e1",
                "citation", "d-1", "reference", "d-1"));

        assertU(commit("waitSearcher", "true"));

        assertQ(req("q", "bibcode:b1"), "//*[@numFound='1']");

        // RAM-cache-based oracles, unaffected by the DocValues/postings normalization mismatch
        assertQ(req("q", "citations(bibcode:b2)"), "//*[@numFound='1']");
        assertQ(req("q", "references(bibcode:b2)"), "//*[@numFound='2']");

        // case-folding: bibcode "B2" must normalize to "b2" to match citation/reference postings
        assertQ(req("q", "joinreferences(bibcode:b2)"), "//*[@numFound='2']");
        assertQ(req("q", "joincitations(bibcode:b2)"), "//*[@numFound='2']");

        // dash-stripping: bibcode "D-1"/"e1" must normalize (strip '-') to match "d1"/"e1"
        assertQ(req("q", "joinreferences(bibcode:d-1)"), "//*[@numFound='1']");
        assertQ(req("q", "joincitations(bibcode:d-1)"), "//*[@numFound='1']");

        // collected terms present but no match on the "to" side
        assertQ(req("q", "joinreferences(bibcode:b1)"), "//*[@numFound='0']");
        // "from" query matches nothing at all -> MatchNoDocsQuery short-circuit
        assertQ(req("q", "joinreferences(bibcode:doesnotexist)"), "//*[@numFound='0']");
    }
}
