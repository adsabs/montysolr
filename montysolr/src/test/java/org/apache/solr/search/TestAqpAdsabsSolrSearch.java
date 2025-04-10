package org.apache.solr.search;

import monty.solr.util.MontySolrQueryTestCase;
import monty.solr.util.MontySolrSetup;
import monty.solr.util.SolrTestSetup;
import org.apache.lucene.queries.function.FunctionScoreQuery;
import org.apache.lucene.queries.mlt.MoreLikeThisQuery;
import org.apache.lucene.queryparser.flexible.aqp.TestAqpAdsabs;
import org.apache.lucene.search.*;
import org.apache.lucene.queries.spans.SpanNearQuery;
import org.apache.lucene.queries.spans.SpanPositionRangeQuery;
import org.apache.lucene.util.BitSet;
import org.apache.lucene.util.FixedBitSet;
import org.apache.solr.common.util.ContentStream;
import org.apache.solr.common.util.ContentStreamBase;
import org.apache.solr.common.util.NamedList;
import org.apache.solr.request.SolrQueryRequestBase;
import org.apache.solr.util.RefCounted;
import org.junit.BeforeClass;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * This unittest is for queries that require solr core
 *
 * @author rchyla
 * <p>
 * XXX: I was uneasy about the family of these tests because they depend
 * on the settings from the other project (contrib/examples) - on the
 * other hand, I don't want to duplicate the code/config files. So, for
 * now I resigned, and I think of contrib/examples as a dependency for
 * adsabs
 * <p>
 * contrib/examples should contain only a code for the live site
 * (setup), but we are developing components for it here and we'll test
 * it here (inside contrib/adsabs)
 * <p>
 * Let's do it pragmatically (not as code puritans)
 * @see TestAqpAdsabs for the other tests
 */
public class TestAqpAdsabsSolrSearch extends MontySolrQueryTestCase {

    @BeforeClass
    public static void beforeClass() throws Exception {
        schemaString = getSchemaFile();

        configString = "solrconfig.xml";

        SolrTestSetup.initCore(configString, schemaString);
    }

    public static String getSchemaFile() {

        /*
         * For purposes of the test, we make a copy of the schema.xml, and create our
         * own synonym files
         */

        String configFile = null;
        try {
            configFile = SolrTestSetup
                    .getRepoUrl(Paths.get("deploy/adsabs/server/solr/collection1/conf/schema.xml"))
                    .getFile();
        } catch (Exception ex) {
            throw new IllegalStateException(ex);
        }

        File newConfig;
        try {

            newConfig = duplicateFile(new File(configFile));

            File multiSynonymsFile = createTempFile("hubble\0space\0telescope, HST", "r\0s\0t, RST",
                    "dark\0energy, DE", "very\0large\0array, VLA");
            replaceInFile(newConfig, "synonyms=\"ads_text_multi.synonyms\"",
                    "synonyms=\"" + multiSynonymsFile.getAbsolutePath() + "\"");

            File synonymsFile = createTempFile("weak => lightweak", "lensing => mikrolinseneffekt",
                    "pink => pinkish", "stephen, stephens => stephen", "bremßtrahlung => brehmen",
                    "protostars, protostellar, protostar, protosterne, circumprotostellar, protoestrellas, protosellar, prototstars, photostellar, preprotostars, protoetoile, protostellarlike, protostarlike => protostars");
            replaceInFile(newConfig, "synonyms=\"ads_text_simple.synonyms\"",
                    "synonyms=\"" + synonymsFile.getAbsolutePath() + "\"");

            // hand-curated synonyms
            File curatedSynonyms = createTempFile("JONES, CHRISTINE;FORMAN, CHRISTINE" // the famous
                    // post-synonym
                    // expansion
            );
            replaceInFile(newConfig, "synonyms=\"author_curated.synonyms\"",
                    "synonyms=\"" + curatedSynonyms.getAbsolutePath() + "\"");

        } catch (IOException e) {
            e.printStackTrace();
            throw new IllegalStateException(e.getMessage());
        }

        return newConfig.getAbsolutePath();
    }

    @Override
    public void tearDown() throws Exception {
        assertU(delQ("*:*"));
        assertU(commit("waitSearcher", "true"));
        super.tearDown();
    }

