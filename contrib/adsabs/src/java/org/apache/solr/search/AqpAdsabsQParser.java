package org.apache.solr.search;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.lucene.document.FieldType.NumericType;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.flexible.core.QueryNodeException;
import org.apache.lucene.queryparser.flexible.core.QueryNodeParseException;
import org.apache.lucene.queryparser.flexible.core.config.QueryConfigHandler;
import org.apache.lucene.queryparser.flexible.standard.config.NumberDateFormat;
import org.apache.lucene.queryparser.flexible.standard.config.NumericConfig;
import org.apache.lucene.queryparser.flexible.standard.config.StandardQueryConfigHandler;
import org.apache.lucene.queryparser.flexible.standard.config.StandardQueryConfigHandler.Operator;
import org.apache.lucene.queryparser.flexible.aqp.AqpAdsabsQueryTreeBuilder;
import org.apache.lucene.queryparser.flexible.aqp.AqpQueryParser;
import org.apache.lucene.queryparser.flexible.aqp.builders.AqpAdsabsFunctionProvider;
import org.apache.lucene.queryparser.flexible.aqp.builders.AqpAdsabsSubQueryProvider;
import org.apache.lucene.queryparser.flexible.aqp.builders.AqpSolrFunctionProvider;
import org.apache.lucene.queryparser.flexible.aqp.config.AqpAdsabsQueryConfigHandler;
import org.apache.lucene.queryparser.flexible.aqp.config.AqpRequestParams;
import org.apache.lucene.queryparser.flexible.aqp.config.AqpAdsabsQueryConfigHandler.ConfigurationKeys;
import org.apache.lucene.queryparser.flexible.aqp.parser.AqpStandardQueryConfigHandler;
import org.apache.lucene.search.Query;
import org.apache.solr.common.SolrException;
import org.apache.solr.common.params.CommonParams;
import org.apache.solr.common.params.SolrParams;
import org.apache.solr.handler.AdsConfigHandler;
import org.apache.solr.request.SolrQueryRequest;
import org.apache.solr.schema.DateField;
import org.apache.solr.schema.IndexSchema;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This is the MAIN solr entry point - this instantiates 'aqp' query 
 * parser - it sets some default parameters from the config and prepares 
 * ulr parameters.
 * 
 * @see AdsQParserPlugin
 * @see AqpAdsabsQueryConfigHandler
 * @see AqpAdsabsQueryTreeBuilder
 * @see AqpAdsabsQParser
 *
 */
public class AqpAdsabsQParser extends QParser {

	public static final Logger log = LoggerFactory
			.getLogger(AqpAdsabsQParser.class);

	private AqpQueryParser qParser;
	private static String adsConfigName = "/ads-config";

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
		
		// if defaultField was set, this will be useless
		config.set(AqpAdsabsQueryConfigHandler.ConfigurationKeys.UNFIELDED_SEARCH_FIELD, "unfielded_search");
		
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
		
