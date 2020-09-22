package org.apache.lucene.queryparser.flexible.aqp;

import java.util.Map.Entry;
import java.util.regex.Pattern;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.core.KeywordAnalyzer;
import org.apache.lucene.analysis.core.WhitespaceAnalyzer;
import org.apache.lucene.analysis.pattern.PatternTokenizer;
import org.apache.lucene.queryparser.flexible.aqp.AqpAdsabsQueryParser;
import org.apache.lucene.queryparser.flexible.aqp.AqpQueryParser;
import org.apache.lucene.queryparser.flexible.aqp.AqpTestAbstractCase;
import org.apache.lucene.queryparser.flexible.aqp.config.AqpAdsabsQueryConfigHandler;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.FuzzyQuery;
import org.apache.lucene.search.MatchAllDocsQuery;
import org.apache.lucene.search.MultiTermQuery;
import org.apache.lucene.search.PhraseQuery;
import org.apache.lucene.search.PrefixQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.RegexpQuery;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.WildcardQuery;

public class TestAqpAdsabs extends AqpTestAbstractCase {
	
	
	
	public void setUp() throws Exception {
		setGrammarName("ADS");
		super.setUp();
	}
	
	
	public AqpQueryParser getParser() throws Exception {
		
		//AqpQueryParser qp = AqpAdsabsQueryParser.init(getGrammarName());
		
		AqpAdsabsQueryConfigHandler config = new AqpAdsabsQueryConfigHandler();
		config.set(AqpAdsabsQueryConfigHandler.ConfigurationKeys.SOLR_READY, false);
		AqpSyntaxParser parser = new AqpSyntaxParserLoadableImpl().initializeGrammar(grammarName);
		AqpAdsabsNodeProcessorPipeline processor = new AqpAdsabsNodeProcessorPipeline(config);
	    AqpAdsabsQueryTreeBuilder builder = new AqpAdsabsQueryTreeBuilder();
	    
	  AqpQueryParser qp = new AqpAdsabsQueryParser(config, parser, processor, builder);
	  for (Entry<String, String> e: parserArgs.entrySet()) {
    	qp.setNamedParameter(e.getKey(), e.getValue());
    }
		qp.setDebug(this.debugParser);
		return qp;
	}
	
	public void testAnalyzers() throws Exception {
		Analyzer pa = new Analyzer() {
			@Override
			protected TokenStreamComponents createComponents(String fieldName) {
				PatternTokenizer filter;
				filter = new PatternTokenizer(Pattern.compile("\\|"), -1);
				return new TokenStreamComponents(filter);
			}
		};
		
		assertQueryEquals("\"term germ\"~2", null, "\"term germ\"~2");
		assertQueryEquals("\"this\" AND that", null, "+this +that", BooleanQuery.class);
		
		assertQueryEquals("\"this\"", null, "this");
		assertQueryEquals("word:\"this  \"", pa, "word:this  ");
		assertQueryEquals("\"this  \"   ", pa, "this  ");
		assertQueryEquals("\"  this  \"", pa, "  this  ");
	}
	
	public void testAuthorField() throws Exception {
		// note: nothing too much exciting here - the real tests must be done with the 
		// ADS author query, and for that we will need solr unittests - so for now, just basic stuff
		
		WhitespaceAnalyzer wsa = new WhitespaceAnalyzer();
		
		assertQueryEquals("author:\"A Einstein\"", null, "author:\"a einstein\"", PhraseQuery.class);
		// probably, this should construct a different query (a phrase perhaps)
		assertQueryEquals("=author:\"A Einstein\"", null, "author:A Einstein", TermQuery.class);
		
		assertQueryEquals("author:\"M. J. Kurtz\" author:\"G. Eichhorn\" 2004", wsa, "+author:\"M. J. Kurtz\" +author:\"G. Eichhorn\" +2004");
		assertQueryEquals("author:\"M. J. Kurtz\" =author:\"G. Eichhorn\" 2004", null, "+author:\"m j kurtz\" +author:G. Eichhorn");
		
		assertQueryEquals("author:\"huchra, j\"", wsa, "author:\"huchra, j\"");
		assertQueryEquals("author:\"huchra, j\"", null, "author:\"huchra j\"");
		assertQueryEquals("=author:\"huchra, j\"", wsa, "author:huchra, j", TermQuery.class);
		assertQueryEquals("author:\"huchra, j.*\"", wsa, "author:huchra, j*", PrefixQuery.class);
		
	}
	
	
	public void testAcronyms() throws Exception {
		assertQueryEquals("\"dark matter\" -LHC", null, "+\"dark matter\" -lhc");
	}
	
	
	/**
	 * OK, done 17Apr
	 * 
	 */
	public void testIdentifiers() throws Exception {
		WhitespaceAnalyzer wsa = new WhitespaceAnalyzer();
		Query q = null;
		assertQueryEquals("arXiv:1012.5859", wsa, "arxiv:1012.5859");
		assertQueryEquals("xfield:10.1086/345794", wsa, "xfield:10.1086/345794");
		assertQueryEquals("xfield:doi:10.1086/345794", wsa, "xfield:10.1086/345794");
		
		assertQueryEquals("arXiv:astro-ph/0601223", wsa, "arxiv:astro-ph/0601223");
		q = assertQueryEquals("xfield:arXiv:0711.2886", wsa, "xfield:0711.2886");
		
		assertQueryEquals("foo AND bar AND 2003AJ....125..525J", wsa, "+foo +bar +2003aj....125..525j");
		
		assertQueryEquals("2003AJ….125..525J", wsa, "2003aj....125..525j");
		
		assertQueryEquals("one x:doi:word/word doi:word/123", wsa, "+one +x:word/word +doi:word/123");
		assertQueryEquals("doi:hey/156-8569", wsa, "doi:hey/156-8569");
		q = assertQueryEquals("doi:10.1000/182", wsa, "doi:10.1000/182");
		
		// pretend we are sending bibcode (this should be handled as a normal token)
		assertQueryEquals("200xAJ....125..525J", wsa, "200xAJ....125..525J");
	}
	
