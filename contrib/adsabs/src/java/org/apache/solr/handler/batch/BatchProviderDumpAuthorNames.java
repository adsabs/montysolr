package org.apache.solr.handler.batch;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.tokenattributes.TypeAttribute;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.LeafReader;
import org.apache.lucene.index.LeafReaderContext;
import org.apache.lucene.search.MatchAllDocsQuery;
import org.apache.lucene.search.SimpleCollector;
import org.apache.solr.analysis.WriteableExplicitSynonymMap;
import org.apache.solr.analysis.WriteableSynonymMap;
import org.apache.solr.analysis.author.AuthorUtils;
import org.apache.solr.common.params.SolrParams;
import org.apache.solr.common.util.StrUtils;
import org.apache.solr.core.SolrCore;
import org.apache.solr.request.SolrQueryRequest;
import org.apache.solr.schema.IndexSchema;
import org.apache.solr.schema.SchemaField;
import org.apache.solr.search.SolrIndexSearcher;

/**
 * Provider which collects all author names,
 * and saves their ASCII => UTF8 mapping to disc. 
 *
 */
public class BatchProviderDumpAuthorNames extends BatchProvider {
	
	public String sourceField = "author";
	public String analyzerField = "author_collector";

	public void run(SolrQueryRequest req, BatchHandlerRequestQueue queue) throws Exception {
		
		SolrParams params = req.getParams();
	  String jobid = params.get("jobid");
	  String workDir = params.get("#workdir");
	  
		SolrCore core = req.getCore();
    IndexSchema schema = req.getSchema();
    
    BatchHandlerRequestData data = queue.pop();
    
    SchemaField field = core.getLatestSchema().getFieldOrNull(sourceField);
    
    if (field==null || !field.stored()) {
      data.setMsg("We cannot dump fields that are not stored: " + sourceField);
      queue.registerFailedBatch(this, data);
      return;
    }
    
    final Analyzer analyzer = core.getLatestSchema().getQueryAnalyzer();
    
    SchemaField tField = core.getLatestSchema().getFieldOrNull(analyzerField);
    
    if (tField == null) {
      data.setMsg("We cannot find analyzer for: " + analyzerField);
      queue.registerFailedBatch(this, data);
      return;
    }
    
    final String targetAnalyzer = analyzerField;
    SolrIndexSearcher se = req.getSearcher();
    
    final HashSet<String> fieldsToLoad = new HashSet<String>();
    fieldsToLoad.add(sourceField);
    
    File jobFile = new File(workDir + "/" + jobid);
    final WriteableSynonymMap synMap = createSynonymMap();
    synMap.setOutput(jobFile.getAbsolutePath());
		
    se.search(new MatchAllDocsQuery(), new SimpleCollector() {
      private LeafReader reader;
      private int i = 0;
      private Set<String> tokenBuffer = new LinkedHashSet<String>();
      private CharTermAttribute termAtt;
      private TypeAttribute typeAtt;
      private String authorInput;
      
      protected void doSetNextReader(LeafReaderContext context) throws IOException {
        reader = context.reader();
      }
      @Override
      public void collect(int i) {
        Document d;
        try {
          d = reader.document(i, fieldsToLoad);
          
          for (String f: fieldsToLoad) {
            String[] vals = d.getValues(f);
            
            for (String s: vals) {
            	//System.out.println(s);
              TokenStream ts = analyzer.tokenStream(targetAnalyzer, new StringReader(s));
              ts.reset();
              
						  while(ts.incrementToken()) {
						  	termAtt = ts.getAttribute(CharTermAttribute.class);
						  	typeAtt = ts.getAttribute(TypeAttribute.class);
              
						  	if (typeAtt.type().equals(AuthorUtils.AUTHOR_INPUT)) {
						  		addTokensToSynMap();
						      authorInput = termAtt.toString();
						      //System.out.println("authorInput " + authorInput);
						    }
						  	else {
						  		tokenBuffer.add(termAtt.toString());
						  	}
						  }
						  
						  addTokensToSynMap();
              ts.close();
            }
          }
        } catch (IOException e) {
          // pass
        }
      }
      
      private void addTokensToSynMap() {
		    if (tokenBuffer.size()>0) {
		    	if (authorInput != null && authorInput.length() >= 4 && authorInput.split(" ").length <= 5) // ignore obvious mistakes
		    		synMap.add(authorInput, tokenBuffer);
		      tokenBuffer.clear();
		      authorInput=null;
		    }
		  }
      @Override
      public boolean needsScores() {
        return false;
      }
    });
    
    synMap.persist();
    
  }
	
	@Override
  public String getDescription() {
	  return "Collects author names and saves them to disk as synonym file";
  }

