package org.apache.lucene.queryParser.aqp;

import org.apache.lucene.analysis.WhitespaceAnalyzer;
import org.apache.lucene.search.MatchAllDocsQuery;
import org.apache.lucene.search.MultiTermQuery;
import org.apache.lucene.search.PhraseQuery;
import org.apache.lucene.search.PrefixQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.WildcardQuery;
import org.apache.lucene.util.Version;

public class TestAqpAdslabs extends AqpTestAbstractCase {

	public void setUp() throws Exception {
		setGrammarName("ADS");
		super.setUp();
	}
	
	public AqpQueryParser getParser() throws Exception {
		AqpQueryParser qp = new AqpAdslabsQueryParser(getGrammarName());
		qp.setDebug(this.debugParser);
		return qp;
	}
	
	public void testAnalyzers() throws Exception {
		
		WhitespaceAnalyzer wsa = new WhitespaceAnalyzer(Version.LUCENE_CURRENT);
		
		assertQueryEquals("\"term germ\"~2", null, "\"term germ\"~2");
		assertQueryEquals("\"this\" AND that", null, "+\"this\" +that", PhraseQuery.class);
		
		assertQueryEquals("\"this\"", null, "\"this\"", PhraseQuery.class);
		assertQueryEquals("\"this  \"", null, "\"this  \"");
		assertQueryEquals("\"this  \"   ", null, "\"this  \"   ");
		assertQueryEquals("\"  this  \"", null, "\"  this  \"");
	}
	
	public void testAuthorField() throws Exception {
		assertQueryEquals("author:\"huchra, j\"", null, "author:\"huchra, j\"");
		
		
		assertQueryEquals("author:\"A Einstein\"", null, "author:\"a einstein\"");
		assertQueryEquals("=author:\"A Einstein\"", null, "author:\"A Einstein\"");
		
		assertQueryEquals("author:\"M. J. Kurtz\" author:\"G. Eichhorn\" 2004", null, "author:\"m. j. kurtz\" author:\"g. eichhorn\" 2004");
		assertQueryEquals("author:\"M. J. Kurtz\" author:\"G. Eichhorn\" 2004", null, "author:\"m. j. kurtz\" author:\"g. eichhorn\" -2004");
		
		assertQueryEquals("author:\"^Peter H. Smith\"", null, "pos(author:peter author:h. author:smith; 0)");
		
		
		// synonym replacement?
		assertQueryEquals("^Kurtz, M. -Eichhorn, G. 2000", null, "");
		
		assertQueryEquals("author:(kurtz~0.2; -echhorn)^2 OR ^accomazzi, a", null, "");
	}
	
	public void testAcronyms() throws Exception {
		assertQueryEquals("\"dark matter\" -LHC", null, "\"dark matter\" -lhc");
	}
	
	
	/**
	 * OK, done 17Apr
	 * 
	 * @throws Exception
	 */
	public void testIdentifiers() throws Exception {
		WhitespaceAnalyzer wsa = new WhitespaceAnalyzer(Version.LUCENE_CURRENT);
		Query q = null;
		setDebug(true);
		assertQueryEquals("arXiv:1012.5859", wsa, "arxiv:1012.5859");
		assertQueryEquals("xfield:10.1086/345794", wsa, "xfield:10.1086/345794");
		
		assertQueryEquals("arXiv:astro-ph/0601223", wsa, "arxiv:astro-ph/0601223");
		q = assertQueryEquals("xfield:arXiv:0711.2886", wsa, "xfield:arxiv:0711.2886");
		
		assertQueryEquals("foo AND bar AND 2003AJ....125..525J", wsa, "+foo +bar +2003aj....125..525j");
		
		assertQueryEquals("2003AJ….125..525J", wsa, "2003aj....125..525j");
		
		assertQueryEquals("one doi:word/word doi:word/123", wsa, "one identifier:doi:word/word identifier:doi:word/123");
		assertQueryEquals("doi:hey/156-8569", wsa, "doi:hey/156-8569");
		q = assertQueryEquals("doi:10.1000/182", wsa, "doi:10.1000/182");
		
		// pretend we are sending bibcode (this should be handled as a normal token)
		assertQueryEquals("200xAJ....125..525J", wsa, "200xAJ....125..525J");
	}
	
