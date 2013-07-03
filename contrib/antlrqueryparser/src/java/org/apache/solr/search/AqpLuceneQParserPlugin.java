package org.apache.solr.search;

import org.apache.lucene.queryparser.flexible.aqp.AqpQueryParser;
import org.apache.lucene.queryparser.flexible.aqp.parser.AqpExtendedLuceneParser;
import org.apache.lucene.queryparser.flexible.core.QueryNodeParseException;
import org.apache.solr.common.SolrException;
import org.apache.solr.common.params.SolrParams;
import org.apache.solr.common.util.NamedList;
import org.apache.solr.request.SolrQueryRequest;

/**
 * An instance of ANTLR query parser. Implements the 
 * standard lucene grammar together with support for 
 * span queries.
 */

public class AqpLuceneQParserPlugin extends QParserPlugin {
	public static String NAME = "lucene2";
	
	private String defaultField = "all";
	
	@SuppressWarnings("rawtypes")
  public void init(NamedList args) {
		NamedList defaults = (NamedList) args.get("defaults");
		if (defaults != null) {
			if (defaults.get("defaultField") != null) {
				defaultField = (String) defaults.get("defaultField"); 
			}
		}
	}

	
	public QParser createParser(String qstr, SolrParams localParams,
			SolrParams params, SolrQueryRequest req) {
		try {
			AqpQueryParser parser = AqpExtendedLuceneParser.init();
			
			parser.setDefaultField(defaultField);
			
			return new AqpLuceneQParser(parser, qstr, localParams, params, req);
		} catch (QueryNodeParseException e) {
			throw new SolrException(SolrException.ErrorCode.SERVICE_UNAVAILABLE, e.getLocalizedMessage());
		} catch (Exception e) {
			throw new SolrException(SolrException.ErrorCode.SERVICE_UNAVAILABLE, e.getLocalizedMessage());
		}
	}
}
