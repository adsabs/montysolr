package org.apache.solr.search;

import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.flexible.core.QueryNodeException;
import org.apache.lucene.queryparser.flexible.core.QueryNodeParseException;
import org.apache.lucene.queryparser.flexible.core.config.QueryConfigHandler;
import org.apache.lucene.queryparser.flexible.standard.config.StandardQueryConfigHandler;
import org.apache.lucene.queryparser.flexible.standard.config.StandardQueryConfigHandler.ConfigurationKeys;
import org.apache.lucene.queryparser.flexible.standard.config.StandardQueryConfigHandler.Operator;
import org.apache.lucene.queryparser.flexible.aqp.AqpQueryParser;
import org.apache.lucene.queryparser.flexible.aqp.parser.AqpStandardLuceneParser;
import org.apache.lucene.queryparser.flexible.aqp.parser.AqpStandardQueryConfigHandler;
import org.apache.lucene.queryparser.flexible.aqp.parser.AqpStandardQueryNodeProcessorPipeline;
import org.apache.lucene.queryparser.flexible.aqp.parser.AqpStandardQueryTreeBuilder;
import org.apache.lucene.search.Query;
import org.apache.solr.common.SolrException;
import org.apache.solr.common.params.CommonParams;
import org.apache.solr.common.params.SolrParams;
import org.apache.solr.request.SolrQueryRequest;
import org.apache.solr.schema.IndexSchema;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This is the MAIN solr entry point - this instantiates a query 
 * parser - it sets some default parameters from the config and prepares 
 * ulr parameters.
 * 
 * <p>
 * 
 * TODO: the parser needs to know configuration of data/numeric fields
 *       so that the analysis is done correctly. It is not done here
 *       for the moment (as this is SOLR-specific and needs more 
 *       components)
 * <p>      
 *       In principle, it is configured like this:
 * <p>      
 * 
 * <pre>
 *      HashMap<String, NumericConfig> ncm = new HashMap<String, NumericConfig>();
 *   		config.set(StandardQueryConfigHandler.ConfigurationKeys.NUMERIC_CONFIG_MAP, ncm);
 *      ncm.put(field_name, new NumericConfig(4, NumberFormat.getNumberInstance(Locale.US), NumericType.INT));
 * </pre>
 * 
 * @see AqpLuceneQParserPlugin
 * @see AqpStandardLuceneParser
 * @see AqpStandardQueryConfigHandler
 * @see AqpStandardQueryNodeProcessorPipeline
 * @see AqpStandardQueryTreeBuilder
 *
 */
public class AqpLuceneQParser extends QParser {

	public static final Logger log = LoggerFactory
			.getLogger(AqpLuceneQParser.class);

	private AqpQueryParser qParser;

	public AqpLuceneQParser(AqpQueryParser parser, String qstr, SolrParams localParams,
			SolrParams params, SolrQueryRequest req)
			throws QueryNodeParseException {
		
		super(qstr, localParams, params, req);
		qParser = parser;

		if (getString() == null) {
			throw new SolrException(SolrException.ErrorCode.BAD_REQUEST,
					"The query is empty");
		}


		IndexSchema schema = req.getSchema();

		
		// now configure the parser using the request params, likely incomplete (yet)
		QueryConfigHandler config = qParser.getQueryConfigHandler();
		qParser.setAnalyzer(schema.getAnalyzer());
		qParser.setDefaultField(params.get(CommonParams.DF, qParser.getDefaultField()));
		
		String opParam = getParam(QueryParsing.OP);
		if (opParam != null) {
			// other operators could also become default, eg. NEAR
			qParser.setDefaultOperator("AND".equals(opParam.toUpperCase()) ? Operator.AND
					: Operator.OR);
		}
		else {
			qParser.setDefaultOperator(Operator.OR);
		}
		
		
		// this is not useful in solr world (?) - but must be available
		config.set(ConfigurationKeys.MULTI_FIELDS, new CharSequence[0]);
		
		if (params.getBool("debugQuery", false) != false) {
			try {
				qParser.setDebug(true);
			} catch (Exception e) {
        e.printStackTrace();
      }
		}
		
  	// your components may need access to solr, if there is some 'semantic analysis'
		// involved, eg. multi-step parsing of a query. I do it this way (the component
		// may be committed to solr in other tickets)
		// AqpRequestParams reqAttr = config.get(AqpAdsabsQueryConfigHandler.ConfigurationKeys.SOLR_REQUEST);
		// reqAttr.setQueryString(getString());
		// reqAttr.setRequest(req);
		// reqAttr.setLocalParams(localParams);
		// reqAttr.setParams(params);
		
	}
	
	@Override
	public Query parse() throws SyntaxError {
		try {
			return qParser.parse(getString(), null);
		} catch (QueryNodeException e) {
			throw new SyntaxError(e.getMessage(), e);
		}
		catch (SolrException e1) {
			throw new SyntaxError(e1.getMessage(), e1);
		}
	}

	public AqpQueryParser getParser() {
		return qParser;
	}
}
