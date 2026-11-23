package org.apache.solr.search;

import monty.solr.util.MontySolrQueryTestCase;
import monty.solr.util.SolrTestSetup;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TopDocs;
import org.apache.solr.request.SolrQueryRequest;
import org.apache.solr.util.RefCounted;
import org.junit.BeforeClass;

import java.nio.file.Files;
import java.nio.file.Path;

public class TestAlignedFieldQuery extends MontySolrQueryTestCase {

    @BeforeClass
    public static void beforeClass() throws Exception {
        Path originalSchema = Path.of(SolrTestSetup.getRepoUrl(
                Path.of("deploy/adsabs/server/solr/collection1/conf/schema.xml")).toURI());
        Path testSchema = createTempDir().resolve("schema.xml");
        Files.writeString(testSchema, Files.readString(originalSchema)
                .replace("</types>", """
                        <fieldType name="preanalyzed" class="solr.PreAnalyzedField"/>
                        <fieldType name="payload_text" class="solr.TextField">
                            <analyzer type="index">
                                <tokenizer class="solr.WhitespaceTokenizerFactory"/>
                                <filter class="solr.DelimitedPayloadTokenFilterFactory" encoder="integer"/>
                            </analyzer>
                            <analyzer type="query"><tokenizer class="solr.WhitespaceTokenizerFactory"/></analyzer>
                        </fieldType>
                        </types>
                        """)
                .replace("</fields>", """
                        <field name="slot_numbers" type="int" indexed="false" stored="true"
                            multiValued="true" docValues="true"/>
                        <field name="slot_labels" type="string" indexed="false" stored="true"
                            multiValued="true" docValues="true"/>
                        <field name="slot_longs" type="long" indexed="true" stored="true" multiValued="true"/>
                        <field name="slot_doubles" type="double" indexed="false" stored="true"
                            multiValued="true" docValues="true"/>
                        <field name="slot_dates" type="date" indexed="false" stored="true"
                            multiValued="true" docValues="true"/>
                        <field name="slot_payloads" type="payload_text" indexed="true" stored="true"
                            multiValued="true"/>
                        <field name="slot_preanalyzed" type="preanalyzed" indexed="true" stored="true"
                            multiValued="true"/>
                        </fields>
                        """));
        SolrTestSetup.initCore("deploy/adsabs/server/solr/collection1/conf/solrconfig.xml", testSchema.toString());
    }

    public void testSameAuthorAffiliationSlot() throws Exception {
        try {
            assertU(delQ("*:*"));
            assertU(adoc("id", "6001", "bibcode", "b6001", "title", "exoplanet",
                    "author", "Alice, A", "author", "Bob, B",
                    "aff", "NASA Research Center", "aff", "ESA"));
            assertU(adoc("id", "6002", "bibcode", "b6002",
                    "author", "Alice, A", "author", "Bob, B",
                    "aff", "ESA Research", "aff", "NASA"));
            assertU(commit("waitSearcher", "true"));

            Query reusableQuery = parseQuery("same(author:Alice, aff:NASA)");
            Query phraseQuery = parseQuery("same(author:Alice, aff:\"NASA Research Center\")");
            Query multiPhraseQuery = parseQuery("same(author:Alice, aff:\"NASA Research-Center\")");
            Query zeroBoostQuery = parseQuery("same(author:Alice^0, aff:NASA)");
            Query requestBoostQuery = parseQuery("same(author:Alice, aff:NASA)", "boost", "1");
            Query requestBqQuery = parseQuery("same(author:Alice, aff:NASA)",
                    "bq", "{!term f=title}exoplanet", "bf", "1");
            Query unmatchedAuthorQuery = parseQuery("same(author:NoSuchAuthor, aff:NASA)");

            RefCounted<SolrIndexSearcher> firstSearcher = h.getCore().getSearcher();
            try {
                assertSameSlotHits(firstSearcher.get(), reusableQuery);
                assertSameSlotHits(firstSearcher.get(), phraseQuery);
                assertSameSlotHits(firstSearcher.get(), multiPhraseQuery);
                assertSameSlotHits(firstSearcher.get(), zeroBoostQuery);
                assertSameSlotHits(firstSearcher.get(), requestBoostQuery);
                assertSameSlotHits(firstSearcher.get(), requestBqQuery);
                assertEquals(0L, firstSearcher.get().search(unmatchedAuthorQuery, 10).totalHits.value);
                assertTrue(firstSearcher.get().search(requestBqQuery, 10).scoreDocs[0].score
                        > firstSearcher.get().search(reusableQuery, 10).scoreDocs[0].score);
            } finally {
                firstSearcher.decref();
            }

            assertU(adoc("id", "6003", "bibcode", "b6003",
                    "author", "Alice, A", "author", "Bob, B", "author", "Carol, C",
                    "aff", "ESA", "aff", "NASA", "aff", "-"));
            assertU(commit("waitSearcher", "true"));
            Query negativeOnlyQuery = parseQuery("same(author:Alice, -aff:ESA)");
            SolrQueryRequest negativeRequest = req(
                    "defType", "aqp",
                    "q", "same((author:Alice AND -author:Bob), (aff:NASA AND -aff:ESA))");
            Query nestedNegativeQuery;
            try {
                nestedNegativeQuery = getParser(negativeRequest).getQuery();
            } finally {
                negativeRequest.close();
            }
            RefCounted<SolrIndexSearcher> secondSearcher = h.getCore().getSearcher();
            try {
                assertSameSlotHits(secondSearcher.get(), reusableQuery);
                assertSameSlotHits(secondSearcher.get(), nestedNegativeQuery);
                assertSameSlotHits(secondSearcher.get(), negativeOnlyQuery);
            } finally {
                secondSearcher.decref();
            }
        } finally {
            assertU(delQ("*:*"));
            assertU(commit("waitSearcher", "true"));
        }
    }

