package org.adsabs;

/**
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



import monty.solr.util.MontySolrQueryTestCase;
import monty.solr.util.MontySolrSetup;

import org.apache.lucene.search.TermRangeQuery;
import org.apache.solr.client.solrj.embedded.EmbeddedSolrServer;
import org.apache.solr.schema.IndexSchema;
import org.apache.solr.schema.SchemaField;
import org.apache.solr.schema.TrieIntField;
import org.apache.solr.servlet.DirectSolrConnection;
import org.junit.BeforeClass;


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
public class TestAdsAllFields extends MontySolrQueryTestCase {

  @BeforeClass
  public static void beforeClass() throws Exception {
  	makeResourcesVisible(Thread.currentThread().getContextClassLoader(), new String[] {
			MontySolrSetup.getMontySolrHome() + "/contrib/examples/adsabs/server/solr/collection1",
		  MontySolrSetup.getSolrHome() + "/example/solr/collection1"
		});

    System.setProperty("solr.allow.unsafe.resourceloading", "true");
    schemaString = MontySolrSetup.getMontySolrHome()
        + "/contrib/examples/adsabs/server/solr/collection1/schema.xml";

    configString = MontySolrSetup.getMontySolrHome()
        + "/contrib/examples/adsabs/server/solr/collection1/solrconfig.xml";

    initCore(configString, schemaString, MontySolrSetup.getSolrHome()
			    + "/example/solr");
  }


	public void test() throws Exception {

		DirectSolrConnection direct = getDirectServer();
		EmbeddedSolrServer embedded = getEmbeddedServer();

		// checking the schema
		IndexSchema schema = h.getCore().getLatestSchema();


		SchemaField field = schema.getField("id");
		assertTrue(field.indexed() == true && field.stored() == true && field.isRequired() == true
				&& field.multiValued() == false);

		field = schema.getUniqueKeyField();
		field.getName().equals("id");

		field = schema.getField("bibcode");
		assertTrue(field.indexed() == true && field.stored() == true && field.isRequired() == true
				&& field.multiValued() == false);
		field.checkSortability();

		field = schema.getField("recid");
		assertTrue(field.indexed() == true && field.stored() == true && field.isRequired() == false
				&& field.multiValued() == false);
		field.checkSortability();
		assertTrue(field.getType().getClass().isAssignableFrom(TrieIntField.class));

		// check field ID is copied to field RECID
//		List<CopyField> copyFields = schema.getCopyFieldsList("id");
//		assertTrue(copyFields.size() == 1);
//		CopyField cField = copyFields.get(0);
//		cField.getSource().getName().equals("id");
//		cField.getDestination().getName().equals(F.RECID);
//		field = cField.getDestination();





		// check authors are correctly indexed/searched
		assertU(adoc("id", "0", "bibcode", "b1", "author", "Dall'oglio, Antonella"));
		assertU(adoc("id", "1", "bibcode", "b2", "author", "VAN DER KAMP, A; Von Accomazzi, Alberto, III, Dr.;Kao, P'ing-Tzu"));
		assertU(adoc("id", "2", "bibcode", "b3", "author", "'t Hooft, Furst Middle"));
		assertU(adoc("id", "3", "bibcode", "b4", "author", "O, Paul S.; Last, Furst Middle More"));
		assertU(adoc("id", "4", "bibcode", "b5", "author", "O, Paul S.", "author", "Last, Furst Middle More"));
		assertU(adoc("id", "5", "bibcode", "b6", "author", "van Tiggelen, Bart A., Jr."));
		assertU(adoc("id", "6", "bibcode", "b7", "author", "Łuczak, Andrzej;John Doe Jr;Mac Low, Furst Middle;'t Hooft, Furst Middle"));
		assertU(adoc("id", "7", "bibcode", "b8", "author", "Łuczak, Andrzej", "author", "John Doe Jr",
				"author", "Mac Low, Furst Middle", "author", "'t Hooft, Furst Middle"));


		// this one JSON document shows our fields and their values (what is sent to /solr/update)
		String json = "{\"add\": {"
		+ "\"doc\": {" +
				"\"id\": 100" +

			  // not needed; it will be taken from 'id'
			  //", \"recid\": 100" +

			  ", \"abstract\": \"all no-sky survey q'i quotient\"" +
			  ", \"aff\": [\"-\", \"NASA Kavli space center, Cambridge, MA 02138, USA\", \"Einstein institute, Zurych, Switzerland\"]" +
			  ", \"alternate_bibcode\": [\"2014JNuM..455...1a1\", \"2014JNuM..455...1a2\"]" +
			  ", \"alternate_title\": \"This is of the alternate\"" +

			  // order and length must be the same for author,aff, email
			  // missing value must be indicated by '-'
			  ", \"author\": [\"t' Hooft, van X\", \"Anders, John Michael\", \"Einstein, A\"]" +
			  ", \"author_count\": 5" +
			  // author_facet_hier must be generated (solr doesn't modify it)
			  ", \"author_facet_hier\": [\"0/T Hooft, V\", \"1/T Hooft, V/T Hooft, Van X\", \"0/Anders, J M\", \"1/Anders, J M/Anders, John Michael\", \"0/Einstein, A\"]" +
			  // in the future, this can contain normalized author names
			  ", \"author_norm\": [\"t' Hooft, van X\", \"Anders, John Michael\", \"Einstein, A\"]" +
			  
				", \"bibcode\": \"2014JNuM..455...10B\"" +
				", \"bibgroup\": [\"Cfa\"]" +
				", \"bibgroup_facet\": [\"Cfa\"]" +
				/*
				 * Bibstem is derived from bibcode, it is either the bibcode[4:9] OR
				 * bibcode[4:13] when the volume information is NOT present
				 *
				 * So this bibcode: 2012yCat..35a09143M
				 * has bibstem:     yCat, yCat..35a
				 *
				 * But this bicode: 2012yCat..35009143M
				 * has bibstem:     yCat
				 *
				 * Bibstem is not case sensitive (at least for now, so the above values
				 * are lowercased)
				 *
				 */
				 ", \"bibstem\": [\"JNuM\", \"JNuM..455\"]" +
				 ", \"body\": \"Some fulltext hashimoto\"" +
				 ", \"caption\": [\"caption1 captionFoo\", \"caption2\"]" +
				 ", \"citation\": [\"2014JNuM..455...10C\", \"2014JNuM..455...10D\"]" +
				 ", \"cite_read_boost\": 0.52" +
				 ", \"citation_count\": 10" +
				 ", \"classic_factor\": 5002" +
				 ", \"comment\": [\"comment1 commentFoo\", \"comment2\"]" +
				 ", \"database\": [\"ASTRONOMY\", \"PHYSICS\"]" +

        ", \"data\": [\"NED:15\", \"CDS:5\"]" +
        ", \"data_count\": 20" +
				 // it is solr format for the pubdate, must be in the right format
				 // we need to add 30 minutes to every day; this allows us to search
				 // for ranges effectively; thus:
				 // 2013-08-5 -> 2013-08-05T00:30:00Z
				 // 2013-08   -> 2013-08-01T00:30:00Z
				 // 2013      -> 2013-01-01T00:30:00Z
        ", \"date\": \"2013-08-05T00:30:00Z\"" +
				", \"doctype\": \"article\"" +
				", \"doctype_facet_hier\": [\"0/Article\", \"1/Article/Book chapter\"]" +
			  ", \"doi\": \"doi:ŽŠČŘĎŤŇ:123456789\"" +
			  ", \"eid\": \"00001\"" +
			  ", \"email\": [\"-\", \"anders@email.com\", \"-\"]" +
			  ", \"esources\": [\"AUTHOR_HTML\", \"PUB_PDF\"]" +
			  // Field that contains both grant ids and grant agencies.
			  ", \"grant\": [\"NASA\", \"123456-78\", \"NSF-AST\", \"0618398\"]" +
			  // grant_agency/grant_id
			  ", \"grant_facet_hier\": [\"0/NASA\", \"1/NASA/123456-78\"]" +
				", \"identifier\": [\"arxiv:1234.5678\", \"ARXIV:hep-ph/1234\"]" +
				", \"ids_data\": [\"{whatever: here there MAST}\"]" +
				", \"issue\": \"24i\"" +

				", \"keyword\": [\"Classical statistical mechanics\", \"foo bar\"]" +
				", \"keyword_norm\": [\"angular momentum\", \"89.20.Hh\"]" +
				", \"keyword_schema\": [\"ADS\", \"PACS Codes\"]" +
				", \"keyword_facet\": [\"angular momentum kw\"]" +

				// ["{whatever: here there MAST}",
				// {"foo": ["bar", "baz"], "one": {"two": "three"}}
				", \"links_data\": [\"{whatever: here there MAST}\"," +
				    "\"{\\\"foo\\\": [\\\"bar\\\", \\\"baz\\\"], \\\"one\\\": {\\\"two\\\": \\\"three\\\"}}\"]" +

        ", \"nedid\": [\"X+1-5 =6\", \"foo bar\"]" +
        ", \"nedtype\": [\"Other\", \"type2\"]" +
        ", \"ned_object_facet_hier\": [ \"0/Other\", \"1/Other/X+1-5 =6\", \"0/type2\", \"1/type2/foo bar\"]" + 

				", \"orcid_pub\": [\"1111-2222-3333-4444\", \"-\", \"0000-0002-4110-3511\"]" +
				", \"orcid_user\": [\"-\", \"-\", \"0000-0002-4110-3511\"]" +
				", \"orcid_other\": [\"1111-2222-3333-4444\", \"1111-2222-3333-5555\", \"-\"]" +
				", \"origin\": [\"Springer\", \"ADS Metadata\"]" +
				
				// we actually index only the first token '2056'
				", \"page\": [\"2056\", \"55\"]" +
				", \"page_count\": 23" +
				", \"page_range\": \"23-55s\"" +
				// this list should contain normalized values
				", \"property\": [\"Catalog\", \"Nonarticle\", \"Data\"]" +
				// must be: "yyyy-MM-dd (metadata often is just: yyyy-MM|yyyy)
				", \"pubdate\": \"2013-08-05\"" +
				", \"pubnote\": [\"pubnote1 pubnoteFoo\", \"pubnote2\"]" +
				", \"read_count\": 50" +
				", \"reader\": [\"abaesrwersdlfkjsd\", \"asfasdflkjsdfsldj\"]" +
				", \"reference\": [\"2014JNuM..455...10R\", \"2014JNuM..455...10T\"]" +
				", \"simbad_object_facet_hier\": [\"0/HII Region\", \"1/HII Region/9000000\"]" +
				", \"simbtype\": [\"Galaxy\", \"HII Region\"]" +
				", \"simbid\": [5, 3000001]" +
				", \"title\": \"This is of the title\"" +

				", \"update_timestamp\": \"2010-03-04T22:01:32.809Z\"" +
				", \"volume\": \"l24\"" +
				", \"year\": \"2013\"" +
				
				// timestamps grouped for sanity-sake
				", \"entry_date\": \"2017-09-19T11:01:32.809000Z\"" + // iso format (2017-09-19T11:01:32.809+00:00) is NOT accepted 
				", \"metadata_ctime\": \"2017-09-19T11:01:32.809Z\"" +   // missing 'Z' (not accepted)
				", \"metadata_mtime\": \"2017-09-19T11:01:32.809Z\"" +
				", \"fulltext_ctime\": \"2017-09-19T11:01:32.809Z\"" +
        ", \"fulltext_mtime\": \"2017-09-19T11:01:32.809Z\"" +
        ", \"nonbib_ctime\": \"2017-09-19T11:01:32.809Z\"" +
        ", \"nonbib_mtime\": \"2017-09-19T11:01:32.809Z\"" +
        ", \"metrics_ctime\": \"2017-09-19T11:01:32.809Z\"" +
        ", \"metrics_mtime\": \"2017-09-19T11:01:32.809Z\"" +
        ", \"orcid_ctime\": \"2017-09-19T11:01:32.809Z\"" +
        ", \"orcid_mtime\": \"2017-09-19T11:01:32.809Z\"" +
				

			"}" +
		"}}";
		updateJ(json, null);


		assertU(adoc("id", "101", "bibcode", "2014JNuM..455...10C",
				"title", "citation 1", "read_count", "0", "cite_read_boost", "0.4649",
				"classic_factor", "5000", "citation", "2014JNuM..455...10B",
				"reader", "0xeeeeeeee", "reader", "1xeeeeeeee", "reader", "2xeeeeeeee"));
		assertU(adoc("id", "102", "bibcode", "2014JNuM..455...10D",
				"title", "citation 2", "read_count", "1", "cite_read_boost", "0.373",
				"classic_factor", "1500", "citation", "2014JNuM..455...10B"));
		assertU(adoc("id", "103", "bibcode", "2014JNuM..455...10R",
				"title", "reference 1", "read_count", "19", "cite_read_boost", "0.2416",
				"classic_factor", "0", "reader", "4xeeeeeeee", "reader", "1xeeeeeeee"));
		assertU(adoc("id", "104", "bibcode", "2014JNuM..455...10T",
				"title", "reference 2", "read_count", "15", "cite_read_boost", "0.4104"));

		assertU(commit());
		assertU(adoc("id", "20", "bibcode", "b20", "title", "datetest",
				"pubdate", "1976-01-01", "date", "1976-01-01T00:30:00Z"));
		assertU(adoc("id", "21", "bibcode", "b21", "title", "datetest",
				"pubdate", "1976-01-02", "date", "1976-01-02T00:30:00Z"));
		assertU(adoc("id", "22", "bibcode", "b22", "title", "datetest",
				"pubdate", "1976-02-01", "date", "1976-02-01T00:30:00Z"));
		assertU(adoc("id", "23", "bibcode", "b23", "title", "datetest",
				"pubdate", "1976-01-02", "date", "1976-01-02T00:30:00Z"));
		assertU(adoc("id", "24", "bibcode", "b24", "title", "datetest",
				"pubdate", "1976-30-12", "date", "1976-12-30T00:30:00Z")); // year 76 had only 30 days in Dec
		assertU(adoc("id", "25", "bibcode", "b25", "title", "datetest",
				"pubdate", "1977-01-01", "date", "1977-01-01T00:30:00Z"));

		assertU(adoc("id", "50", "bibcode", "b50", "author", "Bond, E J"));
		assertU(adoc("id", "51", "bibcode", "b51", "author", "Bond, Edwin James"));
		assertU(adoc("id", "52", "bibcode", "b52", "author", "Bond, E James"));
		assertU(adoc("id", "53", "bibcode", "b53", "author", "Bond, Edwin J"));
		assertU(adoc("id", "54", "bibcode", "b54", "author", "Bond, EJames"));
		assertU(adoc("id", "55", "bibcode", "b55", "author", "Bond, E"));
		assertU(adoc("id", "56", "bibcode", "b56", "author", "Bond, J"));
		assertU(adoc("id", "57", "bibcode", "b57", "author", "Bond,"));

		assertU(commit("waitSearcher", "true"));

		assertQ(req("q", "*:*"),
				"//*[@numFound>='19']"
		);
		assertQ(req("q", "id:100"),
				"//*[@numFound='1']"
		);

		/*
		 * id - str type, the unique id key, we do no processing
		 */

		assertQ(req("q", "id:100"), "//*[@numFound='1']");
		assertQ(req("q", "id:0100"), "//*[@numFound='0']");

		/*
		 * recid - recid is a int field
		 */

		assertQ(req("q", "recid:100"), "//*[@numFound='1']");
		assertQ(req("q", "recid:0100"), "//*[@numFound='1']");
		assertQ(req("q", "recid:[99 TO 100]"), "//*[@numFound='1']");
		assertQ(req("q", "recid:[99 TO *]"), "//*[@numFound>='1']");


		/*
		 * bibcodes
		 */

		assertQ(req("q", "bibcode:2014JNuM..455...10B"), "//*[@numFound='1']");
		assertQ(req("q", "bibcode:2014Jnum..455...10b"), "//*[@numFound='1']");
		assertQ(req("q", "bibcode:2014JNuM..*"), "//*[@numFound='5']");
		assertQ(req("q", "bibcode:2014JnUm..*"), "//*[@numFound='5']");
		assertQ(req("q", "bibcode:2014JNu?..455...10B"), "//*[@numFound='1']");


		/*
		 * alternate_bibcode
		 */
		assertQ(req("q", "alternate_bibcode:2014JNuM..455...1a2"), "//*[@numFound='1']");
		assertQ(req("q", "identifier:2014JNuM..455...1a2"), "//*[@numFound='1']");

		/*
		 * bibstem
		 */
		assertQ(req("q", "bibstem:JNUM"), "//*[@numFound='1']");
		assertQ(req("q", "bibstem:jnum"), "//*[@numFound='1']");

		assertQ(req("q", "bibstem:jnum..455"), "//*[@numFound='1']");
		assertQ(req("q", "bibstem:jnum..45*"), "//*[@numFound='1']");
		assertQ(req("q", "bibstem:jnum..45?"), "//*[@numFound='1']");


		//XXX: this has changed, the last dot gets removed when we try to guess regex query
		// need a better solution for this ambiguity yCat..* becomes 'yCat.*'
		assertQ(req("q", "bibstem:jnum..*"), "//*[@numFound='1']");
		assertQ(req("q", "bibstem:jnum.*"), "//*[@numFound='1']");
		assertQ(req("q", "bibstem:jnum*"), "//*[@numFound='1']");

		
		/*
     * caption
     */
    assertQ(req("q", "caption:caption1"),
        "//*[@numFound='1']",
        "//doc/int[@name='recid'][.='100']");
    assertQ(req("q", "caption:captionfoo"),
        "//*[@numFound='1']",
        "//doc/int[@name='recid'][.='100']");
    
		
		/*
		 * comment
		 */
		assertQ(req("q", "comment:comment1"),
        "//*[@numFound='1']",
        "//doc/int[@name='recid'][.='100']");
		assertQ(req("q", "comment:commentfoo"),
        "//*[@numFound='1']",
        "//doc/int[@name='recid'][.='100']");
		
		/*
		 * doi:
		 *
		 * According to the standard, doi can contain almost any utf-8
		 * char
		 */

		assertQ(req("q", "doi:\"doi:ŽŠČŘĎŤŇ:123456789\""), "//*[@numFound='1']");
		assertQ(req("q", "doi:ŽŠČŘĎŤŇ\\:123456789"), "//*[@numFound='1']");
		assertQ(req("q", "doi:\"doi:žščřďťň:123456789\""), "//*[@numFound='1']");
		//assertQ(req("q", "doi:\"doi:žščŘĎŤŇ?123456789\""), "//*[@numFound='1']");
		assertQ(req("q", "doi:\"doi:žščŘĎŤŇ\\?123456789\""), "//*[@numFound='0']");



		/*
		 * author
		 *
		 * here we really test only the import mechanism, the order of authors
		 * and duplication. The parsing logic has its own unittest
		 */
		assertQ(req("q", "author:\"Einstein, A\""),
				"//*[@numFound='1']",
				"//doc/int[@name='recid'][.='100']");
		assertQ(req("q", "author:\"Einstein, A\" AND author:\"Anders\""),
				"//*[@numFound='1']",
				"//doc/int[@name='recid'][.='100']");

		assert h.query(req("q", "author:\"Einstein, A\""))
				.contains("<arr name=\"author_norm\">" +
				"<str>t' Hooft, van X</str>" +
				"<str>Anders, John Michael</str>" +
				"<str>Einstein, A</str></arr>");
		
		/*
		 * author_count
		 */
		assertQ(req("q", "author_count:5"),
        "//*[@numFound='1']",
        "//doc/int[@name='recid'][.='100']");
		assertQ(req("q", "author_count:[0 TO 6]"),
        "//*[@numFound='1']",
        "//doc/int[@name='recid'][.='100']");

		/*
		 * pos() testing on the author search
		 */
		assertQ(req("q", "pos(author:\"Anders, John Michael\", 2)"),
				"//*[@numFound='1']",
				"//doc/int[@name='recid'][.='100']"
		);
		assertQ(req("q", "pos(author:\"Anders, John Michael\", 1, 2)"),
				"//*[@numFound='1']",
				"//doc/int[@name='recid'][.='100']"
		);
		assertQ(req("q", "pos(author:\"Einstein, A\", 1, 2)"),
				"//*[@numFound='0']"
		);


		/*
		 * author facets
		 */

		assertQ(req("q", "author_facet_hier:\"0/Anders, J M\""), "//*[@numFound='1']");
		assertQ(req("q", "author_facet_hier:\"1/Anders, J M/Anders, John Michael\""), "//*[@numFound='1']");
		assertQ(req("q", "author_facet_hier:\"1/Einstein, A\""), "//*[@numFound='0']");


		/*
		 * aff - must be the same order as authors
		 */
		assertQ(req("q", "aff:NASA"),
				"//doc/int[@name='recid'][.='100']",
				"//*[@numFound='1']"
		);
		assertQ(req("q", "aff:NASA AND author:\"Anders\""),
				"//doc/int[@name='recid'][.='100']",
				"//*[@numFound='1']"
		);
		assertQ(req("q", "aff:SPACE"), "//*[@numFound='0']"); // be case sensitive with uppercased query terms
		assertQ(req("q", "aff:KAVLI"), "//*[@numFound='0']"); // same here
		assertQ(req("q", "aff:kavli"), // otherwise case-insensitive
				"//*[@numFound='1']",
				"//doc/int[@name='recid'][.='100']"
		);
		assertQ(req("q", "aff:Kavli"),
				"//*[@numFound='1']",
				"//doc/int[@name='recid'][.='100']"
		);
		assertQ(req("q", "aff:\"kavli space\""),
				"//*[@numFound='1']",
				"//doc/int[@name='recid'][.='100']"
		);


		//the order/gaps need to be preserved

		assert h.query(req("q", "recid:100"))
 			.contains("<arr name=\"aff\">" +
				"<str>-</str>" +
				"<str>NASA Kavli space center, Cambridge, MA 02138, USA</str>" +
        "<str>Einstein institute, Zurych, Switzerland</str></arr>"
        );
		assertQ(req("q", "pos(aff:kavli, 2) AND recid:100"),
				"//*[@numFound='1']"
		);
		assertQ(req("q", "=aff:\"acr::nasa\" AND recid:100"),
				"//*[@numFound='1']"
		);



		/*
		 * email
		 */
		assertQ(req("q", "email:anders@email.com"),
				"//*[@numFound='1']",
				"//doc/int[@name='recid'][.='100']"
		);
		assertQ(req("q", "pos(email:anders@email.com, 2)"),
				"//*[@numFound='1']",
				"//doc/int[@name='recid'][.='100']"
		);
		assertQ(req("q", "pos(email:anders@email.com, 1)"),
				"//*[@numFound='0']"
		);

		assertQ(req("q", "email:anders@*"), "//*[@numFound='1']");

		// one has to use pos() to combine author and email
		assertQ(req("q", "email:anders@email.com AND author:\"Einstein, A\""),
				"//doc/int[@name='recid'][.='100']",
				"//*[@numFound='1']"
		);
		assertQ(req("q", "pos(email:anders@email.com, 2) AND pos(author:\"Anders\", 2)"),
				"//doc/int[@name='recid'][.='100']",
				"//*[@numFound='1']"
		);



		// order/gaps are important
		assert h.query(req("q", "recid:100"))
 			.contains("<arr name=\"email\">" +
				"<str>-</str>" +
				"<str>anders@email.com</str>" +
        "<str>-</str></arr>"
        );


		/*
     * orcid, added 30/12/14; they must correspond to the author array
     * - updated 13/11/15 - orcid field is now a virtual one; and we have
     *   orcid_pub,_user,_other
     */
    assertQ(req("q", "orcid_pub:1111-2222-3333-4444"),
        "//doc/int[@name='recid'][.='100']",
        "//*[@numFound='1']"
    );
    assertQ(req("q", "orcid_pub:1111*"),
        "//doc/int[@name='recid'][.='100']",
        "//*[@numFound='1']"
    );
    assert h.query(req("q", "recid:100"))
    .contains("<arr name=\"orcid_pub\">" +
      "<str>1111-2222-3333-4444</str>" +
      "<str>-</str>" +
      "<str>0000-0002-4110-3511</str></arr>"
      );
    // this is only present in orcid_other
    assertQ(req("q", "orcid:1111-2222-3333-5555"),
        "//doc/int[@name='recid'][.='100']",
        "//*[@numFound='1']"
    );
    assertQ(req("q", "orcid_other:1111-2222-3333-5555"),
        "//doc/int[@name='recid'][.='100']",
        "//*[@numFound='1']"
    );



		/*
		 * page
		 */
		assertQ(req("q", "page:2056"),
				"//*[@numFound='1']",
				"//doc/int[@name='recid'][.='100']");
		assertQ(req("q", "page:2056 AND page:55"),
				"//*[@numFound='1']",
				"//doc/int[@name='recid'][.='100']");

		/*
		 * eid
		 */
		assertQ(req("q", "eid:00001"),
				"//*[@numFound='1']",
				"//doc/int[@name='recid'][.='100']");



		/*
		 * volume
		 */
		assertQ(req("q", "volume:l24"),
				"//*[@numFound='1']",
				"//doc/int[@name='recid'][.='100']");
		assertQ(req("q", "volume:24"),
				"//*[@numFound='0']");

		/*
		 * issue
		 */
		assertQ(req("q", "issue:24i"),
				"//*[@numFound='1']",
				"//doc/int[@name='recid'][.='100']");


		/*
		 * database & bibgroup
		 */
		assertQ(req("q", "database:astronomy"), "//*[@numFound='1']");
		assertQ(req("q", "database:ASTRONOMY"), "//*[@numFound='1']");
		assertQ(req("q", "database:ASTRONOM*"), "//*[@numFound='1']");

		assertQ(req("q", "bibgroup:cfa"), "//*[@numFound='1']");
		assertQ(req("q", "bibgroup:CFA"), "//*[@numFound='1']");
		assertQ(req("q", "bibgroup:cf*"), "//*[@numFound='1']");
		assertQ(req("q", "bibgroup:CF*"), "//*[@numFound='1']");
		assertQ(req("q", "bibgroup:?FA"), "//*[@numFound='1']");

		// facets are case sensitive and  you must get the exact wording
		// TODO: shall we be consistent and turn *everything* to lowercase?
		assertQ(req("q", "bibgroup_facet:Cfa"), "//*[@numFound='1']");
		assertQ(req("q", "bibgroup_facet:cfa"), "//*[@numFound='0']");


		/*
		 * property
		 */

		assertQ(req("q", "property:catalog AND property:nonarticle"),
				"//*[@numFound='1']",
				"//doc/int[@name='recid'][.='100']");
		assertQ(req("q", "property:CATALOG AND property:NONARTICLE"),
				"//*[@numFound='1']",
				"//doc/int[@name='recid'][.='100']");


		/*
		 * keywords
		 */

		assertQ(req("q", "keyword:\"classical statistical mechanics\""),
				"//*[@numFound='1']",
				"//doc/int[@name='recid'][.='100']"
		);
		assertQ(req("q", "keyword:\"foo bar\""),
				"//*[@numFound='1']",
				"//doc/int[@name='recid'][.='100']"
		);
		assertQ(req("q", "keyword:\"Classical Statistical Mechanics\""), // should be case-insensitive
				"//*[@numFound='1']",
				"//doc/int[@name='recid'][.='100']"
		);


		assertQ(req("q", "keyword_norm:\"89.20.Hh\""),
				"//doc/int[@name='recid'][.='100']",
				"//*[@numFound='1']"
		);
		assertQ(req("q", "keyword_norm:\"89.20.Hh\" AND keyword_schema:\"PACS Codes\""),
				"//doc/int[@name='recid'][.='100']",
				"//*[@numFound='1']"
		);

		assertQ(req("q", "keyword_norm:classical"), "//*[@numFound='0']"); // should not contain keywords
		assertQ(req("q", "keyword:89.20.Hh"), "//*[@numFound='0']"); // should not contain keywords_norm


		/*
		 * keyword_facet (in marc used to be 695__b)
		 */

		assertQ(req("q", "keyword_facet:\"angular momentum kw\""),
				"//*[@numFound='1']");
		assertQ(req("q", "keyword_facet:\"angular momentum\""),
				"//*[@numFound='0']");
		assertQ(req("q", "keyword_facet:angular"),
				"//*[@numFound='0']");


		/*
		 * identifier
		 *
		 * should be translated into the correct field (currently, the grammar
		 * understands only arxiv: and doi: (and doi gets handled separately)
		 *
		 */

		assertQ(req("q", "arxiv:1234.5678"), "//*[@numFound='1']");
		assertQ(req("q", "arxiv:\"arXiv:1234.5678\""), "//*[@numFound='1']");
		assertQ(req("q", "arXiv:1234.5678"), "//*[@numFound='1']");
		assertQ(req("q", "identifier:1234.5678"), "//*[@numFound='1']");
		assertQ(req("q", "arXiv:hep-ph/1234"), "//*[@numFound='1']");
		assertQ(req("q", "arxiv:\"ARXIV:hep-ph/1234\""), "//*[@numFound='1']");
		assertQ(req("q", "arxiv:hep-ph/1234"), "//*[@numFound='1']");
		assertQ(req("q", "identifier:hep-ph/1234"), "//*[@numFound='1']");

		assertQ(req("q", "identifier:2014JNuM..455...10B"),
				"//*[@numFound='1']",
				"//doc/int[@name='recid'][.='100']");


		/*
		 * grants
		 *
		 */
		assertQ(req("q", "grant:\"NSF-AST 0618398\""),
				"//*[@numFound='1']",
				"//doc/int[@name='recid'][.='100']"
		);
		assertQ(req("q", "grant:(NSF-AST 0618398)"),
				"//*[@numFound='1']",
				"//doc/int[@name='recid'][.='100']"
		);
		assertQ(req("q", "grant:0618398"),
				"//*[@numFound='1']",
				"//doc/int[@name='recid'][.='100']"
		);
		assertQ(req("q", "grant:NSF-AST"),
				"//*[@numFound='1']",
				"//doc/int[@name='recid'][.='100']"
		);


		/*
		 * grant_facet_hier
		 */
		assertQ(req("q", "grant_facet_hier:\"0/NASA\""),
				"//*[@numFound='1']",
				"//doc/int[@name='recid'][.='100']"
		);
		assertQ(req("q", "grant_facet_hier:1/NASA/123456-78"),
				"//*[@numFound='1']",
				"//doc/int[@name='recid'][.='100']"
		);
		assertQ(req("q", "grant_facet_hier:NASA"),
				"//*[@numFound='0']"
		);



		/*
		 * title
		 *
		 * just basics here, the parsing tests are inside TestAdstypeFulltextParsing
		 *
		 */
		assertQ(req("q", "title:\"this title\""),
				"//*[@numFound='1']",
				"//doc/int[@name='recid'][.='100']"
		);
		assertQ(req("q", "title:\"this is of the title\""),
				"//*[@numFound='1']",
				"//doc/int[@name='recid'][.='100']"
		);


		/*
		 * alternate_title
		 *
		 * should be copied into main title field
		 */

		assertQ(req("q", "alternate_title:\"this alternate\""),
				"//*[@numFound='1']",
				"//doc/int[@name='recid'][.='100']"
		);
		assertQ(req("q", "alternate_title:\"this is of the alternate\""),
				"//*[@numFound='1']",
				"//doc/int[@name='recid'][.='100']"
		);
		assertQ(req("q", "title:\"this alternate\""),
				"//*[@numFound='1']",
				"//doc/int[@name='recid'][.='100']"
		);



		/*
		 * abstract
		 */

		assertQ(req("q", "abstract:no-sky"),
				"//*[@numFound='1']",
				"//doc/int[@name='recid'][.='100']"
		);
		assertQ(req("q", "abstract:nosky"),
				"//*[@numFound='1']",
				"//doc/int[@name='recid'][.='100']"
		);


		// tokens with special characters inside must be searched as a phrase, otherwise it
		// becomes: abstract:q'i abstract:q abstract:i abstract:qi
		// but even as a phrase, it will search for: "q (i qi)"
		assertQ(req("q", "abstract:\"q\\'i\"", "fl", "recid,abstract,title"), "//*[@numFound='1']");
		assertQ(req("q", "abstract:\"q'i\"", "fl", "recid,abstract,title"), "//*[@numFound='1']");
		assertQ(req("q", "abstract:\"q\\\\'i\"", "fl", "recid,abstract,title"), "//*[@numFound='1']");

		
		/*
		 * pubnote
		 */
		
		assertQ(req("q", "pubnote:pubnotefoo"),
        "//*[@numFound='1']",
        "//doc/int[@name='recid'][.='100']"
    );
		assertQ(req("q", "pubnote:pubnote2"),
        "//*[@numFound='1']",
        "//doc/int[@name='recid'][.='100']"
    );

		/*
		 * reference
		 */
		assertQ(req("q", "reference:2014JNuM..455...10R"),
				"//*[@numFound='1']",
				"//doc/int[@name='recid'][.='100']"
		);


		/*
		 * unfielded search
		 *
		 * test we get records without specifying the field (depends on the current
		 * solrconfig.xml setup)
		 *
		 * author^2 title^1.4 abstract^1.3 keyword^1.4 keyword_norm^1.4 all full^0.1
		 */

		String qf = "author^2 title^1.4 abstract^1.3 keyword^1.4 keyword_norm^1.4 all full^0.1";
		// author
		assertQ(req("q", "einstein", "qf", qf),
				"//*[@numFound='1']",
				"//doc/int[@name='recid'][.='100']"
		);
		// title
		assertQ(req("q", "title", "qf", qf),
				"//*[@numFound='1']",
				"//doc/int[@name='recid'][.='100']"
		);
		// abstract
		assertQ(req("q", "\"q'i\"", "qf", qf),
				"//*[@numFound='1']",
				"//doc/int[@name='recid'][.='100']"
				);



		/*
		 * body
		 */
		assertQ(req("q", "body:hashimoto"),
				"//*[@numFound='1']",
				"//doc/int[@name='recid'][.='100']"
		);


		/*
		 * citations()/references() queries (use special dummy records)
		 */
		// XXX:rca - to activate after fixing citation search
		/*assertQ(req("q", "recid:[101 TO 104]"), "//*[@numFound='4']");
		assertQ(req("q", "citations(recid:100)"),
				"//*[@numFound='2']",
				"//doc/int[@name='recid'][.='101']",
				"//doc/int[@name='recid'][.='102']"
				);
		assertQ(req("q", "references(recid:100)"),
				"//*[@numFound='2']",
				"//doc/int[@name='recid'][.='103']",
				"//doc/int[@name='recid'][.='104']"
				);*/



		/*
		 * read_count (int type)
		 */
		assertQ(req("q", "read_count:[0 TO 19]", "fl", "recid,bibcode,title,read_count"),
				"//doc/int[@name='recid'][.='101']",
				"//doc/int[@name='recid'][.='102']",
				"//doc/int[@name='recid'][.='103']",
				"//doc/int[@name='recid'][.='104']",
				"//*[@numFound='4']");
		assertQ(req("q", "read_count:19"),
				"//doc/int[@name='recid'][.='103']",
				"//*[@numFound='1']");
		assertQ(req("q", "read_count:15.0"),
				"//doc/int[@name='recid'][.='104']",
				"//*[@numFound='1']");
		assertQ(req("q", "read_count:1.0"),
				"//doc/int[@name='recid'][.='102']",
				"//*[@numFound='1']");
		assertQ(req("q", "read_count:0.0"),
				"//doc/int[@name='recid'][.='101']",
				"//*[@numFound='1']");
		assertQ(req("q", "read_count:[0.0 TO *]"),
        "//doc/int[@name='recid'][.='101']",
        "//*[@numFound>='4']");

		/*
		 * cite_read_boost
		 */
		//dumpDoc(null, "recid", "read_count", "cite_read_boost");
		assertQ(req("q", "cite_read_boost:[0.0 TO 1.0]"),
				"//doc/int[@name='recid'][.='100']",
				"//doc/int[@name='recid'][.='101']",
				"//doc/int[@name='recid'][.='102']",
				"//doc/int[@name='recid'][.='103']",
				"//doc/int[@name='recid'][.='104']",
				"//*[@numFound='5']");
		assertQ(req("q", "cite_read_boost:0.4649"),
				"//doc/int[@name='recid'][.='101']",
				"//*[@numFound='1']");
		assertQ(req("q", "cite_read_boost:0.373"),
				"//doc/int[@name='recid'][.='102']",
				"//*[@numFound='1']");
		assertQ(req("q", "cite_read_boost:0.2416"),
				"//doc/int[@name='recid'][.='103']",
				"//*[@numFound='1']");
		assertQ(req("q", "cite_read_boost:0.4104"),
				"//doc/int[@name='recid'][.='104']",
				"//*[@numFound='1']");

		assertQ(req("q", "cite_read_boost:[0.1 TO 0.373]"),
				"//doc/int[@name='recid'][.='102']",
				"//doc/int[@name='recid'][.='103']",
				"//*[@numFound='2']");
		assertQ(req("q", "cite_read_boost:[0.4103 TO 0.410399999999]"),
				"//doc/int[@name='recid'][.='104']",
				"//*[@numFound='1']");
		assertQ(req("q", "cite_read_boost:[0.41039999 TO 0.4648999999]"),
				"//doc/int[@name='recid'][.='104']",
				"//doc/int[@name='recid'][.='101']",
				"//*[@numFound='2']");
		assertQ(req("q", "cite_read_boost:[0.0 TO *]"),
        "//*[@numFound>='5']");

		/*
		 * classic_factor
		 */

		assertQ(req("q", "classic_factor:5000"),
				"//doc/int[@name='recid'][.='101']",
				"//*[@numFound='1']"
		);
		assertQ(req("q", "classic_factor:1500"),
				"//doc/int[@name='recid'][.='102']",
				"//*[@numFound='1']"
		);
		assertQ(req("q", "classic_factor:0"),
				"//doc/int[@name='recid'][.='103']",
				"//*[@numFound='1']"
		);

		assertQ(req("q", "classic_factor:[0 TO 5001]", "indent", "true"),
				"//doc/int[@name='recid'][.='101']",
				"//doc/int[@name='recid'][.='102']",
				"//doc/int[@name='recid'][.='103']",
				"//*[@numFound='3']"
		);

		assertQ(req("q", "classic_factor:[0 TO *]", "indent", "true"),
        "//*[@numFound>='3']"
    );


		/*
		 * simbid - simbad_object_ids
		 */
	  //dumpDoc(null, "bibcode", "simbid");
		assertQ(req("q", "simbid:5 AND simbid:3000001"),
				"//doc/int[@name='recid'][.='100']",
				"//*[@numFound='1']"
		);
		assertQ(req("q", "simbid:[0 TO 9000001]"),
				"//doc/int[@name='recid'][.='100']",
				"//*[@numFound='1']"
		);
		assertQ(req("q", "simbid:[0 TO *]"),
        "//doc/int[@name='recid'][.='100']",
        "//*[@numFound='1']"
    );

		/*
		 * simbtype - simbad object types, added 30/12/14
		 */
		assertQ(req("q", "simbtype:HII"),
        "//doc/int[@name='recid'][.='100']",
        "//*[@numFound='1']"
    );
		assertQ(req("q", "simbtype:hii"),
        "//doc/int[@name='recid'][.='100']",
        "//*[@numFound='1']"
    );
		assertQ(req("q", "simbtype:\"HiI Region\""),
        "//doc/int[@name='recid'][.='100']",
        "//*[@numFound='1']"
    );

		/*
		 * simbad_object_facet_hier, added 30/12/14
		 */
		assertQ(req("q", "simbad_object_facet_hier:\"0/HII Region\""),
        "//doc/int[@name='recid'][.='100']",
        "//*[@numFound='1']"
    );
		assertQ(req("q", "simbad_object_facet_hier:\"1/HII Region/9000000\""),
        "//doc/int[@name='recid'][.='100']",
        "//*[@numFound='1']"
    );

		/*
		 * citations - added 10/12/13
		 */

		assertQ(req("q", "citation:2014JNuM..455...10C"),
				"//doc/int[@name='recid'][.='100']",
				"//*[@numFound='1']"
		);

		/*
		 * citation_count
		 */
		
		assertQ(req("q", "citation_count:[0 TO 10]"),
        "//doc/int[@name='recid'][.='100']",
        "//*[@numFound='1']"
    );
		assertQ(req("q", "citation_count:[0 TO *]"),
        "//doc/int[@name='recid'][.='100']",
        "//*[@numFound='1']"
    );

		
		/*
		 * indexstamp
		 */
		assertQ(req("q", "indexstamp:[\"2012-10-01T00:00:00.000\" TO *]"), 
		    "//doc/int[@name='recid'][.='100']",
        "//*[@numFound>='20']"
    );
		
		
		/*
     * date
     */
    assertQ(req("q", "date:[\"2012-10-01T00:00:00\" TO *]"), 
        "//doc/int[@name='recid'][.='100']",
        "//*[@numFound>='1']"
    );
    
		
		/*
		 * reference
		 */

		assertQ(req("q", "reference:2014JNuM..455...10R"),
				"//doc/int[@name='recid'][.='100']",
				"//*[@numFound='1']"
		);

		/*
		 * pubdate - 17/12/2012 changed to be the date type
		 *
		 * we have records with these dates:
		 *    20: 1976-01-01
		 *    21: 1976-01-02
		 *    22: 1976-02-01
		 *    23: 1976-01-02
		 *    24: 1976-31-12
		 *    25: 1977-01-01
		 *
		 * for more complete tests, look at: TestAdsabsTypeDateParsing
		 */

		assertQ(req("q", "title:datetest"),
				"//*[@numFound='6']");
		assertQ(req("q", "pubdate:[1976 TO 1977]"),
				"//*[@numFound='6']");
		assertQ(req("q", "pubdate:1976"),
				"//*[@numFound='5']",
				"//doc/int[@name='recid'][.='20']",
				"//doc/int[@name='recid'][.='21']",
				"//doc/int[@name='recid'][.='22']",
				"//doc/int[@name='recid'][.='23']",
				"//doc/int[@name='recid'][.='24']"
		);

		
		/*
		 * update_timestamp
		 */
		assertQ(req("q", "update_timestamp:[\"2010-01-04T22:01:32.000\" TO \"2010-08-04T22:01:32.999\"]"),
        "//*[@numFound='1']",
        "//doc/int[@name='recid'][.='100']"
    );
		assertQ(req("q", "update_timestamp:[\"2010-03-04T22:01:32.109Z\" TO \"2010-03-04T22:01:33.099Z\"]"),
		    "//*[@numFound='1']",
		    "//doc/int[@name='recid'][.='100']"
		    );


		/*
		 * year
		 */

		assertQ(req("q", "year:2013"),
        "//doc[1]/int[@name='recid'][.='100']"
        );
		assertQ(req("q", "year:[2011 TO 2014]"),
        "//doc[1]/int[@name='recid'][.='100']"
        );
		assertQ(req("q", "year:1995-2013"),
        "//doc[1]/int[@name='recid'][.='100']"
        );
		assertQueryEquals(req("q", "year:1995-1996", "debugQuery", "true"), "year:[1995 TO 1996]", TermRangeQuery.class);

		/*
		 * links_data (generated and stored as JSON for display purposes)
		 * ids_data (generated and stored as JSON for display purposes)
		 */
		assertQ(req("q", "id:100"),
				"//doc/arr[@name='links_data']/str[contains(text(),'MAST')]",
				"//doc/arr[@name='links_data']/str[contains(text(),'{\"foo\": [\"bar\", \"baz\"], \"one\": {\"two\": \"three\"}}')]"
				);




		/*
		 * 2nd order queries
		 */

		// references/citations() - see TestSolrCitationQuery

		// what other papers we cite
		assertQ(req("q", "references(*:*)"),
				"//*[@numFound='3']");
		assertQ(req("q", "references(id:100)"),
				"//*[@numFound='2']",
				"//doc/int[@name='recid'][.='101']",
				"//doc/int[@name='recid'][.='102']");
		
		// make sure the citation search optimization (short clause first)
		// works well
		assertQ(req("q", "citations(*:*) id:100"),
        "//*[@numFound='1']",
        "//doc/int[@name='recid'][.='100']");
		assertQ(req("q", "references(id:100) *:*"),
		    "//*[@numFound='2']",
        "//doc/int[@name='recid'][.='101']",
        "//doc/int[@name='recid'][.='102']");
		
		// who cites us
		assertQ(req("q", "citations(*:*)"),
		    "//*[@numFound='3']");
		assertQ(req("q", "citations(id:101)"),
				"//*[@numFound='1']",
		    "//doc/int[@name='recid'][.='100']");

		// just check they are working
		assertQ(req("q", "useful(*:*)"),
				"//*[@numFound='3']");
		assertQ(req("q", "reviews(*:*)"),
				"//*[@numFound='3']");


		// cut only the first n results
		assertQ(req("q", "topn(2, reviews(*:*))"),
		"//*[@numFound='2']");

		//dumpDoc(null, "id", "recid", "title");
		assertQ(req("q", "topn(5, recid:[1 TO 10], id asc)"),
				"//*[@numFound='5']",
				"//doc[1]/int[@name='recid'][.='1']",
				"//doc[2]/int[@name='recid'][.='2']",
				"//doc[3]/int[@name='recid'][.='3']",
				"//doc[4]/int[@name='recid'][.='4']");

		// TODO: I am too tired now to find out why the sorting is weird
		// but found it must be!
    //assertQ(req("q", "topn(5, recid:[1 TO 10], \"recid desc\")", "fl", "recid"),
		//		"//*[@numFound='5']",
		//		"//doc[1]/int[@name='recid'][.='7']",
		//		"//doc[2]/int[@name='recid'][.='6']",
		//		"//doc[3]/int[@name='recid'][.='5']",
		//		"//doc[4]/int[@name='recid'][.='4']");

		// trending() - what people read
		assertQ(req("q", "trending(*:*)"),
				"//*[@numFound>='2']",
				"//doc[1]/int[@name='recid'][.='101']",
				"//doc[2]/int[@name='recid'][.='103']"
		);



		// test we can search for all docs that have certain field
		assertQ(req("q", "reference:*"),
				"//doc[1]/int[@name='recid'][.='100']"
		);
		assertQ(req("q", "id:?"), // but works only for text fields
				"//*[@numFound='8']"
		);

    /**
     * doctype
     */
    assertQ(req("q", "doctype:article"),
        "//*[@numFound='1']",
        "//doc/int[@name='recid'][.='100']");
    
    /**
     * doctype_facet_hier
     */
    assertQ(req("q", "doctype_facet_hier:\"0/Article\""),
        "//*[@numFound='1']",
        "//doc/int[@name='recid'][.='100']");
    assertQ(req("q", "doctype_facet_hier:\"1/Article/Book chapter\""),
        "//*[@numFound='1']",
        "//doc/int[@name='recid'][.='100']");
    
    
    /**
     * Author search must give the same results if we use
     * pos() or ^author
     */
    assertQ(req("q", "author:bond"),
        "//*[@numFound='8']"
    );
    assertQ(req("q", "author:\"^bond\""),
        "//*[@numFound='8']"
    );

    assertQ(req("q", "author:\"bond, edwin james\""),
        "//*[@numFound='6']",
        "//doc/int[@name='recid'][.='50']",
        "//doc/int[@name='recid'][.='51']",
        "//doc/int[@name='recid'][.='52']",
        "//doc/int[@name='recid'][.='53']",
        "//doc/int[@name='recid'][.='55']",
        "//doc/int[@name='recid'][.='57']"
    );
    assertQ(req("q", "author:\"^bond, edwin james\""),
        "//*[@numFound='6']",
        "//doc/int[@name='recid'][.='50']",
        "//doc/int[@name='recid'][.='51']",
        "//doc/int[@name='recid'][.='52']",
        "//doc/int[@name='recid'][.='53']",
        "//doc/int[@name='recid'][.='55']",
        "//doc/int[@name='recid'][.='57']"
    );
    assertQ(req("q", "pos(author:\"bond, edwin james\", 1, 2)"),
        "//*[@numFound='6']",
        "//doc/int[@name='recid'][.='50']",
        "//doc/int[@name='recid'][.='51']",
        "//doc/int[@name='recid'][.='52']",
        "//doc/int[@name='recid'][.='53']",
        "//doc/int[@name='recid'][.='55']",
        "//doc/int[@name='recid'][.='57']"
    );
    
    
    /*
     * nedid
     * nedtype
     * ned_object_facet_hier
     */

    assertQ(req("q", "nedid:\"FoO bar\""),
        "//doc[1]/int[@name='recid'][.='100']"
        );
    assertQ(req("q", "nedtype:Other"),
        "//doc[1]/int[@name='recid'][.='100']"
        );
    assertQ(req("q", "ned_object_facet_hier:\"0/OTHER       \""),
        "//doc[1]/int[@name='recid'][.='100']"
        );
    
    /*
     * page_count
     */

    assertQ(req("q", "page_count:23"),
        "//doc[1]/int[@name='recid'][.='100']"
        );
    assertQ(req("q", "page_count:[10 TO 24]"),
        "//doc[1]/int[@name='recid'][.='100']"
        );
    
    /*
     * page_range
     */
    
    assertQ(req("q", "page_range:23"),
        "//*[@numFound='0']" // not searchable
        );
    assertQ(req("q", "page:55"),
        "//doc[1]/str[@name='page_range'][.='23-55s']"
        );
    
    
    /*
     * entry_date + ctimes and modtimes
     */
    
    assertQ(req("q", "entry_date:\"2017-09-19T11:01:32.809+00:00\""),
        "//doc[1]/int[@name='recid'][.='100']"
        );
    assertQ(req("q", "entry_date:[\"2017-09-19T11:01:32.808Z\" TO \"2017-09-19T11:01:32.810Z\"]"),
        "//doc[1]/int[@name='recid'][.='100']"
        );
    assertQ(req("q", "entry_date:[\"2017-09-19T11:01:32.810Z\" TO \"2017-09-19T11:01:32.811Z\"]"),
        "//*[@numFound='0']"
        );
    
    for (String s: "metadata orcid fulltext nonbib metrics".split(" ")) {
      assertQ(req("q", s + "_ctime:\"2017-09-19T11:01:32.809Z\""),
          "//doc[1]/int[@name='recid'][.='100']"
          );
      assertQ(req("q", s + "_mtime:\"2017-09-19T11:01:32.809Z\""),
          "//doc[1]/int[@name='recid'][.='100']"
          );
      assertQ(req("q", s + "_ctime:[\"2010-01-04T22:01:32.000\" TO \"2019-08-04T22:01:32.999\"]"),
          "//*[@numFound='1']",
          "//doc/int[@name='recid'][.='100']"
      );
      assertQ(req("q", s + "_mtime:[\"2010-01-04T22:01:32.000\" TO \"2019-08-04T22:01:32.000b\"]"),
          "//*[@numFound='1']",
          "//doc/int[@name='recid'][.='100']"
      );
    }
    
    /*
     * origin
     */
    
    assertQ(req("q", "origin:SPRINGER AND origin:\"ADS METADATA\""),
        "//doc[1]/int[@name='recid'][.='100']"
        );

    
    /*
     * data and data_count
     * 
     */
    
    assertQ(req("q", "data_count:[18 TO 21]"),
        "//doc[1]/int[@name='recid'][.='100']"
        );
    
    assertQ(req("q", "data:(nEd OR foo)"),
        "//doc[1]/int[@name='recid'][.='100']"
        );
    assertQ(req("q", "data:\"NED:999\""), // numbers should be ignored in search, but stored
        "//doc[1]/int[@name='recid'][.='100']",
        "//doc[1]/arr[@name='data']/str[contains(text(),'NED:15')]"
        );
    
    /*
     * esources
     * 
     */
    assertQ(req("q", "esources:pub_pDF"),
        "//doc[1]/int[@name='recid'][.='100']"
        );
    
	}
}