		Map<String, String> fieldMap;
		for (String fName: new String[]{"fieldMap", "fieldMapPostAnalysis"}) {
		  if (fName.equals("fieldMap")) { // booo
		    fieldMap = config.get(AqpStandardQueryConfigHandler.ConfigurationKeys.FIELD_MAPPER);
		  }
		  else {
		    fieldMap = config.get(AqpStandardQueryConfigHandler.ConfigurationKeys.FIELD_MAPPER_POST_ANALYSIS);
		  }
		  
  		if (parserConfig.get(fName, null) != null) {
  			String[] fields = parserConfig.get(fName).split(";");
  			String ffs[];
  			for (String f: fields) {
  				ffs = f.split("\\s+");
  				if (ffs.length < 2) {
  					throw new SolrException(SolrException.ErrorCode.SERVER_ERROR,
  					"Configuration error inside " + adsConfigName + ", in the section: " + fName);
  				}
  				String target = ffs[ffs.length-1];
  				for (int i=0;i<ffs.length-1;i++) {
  					fieldMap.put(ffs[i], target);
  				}
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
		
		// pass the named parameters from solr request object
		Map<String, String> namedParams = config.get(AqpStandardQueryConfigHandler.ConfigurationKeys.NAMED_PARAMETER);
		if (params != null) {
			for (Entry<String, Object> par: params.toNamedList()) {
				String k = par.getKey();
				if (k.startsWith("aqp.")) {
					namedParams.put(k, (String) par.getValue());
				}
			}
		}
		if (localParams != null) {
			for (Entry<String, Object> par: localParams.toNamedList()) {
				String k = par.getKey();
				if (k.startsWith("aqp.")) {
					namedParams.put(k, (String) par.getValue());
				}
			}
		}
		
		// special analyzers
		config.get(ConfigurationKeys.FUNCTION_QUERY_BUILDER_CONFIG).addProvider(0, new AqpSolrFunctionProvider());
		config.get(ConfigurationKeys.FUNCTION_QUERY_BUILDER_CONFIG).addProvider(1, new AqpAdsabsSubQueryProvider());
		config.get(ConfigurationKeys.FUNCTION_QUERY_BUILDER_CONFIG).addProvider(2, new AqpAdsabsFunctionProvider());
		
		
		config.get(AqpAdsabsQueryConfigHandler.ConfigurationKeys.AUTHOR_FIELDS).put("author", new int[0]);
		config.get(AqpAdsabsQueryConfigHandler.ConfigurationKeys.AUTHOR_FIELDS).put("first_author", new int[0]);
		
		
		if (params.getBool("debugQuery", false) != false) {
			try {
				qParser.setDebug(true);
			} catch (Exception e) {
        e.printStackTrace();
      }
		}
		
		
		// this whole sections is necessary for date parsing, i should revisit and clean it
		// it is a mess
		HashMap<String, NumericConfig> ncm = new HashMap<String, NumericConfig>();
    config.set(StandardQueryConfigHandler.ConfigurationKeys.NUMERIC_CONFIG_MAP, ncm);
    
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.ROOT);
    sdf.setTimeZone(DateField.UTC);
    
    for (String field: new String[]{"read_count", "cite_read_boost"}) {
      ncm.put(field, new NumericConfig(8, NumberFormat.getNumberInstance(Locale.US), NumericType.FLOAT));
    }
    
    ncm.put("date", new NumericConfig(6, new NumberDateFormat(sdf), NumericType.LONG));

    for (String field: new String[]{"recid", "pubdate_sort", "citation_count"}) {
      ncm.put(field, new NumericConfig(4, NumberFormat.getNumberInstance(Locale.US), NumericType.INT));
    }
    
    
    /*
    config.get(StandardQueryConfigHandler.ConfigurationKeys.FIELD_DATE_RESOLUTION_MAP)
      .put("date", DateTools.Resolution.MONTH);
    
		config.getFieldConfig("date").set(StandardQueryConfigHandler.ConfigurationKeys.NUMERIC_CONFIG, 
		    new NumericConfig(6, new NumberDateFormat(sdf), NumericType.LONG));
		FieldConfig ddd = config.getFieldConfig("date");
		
		config.getFieldConfig("pubdate").set(StandardQueryConfigHandler.ConfigurationKeys.NUMERIC_CONFIG, 
        new NumericConfig(6, new NumberDateFormat(sdf), NumericType.LONG));
		*/
	}

	public Query parse() throws ParseException {
		try {
		  //if (qstr.trim().endsWith(",") && !qstr.trim().endsWith("\\,")) {
		  //  QueryConfigHandler config = qParser.getQueryConfigHandler();
		  //  return qParser.parse(getString() + config.get(AqpAdsabsQueryConfigHandler.ConfigurationKeys.DUMMY_VALUE), null);
		  //}
			return qParser.parse(getString(), null);
		} catch (QueryNodeException e) {
			ParseException ex = new ParseException(e.getMessage());
			ex.setStackTrace(e.getStackTrace());
			throw ex;
		}
		catch (SolrException e1) {
			ParseException ex2 = new ParseException(e1.getMessage());
			ex2.setStackTrace(e1.getStackTrace());
			throw ex2;
		}
	}

	public AqpQueryParser getParser() {
		return qParser;
	}
}
