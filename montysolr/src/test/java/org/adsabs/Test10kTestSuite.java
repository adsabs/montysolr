package org.adsabs;

import com.univocity.parsers.csv.CsvParser;
import com.univocity.parsers.csv.CsvParserSettings;
import monty.solr.util.MontySolrQueryTestCase;
import monty.solr.util.SolrTestSetup;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.InputStream;
import java.util.List;
import java.util.stream.Collectors;

public class Test10kTestSuite extends MontySolrQueryTestCase {

    @BeforeClass
    public static void beforeClass() throws Exception {
        schemaString = "deploy/adsabs/server/solr/collection1/conf/schema.xml";

        configString = "deploy/adsabs/server/solr/collection1/conf/solrconfig.xml";

        SolrTestSetup.initCore(configString, schemaString);
    }

    @Test
    public void runTestSuite() {
        // Read the test data using univocity-parser library
        CsvParserSettings settings = new CsvParserSettings();
        settings.setHeaderExtractionEnabled(true);
        settings.getFormat().setLineSeparator("\n");

        CsvParser parser = new CsvParser(settings);
        InputStream is = getClass().getClassLoader().getResourceAsStream("successful_queries.csv");
        List<String[]> allRows = parser.parseAll(is);
        List<String> queries = allRows.stream().map(row -> row[2]).collect(Collectors.toList());


        for (String query : queries) {
            try {
                assertQ(req("q", query));
            } catch (AssertionError | RuntimeException e) {
                fail("Query failed: " + query);
            }
        }
    }
}
