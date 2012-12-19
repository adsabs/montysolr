package org.apache.solr.search;


import java.io.File;
import java.io.IOException;

import monty.solr.util.MontySolrQueryTestCase;
import monty.solr.util.MontySolrSetup;

import org.apache.lucene.queryparser.flexible.aqp.TestAqpAdsabs;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.DisjunctionMaxQuery;
import org.apache.lucene.search.MatchAllDocsQuery;
import org.apache.lucene.search.SecondOrderQuery;
import org.apache.lucene.search.TermQuery;


/**
 * This unittest is for queries that require solr core
 * 
 * @see TestAqpAdsabs for the other tests
 * @author rchyla
 * 
 * XXX: I was uneasy about the family of these tests
 * because they depend on the settings from the other 
 * project (contrib/examples) - on the other hand, I 
 * don't want to duplicate the code/config files. So, 
 * for now I resigned, and I think of contrib/examples
 * as a dependency for adsabs
 * 
 * contrib/examples should contain only a code for the
 * live site (setup), but we are developing components
 * for it here and we'll test it here 
 * (inside contrib/adsabs)
 * 
 * Let's do it pragmatically (not as code puritans)
 *
 */
public class TestAqpAdsabsSolrSearch extends MontySolrQueryTestCase {

	@Override
	public String getSchemaFile() {
		makeResourcesVisible(this.solrConfig.getResourceLoader(),
	    		new String[] {MontySolrSetup.getMontySolrHome() + "/contrib/examples/adsabs/solr/collection1/conf",
	    				      MontySolrSetup.getSolrHome() + "/example/solr/collection1/conf"
	    	});
		
		/*
		 * For purposes of the test, we make a copy of the schema.xml,
		 * and create our own synonym files
		 */
		
		String configFile = MontySolrSetup.getMontySolrHome()
			+ "/contrib/examples/adsabs/solr/collection1/conf/schema.xml";
		
		File newConfig;
		try {
			
			newConfig = duplicateFile(new File(configFile));
			
			File synonymsFile = createTempFile(new String[]{
					"Hst, hubble\\ space\\ telescope, HST",
			});
			replaceInFile(newConfig, "synonyms=\"ads_text.synonyms\"", "synonyms=\"" + synonymsFile.getAbsolutePath() + "\"");
			
		  // hand-curated synonyms
      File curatedSynonyms = createTempFile(new String[]{
          "JONES\\,\\ CHRISTINE,FORMAN\\,\\ CHRISTINE" // the famous post-synonym expansion
      });
      replaceInFile(newConfig, "synonyms=\"author_curated.synonyms\"", "synonyms=\"" + curatedSynonyms.getAbsolutePath() + "\"");
			
		} catch (IOException e) {
			e.printStackTrace();
			throw new IllegalStateException(e.getMessage());
		}
		
		return newConfig.getAbsolutePath();
	}

	public String getSolrConfigFile() {
		return MontySolrSetup.getMontySolrHome()
				+ "/contrib/examples/adsabs/solr/collection1/conf/solrconfig.xml";
	}
	

	
	
