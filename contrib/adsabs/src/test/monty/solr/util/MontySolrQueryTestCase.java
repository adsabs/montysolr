package monty.solr.util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexableField;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.flexible.aqp.AqpTestAbstractCase;
import org.apache.lucene.search.Query;
import org.apache.solr.SolrTestCaseJ4;
import org.apache.solr.common.params.CommonParams;
import org.apache.solr.common.params.SolrParams;
import org.apache.solr.request.SolrQueryRequest;
import org.apache.solr.search.QParser;
import org.apache.solr.search.QueryParsing;
import org.apache.solr.search.SyntaxError;
import org.getopt.luke.DocReconstructor;
import org.getopt.luke.DocReconstructor.Reconstructed;
import org.getopt.luke.GrowableStringArray;
import org.junit.BeforeClass;


public class MontySolrQueryTestCase extends MontySolrAbstractTestCase {

	protected static AqpTestAbstractCase tp = new AqpTestAbstractCase() {
    @Override
    public void setUp() throws Exception {
      super.setUp();
    }
    
    @Override
    public void tearDown() throws Exception {
      super.tearDown();
    }
  };
  
  private int idValue = 0;
  
	
	@Override
	public void setUp() throws Exception {
		super.setUp();
		tp.setUp();
	}
	
	@Override
	public void tearDown() throws Exception {
		
		tp.tearDown();
		super.tearDown();
		
	}
	
	
	public QParser getParser(SolrQueryRequest req) throws SyntaxError, InstantiationException, IllegalAccessException, SecurityException, IllegalArgumentException, NoSuchMethodException, InvocationTargetException {
		SolrParams params = req.getParams();
		String query = params.get(CommonParams.Q);
		String defType = params.get(QueryParsing.DEFTYPE);
		QParser qParser = QParser.getParser(query, defType, req);
		
		// if of type AqpQueryParser - set the debug
		try {
  		Class<? extends QParser> clazz = qParser.getClass();
  		Method getParser = clazz.getDeclaredMethod("getParser");
  		if (getParser != null) {
  		  Method setDebug = getParser.getReturnType().getDeclaredMethod("setDebug", boolean.class);
  		  
  		  Object arglist[] = new Object[1];
  	    arglist[0] = this.tp.debugParser;
  	    
  		  Object parser = getParser.invoke(qParser);
  		  setDebug.invoke(parser, arglist);
  		}
		}
		catch (Exception e){
		  // pass
		}
		return qParser;
		
	}
	
	public static SolrQueryRequest req(String... q) {
	  boolean clean = true;
	  for (String x: q) {
	    if (q.equals("debugQuery")) {
	      clean = false;
	    }
	  }
	  if (clean) {
	    String[] nq = new String[q.length+2];
	    int i =0;
	    for (;i<q.length;i++) {
	      nq[i] = q[i];
	    }
	    nq[i++] = "debugQuery";
	    nq[i++] = tp.debugParser ? "true" : "false";
	    q = nq;
	  }
    return SolrTestCaseJ4.req(q);
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
				tp.debugFail(actual, expected, "Query is not: " + clazz + " but: " + q.getClass() + q.toString());
			}
		}
		
		return q;
	}
	
	public void assertQueryParseException(SolrQueryRequest req) throws Exception {
		try {
			getParser(req).parse();
		} catch (SyntaxError expected) {
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
		  System.out.println("===========================");
			d = reconstructor.reconstruct(dd);
			
			Set<String> fldMap = new HashSet<String>();
			for (String f: fields) {
				fldMap.add(f);
			}
			
			System.out.println("STORED FIELDS: " + dd);
			Map<String, IndexableField[]> sf = d.getStoredFields();
			for (Entry<String, IndexableField[]> es : sf.entrySet()) {
				String fld = es.getKey();
				if (fldMap.size() > 0 && !fldMap.contains(fld)) {
					continue;
				}
				System.out.println("field="+fld);
				IndexableField[] val = es.getValue();
				int j=0;
				for (IndexableField v : val) {
					System.out.println(" " + j + "\t: " + v.stringValue());
					j++;
				}
			}

      //if (true) continue;
			
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
	
  public String addDocs(String[] fields, String...values) {
    ArrayList<String> vals = new ArrayList<String>(Arrays.asList(values));
    String[] fieldsVals = new String[fields.length*(values.length*2)];
    int i = 0;
    for (String f: fields) {
      for (String v: values) {
        fieldsVals[i++] = f;
        fieldsVals[i++] = v;
      }
    }
    return addDocs(fieldsVals);
  }
  
  public String addDocs(String... fieldsAndValues) {
    ArrayList<String> fVals = new ArrayList<String>(Arrays.asList(fieldsAndValues));
    if (fVals.indexOf("id") == -1 || fVals.indexOf("id")%2==1) {
      fVals.add("id");
      fVals.add(Integer.toString(incrementId()));
    }
    if (fVals.indexOf("bibcode") == -1 || fVals.indexOf("bibcode")%2==1) {
      fVals.add("bibcode");
      String bibc = ("AAAAA........" + Integer.toString(idValue));
      fVals.add(bibc.substring(bibc.length()-13, bibc.length()));
    }
    String[] newVals = new String[fVals.size()];
    for (int i=0;i<fVals.size();i++) {
      newVals[i] = fVals.get(i);
    }
    return super.adoc(newVals);
  }
  
  public int incrementId() {
    return idValue++;
  }

  
  public static String[] formatSynonyms(String[] strings) {
    String[] newLines = new String[strings.length];
    int nl = 0;
    for (String line : strings) {
      StringBuilder out = new StringBuilder();
      String[] kv = line.split("=>");
      for (int i=0;i<kv.length;i++) {
        if (i>0) out.append("=>");
        String[] names = kv[i].split(";");
        for (int j=0;j<names.length;j++) {
          if (j>0) out.append(",");
          out.append(names[j].trim().replace(" ", "\\ ").replace(",", "\\,"));
        }
      }
      newLines[nl++] = out.toString();
    }
    return newLines;
  }
  
}
