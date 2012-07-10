package org.apache.solr.search;

import java.util.HashMap;

import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryparser.flexible.core.QueryNodeException;
import org.apache.lucene.queryparser.flexible.core.QueryNodeParseException;
import org.apache.lucene.queryparser.flexible.core.config.QueryConfigHandler;
import org.apache.lucene.queryparser.flexible.standard.config.AnalyzerAttribute;
import org.apache.lucene.queryparser.flexible.standard.config.DefaultOperatorAttribute.Operator;
import org.apache.lucene.queryparser.flexible.aqp.AqpQueryParser;
import org.apache.lucene.queryparser.flexible.aqp.config.AqpFieldMapper;
import org.apache.lucene.queryparser.flexible.aqp.config.AqpRequestParams;
import org.apache.lucene.queryparser.flexible.aqp.config.DefaultFieldAttribute;
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

		SolrParams solrParams = localParams == null ? params
				: new DefaultSolrParams(localParams, params);

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
			DefaultFieldAttribute defFieldAttr = config
					.getAttribute(DefaultFieldAttribute.class);
			defFieldAttr.setDefaultField(defaultField);
		}

		String opParam = getParam(QueryParsing.OP);
		if (opParam == null) {
			opParam = parserConfig.get("defaultOperator", getReq().getSchema()
					.getSolrQueryParser(null).getDefaultOperator().toString());
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
			HashMap<String, String> fieldMap = new HashMap<String, String>();
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
			config.getAttribute(AqpFieldMapper.class).setMap(fieldMap);
		}
		
		AqpRequestParams reqAttr = config.getAttribute(AqpRequestParams.class);
		reqAttr.setQueryString(getString());
		reqAttr.setRequest(req);
		reqAttr.setLocalParams(localParams);
		reqAttr.setParams(params);
		
		
		// now add the special analyzer that knows to use solr token chains
		config.getAttribute(AnalyzerAttribute.class).setAnalyzer(req.getSchema().getQueryAnalyzer());
		
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