	/**
	 * OK, Apr19
	 * 
	 */
	public void testDateRanges() throws Exception {
		WhitespaceAnalyzer wsa = new WhitespaceAnalyzer();
		
		assertQueryEquals("intitle:\"QSO\" 1995-2000", null, "+intitle:qso +date:[1995 TO 2000]");
		
		
		assertQueryEquals("2011-2012", null, "date:[2011 TO 2012]");
		assertQueryEquals("xf:2011-2012", null, "xf:[2011 TO 2012]");
		assertQueryEquals("one 2009-2012", null, "+one +date:[2009 TO 2012]");
		assertQueryEquals("notdate 09-12", wsa, "+notdate +09-12");
		assertQueryEquals("notdate 09-2012", wsa, "+notdate +09-2012");
		
		
		//TODO - also test that warning messages were generated
		//TODO - throw syntax error?
		//TODO - lucene4.0 shows \* but it also has * when the value is null, the TermRanqeQueryNodeBuilder may be wrong 
		assertQueryEquals("2011-", null, "date:[2011 TO \\*]");
		assertQueryEquals("-2011", null, "date:[\\* TO 2011]");
		assertQueryEquals("-2009", null, "date:[\\* TO 2009]");
		assertQueryEquals("2009-", null, "date:[2009 TO \\*]");
		assertQueryEquals("year:2000-", null, "year:[2000 TO 2222]");
		assertQueryEquals("2000-", null, "date:[2000 TO \\*]");
		
		// i don't think we should try to guess this as a date
		assertQueryEquals("2011", null, "MatchNoDocsQuery(\"\")");
		assertQueryEquals("2011", wsa, "2011");
		
	}
	
	/**
	 * OK, 17Apr
	 * 
	 */
	public void testRanges() throws Exception {
		
		WhitespaceAnalyzer wsa = new WhitespaceAnalyzer();
		
		assertQueryEquals("[20020101 TO 20030101]", null, "[20020101 TO 20030101]");
		assertQueryEquals("[20020101 TO 20030101]^0.5", null, "([20020101 TO 20030101])^0.5");
		assertQueryNodeException("[20020101 TO 20030101]^0.5~");
		assertQueryNodeException("[20020101 TO 20030101]^0.5~");
		assertQueryEquals("title:[20020101 TO 20030101]", null, "title:[20020101 TO 20030101]");
		assertQueryEquals("title:[20020101 TO 20030101]^0.5", null, "(title:[20020101 TO 20030101])^0.5");
		assertQueryNodeException("title:[20020101 TO 20030101]^0.5~");
		assertQueryNodeException("title:[20020101 TO 20030101]^0.5~");
		assertQueryEquals("[* TO 20030101]", null, "[\\* TO 20030101]");
		assertQueryEquals("[20020101 TO *]^0.5", null, "([20020101 TO \\*])^0.5");
		assertQueryNodeException("[* 20030101]^0.5~");
		assertQueryNodeException("[20020101 *]^0.5~");
		assertQueryEquals("[this TO that]", null, "[this TO that]");
		assertQueryEquals("[this that]", null, "[this TO that]");
		assertQueryEquals("[this TO *]", null, "[this TO \\*]");
		assertQueryEquals("[this]", null, "[this TO \\*]");
		assertQueryEquals("[* this]", null, "[\\* TO this]");
		assertQueryEquals("[* TO this]", null, "[\\* TO this]");
		assertQueryEquals("[\"this\" TO \"that*\"]", null, "[this TO that*]");
		// TODO: verify this is correct (this phrase is not a phrase inside a range query)
		assertQueryEquals("[\"this phrase\" TO \"that phrase*\"]", null, "[this phrase TO that phrase*]");
		
		assertQueryEquals("[\"#$%^&\" TO \"&*()\"]", wsa, "[#$%^& TO &*()]");
		
		assertQueryEquals("+a:[this TO that]", null, "a:[this TO that]");
		assertQueryEquals("+a:[   this TO that   ]", null, "a:[this TO that]");
		
		assertQueryEquals("year:[2000 TO *]", null, "year:[2000 TO 2222]");
		
	}
	
	
	
