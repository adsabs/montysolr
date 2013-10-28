package org.apache.solr.search;


import java.io.File;
import java.io.IOException;

import monty.solr.util.MontySolrQueryTestCase;
import monty.solr.util.MontySolrSetup;

import org.apache.lucene.search.MoreLikeThisQueryFixed;
import org.apache.lucene.queryparser.flexible.aqp.TestAqpAdsabs;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.DisjunctionMaxQuery;
import org.apache.lucene.search.MatchAllDocsQuery;
import org.apache.lucene.search.PhraseQuery;
import org.apache.lucene.search.PrefixQuery;
import org.apache.lucene.search.RegexpQuery;
import org.apache.lucene.search.SecondOrderQuery;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.WildcardQuery;
import org.apache.lucene.search.spans.SpanNearQuery;
import org.apache.lucene.search.spans.SpanPositionRangeQuery;
import org.junit.BeforeClass;


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

	@BeforeClass
	public static void beforeTestAqpAdsabsSolrSearch() throws Exception {
		MontySolrSetup.addToSysPath(MontySolrSetup.getMontySolrHome()
				+ "/contrib/adsabs/src/python");
		MontySolrSetup.addTargetsToHandler("adsabs.targets");
	}
	
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
			
			File multiSynonymsFile = createTempFile(new String[]{
					"hubble\0space\0telescope, HST",
					"r\0s\0t, RST",
			});
			replaceInFile(newConfig, "synonyms=\"ads_text_multi.synonyms\"", "synonyms=\"" + multiSynonymsFile.getAbsolutePath() + "\"");
			
			File synonymsFile = createTempFile(new String[]{
					"weak => lightweak",
					"lensing => mikrolinseneffekt",
					"pink => pinkish"
			});
			replaceInFile(newConfig, "synonyms=\"ads_text_simple.synonyms\"", "synonyms=\"" + synonymsFile.getAbsolutePath() + "\"");
			
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
	

	public void testSpecialCases() throws Exception {
		
		
		
		// unbalanced brackets for functions
		assertQueryEquals(req("defType", "aqp", "q", "topn(201, ((\"foo bar\") AND database:astronomy), date asc)"), 
        "SecondOrderQuery(+all:\"foo bar\" +database:astronomy, filter=null, collector=topn[201, outOfOrder=false, info=date asc])", 
        SecondOrderQuery.class);
		assertQueryEquals(req("defType", "aqp", "q", "topn(201, ((\"foo bar\") AND database:astronomy),   date asc   )"), 
        "SecondOrderQuery(+all:\"foo bar\" +database:astronomy, filter=null, collector=topn[201, outOfOrder=false, info=date asc])", 
        SecondOrderQuery.class);
		assertQueryEquals(req("defType", "aqp", "q", "topn(201,(  ((\"foo bar\") AND database:astronomy)),date asc)"), 
        "SecondOrderQuery(+all:\"foo bar\" +database:astronomy, filter=null, collector=topn[201, outOfOrder=false, info=date asc])", 
        SecondOrderQuery.class);
		
		
		
		// added ability to interactively tweak queries
		assertQueryEquals(req("defType", "aqp", "q", "tweak(collector_final_value=ARITHM_MEAN, citations(author:muller))"), 
        "SecondOrderQuery(author:muller, author:muller,*, filter=null, collector=citations[cache:reference<bibcode,alternate_bibcode>])", 
        SecondOrderQuery.class);
    
		
		// # 389		
		// make sure the functional parsing is handling things well
		assertQueryEquals(req("defType", "aqp", "q", "topn(200, ((title:foo OR topn(10, title:bar OR title:baz))))"), 
        "SecondOrderQuery(title:foo SecondOrderQuery(title:bar title:baz, filter=null, collector=topn[10, outOfOrder=false]), filter=null, collector=topn[200, outOfOrder=false])", 
        SecondOrderQuery.class);
		assertQueryEquals(req("defType", "aqp", "q", "topn(200, ((title:foo AND topn(10, title:bar OR title:baz))))"), 
        "SecondOrderQuery(+title:foo +SecondOrderQuery(title:bar title:baz, filter=null, collector=topn[10, outOfOrder=false]), filter=null, collector=topn[200, outOfOrder=false])", 
        SecondOrderQuery.class);
		assertQueryEquals(req("defType", "aqp", "q", "topn(200, title:foo, date desc)"), 
        "SecondOrderQuery(title:foo, filter=null, collector=topn[200, outOfOrder=false, info=date desc])", 
        SecondOrderQuery.class);
		assertQueryEquals(req("defType", "aqp", "q", "topn(200, (title:foo), date desc)"), 
        "SecondOrderQuery(title:foo, filter=null, collector=topn[200, outOfOrder=false, info=date desc])", 
        SecondOrderQuery.class);
		assertQueryEquals(req("defType", "aqp", "q", "topn(200, \"foo bar\", \"date desc\")"), 
        "SecondOrderQuery(all:\"foo bar\", filter=null, collector=topn[200, outOfOrder=false, info=date desc])", 
        SecondOrderQuery.class);
		
		
    // trendy() - what people read, it reads data from index
		assertU(addDocs("author", "muller", "reader", "bibcode1", "reader", "bibcode2"));
    assertU(addDocs("author", "muller", "reader", "bibcode2", "reader", "bibcode4"));
    assertU(addDocs("author", "muller", "reader", "bibcode5", "reader", "bibcode2"));
    assertU(commit());
    assertQueryEquals(req("defType", "aqp", "q", "trending(author:muller)"), 
        "like:bibcode1 bibcode2 bibcode2 bibcode4 bibcode5 bibcode2", 
        MoreLikeThisQueryFixed.class);
    
		
		// pos() operator
		assertQueryEquals(req("defType", "aqp", "q", "pos(author:\"Accomazzi, A\", 1, 100)"), 
				"spanPosRange(spanOr([author:accomazzi, a, SpanMultiTermQueryWrapper(author:accomazzi, a*), author:accomazzi,]), 0, 100)", 
				SpanPositionRangeQuery.class);
		
		// notice the use of modifier '='
		assertQueryEquals(req("defType", "aqp", "q", "pos(=author:\"Accomazzi, A\", 1)"), 
				"spanPosRange(author:Accomazzi, A, 0, 1)", 
				SpanPositionRangeQuery.class);
		assertQueryEquals(req("defType", "aqp", "q", "pos(+author:\"Accomazzi, A\", 1, 1)"), 
				"spanPosRange(spanOr([author:accomazzi, a, SpanMultiTermQueryWrapper(author:accomazzi, a*), author:accomazzi,]), 0, 1)", 
				SpanPositionRangeQuery.class);
		
		
		assertQueryParseException(req("defType", "aqp", "q", "pos(author:\"Accomazzi, A\", 1, -1)"));
		assertQueryParseException(req("defType", "aqp", "q", "pos(author:\"Accomazzi, A\", 1, 1, 1)"));
		assertQueryParseException(req("defType", "aqp", "q", "pos(author:\"Accomazzi, A\")"));
		assertQueryParseException(req("defType", "aqp", "q", "^two$"));
		assertQueryParseException(req("defType", "aqp", "q", "two$"));
		assertQueryParseException(req("defType", "aqp", "q", "\"two phrase$\""));

		
		
		// old positional search
	  // TODO: check for the generated warnings
		
		assertQueryEquals(req("defType", "aqp", "q","^two"),
				"spanPosRange(spanOr([author:two,, SpanMultiTermQueryWrapper(author:two,*)]), 0, 1)", 
				SpanPositionRangeQuery.class);
		assertQueryEquals(req("defType", "aqp", "q","one ^two, j k"), 
				"+all:one +spanPosRange(spanOr([author:two, j k, SpanMultiTermQueryWrapper(author:two, j k*), EmptySpanQuery(author:/two, j[^\\s]+ k/), EmptySpanQuery(author:/two, j[^\\s]+ k .*/), author:two, j, author:two,]), 0, 1)",
				BooleanQuery.class);
		assertQueryEquals(req("defType", "aqp", "q","one \"^author phrase\"", "qf", "title author"),
				"+((author:one, author:one,*) | title:one) +spanPosRange(spanOr([author:phrase, author, SpanMultiTermQueryWrapper(author:phrase, author *), author:phrase, a, SpanMultiTermQueryWrapper(author:phrase, a *), author:phrase,]), 0, 1)",
				BooleanQuery.class);
		
		
		// author expansion can generate regexes, so we should deal with them (actually we ignore them)
		assertQueryEquals(req("defType", "aqp", "q", "pos(author:\"Accomazzi, A. K. B.\", 1)"), 
				"spanPosRange(spanOr([author:accomazzi, a k b, SpanMultiTermQueryWrapper(author:accomazzi, a k b*), EmptySpanQuery(author:/accomazzi, a[^\\s]+ k[^\\s]+ b/), EmptySpanQuery(author:/accomazzi, a[^\\s]+ k[^\\s]+ b .*/), author:accomazzi, a, author:accomazzi,]), 0, 1)", 
				SpanPositionRangeQuery.class);
		
		
		//#322 - trailing comma
		assertQueryEquals(req("defType", "aqp", "q", "author:\"^roberts\", author:\"ables\""),
				"+spanPosRange(spanOr([author:roberts,, SpanMultiTermQueryWrapper(author:roberts,*)]), 0, 1) +(author:ables, author:ables,*)",
				BooleanQuery.class);
		
		
				
		/*
		TODO: i don't yet have the implementations for these
		assertQueryEquals("funcA(funcB(funcC(value, \"phrase value\", nestedFunc(0, 2))))", null, "");
		
		assertQueryEquals("simbad(20 54 05.689 +37 01 17.38)", null, "");
		assertQueryEquals("simbad(10:12:45.3-45:17:50)", null, "");
		assertQueryEquals("simbad(15h17m-11d10m)", null, "");
		assertQueryEquals("simbad(15h17+89d15)", null, "");
		assertQueryEquals("simbad(275d11m15.6954s+17d59m59.876s)", null, "");
		assertQueryEquals("simbad(12.34567h-17.87654d)", null, "");
		assertQueryEquals("simbad(350.123456d-17.33333d <=> 350.123456-17.33333)", null, "");
		*/
		
		
		
		//topn sorted - added 15Aug2013
		assertQueryEquals(req("defType", "aqp", "q", "topn(5, *:*, date desc)"), 
        "SecondOrderQuery(*:*, filter=null, collector=topn[5, outOfOrder=false, info=date desc])", 
        SecondOrderQuery.class);
		assertQueryEquals(req("defType", "aqp", "q", "topn(5, author:civano, \"date desc\")"), 
        "SecondOrderQuery(author:civano, author:civano,*, filter=null, collector=topn[5, outOfOrder=false, info=date desc])", 
        SecondOrderQuery.class);
		
		// topN - added Aug2013
		assertQueryEquals(req("defType", "aqp", "q", "topn(5, *:*)"), 
        "SecondOrderQuery(*:*, filter=null, collector=topn[5, outOfOrder=false])", 
        SecondOrderQuery.class);
		assertQueryEquals(req("defType", "aqp", "q", "topn(5, (foo bar))"), 
        "SecondOrderQuery(+all:foo +all:bar, filter=null, collector=topn[5, outOfOrder=false])", 
        SecondOrderQuery.class);
		
		assertQueryEquals(req("defType", "aqp", "q", "topn(5, edismax(dog OR cat))", "qf", "title^1 abstract^0.5"), 
        "SecondOrderQuery((abstract:dog^0.5 | title:dog) (abstract:cat^0.5 | title:cat), filter=null, collector=topn[5, outOfOrder=false])", 
        SecondOrderQuery.class);
		assertQueryEquals(req("defType", "aqp", "q", "topn(5, author:accomazzi)"), 
        "SecondOrderQuery(author:accomazzi, author:accomazzi,*, filter=null, collector=topn[5, outOfOrder=false])", 
        SecondOrderQuery.class);
		
    /*
     * It is different if Aqp handles the boolean operations or if 
     * edismax() does it. 
     * 
     * Aqp has more control, see: https://issues.apache.org/jira/browse/SOLR-4141
     */
    
    assertQueryEquals(req("defType", "aqp", "q", "edismax(dog OR cat)", "qf", "title^1 abstract^0.5"), //edismax 
        "(abstract:dog^0.5 | title:dog) (abstract:cat^0.5 | title:cat)", BooleanQuery.class);
    //setDebug(true);
    assertQueryEquals(req("defType", "aqp", "q", "dog OR cat", "qf", "title^1 abstract^0.5"),          //aqp
        "(abstract:dog^0.5 | title:dog) (abstract:cat^0.5 | title:cat)", BooleanQuery.class);
    
    assertQueryEquals(req("defType", "aqp", "q", "edismax(dog AND cat)", "qf", "title^1 abstract^0.5"), //edismax
        "+(abstract:dog^0.5 | title:dog) +(abstract:cat^0.5 | title:cat)", BooleanQuery.class);
    
    assertQueryEquals(req("defType", "aqp", "q", "dog AND cat", "qf", "title^1 abstract^0.5"), //aqp
        "+(abstract:dog^0.5 | title:dog) +(abstract:cat^0.5 | title:cat)", BooleanQuery.class);
    
    assertQueryEquals(req("defType", "aqp", "q", "edismax(dog OR cat)", "qf", "title^1 abstract^0.5"), //edismax
        "(abstract:dog^0.5 | title:dog) (abstract:cat^0.5 | title:cat)", BooleanQuery.class);
    
    assertQueryEquals(req("defType", "aqp", "q", "dog OR cat", "qf", "title^1 abstract^0.5"), //aqp
        "(abstract:dog^0.5 | title:dog) (abstract:cat^0.5 | title:cat)", BooleanQuery.class);
    
    assertQueryEquals(req("defType", "aqp", "q", "edismax(dog cat)", "qf", "title^1 abstract^0.5"), //edismax (thinks it is a phrase?)
        "((abstract:dog^0.5 | title:dog) (abstract:cat^0.5 | title:cat))~2", BooleanQuery.class);
    
    assertQueryEquals(req("defType", "aqp", "q", "dog cat", "qf", "title^1 abstract^0.5"), //aqp
        "(((abstract:dog abstract:cat)^0.5) | (title:dog title:cat))", DisjunctionMaxQuery.class);
    
    
    
    // make sure the *:* query is not parsed by edismax
    assertQueryEquals(req("defType", "aqp", "q", "*", 
        "qf", "author^2.3 title abstract^0.4"), 
        "*:*", MatchAllDocsQuery.class);
    assertQueryEquals(req("defType", "aqp", "q", "*:*", 
        "qf", "author^2.3 title abstract^0.4"), 
        "*:*", MatchAllDocsQuery.class);
    
    /*
     * raw() function operator
     */
    
    // TODO: #234
    // need to add a processor which puts these local values into a request object
    //  {!raw f=myfield}Foo Bar creates TermQuery(Term("myfield","Foo Bar"))
    // <astLOCAL_PARAMS value="{!f=myfield}" start="4" end="15" name="LOCAL_PARAMS" type="27" />
    // assertQueryEquals(req("defType", "aqp", "f", "myfield", "q", "raw({!f=myfield}Foo Bar)"), "myfield:Foo Bar", TermQuery.class);
    // assertQueryEquals(req("defType", "aqp", "f", "myfield", "q", "raw({!f=x}\"Foo Bar\")"), "x:\"Foo Bar\"", TermQuery.class);
    
    assertQueryParseException(req("defType", "aqp", "f", "myfield", "q", "raw(Foo Bar)"));
    
    
    
    // if we use the solr analyzer to parse the query, all is configured to remove stopwords 
    assertQueryEquals(req("defType", "aqp", "q", "edismax(dog OR cat) OR title:bat all:but"), 
        "((all:dog) (all:cat)) title:bat", BooleanQuery.class);
    
    // but pub is normalized_string with a different analyzer and should retain 'but'
    assertQueryEquals(req("defType", "aqp", "q", "edismax(dog OR cat) OR title:bat OR pub:but"), 
        "((all:dog) (all:cat)) title:bat pub:but", BooleanQuery.class);

	  
	  /**
     *  new function queries, the 2nd order citation operators
     */
    
    // references()
    assertQueryEquals(req("defType", "aqp", "q", "references(author:muller)"), 
        "SecondOrderQuery(author:muller, author:muller,*, filter=null, collector=references[cache:reference])", SecondOrderQuery.class);
    
    
    // various searches
    assertQueryEquals(req("defType", "aqp", "q", "all:x OR all:z references(author:muller OR title:body)"), 
        "+(all:x all:z) +SecondOrderQuery((author:muller, author:muller,*) title:body, filter=null, collector=references[cache:reference])", BooleanQuery.class);
    assertQueryEquals(req("defType", "aqp", "q", "citations((title:(lectures physics) and author:Feynman))"),
        "SecondOrderQuery(+(+title:lectures +title:physics) +(author:feynman, author:feynman,*), filter=null, collector=citations[cache:reference<bibcode,alternate_bibcode>])", 
        SecondOrderQuery.class);
    
    
    
    // citations()
    assertQueryEquals(req("defType", "aqp", "q", "citations(author:muller)"), 
        "SecondOrderQuery(author:muller, author:muller,*, filter=null, collector=citations[cache:reference<bibcode,alternate_bibcode>])", SecondOrderQuery.class);
    
    
    // useful() - ads classic implementation 
    assertQueryEquals(req("defType", "aqp", "q", "useful(author:muller)"), 
        "SecondOrderQuery(SecondOrderQuery(SecondOrderQuery(author:muller, author:muller,*, filter=null, collector=adsrel[cite_read_boost, outOfOrder=false]), filter=null, collector=topn[200, outOfOrder=false]), filter=null, collector=references[cache:reference])", 
        SecondOrderQuery.class);
    
    assertQueryEquals(req("defType", "aqp", "q", "all:(x OR z) useful(author:muller OR title:body)"), 
        "+(all:x all:z) +SecondOrderQuery(SecondOrderQuery(SecondOrderQuery((author:muller, author:muller,*) title:body, filter=null, collector=adsrel[cite_read_boost, outOfOrder=false]), filter=null, collector=topn[200, outOfOrder=false]), filter=null, collector=references[cache:reference])", 
        BooleanQuery.class);
    
    
    // useful2() - original implementation 
    assertQueryEquals(req("defType", "aqp", "q", "useful2(author:muller)"), 
        "SecondOrderQuery(author:muller, author:muller,*, filter=null, collector=cites[using:reference<weightedBy:cite_read_boost>])", SecondOrderQuery.class);
    
    assertQueryEquals(req("defType", "aqp", "q", "all:(x OR z) useful2(author:muller OR title:body)"), 
        "+(all:x all:z) +SecondOrderQuery((author:muller, author:muller,*) title:body, filter=null, collector=cites[using:reference<weightedBy:cite_read_boost>])", BooleanQuery.class);
    
    
    // reviews() - ADS classic impl
    assertQueryEquals(req("defType", "aqp", "q", "reviews(author:muller)"), 
        "SecondOrderQuery(SecondOrderQuery(SecondOrderQuery(author:muller, author:muller,*, filter=null, collector=adsrel[cite_read_boost, outOfOrder=false]), filter=null, collector=topn[200, outOfOrder=false]), filter=null, collector=citations[cache:reference<bibcode,alternate_bibcode>])", 
        SecondOrderQuery.class);
    
    assertQueryEquals(req("defType", "aqp", "q", "all:(x OR z) reviews(author:muller OR title:body)"), 
        "+(all:x all:z) +SecondOrderQuery(SecondOrderQuery(SecondOrderQuery((author:muller, author:muller,*) title:body, filter=null, collector=adsrel[cite_read_boost, outOfOrder=false]), filter=null, collector=topn[200, outOfOrder=false]), filter=null, collector=citations[cache:reference<bibcode,alternate_bibcode>])", 
        BooleanQuery.class);
    
    // reviews2() - original impl 
    assertQueryEquals(req("defType", "aqp", "q", "reviews2(author:muller)"), 
        "SecondOrderQuery(author:muller, author:muller,*, filter=null, collector=citingMostCited[using:cite_read_boost<reference:bibcode,alternate_bibcode>])", SecondOrderQuery.class);
    
    assertQueryEquals(req("defType", "aqp", "q", "all:(x OR z) reviews2(author:muller OR title:body)"), 
        "+(all:x all:z) +SecondOrderQuery((author:muller, author:muller,*) title:body, filter=null, collector=citingMostCited[using:cite_read_boost<reference:bibcode,alternate_bibcode>])", BooleanQuery.class);
    
	}
	
	
	
	public void test() throws Exception {
	  
		// this should not call edismax
		assertQueryEquals(req("defType", "aqp", "q", "accomazzi", "df", "author"),
				"author:accomazzi, author:accomazzi,*",
				BooleanQuery.class);
	
	  assertQueryEquals(req("defType", "aqp", "q", "accomazzi,alberto", 
        "qf", "author^2.3 title",
        "aqp.unfielded.tokens.strategy", "multiply",
				"aqp.unfielded.tokens.new.type", "simple",
				"aqp.unfielded.phrase.edismax.synonym.workaround", "true"),
				// the first branch - author:accomazzi,alberto
        "((((author:accomazzi, alberto author:accomazzi, a author:accomazzi,))^2.3) | ((title:accomazzi title:alberto title:accomazzialberto))) " +
        // the second branch - the phrase
        "((((author:accomazzi, alberto author:accomazzi, alberto * author:accomazzi, a author:accomazzi, a * author:accomazzi,))^2.3) | ((title:\"accomazzi alberto\" title:accomazzialberto)))", 
        BooleanQuery.class);
	  

	  
		// this is default behaviour, 
		// if you see 'all:' it means edismax didn't parse it
		assertQueryEquals(req("defType", "aqp", "q", "author:accomazzi, alberto property:refereed apj"), 
        "+(author:accomazzi, author:accomazzi,*) +all:alberto +property:refereed +all:apj",
        BooleanQuery.class);
		assertQueryEquals(req("defType", "aqp", "q", "author:huchra supernova"), 
        "+(author:huchra, author:huchra,*) +all:supernova", 
        BooleanQuery.class);

  	// smarter handling of missing parentheses/brackets		
		// with the special strategy
		// i expect following: 
		// edismax receives: 'author:accomazzi, alberto' and also 'author:"accomazzi, alberto"
		//      ""         : 'property:refereed r s t'  and 'property:"refereed r s t"'
		// author search does: "accomazzi, alberto"
		assertQueryEquals(req("defType", "aqp",
				"aqp.unfielded.tokens.strategy", "multiply",
				"aqp.unfielded.tokens.new.type", "simple",
				"qf", "title keyword",
				"q", "author:accomazzi, alberto property:refereed r s t"),
				"+(((author:accomazzi, (title:alberto | keyword:alberto))~2) (((author:accomazzi, alberto author:accomazzi, alberto * author:accomazzi, a author:accomazzi, a * author:accomazzi,))~1)) " +
				"+(((property:refereed (title:r | keyword:r) (title:s | keyword:s) (title:t | keyword:t))~4) property:refereedrst)",
        BooleanQuery.class);
		// the same as above + enhanced by multisynonym
		// i expect to see syn::r s t, syn::acr::rst
		assertQueryEquals(req("defType", "aqp",
				"aqp.unfielded.tokens.strategy", "multiply",
				"aqp.unfielded.tokens.new.type", "simple",
				"aqp.unfielded.phrase.edismax.synonym.workaround", "true",
				"q", "author:accomazzi, alberto property:refereed r s t",
				"qf", "title keyword^0.5"), 
        "+(((author:accomazzi, (title:alberto | keyword:alberto^0.5))~2) (((author:accomazzi, alberto author:accomazzi, alberto * author:accomazzi, a author:accomazzi, a * author:accomazzi,))~1)) " +
        "+(((property:refereed (title:r | keyword:r^0.5) (title:s | keyword:s^0.5) (title:t | keyword:t^0.5))~4) property:refereedrst)", 
        BooleanQuery.class);
		
		
		
		// search for all docs with a field
		assertQueryEquals(req("defType", "aqp", "q", "title:*"), 
        "title:*",
        PrefixQuery.class);
		assertQueryEquals(req("defType", "aqp", "q", "title:?"), 
        "title:?",
        WildcardQuery.class);
		
		// fun test of a crazy span query
		assertQueryEquals(req("defType", "aqp", "q", "(consult* or advis*) NEAR4 (fee or retainer or salary or bonus)"), 
        "spanNear([spanOr([SpanMultiTermQueryWrapper(all:consult*), SpanMultiTermQueryWrapper(all:advis*)]), spanOr([all:fee, all:retainer, all:salary, all:bonus])], 4, true)",
        SpanNearQuery.class);
		assertQueryEquals(req("defType", "aqp", "q", "(consult* and advis*) NEAR4 (fee or retainer or salary or bonus)"), 
        "spanNear([spanNear([SpanMultiTermQueryWrapper(all:consult*), SpanMultiTermQueryWrapper(all:advis*)], 4, true), spanOr([all:fee, all:retainer, all:salary, all:bonus])], 4, true)",
        SpanNearQuery.class);
		
		// #375
		assertQueryEquals(req("defType", "aqp", "q", "author:\"Civano, F\" -author_facet_hier:(\"Civano, Fa\" OR \"Civano, Da\")"), 
        "+(author:civano, f author:civano, f* author:civano,) -(author_facet_hier:Civano, Fa author_facet_hier:Civano, Da)",
        BooleanQuery.class);
		assertQueryEquals(req("defType", "aqp", "q", "author:\"Civano, F\" +author_facet_hier:(\"Civano, Fa\" OR \"Civano, Da\")"), 
        "+(author:civano, f author:civano, f* author:civano,) +(author_facet_hier:Civano, Fa author_facet_hier:Civano, Da)",
        BooleanQuery.class);
		assertQueryEquals(req("defType", "aqp", "q", "title:xxx -title:(foo OR bar)"), 
        "+title:xxx -(title:foo title:bar)",
        BooleanQuery.class);
		assertQueryEquals(req("defType", "aqp", "q", "title:xxx +title:(foo OR bar)"), 
        "+title:xxx +(title:foo title:bar)",
        BooleanQuery.class);
		assertQueryEquals(req("defType", "aqp", "q", "title:xxx +title:(-foo OR bar)"), 
        "+title:xxx +(-title:foo title:bar)",
        BooleanQuery.class);
		
		// TO FINISH, it will cause build failure
//		assertQueryEquals(req("defType", "aqp", "q", "title:xxx -title:(foo bar)"), 
//        "+title:xxx -title:foo -title:bar",
//        BooleanQuery.class);
//		assertQueryEquals(req("defType", "aqp", "q", "title:xxx +title:(foo bar)"), 
//        "+title:xxx +title:foo +title:bar",
//        BooleanQuery.class);
//		assertQueryEquals(req("defType", "aqp", "q", "title:xxx +title:(-foo bar)"), 
//        "+title:xxx -title:foo +title:bar",
//        BooleanQuery.class);
		
		
		
	  // regex
	  assertQueryEquals(req("defType", "aqp", "q", "author:/^Kurtz,\\WM./"), 
        "author:/^Kurtz,\\WM./",
        RegexpQuery.class);
	  assertQueryEquals(req("defType", "aqp", "q", "author:/^kurtz,\\Wm./"), 
        "author:/^kurtz,\\Wm./",
        RegexpQuery.class);
	  
	  // this is treated as regex, but because it is unfielded search
	  // it ends up in the all field. Feature or a bug?
	  assertQueryEquals(req("defType", "aqp", "q", "/^Kurtz, M./"), 
        "unfielded_search:/^Kurtz, M./",
        RegexpQuery.class);
	  // regex ignores unfielded queries
	  assertQueryEquals(req("defType", "aqp", "q", "/^Kurtz, M./", "qf", "title^0.5 author^0.8"), 
        "unfielded_search:/^Kurtz, M./",
        RegexpQuery.class);
	  
	  assertQueryEquals(req("defType", "aqp", "q", "author:/kurtz, m.*/"), 
        "author:/kurtz, m.*/",
        RegexpQuery.class);
	  assertQueryEquals(req("defType", "aqp", "q", "author:(/kurtz, m.*/)"), 
        "author:/kurtz, m.*/",
        RegexpQuery.class);
	  assertQueryEquals(req("defType", "aqp", "q", "abstract:/nas\\S+/"), 
        "abstract:/nas\\S+/",
        RegexpQuery.class);
	  
	  
	  // NEAR queries are little bit too crazy and will need taming
	  // and *more* unittest examples
	  assertQueryEquals(req("defType", "aqp", "q", "author:(accomazzi NEAR5 kurtz)"), 
        "spanNear([spanOr([author:accomazzi,, SpanMultiTermQueryWrapper(author:accomazzi,*)]), " +
        "spanOr([author:kurtz,, SpanMultiTermQueryWrapper(author:kurtz,*)])], 5, true)",
        SpanNearQuery.class);
	  
	  
	  assertQueryEquals(req("defType", "aqp", "q", "\"NASA grant\"~3 NEAR N*"), 
        "spanNear([spanNear([all:acr::nasa, all:grant], 3, true), SpanMultiTermQueryWrapper(all:n*)], 5, true)",
        SpanNearQuery.class);
	  assertQueryEquals(req("defType", "aqp", "q", "\"NASA grant\"^0.9 NEAR N*"), 
        "spanNear([spanNear([all:acr::nasa, all:grant], 1, true)^0.9, SpanMultiTermQueryWrapper(all:n*)], 5, true)",
        SpanNearQuery.class);
	  assertQueryEquals(req("defType", "aqp", "q", "\"NASA grant\"~3^0.9 NEAR N*"), 
        "spanNear([spanNear([all:acr::nasa, all:grant], 3, true)^0.9, SpanMultiTermQueryWrapper(all:n*)], 5, true)",
        SpanNearQuery.class);
	  
	  
	  
	  //#238 - single synonyms were caught by the multi-synonym component
	  // also note:
	  // the 'qf' is not set, but still edismax is responsible for parsing this query
	  // and since edismax is using default OR (that is hardcoded!), we cannot change
	  // that, we would have to parse the query ourselves; but after a long discussion
	  // it was decided that we'll use OR for the unfielded searches, so it should be 
	  // OK - if not, I have to rewrite edismax parsing logic myself
	  // 22/10/13 - I've introduced a new strategy that emits both the
	  // original query string and the phrase query, this is a workaround
	  // for edismax
    assertQueryEquals(req("defType", "aqp",
    		"aqp.unfielded.tokens.strategy", "multiply",
				"aqp.unfielded.tokens.new.type", "simple",
    		"q", "pink elephant"), 
        "(((((all:pink all:syn::pinkish))) (all:elephant))~2) all:\"(pink syn::pinkish) elephant\"",
        BooleanQuery.class);
    
    assertQueryEquals(req("defType", "aqp", "q", "pink elephant",
    		"aqp.unfielded.tokens.strategy", "multiply",
				"aqp.unfielded.tokens.new.type", "simple",
        "qf", "title keyword"),
        "(((((title:pink title:syn::pinkish)) | keyword:pink) (title:elephant | keyword:elephant))~2) (title:\"(pink syn::pinkish) elephant\" | keyword:\"pink elephant\")",
        BooleanQuery.class);
    
    // when combined, the ADS's default AND operator should be visible +foo
    assertQueryEquals(req("defType", "aqp", "q", "pink elephant title:foo",
    		"aqp.unfielded.tokens.strategy", "multiply",
				"aqp.unfielded.tokens.new.type", "simple",
        "qf", "title keyword"),
        "+((((((title:pink title:syn::pinkish)) | keyword:pink) (title:elephant | keyword:elephant))~2) (title:\"(pink syn::pinkish) elephant\" | keyword:\"pink elephant\")) +title:foo",
        BooleanQuery.class);
    
    
    // multi-token combined with single token
    // the unfielded search should be exapnded with the phrase "x r s t"
    // and "r s t" should be properly analyzed into: "x rst" OR "x r s t"
    assertQueryEquals(req("defType", "aqp", 
    		"q", "r s t",
    		"aqp.unfielded.tokens.strategy", "multiply",
				"aqp.unfielded.tokens.new.type", "simple",
				"aqp.unfielded.phrase.edismax.synonym.workaround", "true",
        "qf", "title^0.9 keyword^0.7"),
        "(((title:r^0.9 | keyword:r^0.7) (title:s^0.9 | keyword:s^0.7) (title:t^0.9 | keyword:t^0.7))~3) ((((title:\"r s t\" (title:syn::r s t title:syn::acr::rst)))^0.9) | keyword:\"r s t\"^0.7)",
        BooleanQuery.class);
    
    assertQueryEquals(req("defType", "aqp", 
    		"q", "x r s t y",
    		"aqp.unfielded.tokens.strategy", "multiply",
				"aqp.unfielded.tokens.new.type", "simple",
				"aqp.unfielded.phrase.edismax.synonym.workaround", "true",
        "qf", "title^0.9 keyword^0.7"),
        "(((title:x^0.9 | keyword:x^0.7) (title:r^0.9 | keyword:r^0.7) (title:s^0.9 | keyword:s^0.7) (title:t^0.9 | keyword:t^0.7) (title:y^0.9 | keyword:y^0.7))~5) ((((title:\"x r s t y\" title:\"x (syn::r s t syn::acr::rst) ? ? y\"))^0.9) | keyword:\"x r s t y\"^0.7)",
        BooleanQuery.class);
    
    

    
    
	  // author search, unfielded - just test it is there, the rest
    // of unittests is in the dedicated class
	  assertQueryEquals(req("defType", "aqp", "q", "accomazzi", 
        "qf", "author^2.3 title abstract^0.4"), 
        "(abstract:accomazzi^0.4 | ((author:accomazzi, author:accomazzi,*)^2.3) | title:accomazzi)", 
        DisjunctionMaxQuery.class);
	  
	  assertQueryEquals(req("defType", "aqp", "q", "\"accomazzi\\,\"", 
        "qf", "author^2.3 title abstract^0.4"), 
        "(abstract:accomazzi^0.4 | ((author:accomazzi, author:accomazzi,*)^2.3) | title:accomazzi)", 
        DisjunctionMaxQuery.class);
	  
	  // I have doubts about this one: (author:accomazzi, alberto author:accomazzi, a author:accomazzi,)~3
	  // it requires that it finds all three forms
	  assertQueryEquals(req("defType", "aqp", "q", "accomazzi,alberto", 
        "qf", "author^2.3 title",
        "aqp.unfielded.tokens.strategy", "multiply",
				"aqp.unfielded.tokens.new.type", "simple",
				"aqp.unfielded.phrase.edismax.synonym.workaround", "true"),
				// the first branch - the author:accomazzi, alberto
        "(" +
         "((((author:accomazzi, alberto author:accomazzi, a author:accomazzi,)~3)" + // this will never match
           " ((author:accomazzi, alberto author:accomazzi, a author:accomazzi,)~3))^2.3) " + //this will never match
         "| " +
         "(title:accomazzi title:alberto title:accomazzialberto)" +
        ") " +
        // the second branch - the phrase
        "(((author:accomazzi, alberto author:accomazzi, alberto * author:accomazzi, a author:accomazzi, a * author:accomazzi,)^2.3) | title:\"accomazzi (alberto accomazzialberto)\")", 
        BooleanQuery.class);
	  
	  
	  assertQueryEquals(req("defType", "aqp", "q", "accomazzi,alberto", 
        "qf", "author^2.3 title",
        "aqp.unfielded.tokens.strategy", "multiply",
				"aqp.unfielded.tokens.new.type", "simple",
				"aqp.unfielded.phrase.edismax.synonym.workaround", "true",
				"aqp.unfielded.tokens.function.name", "edismax"),
				// the first branch - the author:accomazzi, alberto
        "(" +
         "((((author:accomazzi, alberto author:accomazzi, a author:accomazzi,)~3)" + // this will never match
           " ((author:accomazzi, alberto author:accomazzi, a author:accomazzi,)~3))^2.3) " + //this will never match
         "| " +
         "(title:accomazzi title:alberto title:accomazzialberto)" +
        ") " +
        // the second branch - the phrase
        "(((author:accomazzi, alberto author:accomazzi, alberto * author:accomazzi, a author:accomazzi, a * author:accomazzi,)^2.3) | title:\"accomazzi (alberto accomazzialberto)\")", 
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
	  assertQueryEquals(req("defType", "aqp", "q", "edismax(MÜLLER)", 
	      "qf", "author^2.3 title abstract^0.4"), 
        "(((abstract:acr::müller abstract:acr::muller)^0.4) " +
        "| ((author:müller, author:mueller, author:muller,)^2.3) " +
        "| (title:acr::müller title:acr::muller))", DisjunctionMaxQuery.class);
	  assertQueryEquals(req("defType", "aqp", "q", "edismax(\"forman, c\")", 
        "qf", "author^2.3 title abstract^0.4"), 
        "(abstract:\"forman c\"^0.4 " +
        "| ((author:forman, c author:forman,)^2.3) " +
        "| title:\"forman c\")", DisjunctionMaxQuery.class);

	  // unfielded search should produce the same structure (the parser for author is aqp-type)
	  assertQueryEquals(req("defType", "aqp", "q", "MÜLLER", 
        "qf", "author^2.3 title abstract^0.4"), 
        "(((abstract:acr::müller abstract:acr::muller)^0.4) " +
        "| ((author:müller, author:müller,* author:mueller, author:mueller,* author:muller, author:muller,*)^2.3) " +
        "| (title:acr::müller title:acr::muller))", DisjunctionMaxQuery.class);
    assertQueryEquals(req("defType", "aqp", "q", "\"forman, c\"", 
        "qf", "author^2.3 title abstract^0.4"), 
        "(abstract:\"forman c\"^0.4 " +
        "| ((author:forman, c author:jones, christine author:jones, c author:forman, christine author:forman, c* author:forman,)^2.3) " +
        "| title:\"forman c\")", DisjunctionMaxQuery.class);
	  
    // now add a normal element
    assertQueryEquals(req("defType", "aqp", "q", "title:foo or edismax(MÜLLER)", 
        "qf", "author^2.3 title abstract^0.4"), 
        "title:foo (((abstract:acr::müller abstract:acr::muller)^0.4) " +
        "| ((author:müller, author:mueller, author:muller,)^2.3) " +
        "| (title:acr::müller title:acr::muller))", BooleanQuery.class);
    assertQueryEquals(req("defType", "aqp", "q", "title:foo or edismax(\"forman, c\")", 
        "qf", "author^2.3 title abstract^0.4"), 
        "title:foo (abstract:\"forman c\"^0.4 " +
        "| ((author:forman, c author:forman,)^2.3) " +
        "| title:\"forman c\")", BooleanQuery.class);
    
    
    // unfielded search should produce the same results
    assertQueryEquals(req("defType", "aqp", "q", "title:foo or MÜLLER", 
        "qf", "author^2.3 title abstract^0.4"), 
        "title:foo (((abstract:acr::müller abstract:acr::muller)^0.4) " +
        "| ((author:müller, author:müller,* author:mueller, author:mueller,* author:muller, author:muller,*)^2.3) " +
        "| (title:acr::müller title:acr::muller))", BooleanQuery.class);
    assertQueryEquals(req("defType", "aqp", "q", "title:foo or \"forman, c\"", 
        "qf", "author^2.3 title abstract^0.4"), 
        "title:foo (abstract:\"forman c\"^0.4 " +
        "| ((author:forman, c author:jones, christine author:jones, c author:forman, christine author:forman, c* author:forman,)^2.3) " +
        "| title:\"forman c\")", BooleanQuery.class);
    
    assertQueryEquals(req("defType", "aqp", "q", "title:foo or MÜLLER", 
        "qf", "author^2.3 title abstract^0.4"), 
        "title:foo (((abstract:acr::müller abstract:acr::muller)^0.4) " +
        "| ((author:müller, author:müller,* author:mueller, author:mueller,* author:muller, author:muller,*)^2.3) " +
        "| (title:acr::müller title:acr::muller))", BooleanQuery.class);
    assertQueryEquals(req("defType", "aqp", "q", "title:foo or \"forman, c\"", 
        "qf", "author^2.3 title abstract^0.4"), 
        "title:foo (abstract:\"forman c\"^0.4 " +
        "| ((author:forman, c author:jones, christine author:jones, c author:forman, christine author:forman, c* author:forman,)^2.3) " +
        "| title:\"forman c\")", BooleanQuery.class);
    
		
		assertQueryEquals(req("defType", "aqp", "q", "identifier:2011A&A...536A..89G"), 
        "identifier:2011a&a...536a..89g", TermQuery.class);
		assertQueryEquals(req("defType", "aqp", "q", "identifier:2011A&A" + "\u2026" + "536A..89G"), 
        "identifier:2011a&a...536a..89g", TermQuery.class);
		
		
		
		/*
		 * translation of the fields (on the fly)
		 */
		
		
		// field_map is set to translate arxiv->identifier
		assertQueryEquals(req("defType", "aqp", "q", "arxiv:1002.1524"), 
				"identifier:1002.1524", TermQuery.class);
		assertQueryParseException(req("defType", "aqp", "q", "arxivvvv:1002.1524"));
		
		
	}
	
	public static junit.framework.Test suite() {
        return new junit.framework.JUnit4TestAdapter(TestAqpAdsabsSolrSearch.class);
    }
	
	
}