	/**
	 * 
	 * 
	 * @throws Exception
	 */
	public void testDateRanges() throws Exception {
		assertQueryEquals("intitle:\"QSO\" 1995-2000", null, "intitle:\"qso\" year:[1995 2000]");
		
		assertQueryEquals("2011", null, "");
		assertQueryEquals("2011-", null, "");
		assertQueryEquals("-2011", null, "");
		assertQueryEquals("2011-2012", null, "");
		
		assertQueryEquals("-2009", null, "");
		assertQueryEquals("2009-", null, "");
		assertQueryEquals("one 2009-2012", null, "");
		assertQueryEquals("notdate 09-12", null, "");
		assertQueryEquals("notdate 09-2012", null, "");
		
		assertQueryEquals("year:2000-", null, "");
		assertQueryEquals("2000-", null, "");
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
		assertQueryEquals("[* TO 20030101]", null, "[* TO 20030101]");
		assertQueryEquals("[20020101 TO *]^0.5", null, "[20020101 TO *]^0.5");
		assertQueryNodeException("[* 20030101]^0.5~");
		assertQueryNodeException("[20020101 *]^0.5~");
		assertQueryEquals("[this TO that]", null, "[this TO that]");
		assertQueryEquals("[this that]", null, "[this TO that]");
		assertQueryEquals("[this TO *]", null, "[this TO *]");
		assertQueryEquals("[this]", null, "[this TO *]");
		assertQueryEquals("[* this]", null, "[* TO this]");
		assertQueryEquals("[* TO this]", null, "[* TO this]");
		assertQueryEquals("[\"this\" TO \"that*\"]", null, "[this TO that*]");
		// TODO: verify this is correct (this phrase is not a phrase inside a range query)
		assertQueryEquals("[\"this phrase\" TO \"that phrase*\"]", null, "[this phrase TO that phrase*]");
		
		assertQueryEquals("[\"#$%^&\" TO \"&*()\"]", wsa, "[#$%^& TO &*()]");
		
		assertQueryEquals("+a:[this TO that]", null, "a:[this TO that]");
		assertQueryEquals("+a:[   this TO that   ]", null, "a:[this TO that]");
		
		assertQueryEquals("year:[2000 TO *]", null, "year:[2000 TO *]");
		
	}
	
	public void testFunctionalQueries() throws Exception {
		assertQueryEquals("funcA(funcB(funcC(value, \"phrase value\", nestedFunc(0, 2))))", null, "");
		assertQueryEquals("cites((title:(lectures physics) and author:Feynman))", null, "");
		
		assertQueryEquals("simbad(20 54 05.689 +37 01 17.38)", null, "");
		assertQueryEquals("simbad(10:12:45.3-45:17:50)", null, "");
		assertQueryEquals("simbad(15h17m-11d10m)", null, "");
		assertQueryEquals("simbad(15h17+89d15)", null, "");
		assertQueryEquals("simbad(275d11m15.6954s+17d59m59.876s)", null, "");
		assertQueryEquals("simbad(12.34567h-17.87654d)", null, "");
		assertQueryEquals("simbad(350.123456d-17.33333d <=> 350.123456-17.33333)", null, "");
		
		assertQueryEquals("pos(\"Accomazzi, A\", 1, -1)", null, "");
	}
	
	
	public void testModifiers() throws Exception {
		
		assertQueryEquals("jakarta^4 apache", null, "jakarta^4.0 apache");
		assertQueryEquals("\"jakarta apache\"^4 \"Apache Lucene\"", null, "\"jakarta apache\"^4.0 \"apache lucene\"");
		
		assertQueryEquals("this +(that thus)^7", null, "this +((that thus)^7.0)");
		assertQueryEquals("this (+(that)^7)", null, "this +that^7.0");
		
		assertQueryEquals("roam~", null, "roam~0.5");
		assertQueryEquals("roam~0.8", null, "roam~0.8");
		assertQueryEquals("roam~0.899999999", null, "roam~0.9");
		assertQueryNodeException("roam~8");
		assertQueryEquals("roam^", null, "roam");
		assertQueryEquals("roam^0.8", null, "roam^0.8");
		assertQueryEquals("roam^0.899999999", null, "roam^0.9");
		assertQueryEquals("roam^8", null, "roam^8.0");
		
		
		// this should fail
		assertQueryNodeException("roam^~");
		assertQueryEquals("roam^0.8~", null, "roam~0.5^0.8");
		assertQueryEquals("roam^0.899999999~0.5", null, "roam~0.5^0.9");
		
		// should this fail?
		assertQueryEquals("roam~^", null, "roam~0.5");
		assertQueryEquals("roam~0.8^", null, "roam~0.8");
		assertQueryEquals("roam~0.899999999^0.5", null, "roam~0.9^0.5");
		assertQueryEquals("this^ 5", null, "this^5.0");
		assertQueryEquals("this^5~ 9", null, "");
		assertQueryEquals("9999", null, "9999");
		assertQueryEquals("9999.1", null, "9999.1");
		assertQueryEquals("0.9999", null, "0.9999");
		assertQueryEquals("00000000.9999", null, "00000000.9999");
		
		assertQueryEquals("\"weak lensing\"~", null, "\"weak lensing\"");
		assertQueryEquals("\"jakarta apache\"~10", null, "\"jakarta apache\"~10");
		assertQueryEquals("\"jakarta apache\"^10", null, "\"jakarta apache\"^10.0");
		assertQueryEquals("\"jakarta apache\"~10^", null, "\"jakarta apache\"~10");
		assertQueryEquals("\"jakarta apache\"^10~", null, "\"jakarta apache\"^10.0");
		assertQueryEquals("\"jakarta apache\"~10^0.6", null, "\"jakarta apache\"~10.0^0.6");
		assertQueryEquals("\"jakarta apache\"^10~0.6", null, "\"jakarta apache\"~0.6^10.0");
		
		assertQueryEquals("#synonyms", null, "");
		assertQueryEquals("#(request synonyms)", null, "");
		assertQueryEquals("this and (one #two)", null, "");
		
		assertQueryEquals("=(exact search)", null, "");
		assertQueryEquals("=\"exact phrase\"", null, "");
		
		
		// also, we want to generate a warning message
		assertQueryEquals("one ^two", null, "one pos(field, two, 0)");
		assertQueryEquals("^one ^two$", null, "pos(field, one, 0) pos(field, two, 0, -1)");
		assertQueryEquals("^one NOT two$", null, "pos(field, one, 0) -pos(field, two, 0, -1");
		assertQueryEquals("one ^two, j, k$", null, "one pos(field, \"field:two +field:j +field:k\", 0, -1)");
		assertQueryEquals("one ^two,j,k$", null, "one pos(field, \"field:two +field:j +field:k\", 0, -1)");
		
		assertQueryEquals("one \"^author phrase\"", null, "");
		assertQueryEquals("one \"^author phrase$\"", null, "");
	}
	