    public void testSameSlopAcrossStoredFieldTypes() throws Exception {
        try {
            assertU(adoc("id", "6101", "bibcode", "b6101", "title", "rotation profile interferometry",
                    "keyword_facet", "Target", "simbid", "42", "slot_numbers", "42", "slot_numbers", "9",
                    "slot_labels", "Target", "slot_labels", "Other", "slot_longs", "4294967297",
                    "slot_doubles", "16777217.25", "slot_dates", "2020-10-01T12:00:00Z",
                    "slot_payloads", "indicator|7"));
            assertU(adoc("id", "6102", "bibcode", "b6102", "title", "interferometry",
                    "keyword_facet", "Other", "keyword_facet", "Target",
                    "simbid", "9", "simbid", "42", "slot_numbers", "9", "slot_numbers", "42",
                    "slot_labels", "Other", "slot_labels", "Target", "slot_longs", "1",
                    "slot_doubles", "16777218.0", "slot_dates", "2020-10-02T12:00:00Z",
                    "slot_payloads", "indicator|8"));
            assertU(adoc("id", "6103", "bibcode", "b6103", "title", "rotation",
                    "title", "profile interferometry", "keyword_facet", "Target", "simbid", "42"));
            assertU(adoc("id", "6104", "bibcode", "b6104", "title", "filler", "title", "interferometry",
                    "keyword_facet", "Other", "keyword_facet", "-", "keyword_facet", "Other",
                    "keyword_facet", "Target", "simbid", "9", "simbid", "9", "simbid", "9", "simbid", "42"));
            assertU(adoc("id", "6105", "bibcode", "b6105", "title", "interferometry", "keyword_facet", "target"));
            assertU(commit("waitSearcher", "true"));

            assertSameIds("same(title:interferometry, keyword_facet:Target)", "6101");
            assertSameIds("same(title:interferometry, keyword_facet:Target, 0)", "6101");
            assertSameIds("same(title:interferometry, keyword_facet:Target, 1)", "6101", "6102", "6103");
            assertSameIds("same(keyword_facet:Target, title:interferometry, 1)", "6101", "6102", "6103");
            assertSameIds("same(title:interferometry, keyword_facet:Target, 2)", "6101", "6102", "6103", "6104");
            assertSameIds("same(title:interferometry, keyword_facet:Target, 2147483647)",
                    "6101", "6102", "6103", "6104");
            assertSameIds("same(title:interferometry, simbid:42)", "6101");
            assertSameIds("same(title:interferometry, simbid:[40 TO 43], 1)", "6101", "6102", "6103");
            assertSameIds("same(title:interferometry, slot_numbers:42)", "6101");
            assertSameIds("same(title:interferometry, slot_numbers:[40 TO 43], 1)", "6101", "6102");
            assertSameIds("same(title:interferometry, slot_numbers:[42 TO *])", "6101");
            assertSameIds("same(title:interferometry, slot_numbers:[* TO 9])", "6102");
            assertSameIds("same(title:interferometry, slot_numbers:*)", "6101", "6102");
            assertSameIds("same(title:interferometry, slot_labels:Target)", "6101");
            assertSameIds("same(title:interferometry, slot_labels:[T TO *])", "6101");
            assertSameIds("same(title:interferometry, slot_labels:Tar*)", "6101");
            assertSameIds("same(title:interferometry, slot_longs:4294967297)", "6101");
            assertSameIds("same(title:interferometry, slot_doubles:16777217.25)", "6101");
            assertSameIds(req("defType", "aqp", "q", "id:610*", "sort", "id asc", "aqp.dateFields", "slot_dates",
                    "fq", "same(title:interferometry, slot_dates:[\"2020-10-01T00:00:00\" TO \"2020-10-02T00:00:00\"])"),
                    "6101");
            assertSameIds("same(title:interferometry, slot_dates:[* TO *])", "6101", "6102");
            assertSameIds("same(title:rotation, title:interferometry)", "6101");
            assertSameIds("same(title:rotation, title:interferometry, 1)", "6101", "6103");
            assertSameIds("same(title:(rotation NEAR2 profile), keyword_facet:Target, 1)", "6101");
            assertSameIds("same(constant(pos(title:interferometry,-1)), keyword_facet:Target)", "6101");
            assertSameIds("same(title:interferometry, raw({!payload_check f=slot_payloads payloads=7}indicator))",
                    "6101");
        } finally {
            assertU(delQ("*:*"));
            assertU(commit("waitSearcher", "true"));
        }
    }