	public void testModifiers() throws Exception {
		
		WhitespaceAnalyzer wsa = new WhitespaceAnalyzer();
		
		assertQueryEquals("jakarta^4 apache", null, "+(jakarta)^4.0 +apache");
		assertQueryEquals("\"jakarta apache\"^4 \"Apache Lucene\"", null, "+(\"jakarta apache\")^4.0 +\"apache lucene\"");
		
		assertQueryEquals("this +(that thus)^7", null, "+this +(+that +thus)^7.0");
		assertQueryEquals("this (+(that)^7)", null, "+this +(that)^7.0");
		
		assertQueryEquals("roam~", null, "roam~2", FuzzyQuery.class);
		
		
		assertQueryEquals("roam^", null, "(roam)^1.0");
		assertQueryEquals("roam^0.8", null, "(roam)^0.8");
		assertQueryEquals("roam^0.899999999", null, "(roam)^0.9");
		assertQueryEquals("roam^8", null, "(roam)^8.0");
		
		
		// this should fail
		assertQueryNodeException("roam^~");
		assertQueryEquals("roam^0.8~", null, "(roam~2)^0.8");
		assertQueryEquals("roam^0.899999999~0.5", null, "(roam~2)^0.9");
		
		// should this fail?
		assertQueryEquals("roam~^", null, "(roam~2)^1.0");
		assertQueryEquals("roam~0.8^", null, "(roam~0)^1.0");
		assertQueryEquals("roam~0.899999999^0.5", null, "(roam~0)^0.5");
		
		// with wsa analyzer the 5 is retained as a token
		assertQueryEquals("this^ 5", wsa, "+(this)^1.0 +5");
		
		// with standard tokenizer, it goes away
		assertQueryEquals("this^ 5", null, "(this)^1.0");
		
		assertQueryEquals("this^0. 5", wsa, "+this +5");
		assertQueryEquals("/this^0. 5/", wsa, "/this^0. 5/");
		assertQueryEquals("this^0.4 5", wsa, "+(this)^0.4 +5");
		
		assertQueryEquals("this^5~ 9", null, "(this~2)^5.0");
		assertQueryEquals("this^5~ 9", wsa, "+(this~2)^5.0 +9");
		
		assertQueryEquals("9999", wsa, "9999");
		assertQueryEquals("9999.1", wsa, "9999.1");
		assertQueryEquals("0.9999", wsa, "0.9999");
		assertQueryEquals("00000000.9999", wsa, "00000000.9999");
		
		// tilda used for phrases has a different meaning (it is not a fuzzy paramater)
		// but a proximity operator, thus it can be >= 1.0
		assertQueryEquals("\"weak lensing\"~", null, "\"weak lensing\"~2");
		assertQueryEquals("\"jakarta apache\"~10", null, "\"jakarta apache\"~10");
		assertQueryEquals("\"jakarta apache\"^10", null, "(\"jakarta apache\")^10.0");
		assertQueryEquals("\"jakarta apache\"~10^", null, "(\"jakarta apache\"~10)^1.0");
		assertQueryEquals("\"jakarta apache\"^10~", null, "(\"jakarta apache\"~2)^10.0");
		assertQueryEquals("\"jakarta apache\"~10^0.6", null, "(\"jakarta apache\"~10)^0.6");
		assertQueryEquals("\"jakarta apache\"^10~0.6", null, "(\"jakarta apache\")^10.0");
		assertQueryEquals("\"jakarta apache\"^10~2.4", null, "(\"jakarta apache\"~2)^10.0");
		
		
		// switching-off analysis for individual tokens:
		// this is an example of how complex the query parsing can be, and impossible
		// without a powerful builder (this would just be unthinkable with the standard
		// lucene parser and impossible with the invenio parser)
		assertQueryEquals("#5", null, "MatchNoDocsQuery(\"\")");
		assertQueryEquals("#(request synonyms 5)", null, "+request +synonyms");
		
		
		assertQueryEquals("this and (one #5)", null, "+this +(+one)");
		assertQueryEquals("this and (one #5)", wsa, "+this +(+one +5)");
		
		assertQueryEquals("=5", null, "5");
		
		assertQueryEquals("=(request synonyms 5)", null, "+request +synonyms +5");
		assertQueryEquals("this and (one =5)", null, "+this +(+one +5)");
		assertQueryEquals("this and (one =5)", wsa, "+this +(+one +5)");
		
	}
	
