package org.apache.solr.search;

import java.text.DateFormat;
import java.text.FieldPosition;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.time.temporal.TemporalAccessor;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TimeZone;

import org.apache.lucene.document.FieldType.LegacyNumericType;
import org.apache.lucene.queryparser.flexible.aqp.AqpAdsabsQueryTreeBuilder;
import org.apache.lucene.queryparser.flexible.aqp.AqpQueryParser;
import org.apache.lucene.queryparser.flexible.aqp.builders.AqpAdsabsFunctionProvider;
import org.apache.lucene.queryparser.flexible.aqp.builders.AqpAdsabsSubQueryProvider;
import org.apache.lucene.queryparser.flexible.aqp.builders.AqpSolrFunctionProvider;
import org.apache.lucene.queryparser.flexible.aqp.config.AqpAdsabsQueryConfigHandler;
import org.apache.lucene.queryparser.flexible.aqp.config.AqpAdsabsQueryConfigHandler.ConfigurationKeys;
import org.apache.lucene.queryparser.flexible.aqp.config.AqpRequestParams;
import org.apache.lucene.queryparser.flexible.aqp.parser.AqpStandardQueryConfigHandler;
import org.apache.lucene.queryparser.flexible.core.QueryNodeException;
import org.apache.lucene.queryparser.flexible.core.QueryNodeParseException;
import org.apache.lucene.queryparser.flexible.core.config.QueryConfigHandler;
import org.apache.lucene.queryparser.flexible.standard.config.LegacyNumericConfig;
import org.apache.lucene.queryparser.flexible.standard.config.StandardQueryConfigHandler;
import org.apache.lucene.queryparser.flexible.standard.config.StandardQueryConfigHandler.Operator;
import org.apache.lucene.search.Query;
import org.apache.solr.common.SolrException;
import org.apache.solr.common.params.CommonParams;
import org.apache.solr.common.params.SolrParams;
import org.apache.solr.request.SolrQueryRequest;
import org.apache.solr.schema.IndexSchema;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.solr.util.DateMathParser;

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
  
  public static TimeZone UTC = TimeZone.getTimeZone("UTC");
	public static final Logger log = LoggerFactory
	.getLogger(AqpAdsabsQParser.class);
	

	private AqpQueryParser qParser;

	public AqpAdsabsQParser(AqpQueryParser parser, String qstr, SolrParams localParams,
			SolrParams params, SolrQueryRequest req, SolrParserConfigParams defaultConfig)
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


    Map<String, String> namedParams = config.get(AqpStandardQueryConfigHandler.ConfigurationKeys.NAMED_PARAMETER);
    
    // get the parameters from the parser configuration (and pass them on)
    for (Entry<String, String> par: defaultConfig.params.entrySet()) {
      String k = par.getKey();
      if (k.startsWith("aqp.")) {
        namedParams.put(k, (String) par.getValue());
      }
    }
    
    // get the named parameters from solr request object (they will be passed further on)
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


		qParser.setAnalyzer(schema.getQueryAnalyzer());

		String defaultField = getParam(CommonParams.DF);
		if (defaultField == null) {
		  if (namedParams.containsKey("aqp.defaultField")) {
		    defaultField = namedParams.get("aqp.defaultField");
		  }
		  else {
	      defaultField = getReq().getSchema().getDefaultSearchFieldName();
	    }
		}
		
		if (defaultField != null) {
			config.set(AqpStandardQueryConfigHandler.ConfigurationKeys.DEFAULT_FIELD, defaultField);
		}
		
		// if defaultField was set, this will be useless
		if (namedParams.containsKey("aqp.unfieldedSearchField"))
		  config.set(AqpAdsabsQueryConfigHandler.ConfigurationKeys.UNFIELDED_SEARCH_FIELD, namedParams.get("aqp.unfieldedSearchField"));

		// default operator
		String opParam = getParam(QueryParsing.OP);
		if (opParam == null) {
		  if (namedParams.containsKey("aqp.defaultOperator")) {
		    opParam = namedParams.get("aqp.defaultOperator");
		  }
		  else {
	      opParam = getReq().getSchema().getQueryParserDefaultOperator();
	    }
		}
		
		if (opParam != null) {
			qParser.setDefaultOperator("AND".equals(opParam.toUpperCase()) ? Operator.AND
					: Operator.OR);
		} else {
			throw new SolrException(SolrException.ErrorCode.BAD_REQUEST,
			"The defaultOperator is set to null");
		}

		
		Map<String, String> fieldMap;
		for (String fName: new String[]{"aqp.fieldMap", "aqp.fieldMapPostAnalysis"}) {
			if (fName.equals("aqp.fieldMap")) { // booo
				fieldMap = config.get(AqpStandardQueryConfigHandler.ConfigurationKeys.FIELD_MAPPER);
			}
			else {
				fieldMap = config.get(AqpStandardQueryConfigHandler.ConfigurationKeys.FIELD_MAPPER_POST_ANALYSIS);
			}

			if (namedParams.containsKey(fName)) {
				String[] fields = namedParams.get(fName).split(";");
				String ffs[];
				for (String f: fields) {
					ffs = f.split("\\s+");
					if (ffs.length < 2) {
						throw new SolrException(SolrException.ErrorCode.SERVER_ERROR,
								"Configuration error in the section: " + fName);
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


		if (namedParams.containsKey("aqp.df.fields")) {
			qParser.setMultiFields(namedParams.get("aqp.df.fields").split(","));
		}

		// special analyzers
		config.get(ConfigurationKeys.FUNCTION_QUERY_BUILDER_CONFIG).addProvider(0, new AqpSolrFunctionProvider());
		config.get(ConfigurationKeys.FUNCTION_QUERY_BUILDER_CONFIG).addProvider(1, new AqpAdsabsSubQueryProvider());
		config.get(ConfigurationKeys.FUNCTION_QUERY_BUILDER_CONFIG).addProvider(2, new AqpAdsabsFunctionProvider());

		if (namedParams.containsKey("aqp.authorFields")) {
      for (String f: namedParams.get("aqp.authorFields").split(",")) {
        config.get(AqpAdsabsQueryConfigHandler.ConfigurationKeys.AUTHOR_FIELDS).put(f, new int[0]);
      }
    }
		
		if (params.getBool("debugQuery", false) != false) {
			try {
				qParser.setDebug(true);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}


		HashMap<String, LegacyNumericConfig> ncm = new HashMap<String, LegacyNumericConfig>();
		config.set(StandardQueryConfigHandler.ConfigurationKeys.LEGACY_NUMERIC_CONFIG_MAP, ncm);

		if (namedParams.containsKey("aqp.floatFields")) {
      for (String f: namedParams.get("aqp.floatFields").split(",")) {
        ncm.put(f, new LegacyNumericConfig(8, new MaxNumberFormat(Float.MAX_VALUE), LegacyNumericType.FLOAT)); //UPGRADE: todo
      }
    }

		if (namedParams.containsKey("aqp.dateFields")) {
		  SimpleDateFormat sdf = new SimpleDateFormat(namedParams.containsKey("aqp.dateFormat") 
		      ? namedParams.get("aqp.dateFormat") : "yyyy-MM-dd'T'HH:mm:ss", Locale.ROOT);
		  sdf.setTimeZone(UTC);
      for (String f: namedParams.get("aqp.dateFields").split(",")) {
        ncm.put(f, new LegacyNumericConfig(6, new MaxNumberFormat(new NumberDateFormat(sdf), Long.MAX_VALUE), LegacyNumericType.LONG));
      }
    }
		
		if (namedParams.containsKey("aqp.timestampFields")) {
      SimpleDateFormat sdf = new SimpleDateFormat(namedParams.containsKey("aqp.timestampFormat") 
          ? namedParams.get("aqp.timestampFormat") : "yyyy-MM-dd'T'HH:mm:ss.SSS", Locale.ROOT);
      sdf.setTimeZone(UTC);
      for (String f: namedParams.get("aqp.timestampFields").split(",")) {
        ncm.put(f, new LegacyNumericConfig(6, new MaxNumberFormat(new NumberDateFormat(sdf), Long.MAX_VALUE), LegacyNumericType.LONG));
      }
    }
		
    // when precision step=0 (ie use the default solr value), then it is Integer.MAX_VALUE
		if (namedParams.containsKey("aqp.intFields")) {
      for (String f: namedParams.get("aqp.intFields").split(",")) {
        ncm.put(f, new LegacyNumericConfig(Integer.MAX_VALUE, new MaxNumberFormat(Integer.MAX_VALUE), LegacyNumericType.INT));
      }
    }
		
		config.get(AqpAdsabsQueryConfigHandler.ConfigurationKeys.VIRTUAL_FIELDS).putAll(defaultConfig.virtualFields);
		
		if (params.get("aqp.allow.leading_wildcard", null) != null) {
		  config.set(StandardQueryConfigHandler.ConfigurationKeys.ALLOW_LEADING_WILDCARD, 
		      params.getBool("aqp.allow.leading_wildcard", false));
		}
		
	}
	
	public class NumberDateFormat extends NumberFormat {
	  
    private static final long serialVersionUID = -4334084585068547470L;
    final private DateFormat dateFormat;
	  
	  /**
	   * Constructs a {@link NumberDateFormat} object using the given {@link DateFormat}.
	   * 
	   * @param dateFormat {@link DateFormat} used to parse and format dates
	   */
	  public NumberDateFormat(DateFormat dateFormat) {
	    this.dateFormat = dateFormat;
	  }
	  
	  @Override
	  public StringBuffer format(double number, StringBuffer toAppendTo,
	      FieldPosition pos) {
	    return dateFormat.format(new Date((long) number), toAppendTo, pos);
	  }
	  
	  @Override
	  public StringBuffer format(long number, StringBuffer toAppendTo,
	      FieldPosition pos) {
	    return dateFormat.format(new Date(number), toAppendTo, pos);
	  }
	  
	  @Override
	  public Number parse(String source, ParsePosition parsePosition) {
	    final Date date = dateFormat.parse(source, parsePosition);
	    return (date == null) ? null : date.getTime();
	  }
	  
	  @Override
	  public StringBuffer format(Object number, StringBuffer toAppendTo,
	      FieldPosition pos) {
	    return dateFormat.format(number, toAppendTo, pos);
	  }
	  
	  public DateFormat getFormatter() {
	    return dateFormat;
	  }
	  
	}

  /**
	 * Internal class that allows us to accept '*' (lucene's way of saying 'anything')
	 */
	private class MaxNumberFormat extends NumberFormat {
	  private static final long serialVersionUID = -407706279343648005L;
    private NumberFormat parser = null;
    private Number max = null;
    private DateMathParser dParser = new DateMathParser(UTC);
    
    public MaxNumberFormat(Number max) {
      this(NumberFormat.getNumberInstance(Locale.US), max);
      
    }
	  public MaxNumberFormat(NumberFormat parser, Number max) {
	    this.parser = parser;
	    this.max = max;
	  }
	  
    
    @Override
    public StringBuffer format(double number, StringBuffer toAppendTo, FieldPosition pos) {
      return parser.format(number, toAppendTo, pos);
    }

    @Override
    public StringBuffer format(long number, StringBuffer toAppendTo, FieldPosition pos) {
      return parser.format(number, toAppendTo, pos);
    }

    @Override
    public Number parse(String source, ParsePosition parsePosition) {
      if (source.contains("*")) {
        parsePosition.setIndex(1);
        return max;
      }
      // it might be a symbolic date math
      try {
        Date date = dParser.parseMath(null, source);
        DateFormat formatter = ((NumberDateFormat) parser).getFormatter();
        source = formatter.format(date);
      } catch (SolrException e) {
        // pass
      }
      
      return parser.parse(source, parsePosition);
    }
	  
	}

	public Query parse() throws SyntaxError {
		try {
			//if (qstr.trim().endsWith(",") && !qstr.trim().endsWith("\\,")) {
			//  QueryConfigHandler config = qParser.getQueryConfigHandler();
			//  return qParser.parse(getString() + config.get(AqpAdsabsQueryConfigHandler.ConfigurationKeys.DUMMY_VALUE), null);
			//}
			return qParser.parse(cleanUp(getString()), null);
		} catch (QueryNodeException e) {
		  throw new SyntaxError(e);
		}
		catch (SolrException e1) {
		  throw new SyntaxError(e1);
		}
	}
	

	public AqpQueryParser getParser() {
		return qParser;
	}
	
	private String cleanUp(String queryStr) {
    int c = 0;
    boolean touched = false;
    char[] a = queryStr.toCharArray();
    while (c < a.length) {
      if (a[c] == '\u201c' || a[c] == '\u201d') {
        a[c] = '"';
        touched = true;
      }
      c++;
    }
    if (touched) return new String(a);
    return queryStr;
  }
}