    public void testUnfieldedSearch() throws Exception {

        // NEAR on unfielded search -- will generate error when results have mixed
        // fields
        assertQueryParseException(
                req("defType", "aqp", "q", "foo NEAR2 bar", "qf", "bibcode^5 title^10", "aqp.unfielded.tokens.strategy",
                        "disjuncts", "aqp.unfielded.tokens.new.type", "simple", "aqp.constant_scoring", "bibcode^6"));

        assertQueryParseException(req("defType", "aqp", "q", "foo NEAR2 bar NEAR2 title:baz", "qf",
                "bibcode^5 title^10", "aqp.unfielded.tokens.strategy", "disjuncts", "aqp.unfielded.tokens.new.type",
                "simple", "aqp.constant_scoring", "bibcode^6"));

        // when we generate the phrase search, ignore acronyms
        assertQueryEquals(req("defType", "aqp", "q", "FOO BAR BAZ", "aqp.unfielded.tokens.strategy", "disjuncts",
                        "aqp.unfielded.tokens.new.type", "simple", "aqp.unfielded.max.uppercase.tokens", "2", "qf", "title"),
                "(((title:foo) (title:bar) (title:baz)) | title:\"foo bar baz\")", DisjunctionMaxQuery.class);

        // have constant scoring work even for unfielded searches
        assertQueryEquals(
                req("defType", "aqp", "q", "foo bar", "qf", "bibcode^5 title^10", "aqp.constant_scoring", "bibcode^6"),
                "+(((ConstantScore(bibcode:foo))^6.0)^5.0 | (title:foo)^10.0) +(((ConstantScore(bibcode:bar))^6.0)^5.0 | (title:bar)^10.0)",
                BooleanQuery.class);
        assertQueryEquals(
                req("defType", "aqp", "q", "foo bar", "qf", "bibcode^1 title^10", "aqp.constant_scoring", "bibcode^6"),
                "+((ConstantScore(bibcode:foo))^6.0 | (title:foo)^10.0) +((ConstantScore(bibcode:bar))^6.0 | (title:bar)^10.0)",
                BooleanQuery.class);
        assertQueryEquals(req("defType", "aqp", "q", "foo bar", "qf", "bibcode^5 title^10", "aqp.constant_scoring", ""),
                "+((bibcode:foo)^5.0 | (title:foo)^10.0) +((bibcode:bar)^5.0 | (title:bar)^10.0)", BooleanQuery.class);

        /*
         * Unfielded search should be expanded automatically by edismax
         *
         * However, edismax is not smart enough to deal properly with boolean clauses
         * and default operators, so I have decided to use the edismax on the "value"
         * level only. First, we parse the query, then we pass it to the 'adismax' query
         * parser (a modified edismax) to expand it; adismax will use aqp to build the
         * individual queries - so it is best of both worlds
         *
         */
        // first the individual elements explicitly (notice edismax differs from
        // adismax)
        assertQueryEquals(req("defType", "aqp", "q", "adismax(MÜLLER)", "qf", "author^2.3 title abstract^0.4"),
                "((Synonym(abstract:acr::muller abstract:acr::müller))^0.4 | ((author:müller, | author:müller,* | author:mueller, | author:mueller,* | author:muller, | author:muller,*))^2.3 | Synonym(title:acr::muller title:acr::müller))",
                DisjunctionMaxQuery.class);
        assertQueryEquals(req("defType", "aqp", "q", "edismax(MÜLLER)", "qf", "author^2.3 title abstract^0.4"),
                "(" + "(Synonym(abstract:acr::muller abstract:acr::müller))^0.4 | "
                        + "Synonym(title:acr::muller title:acr::müller) | "
                        + "(Synonym(author:mueller, author:muller, author:müller,))^2.3" + ")",
                DisjunctionMaxQuery.class);

        // unfielded search should handle authors like adismax (with expansions)
        assertQueryEquals(req("defType", "aqp", "q", "MÜLLER", "qf", "author^2.3 title abstract^0.4"),
                "((Synonym(abstract:acr::muller abstract:acr::müller))^0.4 | ((author:müller, | author:müller,* | author:mueller, | author:mueller,* | author:muller, | author:muller,*))^2.3 | Synonym(title:acr::muller title:acr::müller))",
                DisjunctionMaxQuery.class);

        assertQueryEquals(req("defType", "aqp", "q", "\"forman, c\"", "qf", "author^2.3 title abstract^0.4"),
                "((abstract:\"forman c\")^0.4 | ((author:forman, c | author:forman, christine | author:jones, c | author:jones, christine | author:forman, c* | author:forman,))^2.3 | title:\"forman c\")",
                DisjunctionMaxQuery.class);

        // now add a normal element
        assertQueryEquals(req("defType", "aqp", "q", "title:foo or MÜLLER", "qf", "author^2.3 title abstract^0.4"),
                "title:foo ((Synonym(abstract:acr::muller abstract:acr::müller))^0.4 | ((author:müller, | author:müller,* | author:mueller, | author:mueller,* | author:muller, | author:muller,*))^2.3 | Synonym(title:acr::muller title:acr::müller))",
                BooleanQuery.class);
        assertQueryEquals(
                req("defType", "aqp", "q", "title:foo or \"forman, c\"", "qf", "author^2.3 title abstract^0.4"),
                "title:foo ((abstract:\"forman c\")^0.4 | ((author:forman, c | author:forman, christine | author:jones, c | author:jones, christine | author:forman, c* | author:forman,))^2.3 | title:\"forman c\")",
                BooleanQuery.class);

        // this should not call edismax (because qf is missing)
        assertQueryEquals(req("defType", "aqp", "q", "accomazzi", "df", "author"),
                "author:accomazzi, author:accomazzi,*", BooleanQuery.class);

        /*
         * Now various cases of multi-token unfielded searches (incl multi-token
         * synonyms) and full author parse
         */

        // this is default behaviour, if you see 'all:' it means edismax didn't parse it
        assertQueryEquals(req("defType", "aqp", "q", "author:accomazzi, alberto property:refereed apj"),
                "+(author:accomazzi, author:accomazzi,*) +all:alberto +property:refereed +all:apj", BooleanQuery.class);
        assertQueryEquals(req("defType", "aqp", "q", "author:huchra supernova"),
                "+(author:huchra, author:huchra,*) +all:supernova", BooleanQuery.class);

        // smarter handling of missing parentheses/brackets with the special strategy
        // i expect following:
        // edismax receives: 'author:accomazzi, alberto' and also 'author:"accomazzi,
        // alberto"
        // "" : 'property:refereed r s t' and 'property:"refereed r s t"'
        assertQueryEquals(
                req("defType", "aqp", "aqp.unfielded.tokens.strategy", "multiply", "aqp.unfielded.tokens.new.type",
                        "simple", "qf", "title keyword", "q", "author:accomazzi, alberto property:refereed r s t"),
                "+((((author:accomazzi, author:accomazzi,*)) (keyword:alberto | title:alberto)) (author:accomazzi, alberto | author:accomazzi, alberto * | author:accomazzi, a | author:accomazzi, a * | author:accomazzi,)) +((property:refereed (keyword:r | title:r) (keyword:s | title:s) (keyword:t | title:t)) property:refereedrst)",
                BooleanQuery.class);
        // the same as above + enhanced by multisynonym
        // i expect to see syn::r s t, acr::rst
        // only active with the special parameter ...workaround=true
        assertQueryEquals(
                req("defType", "aqp", "aqp.unfielded.tokens.strategy", "multiply", "aqp.unfielded.tokens.new.type",
                        "simple", "aqp.unfielded.phrase.edismax.synonym.workaround", "true", "q",
                        "author:accomazzi, alberto property:refereed r s t", "qf", "title keyword^0.5"),
                "+((((author:accomazzi, author:accomazzi,*)) ((keyword:alberto)^0.5 | title:alberto)) (author:accomazzi, alberto | author:accomazzi, alberto * | author:accomazzi, a | author:accomazzi, a * | author:accomazzi,)) +((property:refereed ((keyword:r)^0.5 | title:r) ((keyword:s)^0.5 | title:s) ((keyword:t)^0.5 | title:t)) ((title:syn::r s t)^1.0 (title:syn::rst)^1.0 (title:acr::rst)^1.0 (keyword:syn::r s t)^0.45 (keyword:syn::rst)^0.45 (keyword:acr::rst)^0.45 property:refereedrst))",
                BooleanQuery.class);

        // +((+((author:accomazzi, author:accomazzi,*)) +((keyword:alberto)^0.5 |
        // title:alberto)) (((author:accomazzi, alberto author:accomazzi, alberto *
        // author:accomazzi, a author:accomazzi, a * author:accomazzi,))~1))
        // +((+property:refereed +((keyword:r)^0.5 | title:r) +((keyword:s)^0.5 |
        // title:s) +((keyword:t)^0.5 | title:t)) ((title:syn::r s t)^1.0
        // (title:acr::rst)^1.0 (keyword:syn::r s t)^0.45 (keyword:acr::rst)^0.45
        // property:refereedrst))

        // #238 - single synonyms were caught by the multi-synonym component
        // also note:
        // the 'qf' is not set, but still edismax is responsible for parsing this query
        // and since edismax is using default OR (that is hardcoded!), we cannot change
        // that, we would have to parse the query ourselves; but after a long discussion
        // it was decided that we'll use OR for the unfielded searches, so it should be
        // OK - if not, I have to rewrite edismax parsing logic myself
        // 22/10/13 - I've introduced a new strategy that emits both the
        // original query string and the phrase query, this is a workaround
        // for edismax
        // 30/09/16 - edismax is using default AND
        assertQueryEquals(
                req("defType", "aqp", "aqp.unfielded.tokens.strategy", "multiply", "aqp.unfielded.tokens.new.type",
                        "simple", "q", "pink elephant"),
                "((Synonym(all:pink all:syn::pinkish)) (all:elephant)) all:\"(pink syn::pinkish) elephant\"",
                BooleanQuery.class);

        assertQueryEquals(
                req("defType", "aqp", "q", "pink elephant", "aqp.unfielded.tokens.strategy", "multiply",
                        "aqp.unfielded.tokens.new.type", "simple", "qf", "title keyword"),
                "((Synonym(keyword:pink keyword:syn::pinkish) | Synonym(title:pink title:syn::pinkish)) (keyword:elephant | title:elephant)) (keyword:\"(pink syn::pinkish) elephant\" | title:\"(pink syn::pinkish) elephant\")",
                BooleanQuery.class);

        // if we make q.op to be the default AND (6.x behaviour)
        assertQueryEquals(
                req("defType", "aqp", "q", "pink elephant", "aqp.unfielded.tokens.strategy", "multiply",
                        "aqp.unfielded.tokens.new.type", "simple", "qf", "title keyword", "q.op", "AND"),
                "(+(Synonym(keyword:pink keyword:syn::pinkish) | Synonym(title:pink title:syn::pinkish)) +(keyword:elephant | title:elephant)) (+(keyword:\"(pink syn::pinkish) elephant\" | title:\"(pink syn::pinkish) elephant\"))",
                BooleanQuery.class);

        // when combined, the ADS's default AND operator should be visible +foo
        assertQueryEquals(
                req("defType", "aqp", "q", "pink elephant title:foo", "aqp.unfielded.tokens.strategy", "multiply",
                        "aqp.unfielded.tokens.new.type", "simple", "qf", "title keyword", "q.op", "AND"),
                "+((+(Synonym(keyword:pink keyword:syn::pinkish) | Synonym(title:pink title:syn::pinkish)) +(keyword:elephant | title:elephant)) (+(keyword:\"(pink syn::pinkish) elephant\" | title:\"(pink syn::pinkish) elephant\"))) +title:foo",
                BooleanQuery.class);

        assertQueryEquals(
                req("defType", "aqp", "q", "pink elephant title:foo", "aqp.unfielded.tokens.strategy", "multiply",
                        "aqp.unfielded.tokens.new.type", "simple", "qf", "title keyword"),
                "+(((Synonym(keyword:pink keyword:syn::pinkish) | Synonym(title:pink title:syn::pinkish)) (keyword:elephant | title:elephant)) (keyword:\"(pink syn::pinkish) elephant\" | title:\"(pink syn::pinkish) elephant\")) +title:foo",
                BooleanQuery.class);

        // multi-token combined with single token
        // the unfielded search should be exapnded with the phrase "x r s t"
        // and "r s t" should be properly analyzed into: "x rst" OR "x r s t"
        assertQueryEquals(
                req("defType", "aqp", "q", "r s t", "aqp.unfielded.tokens.strategy", "disjuncts",
                        "aqp.unfielded.tokens.new.type", "simple", "aqp.unfielded.phrase.edismax.synonym.workaround",
                        "false", "qf", "title^0.9 keyword^0.7"),
                "((((keyword:r)^0.7 | (title:r)^0.9) ((keyword:s)^0.7 | (title:s)^0.9) ((keyword:t)^0.7 | (title:t)^0.9)) | (((keyword:\"r s t\" | Synonym(keyword:acr::rst keyword:syn::r s t keyword:syn::rst)))^0.7 | ((title:\"r s t\" | Synonym(title:acr::rst title:syn::r s t title:syn::rst)))^0.9))",
                DisjunctionMaxQuery.class);
        assertQueryEquals(
                req("defType", "aqp", "q", "r s t", "aqp.unfielded.tokens.strategy", "disjuncts",
                        "aqp.unfielded.tokens.tiebreaker", "0.5", "aqp.unfielded.tokens.new.type", "simple",
                        "aqp.unfielded.phrase.edismax.synonym.workaround", "false", "qf", "title^0.9 keyword^0.7"),
                "((((keyword:r)^0.7 | (title:r)^0.9) ((keyword:s)^0.7 | (title:s)^0.9) ((keyword:t)^0.7 | (title:t)^0.9)) | (((keyword:\"r s t\" | Synonym(keyword:acr::rst keyword:syn::r s t keyword:syn::rst)))^0.7 | ((title:\"r s t\" | Synonym(title:acr::rst title:syn::r s t title:syn::rst)))^0.9))~0.5",
                DisjunctionMaxQuery.class);

        assertQueryEquals(
                req("defType", "aqp", "q", "r s t", "aqp.unfielded.tokens.strategy", "multiply",
                        "aqp.unfielded.tokens.new.type", "simple", "aqp.unfielded.phrase.edismax.synonym.workaround",
                        "false", "qf", "title^0.9 keyword^0.7"),
                "(((keyword:r)^0.7 | (title:r)^0.9) ((keyword:s)^0.7 | (title:s)^0.9) ((keyword:t)^0.7 | (title:t)^0.9)) (((keyword:\"r s t\" | Synonym(keyword:acr::rst keyword:syn::r s t keyword:syn::rst)))^0.7 | ((title:\"r s t\" | Synonym(title:acr::rst title:syn::r s t title:syn::rst)))^0.9)",
                BooleanQuery.class);

        assertQueryEquals(
                req("defType", "aqp", "q", "x r s t y", "aqp.unfielded.tokens.strategy", "multiply",
                        "aqp.unfielded.tokens.new.type", "simple", "aqp.unfielded.phrase.edismax.synonym.workaround",
                        "false", "qf", "title^0.9 keyword_norm^0.7"),
                "(((keyword_norm:x)^0.7 | (title:x)^0.9) ((keyword_norm:r)^0.7 | (title:r)^0.9) ((keyword_norm:s)^0.7 | (title:s)^0.9) ((keyword_norm:t)^0.7 | (title:t)^0.9) ((keyword_norm:y)^0.7 | (title:y)^0.9)) ((keyword_norm:\"x r s t y\")^0.7 | ((title:\"x r s t y\" | title:\"x (syn::r s t syn::rst acr::rst) ? ? y\"~3))^0.9)",
                BooleanQuery.class);

        // author search, unfielded (which looks as one token) - it looks like that to
        // adismax, but aqp will see two tokens...
        // the result is crazy because of recursive parsing
        // 1: accomazzi,alberto
        // 2: author:accomazzi,alberto -> author:accomazzi AND adismax(alberto)
        // 3: adismax(alberto) -> title:alberto OR author:alberto^2.3
        assertQueryEquals(
                req("defType", "aqp", "q", "accomazzi,alberto", "qf", "author^2.3 title",
                        "aqp.unfielded.tokens.strategy", "multiply", "aqp.unfielded.tokens.new.type", "simple"),
                "(((+(author:accomazzi, author:accomazzi,*) +(((author:alberto, author:alberto,*))^2.3 | title:alberto)))^2.3 | ((+title:accomazzi +(((author:alberto, author:alberto,*))^2.3 | title:alberto)))) (((author:accomazzi, alberto | author:accomazzi, alberto * | author:accomazzi, a | author:accomazzi, a * | author:accomazzi,))^2.3 | (title:accomazzialberto | title:\"accomazzi alberto\"))",
                BooleanQuery.class);

        // see what happens during normal parsing
        // author search, unfielded (which looks as one token)
        assertQueryEquals(req("defType", "aqp", "q", "author:accomazzi,alberto"),
                "+(author:accomazzi, author:accomazzi,*) +all:alberto", BooleanQuery.class);

        assertQueryEquals(
                req("defType", "aqp", "q", "author:accomazzi,alberto", "qf", "author^2.3 title",
                        "aqp.unfielded.tokens.strategy", "multiply", "aqp.unfielded.tokens.new.type", "simple"),
                "((+(author:accomazzi, author:accomazzi,*) +(((author:alberto, author:alberto,*))^2.3 | title:alberto))) (author:accomazzi, alberto | author:accomazzi, alberto * | author:accomazzi, a | author:accomazzi, a * | author:accomazzi,)",
                BooleanQuery.class);

    }

