package org.apache.solr.search;

import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.queryparser.flexible.core.QueryNodeException;
import org.apache.lucene.queryparser.flexible.core.QueryNodeParseException;
import org.apache.lucene.queryparser.flexible.core.config.QueryConfigHandler;
import org.apache.lucene.queryparser.flexible.standard.config.StandardQueryConfigHandler.Operator;
import org.apache.lucene.queryparser.flexible.aqp.AqpInvenioQueryParser;
import org.apache.lucene.queryparser.flexible.aqp.AqpQueryParser;
import org.apache.lucene.queryparser.flexible.aqp.config.AqpInvenioQueryConfigHandler;
import org.apache.lucene.queryparser.flexible.aqp.config.InvenioDefaultIdFieldAttribute;
import org.apache.lucene.queryparser.flexible.aqp.config.InvenioQueryAttribute;
import org.apache.lucene.search.Query;
import org.apache.solr.common.SolrException;
import org.apache.solr.common.params.CommonParams;
import org.apache.solr.common.params.DefaultSolrParams;
import org.apache.solr.common.params.SolrParams;
import org.apache.solr.request.SolrQueryRequest;
import org.apache.solr.schema.IndexSchema;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



public class InvenioQParser extends QParser {
	public static final Logger log = LoggerFactory
			.getLogger(InvenioQParser.class);

	private AqpQueryParser invParser;

	public InvenioQParser(String qstr, SolrParams localParams,
			SolrParams params, SolrQueryRequest req, String idField)
			throws QueryNodeParseException {
		super(qstr, localParams, params, req);

		if (getString() == null) {
			throw new SolrException(SolrException.ErrorCode.BAD_REQUEST,
					"The query is empty");
		}

		SolrParams solrParams = localParams == null ? params
				: new DefaultSolrParams(localParams, params);

		IndexSchema schema = req.getSchema();
		invParser = AqpInvenioQueryParser.init();

		// this is allowed to be only in the config (no locals possible)
		QueryConfigHandler config = invParser.getQueryConfigHandler();

		config.set(AqpInvenioQueryConfigHandler.ConfigurationKeys.INVENIO_DEFAULT_ID_FIELD, idField);

		InvenioQueryAttribute iqAttr = config
				.get(AqpInvenioQueryConfigHandler.ConfigurationKeys.INVENIO_QUERY);
		iqAttr.setMode(solrParams.get("iq.mode"), schema.hasExplicitField("*"));
		iqAttr.setChannel(solrParams.get("iq.channel", "default"));
		iqAttr.setOverridenFields(solrParams.getParams("iq.xfields"));

		invParser.setAnalyzer(schema.getAnalyzer());

		String defaultField = getParam(CommonParams.DF);
		if (defaultField == null) {
			defaultField = getReq().getSchema().getDefaultSearchFieldName();
		}
		if (defaultField != null) {
			config.set(AqpInvenioQueryConfigHandler.ConfigurationKeys.INVENIO_DEFAULT_ID_FIELD, defaultField);
		}
		

		String opParam = getParam(QueryParsing.OP);

		if (opParam != null) {
			invParser.setDefaultOperator("AND".equals(opParam) ? Operator.AND
					: Operator.OR);
		} else {
		  QueryParser.Operator op = QueryParsing.getQueryParserDefaultOperator(getReq().getSchema(), 
          getParam(QueryParsing.OP));
			invParser.setDefaultOperator(null == op ? Operator.OR
					: (op == QueryParser.AND_OPERATOR ? Operator.AND
							: Operator.OR));
		}
	}
	
	public Query parse() throws ParseException {
		try {
			return invParser.parse(getString(), null);
		} catch (QueryNodeException e) {
			throw new ParseException(e.getLocalizedMessage());
		}
	}

	public AqpQueryParser getParser() {
		return invParser;
	}

}
