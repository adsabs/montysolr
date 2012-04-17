package org.apache.lucene.queryParser.aqp;

import org.apache.lucene.search.MultiTermQuery;
import org.apache.lucene.search.PhraseQuery;
import org.apache.lucene.search.PrefixQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.WildcardQuery;

public class TestAqpAdslabs extends AqpTestAbstractCase {

	public void setUp() throws Exception {
		setGrammarName("ADS");
		setDebug(true);
		super.setUp();
	}
	
	public AqpQueryParser getParser() throws Exception {
		AqpQueryParser qp = new AqpAdslabsQueryParser(getGrammarName());
		qp.setDebug(this.debugParser);
		return qp;
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
	}
	
	public void testAcronyms() throws Exception {
		assertQueryEquals("\"dark matter\" -LHC", null, "\"dark matter\" -lhc");
	}
	
	public void testIdentifiers() throws Exception {
		assertQueryEquals("arXiv:1012.5859", null, "identifier:\"arxiv:1012.5859\"");
		assertQueryEquals("10.1086/345794", null, "identifier:10.1086/345794");
		
		assertQueryEquals("arXiv:astro-ph/0601223", null, "identifier:arxiv:astro-ph/0601223");
		assertQueryEquals("arXiv:0711.2886", null, "identifier:arxiv:0711.2886");
		
		assertQueryEquals("2003AJ....125..525J", null, "identifier:2003aj....125..525j");
		// todo: maybe deal with the 3dot char?
		//assertQueryEquals("2003AJ….125..525J", null, "identifier:2003AJ….125..525J");
		
		assertQueryEquals("one doi:word/word doi:word/123", null, "one identifier:doi:word/word identifier:doi:word/123");
		assertQueryEquals("doi:hey/156-8569", null, "");
		assertQueryEquals("doi:10.1000/182", null, "");
	}
	
	
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
	}
	
	public void testRanges() throws Exception {
		
		assertQueryEquals("[20020101 TO 20030101]", null, "[20020101 TO 20030101]");
		assertQueryEquals("[20020101 TO 20030101]^0.5", null, "[20020101 TO 20030101]^0.5");
		assertQueryEquals("[20020101 TO 20030101]^0.5~", null, "[20020101 TO 20030101]^0.5");
		assertQueryEquals("[20020101 TO 20030101]^0.5~", null, "[20020101 TO 20030101]^0.5");
		assertQueryEquals("title:[20020101 TO 20030101]", null, "title:[20020101 TO 20030101]");
		assertQueryEquals("title:[20020101 TO 20030101]^0.5", null, "title:[20020101 TO 20030101]^0.5");
		assertQueryEquals("title:[20020101 TO 20030101]^0.5~", null, "title:[20020101 TO 20030101]^0.5");
		assertQueryEquals("title:[20020101 TO 20030101]^0.5~", null, "title:[20020101 TO 20030101]^0.5");
		assertQueryEquals("[* TO 20030101]", null, "[* TO 20030101]");
		assertQueryEquals("[20020101 TO *]^0.5", null, "[20020101 TO *]^0.5");
		assertQueryEquals("[* 20030101]^0.5~", null, "[* 20030101]^0.5");
		assertQueryEquals("[20020101 *]^0.5~", null, "[20020101 *]^0.5");
		assertQueryEquals("[this TO that]", null, "[this TO that]");
		assertQueryEquals("[this that]", null, "[this TO that]");
		assertQueryEquals("[this TO *]", null, "[this TO *]");
		assertQueryEquals("[this]", null, "[this TO *]");
		assertQueryEquals("[* this]", null, "[* TO this]");
		assertQueryEquals("[* TO this]", null, "[* TO this]");
		assertQueryEquals("[\"this\" TO \"that*\"]", null, "[\"this\" TO \"that*\"]");
		assertQueryEquals("[\"#$%^&\" TO \"&*()\"]", null, "[\"#$%^&\" TO \"&*()\"]");
		
		assertQueryEquals("+a:[this TO that]", null, "a:[this TO that]");
		assertQueryEquals("+a:[   this TO that   ]", null, "a:[this TO that]");
		
		assertQueryEquals("year:[2000 TO *]", null, "");
		assertQueryEquals("year:2000-", null, "");
		assertQueryEquals("2000-", null, "");
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
		assertQueryEquals("funcA(funcB(funcC(value, \"phrase value\", nestedFunc(0, 2))))", null, "");
		assertQueryEquals("jakarta^4 apache", null, "jakarta^4 apache");
		assertQueryEquals("\"jakarta apache\"^4 \"Apache Lucene\"", null, "\"jakarta apache\"^4 \"apache lucene\"");
		
		assertQueryEquals("this +(that thus)^7", null, "this +(that thus)^7");
		assertQueryEquals("this (+(that)^7)", null, "this +(that)^7");
		
		assertQueryEquals("roam~", null, "roam");
		assertQueryEquals("roam~0.8", null, "roam~0.8");
		assertQueryEquals("roam~0.899999999", null, "roam~0.899999999");
		assertQueryEquals("roam~8", null, "roam~8");
		assertQueryEquals("roam^", null, "roam");
		assertQueryEquals("roam^0.8", null, "roam^0.8");
		assertQueryEquals("roam^0.899999999", null, "roam^0.899999999");
		assertQueryEquals("roam^8", null, "roam^8");
		
		
		// this should fail?
		//assertQueryEquals("roam^~", null, "");
		assertQueryEquals("roam^0.8~", null, "roam^0.8");
		assertQueryEquals("roam^0.899999999~0.5", null, "roam^0.899999999~0.5");
		
		// this should fail?
		assertQueryEquals("roam~^", null, "");
		assertQueryEquals("roam~0.8^", null, "");
		assertQueryEquals("roam~0.899999999^0.5", null, "");
		assertQueryEquals("this^ 5", null, "");
		assertQueryEquals("this^5~ 9", null, "");
		assertQueryEquals("9999", null, "");
		assertQueryEquals("9999.1", null, "");
		assertQueryEquals("0.9999", null, "");
		assertQueryEquals("00000000.9999", null, "");
		
		assertQueryEquals("\"weak lensing\"~", null, "");
		assertQueryEquals("\"jakarta apache\"~10", null, "\"jakarta apache\"~10");
		assertQueryEquals("\"jakarta apache\"^10", null, "\"jakarta apache\"^10");
		assertQueryEquals("\"jakarta apache\"~10^", null, "\"jakarta apache\"~10");
		assertQueryEquals("\"jakarta apache\"^10~", null, "\"jakarta apache\"^10~");
		assertQueryEquals("\"jakarta apache\"~10^0.6", null, "\"jakarta apache\"~10^0.6");
		assertQueryEquals("\"jakarta apache\"^10~0.6", null, "\"jakarta apache\"~0.6^10");
		
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
	}
	
	public void testWildCards() throws Exception {
		Query q = null;
		q = assertQueryEquals("te?t", null, "te?t", PrefixQuery.class);
		assertEquals(MultiTermQuery.CONSTANT_SCORE_AUTO_REWRITE_DEFAULT, ((MultiTermQuery) q).getRewriteMethod());
		
		q = assertQueryEquals("test*", null, "test*", PrefixQuery.class);
	    assertEquals(MultiTermQuery.CONSTANT_SCORE_AUTO_REWRITE_DEFAULT, ((MultiTermQuery) q).getRewriteMethod());
		
		assertQueryEquals("te*t", null, "te*t", WildcardQuery.class);
		assertQueryEquals("*te*t", null, "*te*t", WildcardQuery.class);
		assertQueryEquals("*te*t*", null, "*te*t*", WildcardQuery.class);
		assertQueryEquals("?te*t?", null, "?te*t?", WildcardQuery.class);
		assertQueryEquals("te?t", null, "te?t", WildcardQuery.class);
		assertQueryEquals("te??t", null, "te??t", WildcardQuery.class);
		
		//assertQueryEquals("te*?t"		OK	
		
		assertQueryEquals("\"text\"", null, "\"text\"");
		assertQueryEquals("\"te*t\"", null, "\"te*t\"");
		assertQueryEquals("\"test*\"", null, "\"test*\"");
		assertQueryEquals("\"te*t\"", null, "\"te*t\"");
		assertQueryEquals("\"*te*t\"", null, "\"*te*t\"");
		assertQueryEquals("\"*te*t*\"", null, "\"*te*t*\"");
		assertQueryEquals("\"?te*t?\"", null, "\"?te*t?\"");
		assertQueryEquals("\"te?t\"", null, "\"te?t\"");
		assertQueryEquals("\"te??t\"", null, "\"te??t\"");
		assertQueryEquals("\"te*?t\"", null, "\"te*?t\"");
		
		assertQueryEquals("*", null, "*:*");
		assertQueryEquals("*:*", null, "*:*");
		
		assertQueryEquals("?", null, "");
		
		
		assertQueryEquals("*t\\*a", null, "*t*a"); // todo: test that the word = 't*a'
		assertQueryEquals("*t*a\\*", null, "*t*a*");
		assertQueryEquals("*t*a\\?", null, "");
		assertQueryEquals("*t*\\a", null, "");
		
		
	}
	
	public void testEscaped() throws Exception {
		assertQueryEquals("\\(1\\+1\\)\\:2", null, "(1+1):2", TermQuery.class);
		assertQueryEquals("th\\*is", null, "th*is", TermQuery.class);
		assertQueryEquals("a\\\\\\+b", null, "a\\\\\\+b", TermQuery.class);
		assertQueryEquals("a\\u0062c", null, "");
		assertQueryEquals("\\*t", null, "");
	}
	
	public void testBasics() throws Exception{
		
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
		assertQueryEquals("x:four -field:(-one +two x:three)", null, "x:four -(-one +two x:three)");
		
		assertQueryEquals("a test:(one)", null, "a test:one");
		assertQueryEquals("a test:(a)", null, "a test:a");
		
		assertQueryEquals("test:(one)", null, "test:one");
		assertQueryEquals("field: (one)", null, "one");
		assertQueryEquals("field:( one )", null, "one");
		assertQueryEquals("+value", null, "value");
		assertQueryEquals("-value", null, "value"); //? should we allow - at the beginning?
		
		
		
		
		
		assertQueryEquals("m:(a b c)", null, "m:a m:b m:c");
		assertQueryEquals("+m:(a b c)", null, "m:a m:b m:c"); //? +m:a +m:b +m:c
		assertQueryEquals("+m:(a b c) x:d", null, "+(m:a m:b m:c) x:d"); //? +m:a +m:b +m:c
		
		assertQueryEquals("m:(+a b c)", null, "+m:a m:b m:c");
		assertQueryEquals("m:(-a +b c)^0.6", null, "-m:a^0.6 +m:b^0.6 m:c^0.6");
		assertQueryEquals("m:(a b c or d)", null, "m:a m:b (m:c m:d)");
		assertQueryEquals("m:(a b c OR d)", null, "m:a m:b (m:c m:d)");
		assertQueryEquals("m:(a b c AND d)", null, "m:a m:b (+m:c +m:d)");
		assertQueryEquals("m:(a b c OR d NOT e)", null, "m:a m:b (m:c (+m:d -m:e))");
		assertQueryEquals("m:(a b NEAR c)", null, "m:a spanNear([m:b, m:c], 5, true)");
		assertQueryEquals("m:(a b NEAR c d AND e)", null, "m:a spanNear([m:b, m:c], 5, true) (+m:d +m:e)");
		assertQueryEquals("-m:(a b NEAR c d AND e)", null, "m:a spanNear([m:b, m:c], 5, true) (+m:d +m:e)"); //? should we allow - at the beginning?
		
		assertQueryEquals("author:(huchra)", null, "author:huchra");
		assertQueryEquals("author:(huchra, j)", null, "");
		assertQueryEquals("author:(kurtz; -eichhorn, g)", null, "");
		assertQueryEquals("author:(kurtz~2; -echhorn)^2 OR ^accomazzi, a", null, "");
		assertQueryEquals("author:(muench-nashrallah)", null, "");
		assertQueryEquals("\"dark matter\" OR (dark matter -LHC)", null, "");
		
		
		assertQueryEquals("this999", null, "this999");
		assertQueryEquals("this0.9", null, "this0.9");
		
		assertQueryEquals("\"this\"", null, "\"this\"", PhraseQuery.class);
		assertQueryEquals("\"this  \"", null, "\"this  \"");
		assertQueryEquals("\"this  \"   ", null, "\"this  \"   ");
		assertQueryEquals("\"  this  \"", null, "\"  this  \"");
		
		assertQueryEquals("\"a \\\"b c\\\" d\"", null, "\"a \\\"b c\\\" d\"", PhraseQuery.class);
		assertQueryEquals("\"a \\\"b c\\\" d\"", null, "\"a \\\"b c\\\" d\"", PhraseQuery.class);
		assertQueryEquals("\"a \\+b c d\"", null, "\"a \\+b c d\"");
		
		
		assertQueryEquals("\"+() AND that\"", null, "\"+() AND that\"");
		assertQueryEquals("\"func(*) AND that\"", null, "\"func(*) AND that\"");
		assertQueryEquals("\"+() AND that\"", null, "\"+() AND that\"");
		assertQueryEquals("\"func(*) AND that\"", null, "\"func(*) AND that\"");
		
		
		
		
		assertQueryEquals("What , happens,with, commas ,,", null, "");
		
		assertQueryEquals("CO2+", null, "co2+", TermQuery.class);
		

	}
}