    public void testFirstAuthorRemapping() throws Exception {
        // Simple case
        assertQueryEquals(
                req("defType", "aqp", "aqp.constant_scoring", "author^1", "aqp.classic_scoring.modifier", "0.6", "q",
                        "=author:\"^foo\""),
                "FunctionScoreQuery(first_author:foo, scored by boost(sum(float(cite_read_boost),const(0.6))))",
                FunctionScoreQuery.class);

        // Complex case
        assertQueryEquals(
                req("defType", "aqp", "aqp.constant_scoring", "author^1", "aqp.classic_scoring.modifier", "0.6", "q",
                        "=author:(^accomazzi kurtz)"),
                "FunctionScoreQuery(+mask(spanOr([author:accomazzi,, SpanMultiTermQueryWrapper(author:accomazzi,*)])) as first_author +ConstantScore(author:kurtz,), scored by boost(sum(float(cite_read_boost),const(0.6))))",
                FunctionScoreQuery.class);

        assertQueryEquals(
                req("defType", "aqp", "aqp.constant_scoring", "author^1", "aqp.classic_scoring.modifier", "0.6", "q",
                        "=author:((^accomazzi AND kurtz) OR (^accomazzi AND lockhart))"),
                "FunctionScoreQuery((+mask(spanOr([author:accomazzi,, SpanMultiTermQueryWrapper(author:accomazzi,*)])) as first_author +ConstantScore(author:kurtz,)) (+mask(spanOr([author:accomazzi,, SpanMultiTermQueryWrapper(author:accomazzi,*)])) as first_author +ConstantScore(author:lockhart,)), scored by boost(sum(float(cite_read_boost),const(0.6))))",
                FunctionScoreQuery.class);
        // first_
    }

