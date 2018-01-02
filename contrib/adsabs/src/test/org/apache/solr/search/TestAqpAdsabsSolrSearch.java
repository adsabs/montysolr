package org.apache.solr.search;


import java.io.File;
import java.io.IOException;

import monty.solr.util.MontySolrQueryTestCase;
import monty.solr.util.MontySolrSetup;

import org.apache.lucene.queryparser.flexible.aqp.TestAqpAdsabs;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.BoostQuery;
import org.apache.lucene.search.DisjunctionMaxQuery;
import org.apache.lucene.search.MatchAllDocsQuery;
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
 * @author rchyla
 *         <p>
 *         XXX: I was uneasy about the family of these tests
 *         because they depend on the settings from the other
 *         project (contrib/examples) - on the other hand, I
 *         don't want to duplicate the code/config files. So,
 *         for now I resigned, and I think of contrib/examples
 *         as a dependency for adsabs
 *         <p>
 *         contrib/examples should contain only a code for the
 *         live site (setup), but we are developing components
 *         for it here and we'll test it here
 *         (inside contrib/adsabs)
 *         <p>
 *         Let's do it pragmatically (not as code puritans)
 * @see TestAqpAdsabs for the other tests
 */
public class TestAqpAdsabsSolrSearch extends MontySolrQueryTestCase {

    @BeforeClass
    public static void beforeClass() throws Exception {

        makeResourcesVisible(Thread.currentThread().getContextClassLoader(),
                new String[]{MontySolrSetup.getMontySolrHome() + "/contrib/examples/adsabs/server/solr/collection1",
                        MontySolrSetup.getSolrHome() + "/example/solr/collection1"
                });

        System.setProperty("solr.allow.unsafe.resourceloading", "true");
        schemaString = getSchemaFile();


        configString = MontySolrSetup.getMontySolrHome()
                + "/contrib/examples/adsabs/server/solr/collection1/solrconfig.xml";

        initCore(configString, schemaString, MontySolrSetup.getSolrHome()
                + "/example/solr");
    }

    public static String getSchemaFile() {

		/*
		 * For purposes of the test, we make a copy of the schema.xml,
		 * and create our own synonym files
		 */

        String configFile = MontySolrSetup.getMontySolrHome()
                + "/contrib/examples/adsabs/server/solr/collection1/schema.xml";

        File newConfig;
        try {

            newConfig = duplicateFile(new File(configFile));

            File multiSynonymsFile = createTempFile(new String[]{
                    "hubble\0space\0telescope, HST",
                    "r\0s\0t, RST",
                    "dark\0energy, DE"
            });
            replaceInFile(newConfig, "synonyms=\"ads_text_multi.synonyms\"", "synonyms=\"" + multiSynonymsFile.getAbsolutePath() + "\"");

            File synonymsFile = createTempFile(new String[]{
                    "weak => lightweak",
                    "lensing => mikrolinseneffekt",
                    "pink => pinkish",
                    "stephen, stephens => stephen",
                    "bremßtrahlung => brehmen"
            });
            replaceInFile(newConfig, "synonyms=\"ads_text_simple.synonyms\"", "synonyms=\"" + synonymsFile.getAbsolutePath() + "\"");

            // hand-curated synonyms
            File curatedSynonyms = createTempFile(new String[]{
                    "JONES, CHRISTINE;FORMAN, CHRISTINE" // the famous post-synonym expansion
            });
            replaceInFile(newConfig, "synonyms=\"author_curated.synonyms\"", "synonyms=\"" + curatedSynonyms.getAbsolutePath() + "\"");

        } catch (IOException e) {
            e.printStackTrace();
            throw new IllegalStateException(e.getMessage());
        }

        return newConfig.getAbsolutePath();
    }


