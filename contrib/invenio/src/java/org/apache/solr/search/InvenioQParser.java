package org.apache.solr.search;

import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.queryparser.flexible.core.QueryNodeException;
import org.apache.lucene.queryparser.flexible.core.QueryNodeParseException;
import org.apache.lucene.queryparser.flexible.core.config.QueryConfigHandler;
import org.apache.lucene.queryparser.flexible.standard.config.DefaultOperatorAttribute.Operator;
import org.apache.lucene.queryparser.flexible.aqp.AqpInvenioQueryParser;
import org.apache.lucene.queryparser.flexible.aqp.AqpQueryParser;
import org.apache.lucene.queryparser.flexible.aqp.config.DefaultFieldAttribute;
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

		config.getAttribute(InvenioDefaultIdFieldAttribute.class).setDefaultIdField(
				idField);

		InvenioQueryAttribute iqAttr = config
				.getAttribute(InvenioQueryAttribute.class);
		iqAttr.setMode(solrParams.get("iq.mode"), schema.hasExplicitField("*"));
		iqAttr.setChannel(solrParams.get("iq.channel", "default"));
		iqAttr.setOverridenFields(solrParams.getParams("iq.xfields"));

		invParser.setAnalyzer(schema.getAnalyzer());

		String defaultField = getParam(CommonParams.DF);
		if (defaultField == null) {
			defaultField = getReq().getSchema().getDefaultSearchFieldName();
		}
		if (defaultField != null) {
			DefaultFieldAttribute defFieldAttr = config.getAttribute(DefaultFieldAttribute.class);
			defFieldAttr.setDefaultField(defaultField);
		}
		

		String opParam = getParam(QueryParsing.OP);

		if (opParam != null) {
			invParser.setDefaultOperator("AND".equals(opParam) ? Operator.AND
					: Operator.OR);
		} else {
			// try to get default operator from schema
			QueryParser.Operator operator = getReq().getSchema()
					.getSolrQueryParser(null).getDefaultOperator();
			invParser.setDefaultOperator(null == operator ? Operator.OR
					: (operator == QueryParser.AND_OPERATOR ? Operator.AND
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