    public void testSpecialCases() throws Exception {

        assertU(adoc("id", "61", "bibcode", "b61", "title",
                "A Change of Rotation Profile in the Envelope in the HH 111 Protostellar System: A Transition to a Disk?"));

        assertU(adoc("id", "2", "bibcode", "XXX", "abstract", "foo bar baz", "title", "title bitle"));

        assertU(commit("waitSearcher", "true"));

        assertQ(req("q",
                        "title:\"A Change of Rotation Profile in the Envelope in the HH 111 Protostellar System: A Transition to a Disk\""),
                "//*[@numFound='1']", "//doc/str[@name='id'][.='61']");

        // check NEAR ignores order
        assertQ(req("q", "title:(rotation NEAR2 profile)"), "//*[@numFound='1']", "//doc/str[@name='id'][.='61']");
        assertQ(req("q", "title:(profile NEAR2 rotation)"), "//*[@numFound='1']", "//doc/str[@name='id'][.='61']");

        assertQEx("INVALID_SYNTAX", req("q", "author:\"^\"de marco year:2015"), 400);

        assertQ(req("q", "similar(foo bar baz title bitle, input abstract title, 100, 100, 1, 1)"),
                "//*[@numFound='1']", "//doc/str[@name='id'][.='2']");

        // similar()
        assertQueryEquals(req("defType", "aqp", "q", "similar(foo bar baz, input)"), "like:foo bar baz",
                MoreLikeThisQuery.class);
        // default docfreq=2, termfreq=2
        assertQ(req("q", "similar(foo bar baz, input abstract, 100, 100, 2, 2)"), "//*[@numFound='0']");
        // change defaults
        assertQ(req("q", "similar(foo bar baz, input abstract, 100, 100, 1, 1)"), "//*[@numFound='1']",
                "//doc/str[@name='id'][.='2']");

        assertQueryEquals(req("defType", "aqp", "q", "similar(recid:2, title)"), "+like:title bitle  -BitSetQuery(1)",
                BooleanQuery.class);

        // topn() with score sorting
        // TODO: solve it differently https://github.com/romanchyla/montysolr/issues/185
        assertQueryEquals(req("defType", "aqp", "q", "topn(2, title:foo, score desc)"),
                "FunctionScoreQuery(SecondOrderQuery(title:foo, collector=SecondOrderCollectorTopN(2)), scored by boost(sum(float(cite_read_boost),const(0.5))))",
                FunctionScoreQuery.class);
        assertQueryEquals(req("defType", "aqp", "q", "topn(2, title:foo, \"score desc,bibcode asc\")"),
                "FunctionScoreQuery(SecondOrderQuery(title:foo, collector=SecondOrderCollectorTopN(2, info=score desc,bibcode asc)), scored by boost(sum(float(cite_read_boost),const(0.5))))",
                FunctionScoreQuery.class);

        // custom scoring should be possible even with constant scores
        assertQueryEquals(
                req("defType", "aqp", "aqp.constant_scoring", "author^1", "aqp.classic_scoring.modifier", "0.6", "q",
                        "=author:\"foo\""),
                "FunctionScoreQuery(ConstantScore(author:foo,), scored by boost(sum(float(cite_read_boost),const(0.6))))",
                FunctionScoreQuery.class);

        assertQueryEquals(
                req("defType", "aqp", "aqp.constant_scoring", "author^1", "aqp.classic_scoring.modifier", "0.6", "q",
                        "=author:\"^foo\""),
                "FunctionScoreQuery(ConstantScore(spanPosRange(author:foo,, 0, 1)), scored by boost(sum(float(cite_read_boost),const(0.6))))",
                FunctionScoreQuery.class);

        assertQueryEquals(req("defType", "aqp", "q", "similar(bibcode:XX)"), "MatchNoDocsQuery(\"\")",
                MatchNoDocsQuery.class);

        assertQueryEquals(req("defType", "aqp", "q", "similar(bibcode:XXX)"),
                "+like:foo bar baz title bitle  -BitSetQuery(1)", BooleanQuery.class);

        assertQueryEquals(req("defType", "aqp", "q", "similar(bibcode:XXX, title)"),
                "+like:title bitle  -BitSetQuery(1)", BooleanQuery.class);

        assertQueryEquals(req("defType", "aqp", "q", "similar(bibcode:XXX, title abstract)"),
                "+like:foo bar baz title bitle  -BitSetQuery(1)", BooleanQuery.class);

        RefCounted<SolrIndexSearcher> ir = h.getCore().getSearcher();
        int md = ir.get().maxDoc();
        ir.decref();

        assertQueryEquals(req("defType", "aqp", "q", "similar(topn(200, abstract:foo), title abstract)"),
                "+like:foo bar baz title bitle  -BitSetQuery(" + (md - 1) + ")", BooleanQuery.class);
        assertQueryEquals(req("defType", "aqp", "q", "similar(topn(200, abstract:foo) , title abstract) foo"),
                "+(+like:foo bar baz title bitle  -BitSetQuery(" + (md - 1) + ")) +all:foo",
                BooleanQuery.class);

        // make sure the cache key of the query is different
        Query aq = assertQueryEquals(req("defType", "aqp", "q", "author:\"Accomazzi, A\" abs:\"ADS\" year:2000-2015"),
                "+(author:accomazzi, a | author:accomazzi, a* | author:accomazzi,) +(abstract:acr::ads title:acr::ads keyword:acr::ads) +year:[2000 TO 2015]",
                BooleanQuery.class);
        Query bq = assertQueryEquals(req("defType", "aqp", "q", "abs:\"ADS\" year:2000-2015"),
                "+(abstract:acr::ads title:acr::ads keyword:acr::ads) +year:[2000 TO 2015]", BooleanQuery.class);
        Query cq = assertQueryEquals(req("defType", "aqp", "q", "author:\"Accomazzi, A\" abs:\"ADS\" year:2000-2015"),
                "+(author:accomazzi, a | author:accomazzi, a* | author:accomazzi,) +(abstract:acr::ads title:acr::ads keyword:acr::ads) +year:[2000 TO 2015]",
                BooleanQuery.class);

        // System.out.println(aq.hashCode());
        // System.out.println(bq.hashCode());
        // System.out.println(cq.hashCode());

        assertNotSame(aq, bq);
        assertNotSame(aq, cq);

        assertTrue(aq.hashCode() != bq.hashCode());
        assertEquals(aq.hashCode(), cq.hashCode());

        // another method for constant scoring for fields (this time applied
        // universally; to
        // every query type used in a field)
        assertQueryEquals(req("defType", "aqp", "aqp.constant_scoring", "author^1", "q", "=author:\"foo\""),
                "ConstantScore(author:foo,)", ConstantScoreQuery.class);

        // https://github.com/romanchyla/montysolr/issues/101
        assertQueryEquals(req("defType", "aqp", "q", "=author:\"foo, bar\""), "author:foo, bar", TermQuery.class);
        assertQueryEquals(req("defType", "aqp", "q", "pos(=author:\"foo, bar\", 1)"),
                "spanPosRange(author:foo, bar, 0, 1)", SpanPositionRangeQuery.class);
        assertQueryEquals(req("defType", "aqp", "q", "=author:\"^foo, bar\""), "spanPosRange(author:foo, bar, 0, 1)",
                SpanPositionRangeQuery.class);

        // constant() score
        assertQueryEquals(req("defType", "aqp", "q", "constant(title:foo)"), "ConstantScore(title:foo)",
                ConstantScoreQuery.class);
        assertQueryEquals(req("defType", "aqp", "q", "constant(title:foo^2)"), "ConstantScore((title:foo)^2.0)",
                ConstantScoreQuery.class);

        // allow to set scoring method for a given field
        BooleanQuery q = (BooleanQuery) assertQueryEquals(
                req("defType", "aqp", "q", "author:riess", "aqp.qprefix.scoring.author", "boolean"),
                "author:riess, author:riess,*", BooleanQuery.class);
        assertEquals(((PrefixQuery) q.clauses().get(1).getQuery()).getRewriteMethod(),
                MultiTermQuery.SCORING_BOOLEAN_REWRITE);

        q = (BooleanQuery) assertQueryEquals(
                req("defType", "aqp", "q", "author:riess", "aqp.qprefix.scoring.author", "constant_boolean"),
                "author:riess, author:riess,*", BooleanQuery.class);
        assertEquals(((PrefixQuery) q.clauses().get(1).getQuery()).getRewriteMethod(),
                MultiTermQuery.CONSTANT_SCORE_BOOLEAN_REWRITE);

        q = (BooleanQuery) assertQueryEquals(
                req("defType", "aqp", "q", "author:riess", "aqp.qprefix.scoring.author", "constant"),
                "author:riess, author:riess,*", BooleanQuery.class);
        assertEquals(((PrefixQuery) q.clauses().get(1).getQuery()).getRewriteMethod(),
                MultiTermQuery.CONSTANT_SCORE_BLENDED_REWRITE);

        q = (BooleanQuery) assertQueryEquals(
                req("defType", "aqp", "q", "author:riess", "aqp.qprefix.scoring.author", "topterms"),
                "author:riess, author:riess,*", BooleanQuery.class);
        assertEquals(((PrefixQuery) q.clauses().get(1).getQuery()).getRewriteMethod().getClass(),
                MultiTermQuery.TopTermsScoringBooleanQueryRewrite.class);

        // verification for https://github.com/romanchyla/montysolr/issues/45
        // expansion of synonyms inside a virtual field together with nested boolean
        // query
        // VLBA doesn't have a synonym, VLA has multi-synonym; VLA must be the same
        assertQueryEquals(req("defType", "aqp", "q", "full:(VLBA OR Chandra)"),
                "(ack:acr::vlba (abstract:acr::vlba)^2.0 (title:acr::vlba)^2.0 body:acr::vlba keyword:acr::vlba) "
                        + "(ack:chandra (abstract:chandra)^2.0 (title:chandra)^2.0 body:chandra keyword:chandra)",
                BooleanQuery.class);

        assertQueryEquals(req("defType", "aqp", "q", "full:(\"Very Large Array\" OR \"Very Long Baseline Array\")"),
                "((ack:\"very large array\" | Synonym(ack:acr::vla ack:syn::very large array ack:syn::vla)) ((abstract:\"very large array\" | Synonym(abstract:acr::vla abstract:syn::very large array abstract:syn::vla)))^2.0 ((title:\"very large array\" | Synonym(title:acr::vla title:syn::very large array title:syn::vla)))^2.0 (body:\"very large array\" | Synonym(body:acr::vla body:syn::very large array body:syn::vla)) (keyword:\"very large array\" | Synonym(keyword:acr::vla keyword:syn::very large array keyword:syn::vla))) (ack:\"very long baseline array\" (abstract:\"very long baseline array\")^2.0 (title:\"very long baseline array\")^2.0 body:\"very long baseline array\" keyword:\"very long baseline array\")",
                BooleanQuery.class);

        assertQueryEquals(req("defType", "aqp", "q", "full:(VLA OR \"Very Long Baseline Array\")"),
                "(Synonym(ack:acr::vla ack:syn::very large array ack:syn::vla) "
                        + "(Synonym(abstract:acr::vla abstract:syn::very large array abstract:syn::vla))^2.0 "
                        + "(Synonym(title:acr::vla title:syn::very large array title:syn::vla))^2.0 "
                        + "Synonym(body:acr::vla body:syn::very large array body:syn::vla) "
                        + "Synonym(keyword:acr::vla keyword:syn::very large array keyword:syn::vla)) "
                        + "(ack:\"very long baseline array\" " + "(abstract:\"very long baseline array\")^2.0 "
                        + "(title:\"very long baseline array\")^2.0 " + "body:\"very long baseline array\" "
                        + "keyword:\"very long baseline array\")",
                BooleanQuery.class);

        assertQueryEquals(req("defType", "aqp", "q", "full:(HST OR Chandra)"),
                "(Synonym(ack:acr::hst ack:syn::hst ack:syn::hubble space telescope) "
                        + "(Synonym(abstract:acr::hst abstract:syn::hst abstract:syn::hubble space telescope))^2.0 "
                        + "(Synonym(title:acr::hst title:syn::hst title:syn::hubble space telescope))^2.0 "
                        + "Synonym(body:acr::hst body:syn::hst body:syn::hubble space telescope) "
                        + "Synonym(keyword:acr::hst keyword:syn::hst keyword:syn::hubble space telescope)) "
                        + "(ack:chandra (abstract:chandra)^2.0 (title:chandra)^2.0 body:chandra keyword:chandra)",
                BooleanQuery.class);

        assertQueryEquals(req("defType", "aqp", "q", "abs:(HST OR baz)"),
                "(Synonym(abstract:acr::hst abstract:syn::hst abstract:syn::hubble space telescope) "
                        + "Synonym(title:acr::hst title:syn::hst title:syn::hubble space telescope) "
                        + "Synonym(keyword:acr::hst keyword:syn::hst keyword:syn::hubble space telescope)) "
                        + "(abstract:baz title:baz keyword:baz)",
                BooleanQuery.class);

        // https://github.com/romanchyla/montysolr/issues/78
        // make proximity operators work with virtual fields
        assertQueryEquals(req("defType", "aqp", "q", " full:\"frew\" NEAR2 full:\"j\""),
                "spanNear([ack:frew, ack:j], 2, false) spanNear([(abstract:frew)^2.0, (abstract:j)^2.0], 2, false) spanNear([(title:frew)^2.0, (title:j)^2.0], 2, false) spanNear([body:frew, body:j], 2, false) spanNear([keyword:frew, keyword:j], 2, false)",
                BooleanQuery.class);

        assertQueryEquals(req("defType", "aqp", "q", " full:\"HST\" NEAR2 full:\"proposal\""),
                "spanNear([spanOr([ack:acr::hst, ack:syn::hst, ack:syn::hubble space telescope]), ack:proposal], 2, false) spanNear([(spanOr([abstract:acr::hst, abstract:syn::hst, abstract:syn::hubble space telescope]))^2.0, (abstract:proposal)^2.0], 2, false) spanNear([(spanOr([title:acr::hst, title:syn::hst, title:syn::hubble space telescope]))^2.0, (title:proposal)^2.0], 2, false) spanNear([spanOr([body:acr::hst, body:syn::hst, body:syn::hubble space telescope]), body:proposal], 2, false) spanNear([spanOr([keyword:acr::hst, keyword:syn::hst, keyword:syn::hubble space telescope]), keyword:proposal], 2, false)",
                BooleanQuery.class);

        // yeah, if you don't specify any field, then i'll refuse to serve you anything
        // useful!
        assertQueryEquals(req("defType", "aqp", "q", " HST NEAR2 galaxy"),
                "spanNear([spanOr([all:acr::hst, all:syn::hst, all:syn::hubble space telescope]), all:galaxy], 2, false)",
                SpanNearQuery.class);

        // fuzzy search for authors
        assertQueryEquals(req("defType", "aqp", "q", "author:kurtz~2"), "author:kurtz,~2", FuzzyQuery.class);

        // levenshtein automata only considers distances (and max is 2)
        assertQueryEquals(req("defType", "aqp", "q", "=author:\"Hoffmann, W.\"~3"), "author:hoffmann, w~2",
                FuzzyQuery.class);
        assertQueryEquals(req("defType", "aqp", "q", "=author:\"Hoffmann, W.\"~1"), "author:hoffmann, w~1",
                FuzzyQuery.class);

        assertQueryEquals(req("defType", "aqp", "q", "author:\"Hoffmann, W.\"~2"), "author:hoffmann, w~2",
                FuzzyQuery.class);

        // inconsistency disabling synonyms: #39
        assertQueryEquals(req("defType", "aqp", "q", "full:bremßtrahlung"),
                "Synonym(ack:bremsstrahlung ack:bremßtrahlung ack:syn::brehmen) "
                        + "(Synonym(abstract:bremsstrahlung abstract:bremßtrahlung abstract:syn::brehmen))^2.0 "
                        + "(Synonym(title:bremsstrahlung title:bremßtrahlung title:syn::brehmen))^2.0 "
                        + "Synonym(body:bremsstrahlung body:bremßtrahlung body:syn::brehmen) "
                        + "Synonym(keyword:bremsstrahlung keyword:bremßtrahlung keyword:syn::brehmen)",
                BooleanQuery.class);
        assertQueryEquals(req("defType", "aqp", "q", "=full:bremßtrahlung"),
                "Synonym(ack:bremsstrahlung ack:bremßtrahlung) "
                        + "(Synonym(abstract:bremsstrahlung abstract:bremßtrahlung))^2.0 "
                        + "(Synonym(title:bremsstrahlung title:bremßtrahlung))^2.0 "
                        + "Synonym(body:bremsstrahlung body:bremßtrahlung) "
                        + "Synonym(keyword:bremsstrahlung keyword:bremßtrahlung)",
                BooleanQuery.class);
        assertQueryEquals(req("defType", "aqp", "q", "body:bremßtrahlung"),
                "Synonym(body:bremsstrahlung body:bremßtrahlung body:syn::brehmen)", SynonymQuery.class);
        assertQueryEquals(req("defType", "aqp", "q", "=body:bremßtrahlung"),
                "Synonym(body:bremsstrahlung body:bremßtrahlung)", SynonymQuery.class);

        // disable synonyms (also for virtual fiels) - #36
        assertQueryEquals(req("defType", "aqp", "q", "abs:\"dark energy\""),
                "(abstract:\"dark energy\" | Synonym(abstract:acr::de abstract:syn::dark energy abstract:syn::de)) (title:\"dark energy\" | Synonym(title:acr::de title:syn::dark energy title:syn::de)) (keyword:\"dark energy\" | Synonym(keyword:acr::de keyword:syn::dark energy keyword:syn::de))",
                BooleanQuery.class);
        assertQueryEquals(req("defType", "aqp", "q", "=abs:\"dark energy\""),
                "abstract:\"dark energy\" title:\"dark energy\" keyword:\"dark energy\"", BooleanQuery.class);

        assertQueryEquals(req("defType", "aqp", "q", "=abs:(\"dark energy\")"),
                "abstract:\"dark energy\" title:\"dark energy\" keyword:\"dark energy\"", BooleanQuery.class);

        assertQueryEquals(req("defType", "aqp", "q", "abs:(weak)"),
                "Synonym(abstract:syn::lightweak abstract:weak) Synonym(title:syn::lightweak title:weak) Synonym(keyword:syn::lightweak keyword:weak)",
                BooleanQuery.class);
        assertQueryEquals(req("defType", "aqp", "q", "=abs:(weak)"), "abstract:weak title:weak keyword:weak",
                BooleanQuery.class);
        assertQueryEquals(req("defType", "aqp", "q", "abs:(=weak weak)"),
                "+(abstract:weak title:weak keyword:weak) +(Synonym(abstract:syn::lightweak abstract:weak) Synonym(title:syn::lightweak title:weak) Synonym(keyword:syn::lightweak keyword:weak))",
                BooleanQuery.class);

        // full - virtual field with wrong date
        assertQueryEquals(req("defType", "aqp", "q", "full:(\"15-52-15050\" OR \"15-32-21062\")"),
                "((ack:155215050 | ack:\"15 52 15050\") ((abstract:155215050 | abstract:\"15 52 15050\"))^2.0 ((title:155215050 | title:\"15 52 15050\"))^2.0 (body:155215050 | body:\"15 52 15050\") (keyword:155215050 | keyword:\"15 52 15050\")) ((ack:153221062 | ack:\"15 32 21062\") ((abstract:153221062 | abstract:\"15 32 21062\"))^2.0 ((title:153221062 | title:\"15 32 21062\"))^2.0 (body:153221062 | body:\"15 32 21062\") (keyword:153221062 | keyword:\"15 32 21062\"))",
                BooleanQuery.class);

        // nested functions should parse well: citations(author:"^kurtz")
        assertQueryEquals(req("defType", "aqp", "q", "citations(author:\"^kurtz\")"),
                "SecondOrderQuery(spanPosRange(spanOr([author:kurtz,, SpanMultiTermQueryWrapper(author:kurtz,*)]), 0, 1), collector=SecondOrderCollectorCitedBy(cache:citations-cache))",
                SecondOrderQuery.class);

        assertQueryEquals(req("defType", "aqp", "q", "citations(citations(author:\"^kurtz\"))"),
                "SecondOrderQuery(SecondOrderQuery(spanPosRange(spanOr([author:kurtz,, SpanMultiTermQueryWrapper(author:kurtz,*)]), 0, 1), collector=SecondOrderCollectorCitedBy(cache:citations-cache)), collector=SecondOrderCollectorCitedBy(cache:citations-cache))",
                SecondOrderQuery.class);

        // #30 - first_author and author:"^fooo" give diff results
        assertQueryEquals(req("defType", "aqp", "q", "first_author:\"kurtz, m j\""),
                "(first_author:kurtz, m j | first_author:kurtz, m j* | first_author:/kurtz, m[^ ]*/ | first_author:/kurtz, m[^ ]* j.*/ | first_author:kurtz, m | first_author:kurtz,)",
                DisjunctionMaxQuery.class);
        // TODO: Re-enable this after fixing SpanOr query string comparison
//        assertQueryEquals(req("defType", "aqp", "q", "author:\"^kurtz, m j\""),
//                "spanPosRange(spanOr([author:kurtz, m j, SpanMultiTermQueryWrapper(author:kurtz, m j*), SpanMultiTermQueryWrapper(author:/kurtz, m[^ ]*/), SpanMultiTermQueryWrapper(author:/kurtz, m[^ ]* j.*/), author:kurtz, m, author:kurtz,]), 0, 1)",
//                SpanPositionRangeQuery.class);

        // strange effect of paranthesis - github #23; we want to see this even (inside
        // brackets)
        // +(
        // (
        // (
        // DisjunctionMaxQuery((((author:stephen, author:stephen,*)) | ((title:stephen
        // title:syn::stephen))))
        // DisjunctionMaxQuery((((author:murray, author:murray, margaret a
        // author:murray, m a author:hanson, m m author:hanson, margaret m
        // author:murray,*)) | title:murray))
        // )~2
        // )
        // DisjunctionMaxQuery((
        // ((author:stephen murray, author:stephen murray,* author:murray, stephen
        // author:murray, stephen * author:murray, stephen * author:murray, s
        // author:murray, s * author:murray, s * author:murray, author:murray,*))
        // |title:\"(stephen syn::stephen) murray\"
        // ))
        // )
        // +author_facet_hier:0/Murray, S

        assertQueryEquals(
                req("defType", "aqp", "q", "stephen murray author_facet_hier:\"0/Murray, S\"", "qf", "abstract title",
                        "aqp.unfielded.tokens.strategy", "multiply", "aqp.unfielded.tokens.new.type", "simple",
                        "aqp.unfielded.tokens.function.name", "edismax_combined_aqp"),
                "+(((Synonym(abstract:stephen abstract:syn::stephen) | Synonym(title:stephen title:syn::stephen)) (abstract:murray | title:murray)) (abstract:\"(stephen syn::stephen) murray\" | title:\"(stephen syn::stephen) murray\")) +author_facet_hier:0/murray, s",
                BooleanQuery.class);

        assertQueryEquals(
                req("defType", "aqp", "q", "((stephen murray)) author_facet_hier:\"0/Murray, S\"", "qf",
                        "title abstract", "aqp.unfielded.tokens.strategy", "multiply", "aqp.unfielded.tokens.new.type",
                        "simple", "aqp.unfielded.tokens.function.name", "edismax_combined_aqp"),
                "+(((Synonym(abstract:stephen abstract:syn::stephen) | Synonym(title:stephen title:syn::stephen)) (abstract:murray | title:murray)) (abstract:\"(stephen syn::stephen) murray\" | title:\"(stephen syn::stephen) murray\")) +author_facet_hier:0/murray, s",
                BooleanQuery.class);
        assertQueryEquals(
                req("defType", "aqp", "q", "=(stephen murray) author_facet_hier:\"0/Murray, S\"", "qf",
                        "title abstract", "aqp.unfielded.tokens.strategy", "multiply", "aqp.unfielded.tokens.new.type",
                        "simple", "aqp.unfielded.tokens.function.name", "edismax_combined_aqp"),
                "+(+(abstract:stephen | title:stephen) +(abstract:murray | title:murray)) +author_facet_hier:0/murray, s",
                BooleanQuery.class);

        // virtual fields (their definition is in the solrconfig.xml)
        assertQueryEquals(req("defType", "aqp", "q", "full:foo"),
                "ack:foo (abstract:foo)^2.0 (title:foo)^2.0 body:foo keyword:foo", BooleanQuery.class);
        assertQueryEquals(req("defType", "aqp", "q", "full:\"foo phrase\""),
                "ack:\"foo phrase\" (abstract:\"foo phrase\")^2.0 (title:\"foo phrase\")^2.0 body:\"foo phrase\" keyword:\"foo phrase\"",
                BooleanQuery.class);
        assertQueryEquals(req("defType", "aqp", "q", "abs:foo"), "abstract:foo title:foo keyword:foo",
                BooleanQuery.class);

        // unbalanced brackets for functions
        assertQueryEquals(req("defType", "aqp", "q", "topn(201, ((\"foo bar\") AND database:astronomy), date asc)"),
                "FunctionScoreQuery(SecondOrderQuery(+all:\"foo bar\" +database:astronomy, collector=SecondOrderCollectorTopN(201, info=date asc)), scored by boost(sum(float(cite_read_boost),const(0.5))))",
                FunctionScoreQuery.class);
        assertQueryEquals(
                req("defType", "aqp", "q", "topn(201, ((\"foo bar\") AND database:astronomy),   date asc   )"),
                "FunctionScoreQuery(SecondOrderQuery(+all:\"foo bar\" +database:astronomy, collector=SecondOrderCollectorTopN(201, info=date asc)), scored by boost(sum(float(cite_read_boost),const(0.5))))",
                FunctionScoreQuery.class);
        assertQueryEquals(req("defType", "aqp", "q", "topn(201,(  ((\"foo bar\") AND database:astronomy)),date asc)"),
                "FunctionScoreQuery(SecondOrderQuery(+all:\"foo bar\" +database:astronomy, collector=SecondOrderCollectorTopN(201, info=date asc)), scored by boost(sum(float(cite_read_boost),const(0.5))))",
                FunctionScoreQuery.class);

        // added ability to interactively tweak queries
        assertQueryEquals(req("defType", "aqp", "q", "tweak(collector_final_value=ARITHM_MEAN, citations(author:foo))"),
                "SecondOrderQuery(author:foo, author:foo,*, collector=SecondOrderCollectorCitedBy(cache:citations-cache))",
                SecondOrderQuery.class);

        // # 389
        // make sure the functional parsing is handling things well
        assertQueryEquals(req("defType", "aqp", "q", "topn(200, ((title:foo OR topn(10, title:bar OR title:baz))))"),
                "FunctionScoreQuery(SecondOrderQuery(title:foo FunctionScoreQuery(SecondOrderQuery(title:bar title:baz, collector=SecondOrderCollectorTopN(10)), scored by boost(sum(float(cite_read_boost),const(0.5)))), collector=SecondOrderCollectorTopN(200)), scored by boost(sum(float(cite_read_boost),const(0.5))))",
                FunctionScoreQuery.class);
        assertQueryEquals(req("defType", "aqp", "q", "topn(200, ((title:foo AND topn(10, title:bar OR title:baz))))"),
                "FunctionScoreQuery(SecondOrderQuery(+title:foo +FunctionScoreQuery(SecondOrderQuery(title:bar title:baz, collector=SecondOrderCollectorTopN(10)), scored by boost(sum(float(cite_read_boost),const(0.5)))), collector=SecondOrderCollectorTopN(200)), scored by boost(sum(float(cite_read_boost),const(0.5))))",
                FunctionScoreQuery.class);
        assertQueryEquals(req("defType", "aqp", "q", "topn(200, title:foo, date desc)"),
                "FunctionScoreQuery(SecondOrderQuery(title:foo, collector=SecondOrderCollectorTopN(200, info=date desc)), scored by boost(sum(float(cite_read_boost),const(0.5))))",
                FunctionScoreQuery.class);
        assertQueryEquals(req("defType", "aqp", "q", "topn(200, (title:foo), date desc)"),
                "FunctionScoreQuery(SecondOrderQuery(title:foo, collector=SecondOrderCollectorTopN(200, info=date desc)), scored by boost(sum(float(cite_read_boost),const(0.5))))",
                FunctionScoreQuery.class);
        assertQueryEquals(req("defType", "aqp", "q", "topn(200, \"foo bar\", \"date desc\")"),
                "FunctionScoreQuery(SecondOrderQuery(all:\"foo bar\", collector=SecondOrderCollectorTopN(200, info=date desc)), scored by boost(sum(float(cite_read_boost),const(0.5))))",
                FunctionScoreQuery.class);

        // trendy() - what people read, it reads data from index
        assertU(addDocs("author", "muller", "reader", "bibcode1", "reader", "bibcode2"));
        assertU(addDocs("author", "muller", "reader", "bibcode2", "reader", "bibcode4"));
        assertU(addDocs("author", "muller", "reader", "bibcode5", "reader", "bibcode2"));
        assertU(commit());

        assertQueryEquals(req("defType", "aqp", "q", "trending(author:muller)"),
                "(like:bibcode1 bibcode2 bibcode2 bibcode4 bibcode5 bibcode2)^2.0", BoostQuery.class);

        // pos() operator
//        assertQueryEquals(req("defType", "aqp", "q", "pos(author:\"Accomazzi, A\", 1, 100)"),
//                "spanPosRange(spanOr([author:accomazzi, a, SpanMultiTermQueryWrapper(author:accomazzi, a*), author:accomazzi,]), 0, 100)",
//                SpanPositionRangeQuery.class);

        // notice the use of modifier '=' (if it is lowercased, it means _nosyn analyzer
        // was used)
        assertQueryEquals(req("defType", "aqp", "q", "pos(=author:\"Accomazzi, A\", 1)"),
                "spanPosRange(author:accomazzi, a, 0, 1)", SpanPositionRangeQuery.class);
//        assertQueryEquals(req("defType", "aqp", "q", "pos(+author:\"Accomazzi, A\", 1, 1)"),
//                "spanPosRange(spanOr([author:accomazzi, a, SpanMultiTermQueryWrapper(author:accomazzi, a*), author:accomazzi,]), 0, 1)",
//                SpanPositionRangeQuery.class);

        assertQueryParseException(req("defType", "aqp", "q", "pos(author:\"Accomazzi, A\", 1, 1, 1)"));
        assertQueryParseException(req("defType", "aqp", "q", "pos(author:\"Accomazzi, A\")"));
        assertQueryParseException(req("defType", "aqp", "q", "^two$"));
        assertQueryParseException(req("defType", "aqp", "q", "two$"));
        assertQueryParseException(req("defType", "aqp", "q", "\"two phrase$\""));

        // old positional search
        // TODO: check for the generated warnings

//        assertQueryEquals(req("defType", "aqp", "q", "^two"),
//                "spanPosRange(spanOr([author:two,, SpanMultiTermQueryWrapper(author:two,*)]), 0, 1)",
//                SpanPositionRangeQuery.class);
//        assertQueryEquals(req("defType", "aqp", "q", "one ^two, j k"),
//                "+all:one +spanPosRange(spanOr([author:two, j k, SpanMultiTermQueryWrapper(author:two, j k*), SpanMultiTermQueryWrapper(author:/two, j[^ ]*/), SpanMultiTermQueryWrapper(author:/two, j[^ ]* k.*/), author:two, j, author:two,]), 0, 1)",
//                BooleanQuery.class);
//        assertQueryEquals(req("defType", "aqp", "q", "one \"^phrase, author\"", "qf", "title author"),
//                "+(((author:one, author:one,*)) | title:one) +spanPosRange(spanOr([author:phrase, author, SpanMultiTermQueryWrapper(author:phrase, author *), author:phrase, a, SpanMultiTermQueryWrapper(author:phrase, a *), author:phrase,]), 0, 1)",
//                BooleanQuery.class);

        // author expansion can generate regexes, so we should deal with them (actually
        // we ignore them)
//        assertQueryEquals(req("defType", "aqp", "q", "pos(author:\"Accomazzi, A. K. B.\", 1)"),
//                "spanPosRange(spanOr([author:accomazzi, a k b, SpanMultiTermQueryWrapper(author:accomazzi, a k b*), SpanMultiTermQueryWrapper(author:/accomazzi, a[^ ]*/), SpanMultiTermQueryWrapper(author:/accomazzi, a[^ ]* k[^ ]*/), SpanMultiTermQueryWrapper(author:/accomazzi, a[^ ]* k[^ ]* b.*/), author:accomazzi, a, author:accomazzi,]), 0, 1)",
//                SpanPositionRangeQuery.class);

        // #322 - trailing comma
        assertQueryEquals(req("defType", "aqp", "q", "author:\"^roberts\", author:\"ables\""),
                "+spanPosRange(spanOr([author:roberts,, SpanMultiTermQueryWrapper(author:roberts,*)]), 0, 1) +(author:ables, author:ables,*)",
                BooleanQuery.class);


        // topn sorted - added 15Aug2013
        assertQueryEquals(req("defType", "aqp", "q", "topn(5, *:*, date desc)"),
                "FunctionScoreQuery(SecondOrderQuery(*:*, collector=SecondOrderCollectorTopN(5, info=date desc)), scored by boost(sum(float(cite_read_boost),const(0.5))))",
                FunctionScoreQuery.class);
        assertQueryEquals(req("defType", "aqp", "q", "topn(5, author:civano, \"date desc\")"),
                "FunctionScoreQuery(SecondOrderQuery(author:civano, author:civano,*, collector=SecondOrderCollectorTopN(5, info=date desc)), scored by boost(sum(float(cite_read_boost),const(0.5))))",
                FunctionScoreQuery.class);
        assertQueryEquals(req("defType", "aqp", "q", "topn(5, author:civano, \"date desc,citation_count desc\")"),
                "FunctionScoreQuery(SecondOrderQuery(author:civano, author:civano,*, collector=SecondOrderCollectorTopN(5, info=date desc,citation_count desc)), scored by boost(sum(float(cite_read_boost),const(0.5))))",
                FunctionScoreQuery.class);

        // topN - added Aug2013
        assertQueryEquals(req("defType", "aqp", "q", "topn(5, *:*)"),
                "FunctionScoreQuery(SecondOrderQuery(*:*, collector=SecondOrderCollectorTopN(5)), scored by boost(sum(float(cite_read_boost),const(0.5))))",
                FunctionScoreQuery.class);
        assertQueryEquals(req("defType", "aqp", "q", "topn(5, (foo bar))"),
                "FunctionScoreQuery(SecondOrderQuery(+all:foo +all:bar, collector=SecondOrderCollectorTopN(5)), scored by boost(sum(float(cite_read_boost),const(0.5))))",
                FunctionScoreQuery.class);

//        assertQueryEquals(req("defType", "aqp", "q", "topn(5, edismax(dog OR cat))", "qf", "title^1 abstract^0.5"),
//                "FunctionScoreQuery(SecondOrderQuery(((abstract:dog)^0.5 | title:dog) ((abstract:cat)^0.5 | title:cat), collector=SecondOrderCollectorTopN(5)), scored by boost(sum(float(cite_read_boost),const(0.5))))",
//                FunctionScoreQuery.class);
        assertQueryEquals(req("defType", "aqp", "q", "topn(5, author:accomazzi)"),
                "FunctionScoreQuery(SecondOrderQuery(author:accomazzi, author:accomazzi,*, collector=SecondOrderCollectorTopN(5)), scored by boost(sum(float(cite_read_boost),const(0.5))))",
                FunctionScoreQuery.class);

        /*
         * It is different if Aqp handles the boolean operations or if edismax() does
         * it.
         *
         * Aqp has more control, see: https://issues.apache.org/jira/browse/SOLR-4141
         */

        assertQueryEquals(req("defType", "aqp", "q", "edismax(dog OR cat)", "qf", "title^1 abstract^0.5"), // edismax
                "((abstract:dog)^0.5 | title:dog) ((abstract:cat)^0.5 | title:cat)", BooleanQuery.class);
        assertQueryEquals(req("defType", "aqp", "q", "dog OR cat", "qf", "title^1 abstract^0.5"), // aqp
                "((abstract:dog)^0.5 | title:dog) ((abstract:cat)^0.5 | title:cat)", BooleanQuery.class);

        assertQueryEquals(req("defType", "aqp", "q", "edismax(dog AND cat)", "qf", "title^1 abstract^0.5"), // edismax
                "+((abstract:dog)^0.5 | title:dog) +((abstract:cat)^0.5 | title:cat)", BooleanQuery.class);

        assertQueryEquals(req("defType", "aqp", "q", "dog AND cat", "qf", "title^1 abstract^0.5"), // aqp
                "+((abstract:dog)^0.5 | title:dog) +((abstract:cat)^0.5 | title:cat)", BooleanQuery.class);

        assertQueryEquals(req("defType", "aqp", "q", "edismax(dog OR cat)", "qf", "title^1 abstract^0.5"), // edismax
                "((abstract:dog)^0.5 | title:dog) ((abstract:cat)^0.5 | title:cat)", BooleanQuery.class);

        assertQueryEquals(req("defType", "aqp", "q", "dog OR cat", "qf", "title^1 abstract^0.5"), // aqp
                "((abstract:dog)^0.5 | title:dog) ((abstract:cat)^0.5 | title:cat)", BooleanQuery.class);

        assertQueryEquals(req("defType", "aqp", "q", "edismax(dog cat)", "qf", "title^1 abstract^0.5"), // edismax
                "((abstract:dog)^0.5 | title:dog) ((abstract:cat)^0.5 | title:cat)", BooleanQuery.class);

        assertQueryEquals(req("defType", "aqp", "q", "dog cat", "qf", "title^1 abstract^0.5", "q.op", "OR"), // aqp
                "((abstract:dog)^0.5 | title:dog) ((abstract:cat)^0.5 | title:cat)", BooleanQuery.class);

        assertQueryEquals(req("defType", "aqp", "q", "dog cat", "qf", "title^1 abstract^0.5", "q.op", "AND"), // aqp
                "+(+((abstract:dog)^0.5 | title:dog)) +(+((abstract:cat)^0.5 | title:cat))", BooleanQuery.class);

        assertQueryEquals(req("defType", "aqp", "q", "dog cat", "qf", "title^1 abstract^0.5"), // aqp
                "+((abstract:dog)^0.5 | title:dog) +((abstract:cat)^0.5 | title:cat)", BooleanQuery.class);

        // make sure the *:* query is not parsed by edismax
        assertQueryEquals(req("defType", "aqp", "q", "*", "qf", "author^2.3 title abstract^0.4"), "*:*",
                MatchAllDocsQuery.class);
        assertQueryEquals(req("defType", "aqp", "q", "*:*", "qf", "author^2.3 title abstract^0.4"), "*:*",
                MatchAllDocsQuery.class);

        /*
         * raw() function operator
         */

        // TODO: #234
        // need to add a processor which puts these local values into a request object
        // {!raw f=myfield}Foo Bar creates TermQuery(Term("myfield","Foo Bar"))
        // <astLOCAL_PARAMS value="{!f=myfield}" start="4" end="15" name="LOCAL_PARAMS"
        // type="27" />
        // assertQueryEquals(req("defType", "aqp", "f", "myfield", "q",
        // "raw({!f=myfield}Foo Bar)"), "myfield:Foo Bar", TermQuery.class);
        // assertQueryEquals(req("defType", "aqp", "f", "myfield", "q", "raw({!f=x}\"Foo
        // Bar\")"), "x:\"Foo Bar\"", TermQuery.class);

        assertQueryParseException(req("defType", "aqp", "f", "myfield", "q", "raw(Foo Bar)"));

        // if we use the solr analyzer to parse the query, all is configured to remove
        // stopwords
        assertQueryEquals(req("defType", "aqp", "q", "edismax(dog OR cat) OR title:bat all:but", "df", "all"),
                "((all:dog) (all:cat)) title:bat", BooleanQuery.class);

        // but pub is normalized_string with a different analyzer and should retain
        // 'but'
        assertQueryEquals(req("defType", "aqp", "q", "edismax(dog OR cat) OR title:bat OR pub:but", "df", "all"),
                "((all:dog) (all:cat)) title:bat pub:but", BooleanQuery.class);

        /**
         * new function queries, the 2nd order citation operators
         */

        // references()
        assertQueryEquals(req("defType", "aqp", "q", "references(author:foo)"),
                "SecondOrderQuery(author:foo, author:foo,*, collector=SecondOrderCollectorCitesRAM(cache:citations-cache))",
                SecondOrderQuery.class);

        // various searches
        assertQueryEquals(req("defType", "aqp", "q", "all:x OR all:z references(author:foo OR title:body)"),
                "+(all:x all:z) +SecondOrderQuery((author:foo, author:foo,*) title:body, collector=SecondOrderCollectorCitesRAM(cache:citations-cache))",
                BooleanQuery.class);
        assertQueryEquals(req("defType", "aqp", "q", "citations((title:(lectures physics) and author:Feynman))"),
                "SecondOrderQuery(+(+title:lectures +title:physics) +(author:feynman, author:feynman,*), collector=SecondOrderCollectorCitedBy(cache:citations-cache))",
                SecondOrderQuery.class);

        // citations()
        assertQueryEquals(req("defType", "aqp", "q", "citations(author:foo)"),
                "SecondOrderQuery(author:foo, author:foo,*, collector=SecondOrderCollectorCitedBy(cache:citations-cache))",
                SecondOrderQuery.class);

        // useful() - ads classic implementation
        assertQueryEquals(req("defType", "aqp", "q", "useful(author:foo)"),
                "SecondOrderQuery(SecondOrderQuery(author:foo, author:foo,*, collector=SecondOrderCollectorTopN(200)), collector=SecondOrderCollectorCitesRAM(cache:citations-cache))",
                SecondOrderQuery.class);

        assertQueryEquals(req("defType", "aqp", "q", "all:(x OR z) useful(author:foo OR title:body)"),
                "+(all:x all:z) +SecondOrderQuery(SecondOrderQuery((author:foo, author:foo,*) title:body, collector=SecondOrderCollectorTopN(200)), collector=SecondOrderCollectorCitesRAM(cache:citations-cache))",
                BooleanQuery.class);

        // useful2() - original implementation
        assertQueryEquals(req("defType", "aqp", "q", "useful2(author:foo)"),
                "SecondOrderQuery(SecondOrderQuery(author:foo, author:foo,*, collector=SecondOrderCollectorTopN(200)), collector=SecondOrderCollectorOperatorExpertsCiting(cache=citations-cache, boost=float[] cite_read_boost))",
                SecondOrderQuery.class);

        assertQueryEquals(req("defType", "aqp", "q", "all:(x OR z) useful2(author:foo OR title:body)"),
                "+(all:x all:z) +SecondOrderQuery(SecondOrderQuery((author:foo, author:foo,*) title:body, collector=SecondOrderCollectorTopN(200)), collector=SecondOrderCollectorOperatorExpertsCiting(cache=citations-cache, boost=float[] cite_read_boost))",
                BooleanQuery.class);

        // reviews() - ADS classic impl
        assertQueryEquals(req("defType", "aqp", "q", "reviews(author:foo)"),
                // "SecondOrderQuery(SecondOrderQuery(author:foo, author:foo,*,
                // collector=SecondOrderCollectorTopN(200)),
                // collector=SecondOrderCollectorCitedBy(cache:citations-cache))",
                "SecondOrderQuery(SecondOrderQuery(SecondOrderQuery(author:foo, author:foo,*, collector=SecondOrderCollectorAdsClassicScoringFormula(cache=citations-cache, boost=float[] citation_count, lucene=0.0, adsPart=1.0)), collector=SecondOrderCollectorTopN(200)), collector=SecondOrderCollectorCitedBy(cache:citations-cache))",
                SecondOrderQuery.class);

        assertQueryEquals(req("defType", "aqp", "q", "all:(x OR z) reviews(author:foo OR title:body)"),
                // "+(all:x all:z) +SecondOrderQuery(SecondOrderQuery((author:foo, author:foo,*)
                // title:body, collector=SecondOrderCollectorTopN(200)),
                // collector=SecondOrderCollectorCitedBy(cache:citations-cache))",
                "+(all:x all:z) +SecondOrderQuery(SecondOrderQuery(SecondOrderQuery((author:foo, author:foo,*) title:body, collector=SecondOrderCollectorAdsClassicScoringFormula(cache=citations-cache, boost=float[] citation_count, lucene=0.0, adsPart=1.0)), collector=SecondOrderCollectorTopN(200)), collector=SecondOrderCollectorCitedBy(cache:citations-cache))",
                BooleanQuery.class);

        // reviews2() - original impl
        assertQueryEquals(req("defType", "aqp", "q", "reviews2(author:foo)"),
                // "SecondOrderQuery(author:foo, author:foo,*,
                // collector=SecondOrderCollectorCitingTheMostCited(cache=citations-cache,
                // boost=float[] cite_read_boost))",
                "SecondOrderQuery(SecondOrderQuery(author:foo, author:foo,*, collector=SecondOrderCollectorTopN(200)), collector=SecondOrderCollectorCitingTheMostCited(cache=citations-cache, boost=float[] cite_read_boost))",
                SecondOrderQuery.class);

        assertQueryEquals(req("defType", "aqp", "q", "all:(x OR z) reviews2(author:foo OR title:body)"),
                // "+(all:x all:z) +SecondOrderQuery((author:foo, author:foo,*) title:body,
                // collector=SecondOrderCollectorCitingTheMostCited(cache=citations-cache,
                // boost=float[] cite_read_boost))",
                "+(all:x all:z) +SecondOrderQuery(SecondOrderQuery((author:foo, author:foo,*) title:body, collector=SecondOrderCollectorTopN(200)), collector=SecondOrderCollectorCitingTheMostCited(cache=citations-cache, boost=float[] cite_read_boost))",
                BooleanQuery.class);

        // classic_relevance() - cr()
        assertQueryEquals(req("defType", "aqp", "q", "classic_relevance(title:foo)"),
                "SecondOrderQuery(title:foo, collector=SecondOrderCollectorAdsClassicScoringFormula(cache=citations-cache, boost=float[] cite_read_boost, lucene=0.5, adsPart=0.5))",
                SecondOrderQuery.class);
        assertQueryEquals(req("defType", "aqp", "q", "cr(title:foo)"),
                "SecondOrderQuery(title:foo, collector=SecondOrderCollectorAdsClassicScoringFormula(cache=citations-cache, boost=float[] cite_read_boost, lucene=0.5, adsPart=0.5))",
                SecondOrderQuery.class);
        assertQueryEquals(req("defType", "aqp", "q", "cr(title:foo, 0.4)"),
                "SecondOrderQuery(title:foo, collector=SecondOrderCollectorAdsClassicScoringFormula(cache=citations-cache, boost=float[] cite_read_boost, lucene=0.4, adsPart=0.6))",
                SecondOrderQuery.class);

    }

