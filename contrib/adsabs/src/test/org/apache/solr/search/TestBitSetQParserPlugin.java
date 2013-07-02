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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.BitSet;
import java.util.Random;
import java.util.zip.DeflaterOutputStream;
import java.util.zip.GZIPOutputStream;

import monty.solr.util.MontySolrAbstractTestCase;
import monty.solr.util.MontySolrSetup;

import org.ads.solr.InvenioBitSet;
import org.apache.solr.SolrTestCaseJ4;
import org.apache.solr.common.util.Base64;
import org.apache.solr.common.util.NamedList;
import org.junit.BeforeClass;
import org.junit.Test;

import com.jcraft.jzlib.JZlib;
import com.jcraft.jzlib.ZOutputStream;

public class TestBitSetQParserPlugin extends MontySolrAbstractTestCase {

	public String getSchemaFile() {
		makeResourcesVisible(this.solrConfig.getResourceLoader(),
				new String[] {MontySolrSetup.getMontySolrHome() + "/contrib/adsabs/src/test-files/solr/collection1/conf",
			MontySolrSetup.getSolrHome() + "/example/solr/collection1/conf"
		});
		return "schema.xml";
	}

	public String getSolrConfigFile() {
		return MontySolrSetup.getMontySolrHome() + "/contrib/adsabs/src/test-files/solr/collection1/conf/" + 
		"bitset-solrconfig.xml";
	}

	public void createIndex() {
		assertU(adoc("id","1", "text", "who"));
		assertU(adoc("id","2", "text", "is stopword"));
		assertU(adoc("id","3", "text", "able"));
		assertU(adoc("id","4", "text", "to stopword"));
		assertU(adoc("id","5", "text", "exchange"));
		assertU(adoc("id","16", "text", "liberty"));
		assertU(adoc("id","17", "text", "for stopword"));
		assertU(adoc("id","18", "text", "safety"));
		assertU(adoc("id","19", "text", "deserves"));
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

//		System.out.println(enc(new int[]{5,6}));
//		System.out.println(enc(new int[]{5,6,1000000}));
//		System.out.println(enc(new int[]{5,6,1000000}));
//		System.out.println(enc(new int[]{5,6,10000000}));
//		System.out.println(enc(new int[]{5,6,1000000}));
//		System.out.println(enc(new int[]{5,6,34534,35345,2345,698,69789,10000000}));

//		int[] x = new int[1000000];
//		float max = x.length * 0.5f;
//		int i = 0;
//		Random r = random();
//		while (i < max) {
//			x[r.nextInt(x.length)] = r.nextInt(1000000);
//			i++;
//		}
//		enc(x);
		//		enc(x);
		//		enc(x);
		
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
		assertQ(req("q","text:*", "fq", 
				"{!bitset compression=none} " + bqp.encodeBase64(bqp.toByteArray(convert(new int[]{5,6,20,30}))))
				,"//*[@numFound='2']"
				);
		
		assertQ(req("q","text:*", "fq", "{!bitset field=id} " + base64string)
				,"//*[@numFound='2']",
				"//doc/int[@name='id'][.='5']",
				"//doc/int[@name='id'][.='16']"
				);
		
		assertQ(req("q","text:*", "fq", "{!bitset compression=none field=id} " + base64string)
				,"//*[@numFound='2']",
				"//doc/int[@name='id'][.='5']",
				"//doc/int[@name='id'][.='16']"
				);
		
		assertQ(req("q","text:*", "fq", "{!bitset compression=gzip field=id} " + gzipBase64string)
				,"//*[@numFound='2']",
				"//doc/int[@name='id'][.='5']",
				"//doc/int[@name='id'][.='16']"
				);
		
	}


	private String enc(int[] data) throws IOException, Exception {
		t("zip", encode(getBitSetDeflated(data)));
		t("gzip", encode(getBitSetGzip(data)));
		t("zlib", encode(getBitSet(data)));
		//t("zeroes", encode(getBitSetString(data)));

		return encode(getBitSetGzip(data));

	}