	public void testExceptions() throws Exception {
		assertQueryNodeException("this (+(((+(that))))");		
		assertQueryNodeException("this (++(((+(that)))))");		
		
		assertQueryNodeException("this (+(((+(that))))");	
		assertQueryNodeException("this (++(((+(that)))))");
		
		assertQueryNodeException("escape:(\\+\\-\\&\\&\\|\\|\\!\\(\\)\\{\\}\\[\\]\\^\\\"\\~\\*\\?\\:\\\\)");
		
		assertQueryNodeException("[]");	
		
		assertQueryNodeException("+field:");
		
		assertQueryNodeException("=");
		assertQueryNodeException("one ^\"author phrase\"");		
		assertQueryNodeException("one ^\"author phrase\"$");
		
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
		
		assertQueryEquals("*", null, "*:*", MatchAllDocsQuery.class);
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
		
		assertQueryEquals("weak lensing", null, "weak lensing");		
		assertQueryEquals("+contact +binaries -eclipsing", null, "+contact +binaries -eclipsing");
		assertQueryEquals("+contact +xfield:binaries -eclipsing", null, "+contact +xfield:binaries -eclipsing");
		assertQueryEquals("intitle:\"yellow symbiotic\"", null, "intitle:\"yellow symbiotic\"");
		assertQueryEquals("\"galactic rotation\"", null, "\"galactic rotation\"");
		assertQueryEquals("title:\"X x\" AND text:go title:\"x y\" AND A", null, "(+title:\"x x\" +text:go) (+title:\"x y\" +a)");
		assertQueryEquals("title:X Y Z", null, "title:x y z"); // effectively --> title:x field:y field:z
		
		
		
		assertQueryEquals("\"jakarta apache\" OR jakarta", null, "\"jakarta apache\" jakarta");
		assertQueryEquals("\"jakarta apache\" AND \"Apache Lucene\"", null, "+\"jakarta apache\" +\"apache lucene\"");
		assertQueryEquals("\"jakarta apache\" NOT \"Apache Lucene\"", null, "+\"jakarta apache\" -\"apache lucene\"");
		assertQueryEquals("(jakarta OR apache) AND website", null, "+(jakarta apache) +website");
		
		assertQueryEquals("weak NEAR lensing", null, "spanNear([weak, lensing], 5, true)");
		
		//TODO: the parser does not recognize the number
		//assertQueryEquals("weka NEAR/2 lensing", null, "");
		//assertQueryEquals("weka NEAR2 lensing", null, "");
		
		assertQueryEquals("a -b", null, "a -b");
		assertQueryEquals("a +b", null, "a +b");
		assertQueryEquals("A – b", null, "a -b");
		assertQueryEquals("A + b", null, "a +b");
		
		
		assertQueryEquals("+jakarta lucene", null, "+jakarta lucene");
		
		
		assertQueryEquals("this (that)", null, "this that");
		assertQueryEquals("this ((that))", null, "this that");
		assertQueryEquals("(this) ((((((that))))))", null, "this that");
		assertQueryEquals("(this) (that)", null, "this that");
		assertQueryEquals("this +(that)", null, "this +that");
		assertQueryEquals("this ((((+(that)))))", null, "this +that");
		assertQueryEquals("this (+(((+(that)))))", null, "this +that");
		assertQueryEquals("this +((((+(that)))))", null, "this +that");
		assertQueryEquals("this +(+((((that)))))", null, "this +that");
		
		
		
		
				
		assertQueryEquals("title:(+return +\"pink panther\")", null, "+title:return +title:\"pink panther\"");
		assertQueryEquals("field:(one two three)", null, "one two three");
		assertQueryEquals("fieldx:(one +two -three)", null, "fieldx:one +fieldx:two -fieldx:three");
		assertQueryEquals("+field:(-one +two three)", null, "-one +two three");
		assertQueryEquals("-field:(-one +two three)", null, "-one +two three");
		assertQueryEquals("+field:(-one +two three) x:four", null, "+(-one +two three) x:four");
		assertQueryEquals("x:four -field:(-one +two three)", null, "x:four -(-one +two three)");
		
		//TODO
		//assertQueryEquals("x:four -field:(-one +two x:three)", null, "x:four -(-one +two x:three)");
		
		assertQueryEquals("a test:(one)", null, "a test:one");
		assertQueryEquals("a test:(a)", null, "a test:a");
		
		assertQueryEquals("test:(one)", null, "test:one");
		assertQueryEquals("field: (one)", null, "one");
		assertQueryEquals("field:( one )", null, "one");
		assertQueryEquals("+value", null, "value");
		assertQueryEquals("-value", null, "value"); //? should we allow - at the beginning?
		
		
		
		
		
		assertQueryEquals("m:(a b c)", null, "m:a m:b m:c");
		assertQueryEquals("+m:(a b c)", null, "m:a m:b m:c"); //??? +m:a +m:b +m:c
		assertQueryEquals("+m:(a b c) x:d", null, "+(m:a m:b m:c) x:d"); //? +m:a +m:b +m:c
		
		assertQueryEquals("m:(+a b c)", null, "+m:a m:b m:c");
		assertQueryEquals("m:(-a +b c)^0.6", null, "(-m:a +m:b m:c)^0.6");
		assertQueryEquals("m:(a b c or d)", null, "m:a m:b (m:c m:d)");
		assertQueryEquals("m:(a b c OR d)", null, "m:a m:b (m:c m:d)");
		assertQueryEquals("m:(a b c AND d)", null, "m:a m:b (+m:c +m:d)");
		assertQueryEquals("m:(a b c OR d NOT e)", null, "m:a m:b (m:c (+m:d -m:e))");
		assertQueryEquals("m:(a b NEAR c)", null, "m:a spanNear([m:b, m:c], 5, true)");
		assertQueryEquals("m:(a b NEAR c d AND e)", null, "m:a spanNear([m:b, m:c], 5, true) (+m:d +m:e)");
		assertQueryEquals("-m:(a b NEAR c d AND e)", null, "m:a spanNear([m:b, m:c], 5, true) (+m:d +m:e)"); //? should we allow - at the beginning?
		
		assertQueryEquals("author:(huchra)", null, "author:huchra");
		assertQueryEquals("author:(huchra, j)", null, "spanNear([author:huchra, author:j], 1, true)");
		assertQueryEquals("author:(kurtz; -eichhorn, g)", null, "+author:kurtz +spanNear([author:eichhorn, author:g], 1, true)");
		assertQueryEquals("author:(muench-nashrallah)", wsa, "author:muench-nashrallah");
		assertQueryEquals("\"dark matter\" OR (dark matter -LHC)", null, "\"dark matter\" dark matter -lhc");
		
		
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
}