    public void testSearch() throws Exception {

        // search for all docs with a field
        assertQueryEquals(req("defType", "aqp", "q", "title:*", "aqp.allow.leading_wildcard", "true"), "title:*",
                PrefixQuery.class);
        assertQueryEquals(req("defType", "aqp", "q", "title:?", "aqp.allow.leading_wildcard", "true"), "title:?",
                WildcardQuery.class);

        // fun test of a crazy span query
        assertQueryEquals(req("defType", "aqp", "q", "(consult* or advis*) NEAR4 (fee or retainer or salary or bonus)"),
                "spanNear([spanOr([SpanMultiTermQueryWrapper(all:consult*), SpanMultiTermQueryWrapper(all:advis*)]), spanOr([all:fee, all:retainer, all:salary, all:bonus])], 4, false)",
                SpanNearQuery.class);
        assertQueryEquals(
                req("defType", "aqp", "q", "(consult* and advis*) NEAR4 (fee or retainer or salary or bonus)"),
                "spanNear([spanNear([SpanMultiTermQueryWrapper(all:consult*), SpanMultiTermQueryWrapper(all:advis*)], 4, false), spanOr([all:fee, all:retainer, all:salary, all:bonus])], 4, false)",
                SpanNearQuery.class);

        // #375
        assertQueryEquals(
                req("defType", "aqp", "q",
                        "author:\"Civano, F\" -author_facet_hier:(\"Civano, Fa\" OR \"Civano, Da\")"),
                "+(author:civano, f | author:civano, f* | author:civano,) -(author_facet_hier:civano, fa author_facet_hier:civano, da)",
                BooleanQuery.class);
        assertQueryEquals(
                req("defType", "aqp", "q",
                        "author:\"Civano, F\" +author_facet_hier:(\"Civano, Fa\" OR \"Civano, Da\")"),
                "+(author:civano, f | author:civano, f* | author:civano,) +(author_facet_hier:civano, fa author_facet_hier:civano, da)",
                BooleanQuery.class);
        assertQueryEquals(req("defType", "aqp", "q", "title:xxx -title:(foo OR bar)"),
                "+title:xxx -(title:foo title:bar)", BooleanQuery.class);
        assertQueryEquals(req("defType", "aqp", "q", "title:xxx +title:(foo OR bar)"),
                "+title:xxx +(title:foo title:bar)", BooleanQuery.class);
        assertQueryEquals(req("defType", "aqp", "q", "title:xxx +title:(-foo OR bar)"),
                "+title:xxx +(-title:foo title:bar)", BooleanQuery.class);

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

        assertU(adoc("id", "57", "bibcode", "b57", "author", "Kurtz, M.", "author", "Foo, Bar"));
        assertU(adoc("id", "58", "bibcode", "b58", "author", "Kurtz, M J", "author", "Foo, Bar"));
        assertU(adoc("id", "59", "bibcode", "b59", "author", "Mason, James Paul"));
        assertU(commit("waitSearcher", "true"));

        // regex
        assertQ(req("q", "author:/kurtz, [^ ]+/"), "//*[@numFound='1']", "//doc/str[@name='id'][.='57']");
        assertQ(req("q", "author:/kurtz,[^ ]+/"), "//*[@numFound='0']");

        assertQ(req("q", "author:/kurtz, [^ ]+ [^ ]+/"), "//*[@numFound='1']", "//doc/str[@name='id'][.='58']");
        assertQ(req("q", "author:/mason, j[^ ]+ p[^ ]+/"), "//*[@numFound='1']");
        assertQ(req("q", "author:/mason, james p[^ ]+/"), "//*[@numFound='1']");
        assertQ(req("q", "author:/mason, james paul/"), "//*[@numFound='1']");
        assertQ(req("q", "author:/mason, j[^ ]+/"), "//*[@numFound='0']");
        assertQ(req("q", "author:/mason, p[^ ]+/"), "//*[@numFound='0']");

        // this is treated as regex, but because it is unfielded search
        // it ends up in the unfielded_search field. Feature or a bug?
        assertQueryEquals(req("defType", "aqp", "q", "/^Kurtz, M./"), "unfielded_search:/^Kurtz, M./",
                RegexpQuery.class);
        // regex ignores unfielded queries even when qf is set
        assertQueryEquals(req("defType", "aqp", "q", "/^Kurtz, M./", "qf", "title^0.5 author^0.8"),
                "unfielded_search:/^Kurtz, M./", RegexpQuery.class);

        assertQueryEquals(req("defType", "aqp", "q", "author:/kurtz, m.*/"), "author:/kurtz, m.*/", RegexpQuery.class);
        assertQueryEquals(req("defType", "aqp", "q", "author:(/kurtz, m.*/)"), "author:/kurtz, m.*/",
                RegexpQuery.class);
        assertQueryEquals(req("defType", "aqp", "q", "abstract:/nas[^ ]+/"), "abstract:/nas[^ ]+/", RegexpQuery.class);

        // NEAR queries are little bit too crazy and will need taming
        // and *more* unittest examples
        assertQueryEquals(req("defType", "aqp", "q", "author:(accomazzi NEAR5 kurtz)"),
                "spanNear([spanOr([author:accomazzi,, SpanMultiTermQueryWrapper(author:accomazzi,*)]), "
                        + "spanOr([author:kurtz,, SpanMultiTermQueryWrapper(author:kurtz,*)])], 5, false)",
                SpanNearQuery.class);

        assertQueryEquals(req("defType", "aqp", "q", "\"NASA grant\"~3 NEAR N*"),
                "spanNear([spanNear([all:acr::nasa, all:grant], 3, true), SpanMultiTermQueryWrapper(all:n*)], 5, false)",
                SpanNearQuery.class);
        assertQueryEquals(req("defType", "aqp", "q", "\"NASA grant\"^0.9 NEAR N*"),
                "spanNear([PayloadScoreQuery(spanNear([all:acr::nasa, all:grant], 1, true), function: ConstantPayloadFunction, includeSpanScore: true), SpanMultiTermQueryWrapper(all:n*)], 5, false)",
                SpanNearQuery.class);
        assertQueryEquals(req("defType", "aqp", "q", "\"NASA grant\"~3^0.9 NEAR N*"),
                "spanNear([PayloadScoreQuery(spanNear([all:acr::nasa, all:grant], 3, true), function: ConstantPayloadFunction, includeSpanScore: true), SpanMultiTermQueryWrapper(all:n*)], 5, false)",
                SpanNearQuery.class);

        // identifiers
        assertQueryEquals(req("defType", "aqp", "q", "identifier:2011A&A...536A..89G"),
                "identifier:2011a&a...536a..89g", TermQuery.class);
        assertQueryEquals(req("defType", "aqp", "q", "identifier:2011A&A" + "\u2026" + "536A..89G"),
                "identifier:2011a&a...536a..89g", TermQuery.class);

        /*
         * translation of the fields (on the fly)
         */

        // field_map is set to translate arxiv->identifier
        assertQueryEquals(req("defType", "aqp", "q", "arxiv:1002.1524"), "identifier:1002.1524", TermQuery.class);
        assertQueryParseException(req("defType", "aqp", "q", "arxivvvv:1002.1524"));

    }

