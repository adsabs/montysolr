package org.apache.solr.search;

import monty.solr.util.MontySolrQueryTestCase;
import monty.solr.util.MontySolrSetup;
import monty.solr.util.SolrTestSetup;
import org.junit.BeforeClass;


public class TestSolrCitationQuery extends MontySolrQueryTestCase {


    @BeforeClass
    public static void beforeClass() throws Exception {
        schemaString = "schema.xml";

        configString = "solrconfig.xml";

        SolrTestSetup.initCore(configString, schemaString);
    }


    @Override
    public void tearDown() throws Exception {
        //FieldCache.DEFAULT.purgeAllCaches();
        super.tearDown();
    }

    public void testSearch() throws Exception {

        //assertU(delQ("*:*"));
        //assertU(commit()); // if i remove this, the test will sometimes fail (i don't understand...)
        assertU(adoc("id", "0", "bibcode", "a",
                "reference", "b", "reference", "c", "reference", "b"
        ));
        assertU(adoc("id", "1", "bibcode", "b",
                "reference", "X",
                "citation", "A", "citation", "D", "citation", "G"
        ));
        assertU(commit("waitSearcher", "true"));
        assertU(adoc("id", "2", "bibcode", "c",
                "reference", "E", "reference", "F",
                "citation", "A", "citation", "E"
        ));
        assertU(adoc("id", "3", "bibcode", "d",
                "reference", "B",
                "citation", "A"
        ));
        assertU(commit("waitSearcher", "true"));
        assertU(adoc("id", "4", "bibcode", "e",
                "citation", "C", "reference", "C"
        ));
        assertU(adoc("id", "5", "bibcode", "f",
                "citation", "C"
        ));
        assertU(adoc("id", "6", "bibcode", "g",
                "reference", "b"));
        assertU(adoc("id", "7", "bibcode", "p",
                "citation", "x", "citation", "y"));
        assertU(adoc("id", "8", "bibcode", "q",
                "citation", "z"));
        assertU(adoc("id", "9", "bibcode", "x",
                "reference", "p"));
        assertU(adoc("id", "10", "bibcode", "y",
                "reference", "p"));
        assertU(adoc("id", "11", "bibcode", "z",
                "reference", "q"));
        assertU(commit("waitSearcher", "true"));


        assertQ(req("q", "*:*"),
                "//*[@numFound='12']"
        );

        assertQ(req("q", "bibcode:A", "fl", "bibcode"),
                "//*[@numFound='1']",
                "//result/doc[1]/str[@name='bibcode']='a'"
        );

        assertQ(req("q", "citations(bibcode:A)"),
                "//*[@numFound='0']"
        );

        assertQ(req("q", "citations(author:\"Torkelsson, U.\")"),
                "//*[@numFound='0']");
        assertQ(req("q", "citations(Torkelsson, U.)"),
                "//*[@numFound='0']");

        assertQ(req("q", "citations(bibcode:b)", "fl", "bibcode"),
                "//*[@numFound='3']",
                "//result/doc/str[@name='bibcode']='a'",
                "//result/doc/str[@name='bibcode']='d'",
                "//result/doc/str[@name='bibcode']='g'"
        );
        // Root normalization must preserve the relative citation counts.
        assertQ(req("q", "citations((bibcode:b OR bibcode:c))", "fl", "bibcode,score",
                        "sort", "score desc,bibcode asc"),
                "//*[@numFound='4']",
                "//result/doc[1]/str[@name='bibcode']='a'",
                "//result/doc[1]/float[@name='score'] = 2 * //result/doc[2]/float[@name='score']",
                "//result/doc[2]/str[@name='bibcode']='d'",
                "//result/doc[3]/str[@name='bibcode']='e'",
                "//result/doc[4]/str[@name='bibcode']='g'");

//		ModifiableSolrParams p = params("sort","id asc");
//		assertJQ(req(p, "q","{!join from=bibcode to=reference}bibcode:b", "fl","id", "debugQuery", "true")
//        ,"/response=={'numFound':2,'start':0,'docs':[{'id':'0'},{'id':'1'}]}"
//    );

        assertQ(req("q", "joincitations(bibcode:B)", "fl", "bibcode"),
                "//*[@numFound='3']",
                "//result/doc/str[@name='bibcode']='a'",
                "//result/doc/str[@name='bibcode']='d'",
                "//result/doc/str[@name='bibcode']='g'"
        );


        assertQ(req("q", "references(bibcode:A)", "fl", "bibcode"),
                "//*[@numFound='3']",
                "//result/doc/str[@name='bibcode']='b'",
                "//result/doc/str[@name='bibcode']='c'",
                "//result/doc/str[@name='bibcode']='d'"
        );
        assertQ(req("q", "references((bibcode:a OR bibcode:g))", "fl", "bibcode,score",
                        "sort", "score desc"),
                "//*[@numFound='3']",
                "//result/doc[1]/str[@name='bibcode']='b'",
                "//result/doc[1]/float[@name='score'] = 2 * //result/doc[2]/float[@name='score']",
                "//result/doc[2]/str[@name='bibcode']='c'",
                "//result/doc[3]/str[@name='bibcode']='d'");
        assertQ(req("q", "references((bibcode:x OR bibcode:y OR bibcode:z^10),1.0)",
                        "fl", "bibcode,score", "sort", "score desc,bibcode asc"),
                "//*[@numFound='2']",
                "//result/doc[1]/str[@name='bibcode']='q'",
                "//result/doc[1]/float[@name='score'] > //result/doc[2]/float[@name='score']",
                "//result/doc[2]/str[@name='bibcode']='p'");
        assertQ(req("q", "citations((bibcode:p OR bibcode:q^10),1.0)",
                        "fl", "bibcode,score", "sort", "score desc,bibcode asc"),
                "//*[@numFound='3']",
                "//result/doc[1]/str[@name='bibcode']='z'",
                "//result/doc[1]/float[@name='score'] > //result/doc[2]/float[@name='score']");
        assertQ(req("q", "joinreferences(bibcode:A)", "fl", "bibcode"),
                "//*[@numFound='3']",
                "//result/doc/str[@name='bibcode']='b'",
                "//result/doc/str[@name='bibcode']='c'",
                "//result/doc/str[@name='bibcode']='d'"
        );


    }

    // Uniquely for Junit 3
    public static junit.framework.Test suite() {
        return new junit.framework.JUnit4TestAdapter(TestSolrCitationQuery.class);
    }

}
