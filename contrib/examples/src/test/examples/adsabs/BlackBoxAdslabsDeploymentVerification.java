package examples.adsabs;

import java.util.ArrayList;
import java.util.List;

import org.apache.solr.common.params.CommonParams;
import org.apache.solr.common.util.ContentStream;
import org.apache.solr.common.util.ContentStreamBase;
import org.apache.solr.core.SolrCore;
import org.apache.solr.request.SolrQueryRequestBase;
import org.apache.solr.response.SolrQueryResponse;
import org.junit.BeforeClass;

import examples.BlackAbstractTestCase;

/*
 * this needs INVENIO with ADS Data
 */

public class BlackBoxAdslabsDeploymentVerification extends BlackAbstractTestCase{
	
	@BeforeClass
	public static void beforeBlackBoxAdslabsIndexing() throws Exception {
		setEName("adsabs");
		exampleInit();
	}
	
	@Override
	public void setUp() throws Exception {
		super.setUp();
	}
	
	@Override
	  public void tearDown() throws Exception {
	  //FieldCache.DEFAULT.purgeAllCaches();
    super.tearDown();
	}
	
	@SuppressWarnings("unused")
  public void testUpdates() throws Exception {
		SolrCore core = h.getCore();
		String data;
		
		// add some empty docs
		assertU(adoc("id","1","recid","1", "bibcode", "b1"));
		assertU(adoc("id","2","recid","2", "bibcode", "b2", "reference", "b2", "reference", "b3", "reference", "b4"));
		assertU(adoc("id","3","recid","3", "bibcode", "b3"));
		assertU(adoc("id","4","recid","4", "bibcode", "b4"));
		assertU(adoc("id","5","recid","5", "bibcode", "b5", "alternate_bibcode", "x5"));
		
		assertU(adoc("id","6","recid","6", "bibcode", "b6"));
		assertU(adoc("id","6","recid","6", "bibcode", "b6", "title", "all-sky", "abstract", "all-sky"));
		
		assertU(adoc("id","10","recid","10", "bibcode", "b10", "citation", "b11"));
		assertU(adoc("id","11","recid","11", "bibcode", "b11", "citation", "b10"));
		assertU(adoc("id","12","recid","12", "bibcode", "b12", "citation", "b10", "reference", "b11"));
		
		assertU(commit("waitSearcher", "true"));
		
	  // the first search is not auto-warmed (the code seems
		// that seems like a SOLR bug (I checked the SolrIndexSearcher
		// code and it is right; so i created own function for 
		// warming warm_cache()
		// assertU(commit());  
		
		assertQ(req("q", "*:*"),"//*[@numFound>'6']");
		assertQ(req("q", "id:*"),"//*[@numFound>'6']");
		
		SolrQueryRequestBase req = (SolrQueryRequestBase) req(
				"qt", "/bigquery",
				"q","id:*", 
				"fq","{!bitset compression=none}");
		List<ContentStream> streams = new ArrayList<ContentStream>(1);
    ContentStreamBase cs = new ContentStreamBase.StringStream("bibcode\nb2\nx5");
    cs.setContentType("big-query/csv");
    streams.add(cs);
		req.setContentStreams(streams);
		
		assertQ(req
				,"//*[@numFound='2']",
				"//doc/str[@name='id'][.='2']",
				"//doc/str[@name='id'][.='5']"
		);
		
		
		// these queries will not find anything, but we can test the proper
		// search behaviour (the extensive tests are in separate classes,
		// here we just make sure that handful of queries is expanded/transformed
		// properly (that all synonym files are at place etc) for MÜLLER
		data = direct.request("/select?q=M%C3%9CLLER&debugQuery=true&wt=json", null);
		
		data.contains("(abstract:acr::müller abstract:acr::muller)^1.3)");
		data.contains("(author:müller, author:müller,* author:mueller, author:mueller,* author:muller, author:muller,*)^2.0");
		data.contains("DisjunctionMaxQuery(");
		
		data = direct.request("/select?q=accomazzi&debugQuery=false&wt=json", null);
		data.contains("abstract:accomazzi");
		data.contains("title:accomazzi");
		data.contains("full:accomazzi");
		data.contains("abstract:accomazzi");
		data.contains("keyword:accomazzi");
		data.contains("acc:accomazzi");
		
		
		
		// index some (random) docs and check we got them
		SolrQueryResponse rsp = new SolrQueryResponse();
		

		// TODO: index somthing
		assertU(commit("waitSearcher", "true"));
		
		
		
		
		
		
	  // #231 - use 'aqp' as a default parser also for filter queries
    assert direct.request("/select?q=*:*&fq={!aqp}author:\"Civano, F\"&debugQuery=true&wt=json", null)
      .contains("author:civano, f author:civano, f* author:civano,");
    assert direct.request("/select?q=*:*&fq=author:\"Civano, F\"&debugQuery=true&wt=json", null)
      .contains("author:civano, f author:civano, f* author:civano,");
    
    // check we are using synonym for translation
    data = direct.request("/select?q=AAS&debugQuery=true&wt=json", null);
    assert data.contains("title:syn::american astronomical society");
    assert data.contains("title:acr::aas");
    
    data = direct.request("/select?q=aborigines&debugQuery=true&wt=json", null);
    assert data.contains("title:syn::aboriginal");
    
    data = direct.request("/select?q=\"STERN, CAROLYN P\"&debugQuery=true&wt=json", null);
    assert data.contains("author:stern grant, c");
    
    /*
		 * /tvrh is available and serves title+abstract
		 */
    if (false) { // depends on vectors being stored, which we disabled
  		assertQ(req(CommonParams.QT, "/tvrh", 
  				"q", "abstract:all-sky",
  				"fl", "recid",
  				"tv", "true",
  				"tv.all", "true",
  				"tv.fl", "abstract",
  				"indent", "true"
  		),
  		"//lst[@name='termVectors']/lst/lst[@name='abstract']/lst[@name='allsky']/int[@name='tf']",
  		"//lst[@name='termVectors']/lst/lst[@name='abstract']/lst[@name='allsky']/int[@name='df']",
  		"//lst[@name='termVectors']/lst/lst[@name='abstract']/lst[@name='allsky']/double[@name='tf-idf']",
  		"//lst[@name='termVectors']/lst/lst[@name='abstract']/lst[@name='allsky']/lst[@name='positions']/int[@name='position']",
  		"//lst[@name='termVectors']/lst/lst[@name='abstract']/lst[@name='allsky']/lst[@name='offsets']/int[@name='start']",
  		"//lst[@name='termVectors']/lst/lst[@name='abstract']/lst[@name='allsky']/lst[@name='offsets']/int[@name='end']",
  		"//*[@numFound='1']");
  		assertQ(req(CommonParams.QT, "/tvrh", 
  				"q", "title:all-sky",
  				"fl", "recid,title",
  				"tv", "true",
  				"tv.all", "true",
  				"tv.fl", "title",
  				"indent", "true"
  		),
  		"//lst[@name='termVectors']/lst/lst[@name='title']/lst[@name='allsky']/int[@name='tf']",
  		"//lst[@name='termVectors']/lst/lst[@name='title']/lst[@name='allsky']/int[@name='df']",
  		"//lst[@name='termVectors']/lst/lst[@name='title']/lst[@name='allsky']/double[@name='tf-idf']",
  		"//lst[@name='termVectors']/lst/lst[@name='title']/lst[@name='allsky']/lst[@name='positions']/int[@name='position']",
  		"//lst[@name='termVectors']/lst/lst[@name='title']/lst[@name='allsky']/lst[@name='offsets']/int[@name='start']",
  		"//lst[@name='termVectors']/lst/lst[@name='title']/lst[@name='allsky']/lst[@name='offsets']/int[@name='end']",
  		"//*[@numFound='1']");
  				
  		
  		// test we can retrieve citations data (from the cache)
  		assertQ(req("q", "bibcode:b10", 
  				"fl", "recid,[citations values=citations,references resolve=true]",
  				"indent", "true"), 
  				"//*[@numFound='1']",
  				"//doc/lst[@name='[citations]']/int[@name='num_references'][.='2']",
  				"//doc/lst[@name='[citations]']/arr[@name='references']/str[1][.='b11']",
  				"//doc/lst[@name='[citations]']/arr[@name='references']/str[2][.='b12']",
  
  				"//doc/lst[@name='[citations]']/int[@name='num_citations'][.='1']",
  				"//doc/lst[@name='[citations]']/arr[@name='citations']/str[1][.='b11']"
  		);
    }
		
		//data = direct.request("/select?q=author:goodman&debugQuery=true&wt=json", null);
		//System.out.println(data);
		
		data = direct.request("/replicoordinator?hostid=foo&wt=json", null);
		assert data.contains("maxDelay");
		assert data.contains("cycles");
		
		data = direct.request("/replicoordinator?hostid=foo&event=give-me-delay&wt=json", null);
		assert data.contains("delay");
		
		
		System.out.println(direct.request("/replication?command=indexversion&wt=json", null));
		System.out.println(direct.request("/replication?command=filelist&generation=2&wt=json&indent=true", null));
		
	}
	
	
	
	
	// Uniquely for Junit 3
	public static junit.framework.Test suite() {
        return new junit.framework.JUnit4TestAdapter(BlackBoxAdslabsDeploymentVerification.class);
    }
}