    public void testSubquery() throws Exception {
        // fixedbitset has to be one bit larger; also we need to round up num of bits
        int size = ((100 + 8) / 8) * 8;
        BitSet bitSet = new FixedBitSet(size);
        bitSet.set(1);
        bitSet.set(2);

        BitSetQParserPlugin bqp = new BitSetQParserPlugin();
        bqp.init(new NamedList() {
        });

        String stream1 = bqp.encodeBase64(bqp.toByteArray(bitSet));
        bitSet.set(5);
        String stream2 = bqp.encodeBase64(bqp.toByteArray(bitSet));

        assertU(addDocs("id", "0", "bibcode", "b0"));
        assertU(addDocs("id", "1", "bibcode", "b1"));
        assertU(addDocs("id", "2", "bibcode", "b2"));
        assertU(commit("waitSearcher", "true"));
        assertU(addDocs("id", "3", "bibcode", "b3"));
        assertU(addDocs("id", "4", "bibcode", "b4"));
        assertU(addDocs("id", "5", "bibcode", "b5"));
        assertU(commit("waitSearcher", "true"));

        assertQueryEquals(
                req("defType", "aqp", "q", "*:* AND docs(fq_foo)", "fq_foo", "{!bitset compression=none} " + stream1),
                "+*:* +BitSetQuery(2)", BooleanQuery.class);

        assertQueryEquals(
                req("defType", "aqp", "q", "docs(foo) OR docs(bar)", "foo", "{!bitset compression=none} " + stream1,
                        "bar", "{!bitset compression=none} " + stream2),
                "BitSetQuery(2) BitSetQuery(3)", BooleanQuery.class);

        assertQueryEquals(
                req("defType", "aqp", "q", "docs(foo) AND docs(bar)", "foo", "{!bitset compression=none} " + stream1,
                        "bar", "{!bitset compression=none} " + stream2),
                "+BitSetQuery(2) +BitSetQuery(3)", BooleanQuery.class);

        assertQ(req("defType", "aqp", "q", "docs(foo) OR docs(bar)", "foo", "{!bitset compression=none} " + stream1,
                        "bar", "{!bitset compression=none} " + stream2), "//*[@numFound='3']", "//doc/str[@name='id'][.='1']",
                "//doc/str[@name='id'][.='2']", "//doc/str[@name='id'][.='5']");

        assertQ(req("defType", "aqp", "q", "docs(foo) AND docs(bar)", "foo", "{!bitset compression=none} " + stream1,
                        "bar", "{!bitset compression=none} " + stream2), "//*[@numFound='2']", "//doc/str[@name='id'][.='1']",
                "//doc/str[@name='id'][.='2']");

        assertQ(req("defType", "aqp", "q", "docs(bar) bibcode:b1", "foo", "{!bitset compression=none} " + stream1,
                "bar", "{!bitset compression=none} " + stream2), "//*[@numFound='1']", "//doc/str[@name='id'][.='1']");

        // sending bibcodes as a stream
        List<ContentStream> streams = new ArrayList<ContentStream>(1);
        ContentStreamBase cs = new ContentStreamBase.StringStream("bibcode\nb0\nb1\nb3");
        cs.setName("fq_foo");
        cs.setContentType("big-query/csv");
        streams.add(cs);

        cs = new ContentStreamBase.StringStream("bibcode\nb0\nb2\nb4");
        cs.setName("fq_bar");
        cs.setContentType("big-query/csv");
        streams.add(cs);

        SolrQueryRequestBase req = (SolrQueryRequestBase) req("qt", "/bigquery", "q",
                "(docs(fq_foo) OR docs(fq_bar)) AND bibcode:b4");
        req.setContentStreams(streams);

        assertQ(req, "//*[@numFound='1']", "//doc/str[@name='bibcode'][.='b4']");

        req = (SolrQueryRequestBase) req("qt", "/bigquery", "q", "(docs(fq_foo) AND docs(fq_bar)) AND bibcode:b4");
        req.setContentStreams(streams);
        assertQ(req, "//*[@numFound='0']");

        req = (SolrQueryRequestBase) req("qt", "/bigquery", "q", "(docs(fq_foo) AND docs(fq_bar)) OR bibcode:b4");
        req.setContentStreams(streams);
        assertQ(req, "//*[@numFound='2']", "//doc/str[@name='bibcode'][.='b0']", "//doc/str[@name='bibcode'][.='b4']");

    }