    public void testFuzzyMembershipDoesNotDependOnOtherDocuments() {
        try {
            assertU(adoc("id", "6106", "bibcode", "b6106", "title", "interferometry",
                    "keyword_facet", "ordinalzrobe"));
            assertU(commit("waitSearcher", "true"));
            String query = "same(title:interferometry, keyword_facet:ordinalprobe~1)";
            assertSameIds(query, "6106");

            // Add more than 50 equally good or better variants that sort before ordinalzrobe.
            for (char letter = 'a'; letter <= 'z'; letter++) {
                assertU(adoc("id", Integer.toString(6200 + letter), "bibcode", "f" + letter,
                        "keyword_facet", "ordinal" + letter + "robe",
                        "keyword_facet", "ordinalp" + letter + "obe"));
            }
            assertU(commit("waitSearcher", "true"));
            assertSameIds(query, "6106");
        } finally {
            assertU(delQ("*:*"));
            assertU(commit("waitSearcher", "true"));
        }
    }

    public void testSameRejectsInvalidFieldsAndSlop() {
        for (String query : new String[]{
                "same(id:6101, title:interferometry)",
                "same(all:interferometry, title:interferometry)",
                "same(slot_preanalyzed:profile, title:interferometry)",
                "same(links_data:profile, title:interferometry)",
                "same((title:interferometry OR keyword:spectroscopy), keyword_facet:Target)",
                "same(topn(1, title:interferometry), keyword_facet:Target)",
                "same(title:interferometry)",
                "same(title:interferometry, keyword_facet:Target, -1)",
                "same(title:interferometry, keyword_facet:Target, 1.5)",
                "same(title:interferometry, keyword_facet:Target, 1, 2)"}) {
            assertQEx("Invalid same() query: " + query, req("defType", "aqp", "q", query), 400);
        }
    }

    private void assertSameIds(String filter, String... expected) {
        assertSameIds(req("defType", "aqp", "q", "id:610*", "fq", filter, "sort", "id asc"), expected);
    }

    private void assertSameIds(SolrQueryRequest request, String... expected) {
        String[] checks = new String[expected.length + 1];
        checks[0] = "//result[@numFound='" + expected.length + "']";
        for (int i = 0; i < expected.length; i++) {
            checks[i + 1] = "//result/doc[" + (i + 1) + "]/str[@name='id'][.='" + expected[i] + "']";
        }
        assertQ(request, checks);
    }

    private Query parseQuery(String queryString, String... additionalParams) throws Exception {
        String[] params = new String[4 + additionalParams.length];
        params[0] = "defType";
        params[1] = "aqp";
        params[2] = "q";
        params[3] = queryString;
        System.arraycopy(additionalParams, 0, params, 4, additionalParams.length);
        SolrQueryRequest queryRequest = req(params);
        try {
            return getParser(queryRequest).getQuery();
        } finally {
            queryRequest.close();
        }
    }

    private void assertSameSlotHits(SolrIndexSearcher searcher, Query query) throws Exception {
        TopDocs hits = searcher.search(query, 10);
        assertEquals(1L, hits.totalHits.value);
        assertEquals("6001", searcher.doc(hits.scoreDocs[0].doc).get("id"));
    }
}
