package invenio.montysolr.examples;

import invenio.montysolr.jni.PythonMessage;
import invenio.montysolr.jni.MontySolrVM;

import org.apache.solr.handler.RequestHandlerBase;
import org.apache.solr.request.SolrQueryRequest;
import org.apache.solr.response.SolrQueryResponse;


public class TwitterAPIHandler extends RequestHandlerBase{

	  @Override
	  public void handleRequestBody(SolrQueryRequest req, SolrQueryResponse rsp)
	  {

	    long start = System.currentTimeMillis();

		PythonMessage message = MontySolrVM.INSTANCE
			.createMessage("twitter_api")
			.setSender(this.getClass().getSimpleName())
			.setSolrQueryRequest(req)
			.setSolrQueryResponse(rsp);

		MontySolrVM.INSTANCE.sendMessage(message);

	    long end = System.currentTimeMillis();

	    rsp.add( "QTime", end-start);
	  }


	  //////////////////////// SolrInfoMBeans methods //////////////////////

	  @Override
	  public String getVersion() {
	    return "";
	  }

	  @Override
	  public String getDescription() {
	    return "Adds new Tweets each time search term is passed";
	  }

	  @Override
	  public String getSourceId() {
	    return "";
	  }

	  @Override
	  public String getSource() {
	    return "";
  }


}
