package org.apache.solr.search;

import monty.solr.util.MontySolrQueryTestCase;
import monty.solr.util.SolrTestSetup;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.solr.request.SolrQueryRequestBase;
import org.apache.solr.util.RefCounted;
import org.junit.BeforeClass;

public class TestAqpSecondOrderScoring extends MontySolrQueryTestCase {

    @BeforeClass
    public static void beforeClass() throws Exception {
        schemaString = "schema.xml";
        configString = "solrconfig.xml";
        SolrTestSetup.initCore(configString, schemaString);
    }

    @Override
    public void tearDown() throws Exception {
        assertU(delQ("*:*"));
        assertU(commit("waitSearcher", "true"));
        super.tearDown();
    }

    public void testTopNUsesTheClassicScoredSeedAsStandaloneAqpQuery() throws Exception {
        // Raw BM25 favors the repeated-term document; the classic read boost favors seed-popular.
        assertU(adoc("id", "9810", "bibcode", "seed-raw", "title",
                "rankprobe rankprobe rankprobe rankprobe rankprobe rankprobe rankprobe rankprobe rankprobe rankprobe",
                "cite_read_boost", "0.0"));
        assertU(adoc("id", "9811", "bibcode", "seed-popular", "title", "rankprobe",
                "cite_read_boost", "100.0"));
        assertU(commit("waitSearcher", "true"));

        assertQ(req("defType", "aqp", "q", "title:rankprobe",
                        "aqp.classic_scoring.modifier", "", "rows", "2", "fl", "bibcode,score"),
                "//*[@numFound='2']",
                "//result/doc[1]/str[@name='bibcode'][.='seed-raw']");
        assertQ(req("defType", "aqp", "q", "title:rankprobe", "rows", "2", "fl", "bibcode,score"),
                "//*[@numFound='2']",
                "//result/doc[1]/str[@name='bibcode'][.='seed-popular']");

        float standaloneScore = scoreForAqpQuery("title:rankprobe", "seed-popular");
        for (String query : new String[]{
                "topn(1, title:rankprobe)",
                "topn(1, title:rankprobe, \"score desc,bibcode asc\")",
                "topn(1, title:rankprobe, bibcode asc)"}) {
            assertEquals(standaloneScore, scoreForAqpQuery(query, "seed-popular"), 0.0001f);
        }

        String boostedRoot = "(topn(1, title:rankprobe))^2";
        assertEquals(standaloneScore * 2.0f,
                scoreForAqpQuery(boostedRoot, "seed-popular"), 0.0001f);

        String boostedIntermediate = "topn(1, (topn(1, title:rankprobe))^2)";
        assertEquals(standaloneScore * 2.0f,
                scoreForAqpQuery(boostedIntermediate, "seed-popular"), 0.0001f);

        assertEquals(standaloneScore * 2.0f,
                scoreForAqpQuery("topn(1, title:rankprobe) OR title:rankprobe", "seed-popular"),
                0.0001f);
    }

    public void testUsefulAndReviewsKeepRelationshipCountScores() throws Exception {
        // Read popularity must not outweigh the count of seed relationships.
        assertU(adoc("id", "9821", "bibcode", "seedone", "citation", "targetx",
                "citation", "targety", "citation_count", "1", "cite_read_boost", "0.0",
                "date", "2000-01-01T00:00:00Z"));
        assertU(adoc("id", "9822", "bibcode", "seedtwo", "citation", "targetx",
                "citation_count", "1", "cite_read_boost", "0.0", "date", "2000-01-02T00:00:00Z"));
        assertU(commit("waitSearcher", "true"));
        assertU(adoc("id", "9823", "bibcode", "targetx", "citation", "seedone",
                "citation", "seedtwo", "citation_count", "0", "cite_read_boost", "0.0",
                "date", "2000-01-01T00:00:00Z"));
        assertU(adoc("id", "9824", "bibcode", "targety", "citation", "seedone",
                "citation_count", "0", "cite_read_boost", "100.0", "date", "2000-01-02T00:00:00Z"));
        assertU(commit("waitSearcher", "true"));

        RefCounted<SolrIndexSearcher> indexSearcher = h.getCore().getSearcher();
        try {
            assertTrue("citation fixture must exercise multiple index leaves",
                    indexSearcher.get().getIndexReader().leaves().size() > 1);
        } finally {
            indexSearcher.decref();
        }

        // A rewriting seed exposes collector copies that lose their aggregation mode.
        assertRelationshipCounts("useful(bibcode:seed*)");
        assertRelationshipCounts("reviews(bibcode:seed*)");

        assertQ(req("defType", "aqp", "q",
                        "useful(bibcode:seed*) OR reviews(bibcode:seedtwo)",
                        "rows", "2", "sort", "score desc,date desc", "fl", "bibcode,score"),
                "//*[@numFound='2']",
                "//result/doc[1]/str[@name='bibcode'][.='targetx']",
                "//result/doc[1]/float[@name='score'][.='3.0']",
                "//result/doc[2]/str[@name='bibcode'][.='targety']",
                "//result/doc[2]/float[@name='score'][.='1.0']");
    }

    private void assertRelationshipCounts(String query) {
        assertQ(req("defType", "aqp", "q", query,
                        "rows", "2", "sort", "score desc,date desc", "fl", "bibcode,score"),
                "//*[@numFound='2']",
                "//result/doc[1]/str[@name='bibcode'][.='targetx']",
                "//result/doc[1]/float[@name='score'][.='2.0']",
                "//result/doc[2]/str[@name='bibcode'][.='targety']",
                "//result/doc[2]/float[@name='score'][.='1.0']");
    }

    private float scoreForAqpQuery(String query, String bibcode) throws Exception {
        SolrQueryRequestBase searchRequest =
                (SolrQueryRequestBase) req("defType", "aqp", "q", query,
                        "aqp.classic_scoring.modifier", "0.5");
        try {
            Query parsedQuery = getParser(searchRequest).parse();
            IndexSearcher searcher = searchRequest.getSearcher();
            TopDocs results = searcher.search(parsedQuery, 1);
            assertEquals("Expected exactly one returned top hit for " + query, 1, results.scoreDocs.length);
            ScoreDoc hit = results.scoreDocs[0];
            assertEquals(bibcode, searcher.storedFields().document(hit.doc).get("bibcode"));
            return hit.score;
        } finally {
            searchRequest.close();
        }
    }
}
