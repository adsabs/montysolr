package org.apache.solr.analysis;

import monty.solr.util.MontySolrQueryTestCase;
import monty.solr.util.SolrTestSetup;
import org.junit.BeforeClass;

public class TestAcronymSearch extends MontySolrQueryTestCase {

    @BeforeClass
    public static void beforeClass() throws Exception {
        String configDir = "deploy/adsabs/server/solr/collection1/conf/";
        SolrTestSetup.initCore(configDir + "solrconfig.xml", configDir + "schema.xml");
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();
        clearIndex();
        index(1, "title", "SALT");
        index(2, "title", "salt");
        index(3, "title", "salts");
        index(4, "title", "southern african large telescope");
        index(5, "title", "SALT salt salts");
        index(6, "abstract", "SALT");
        index(7, "body", "SALT");
        index(8, "keyword", "SALT");
        index(9, "ack", "SALT");
        index(10, "abstract", "salts");
        index(11, "body", "salt");
        index(12, "keyword", "salt");
        index(13, "ack", "salts");
        index(14, "title", "SALT astronomical spectra");
        index(15, "title", "salt astronomical spectra");
        index(16, "title", "salts astronomical spectra");
        index(17, "title", "southern african large telescope astronomical spectra");
        assertU(commit());
    }

    public void testUppercaseSearchExcludesOrdinaryWordSynonyms() throws Exception {
        for (String query : new String[]{"full:SALT", "+full:SALT", "full:\"SALT\""}) {
            assertMatches(query, "1", "4", "5", "6", "7", "8", "9", "14", "17");
        }
    }

    public void testUppercasePhraseRetainsItsAcronymConstraint() throws Exception {
        assertMatches("title:\"SALT astronomical spectra\"", "14", "17");
    }

    public void testLowercaseSearchStillIncludesAcronymsAndWordVariants() throws Exception {
        assertMatches("full:salt", "1", "2", "3", "4", "5", "6", "7", "8", "9",
                "10", "11", "12", "13", "14", "15", "16", "17");
    }

    public void testExpandedNameStillMatchesItsAcronym() throws Exception {
        assertMatches("title:\"southern african large telescope\"", "1", "4", "5", "14", "17");
    }

    public void testExactSearchStillIgnoresCaseWithoutExpandingSynonyms() throws Exception {
        assertMatches("=full:SALT", "1", "2", "5", "6", "7", "8", "9", "11", "12", "14", "15");
    }

    private void index(int id, String field, String text) {
        assertU(adoc("id", Integer.toString(id), "bibcode", "acronym" + id, field, text));
    }

    private void assertMatches(String query, String... ids) throws Exception {
        String[] expected = new String[ids.length + 1];
        expected[0] = "//*[@numFound='" + ids.length + "']";
        for (int i = 0; i < ids.length; i++) {
            expected[i + 1] = "//doc/str[@name='id'][.='" + ids[i] + "']";
        }
        assertQ(req("defType", "aqp", "q", query, "fl", "id", "rows", "100"), expected);
    }
}
