package org.apache.solr.analysis.author;

import monty.solr.util.MontySolrQueryTestCase;
import monty.solr.util.SolrTestSetup;
import org.adsabs.solr.AdsConfig.F;
import org.junit.BeforeClass;

public class TestAuthorApostrophes extends MontySolrQueryTestCase {

    @BeforeClass
    public static void beforeClass() throws Exception {
        schemaString = "schema.xml";
        configString = "solrconfig.xml";
        SolrTestSetup.initCore(configString, schemaString);
    }

    public void testElidedNamesAndUnicodeApostrophes() throws Exception {
        assertEquivalentNames("A'Hearn, M", "Ahearn, M", "A’Hearn, M",
                "AʼHearn, M", "A＇Hearn, M", "A' Hearn, M");
        assertEquivalentNames("A'Hearn, N", "Ahearn, N");
        assertEquivalentNames("O'Neill, Siobhan", "ONeill, Siobhan", "O’Neill, Siobhan");
        assertEquivalentNames("d'Artagnan, Rene", "dArtagnan, Rene");
        assertEquivalentNames("Dall'Oglio, Antonella", "DallOglio, Antonella");
        assertEquivalentNames("N'Guessan, Kouassi", "NGuessan, Kouassi");
    }

    public void testSyllableAndGlottalStopMarks() throws Exception {
        assertEquivalentNames("Xi'an, P'ing", "Xian, Ping", "Xi’an, P’ing");
        assertEquivalentNames("ʻIʻi, Keʻala", "Iʻi, Keʻala", "I'i, Ke'ala",
                "Ii, Keala", "‘I‘i, Ke‘ala");
        assertEquivalentNames("Ma'afu, Enele", "Maafu, Enele");
    }

    public void testDutchParticlesRetainWordBoundaries() throws Exception {
        assertEquivalentNames("'t Hooft, Gerard", "t Hooft, Gerard", "’t Hooft, Gerard");
        assertEquivalentNames("'s-Gravesande, Willem", "s Gravesande, Willem");
    }

    public void testApostrophesDoNotBecomeHyphensOrSpaces() throws Exception {
        assertU(adoc(F.ID, "1", F.BIBCODE, "xxxxxxxxxxxxx", F.AUTHOR, "A'Hearn, M"));
        assertU(adoc(F.ID, "2", F.BIBCODE, "xxxxxxxxxxxxx", F.AUTHOR, "A-Hearn, M"));
        assertU(adoc(F.ID, "3", F.BIBCODE, "xxxxxxxxxxxxx", F.AUTHOR, "A Hearn, M"));
        assertU(commit());
        assertQ(req("defType", "aqp", "q", "author:\"Ahearn, M\""),
                "//*[@numFound='1']", "//doc/str[@name='id'][.='1']");
        assertQ(req("defType", "aqp", "q", "author:\"A-Hearn, M\""),
                "//*[@numFound='2']", "//doc/str[@name='id'][.='2']",
                "//doc/str[@name='id'][.='3']");
    }

    private void assertEquivalentNames(String... names) throws Exception {
        assertU(delQ("*:*"));
        for (int i = 0; i < names.length; i++) {
            assertU(adoc(F.ID, Integer.toString(i), F.BIBCODE, "xxxxxxxxxxxxx",
                    F.AUTHOR, names[i], "first_author", names[i]));
        }
        // The same surname with a different given name must not be included.
        String other = names[0].substring(0, names[0].indexOf(',') + 1) + " Z";
        assertU(adoc(F.ID, "999", F.BIBCODE, "xxxxxxxxxxxxx",
                F.AUTHOR, other, "first_author", other));
        assertU(commit());

        String[] expected = new String[names.length + 1];
        expected[0] = "//*[@numFound='" + names.length + "']";
        for (int i = 0; i < names.length; i++) {
            expected[i + 1] = "//doc/str[@name='id'][.='" + i + "']";
        }
        for (String name : names) {
            for (String field : new String[]{"author", "first_author"}) {
                for (String exact : new String[]{"", "="}) {
                    assertQ(req("defType", "aqp", "fl", "id,author", "rows", "100",
                            "q", exact + field + ":\"" + name + "\""), expected);
                }
            }
        }
    }
}