    public void testUnfieldedSearch() throws Exception {
		
		/*
		 * Unfielded search should be expanded automatically by edismax
		 * 
		 * However, edismax is not smart enough to deal properly with boolean clauses
		 * and default operators, so I have decided to use the edismax on the "value"
		 * level only. First, we parse the query, then we pass it to the 'adismax'
		 * query parser (a modified edismax) to expand it; adismax will use aqp 
		 * to build the individual queries - so it is best of both worlds
		 *  
		 */
        // first the individual elements explicitly (notice edismax differs from adismax)
        assertQueryEquals(req("defType", "aqp", "q", "adismax(MÜLLER)",
                "qf", "author^2.3 title abstract^0.4"),
                "("
                + "((abstract:acr::müller abstract:acr::muller))^0.4 | "
                + "((author:müller, author:müller,* author:mueller, author:mueller,* author:muller, author:muller,*))^2.3 | "
                + "((title:acr::müller title:acr::muller))"
                + ")",
                DisjunctionMaxQuery.class);
        assertQueryEquals(req("defType", "aqp", "q", "edismax(MÜLLER)",
                "qf", "author^2.3 title abstract^0.4"),
                "("
                + "(Synonym(abstract:acr::muller abstract:acr::müller))^0.4 | "
                + "Synonym(title:acr::muller title:acr::müller) | "
                + "(Synonym(author:mueller, author:muller, author:müller,))^2.3"
                + ")",
                DisjunctionMaxQuery.class);


        // unfielded search should handle authors like adismax (with expansions)
        assertQueryEquals(req("defType", "aqp", "q", "MÜLLER",
                "qf", "author^2.3 title abstract^0.4"),
                "("
                + "((abstract:acr::müller abstract:acr::muller))^0.4 | "
                + "((author:müller, author:müller,* author:mueller, author:mueller,* author:muller, author:muller,*))^2.3 | "
                + "((title:acr::müller title:acr::muller))"
                + ")",
                DisjunctionMaxQuery.class);
        assertQueryEquals(req("defType", "aqp", "q", "\"forman, c\"",
                "qf", "author^2.3 title abstract^0.4"),
                "((abstract:\"forman c\")^0.4 | ((author:forman, c author:forman, christine author:jones, c author:jones, christine author:forman, c* author:forman,))^2.3 | title:\"forman c\")",
                DisjunctionMaxQuery.class);

        // now add a normal element
        assertQueryEquals(req("defType", "aqp", "q", "title:foo or MÜLLER",
                "qf", "author^2.3 title abstract^0.4"),
                "title:foo (((abstract:acr::müller abstract:acr::muller))^0.4 | ((author:müller, author:müller,* author:mueller, author:mueller,* author:muller, author:muller,*))^2.3 | ((title:acr::müller title:acr::muller)))",
                BooleanQuery.class);
        assertQueryEquals(req("defType", "aqp", "q", "title:foo or \"forman, c\"",
                "qf", "author^2.3 title abstract^0.4"),
                "title:foo ((abstract:\"forman c\")^0.4 | ((author:forman, c author:forman, christine author:jones, c author:jones, christine author:forman, c* author:forman,))^2.3 | title:\"forman c\")",
                BooleanQuery.class);


        // this should not call edismax (because qf is missing)
        assertQueryEquals(req("defType", "aqp", "q", "accomazzi", "df", "author"),
                "author:accomazzi, author:accomazzi,*",
                BooleanQuery.class);
		
		
		/*
		 *  Now various cases of multi-token unfielded searches (incl multi-token synonyms)
		 *  and full author parse
		 */

        // this is default behaviour, if you see 'all:' it means edismax didn't parse it
        assertQueryEquals(req("defType", "aqp", "q", "author:accomazzi, alberto property:refereed apj"),
                "+(author:accomazzi, author:accomazzi,*) +all:alberto +property:refereed +all:apj",
                BooleanQuery.class);
        assertQueryEquals(req("defType", "aqp", "q", "author:huchra supernova"),
                "+(author:huchra, author:huchra,*) +all:supernova",
                BooleanQuery.class);

        // smarter handling of missing parentheses/brackets	 with the special strategy
        // i expect following:
        // edismax receives: 'author:accomazzi, alberto' and also 'author:"accomazzi, alberto"
        //      ""         : 'property:refereed r s t'  and 'property:"refereed r s t"'
        
        assertQueryEquals(req("defType", "aqp",
                "aqp.unfielded.tokens.strategy", "multiply",
                "aqp.unfielded.tokens.new.type", "simple",
                "qf", "title keyword",
                "q", "author:accomazzi, alberto property:refereed r s t"),
                "+("
                + "(+((author:accomazzi, author:accomazzi,*)) +(keyword:alberto | title:alberto)) "
                + "(((author:accomazzi, alberto author:accomazzi, alberto * author:accomazzi, a author:accomazzi, a * author:accomazzi,))~1)"
                + ") "
                + "+("
                + "(+property:refereed +(keyword:r | title:r) +(keyword:s | title:s) +(keyword:t | title:t)) property:refereedrst"
                + ")",
                BooleanQuery.class);
        // the same as above + enhanced by multisynonym
        // i expect to see syn::r s t, syn::acr::rst
        assertQueryEquals(req("defType", "aqp",
                "aqp.unfielded.tokens.strategy", "multiply",
                "aqp.unfielded.tokens.new.type", "simple",
                "aqp.unfielded.phrase.edismax.synonym.workaround", "true",
                "q", "author:accomazzi, alberto property:refereed r s t",
                "qf", "title keyword^0.5"),
                "+((+((author:accomazzi, author:accomazzi,*)) +((keyword:alberto)^0.5 | title:alberto)) "
                + "(((author:accomazzi, alberto author:accomazzi, alberto * author:accomazzi, a author:accomazzi, a * author:accomazzi,))~1)) "
                +"+((+property:refereed +((keyword:r)^0.5 | title:r) +((keyword:s)^0.5 | title:s) +((keyword:t)^0.5 | title:t)) property:refereedrst)",
                BooleanQuery.class);


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
        // 30/09/16 - edismax is using default AND
        assertQueryEquals(req("defType", "aqp",
                "aqp.unfielded.tokens.strategy", "multiply",
                "aqp.unfielded.tokens.new.type", "simple",
                "q", "pink elephant"),
                "(+(((all:pink all:syn::pinkish))) +(all:elephant)) all:\"(pink syn::pinkish) elephant\"",
                BooleanQuery.class);

        assertQueryEquals(req("defType", "aqp", "q", "pink elephant",
                "aqp.unfielded.tokens.strategy", "multiply",
                "aqp.unfielded.tokens.new.type", "simple",
                "qf", "title keyword"),
                "(+(((keyword:pink keyword:syn::pinkish)) | ((title:pink title:syn::pinkish))) +(keyword:elephant | title:elephant)) (keyword:\"(pink syn::pinkish) elephant\" | title:\"(pink syn::pinkish) elephant\")",
                BooleanQuery.class);

        // when combined, the ADS's default AND operator should be visible +foo
        assertQueryEquals(req("defType", "aqp", "q", "pink elephant title:foo",
                "aqp.unfielded.tokens.strategy", "multiply",
                "aqp.unfielded.tokens.new.type", "simple",
                "qf", "title keyword"),
                "+((+(((keyword:pink keyword:syn::pinkish)) | ((title:pink title:syn::pinkish))) +(keyword:elephant | title:elephant)) (keyword:\"(pink syn::pinkish) elephant\" | title:\"(pink syn::pinkish) elephant\")) +title:foo",
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
                "(+((keyword:r)^0.7 | (title:r)^0.9) +((keyword:s)^0.7 | (title:s)^0.9) +((keyword:t)^0.7 | (title:t)^0.9)) (((keyword:\"r s t\" keyword:syn::r s t keyword:syn::acr::rst))^0.7 | ((title:\"r s t\" title:syn::r s t title:syn::acr::rst))^0.9)",
                BooleanQuery.class);

        assertQueryEquals(req("defType", "aqp",
                "q", "x r s t y",
                "aqp.unfielded.tokens.strategy", "multiply",
                "aqp.unfielded.tokens.new.type", "simple",
                "aqp.unfielded.phrase.edismax.synonym.workaround", "true",
                "qf", "title^0.9 keyword_norm^0.7"),
                "(+((keyword_norm:x)^0.7 | (title:x)^0.9) +((keyword_norm:r)^0.7 | (title:r)^0.9) +((keyword_norm:s)^0.7 | (title:s)^0.9) +((keyword_norm:t)^0.7 | (title:t)^0.9) +((keyword_norm:y)^0.7 | (title:y)^0.9)) ((keyword_norm:\"x r s t y\")^0.7 | ((title:\"x r s t y\" title:\"x (syn::r s t syn::acr::rst) ? ? y\"~2))^0.9)",
                BooleanQuery.class);


        // author search, unfielded (which looks as one token) - it looks like that to
        // adismax, but aqp will see two tokens...
        // the result is crazy because of recursive parsing
        // 1: accomazzi,alberto
        // 2:   author:accomazzi,alberto -> author:accomazzi AND adismax(alberto)
        // 3:      adismax(alberto) -> title:alberto OR author:alberto^2.3
        assertQueryEquals(req("defType", "aqp", "q", "accomazzi,alberto",
                "qf", "author^2.3 title",
                "aqp.unfielded.tokens.strategy", "multiply",
                "aqp.unfielded.tokens.new.type", "simple"
                ),
                "("
                + "((+(author:accomazzi, author:accomazzi,*) +(((author:alberto, author:alberto,*))^2.3 | title:alberto)))^2.3 "
                + "| ((+title:accomazzi +(((author:alberto, author:alberto,*))^2.3 | title:alberto)))) "
                + "(((author:accomazzi, alberto author:accomazzi, alberto * author:accomazzi, a author:accomazzi, a * author:accomazzi,))^2.3 | ((title:\"accomazzi alberto\" title:accomazzialberto)))",
                BooleanQuery.class);


        // see what happens during normal parsing
        // author search, unfielded (which looks as one token)
        assertQueryEquals(req("defType", "aqp",
                "q", "author:accomazzi,alberto"
                ),
                "+(author:accomazzi, author:accomazzi,*) +all:alberto",
                BooleanQuery.class);

        assertQueryEquals(req("defType", "aqp", "q", "author:accomazzi,alberto",
                "qf", "author^2.3 title",
                "aqp.unfielded.tokens.strategy", "multiply",
                "aqp.unfielded.tokens.new.type", "simple"
                ),
                "(((+(author:accomazzi, author:accomazzi,*) +(((author:alberto, author:alberto,*))^2.3 | title:alberto)))~1) (((author:accomazzi, alberto author:accomazzi, alberto * author:accomazzi, a author:accomazzi, a * author:accomazzi,))~1)",
                BooleanQuery.class);


    }

