package org.apache.solr.response.transform;

import monty.solr.util.MontySolrAbstractTestCase;
import monty.solr.util.SolrTestSetup;
import org.junit.BeforeClass;

public class TestFieldTransformer extends MontySolrAbstractTestCase {

    @BeforeClass
    public static void beforeClass() throws Exception {
        schemaString = "solr/collection1/conf/schema-field-transformer.xml";

        configString = "solr/collection1/conf/solrconfig-field-transformer.xml";

        SolrTestSetup.initCore(configString, schemaString);
    }


    @Override
    public void setUp() throws Exception {
        super.setUp();

        assertU(adoc("id", "3", "bibcode", "b3",
                "reference", "b2", "reference", "b3", "reference", "b4", "reference", "foo",
                "body", "foo bar baz"));

        assertU(commit());
    }


    public void test() throws Exception {

        assertQ(req("q", "bibcode:b3", "fl", "id,reference"),
                "//*[@numFound='1']",
                "//doc/int[@name='id'][.='3']",
                "//doc/arr[@name='reference']/str[.='b2']",
                "//doc/arr[@name='reference']/str[.='b3']",
                "//doc/arr[@name='reference']/str[.='b4']"
        );

        assertQ(req("q", "bibcode:b3",
                        "fl", "id,reference,body,[fields reference=2 body=3]",
                        "indent", "true"),
                "//*[@numFound='1']",
                "//doc/int[@name='id'][.='3']",
                "//doc/arr[@name='reference']/str[.='b2']",
                "//doc/arr[@name='reference']/str[.='b3']",
                "not(//doc/arr[@name='reference']/str[.='b4'])",
                "//doc/str[@name='body'][.='foo bar baz']"
        );

    }
}