	public void testExceptions() throws Exception {
		assertQueryNodeException("this (+(((+(that))))");		
		assertQueryNodeException("this (++(((+(that)))))");		
		
		assertQueryNodeException("this (+(((+(that))))");	
		assertQueryNodeException("this (++(((+(that)))))");
		
		//assertQueryNodeException("escape:(\\+\\-\\&\\&\\|\\|\\!\\(\\)\\{\\}\\[\\]\\^\\\"\\~\\*\\?\\:\\\\)");
		
		assertQueryNodeException("[]");	
		
		assertQueryNodeException("+field:");
		
		assertQueryNodeException("=");
		assertQueryNodeException("one ^\"author phrase\"");
		
		// this parses well, shall we consider it mistake?
		//assertQueryNodeException("one ^\"author phrase\"$");
		
		assertQueryNodeException("this =and that");
		assertQueryNodeException("(doi:tricky:01235)");
		
	}
	
	public void testWildCards() throws Exception {
		Query q = null;
		q = assertQueryEquals("te?t", null, "te?t", WildcardQuery.class);
		
		q = assertQueryEquals("test*", null, "test*", WildcardQuery.class);
	    assertEquals(MultiTermQuery.CONSTANT_SCORE_REWRITE, ((MultiTermQuery) q).getRewriteMethod());
	    
	    q = assertQueryEquals("test?", null, "test?", WildcardQuery.class);
	    assertEquals(MultiTermQuery.CONSTANT_SCORE_REWRITE, ((MultiTermQuery) q).getRewriteMethod());
		
		assertQueryEquals("te*t", null, "te*t", WildcardQuery.class);
		assertQueryEqualsAllowLeadingWildcard("*te*t", null, "*te*t");
		assertQueryEqualsAllowLeadingWildcard("*te*t*", null, "*te*t*");
		assertQueryEqualsAllowLeadingWildcard("?te*t?", null, "?te*t?");
		assertQueryEquals("te?t", null, "te?t", WildcardQuery.class);
		assertQueryEquals("te??t", null, "te??t", WildcardQuery.class);
		
		
		assertQueryNodeException("te*?t");	
		assertQueryNodeException("te?*t");
		
		
		// as I am discovering, there is no such a thing as a quoted wildcard 
		// query, it just turns into a regular wildcard query, well...
		
		assertQueryEquals("\"te*t phrase\"", null, "te*t phrase", WildcardQuery.class);
		assertQueryEquals("\"test* phrase\"", null, "test* phrase", WildcardQuery.class);
		assertQueryEquals("\"te*t phrase\"", null, "te*t phrase", WildcardQuery.class);
		assertQueryEqualsAllowLeadingWildcard("\"*te*t phrase\"", null, "*te*t phrase");
		assertQueryEqualsAllowLeadingWildcard("\"*te*t* phrase\"", null, "*te*t* phrase");
		assertQueryEqualsAllowLeadingWildcard("\"?te*t? phrase\"", null, "?te*t? phrase");
		assertQueryEquals("\"te?t phrase\"", null, "te?t phrase", WildcardQuery.class);
		assertQueryEquals("\"te??t phrase\"", null, "te??t phrase", WildcardQuery.class);
		assertQueryEquals("\"te*?t phrase\"", null, "te*?t phrase", WildcardQuery.class);
		
		
		assertQueryEquals("*", null, "*:*", MatchAllDocsQuery.class);
		assertQueryEquals("*:*", null, "*:*", MatchAllDocsQuery.class);
		
		assertQueryEqualsAllowLeadingWildcard("?", null, "?");
		
		
		// XXX: in fact, in the WildcardQuery, even escaped start \* will become *
		// so it is not possible to search for words that contain * as a literal 
		// character, to have it differently, WildcardTermEnum class would have 
		// to think of skipping \* and \?
		
		assertQueryEqualsAllowLeadingWildcard("*t\\*a", null, "*t*a"); 
		
		assertQueryEqualsAllowLeadingWildcard("*t*a\\*", null, "*t*a*");
		assertQueryEqualsAllowLeadingWildcard("*t*a\\?", null, "*t*a?");
		assertQueryEqualsAllowLeadingWildcard("*t*\\a", null, "*t*a");
		
		assertQueryEqualsAllowLeadingWildcard("title:*", null, "title:*", PrefixQuery.class);
		assertQueryEqualsAllowLeadingWildcard("doi:*", null, "doi:*", PrefixQuery.class);
		
	}
	
	public void testEscaped() throws Exception {
		WhitespaceAnalyzer wsa = new WhitespaceAnalyzer();
		assertQueryEquals("\\(1\\+1\\)\\:2", wsa, "(1+1):2", TermQuery.class);
		assertQueryEquals("th*is", wsa, "th*is", WildcardQuery.class);
		assertQueryEquals("th\\*is", wsa, "th*is", TermQuery.class);
		assertQueryEquals("a\\\\\\\\+b", wsa, "a\\\\+b", TermQuery.class);
		assertQueryEquals("a\\u0062c", wsa, "abc", TermQuery.class);
		assertQueryEquals("\\*t", wsa, "*t", TermQuery.class);
	}
	
