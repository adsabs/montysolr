package org.apache.lucene.queryparser.flexible.aqp;

import java.io.IOException;
import java.io.Reader;
import java.util.regex.Pattern;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.core.KeywordAnalyzer;
import org.apache.lucene.analysis.core.WhitespaceAnalyzer;
import org.apache.lucene.analysis.pattern.PatternTokenizer;
import org.apache.lucene.queries.function.FunctionQuery;
import org.apache.lucene.queryparser.flexible.aqp.AqpAdsabsQueryParser;
import org.apache.lucene.queryparser.flexible.aqp.AqpQueryParser;
import org.apache.lucene.queryparser.flexible.aqp.AqpTestAbstractCase;
import org.apache.lucene.queryparser.flexible.aqp.config.AqpAdsabsQueryConfigHandler;
import org.apache.lucene.sandbox.queries.SlowFuzzyQuery;
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
import org.apache.lucene.util.Version;

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
		qp.setDebug(this.debugParser);
		return qp;
	}
	
	public void testAnalyzers() throws Exception {
		WhitespaceAnalyzer wsa = new WhitespaceAnalyzer(Version.LUCENE_CURRENT);
		Analyzer pa = new Analyzer() {
			@Override
			protected TokenStreamComponents createComponents(String fieldName,
					Reader reader) {
				PatternTokenizer filter;
				try {
					filter = new PatternTokenizer(reader, Pattern.compile("\\|"), -1);
					return new TokenStreamComponents(filter);
				} catch (IOException e) {
					e.printStackTrace();
				}
				return null;
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
		
		WhitespaceAnalyzer wsa = new WhitespaceAnalyzer(Version.LUCENE_CURRENT);
		
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
	
	public void testOldPositionalSearch() throws Exception {
		
	  WhitespaceAnalyzer wsa = new WhitespaceAnalyzer(Version.LUCENE_CURRENT);
	  KeywordAnalyzer kwa = new KeywordAnalyzer();
	  
		// TODO: check for the generated warnings
		assertQueryEquals("^two", null, "pos(author,two,1,1)", FunctionQuery.class);
		assertQueryEquals("^two$", null, "pos(author,two,1,-1)", FunctionQuery.class);
		assertQueryEquals("two$", null, "pos(author,two,-1,-1)", FunctionQuery.class);
		
		assertQueryEquals("one ^two", null, "+one +pos(author,two,1,1)");
		assertQueryEquals("^one ^two$", null, "+pos(author,one,1,1) +pos(author,two,1,-1)");
		assertQueryEquals("^one NOT two$", null, "+pos(author,one,1,1) -pos(author,two,-1,-1)");
		assertQueryEquals("one ^two, j, k$", null, "+one +pos(author,\"two, j, k\",1,-1)");
		assertQueryEquals("one ^two,j,k$", null, "+one +pos(author,\"two,j,k\",1,-1)");
		
		assertQueryEquals("one \"^author phrase\"", null, "+one +pos(author,\"author phrase\",1,1)");
		assertQueryEquals("one \"^author phrase$\"", null, "+one +pos(author,\"author phrase\",1,-1)");
		assertQueryEquals("one \"author phrase$\"", null, "+one +pos(author,\"author phrase\",-1,-1)");
		
		
		// and now the very weird cases, but as the example shows, the name is NOT analyzed
		// and for this to work, we are required to make a strange number of steps, OR, translate
		// this query into another functional query
		assertQueryEquals("author:\"^Peter H. Smith\"", null, "pos(author,\"Peter H. Smith\",1,1)");
		assertQueryEquals("xfield:\"^Peter H. Smith\"", null, "pos(xfield,\"Peter H. Smith\",1,1)");
		
		
		// and what about synonym replacement? How shall we distinguish these cases and apply 
		// appropriate synonym/acronym translation/expansion to them? Worse, they can be mixed
		// by users with normal words
		assertQueryEquals("^Kurtz, M. -Eichhorn", null, 
			"+pos(author,\"Kurtz, M.\",1,1) -eichhorn");
		assertQueryEquals("^Kurtz, M. -Eichhorn, G. 2000", kwa, 
			"+pos(author,\"Kurtz, M.\",1,1) -Eichhorn, G. +2000");
		assertQueryEquals("^CERN, PH. -nothing", null, 
			"+pos(author,\"CERN, PH.\",1,1) -nothing");
		
		assertQueryEquals("author:(A~0.2; -B)^2 OR c", null, 
			"((author:a~0.2 -author:b)^2.0) c");
		assertQueryEquals("author:(A~0.2 -B)^2 OR c", null, 
			"((+author:a~0.2 -author:b)^2.0) c");
		assertQueryEquals("author:(kurtz~0.2; -echhorn)^2 OR ^accomazzi, a", null, 
				"((author:kurtz~0.2 -author:echhorn)^2.0) pos(author,\"accomazzi, a\",1,1)");
	}
	
	public void testAcronyms() throws Exception {
		assertQueryEquals("\"dark matter\" -LHC", null, "+\"dark matter\" -lhc");
	}
	
	
	/**
	 * OK, done 17Apr
	 * 
	 * @throws Exception
	 */
	public void testIdentifiers() throws Exception {
		WhitespaceAnalyzer wsa = new WhitespaceAnalyzer(Version.LUCENE_CURRENT);
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
	 * @throws Exception
	 */
	public void testDateRanges() throws Exception {
		WhitespaceAnalyzer wsa = new WhitespaceAnalyzer(Version.LUCENE_CURRENT);
		
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
		assertQueryEquals("year:2000-", null, "year:[2000 TO \\*]");
		assertQueryEquals("2000-", null, "date:[2000 TO \\*]");
		
		// i don't think we should try to guess this as a date
		assertQueryEquals("2011", null, "");
		assertQueryEquals("2011", wsa, "2011");
		
	}
	
	/**
	 * OK, 17Apr
	 * 
	 * @throws Exception
	 */
	public void testRanges() throws Exception {
		
		WhitespaceAnalyzer wsa = new WhitespaceAnalyzer(Version.LUCENE_CURRENT);
		
		assertQueryEquals("[20020101 TO 20030101]", null, "[20020101 TO 20030101]");
		assertQueryEquals("[20020101 TO 20030101]^0.5", null, "[20020101 TO 20030101]^0.5");
		assertQueryNodeException("[20020101 TO 20030101]^0.5~");
		assertQueryNodeException("[20020101 TO 20030101]^0.5~");
		assertQueryEquals("title:[20020101 TO 20030101]", null, "title:[20020101 TO 20030101]");
		assertQueryEquals("title:[20020101 TO 20030101]^0.5", null, "title:[20020101 TO 20030101]^0.5");
		assertQueryNodeException("title:[20020101 TO 20030101]^0.5~");
		assertQueryNodeException("title:[20020101 TO 20030101]^0.5~");
		assertQueryEquals("[* TO 20030101]", null, "[\\* TO 20030101]");
		assertQueryEquals("[20020101 TO *]^0.5", null, "[20020101 TO \\*]^0.5");
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
		
		assertQueryEquals("year:[2000 TO *]", null, "year:[2000 TO \\*]");
		
	}
	
	public void testFunctionalQueries() throws Exception {
		assertQueryEquals("pos(author, \"Accomazzi, A\", 1, \\-1)", null, "pos(author,\"Accomazzi, A\",1,-1)", FunctionQuery.class);
		assertQueryEquals("pos(author, Kurtz, 1, 1)", null, "pos(author,Kurtz,1,1)", FunctionQuery.class);
		
		
		// with the new parser, we are stricter (or lazier ;)) - i am not sure if it was a good idea to 
		// fill in the missing values
		//assertQueryEquals("pos(author, Kurtz, 1)", null, "pos(author,Kurtz,1,1)", FunctionQuery.class);
		//assertQueryEquals("pos(author, Kurtz, 5)", null, "pos(author,Kurtz,5,5)", FunctionQuery.class);
		
		assertQueryNodeException("pos(author:\"Accomazzi, A\", 1, \\-1)");
		assertQueryNodeException("pos(author, Kurtz, 1, \\-1, 5)");
		assertQueryNodeException("pos(author, Kurtz, 1)");
		
		/*
		 * 
		XXX: these work with our provider but should be inside SolrFunctionProvider
		as they may depend on the req objects
		 
		assertQueryEquals("ord(author)", null, "top(ord(author))", FunctionQuery.class);
		assertQueryEquals("literal(author)", null, "literal(author)", FunctionQuery.class);
		assertQueryEquals("literal(\"author\")", null, "literal(\"author\")", FunctionQuery.class);
		
		assertQueryEquals("max(5, 10)", null, "max(const(5.0),10.0)", FunctionQuery.class);
		assertQueryEquals("max(10, 5)", null, "max(const(10.0),5.0)", FunctionQuery.class);
		assertQueryEquals("log(0)", null, "log(const(0.0))", FunctionQuery.class);
		assertQueryEquals("sum(10, 5)", null, "sum(const(10.0),const(5.0))", FunctionQuery.class);
		*/
		
		
		
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
		
	}
	
	
	public void testModifiers() throws Exception {
		
		WhitespaceAnalyzer wsa = new WhitespaceAnalyzer(Version.LUCENE_CURRENT);
		
		assertQueryEquals("jakarta^4 apache", null, "+jakarta^4.0 +apache");
		assertQueryEquals("\"jakarta apache\"^4 \"Apache Lucene\"", null, "+\"jakarta apache\"^4.0 +\"apache lucene\"");
		
		assertQueryEquals("this +(that thus)^7", null, "+this +((+that +thus)^7.0)");
		assertQueryEquals("this (+(that)^7)", null, "+this +that^7.0");
		
		assertQueryEquals("roam~", null, "roam~2", FuzzyQuery.class);
		assertQueryEquals("roam~0.8", null, "roam~0.8", SlowFuzzyQuery.class);
		assertQueryEquals("roam~0.899999999", null, "roam~0.9");
		
		
		assertQueryEquals("roam^", null, "roam");
		assertQueryEquals("roam^0.8", null, "roam^0.8");
		assertQueryEquals("roam^0.899999999", null, "roam^0.9");
		assertQueryEquals("roam^8", null, "roam^8.0");
		
		
		// this should fail
		assertQueryNodeException("roam^~");
		assertQueryEquals("roam^0.8~", null, "roam~2^0.8");
		assertQueryEquals("roam^0.899999999~0.5", null, "roam~0.5^0.9");
		
		// should this fail?
		assertQueryEquals("roam~^", null, "roam~2");
		assertQueryEquals("roam~0.8^", null, "roam~0.8");
		assertQueryEquals("roam~0.899999999^0.5", null, "roam~0.9^0.5");
		
		// with wsa analyzer the 5 is retained as a token
		assertQueryEquals("this^ 5", wsa, "+this +5");
		
		// with standard tokenizer, it goes away
		assertQueryEquals("this^ 5", null, "this");
		
		assertQueryEquals("this^0. 5", wsa, "/this^0. 5/");
		assertQueryEquals("this^0.4 5", wsa, "+this^0.4 +5");
		
		assertQueryEquals("this^5~ 9", null, "this~2^5.0");
		assertQueryEquals("this^5~ 9", wsa, "+this~2^5.0 +9");
		
		assertQueryEquals("9999", wsa, "9999");
		assertQueryEquals("9999.1", wsa, "9999.1");
		assertQueryEquals("0.9999", wsa, "0.9999");
		assertQueryEquals("00000000.9999", wsa, "00000000.9999");
		
		// tilda used for phrases has a different meaning (it is not a fuzzy paramater)
		// but a proximity operator, thus it can be >= 1.0
		assertQueryEquals("\"weak lensing\"~", null, "\"weak lensing\"~2");
		assertQueryEquals("\"jakarta apache\"~10", null, "\"jakarta apache\"~10");
		assertQueryEquals("\"jakarta apache\"^10", null, "\"jakarta apache\"^10.0");
		assertQueryEquals("\"jakarta apache\"~10^", null, "\"jakarta apache\"~10");
		assertQueryEquals("\"jakarta apache\"^10~", null, "\"jakarta apache\"~2^10.0");
		assertQueryEquals("\"jakarta apache\"~10^0.6", null, "\"jakarta apache\"~10^0.6");
		assertQueryEquals("\"jakarta apache\"^10~0.6", null, "\"jakarta apache\"^10.0");
		assertQueryEquals("\"jakarta apache\"^10~2.4", null, "\"jakarta apache\"~2^10.0");
		
		
		// switching-off analysis for individual tokens:
		// this is an example of how complex the query parsing can be, and impossible
		// without a powerful builder (this would just be unthinkable with the standard
		// lucene parser and impossible with the invenio parser)
		assertQueryEquals("#5", null, "");
		assertQueryEquals("#(request synonyms 5)", null, "+request +synonyms");
		
		
		assertQueryEquals("this and (one #5)", null, "+this +one");
		assertQueryEquals("this and (one #5)", wsa, "+this +one +5");
		
		assertQueryEquals("=5", null, "5");
		assertQueryEquals("=(request synonyms 5)", null, "+request +synonyms +5");
		assertQueryEquals("this and (one =5)", null, "+this +one +5");
		assertQueryEquals("this and (one =5)", wsa, "+this +one +5");
		
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
		
		assertQueryNodeException("What , happens,with, commas ,,");
	}
	
	public void testWildCards() throws Exception {
		Query q = null;
		q = assertQueryEquals("te?t", null, "te?t", WildcardQuery.class);
		
		q = assertQueryEquals("test*", null, "test*", PrefixQuery.class);
	    assertEquals(MultiTermQuery.CONSTANT_SCORE_AUTO_REWRITE_DEFAULT, ((MultiTermQuery) q).getRewriteMethod());
	    
	    q = assertQueryEquals("test?", null, "test?", WildcardQuery.class);
	    assertEquals(MultiTermQuery.CONSTANT_SCORE_AUTO_REWRITE_DEFAULT, ((MultiTermQuery) q).getRewriteMethod());
		
		assertQueryEquals("te*t", null, "te*t", WildcardQuery.class);
		assertQueryEquals("*te*t", null, "*te*t", WildcardQuery.class);
		assertQueryEquals("*te*t*", null, "*te*t*", WildcardQuery.class);
		assertQueryEquals("?te*t?", null, "?te*t?", WildcardQuery.class);
		assertQueryEquals("te?t", null, "te?t", WildcardQuery.class);
		assertQueryEquals("te??t", null, "te??t", WildcardQuery.class);
		
		
		assertQueryNodeException("te*?t");	
		assertQueryNodeException("te?*t");
		
		
		// as I am discovering, there is no such a thing as a quoted wildcard 
		// query, it just turns into a regular wildcard query, well...
		
		assertQueryEquals("\"te*t phrase\"", null, "te*t phrase", WildcardQuery.class);
		assertQueryEquals("\"test* phrase\"", null, "test* phrase", WildcardQuery.class);
		assertQueryEquals("\"te*t phrase\"", null, "te*t phrase", WildcardQuery.class);
		assertQueryEquals("\"*te*t phrase\"", null, "*te*t phrase", WildcardQuery.class);
		assertQueryEquals("\"*te*t* phrase\"", null, "*te*t* phrase", WildcardQuery.class);
		assertQueryEquals("\"?te*t? phrase\"", null, "?te*t? phrase", WildcardQuery.class);
		assertQueryEquals("\"te?t phrase\"", null, "te?t phrase", WildcardQuery.class);
		assertQueryEquals("\"te??t phrase\"", null, "te??t phrase", WildcardQuery.class);
		assertQueryEquals("\"te*?t phrase\"", null, "te*?t phrase", WildcardQuery.class);
		
		
		assertQueryEquals("*", null, "*", WildcardQuery.class);
		assertQueryEquals("*:*", null, "*:*", MatchAllDocsQuery.class);
		
		assertQueryEquals("?", null, "?", WildcardQuery.class);
		
		
		// XXX: in fact, in the WildcardQuery, even escaped start \* will become *
		// so it is not possible to search for words that contain * as a literal 
		// character, to have it differently, WildcardTermEnum class would have 
		// to think of skipping \* and \?
		
		q = assertQueryEquals("*t\\*a", null, "*t*a", WildcardQuery.class); 
		
		assertQueryEquals("*t*a\\*", null, "*t*a*", WildcardQuery.class);
		assertQueryEquals("*t*a\\?", null, "*t*a?", WildcardQuery.class);
		assertQueryEquals("*t*\\a", null, "*t*a", WildcardQuery.class);
		
		assertQueryEquals("title:*", null, "title:*", WildcardQuery.class);
		assertQueryEquals("doi:*", null, "doi:*", WildcardQuery.class);
		
	}
	
	/**
	 * OK: 17Apr
	 * @throws Exception
	 */
	public void testEscaped() throws Exception {
		WhitespaceAnalyzer wsa = new WhitespaceAnalyzer(Version.LUCENE_CURRENT);
		assertQueryEquals("\\(1\\+1\\)\\:2", wsa, "(1+1):2", TermQuery.class);
		assertQueryEquals("th\\*is", wsa, "th*is", TermQuery.class);
		assertQueryEquals("a\\\\\\\\+b", wsa, "a\\\\+b", TermQuery.class);
		assertQueryEquals("a\\u0062c", wsa, "abc", TermQuery.class);
		assertQueryEquals("\\*t", wsa, "*t", TermQuery.class);
	}
	
	/**
	 * Almost finished: 17Apr
	 * 	TODO: x NEAR/2 y
	 *        x:four -field:(-one +two x:three)
	 *        "\"func(*) AND that\"" (should not be analyzed; AND becomes and)
	 *        
	 * @throws Exception
	 */
	public void testBasics() throws Exception{
		
		WhitespaceAnalyzer wsa = new WhitespaceAnalyzer(Version.LUCENE_CURRENT);
		KeywordAnalyzer kwa = new KeywordAnalyzer();
		assertQueryEquals("keyword:\"planets and satellites\"", wsa, "keyword:\"planets and satellites\"", PhraseQuery.class);
		
		assertQueryEquals("weak lensing", null, "+weak +lensing");
		assertQueryEquals("+contact +binaries -eclipsing", null, "+contact +binaries -eclipsing");
		//x assertQueryEquals("+contact +xfield:binaries -eclipsing", null, "+contact +xfield:binaries -eclipsing");
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
		
		//TODO: the parser does not recognize the number
		//assertQueryEquals("weka NEAR/2 lensing", null, "");
		//assertQueryEquals("weka NEAR2 lensing", null, "");
		
		assertQueryEquals("a -b", null, "+a -b");
		assertQueryEquals("a +b", null, "+a +b");
		assertQueryEquals("A – b", null, "+a -b");
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
		
		
		
		
				
		assertQueryEquals("title:(+return +\"pink panther\")", null, "+title:return +title:\"pink panther\"");
		assertQueryEquals("field:(one two three)", null, "+one +two +three");
		assertQueryEquals("field:(one OR two OR three)", null, "one two three");
		assertQueryEquals("fieldx:(one +two -three)", null, "+fieldx:one +fieldx:two -fieldx:three");
		assertQueryEquals("+field:(-one +two three)", null, "-one +two +three");
		assertQueryEquals("-field:(-one +two three)", null, "-one +two +three");
		assertQueryEquals("+field:(-one +two three) x:four", null, "-one +two +three +x:four");
		
		//TODO
		//assertQueryEquals("x:four -field:(-one +two three)", null, "+x:four -(-one +two +three)");
		//assertQueryEquals("x:four -field:(-one +two x:three)", null, "x:four -(-one +two x:three)");
		
		assertQueryEquals("a test:(one)", null, "+a +test:one");
		assertQueryEquals("a test:(a)", null, "+a +test:a");
		
		assertQueryEquals("test:(one)", null, "test:one");
		assertQueryEquals("field: (one)", null, "one");
		assertQueryEquals("field:( one )", null, "one");
		assertQueryEquals("+value", null, "value");
		assertQueryEquals("-value", null, "value"); //? should we allow - at the beginning?
		
		
		
		
		
		assertQueryEquals("m:(a b c)", null, "+m:a +m:b +m:c");
		assertQueryEquals("+m:(a b c)", null, "+m:a +m:b +m:c"); 
		assertQueryEquals("+m:(a b c) +x:d", null, "+m:a +m:b +m:c +x:d");
		assertQueryEquals("+m:(a b c) x:d", null, "+m:a +m:b +m:c +x:d"); // OR is default, I always get tripped
		assertQueryEquals("+m:(a b c) OR x:d", null, "+(+m:a +m:b +m:c) x:d");
		assertQueryEquals("+m:(a b c) -x:d", null, "+m:a +m:b +m:c -x:d");
		
		assertQueryEquals("+x:d +m:(a b c)", null, "+x:d +m:a +m:b +m:c");
		assertQueryEquals("x:d +m:(a b c)", null, "+x:d +m:a +m:b +m:c"); // OR is default, I always get tripped
		assertQueryEquals("x:d OR +m:(a b c)", null, "x:d +(+m:a +m:b +m:c)");
		assertQueryEquals("-x:d +m:(a b c)", null, "-x:d +m:a +m:b +m:c");
		
		assertQueryEquals("+x:d +m:(a b c) +y:e", null, "+x:d +m:a +m:b +m:c +y:e");
		assertQueryEquals("x:d +m:(a b c) y:e", null, "+x:d +m:a +m:b +m:c +y:e"); // OR is default, I always get tripped
		assertQueryEquals("x:d OR +m:(a b c) OR y:e", null, "x:d +(+m:a +m:b +m:c) y:e");
		assertQueryEquals("-x:d +m:(a b c) -y:e", null, "-x:d +m:a +m:b +m:c -y:e");
		
		assertQueryEquals("+m:(a OR b OR c) x:d", null, "+(m:a m:b m:c) +x:d");
		
		assertQueryEquals("m:(+a b c)", null, "+m:a +m:b +m:c");
		assertQueryEquals("m:(+a b OR c)", null, "+m:a +(m:b m:c)");
		assertQueryEquals("m:(-a +b c)^0.6", null, "(-m:a +m:b +m:c)^0.6");
		assertQueryEquals("m:(a b c or d)", null, "+(+m:a +m:b) +(m:c m:d)");
		
		assertQueryEquals("m:(a b c OR d)", null, "+(+m:a +m:b) +(m:c m:d)"); 
		assertQueryEquals("m:(a b c AND d)", null, "+m:a +m:b +m:c +m:d");
		assertQueryEquals("m:(a b c OR d NOT e)", null, "+(+m:a +m:b) +(m:c (+m:d -m:e))");
		assertQueryEquals("m:(a b NEAR c)", null, "+m:a +spanNear([m:b, m:c], 5, true)");
		assertQueryEquals("m:(a b NEAR c d AND e)", null, "+m:a +spanNear([m:b, m:c], 5, true) +(+m:d +m:e)");
		assertQueryEquals("-m:(a b NEAR c d AND e)", null, "+m:a +spanNear([m:b, m:c], 5, true) +(+m:d +m:e)"); //? should we allow - at the beginning?
		
		assertQueryEquals("m:(a b NEAR2 c)", null, "+m:a +spanNear([m:b, m:c], 2, true)");
    assertQueryEquals("m:(a b NEAR3 c d AND e)", null, "+m:a +spanNear([m:b, m:c], 3, true) +(+m:d +m:e)");
    assertQueryEquals("-m:(a b NEAR4 c d AND e)", null, "+m:a +spanNear([m:b, m:c], 4, true) +(+m:d +m:e)");
    assertQueryNodeException("m:(a b NEAR7 c)"); // by default, only range 1-5 is allowed (in configuration)
    
    assertQueryEquals("author:accomazzi, *a*", null, "author:accomazzi, *a*");
		assertQueryEquals("author:(huchra)", null, "author:huchra");
		assertQueryEquals("author:(huchra, j)", null, "+author:huchra +author:j");
		
		assertQueryEquals("author:(kurtz; -eichhorn, g)", kwa, "author:kurtz -author:eichhorn, g");
		assertQueryEquals("author:(kurtz; -\"eichhorn, g\")", null, "author:kurtz -author:\"eichhorn g\"");
		
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
      
      WhitespaceAnalyzer wsa = new WhitespaceAnalyzer(Version.LUCENE_CURRENT);
      assertQueryEquals("/foo$/", wsa, "/foo$/", RegexpQuery.class);
      assertQueryEquals("keyword:/foo$/", wsa, "keyword:/foo$/", RegexpQuery.class);
      assertQueryEquals("keyword:/^foo$/", wsa, "keyword:/^foo$/", RegexpQuery.class);
      assertQueryEquals("keyword:/^foo$/ AND \"^foo$\"", wsa, "+keyword:/^foo$/ +pos(author,foo,1,-1)", BooleanQuery.class);
  }
	
	public static junit.framework.Test suite() {
        return new junit.framework.JUnit4TestAdapter(TestAqpAdsabs.class);
    }
	
}
