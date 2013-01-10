package org.apache.solr.search;


import java.io.File;
import java.io.IOException;

import monty.solr.util.MontySolrQueryTestCase;
import monty.solr.util.MontySolrSetup;

import org.apache.lucene.queryparser.flexible.aqp.TestAqpAdsabs;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.DisjunctionMaxQuery;
import org.apache.lucene.search.MatchAllDocsQuery;
import org.apache.lucene.search.PhraseQuery;
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
					"hubble\0space\0telescope, HST",
					"weak => lightweak",
					"lensing => mikrolinseneffekt",
					"pink => pinkish"
			});
			replaceInFile(newConfig, "synonyms=\"ads_text_query.synonyms\"", "synonyms=\"" + synonymsFile.getAbsolutePath() + "\"");
			
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
	

	public void testOperators() throws Exception {
	  
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
    
    // TODO: #234
    //  {!raw f=myfield}Foo Bar creates TermQuery(Term("myfield","Foo Bar"))
    //assertQueryEquals(req("qt", "aqp", "f", "myfield", "q", "raw({!f=myfield}Foo Bar)"), "myfield:Foo Bar", TermQuery.class);
    //assertQueryEquals(req("qt", "aqp", "f", "myfield", "q", "raw({!f=x}\"Foo Bar\")"), "x:\"Foo Bar\"", TermQuery.class);
    
    assertQueryParseException(req("qt", "aqp", "f", "myfield", "q", "raw(Foo Bar)"));
    
    
    
    // if we use the solr analyzer to parse the query, all is configured to remove stopwords 
    assertQueryEquals(req("qt", "aqp", "q", "edismax(dog OR cat) OR title:bat all:but"), 
        "((all:dog) (all:cat)) title:bat", BooleanQuery.class);
    
    // but pub is normalized_string with a different analyzer and should retain 'but'
    assertQueryEquals(req("qt", "aqp", "q", "edismax(dog OR cat) OR title:bat OR pub:but"), 
        "((all:dog) (all:cat)) title:bat pub:but", BooleanQuery.class);

	  
	  /**
     *  new function queries, the 2nd order citation operators
     */
    
    // references()
    assertQueryEquals(req("qt", "aqp", "q", "references(author:muller)"), 
        "SecondOrderQuery(author:muller, author:muller,*, filter=null, collector=references[cache:reference])", SecondOrderQuery.class);
    assertQueryEquals(req("qt", "aqp", "q", "outgoing_links(author:muller)"), 
        "SecondOrderQuery(author:muller, author:muller,*, filter=null, collector=references[cache:reference])", SecondOrderQuery.class);
    
    //to be removed!
    assertQueryEquals(req("qt", "aqp", "q", "cites(author:muller)"),  
        "SecondOrderQuery(author:muller, author:muller,*, filter=null, collector=references[cache:reference])", SecondOrderQuery.class);
    assertQueryEquals(req("qt", "aqp", "q", "refersto(author:muller)"),
        "SecondOrderQuery(author:muller, author:muller,*, filter=null, collector=references[cache:reference])", SecondOrderQuery.class);
    
    // various searches
    assertQueryEquals(req("qt", "aqp", "q", "all:x OR all:z references(author:muller OR title:body)"), 
        "+(all:x all:z) +SecondOrderQuery((author:muller, author:muller,*) title:body, filter=null, collector=references[cache:reference])", BooleanQuery.class);
    assertQueryEquals(req("qt", "aqp", "q", "citations((title:(lectures physics) and author:Feynman))"),
        "SecondOrderQuery(+(+title:lectures +title:physics) +(author:feynman, author:feynman,*), filter=null, collector=citations[cache:reference<bibcode>])", 
        SecondOrderQuery.class);
    
    
    
    // citations()
    assertQueryEquals(req("qt", "aqp", "q", "citations(author:muller)"), 
        "SecondOrderQuery(author:muller, author:muller,*, filter=null, collector=citations[cache:reference<bibcode>])", SecondOrderQuery.class);
    assertQueryEquals(req("qt", "aqp", "q", "incoming_links(author:muller)"), 
        "SecondOrderQuery(author:muller, author:muller,*, filter=null, collector=citations[cache:reference<bibcode>])", SecondOrderQuery.class);
    
    // to be removed!!!
    assertQueryEquals(req("qt", "aqp", "q", "citedby(author:muller)"), 
        "SecondOrderQuery(author:muller, author:muller,*, filter=null, collector=citations[cache:reference<bibcode>])", SecondOrderQuery.class);
    assertQueryEquals(req("qt", "aqp", "q", "all:(x OR z) citedby(author:muller OR title:body)"), 
        "+(all:x all:z) +SecondOrderQuery((author:muller, author:muller,*) title:body, filter=null, collector=citations[cache:reference<bibcode>])", BooleanQuery.class);    
    
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
	
	public void test() throws Exception {
	  
	  setDebug(true);
	  assertQueryEquals(req("qt", "aqp", "q", "author:(accomazzi NEAR5 kurtz)"), 
        "first_author:kurtz, m first_author:kurtz, m* first_author:kurtz,",
        BooleanQuery.class);
	  
	  
	  // temporary workaround for 'first author' search
	  assertQueryEquals(req("qt", "aqp", "q", "^Kurtz, M."), 
	      "first_author:kurtz, m first_author:kurtz, m* first_author:kurtz,",
        BooleanQuery.class);
	  assertQueryEquals(req("qt", "aqp", "q", "^Kurtz, Michael"), 
        "first_author:kurtz, michael first_author:kurtz, michael * first_author:kurtz, m first_author:kurtz, m * first_author:kurtz,",
        BooleanQuery.class);
	  
	  //pink elephant #238
    assertQueryEquals(req("qt", "aqp", "q", "weak lensing"), 
        "+all:lightweak +all:mikrolinseneffekt", 
        BooleanQuery.class);
    assertQueryEquals(req("qt", "aqp", "q", "weak lensing", 
        "qf", "title full keyword abstract"), 
        "+(abstract:lightweak | title:lightweak | full:lightweak | keyword:weak) " +
        "+(abstract:mikrolinseneffekt | title:mikrolinseneffekt | full:mikrolinseneffekt | keyword:lensing)", 
        BooleanQuery.class);
    
    assertQueryEquals(req("qt", "aqp", "q", "pink elephant"), 
        "+all:pinkish +all:elephant", 
        BooleanQuery.class);
    assertQueryEquals(req("qt", "aqp", "q", "pink elephant",
        "qf", "title full keyword abstract"),
        "+(abstract:pinkish | title:pinkish | full:pinkish | keyword:pink) " +
        "+(abstract:elephant | title:elephant | full:elephant | keyword:elephant)", 
        BooleanQuery.class);

    
    // multi-token combined with single token
    // TODO: I'd like to see the following parse:
    // 
    // +abstract:lightweak^0.6....
    // +((abstract:hubble space telescope | abstract:accr::hst)^0.6 | (title:hubble....))
    // 
    // Yet, for this to work, we must reorder the steps - right now, the multi-token
    // discovery happens before edismax, edismax then receives certain tokens wrapped
    // into nonAnalyzed node ("hubble space telescope" | accr::hst) -- the analysis is
    // done only on the 'weak', the other two tokens are simply spread across the 
    // various fields by edismax
    assertQueryEquals(req("qt", "aqp", "q", "weak hubble space telescope",
        "qf", "title^0.9 full^0.8 keyword^0.7 abstract^0.6"),
        "+(abstract:lightweak^0.6 | title:lightweak^0.9 | full:lightweak^0.8 | keyword:weak^0.7) " +
        "+((abstract:hubble space telescope^0.6 | title:hubble space telescope^0.9 | full:hubble space telescope^0.8 | keyword:hubble space telescope^0.7) " +
          "(abstract:acr::hst^0.6 | title:acr::hst^0.9 | full:acr::hst^0.8 | keyword:acr::hst^0.7))", 
        BooleanQuery.class);
    
    
	  // author search, unfielded
	  assertQueryEquals(req("qt", "aqp", "q", "accomazzi,", 
        "qf", "author^2.3 title abstract^0.4"), 
        "(abstract:accomazzi^0.4 | ((author:accomazzi, author:accomazzi,*)^2.3) | title:accomazzi)", 
        DisjunctionMaxQuery.class);
	  
	  assertQueryEquals(req("qt", "aqp", "q", "accomazzi\\,", 
        "qf", "author^2.3 title abstract^0.4"), 
        "(abstract:accomazzi^0.4 | ((author:accomazzi, author:accomazzi,*)^2.3) | title:accomazzi)", 
        DisjunctionMaxQuery.class);
	  
	  assertQueryEquals(req("qt", "aqp", "q", "\"accomazzi\\,\"", 
        "qf", "author^2.3 title abstract^0.4"), 
        "(abstract:accomazzi^0.4 | ((author:accomazzi, author:accomazzi,*)^2.3) | title:accomazzi)", 
        DisjunctionMaxQuery.class);
	  
	  assertQueryEquals(req("qt", "aqp", "q", "accomazzi,alberto", 
        "qf", "author^2.3 title abstract^0.4"), 
        "(((abstract:accomazzi abstract:alberto abstract:accomazzialberto)^0.4) " +
        "| ((author:accomazzi, alberto author:accomazzi, alberto * author:accomazzi, a author:accomazzi, a * author:accomazzi,)^2.3) " +
        "| (title:accomazzi title:alberto title:accomazzialberto))", 
        DisjunctionMaxQuery.class);
	  
	  assertQueryEquals(req("qt", "aqp", "q", "accomazzi, alberto", 
        "qf", "author^2.3 title abstract^0.4"), 
        "(((abstract:accomazzi abstract:alberto abstract:accomazzialberto)^0.4) " +
        "| ((author:accomazzi, alberto author:accomazzi, alberto * author:accomazzi, a author:accomazzi, a * author:accomazzi,)^2.3) " +
        "| (title:accomazzi title:alberto title:accomazzialberto))", 
        DisjunctionMaxQuery.class);
	  
	  assertQueryEquals(req("qt", "aqp", "q", "\"accomazzi, alberto\"", 
        "qf", "author^2.3 title abstract^0.4"), 
        "(abstract:\"accomazzi alberto\"^0.4 " +
        "| ((author:accomazzi, alberto author:accomazzi, alberto * author:accomazzi, a author:accomazzi, a * author:accomazzi,)^2.3) " +
        "| title:\"accomazzi alberto\")", 
        DisjunctionMaxQuery.class);
	  
	  assertQueryEquals(req("qt", "aqp", "q", "accomazzi, alberto, xxx", 
        "qf", "author^2.3 title abstract^0.4"), 
        "(((abstract:accomazzi abstract:alberto abstract:xxx abstract:accomazzialbertoxxx)^0.4) " +
        "| ((author:accomazzi, alberto, xxx author:accomazzi, alberto, xxx * author:accomazzi, a xxx author:accomazzi, a xxx * author:accomazzi, alberto, x author:accomazzi, alberto, x * author:accomazzi, a x author:accomazzi, a x * author:accomazzi, alberto, author:accomazzi, a author:accomazzi,)^2.3) " +
        "| (title:accomazzi title:alberto title:xxx title:accomazzialbertoxxx))", 
        DisjunctionMaxQuery.class);
    
    assertQueryEquals(req("qt", "aqp", "q", "\"accomazzi, alberto, xxx.\"", 
        "qf", "author^2.3 title abstract^0.4"), 
        "(abstract:\"accomazzi alberto xxx\"^0.4 " +
        "| ((author:accomazzi, alberto, xxx author:accomazzi, alberto, xxx * author:accomazzi, a xxx author:accomazzi, a xxx * author:accomazzi, alberto, x author:accomazzi, alberto, x * author:accomazzi, a x author:accomazzi, a x * author:accomazzi, alberto, author:accomazzi, a author:accomazzi,)^2.3) " +
        "| title:\"accomazzi alberto xxx\")", 
        DisjunctionMaxQuery.class);
    
    // now some esoteric cases of the comma parsing
    assertQueryEquals(req("qt", "aqp", "q", "abstract:\"accomazzi, alberto\"", 
        "qf", "author^2.3 title abstract^0.4"), 
        "abstract:\"accomazzi alberto\"", 
        PhraseQuery.class);
    
    assertQueryEquals(req("qt", "aqp", "q", "abstract:accomazzi, alberto", 
        "qf", "author^2.3 title abstract^0.4"), 
        "abstract:accomazzi abstract:alberto abstract:accomazzialberto", 
        BooleanQuery.class);
    
    assertQueryEquals(req("qt", "aqp", "q", "author:accomazzi, alberto", 
        "qf", "author^2.3 title abstract^0.4"), 
        "author:accomazzi, alberto author:accomazzi, alberto * author:accomazzi, a author:accomazzi, a * author:accomazzi,", 
        BooleanQuery.class);
		
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
    
		
		assertQueryEquals(req("qt", "aqp", "q", "identifier:2011A&A...536A..89G"), 
        "identifier:2011a&a...536a..89g", TermQuery.class);
		assertQueryEquals(req("qt", "aqp", "q", "identifier:2011A&A" + "\u2026" + "536A..89G"), 
        "identifier:2011a&a...536a..89g", TermQuery.class);
		
		
		
		/*
		 * translation of the fields (on the fly)
		 */
		
		
		// field map is set to translate arxiv->identifier
		assertQueryEquals(req("qt", "aqp", "q", "arxiv:1002.1524"), 
				"identifier:1002.1524", TermQuery.class);
		assertQueryParseException(req("qt", "aqp", "q", "arxivvvv:1002.1524"));
		
		
		    
	}
	
	public static junit.framework.Test suite() {
        return new junit.framework.JUnit4TestAdapter(TestAqpAdsabsSolrSearch.class);
    }
	
	
}