	/**
	 * 	TODO: x NEAR/2 y
	 *        x:four -field:(-one +two x:three)
	 *        "\"func(*) AND that\"" (should not be analyzed; AND becomes and)
	 *        
	 */
	public void testBasics() throws Exception{
		
		WhitespaceAnalyzer wsa = new WhitespaceAnalyzer();
		KeywordAnalyzer kwa = new KeywordAnalyzer();
		assertQueryEquals("keyword:\"planets and satellites\"", wsa, "keyword:\"planets and satellites\"", PhraseQuery.class);
		
		assertQueryEqualsAllowLeadingWildcard("full:*", null, "full:*", PrefixQuery.class);
		
		assertQueryEquals("weak lensing", null, "+weak +lensing");
		assertQueryEquals("+contact +binaries -eclipsing", null, "+contact +binaries -eclipsing");
		assertQueryEquals("+contact +foo:binaries -eclipsing", null, "+contact +foo:binaries -eclipsing");
		
		assertQueryEquals("intitle:\"yellow symbiotic\"", null, "intitle:\"yellow symbiotic\"");
		assertQueryEquals("\"galactic rotation\"", null, "\"galactic rotation\"", PhraseQuery.class);
		
		assertQueryEquals("title:\"X x\" AND text:go title:\"x y\" AND A", null, "+title:\"x x\" +text:go +title:\"x y\" +a");
		assertQueryEquals("title:\"X x\" OR text:go title:\"x y\" OR A", null, "+(title:\"x x\" text:go) +(title:\"x y\" a)");
		assertQueryEquals("title:X Y Z", null, "+title:x +y +z");
		assertQueryEquals("title:(X Y Z)", null, "+title:x +title:y +title:z");
		
		
		assertQueryEquals("\"jakarta apache\" OR jakarta", null, "\"jakarta apache\" jakarta");
		assertQueryEquals("\"jakarta apache\" AND \"Apache Lucene\"", null, "+\"jakarta apache\" +\"apache lucene\"");
		assertQueryEquals("\"jakarta apache\" NOT \"Apache Lucene\"", null, "+\"jakarta apache\" -\"apache lucene\"");
		assertQueryEquals("(jakarta OR apache) AND website", null, "+(jakarta apache) +website");
		
		assertQueryEquals("weak NEAR lensing", null, "spanNear([weak, lensing], 5, true)");
		assertQueryEquals("weka NEAR2 lensing", null, "spanNear([weka, lensing], 2, true)");
		
		assertQueryEquals("a -b", null, "+a -b");
		assertQueryEquals("a +b", null, "+a +b");
		assertQueryEquals("A–b", null, "+a +b"); // em dash is not an operator
		assertQueryEquals("A + b", null, "+a +b");
		
		
		assertQueryEquals("+jakarta lucene", null, "+jakarta +lucene");
		assertQueryEquals("+jakarta OR lucene", null, "+jakarta lucene");
		
		//setDebug(true);
		assertQueryEquals("this (that)", null, "+this +that");
		assertQueryEquals("this ((that))", null, "+this +that");
		assertQueryEquals("(this) ((((((that))))))", null, "+this +that");
		assertQueryEquals("(this) (that)", null, "+this +that");
		assertQueryEquals("this +(that)", null, "+this +that");
		assertQueryEquals("this OR +(that)", null, "this +that");
		assertQueryEquals("this ((((+(that)))))", null, "+this +that");
		assertQueryEquals("this (+(((+(that)))))", null, "+this +that");
		assertQueryEquals("this +((((+(that)))))", null, "+this +that");
		assertQueryEquals("this +(+((((that)))))", null, "+this +that");
		assertQueryEquals("(this that)", null, "+this +that");
		assertQueryEquals("(foo:this that)", null, "+foo:this +that");
		
		
		
				
		assertQueryEquals("title:(+return +\"pink panther\")", null, "+title:return +title:\"pink panther\"");
		assertQueryEquals("field:(one two three)", null, "+one +two +three");
		assertQueryEquals("field:(one OR two OR three)", null, "one two three");
		assertQueryEquals("fieldx:(one +two -three)", null, "+fieldx:one +fieldx:two -fieldx:three");
		assertQueryEquals("+field:(-one +two three)", null, "-one +two +three");
		assertQueryEquals("-field:(-one +two three)", null, "-one +two +three");
		assertQueryEquals("+field:(-one +two three) x:four", null, "+(-one +two +three) +x:four");
		
		
		assertQueryEquals("x:four -field:(-one +two three)", null, "+x:four -(-one +two +three)");
		
  	//TODO: the last x: field is overwritten, a bug, a feature?
		assertQueryEquals("x:four -foo:(-one +two x:three)", null, "+x:four -(-foo:one +foo:two +foo:three)");
		
	  //XXX: I know about this bug, but having no time to fix, higher priorities...
		assertQueryEquals("x:four -foo:(-one two x:three)", null, "+x:four -(-foo:one +foo:two +foo:three)");
		assertQueryEquals("x:a -f:(-b c x:z)", null, "+x:a -(-f:b +f:c +f:z)");
		
		
		assertQueryEquals("a test:(one)", null, "+a +test:one");
		assertQueryEquals("a test:(a)", null, "+a +test:a");
		
		assertQueryEquals("test:(one)", null, "test:one");
		assertQueryEquals("field: (one)", null, "one");
		assertQueryEquals("field:( one )", null, "one");
		assertQueryEquals("+value", null, "value");
		assertQueryEquals("-value", null, "value"); //? should we allow - at the beginning?
		
		
		
		
		
		assertQueryEquals("m:(a b c)", null, "+m:a +m:b +m:c");
		assertQueryEquals("+m:(a b c)", null, "+m:a +m:b +m:c"); 
		assertQueryEquals("+m:(a b c) +x:d", null, "+(+m:a +m:b +m:c) +x:d");
		assertQueryEquals("+m:(a b c) x:d", null, "+(+m:a +m:b +m:c) +x:d");
		assertQueryEquals("+m:(a b c) OR x:d", null, "+(+m:a +m:b +m:c) x:d");
		assertQueryEquals("+m:(a b c) -x:d", null, "+(+m:a +m:b +m:c) -x:d");
		
		assertQueryEquals("+x:d +m:(a b c)", null, "+x:d +(+m:a +m:b +m:c)");
		assertQueryEquals("x:d +m:(a b c)", null, "+x:d +(+m:a +m:b +m:c)");
		assertQueryEquals("x:d OR +m:(a b c)", null, "x:d +(+m:a +m:b +m:c)");
		assertQueryEquals("-x:d +m:(a b c)", null, "-x:d +(+m:a +m:b +m:c)");
		
		assertQueryEquals("+x:d +m:(a b c) +y:e", null, "+x:d +(+m:a +m:b +m:c) +y:e");
		assertQueryEquals("x:d +m:(a b c) y:e", null, "+x:d +(+m:a +m:b +m:c) +y:e");
		assertQueryEquals("x:d OR +m:(a b c) OR y:e", null, "x:d +(+m:a +m:b +m:c) y:e");
		assertQueryEquals("-x:d +m:(a b c) -y:e", null, "-x:d +(+m:a +m:b +m:c) -y:e");
		
		assertQueryEquals("+m:(a OR b OR c) x:d", null, "+(m:a m:b m:c) +x:d");
		
		assertQueryEquals("m:(+a b c)", null, "+m:a +m:b +m:c");
		assertQueryEquals("m:(+a b OR c)", null, "+m:a +(m:b m:c)");
		assertQueryEquals("m:(-a +b c)^0.6", null, "(-m:a +m:b +m:c)^0.6");
		assertQueryEquals("m:(a b c or d)", null, "+m:a +m:b +(m:c m:d)");
		
		assertQueryEquals("m:(a b c OR d)", null, "+m:a +m:b +(m:c m:d)"); 
		assertQueryEquals("m:(a b c AND d)", null, "+m:a +m:b +(+m:c +m:d)");
		assertQueryEquals("m:(a b c OR d NOT e)", null, "+m:a +m:b +(m:c (+m:d -m:e))");
		assertQueryEquals("m:(a b NEAR c)", null, "+m:a +spanNear([m:b, m:c], 5, true)");
		assertQueryEquals("m:(a b NEAR c d AND e)", null, "+m:a +spanNear([m:b, m:c], 5, true) +(+m:d +m:e)");
		assertQueryEquals("-m:(a b NEAR c d AND e)", null, "+m:a +spanNear([m:b, m:c], 5, true) +(+m:d +m:e)"); //? should we allow - at the beginning?
		
		assertQueryEquals("m:(a b NEAR2 c)", null, "+m:a +spanNear([m:b, m:c], 2, true)");
    assertQueryEquals("m:(a b NEAR3 c d AND e)", null, "+m:a +spanNear([m:b, m:c], 3, true) +(+m:d +m:e)");
    assertQueryEquals("-m:(a b NEAR4 c d AND e)", null, "+m:a +spanNear([m:b, m:c], 4, true) +(+m:d +m:e)");
    assertQueryNodeException("m:(a b NEAR7 c)"); // by default, only range 1-5 is allowed (in configuration)
    
    
		
		
		assertQueryEquals("author:(muench-nashrallah)", wsa, "author:muench-nashrallah");
		assertQueryEquals("\"dark matter\" OR (dark matter -LHC)", null, "\"dark matter\" (+dark +matter -lhc)");
		
		
		assertQueryEquals("this999", wsa, "this999");
		assertQueryEquals("this0.9", wsa, "this0.9");
		
		assertQueryEquals("\"a \\\"b c\\\" d\"", wsa, "\"a \"b c\" d\"", PhraseQuery.class);
		assertQueryEquals("\"a \\\"b c\\\" d\"", wsa, "\"a \"b c\" d\"", PhraseQuery.class);
		assertQueryEquals("\"a \\+b c d\"", wsa, "\"a +b c d\"");
		
		
		
		
		assertQueryEquals("\"+() AND that\"", wsa, "\"+() AND that\"");
		assertQueryEquals("\"func(a) AND that\"", wsa, "\"func(a) AND that\"");
		
		// TODO: something funny happens with quoted-truncated (it is analyzed)
		//assertQueryEquals("\"func(*) AND that\"", wsa, "\"func(*) AND that\"");
		
		assertQueryEquals("CO2+", wsa, "CO2+", TermQuery.class);
		
		 
	}
	
	
	public void _testMultiToken() throws Exception{
			
			KeywordAnalyzer a = new KeywordAnalyzer();
			//setDebug(true);
			assertQueryEquals("weak lensing", null, "weak lensing");
			assertQueryEquals("all:weak lensing", null, "all:weak lensing");
			assertQueryEquals("weak all:lensing", null, "weak all:lensing");
			
			// the grammar parses this as: A B D -E (which is actually correct)
			assertQueryEquals("A B (D -E)", null, "A B (D -E)");
			
			assertQueryEquals("A B +(D -E)", null, "A B (D -E)");
			
			assertQueryEquals("weak lensing", null, "+weak lensing");
			assertQueryEquals("weak lensing", null, "+weak lensing");
			assertQueryEquals("weak lensing", null, "+weak lensing");
	}
	
