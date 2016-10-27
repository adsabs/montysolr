package org.apache.solr.search;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import org.apache.lucene.util.BitSet;
import org.apache.lucene.util.FixedBitSet;
import org.apache.solr.common.util.NamedList;
import org.junit.BeforeClass;
import org.junit.Test;

import monty.solr.util.MontySolrAbstractTestCase;
import monty.solr.util.MontySolrSetup;

public class BenchmarkBitSetQParserPlugin extends MontySolrAbstractTestCase {

	private int indexSize = 10000000;
	private long maxTime = 60*1000; // max time benchmark is allowed to run
	private ArrayList<ArrayList<Object>> timerStack = new ArrayList<ArrayList<Object>>();
	
	@BeforeClass
	public static void beforeClass() throws Exception {
		
		System.setProperty("solr.allow.unsafe.resourceloading", "true");
		schemaString = MontySolrSetup.getMontySolrHome()
				+ "/contrib/adsabs/src/test-files/solr/collection1/conf/"
				+ "schema-minimal.xml";

		configString = MontySolrSetup.getMontySolrHome()
				+ "/contrib/adsabs/src/test-files/solr/collection1/conf/"
				+ "bitset-solrconfig.xml";
		initCore(configString, schemaString, MontySolrSetup.getMontySolrHome() + "/contrib/adsabs/src/test-files/solr/collection1");
	}


	@Override
	public void setUp() throws Exception {
		super.setUp();
		//createIndex();
	}

	public void createIndex() {
		System.out.println("Building index of: " + indexSize);
		for (int i=0;i<indexSize;i++) {
			assertU(adoc("id", String.valueOf(i)));
			if (i % 100000 == 0) {
				System.out.println("Finished: " + i);
			}
		}
		assertU(commit());
	}

	@Test
	public void test() throws IOException, Exception {

		for (float fill: new float[]{0.05f, 0.1f, 0.25f, 0.5f, 0.75f, 0.9f, 1f}) {
			startTimer("run");
			startTimer("Building random bitset indexSize=" + indexSize + " fill=" + fill);
			BitSet r = randomBitSet(indexSize, fill);
			appendToTimer("Size=" +r.length() + ",cardinality=" +r.cardinality()+ " highestBit=" + r.prevSetBit(r.length()-1));
			stopTimer();

			BitSetQParserPlugin bqp = new BitSetQParserPlugin();
			bqp.init(new NamedList(){});
			startTimer("running");
			measure(bqp, r);
			stopTimer();
			stopTimer();
		}


	}

	private void measure(BitSetQParserPlugin bqp, BitSet data) throws Exception {

		startTimer("Converting bitset to byte array");
		byte[] byteData = bqp.toByteArray(data);
		appendToTimer("resulting array length=" + byteData.length);
		stopTimer();

		startTimer("Encoding byte array into base64");
		String base64string = bqp.encodeBase64(byteData);
		appendToTimer("resulting array length=" + base64string.length() + " ratio=" + 1.0f * base64string.length() / byteData.length);
		stopTimer();

		startTimer("Compressing byte array with GZIP");
		byte[] gzipData = bqp.doGZip(byteData);
		appendToTimer("resulting array length=" + gzipData.length + " ratio=" + 1.0f * gzipData.length / byteData.length);
		stopTimer();

		startTimer("Encoding gzipped byte array into base64");
		String gzipBase64string = bqp.encodeBase64(gzipData);
		appendToTimer("resulting string length=" + gzipBase64string.length() + " ratio=" + 1.0f * gzipBase64string.length() / byteData.length);
		stopTimer();

		startTimer("Decoding gzipped byte array from base64");
		byte[] d = bqp.decodeBase64(gzipBase64string);
		stopTimer();
		assertArrayEquals(d, gzipData);

		startTimer("Uncompressing decoded byte array");
		byte[] e = bqp.unGZip(gzipData);
		stopTimer();
		assertArrayEquals(e, byteData);

		startTimer("Converting from byte array to bitset");
		BitSet f = bqp.fromByteArray(e);
		stopTimer();

	}

	private BitSet randomBitSet(int size, float fill) {
	  BitSet bs = new FixedBitSet(size+1);
		float max = size * fill;
		int i = 0;
		Random r = random();
		while (i < max) {
			bs.set(r.nextInt(size));
			i++;
		}
		return bs;
	}

	private void startTimer(String message) {
		ArrayList<Object> l = new ArrayList<Object>();
		l.add(System.currentTimeMillis());
		l.add(message);
		timerStack.add(l);
	}

	private long stopTimer() {
		ArrayList<Object> l = timerStack.remove(timerStack.size()-1);
		long endTime = System.currentTimeMillis();
		long startTime = (Long) l.get(0);
		String msg = (String) l.get(1);

		long resTime = endTime - startTime;

		StringBuilder out = new StringBuilder();
		for (int i=0;i<timerStack.size();i++) {
			out.append("\t");
		}
		out.append(resTime);
		out.append("ms.  " + msg);
		System.out.println(out.toString());

		return resTime;
	}

	private void appendToTimer(String msg) {
		timerStack.get(timerStack.size()-1).set(1, (timerStack.get(timerStack.size()-1).get(1) + " -- " + msg));
	}

}
