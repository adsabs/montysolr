package org.apache.solr.search;

import org.apache.lucene.queryparser.flexible.core.QueryNodeParseException;
import org.apache.lucene.queryparser.flexible.aqp.AqpAdsabsQueryParser;
import org.apache.lucene.queryparser.flexible.aqp.AqpQueryParser;
import org.apache.solr.common.SolrException;
import org.apache.solr.common.params.SolrParams;
import org.apache.solr.common.util.NamedList;
import org.apache.solr.request.SolrQueryRequest;

/**
 * An instance of absolute crazy (but fun to work with) query parser.
 * Implements the ADS grammar together with lots of business logic.
 * 
 */
public class AdsQParserPlugin extends QParserPlugin {
	public static String NAME = "aqp";
	
	public void init(NamedList args) {
		//System.out.println(args);
	}

	
	public QParser createParser(String qstr, SolrParams localParams,
			SolrParams params, SolrQueryRequest req) {
		try {
			// TODO: optimization -- keep the instance of the parser 
			// and instantiate only the syntax parser
			AqpQueryParser parser = AqpAdsabsQueryParser.init();
			return new AqpAdsabsQParser(parser, qstr, localParams, params, req);
		} catch (QueryNodeParseException e) {
			throw new SolrException(SolrException.ErrorCode.SERVICE_UNAVAILABLE, e.getLocalizedMessage());
		} catch (Exception e) {
			throw new SolrException(SolrException.ErrorCode.SERVICE_UNAVAILABLE, e.getLocalizedMessage());
		}
	}
}

