package perf;

/*
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

import java.io.IOException;
import monty.solr.util.MontySolrAbstractTestCase;
import monty.solr.util.MontySolrSetup;

import org.junit.Test;

public class TestCreateQueries extends MontySolrAbstractTestCase {

	public String getSchemaFile() {
		makeResourcesVisible(this.solrConfig.getResourceLoader(),
				new String[] {MontySolrSetup.getMontySolrHome() + "/contrib/adsabs/src/test-files/solr/collection1/conf",
			MontySolrSetup.getSolrHome() + "/example/solr/collection1/conf"
		});
		return "schema.xml";
	}

	public String getSolrConfigFile() {
		return MontySolrSetup.getMontySolrHome() + "/contrib/adsabs/src/test-files/solr/collection1/conf/" + 
		"perf-solrconfig.xml";
	}

	public void createIndex() {
		assertU(adoc("id","1", "text", "who"));
		assertU(adoc("id","2", "text", "is stopword giving some changes for collocations"));
		assertU(adoc("id","3", "text", "able maple tree"));
		assertU(adoc("id","4", "text", "to stopword or not to stop word"));
		assertU(adoc("id","5", "text", "exchange change"));
		assertU(adoc("id","16", "text", "liberty property"));
		assertU(adoc("id","17", "text", "for stopword buzz word"));
		assertU(adoc("id","18", "text", "safety gives you neither"));
		assertU(adoc("id","19", "text", "deserves preserve deserters"));
		assertU(adoc("id","20", "text", "neither"));
		assertU(commit());
	}

	@Override
	public void setUp() throws Exception {
		super.setUp();
		createIndex();
	}

	@Test
	public void test() throws IOException, Exception {
		
		
//		System.out.println(h.query(req("qt", "/perf", "fields","text,id", "topN", "20", "numQueries", "5")));
//		System.out.println(h.query(req("qt", "/perf", "fields","text,id", "topN", "20", "numQueries", "5", 
//				"defType", "aqp", "runTotalHitsResolution", "true")));
//		System.out.println(h.query(req("qt", "/perf", "fields","text,id", "topN", "20", 
//				"numQueries", "5", "runTotalHitsResolution", "true", "defType", "aqp", "indent", "true")));
		
		assertQ(req("qt", "/perf", "fields","text,id", "topN", "20", 
				"numQueries", "5", "defType", "aqp"),
				"//lst[@name='text']/str[@name='termQueriesHighFreq'][contains(text(),'for\t#freq=2')]",
				"//lst[@name='text']/str[@name='wildcardQueriesBeginStarHighFreq'][contains(text(),'*ple\t#freq=1')]",
				"//lst[@name='text']/str[@name='wildcardQueriesEndStarHighFreq'][contains(text(),'lib*\t#freq=1')]",
				"//lst[@name='text']/str[@name='wildcardQueriesMidStarHighFreq'][contains(text(),'exch*e\t#freq=1')]",
				"//lst[@name='text']/str[@name='fuzzyQueries1HighFreq'][contains(text(),'\"stopword\"~1\t#freq=3')]",
				"//lst[@name='text']/str[@name='fuzzyQueries2HighFreq'][contains(text(),'\"stopword\"~2\t#freq=3')]"
		);

		assertQ(req("qt", "/perf", "fields","text,id", "topN", "20", 
				"numQueries", "5", "runTotalHitsResolution", "true", "defType", "aqp"),
				"//lst[@name='text']/str[@name='termQueriesHighFreq'][contains(text(),'for\t#freq=2')]",
				"//lst[@name='text']/str[@name='wildcardQueriesBeginStarHighFreq'][contains(text(),'*ple\t#freq=1\t#numFound=1')]",
				"//lst[@name='text']/str[@name='wildcardQueriesEndStarHighFreq'][contains(text(),'lib*\t#freq=1\t#numFound=1')]",
				"//lst[@name='text']/str[@name='wildcardQueriesMidStarHighFreq'][contains(text(),'exch*e\t#freq=1\t#numFound=1')]",
				"//lst[@name='text']/str[@name='fuzzyQueries1HighFreq'][contains(text(),'\"stopword\"~1\t#freq=3\t#numFound=3')]",
				"//lst[@name='text']/str[@name='fuzzyQueries2HighFreq'][contains(text(),'\"stopword\"~2\t#freq=3\t#numFound=3')]"
		);

		assertQ(req("qt", "/perf", "fields","text,nonfield", "topN", "20", 
				"numQueries", "5", "runTotalHitsResolution", "true", "defType", "aqp"),
				"//lst[@name='text']/str[@name='termQueriesHighFreq'][contains(text(),'for\t#freq=2')]",
				"//lst[@name='text']/str[@name='wildcardQueriesBeginStarHighFreq'][contains(text(),'*ple\t#freq=1\t#numFound=1')]",
				"//lst[@name='text']/str[@name='wildcardQueriesEndStarHighFreq'][contains(text(),'lib*\t#freq=1\t#numFound=1')]",
				"//lst[@name='text']/str[@name='wildcardQueriesMidStarHighFreq'][contains(text(),'exch*e\t#freq=1\t#numFound=1')]",
				"//lst[@name='text']/str[@name='fuzzyQueries1HighFreq'][contains(text(),'\"stopword\"~1\t#freq=3\t#numFound=3')]",
				"//lst[@name='text']/str[@name='fuzzyQueries2HighFreq'][contains(text(),'\"stopword\"~2\t#freq=3\t#numFound=3')]",
				"//lst[@name='nonfield']/str[@name='error'][contains(text(),'does not exist')]"
		);
		
	}





}