    public void testCustomScoring() throws Exception {

        assertQueryEquals(req("defType", "aqp", "q", "abs:\"dark energy\""),
                "(abstract:\"dark energy\" | Synonym(abstract:acr::de abstract:syn::dark energy abstract:syn::de)) "
                        + "(title:\"dark energy\" | Synonym(title:acr::de title:syn::dark energy title:syn::de)) "
                        + "(keyword:\"dark energy\" | Synonym(keyword:acr::de keyword:syn::dark energy keyword:syn::de))",
                BooleanQuery.class);

        /*
        assertQueryEquals(req("defType", "aqp", "q", "abs:\"dark energy\"", "aqp.classic_scoring.modifier", "0.6"),
                "custom((abstract:\"dark energy\" | Synonym(abstract:acr::de abstract:syn::dark energy abstract:syn::de)) "
                        + "(title:\"dark energy\" | Synonym(title:acr::de title:syn::dark energy title:syn::de)) "
                        + "(keyword:\"dark energy\" | Synonym(keyword:acr::de keyword:syn::dark energy keyword:syn::de)), "
                        + "sum(float(cite_read_boost),const(0.6)))",
                FunctionScoreQuery.class);
         */

        assertQueryContains(req("defType", "aqp", "q", "author:\"foo, bar\"", "aqp.classic_scoring.modifier", "0.5"),
                "scored by boost(sum(float(cite_read_boost),const(0.5))))",
                FunctionScoreQuery.class);

        assertQueryNotContains(req("defType", "aqp", "q", "author:\"^foo, bar\"", "no.classic_scoring.modifier", "0.5"),
                "scored by",
                SpanPositionRangeQuery.class);
        assertQueryContains(req("defType", "aqp", "q", "author:\"^foo, bar\"", "aqp.classic_scoring.modifier", "0.5"),
                "scored by boost(sum(float(cite_read_boost),const(0.5))))",
                FunctionScoreQuery.class);

        assertQueryContains(
                req("defType", "aqp", "q", "foo bar aqp(baz)", "aqp.classic_scoring.modifier", "0.6", "qf",
                        "title keyword"),
                "scored by boost(sum(float(cite_read_boost),const(0.6))))",
                FunctionScoreQuery.class);

        // when used in conjunction with constant scoring, custom modifies constant (but
        // it won't replace it)
        assertQueryEquals(
                req("defType", "aqp", "q", "^accomazzi", "aqp.constant_scoring", "author^5", "qf", "author title"),
                "(ConstantScore(spanPosRange(spanOr([author:accomazzi,, SpanMultiTermQueryWrapper(author:accomazzi,*)]), 0, 1)))^5.0",
                BoostQuery.class);
        assertQueryContains(
                req("defType", "aqp", "q", "^accomazzi", "aqp.classic_scoring.modifier", "0.5", "aqp.constant_scoring",
                        "author^5", "qf", "author title"),
                ")))^5.0, scored by boost(sum(float(cite_read_boost),const(0.5))))",
                FunctionScoreQuery.class);

    }

    public static junit.framework.Test suite() {
        return new junit.framework.JUnit4TestAdapter(TestAqpAdsabsSolrSearch.class);
    }

}
