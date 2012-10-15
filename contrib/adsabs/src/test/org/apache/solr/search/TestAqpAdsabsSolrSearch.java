package org.apache.solr.search;


import java.io.File;
import java.io.IOException;

import monty.solr.util.MontySolrQueryTestCase;
import monty.solr.util.MontySolrSetup;

import org.apache.lucene.queryparser.flexible.aqp.TestAqpAdsabs;
import org.apache.lucene.search.BooleanQuery;
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
		
		
		assertQueryEquals(req("qt", "aqp", "q", "edismax(dog OR cat)"), 
				"+((all:dog) (all:cat))", BooleanQuery.class);
		assertQueryEquals(req("qt", "aqp", "q", "edismax(dog AND cat)"), 
				"+(+(all:dog) +(all:cat))", BooleanQuery.class);
		assertQueryEquals(req("qt", "aqp", "q", "edismax(frank bank)"), 
				"+(((all:frank) (all:bank))~2)", BooleanQuery.class);
		
		assertQueryEquals(req("qt", "aqp", "q", "edismax(dog OR cat) OR bat"), 
				"(+((all:dog) (all:cat))) all:bat", BooleanQuery.class);
		
		assertQueryEquals(req("qt", "aqp", "q", "edismax(dog AND cat) AND bat"), 
				"+(+(+(all:dog) +(all:cat))) +all:bat", BooleanQuery.class);
		assertQueryEquals(req("qt", "aqp", "q", "edismax(frank bank) bat"), 
				"(+(((all:frank) (all:bank))~2)) all:bat", BooleanQuery.class);
		
		//  {!raw f=myfield}Foo Bar creates TermQuery(Term("myfield","Foo Bar"))
		assertQueryEquals(req("qt", "aqp", "f", "myfield", "q", "raw({!f=myfield}Foo Bar)"), "myfield:Foo Bar", TermQuery.class);
		assertQueryEquals(req("qt", "aqp", "f", "myfield", "q", "raw({!f=x}\"Foo Bar\")"), "x:\"Foo Bar\"", TermQuery.class);
		
		assertQueryParseException(req("qt", "aqp", "f", "myfield", "q", "raw(Foo Bar)"));
		
		
		// if we use the solr analyzer to parse the query, all is configured to remove stopwords 
		assertQueryEquals(req("qt", "aqp", "q", "edismax(dog OR cat) OR title:bat all:but"), 
				"(+((all:dog) (all:cat))) title:bat", BooleanQuery.class);
		
		// but pub is normalized_string with a different analyzer and should retain 'but'
		assertQueryEquals(req("qt", "aqp", "q", "edismax(dog OR cat) OR title:bat OR pub:but"), 
				"(+((all:dog) (all:cat))) title:bat pub:but", BooleanQuery.class);
		
		
		
		
		
		// field map is set to translate arxiv->identifier
		assertQueryEquals(req("qt", "aqp", "q", "arxiv:1002.1524"), 
				"identifier:1002.1524", TermQuery.class);
		assertQueryParseException(req("qt", "aqp", "q", "arxivvvv:1002.1524"));
		
		
		// new function queries, the 2nd order citation operators
		
		assertQueryEquals(req("qt", "aqp", "q", "x OR z cites(author:muller OR title:body)"), 
				"(all:x all:z) SecondOrderQuery((author:muller author:muller,*) title:body, filter=null, collector=cites[using_cache:reference])", BooleanQuery.class);
		
		assertQueryEquals(req("qt", "aqp", "q", "cites(author:muller)"), 
        "SecondOrderQuery(author:muller author:muller,*, filter=null, collector=cites[using_cache:reference])", SecondOrderQuery.class);
		
		assertQueryEquals(req("qt", "aqp", "q", "refersto(author:muller)"), 
				"SecondOrderQuery(author:muller author:muller,*, filter=null, collector=citedby[using:reference<bibcode>])", SecondOrderQuery.class);
		
		assertQueryEquals(req("qt", "aqp", "q", "x OR z refersto(author:muller OR title:body)"), 
				"(all:x all:z) SecondOrderQuery((author:muller author:muller,*) title:body, filter=null, collector=citedby[using:reference<bibcode>])", BooleanQuery.class);
		
		
		
	}
	
	public static junit.framework.Test suite() {
        return new junit.framework.JUnit4TestAdapter(TestAqpAdsabsSolrSearch.class);
    }
	
	
}