	public void test() throws Exception {
		
		/*
		 * Unfielded search should be expanded automatically by edismax
		 * 
		 * However, edismax is not smart enough to deal properly with boolean clauses
		 * and default operators, so I have decided to use the edismax on the "value"
		 * level only. First, we parse the query, then we let the edismax 
		 * expand it (actually, edismax will use the correct tokenizer chain
		 * to process authors for example) XXX: verify, it does use all the 
		 * elements of the processing chain - see forman, c => forman, christine
		 * For that, the synonym translation must be set up correctly
		 */
	  
	  // first the individual elements explicitly
	  assertQueryEquals(req("qt", "aqp", "q", "edismax(MÜLLER)", 
	      "qf", "author^2.3 title abstract^0.4"), 
        "(((abstract:acr::müller abstract:acr::muller)^0.4) " +
        "| ((author:müller, author:mueller, author:muller,)^2.3) " +
        "| (title:acr::müller title:acr::muller))", DisjunctionMaxQuery.class);
	  assertQueryEquals(req("qt", "aqp", "q", "edismax(\"forman, c\")", 
        "qf", "author^2.3 title abstract^0.4"), 
        "(abstract:\"forman c\"^0.4 " +
        "| ((author:forman, c author:forman,)^2.3) " +
        "| title:\"forman c\")", DisjunctionMaxQuery.class);

	  // unfielded search should produce the same structure (the parser for author is aqp-type)
	  assertQueryEquals(req("qt", "aqp", "q", "MÜLLER", 
        "qf", "author^2.3 title abstract^0.4"), 
        "(((abstract:acr::müller abstract:acr::muller)^0.4) " +
        "| ((author:müller, author:müller,* author:mueller, author:mueller,* author:muller, author:muller,*)^2.3) " +
        "| (title:acr::müller title:acr::muller))", DisjunctionMaxQuery.class);
    assertQueryEquals(req("qt", "aqp", "q", "\"forman, c\"", 
        "qf", "author^2.3 title abstract^0.4"), 
        "(abstract:\"forman c\"^0.4 " +
        "| ((author:forman, c author:jones, christine author:jones, c author:forman, christine author:forman, c* author:forman,)^2.3) " +
        "| title:\"forman c\")", DisjunctionMaxQuery.class);
	  
    // now add a normal element
    assertQueryEquals(req("qt", "aqp", "q", "title:foo or edismax(MÜLLER)", 
        "qf", "author^2.3 title abstract^0.4"), 
        "title:foo (((abstract:acr::müller abstract:acr::muller)^0.4) " +
        "| ((author:müller, author:mueller, author:muller,)^2.3) " +
        "| (title:acr::müller title:acr::muller))", BooleanQuery.class);
    assertQueryEquals(req("qt", "aqp", "q", "title:foo or edismax(\"forman, c\")", 
        "qf", "author^2.3 title abstract^0.4"), 
        "title:foo (abstract:\"forman c\"^0.4 " +
        "| ((author:forman, c author:forman,)^2.3) " +
        "| title:\"forman c\")", BooleanQuery.class);
    
    
    // unfielded search should produce the same results
    assertQueryEquals(req("qt", "aqp", "q", "title:foo or MÜLLER", 
        "qf", "author^2.3 title abstract^0.4"), 
        "title:foo (((abstract:acr::müller abstract:acr::muller)^0.4) " +
        "| ((author:müller, author:müller,* author:mueller, author:mueller,* author:muller, author:muller,*)^2.3) " +
        "| (title:acr::müller title:acr::muller))", BooleanQuery.class);
    assertQueryEquals(req("qt", "aqp", "q", "title:foo or \"forman, c\"", 
        "qf", "author^2.3 title abstract^0.4"), 
        "title:foo (abstract:\"forman c\"^0.4 " +
        "| ((author:forman, c author:jones, christine author:jones, c author:forman, christine author:forman, c* author:forman,)^2.3) " +
        "| title:\"forman c\")", BooleanQuery.class);
    
    assertQueryEquals(req("qt", "aqp", "q", "title:foo or MÜLLER", 
        "qf", "author^2.3 title abstract^0.4"), 
        "title:foo (((abstract:acr::müller abstract:acr::muller)^0.4) " +
        "| ((author:müller, author:müller,* author:mueller, author:mueller,* author:muller, author:muller,*)^2.3) " +
        "| (title:acr::müller title:acr::muller))", BooleanQuery.class);
    assertQueryEquals(req("qt", "aqp", "q", "title:foo or \"forman, c\"", 
        "qf", "author^2.3 title abstract^0.4"), 
        "title:foo (abstract:\"forman c\"^0.4 " +
        "| ((author:forman, c author:jones, christine author:jones, c author:forman, christine author:forman, c* author:forman,)^2.3) " +
        "| title:\"forman c\")", BooleanQuery.class);
    
    /*
     * It is different if Aqp handles the boolean operations or if 
     * edismax() does it. 
     * 
     * Aqp has more control, see: https://issues.apache.org/jira/browse/SOLR-4141
     */
    
		assertQueryEquals(req("qt", "aqp", "q", "edismax(dog OR cat)", "qf", "title^1 abstract^0.5"), //edismax 
				"(abstract:dog^0.5 | title:dog) (abstract:cat^0.5 | title:cat)", BooleanQuery.class);
		
		assertQueryEquals(req("qt", "aqp", "q", "dog OR cat", "qf", "title^1 abstract^0.5"),          //aqp
        "(abstract:dog^0.5 | title:dog) (abstract:cat^0.5 | title:cat)", BooleanQuery.class);
		
		assertQueryEquals(req("qt", "aqp", "q", "edismax(dog AND cat)", "qf", "title^1 abstract^0.5"), //edismax
				"+(abstract:dog^0.5 | title:dog) +(abstract:cat^0.5 | title:cat)", BooleanQuery.class);
		
		assertQueryEquals(req("qt", "aqp", "q", "dog AND cat", "qf", "title^1 abstract^0.5"), //aqp
        "+(abstract:dog^0.5 | title:dog) +(abstract:cat^0.5 | title:cat)", BooleanQuery.class);
		
		assertQueryEquals(req("qt", "aqp", "q", "edismax(dog OR cat)", "qf", "title^1 abstract^0.5"), //edismax
				"(abstract:dog^0.5 | title:dog) (abstract:cat^0.5 | title:cat)", BooleanQuery.class);
		
		assertQueryEquals(req("qt", "aqp", "q", "dog OR cat", "qf", "title^1 abstract^0.5"), //aqp
        "(abstract:dog^0.5 | title:dog) (abstract:cat^0.5 | title:cat)", BooleanQuery.class);
		
    assertQueryEquals(req("qt", "aqp", "q", "edismax(dog cat)", "qf", "title^1 abstract^0.5"), //edismax (thinks it is a phrase?)
        "((abstract:dog^0.5 | title:dog) (abstract:cat^0.5 | title:cat))~2", BooleanQuery.class);
    
    assertQueryEquals(req("qt", "aqp", "q", "dog cat", "qf", "title^1 abstract^0.5"), //aqp
        "+(abstract:dog^0.5 | title:dog) +(abstract:cat^0.5 | title:cat)", BooleanQuery.class);
		
    
    // make sure the *:* query is not parsed by edismax
    assertQueryEquals(req("qt", "aqp", "q", "*", 
        "qf", "author^2.3 title abstract^0.4"), 
        "*:*", MatchAllDocsQuery.class);
    assertQueryEquals(req("qt", "aqp", "q", "*:*", 
        "qf", "author^2.3 title abstract^0.4"), 
        "*:*", MatchAllDocsQuery.class);
		
    /*
     * raw() function operator
     */
		
    //  {!raw f=myfield}Foo Bar creates TermQuery(Term("myfield","Foo Bar"))
    assertQueryEquals(req("qt", "aqp", "f", "myfield", "q", "raw({!f=myfield}Foo Bar)"), "myfield:Foo Bar", TermQuery.class);
    assertQueryEquals(req("qt", "aqp", "f", "myfield", "q", "raw({!f=x}\"Foo Bar\")"), "x:\"Foo Bar\"", TermQuery.class);
    
    assertQueryParseException(req("qt", "aqp", "f", "myfield", "q", "raw(Foo Bar)"));
		
		
		
		// if we use the solr analyzer to parse the query, all is configured to remove stopwords 
		assertQueryEquals(req("qt", "aqp", "q", "edismax(dog OR cat) OR title:bat all:but"), 
				"((all:dog) (all:cat)) title:bat", BooleanQuery.class);
		
		// but pub is normalized_string with a different analyzer and should retain 'but'
		assertQueryEquals(req("qt", "aqp", "q", "edismax(dog OR cat) OR title:bat OR pub:but"), 
				"((all:dog) (all:cat)) title:bat pub:but", BooleanQuery.class);
		
		
		
		/*
		 * translation of the fields (on the fly)
		 */
		
		
		// field map is set to translate arxiv->identifier
		assertQueryEquals(req("qt", "aqp", "q", "arxiv:1002.1524"), 
				"identifier:1002.1524", TermQuery.class);
		assertQueryParseException(req("qt", "aqp", "q", "arxivvvv:1002.1524"));
		
		
		/**
		 *  new function queries, the 2nd order citation operators
		 */
		
		// cites()
		assertQueryEquals(req("qt", "aqp", "q", "cites(author:muller)"), 
        "SecondOrderQuery(author:muller, author:muller,*, filter=null, collector=cites[using_cache:reference])", SecondOrderQuery.class);
		
		assertQueryEquals(req("qt", "aqp", "q", "all:x OR all:z cites(author:muller OR title:body)"), 
        "+(all:x all:z) +SecondOrderQuery((author:muller, author:muller,*) title:body, filter=null, collector=cites[using_cache:reference])", BooleanQuery.class);
		
		
		
		// cites() == refersto()
    assertQueryEquals(req("qt", "aqp", "q", "refersto(author:muller)"), 
        "SecondOrderQuery(author:muller, author:muller,*, filter=null, collector=cites[using_cache:reference])", SecondOrderQuery.class);
    
    assertQueryEquals(req("qt", "aqp", "q", "all:(x OR z) refersto(author:muller OR title:body)"), 
        "+(all:x all:z) +SecondOrderQuery((author:muller, author:muller,*) title:body, filter=null, collector=cites[using_cache:reference])", BooleanQuery.class);
    
    
    
		// citedby()
		assertQueryEquals(req("qt", "aqp", "q", "citedby(author:muller)"), 
				"SecondOrderQuery(author:muller, author:muller,*, filter=null, collector=citedby[using:reference<bibcode>])", SecondOrderQuery.class);
		
		assertQueryEquals(req("qt", "aqp", "q", "all:(x OR z) citedby(author:muller OR title:body)"), 
				"+(all:x all:z) +SecondOrderQuery((author:muller, author:muller,*) title:body, filter=null, collector=citedby[using:reference<bibcode>])", BooleanQuery.class);
		
		
		// useful() 
		assertQueryEquals(req("qt", "aqp", "q", "useful(author:muller)"), 
        "SecondOrderQuery(author:muller, author:muller,*, filter=null, collector=cites[using:reference<weightedBy:cite_read_boost>])", SecondOrderQuery.class);
    
    assertQueryEquals(req("qt", "aqp", "q", "all:(x OR z) useful(author:muller OR title:body)"), 
        "+(all:x all:z) +SecondOrderQuery((author:muller, author:muller,*) title:body, filter=null, collector=cites[using:reference<weightedBy:cite_read_boost>])", BooleanQuery.class);
    
    // reviews() 
    assertQueryEquals(req("qt", "aqp", "q", "reviews(author:muller)"), 
        "SecondOrderQuery(author:muller, author:muller,*, filter=null, collector=citingMostCited[using:cite_read_boost<reference:bibcode>])", SecondOrderQuery.class);
    
    assertQueryEquals(req("qt", "aqp", "q", "all:(x OR z) reviews(author:muller OR title:body)"), 
        "+(all:x all:z) +SecondOrderQuery((author:muller, author:muller,*) title:body, filter=null, collector=citingMostCited[using:cite_read_boost<reference:bibcode>])", BooleanQuery.class);    
	}
	
	public static junit.framework.Test suite() {
        return new junit.framework.JUnit4TestAdapter(TestAqpAdsabsSolrSearch.class);
    }
	
	
}