  public void testRegex() throws Exception{
      
      WhitespaceAnalyzer wsa = new WhitespaceAnalyzer();
      assertQueryEquals("/foo$/", wsa, "/foo$/", RegexpQuery.class);
      assertQueryEquals("keyword:/foo$/", wsa, "keyword:/foo$/", RegexpQuery.class);
      assertQueryEquals("keyword:/^foo$/", wsa, "keyword:/^foo$/", RegexpQuery.class);
      assertQueryEquals("keyword:/^foo$/ AND \"foo bar\"", wsa, "+keyword:/^foo$/ +\"foo bar\"", BooleanQuery.class);
  }
  
  public void testDelimiters() throws Exception {
  	
  	KeywordAnalyzer wsa = new KeywordAnalyzer();
  	
  	assertQueryEquals("What , happens,with commas ,,",
  			null,
  			"+what +happens +with +commas", 
  			BooleanQuery.class);
  	
  	// this instructs parser to concatenate unfielded values
  	parserArgs.put("aqp.unfielded.tokens.strategy", "join");
  	assertQueryEquals("What , happens,with commas ,,",
  			null,
  			"+what +happens +with +commas", 
  			BooleanQuery.class);
  	
  	assertQueryEquals("What ; happens;with semicolons ;;",
  			null,
  			"+what +happens +with +semicolons", 
  			BooleanQuery.class);

  	// now using different analyzer
  	assertQueryEquals("What , happens,with commas ,,",
  			wsa,
  			"What , happens,with commas ,,", 
  			TermQuery.class);
  	
  	assertQueryEquals("What ; happens;with semicolons ;;",
  			wsa,
  			"What ; happens;with semicolons ;;", 
  			TermQuery.class);
  	
  	// deactivate joining
  	parserArgs.clear();
  	
  	assertQueryEquals("What , happens,with commas ,,",
  			null,
  			"+what +happens +with +commas", 
  			BooleanQuery.class);
  	
  	assertQueryEquals("What ; happens;with semicolons ;;",
  			null,
  			"+what +happens +with +semicolons", 
  			BooleanQuery.class);

  	// now using different analyzer
  	assertQueryEquals("What , happens,with commas ,,",
  			wsa,
  			"+What +happens +with +commas", 
  			BooleanQuery.class);
  	
  	assertQueryEquals("What ; happens;with semicolons ;;",
  			wsa,
  			"+What +happens +with +semicolons", 
  			BooleanQuery.class);
  }
	
  
  public void testMultipleTokenConcatenation() throws Exception  {
  	
  	KeywordAnalyzer kwa = new KeywordAnalyzer();
  	
  	// this should be concatenated into one phrase
  	parserArgs.put("aqp.unfielded.tokens.strategy", "join");
  	parserArgs.put("aqp.unfielded.tokens.new.type", "phrase");
  	
  	assertQueryEquals("foo:(A)", null, "foo:a");
  	assertQueryEquals("foo:(A -B)", null, "+foo:a -foo:b");
  	assertQueryEquals("foo:(A B D E)", null, "+foo:a +foo:b +foo:d +foo:e"); // but this is fielded
  	assertQueryEquals("A B D E", null, "\"a b d e\"");
  	assertQueryEquals("+A B D E", null, "\"a b d e\"");
  	assertQueryEquals("A +B D E", null, "+a +\"b d e\"");
  	assertQueryEquals("+(A B D E)", null, "\"a b d e\"");
  	//setDebug(true);
  	assertQueryEquals("=(A B D E)", null, "+A +B +D +E"); // '=' modifier makes it reject concatenation
  	
  	assertQueryEquals("+foo:z +A B D E", null, "+foo:z +\"a b d e\"");
  	assertQueryEquals("+A B D E +foo:z", null, "+\"a b d e\" +foo:z");

  	// this should add new phrase
  	parserArgs.put("aqp.unfielded.tokens.strategy", "add");
  	parserArgs.put("aqp.unfielded.tokens.new.type", "phrase");
  	
  	assertQueryEquals("foo:(A)", null, "foo:a");
  	assertQueryEquals("foo:(A -B)", null, "+foo:a -foo:b");
  	assertQueryEquals("foo:(A B D E)", null, "+foo:a +foo:b +foo:d +foo:e"); // fielded
  	assertQueryEquals("A B D E", null, "(+a +b +d +e) \"a b d e\"");
  	assertQueryEquals("+A B D E", null, "(+a +b +d +e) \"a b d e\"");
  	assertQueryEquals("A +B D E", null, "+a +((+b +d +e) \"b d e\")");
  	
  	assertQueryEquals("+foo:z +A B D E", null, "+foo:z +((+a +b +d +e) \"a b d e\")");
  	assertQueryEquals("+A B D E +foo:z", null, "+((+a +b +d +e) \"a b d e\") +foo:z");
  	
    // this should add new token (which will be analyzed by analyzer and can produce st different)
  	parserArgs.put("aqp.unfielded.tokens.strategy", "add");
  	parserArgs.put("aqp.unfielded.tokens.new.type", "normal");
  	assertQueryEquals("foo:(A)", null, "foo:a");
  	assertQueryEquals("foo:(A -B)", null, "+foo:a -foo:b");
  	assertQueryEquals("foo:(A B D E)", kwa, "+foo:A +foo:B +foo:D +foo:E"); // fielded
  	assertQueryEquals("A B D E", kwa, "(+A +B +D +E) A B D E");
  	assertQueryEquals("+A B D E", kwa, "(+A +B +D +E) A B D E");
  	assertQueryEquals("A +B D E", kwa, "+A +((+B +D +E) B D E)");
  	
  	assertQueryEquals("+foo:z +A B D E", null, "+foo:z +((+a +b +d +e) (+a +b +d +e))");
  	assertQueryEquals("+A B D E +foo:z", null, "+((+a +b +d +e) (+a +b +d +e)) +foo:z");
  	
  	
  	
  	parserArgs.put("aqp.unfielded.tokens.strategy", "add");
  	parserArgs.put("aqp.unfielded.tokens.new.type", "phrase");
  	assertQueryEquals("author:(huchra)", null, "author:huchra");
  	// without field specific logic, we don't understand this as elements of the same name
		assertQueryEquals("author:(huchra, j)", null, "+author:huchra +author:j");
		assertQueryEquals("author:(kurtz; -eichhorn, g)", kwa, "+author:kurtz -author:eichhorn +author:g");
		
		assertQueryEquals("author:(kurtz; -\"eichhorn, g\")", null, "+author:kurtz -author:\"eichhorn g\"");
		
		
		// here the stakes are higher - we don't understand that the next value
		// is not author
		parserArgs.put("aqp.unfielded.tokens.strategy", "add");
  	parserArgs.put("aqp.unfielded.tokens.new.type", "phrase");
		assertQueryEquals("author:huchra nasa", null, "(+author:huchra +nasa) author:\"huchra nasa\"");
		assertQueryEquals("author:accomazzi property:refereed apj", null, "+author:accomazzi +((+property:refereed +apj) property:\"refereed apj\")");
   
		// this is the best what we can do - it is similar to the strategy
		// above, only that in SOLR the values can be analyzed properly by edismax
		// in this test, lucene cant handle it well...
		parserArgs.put("aqp.unfielded.tokens.strategy", "multiply");
  	parserArgs.put("aqp.unfielded.tokens.new.type", "phrase");
		assertQueryEquals("author:huchra nasa", kwa, "author:huchra nasa author:\"huchra nasa\"");
		assertQueryEquals("author:accomazzi property:refereed apj", kwa, "+author:accomazzi +(property:refereed apj property:\"refereed apj\")");
  }
  
  
	public static junit.framework.Test suite() {
        return new junit.framework.JUnit4TestAdapter(TestAqpAdsabs.class);
    }
	
}