    public void testSpecialCases() throws Exception {
      
        // inconsistency disabling synonyms: #39
        assertQueryEquals(req("defType", "aqp", "q", "full:bremßtrahlung"),
          "(ack:bremßtrahlung ack:bremsstrahlung ack:syn::brehmen) "
          + "(abstract:bremßtrahlung abstract:bremsstrahlung abstract:syn::brehmen)^2.0 "
          + "(title:bremßtrahlung title:bremsstrahlung title:syn::brehmen)^2.0 "
          + "(body:bremßtrahlung body:bremsstrahlung body:syn::brehmen) "
          + "(keyword:bremßtrahlung keyword:bremsstrahlung keyword:syn::brehmen)",
          BooleanQuery.class);
        assertQueryEquals(req("defType", "aqp", "q", "=full:bremßtrahlung"),
            "(ack:bremßtrahlung ack:bremsstrahlung) "
                + "(abstract:bremßtrahlung abstract:bremsstrahlung)^2.0 "
                + "(title:bremßtrahlung title:bremsstrahlung)^2.0 "
                + "(body:bremßtrahlung body:bremsstrahlung) "
                + "(keyword:bremßtrahlung keyword:bremsstrahlung)",
            BooleanQuery.class);
        assertQueryEquals(req("defType", "aqp", "q", "body:bremßtrahlung"),
            "body:bremßtrahlung body:bremsstrahlung body:syn::brehmen",
            BooleanQuery.class);
        assertQueryEquals(req("defType", "aqp", "q", "=body:bremßtrahlung"),
            "body:bremßtrahlung body:bremsstrahlung",
            BooleanQuery.class);


      
        // disable synonyms (also for virtual fiels) - #36
        assertQueryEquals(req("defType", "aqp", "q", "abs:\"dark energy\""),
          "(abstract:\"dark energy\" abstract:syn::dark energy abstract:syn::acr::de) "
          + "(title:\"dark energy\" title:syn::dark energy title:syn::acr::de) "
          + "(keyword:\"dark energy\" keyword:syn::dark energy keyword:syn::acr::de)",
          BooleanQuery.class);
        assertQueryEquals(req("defType", "aqp", "q", "=abs:\"dark energy\""),
            "abstract:\"dark energy\" title:\"dark energy\" keyword:\"dark energy\"",
            BooleanQuery.class);
        
        assertQueryEquals(req("defType", "aqp", "q", "=abs:(\"dark energy\")"),
            "abstract:\"dark energy\" title:\"dark energy\" keyword:\"dark energy\"",
            BooleanQuery.class);

        assertQueryEquals(req("defType", "aqp", "q", "abs:(weak)"),
            "(abstract:weak abstract:syn::lightweak) (title:weak title:syn::lightweak) (keyword:weak keyword:syn::lightweak)",
            BooleanQuery.class);
        assertQueryEquals(req("defType", "aqp", "q", "=abs:(weak)"),
            "abstract:weak title:weak keyword:weak",
            BooleanQuery.class);
        assertQueryEquals(req("defType", "aqp", "q", "abs:(=weak weak)"),
            "+(abstract:weak title:weak keyword:weak) +((abstract:weak abstract:syn::lightweak) (title:weak title:syn::lightweak) (keyword:weak keyword:syn::lightweak))",
            BooleanQuery.class);
        
        // full - virtual field with wrong date
        assertQueryEquals(req("defType", "aqp", "q", "full:(\"15-52-15050\" OR \"15-32-21062\")"),
          "((ack:\"15 52 15050\" ack:155215050) (abstract:\"15 52 15050\" abstract:155215050)^2.0 (title:\"15 52 15050\" title:155215050)^2.0 (body:\"15 52 15050\" body:155215050) (keyword:\"15 52 15050\" keyword:155215050)) ((ack:\"15 32 21062\" ack:153221062) (abstract:\"15 32 21062\" abstract:153221062)^2.0 (title:\"15 32 21062\" title:153221062)^2.0 (body:\"15 32 21062\" body:153221062) (keyword:\"15 32 21062\" keyword:153221062))",
          BooleanQuery.class);

      

        // nested functions should parse well: citations(author:"^kurtz")
        assertQueryEquals(req("defType", "aqp", "q", "citations(author:\"^kurtz\")"),
                "SecondOrderQuery(spanPosRange(spanOr([author:kurtz,, SpanMultiTermQueryWrapper(author:kurtz,*)]), 0, 1), collector=SecondOrderCollectorCitedBy(cache:citations-cache))",
                SecondOrderQuery.class);

        assertQueryEquals(req("defType", "aqp", "q", "citations(citations(author:\"^kurtz\"))"),
                "SecondOrderQuery(SecondOrderQuery(spanPosRange(spanOr([author:kurtz,, SpanMultiTermQueryWrapper(author:kurtz,*)]), 0, 1), collector=SecondOrderCollectorCitedBy(cache:citations-cache)), collector=SecondOrderCollectorCitedBy(cache:citations-cache))",
                SecondOrderQuery.class);

        // #30 - first_author and author:"^fooo" give diff results
        assertQueryEquals(req("defType", "aqp",
                "q", "first_author:\"kurtz, m j\""
                ),
                "first_author:kurtz, m j first_author:kurtz, m j* first_author:/kurtz, m[^\\s]+/ first_author:/kurtz, m[^\\s]+ j.*/ first_author:kurtz, m first_author:kurtz,",
                BooleanQuery.class
        );
        assertQueryEquals(req("defType", "aqp",
                "q", "author:\"^kurtz, m j\""
                ),
                "spanPosRange(spanOr([author:kurtz, m j, SpanMultiTermQueryWrapper(author:kurtz, m j*), SpanMultiTermQueryWrapper(author:/kurtz, m[^\\s]+/), SpanMultiTermQueryWrapper(author:/kurtz, m[^\\s]+ j.*/), author:kurtz, m, author:kurtz,]), 0, 1)",
                SpanPositionRangeQuery.class
        );

        // strange effect of paranthesis - github #23; we want to see this even (inside brackets)
        // +(
        //   (
        //    (
        //     DisjunctionMaxQuery((((author:stephen, author:stephen,*)) | ((title:stephen title:syn::stephen))))
        //     DisjunctionMaxQuery((((author:murray, author:murray, margaret a author:murray, m a author:hanson, m m author:hanson, margaret m author:murray,*)) | title:murray))
        //    )~2
        //   )
        //   DisjunctionMaxQuery((
        //    ((author:stephen murray, author:stephen murray,* author:murray, stephen author:murray, stephen * author:murray, stephen * author:murray, s author:murray, s * author:murray, s * author:murray, author:murray,*))
        //    |title:\"(stephen syn::stephen) murray\"
        //   ))
        //  )
        // +author_facet_hier:0/Murray, S

        //setDebug(true);
        assertQueryEquals(req("defType", "aqp",
                "q", "stephen murray author_facet_hier:\"0/Murray, S\"",
                "qf", "abstract title",
                "aqp.unfielded.tokens.strategy", "multiply",
                "aqp.unfielded.tokens.new.type", "simple",
                "aqp.unfielded.tokens.function.name", "edismax_combined_aqp"
                ),
                "+("
                + "(+(((abstract:stephen abstract:syn::stephen)) | ((title:stephen title:syn::stephen))) "
                +  "+(abstract:murray | title:murray)) "
                + "(abstract:\"(stephen syn::stephen) murray\" | title:\"(stephen syn::stephen) murray\")"
                + ") "
                + "+author_facet_hier:0/Murray, S",
                BooleanQuery.class
        );

        assertQueryEquals(req("defType", "aqp",
                "q", "((stephen murray)) author_facet_hier:\"0/Murray, S\"",
                "qf", "title abstract",
                "aqp.unfielded.tokens.strategy", "multiply",
                "aqp.unfielded.tokens.new.type", "simple",
                "aqp.unfielded.tokens.function.name", "edismax_combined_aqp"
                ),
                //"+((((((abstract:stephen abstract:syn::stephen)) | ((title:stephen title:syn::stephen))) (abstract:murray | title:murray))~2) (abstract:\"(stephen syn::stephen) murray\" | title:\"(stephen syn::stephen) murray\")) +author_facet_hier:0/Murray, S",
                "+("
                + "(+(((abstract:stephen abstract:syn::stephen)) | ((title:stephen title:syn::stephen))) "
                +  "+(abstract:murray | title:murray)) "
                + "(abstract:\"(stephen syn::stephen) murray\" | title:\"(stephen syn::stephen) murray\")"
                + ") "
                + "+author_facet_hier:0/Murray, S",
                BooleanQuery.class
        );
        assertQueryEquals(req("defType", "aqp",
                "q", "=(stephen murray) author_facet_hier:\"0/Murray, S\"",
                "qf", "title abstract",
                "aqp.unfielded.tokens.strategy", "multiply",
                "aqp.unfielded.tokens.new.type", "simple",
                "aqp.unfielded.tokens.function.name", "edismax_combined_aqp"
                ),
                "+(+(abstract:stephen | title:stephen) +(abstract:murray | title:murray)) +author_facet_hier:0/Murray, S",
                BooleanQuery.class
        );


        // virtual fields (their definition is in the solrconfig.xml)
        assertQueryEquals(req("defType", "aqp", "q", "full:foo"),
                "ack:foo (abstract:foo)^2.0 (title:foo)^2.0 body:foo keyword:foo",
                BooleanQuery.class
        );
        assertQueryEquals(req("defType", "aqp", "q", "full:\"foo phrase\""),
                "ack:\"foo phrase\" (abstract:\"foo phrase\")^2.0 (title:\"foo phrase\")^2.0 body:\"foo phrase\" keyword:\"foo phrase\"",
                BooleanQuery.class
        );
        assertQueryEquals(req("defType", "aqp", "q", "abs:foo"),
                "abstract:foo title:foo keyword:foo",
                BooleanQuery.class
        );


        // unbalanced brackets for functions
        assertQueryEquals(req("defType", "aqp", "q", "topn(201, ((\"foo bar\") AND database:astronomy), date asc)"),
                "SecondOrderQuery(+all:\"foo bar\" +database:astronomy, collector=SecondOrderCollectorTopN(201, info=date asc))",
                SecondOrderQuery.class);
        assertQueryEquals(req("defType", "aqp", "q", "topn(201, ((\"foo bar\") AND database:astronomy),   date asc   )"),
                "SecondOrderQuery(+all:\"foo bar\" +database:astronomy, collector=SecondOrderCollectorTopN(201, info=date asc))",
                SecondOrderQuery.class);
        assertQueryEquals(req("defType", "aqp", "q", "topn(201,(  ((\"foo bar\") AND database:astronomy)),date asc)"),
                "SecondOrderQuery(+all:\"foo bar\" +database:astronomy, collector=SecondOrderCollectorTopN(201, info=date asc))",
                SecondOrderQuery.class);


        // added ability to interactively tweak queries
        assertQueryEquals(req("defType", "aqp", "q", "tweak(collector_final_value=ARITHM_MEAN, citations(author:foo))"),
                "SecondOrderQuery(author:foo, author:foo,*, collector=SecondOrderCollectorCitedBy(cache:citations-cache))",
                SecondOrderQuery.class);


        // # 389
        // make sure the functional parsing is handling things well
        assertQueryEquals(req("defType", "aqp", "q", "topn(200, ((title:foo OR topn(10, title:bar OR title:baz))))"),
                "SecondOrderQuery(title:foo SecondOrderQuery(title:bar title:baz, collector=SecondOrderCollectorTopN(10)), collector=SecondOrderCollectorTopN(200))",
                SecondOrderQuery.class);
        assertQueryEquals(req("defType", "aqp", "q", "topn(200, ((title:foo AND topn(10, title:bar OR title:baz))))"),
                "SecondOrderQuery(+title:foo +SecondOrderQuery(title:bar title:baz, collector=SecondOrderCollectorTopN(10)), collector=SecondOrderCollectorTopN(200))",
                SecondOrderQuery.class);
        assertQueryEquals(req("defType", "aqp", "q", "topn(200, title:foo, date desc)"),
                "SecondOrderQuery(title:foo, collector=SecondOrderCollectorTopN(200, info=date desc))",
                SecondOrderQuery.class);
        assertQueryEquals(req("defType", "aqp", "q", "topn(200, (title:foo), date desc)"),
                "SecondOrderQuery(title:foo, collector=SecondOrderCollectorTopN(200, info=date desc))",
                SecondOrderQuery.class);
        assertQueryEquals(req("defType", "aqp", "q", "topn(200, \"foo bar\", \"date desc\")"),
                "SecondOrderQuery(all:\"foo bar\", collector=SecondOrderCollectorTopN(200, info=date desc))",
                SecondOrderQuery.class);


        // trendy() - what people read, it reads data from index
        assertU(addDocs("author", "muller", "reader", "bibcode1", "reader", "bibcode2"));
        assertU(addDocs("author", "muller", "reader", "bibcode2", "reader", "bibcode4"));
        assertU(addDocs("author", "muller", "reader", "bibcode5", "reader", "bibcode2"));
        assertU(commit());

        assertQueryEquals(req("defType", "aqp", "q", "trending(author:muller)"),
                "(like:bibcode1 bibcode2 bibcode2 bibcode4 bibcode5 bibcode2)^2.0",
                BoostQuery.class);


        // pos() operator
        assertQueryEquals(req("defType", "aqp", "q", "pos(author:\"Accomazzi, A\", 1, 100)"),
                "spanPosRange(spanOr([author:accomazzi, a, SpanMultiTermQueryWrapper(author:accomazzi, a*), author:accomazzi,]), 0, 100)",
                SpanPositionRangeQuery.class);

        // notice the use of modifier '=' (if it is lowercased, it means _nosyn analyzer
        // was used)
        assertQueryEquals(req("defType", "aqp", "q", "pos(=author:\"Accomazzi, A\", 1)"),
                "spanPosRange(author:accomazzi, a, 0, 1)",
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

        assertQueryEquals(req("defType", "aqp", "q", "^two"),
                "spanPosRange(spanOr([author:two,, SpanMultiTermQueryWrapper(author:two,*)]), 0, 1)",
                SpanPositionRangeQuery.class);
        assertQueryEquals(req("defType", "aqp", "q", "one ^two, j k"),
                "+all:one +spanPosRange(spanOr([author:two, j k, SpanMultiTermQueryWrapper(author:two, j k*), SpanMultiTermQueryWrapper(author:/two, j[^\\s]+/), SpanMultiTermQueryWrapper(author:/two, j[^\\s]+ k.*/), author:two, j, author:two,]), 0, 1)",
                BooleanQuery.class);
        assertQueryEquals(req("defType", "aqp", "q", "one \"^phrase, author\"", "qf", "title author"),
                "+(((author:one, author:one,*)) | title:one) +spanPosRange(spanOr([author:phrase, author, SpanMultiTermQueryWrapper(author:phrase, author *), author:phrase, a, SpanMultiTermQueryWrapper(author:phrase, a *), author:phrase,]), 0, 1)",
                BooleanQuery.class);


        // author expansion can generate regexes, so we should deal with them (actually we ignore them)
        assertQueryEquals(req("defType", "aqp", "q", "pos(author:\"Accomazzi, A. K. B.\", 1)"),
                "spanPosRange(spanOr([author:accomazzi, a k b, SpanMultiTermQueryWrapper(author:accomazzi, a k b*), SpanMultiTermQueryWrapper(author:/accomazzi, a[^\\s]+/), SpanMultiTermQueryWrapper(author:/accomazzi, a[^\\s]+ k[^\\s]+/), SpanMultiTermQueryWrapper(author:/accomazzi, a[^\\s]+ k[^\\s]+ b.*/), author:accomazzi, a, author:accomazzi,]), 0, 1)",
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
                "SecondOrderQuery(*:*, collector=SecondOrderCollectorTopN(5, info=date desc))",
                SecondOrderQuery.class);
        assertQueryEquals(req("defType", "aqp", "q", "topn(5, author:civano, \"date desc\")"),
                "SecondOrderQuery(author:civano, author:civano,*, collector=SecondOrderCollectorTopN(5, info=date desc))",
                SecondOrderQuery.class);

        // topN - added Aug2013
        assertQueryEquals(req("defType", "aqp", "q", "topn(5, *:*)"),
                "SecondOrderQuery(*:*, collector=SecondOrderCollectorTopN(5))",
                SecondOrderQuery.class);
        assertQueryEquals(req("defType", "aqp", "q", "topn(5, (foo bar))"),
                "SecondOrderQuery(+all:foo +all:bar, collector=SecondOrderCollectorTopN(5))",
                SecondOrderQuery.class);

        assertQueryEquals(req("defType", "aqp", "q", "topn(5, edismax(dog OR cat))", "qf", "title^1 abstract^0.5"),
                "SecondOrderQuery(((abstract:dog)^0.5 | title:dog) ((abstract:cat)^0.5 | title:cat), collector=SecondOrderCollectorTopN(5))",
                SecondOrderQuery.class);
        assertQueryEquals(req("defType", "aqp", "q", "topn(5, author:accomazzi)"),
                "SecondOrderQuery(author:accomazzi, author:accomazzi,*, collector=SecondOrderCollectorTopN(5))",
                SecondOrderQuery.class);
		
    /*
     * It is different if Aqp handles the boolean operations or if 
     * edismax() does it. 
     * 
     * Aqp has more control, see: https://issues.apache.org/jira/browse/SOLR-4141
     */

        assertQueryEquals(req("defType", "aqp", "q", "edismax(dog OR cat)", "qf", "title^1 abstract^0.5"), //edismax
                "((abstract:dog)^0.5 | title:dog) ((abstract:cat)^0.5 | title:cat)", BooleanQuery.class);
        assertQueryEquals(req("defType", "aqp", "q", "dog OR cat", "qf", "title^1 abstract^0.5"),          //aqp
                "((abstract:dog)^0.5 | title:dog) ((abstract:cat)^0.5 | title:cat)", BooleanQuery.class);

        assertQueryEquals(req("defType", "aqp", "q", "edismax(dog AND cat)", "qf", "title^1 abstract^0.5"), //edismax
                "+((abstract:dog)^0.5 | title:dog) +((abstract:cat)^0.5 | title:cat)", BooleanQuery.class);

        assertQueryEquals(req("defType", "aqp", "q", "dog AND cat", "qf", "title^1 abstract^0.5"), //aqp
                "+((abstract:dog)^0.5 | title:dog) +((abstract:cat)^0.5 | title:cat)", BooleanQuery.class);

        assertQueryEquals(req("defType", "aqp", "q", "edismax(dog OR cat)", "qf", "title^1 abstract^0.5"), //edismax
                "((abstract:dog)^0.5 | title:dog) ((abstract:cat)^0.5 | title:cat)", BooleanQuery.class);

        assertQueryEquals(req("defType", "aqp", "q", "dog OR cat", "qf", "title^1 abstract^0.5"), //aqp
                "((abstract:dog)^0.5 | title:dog) ((abstract:cat)^0.5 | title:cat)", BooleanQuery.class);

        assertQueryEquals(req("defType", "aqp", "q", "edismax(dog cat)", "qf", "title^1 abstract^0.5"), //edismax
                "+((abstract:dog)^0.5 | title:dog) +((abstract:cat)^0.5 | title:cat)", BooleanQuery.class);

        assertQueryEquals(req("defType", "aqp", "q", "dog cat", "qf", "title^1 abstract^0.5", "q.op", "OR"), //aqp
                "((abstract:dog)^0.5 | title:dog) ((abstract:cat)^0.5 | title:cat)", BooleanQuery.class);

        assertQueryEquals(req("defType", "aqp", "q", "dog cat", "qf", "title^1 abstract^0.5"), //aqp
                "+((abstract:dog)^0.5 | title:dog) +((abstract:cat)^0.5 | title:cat)", BooleanQuery.class);


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
        assertQueryEquals(req("defType", "aqp", "q", "references(author:foo)"),
                "SecondOrderQuery(author:foo, author:foo,*, collector=SecondOrderCollectorCitesRAM(cache:citations-cache))", SecondOrderQuery.class);


        // various searches
        assertQueryEquals(req("defType", "aqp", "q", "all:x OR all:z references(author:foo OR title:body)"),
                "+(all:x all:z) +SecondOrderQuery((author:foo, author:foo,*) title:body, collector=SecondOrderCollectorCitesRAM(cache:citations-cache))", BooleanQuery.class);
        assertQueryEquals(req("defType", "aqp", "q", "citations((title:(lectures physics) and author:Feynman))"),
                "SecondOrderQuery(+(+title:lectures +title:physics) +(author:feynman, author:feynman,*), collector=SecondOrderCollectorCitedBy(cache:citations-cache))",
                SecondOrderQuery.class);


        // citations()
        assertQueryEquals(req("defType", "aqp", "q", "citations(author:foo)"),
                "SecondOrderQuery(author:foo, author:foo,*, collector=SecondOrderCollectorCitedBy(cache:citations-cache))", SecondOrderQuery.class);


        // useful() - ads classic implementation
        assertQueryEquals(req("defType", "aqp", "q", "useful(author:foo)"),
                "SecondOrderQuery(SecondOrderQuery(SecondOrderQuery(author:foo, author:foo,*, collector=SecondOrderCollectorAdsClassicScoringFormula(cache=citations-cache, boost=float[] cite_read_boost, lucene=0.5, adsPart=0.5)), collector=SecondOrderCollectorTopN(200)), collector=SecondOrderCollectorCitesRAM(cache:citations-cache))",
                SecondOrderQuery.class);

        assertQueryEquals(req("defType", "aqp", "q", "all:(x OR z) useful(author:foo OR title:body)"),
                "+(all:x all:z) +SecondOrderQuery(SecondOrderQuery(SecondOrderQuery((author:foo, author:foo,*) title:body, collector=SecondOrderCollectorAdsClassicScoringFormula(cache=citations-cache, boost=float[] cite_read_boost, lucene=0.5, adsPart=0.5)), collector=SecondOrderCollectorTopN(200)), collector=SecondOrderCollectorCitesRAM(cache:citations-cache))",
                BooleanQuery.class);


        // useful2() - original implementation
        assertQueryEquals(req("defType", "aqp", "q", "useful2(author:foo)"),
                "SecondOrderQuery(author:foo, author:foo,*, collector=SecondOrderCollectorOperatorExpertsCiting(cache=citations-cache, boost=float[] cite_read_boost))", SecondOrderQuery.class);

        assertQueryEquals(req("defType", "aqp", "q", "all:(x OR z) useful2(author:foo OR title:body)"),
                "+(all:x all:z) +SecondOrderQuery((author:foo, author:foo,*) title:body, collector=SecondOrderCollectorOperatorExpertsCiting(cache=citations-cache, boost=float[] cite_read_boost))", BooleanQuery.class);


        // reviews() - ADS classic impl
        assertQueryEquals(req("defType", "aqp", "q", "reviews(author:foo)"),
                "SecondOrderQuery(SecondOrderQuery(SecondOrderQuery(author:foo, author:foo,*, collector=SecondOrderCollectorAdsClassicScoringFormula(cache=citations-cache, boost=float[] cite_read_boost, lucene=0.5, adsPart=0.5)), collector=SecondOrderCollectorTopN(200)), collector=SecondOrderCollectorCitedBy(cache:citations-cache))",
                SecondOrderQuery.class);

        assertQueryEquals(req("defType", "aqp", "q", "all:(x OR z) reviews(author:foo OR title:body)"),
                "+(all:x all:z) +SecondOrderQuery(SecondOrderQuery(SecondOrderQuery((author:foo, author:foo,*) title:body, collector=SecondOrderCollectorAdsClassicScoringFormula(cache=citations-cache, boost=float[] cite_read_boost, lucene=0.5, adsPart=0.5)), collector=SecondOrderCollectorTopN(200)), collector=SecondOrderCollectorCitedBy(cache:citations-cache))",
                BooleanQuery.class);

        // reviews2() - original impl
        assertQueryEquals(req("defType", "aqp", "q", "reviews2(author:foo)"),
                "SecondOrderQuery(author:foo, author:foo,*, collector=SecondOrderCollectorCitingTheMostCited(cache=citations-cache, boost=float[] cite_read_boost))", SecondOrderQuery.class);

        assertQueryEquals(req("defType", "aqp", "q", "all:(x OR z) reviews2(author:foo OR title:body)"),
                "+(all:x all:z) +SecondOrderQuery((author:foo, author:foo,*) title:body, collector=SecondOrderCollectorCitingTheMostCited(cache=citations-cache, boost=float[] cite_read_boost))", BooleanQuery.class);

        // classic_relevance() - cr()
        assertQueryEquals(req("defType", "aqp", "q", "classic_relevance(title:foo)"),
                "SecondOrderQuery(title:foo, collector=SecondOrderCollectorAdsClassicScoringFormula(cache=citations-cache, boost=float[] cite_read_boost, lucene=0.5, adsPart=0.5))", SecondOrderQuery.class);
        assertQueryEquals(req("defType", "aqp", "q", "cr(title:foo)"),
                "SecondOrderQuery(title:foo, collector=SecondOrderCollectorAdsClassicScoringFormula(cache=citations-cache, boost=float[] cite_read_boost, lucene=0.5, adsPart=0.5))", SecondOrderQuery.class);
        assertQueryEquals(req("defType", "aqp", "q", "cr(title:foo, 0.4)"),
                "SecondOrderQuery(title:foo, collector=SecondOrderCollectorAdsClassicScoringFormula(cache=citations-cache, boost=float[] cite_read_boost, lucene=0.4, adsPart=0.6))", SecondOrderQuery.class);

    }


    public void test() throws Exception {


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
        // it ends up in the unfielded_search field. Feature or a bug?
        assertQueryEquals(req("defType", "aqp", "q", "/^Kurtz, M./"),
                "unfielded_search:/^Kurtz, M./",
                RegexpQuery.class);
        // regex ignores unfielded queries even when qf is set
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
                "spanNear([(spanNear([all:acr::nasa, all:grant], 1, true))^0.9, SpanMultiTermQueryWrapper(all:n*)], 5, true)",
                SpanNearQuery.class);
        assertQueryEquals(req("defType", "aqp", "q", "\"NASA grant\"~3^0.9 NEAR N*"),
                "spanNear([(spanNear([all:acr::nasa, all:grant], 3, true))^0.9, SpanMultiTermQueryWrapper(all:n*)], 5, true)",
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
        assertQueryEquals(req("defType", "aqp", "q", "arxiv:1002.1524"),
                "identifier:1002.1524", TermQuery.class);
        assertQueryParseException(req("defType", "aqp", "q", "arxivvvv:1002.1524"));


    }

    public static junit.framework.Test suite() {
        return new junit.framework.JUnit4TestAdapter(TestAqpAdsabsSolrSearch.class);
    }


}