	private void t(String name, String s) {
		System.out.println(name + " len=" + s.length());
	}
	
	private BitSet convert(int[] numbers) {
		BitSet bitSet = new BitSet();
		for (int i: numbers) {
			bitSet.set(i);
		}
		return bitSet;
	}
	
	private byte[] getBitSetString(BitSet bitSet) throws IOException {
		
		StringBuffer buffer = new StringBuffer();
		for (int i=0;i<bitSet.size();i++) {
			if (bitSet.get(i)) {
				buffer.append("1"); 
			}
			else {
				buffer.append("0");
			}
		}

		ByteArrayOutputStream out = new ByteArrayOutputStream();

		DeflaterOutputStream zOut = new DeflaterOutputStream(out);
		zOut.write(buffer.toString().getBytes());
		zOut.flush();
		zOut.close();
		System.out.printf("String Compression ratio %f\n", (1.0f * out.size()/bitSet.cardinality()));
		return out.toByteArray();
	}

	private byte[] getBitSetDeflated(int[] numbers) throws IOException {
		//InvenioBitSet bitSet = new InvenioBitSet();
		BitSet bitSet = new BitSet();
		for (int i: numbers) {
			bitSet.set(i);
		}



		ByteArrayOutputStream out = new ByteArrayOutputStream();

		DeflaterOutputStream zOut = new DeflaterOutputStream(out);
		//zOut.write(bitSet.toByteArray());
		zOut.write(toByteArray(bitSet));
		zOut.flush();
		zOut.close();
		System.out.printf("Deflater Compression ratio %f\n", (1.0f * out.size()/bitSet.cardinality()));
		return out.toByteArray();
	}

	private byte[] getBitSetGzip(int[] numbers) throws IOException {
		//InvenioBitSet bitSet = new InvenioBitSet();
		BitSet bitSet = new BitSet();
		for (int i: numbers) {
			bitSet.set(i);
		}
		System.out.println(bitSet);
		
		ByteArrayOutputStream out = new ByteArrayOutputStream();

		GZIPOutputStream zOut = new GZIPOutputStream(out);
		zOut.write(toByteArray(bitSet));
		zOut.flush();
		zOut.close();
		System.out.printf("Deflater Compression ratio %f\n", (1.0f * out.size()/bitSet.cardinality()));
		return out.toByteArray();
	}

	private byte[] getBitSet(int[] numbers) throws IOException {
		//InvenioBitSet bitSet = new InvenioBitSet();
		BitSet bitSet = new BitSet();
		for (int i: numbers) {
			bitSet.set(i);
		}


		ByteArrayOutputStream out = new ByteArrayOutputStream();

		ZOutputStream zOut = new ZOutputStream(out, JZlib.Z_BEST_SPEED);
		zOut.write(toByteArray(bitSet));
		zOut.flush();
		zOut.close();
		System.out.printf("Deflater Compression ratio %f\n", (1.0f * out.size()/bitSet.cardinality()));
		return out.toByteArray();
	}


	public String encode(byte[] data) throws Exception {
		System.out.println("to encode");
		for (byte b: data) {
			System.out.print(Byte.toString(b) + " ");
		}
		System.out.println("");
		return Base64.byteArrayToBase64(data, 0, data.length);
	}

	public byte[] decode(String data) throws Exception {
		return Base64.base64ToByteArray(data);
	}

	private byte[] toByteArray(BitSet bitSet) {
		// java6 doesn't have toByteArray()
		byte[] bytes = new byte[(bitSet.length() + 7) / 8];
		for ( int i = bitSet.nextSetBit(0); i >= 0; i = bitSet.nextSetBit(i+1) ) {
			bytes[i / 8] |= 128 >> (i % 8);
		}
		return bytes;
	}
}


