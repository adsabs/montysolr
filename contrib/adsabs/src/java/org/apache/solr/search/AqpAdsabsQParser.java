package org.apache.solr.search;

import java.util.Map;

import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.flexible.core.QueryNodeException;
import org.apache.lucene.queryparser.flexible.core.QueryNodeParseException;
import org.apache.lucene.queryparser.flexible.core.config.QueryConfigHandler;
import org.apache.lucene.queryparser.flexible.standard.config.StandardQueryConfigHandler;
import org.apache.lucene.queryparser.flexible.standard.config.StandardQueryConfigHandler.Operator;
import org.apache.lucene.queryparser.flexible.aqp.AqpAdsabsQueryParser;
import org.apache.lucene.queryparser.flexible.aqp.AqpQueryParser;
import org.apache.lucene.queryparser.flexible.aqp.builders.AqpAdsabsSubSueryProvider;
import org.apache.lucene.queryparser.flexible.aqp.builders.AqpSolrFunctionProvider;
import org.apache.lucene.queryparser.flexible.aqp.config.AqpAdsabsQueryConfigHandler;
import org.apache.lucene.queryparser.flexible.aqp.config.AqpRequestParams;
import org.apache.lucene.queryparser.flexible.aqp.config.AqpStandardQueryConfigHandler;
import org.apache.lucene.queryparser.flexible.aqp.config.AqpAdsabsQueryConfigHandler.ConfigurationKeys;
import org.apache.lucene.search.Query;
import org.apache.solr.common.SolrException;
import org.apache.solr.common.params.CommonParams;
import org.apache.solr.common.params.DefaultSolrParams;
import org.apache.solr.common.params.DisMaxParams;
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
	private static String adsConfigName = "ads-config";

	public AqpAdsabsQParser(AqpQueryParser parser, String qstr, SolrParams localParams,
			SolrParams params, SolrQueryRequest req)
			throws QueryNodeParseException {
		
		super(qstr, localParams, params, req);
		qParser = parser;

		if (getString() == null) {
			throw new SolrException(SolrException.ErrorCode.BAD_REQUEST,
					"The query is empty");
		}


		IndexSchema schema = req.getSchema();

		
		// now configure the parser using the arguments given in config
		// and also in the request
		
		
		QueryConfigHandler config = qParser.getQueryConfigHandler();
		
		
		AdsConfigHandler extra = (AdsConfigHandler) req.getCore().getRequestHandler(adsConfigName);
		
		
		if (extra == null) {
			throw new SolrException(SolrException.ErrorCode.SERVER_ERROR,
				"Configuration error, ads-config resource missing");
		}
		SolrParams parserConfig = extra.getParams("queryParser");
		
		qParser.setAnalyzer(schema.getAnalyzer());

		String defaultField = getParam(CommonParams.DF);
		if (defaultField == null) {
			defaultField = parserConfig.get("defaultField", getReq().getSchema().getDefaultSearchFieldName());
		}
		
		if (defaultField != null) {
			config.set(AqpStandardQueryConfigHandler.ConfigurationKeys.DEFAULT_FIELD, defaultField);
		}
		
		
		String opParam = getParam(QueryParsing.OP);
		if (opParam == null) {
			opParam = parserConfig.get("defaultOperator", getReq().getSchema().getQueryParserDefaultOperator());
		}

		if (opParam != null) {
			qParser.setDefaultOperator("AND".equals(opParam.toUpperCase()) ? Operator.AND
					: Operator.OR);
		} else {
			throw new SolrException(SolrException.ErrorCode.BAD_REQUEST,
			"The defaultOperator is set to null");
		}
		
		if (parserConfig.get("fieldMap", null) != null) {
			String[] fields = parserConfig.get("fieldMap").split(";");
			Map<String, String> fieldMap = config.get(AqpStandardQueryConfigHandler.ConfigurationKeys.FIELD_MAPPER);
			String ffs[];
			for (String f: fields) {
				ffs = f.split("\\s+");
				if (ffs.length < 2) {
					throw new SolrException(SolrException.ErrorCode.SERVER_ERROR,
					"Configuration error inside " + adsConfigName + ", in the section: fieldMap.");
				}
				String target = ffs[ffs.length-1];
				for (int i=0;i<ffs.length-1;i++) {
					fieldMap.put(ffs[i], target);
				}
			}
		}
		
		AqpRequestParams reqAttr = config.get(AqpAdsabsQueryConfigHandler.ConfigurationKeys.SOLR_REQUEST);
		reqAttr.setQueryString(getString());
		reqAttr.setRequest(req);
		reqAttr.setLocalParams(localParams);
		reqAttr.setParams(params);
		
		
		// now add the special analyzer that knows to use solr token chains
		config.set(StandardQueryConfigHandler.ConfigurationKeys.ANALYZER, req.getSchema().getQueryAnalyzer());
		
		config.set(AqpAdsabsQueryConfigHandler.ConfigurationKeys.SOLR_READY, true);
		
		// special analyzers
		config.get(ConfigurationKeys.FUNCTION_QUERY_BUILDER_CONFIG).addProvider(new AqpSolrFunctionProvider());
		config.get(ConfigurationKeys.FUNCTION_QUERY_BUILDER_CONFIG).addProvider(new AqpAdsabsSubSueryProvider());
		
		config.get(AqpAdsabsQueryConfigHandler.ConfigurationKeys.AUTHOR_FIELDS).put("author", new int[0]);
		
		
		if (params.getBool("debugQuery", false) != false) {
			try {
				qParser.setDebug(true);
			} catch (InstantiationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		
		
	}

	public Query parse() throws ParseException {
		try {
			return qParser.parse(getString(), null);
		} catch (QueryNodeException e) {
			throw new ParseException(e.getLocalizedMessage());
		}
		catch (SolrException e1) {
			throw new ParseException(e1.getLocalizedMessage());
		}
	}

	public AqpQueryParser getParser() {
		return qParser;
	}
}
