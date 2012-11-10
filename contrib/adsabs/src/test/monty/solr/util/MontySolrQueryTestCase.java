package monty.solr.util;

import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;


import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexableField;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.flexible.aqp.AqpTestAbstractCase;
import org.apache.lucene.search.Query;
import org.apache.solr.common.params.CommonParams;
import org.apache.solr.common.params.SolrParams;
import org.apache.solr.request.SolrQueryRequest;
import org.apache.solr.search.AqpAdsabsQParser;
import org.apache.solr.search.QParser;
import org.getopt.luke.DocReconstructor;
import org.getopt.luke.DocReconstructor.Reconstructed;
import org.getopt.luke.GrowableStringArray;


public class MontySolrQueryTestCase extends MontySolrAbstractTestCase {

	protected AqpTestAbstractCase tp = null;
	
	@Override
	public String getSchemaFile() {
		throw new IllegalAccessError("You must override this method");
	}

	@Override
	public String getSolrConfigFile() {
		throw new IllegalAccessError("You must override this method");
	} 
	
	public void setUp() throws Exception {
		super.setUp();
		
		final MontySolrQueryTestCase that = this;
		
		tp = new AqpTestAbstractCase() {
			
			@Override
			public void setUp() throws Exception {
				super.setUp();
			}
			
			@Override
			public void tearDown() throws Exception {
				super.tearDown();
			}
			
			
		};
		tp.setUp();
	}
	
	public void tearDown() throws Exception {
		super.tearDown();
		
		tp.tearDown();
		tp = null;
		
	}
	
	public QParser getParser(SolrQueryRequest req) throws ParseException, InstantiationException, IllegalAccessException {
		SolrParams params = req.getParams();
		String query = params.get(CommonParams.Q);
		String qt = params.get(CommonParams.QT);
		QParser qParser = QParser.getParser(query, qt, req);
		
		if (qParser instanceof AqpAdsabsQParser) {
			((AqpAdsabsQParser) qParser).getParser().setDebug(tp.debugParser);
		}
		return qParser;
		
	}
	
	public Query assertQueryEquals(SolrQueryRequest req, String expected, Class<?> clazz)
		throws Exception {
		
		QParser qParser = getParser(req);
		String query = req.getParams().get(CommonParams.Q);
		Query q = qParser.parse();
		
		String actual = q.toString("field");
		if (!actual.equals(expected)) {
			tp.debugFail(query, expected, actual);
		}
		
		if (clazz != null) {
			if (!q.getClass().isAssignableFrom(clazz)) {
				tp.debugFail("Query is not: " + clazz + " but: " + q.getClass(), expected, "-->" + q.toString());
			}
		}
		
		return q;
	}
	
	public void assertQueryParseException(SolrQueryRequest req) throws Exception {
		try {
			getParser(req).parse();
		} catch (ParseException expected) {
			return;
		}
		tp.debugFail("ParseException expected, not thrown");
	}
	
	public void setDebug(boolean v) {
		tp.setDebug(v);
	}
	
	/*
	 * This is only for printing/debugging, DO NOT use this for testing!!!
	 * This method can go away
	 */
	public void dumpDoc(Integer docId, String...fields) throws Exception {
		//DirectoryReader reader = h.getCore().getSearcher().get().getIndexReader();
		SolrQueryRequest sr = req();
		
		
		//IndexReader reader = req.getSearcher().getIndexReader();
		IndexReader reader = sr.getSearcher().getTopReaderContext().reader();
		
		int[] docs;
		if (docId == null) {
			docs = new int[reader.numDocs()];
			for (int i=0;i<docs.length;i++) {
				docs[i] = i;
			}
		}
		else {
			docs = new int[]{docId};
		}
		
		DocReconstructor reconstructor = new DocReconstructor(reader, fields, 10);
		Reconstructed d;
		
		for (Integer dd: docs) {
			d = reconstructor.reconstruct(dd);
			
			Set<String> fldMap = new HashSet<String>();
			for (String f: fields) {
				fldMap.add(f);
			}
			
			System.out.println("INDEXED FIELDS: " + dd);
			Map<String, GrowableStringArray> rf = d.getReconstructedFields();
			for (Entry<String, GrowableStringArray> es : rf.entrySet()) {
				String fld = es.getKey();
				if (fldMap.size() > 0 && !fldMap.contains(fld)) {
					continue;
				}
				System.out.println(fld);
				System.out.println(docToString(es.getValue(), "\n"));
				
			}
			
			if (true) continue;
			
			System.out.println("STORED FIELDS: " + dd);
			Map<String, IndexableField[]> sf = d.getStoredFields();
			for (Entry<String, IndexableField[]> es : sf.entrySet()) {
				String fld = es.getKey();
				if (fldMap.size() > 0 && !fldMap.contains(fld)) {
					continue;
				}
				System.out.println(fld);
				IndexableField[] val = es.getValue();
				int j=0;
				for (IndexableField v : val) {
					System.out.println(" " + j + "\t: " + v.stringValue());
					j++;
				}
			}
		}
		sr.close();
	}
	
	private String docToString(GrowableStringArray doc, String separator) {
		StringBuffer sb = new StringBuffer();
		String sNull = "null";
		int k = 0, m = 0;
		for (int j = 0; j < doc.size(); j++) {
			if (doc.get(j) == null)
				k++;
			else {
				if (sb.length() > 0) sb.append(separator);
				if (m > 0 && m % 5 == 0) sb.append('\n');
				if (k > 0) {
					sb.append(sNull + "_" + k + separator);
					k = 0;
					m++;
				}
				sb.append(j + "\t:");
				sb.append(doc.get(j));
				m++;
			}
		}
		return sb.toString();
	}

}
