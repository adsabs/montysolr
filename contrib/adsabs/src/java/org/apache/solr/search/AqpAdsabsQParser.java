package org.apache.solr.search;

import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.aqp.AqpQueryParser;
import org.apache.lucene.queryParser.aqp.config.DefaultFieldAttribute;
import org.apache.lucene.queryParser.core.QueryNodeException;
import org.apache.lucene.queryParser.core.QueryNodeParseException;
import org.apache.lucene.queryParser.core.config.QueryConfigHandler;
import org.apache.lucene.queryParser.standard.config.DefaultOperatorAttribute.Operator;
import org.apache.lucene.search.Query;
import org.apache.solr.common.SolrException;
import org.apache.solr.common.params.CommonParams;
import org.apache.solr.common.params.DefaultSolrParams;
import org.apache.solr.common.params.SolrParams;
import org.apache.solr.handler.AdsConfigHandler;
import org.apache.solr.request.SolrQueryRequest;
import org.apache.solr.schema.IndexSchema;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AqpAdsabsQParser extends QParser {

	public static final Logger log = LoggerFactory
			.getLogger(AqpAdsabsQParser.class);

	private AqpQueryParser qParser;

	public AqpAdsabsQParser(AqpQueryParser parser, String qstr, SolrParams localParams,
			SolrParams params, SolrQueryRequest req)
			throws QueryNodeParseException {
		
		super(qstr, localParams, params, req);
		qParser = parser;

		if (getString() == null) {
			throw new SolrException(SolrException.ErrorCode.BAD_REQUEST,
					"The query is empty");
		}

		SolrParams solrParams = localParams == null ? params
				: new DefaultSolrParams(localParams, params);

		IndexSchema schema = req.getSchema();

		
		// now configure the parser using the arguments given in config
		// and also in the request
		
		
		QueryConfigHandler config = qParser.getQueryConfigHandler();
		AdsConfigHandler extra = (AdsConfigHandler) req.getCore().getRequestHandler("ads-config");
		if (extra == null) {
			throw new SolrException(SolrException.ErrorCode.SERVER_ERROR,
				"Configuration error, ads-config resource missing");
		}
		SolrParams xmlConfig = extra.getParams("queryParser");
		
		qParser.setAnalyzer(schema.getAnalyzer());

		String defaultField = getParam(CommonParams.DF);
		if (defaultField == null) {
			defaultField = xmlConfig.get("defaultField", getReq().getSchema().getDefaultSearchFieldName());
		}
		if (defaultField != null) {
			DefaultFieldAttribute defFieldAttr = config
					.getAttribute(DefaultFieldAttribute.class);
			defFieldAttr.setDefaultField(defaultField);
		}

		String opParam = getParam(QueryParsing.OP);
		if (opParam == null) {
			opParam = xmlConfig.get("defaultOperator", getReq().getSchema()
					.getSolrQueryParser(null).getDefaultOperator().toString());
		}

		if (opParam != null) {
			qParser.setDefaultOperator("AND".equals(opParam.toUpperCase()) ? Operator.AND
					: Operator.OR);
		} else {
			throw new SolrException(SolrException.ErrorCode.BAD_REQUEST,
			"The defaultOperator is set to null");
		}
	}

	public Query parse() throws ParseException {
		try {
			return qParser.parse(getString(), null);
		} catch (QueryNodeException e) {
			throw new ParseException(e.getLocalizedMessage());
		}
	}

	public AqpQueryParser getParser() {
		return qParser;
	}
}