  private WriteableSynonymMap createSynonymMap() {
  	
    return new WriteableExplicitSynonymMap() { // no configuration allowed!
      /*
       * This synonym map has ascii forms as a key
       * and the upgraded utf8 values are values, 
       * when it is persisted, it will generate the
       * variation of the names. I.e. 
       * MÜLLER, BILL
       * 
       * will become
       * 
       *    MULLER, BILL=>MÜLLER, BILL
       *    MUELLER, BILL=>MÜLLER, BILL
       *    MULLER, B=>MÜLLER, B
       *    MUELLER, B=>MÜLLER, B
       *    MULLER,=>MÜLLER,
       * 
       * (non-Javadoc)
       * @see org.apache.solr.analysis.WriteableExplicitSynonymMap#add(java.lang.String, java.util.Set)
       */
      
      @Override
      public void populateMap(List<String> rules) {
        HashSet<String> hs = new HashSet<String>();
        for (String rule : rules) {
          List<String> mapping = StrUtils.splitSmart(rule, "=>", false);
          if (mapping.size() != 2) {
            log.error("Invalid Synonym Rule:" + rule);
            continue;
          }
          String key = mapping.get(0).trim().replace("\\,", ",").replace("\\ ", " ");
          hs.clear();
          hs.add(key);
          for (String val: splitValues(mapping.get(1))) {
            add(val, hs);
          }
          
        }
      }

      
      @Override
      public void add(String origName, Set<String> values) {
        // key = the original author input (possibly with utf8 characters)
        // values = set of transliterated values
        Set<String> masterSet = null;
        for (String key: values) {
          if (containsKey(key)) {
            masterSet = get(key);
            break;
          }
        }
        
        if (masterSet==null) { 
          masterSet = new LinkedHashSet<String>();
        }
        masterSet.add(origName);
        
        for (String key: values) {
          put(key, masterSet);
        }
      }
      
      @Override
      public String formatEntry(String key, Set<String>values) {
        List<String> rows = new ArrayList<String>();

        // remove all but the first comma
        //System.out.println("before replace " + key);
        key = key.replaceAll("\\G((?!^).*?|[^,]*,.*?),", "$1"); //"agusan, Adrian, , Dr" -> "agusan, Adrian Dr"
        key = AuthorUtils.normalizeAuthor(key);
        //System.out.println("after replace " + key);
            
        String[] nameParts = key.split(" ");
        if (nameParts.length > 1) {
          //nameParts[0] = nameParts[0].replace(",", "\\,");
          String[][] otherNames = new String[values.size()][];
          int n = 0;
          for (String name: values) {
          	// remove all but the first comma
          	name = name.replaceAll("\\G((?!^).*?|[^,]*,.*?),", "$1");
	        	name = AuthorUtils.normalizeAuthor(name);
            otherNames[n++] = name.split(" ");
            //otherNames[n-1][0] = otherNames[n-1][0].replace(",", "\\,"); 
          }
          int cycle=0;
          do {
          	
            for (n=0;n<nameParts.length;n++) {
            	StringBuffer out = new StringBuffer();
              if (cycle>0 && n==0) continue;
              out.append(join(nameParts, n));
              out.append("=>");
              boolean notFirst = false;
              for (String[] other: otherNames) {
                if (notFirst) out.append(";");
                out.append(join(other, n));
                notFirst = true;
              }
              rows.add(out.toString());
            }
            cycle++;
          } while (shortened(nameParts, otherNames));
        }
        
        // cleanup entries, keep only those that have non-ascii character
        StringBuffer toReturn = new StringBuffer();
        for (String row: rows) {
        	if (hasNonAscii(row)) {
        		toReturn.append(row);
        		toReturn.append("\n");
        	}
        }
        return toReturn.toString();
      }
      
      private boolean hasNonAscii(String s) {
      	for (char c: s.toCharArray()) {
      		if ((int)c > 128) {
      			return true;
      		}
      	}
      	return false;
      }
      
      private String join(String[] name, int v) {
        StringBuffer out = new StringBuffer();
        boolean notFirst = false;
        for (int i=0;i<=v;i++) {
          if (notFirst) out.append(" ");
          out.append(name[i]);
          notFirst=true;
        }
        return out.toString();
      }
      
      private boolean shortened(String[]nameParts, String[][] otherNames) {
        for (int i=nameParts.length-1;i>0;i--) {
          if (nameParts[i].length() > 1) {
            nameParts[i] = nameParts[i].substring(0, 1);
            for (String[] other: otherNames) {
              if (other[i] == null || other[i].length() < 2)
                return false;  // this may happen if synonyms map the name to a shorter version, my solution is to stop processing (cheap?)
              other[i] = other[i].substring(0, 1);
            }
            return true;
          }
        }
        return false;
      }
    };
	}

}
