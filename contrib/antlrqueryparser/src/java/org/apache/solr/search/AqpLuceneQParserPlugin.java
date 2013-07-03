package org.apache.solr.search;

import org.apache.lucene.queryparser.flexible.aqp.AqpQueryParser;
import org.apache.lucene.queryparser.flexible.aqp.builders.AqpNearQueryNodeBuilder;
import org.apache.lucene.queryparser.flexible.aqp.builders.AqpQueryTreeBuilder;
import org.apache.lucene.queryparser.flexible.aqp.nodes.AqpNearQueryNode;
import org.apache.lucene.queryparser.flexible.aqp.parser.AqpStandardLuceneParser;
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
	
	public void init(NamedList args) {
		//pass
	}

	
	public QParser createParser(String qstr, SolrParams localParams,
			SolrParams params, SolrQueryRequest req) {
		try {
			AqpQueryParser parser = AqpStandardLuceneParser.init("ExtendedLuceneGrammar");
			((AqpQueryTreeBuilder) parser.getQueryBuilder()).setBuilder(AqpNearQueryNode.class,	new AqpNearQueryNodeBuilder());
			return new AqpLuceneQParser(parser, qstr, localParams, params, req);
		} catch (QueryNodeParseException e) {
			throw new SolrException(SolrException.ErrorCode.SERVICE_UNAVAILABLE, e.getLocalizedMessage());
		} catch (Exception e) {
			throw new SolrException(SolrException.ErrorCode.SERVICE_UNAVAILABLE, e.getLocalizedMessage());
		}
	}
}
