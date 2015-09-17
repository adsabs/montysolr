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
package org.apache.solr.search;

import java.io.IOException;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;

import monty.solr.util.MontySolrAbstractTestCase;
import monty.solr.util.MontySolrSetup;

import org.apache.solr.common.util.ContentStream;
import org.apache.solr.common.util.ContentStreamBase;
import org.apache.solr.common.util.NamedList;
import org.apache.solr.request.SolrQueryRequestBase;
import org.junit.BeforeClass;
import org.junit.Test;

public class TestBitSetQParserPlugin extends MontySolrAbstractTestCase {

	@BeforeClass
	public static void beforeClass() throws Exception {
		
		makeResourcesVisible(Thread.currentThread().getContextClassLoader(), new String[] {
			    MontySolrSetup.getMontySolrHome() + "/contrib/adsabs/src/test-files/solr/collection1/conf",
		      MontySolrSetup.getSolrHome() + "/example/solr/collection1/conf"
		    });
				
		System.setProperty("solr.allow.unsafe.resourceloading", "true");
		schemaString = MontySolrSetup.getMontySolrHome() + "/contrib/adsabs/src/test-files/solr/collection1/conf/"
				  +	"schema-minimal.xml";
		
		configString = MontySolrSetup.getMontySolrHome() + "/contrib/adsabs/src/test-files/solr/collection1/conf/" 
					+ "bitset-solrconfig.xml";
		
		initCore(configString, schemaString, MontySolrSetup.getSolrHome()
			    + "/example/solr");
	}
	

	public void createIndex() {
		assertU(adoc("id","1","recid","1", "text", "who"));
		assertU(adoc("id","2","recid","2", "text", "is stopword"));
		assertU(adoc("id","3","recid","3", "text", "able"));
		assertU(adoc("id","4","recid","4", "text", "to stopword"));
		assertU(adoc("id","5","recid","5", "text", "exchange"));
		assertU(commit("waitSearcher", "true"));
		
		assertU(adoc("id","16","recid","16", "text", "liberty"));
		assertU(adoc("id","17","recid","17", "text", "for stopword"));
		assertU(adoc("id","18","recid","18", "text", "safety"));
		assertU(adoc("id","19","recid","19", "text", "deserves"));
		assertU(adoc("id","20","recid","20", "text", "neither"));
		assertU(commit("waitSearcher", "true"));
	}

	@Override
	public void setUp() throws Exception {
		super.setUp();
		createIndex();
	}

	@Test
	public void test() throws IOException, Exception {


		BitSetQParserPlugin bqp = new BitSetQParserPlugin();
		bqp.init(new NamedList(){});


		assertQ(req("q","text:*", "indent","true")
				,"//*[@numFound='10']"
		);

		BitSet data = convert(new int[]{5,16});
		byte[] byteData = bqp.toByteArray(data);
		String base64string = bqp.encodeBase64(byteData);
		byte[] gzipData = bqp.doGZip(byteData);
		String gzipBase64string = bqp.encodeBase64(gzipData);

		assertEquals(base64string, "BACA");
		assertArrayEquals(byteData, bqp.decodeBase64(base64string));
		assertEquals(data, bqp.fromByteArray(bqp.decodeBase64(base64string)));

		assertEquals(gzipBase64string, "H4sIAAAAAAAAAGNhaAAA7vLwFQMAAAA=");
		assertArrayEquals(byteData, bqp.unGZip(gzipData));
		assertArrayEquals(byteData, bqp.unGZip(bqp.decodeBase64(gzipBase64string)));
		assertEquals(data, bqp.fromByteArray(bqp.unGZip(bqp.decodeBase64(gzipBase64string))));

		
		
	  // sending lucene doc-ids (just a test)
		SolrQueryRequestBase req = (SolrQueryRequestBase) req("q","text:*", 
				"fq","{!bitset compression=none}");
		List<ContentStream> streams = new ArrayList<ContentStream>(1);
    ContentStreamBase cs = new ContentStreamBase.StringStream("id\n5\n16");
    cs.setContentType("big-query/csv");
    streams.add(cs);
		req.setContentStreams(streams);
		
		assertQ(req
				,"//*[@numFound='2']",
				"//doc/str[@name='id'][.='5']",
				"//doc/str[@name='id'][.='16']"
		);
		
		// sending lucene doc-ids (these will not be translated)
		assertQ(req("q","text:*", "fq", 
				"{!bitset compression=none} " + bqp.encodeBase64(bqp.toByteArray(convert(new int[]{4,5}))))
				,"//*[@numFound='2']",
				"//doc/str[@name='id'][.='5']",
				"//doc/str[@name='id'][.='16']"
		);

		assertQ(req("q","text:*", "fq", "{!bitset field=id} " + base64string)
				,"//*[@numFound='2']",
				"//doc/str[@name='id'][.='5']",
				"//doc/str[@name='id'][.='16']"
		);

		assertQ(req("q","text:*", "fq", "{!bitset compression=none field=id} " + base64string)
				,"//*[@numFound='2']",
				"//doc/str[@name='id'][.='5']",
				"//doc/str[@name='id'][.='16']"
		);

		assertQ(req("q","text:*", "fq", "{!bitset compression=gzip field=id} " + gzipBase64string)
				,"//*[@numFound='2']",
				"//doc/str[@name='id'][.='5']",
				"//doc/str[@name='id'][.='16']"
		);
		
		// this is similar to the previous query, but the field is of type int
		// and will use a different cache
		assertQ(req("q","text:*", "fq", "{!bitset field=recid} " + base64string)
				,"//*[@numFound='2']",
				"//doc/str[@name='id'][.='5']",
				"//doc/str[@name='id'][.='16']"
		);
		
		// and finally non-sensical input
	  // sending lucene doc-ids (these will not be translated)
		assertQ(req("q","text:*", "fq", 
				"{!bitset compression=none} " + bqp.encodeBase64(bqp.toByteArray(convert(new int[]{40,500}))))
				,"//*[@numFound='0']"
		);
		assertQ(req("q","text:*", "fq", 
				"{!bitset field=id} " + bqp.encodeBase64(bqp.toByteArray(convert(new int[]{40,500}))))
				,"//*[@numFound='0']"
		);
	}



	private BitSet convert(int[] numbers) {
		BitSet bitSet = new BitSet();
		for (int i: numbers) {
			bitSet.set(i);
		}
		return bitSet;
	}

}


