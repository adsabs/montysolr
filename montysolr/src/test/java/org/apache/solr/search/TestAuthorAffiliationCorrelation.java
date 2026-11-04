package org.apache.solr.search;

import monty.solr.util.MontySolrQueryTestCase;
import monty.solr.util.SolrTestSetup;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TopDocs;
import org.apache.solr.request.SolrQueryRequest;
import org.apache.solr.util.RefCounted;
import org.junit.BeforeClass;

public class TestAuthorAffiliationCorrelation extends MontySolrQueryTestCase {

    @BeforeClass
    public static void beforeClass() throws Exception {
        schemaString = "deploy/adsabs/server/solr/collection1/conf/schema.xml";
        configString = "deploy/adsabs/server/solr/collection1/conf/solrconfig.xml";
        SolrTestSetup.initCore(configString, schemaString);
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
