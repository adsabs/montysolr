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
import java.util.BitSet;
import java.util.Random;
import java.util.zip.DeflaterOutputStream;
import java.util.zip.GZIPOutputStream;

import org.ads.solr.InvenioBitSet;
import org.apache.solr.SolrTestCaseJ4;
import org.apache.solr.common.util.Base64;
import org.junit.BeforeClass;
import org.junit.Test;

import com.jcraft.jzlib.JZlib;
import com.jcraft.jzlib.ZOutputStream;

public class TestBitSetQParserPlugin extends SolrTestCaseJ4 {
	@BeforeClass
	public static void beforeClass() throws Exception {
		initCore("solrconfig.xml", "schema12.xml");
		createIndex();
	}

	public static void createIndex() {
		assertU(adoc("id","1", "text", "who"));
		assertU(adoc("id","2", "text", "is"));
		assertU(adoc("id","3", "text", "able"));
		assertU(adoc("id","4", "text", "to"));
		assertU(adoc("id","5", "text", "exchange"));
		assertU(adoc("id","6", "text", "liberty"));
		assertU(adoc("id","7", "text", "for"));
		assertU(adoc("id","8", "text", "safety"));
		assertU(adoc("id","9", "text", "deserves"));
		assertU(adoc("id","10", "text", "neither"));
		assertU(commit());
	}

	@Test
	public void testPhrase() throws IOException, Exception {
		
		System.out.println(enc(new int[]{5,6}));
		System.out.println(enc(new int[]{5,6,1000000}));
		System.out.println(enc(new int[]{5,6,1000000}));
		System.out.println(enc(new int[]{5,6,10000000}));
		System.out.println(enc(new int[]{5,6,1000000}));
		System.out.println(enc(new int[]{5,6,34534,35345,2345,698,69789,10000000}));
		
		int[] x = new int[1000000];
		float max = x.length * 0.5f;
		int i = 0;
		Random r = random();
		while (i < max) {
			x[r.nextInt(x.length)] = r.nextInt(1000000);
			i++;
		}
		enc(x);
		enc(x);
		enc(x);
		
		
		assertQ(req("q","text:*", "indent","true")
				,"//*[@numFound='10']"
		);

		assertQ(req("q","text:*", "fq", "{!bitset} " + enc(new int[]{5,6}))
				,"//*[@numFound='2']"
		);
	}

	
	private String enc(int[] data) throws IOException, Exception {
		t("zip", encode(getBitSetDeflated(data)));
		t("gzip", encode(getBitSetGzip(data)));
		t("zlib", encode(getBitSet(data)));
		t("zeroes", encode(getBitSetString(data)));
		return "XX";
		
	}
	
	private void t(String name, String s) {
		System.out.println(name + " len=" + s.length());
	}
	
	private byte[] getBitSetString(int[] numbers) throws IOException {
		//InvenioBitSet bitSet = new InvenioBitSet();
		BitSet bitSet = new BitSet();
		for (int i: numbers) {
			bitSet.set(i);
		}
		
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
		zOut.write(bitSet.toByteArray());
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

		ByteArrayOutputStream out = new ByteArrayOutputStream();

		GZIPOutputStream zOut = new GZIPOutputStream(out);
		zOut.write(bitSet.toByteArray());
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
		zOut.write(bitSet.toByteArray());
		zOut.flush();
		zOut.close();
		System.out.printf("Deflater Compression ratio %f\n", (1.0f * out.size()/bitSet.cardinality()));
		return out.toByteArray();
	}


	public String encode(byte[] data) throws Exception {
		return Base64.byteArrayToBase64(data, 0, data.length);
	}

	public byte[] decode(String data) throws Exception {
		return Base64.base64ToByteArray(data);
	}
}


