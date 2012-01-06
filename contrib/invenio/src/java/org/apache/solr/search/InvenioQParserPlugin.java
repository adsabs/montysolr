package org.apache.solr.search;

import java.util.List;

import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.queryParser.aqp.AqpQueryParser;
import org.apache.lucene.queryParser.aqp.config.DefaultIdFieldAttribute;
import org.apache.lucene.queryParser.aqp.config.InvenioQueryAttribute;
import org.apache.lucene.queryParser.core.QueryNodeException;
import org.apache.lucene.queryParser.core.QueryNodeParseException;
import org.apache.lucene.queryParser.core.config.QueryConfigHandler;
import org.apache.lucene.queryParser.standard.config.DefaultOperatorAttribute.Operator;
import org.apache.solr.common.SolrException;
import org.apache.solr.common.params.CommonParams;
import org.apache.solr.common.params.DefaultSolrParams;
import org.apache.solr.common.params.SolrParams;
import org.apache.solr.common.util.NamedList;
import org.apache.solr.core.PluginInfo;
import org.apache.solr.core.SolrConfig;
import org.apache.solr.request.SolrQueryRequest;
import org.apache.solr.schema.IndexSchema;
import org.apache.lucene.search.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.apache.solr.search.QueryParsing;

/**
 * Parse query that is made of the solr fields as well as Invenio query syntax,
 * the field that are prefixed using the special code <code>inv_</code> get
 * automatically passed to Invenio
 * 
 * Other parameters:
 * <ul>
 * <li>q.op - the default operator "OR" or "AND"</li>
 * <li>df - the default field name</li>
 * </ul>
 * <br>
 * Example: <code>{!iq mode=maxinv xfields=fulltext}035:arxiv +bar -baz</code>
 * 
 * The example above would query everything as Invenio field, but fulltext will
 * be served by Solr.
 * 
 * Example:
 * <code>{!iq iq.mode=maxsolr iq.xfields=fulltext,abstract iq.channel=bitset}035:arxiv +bar -baz</code>
 * 
 * The example above will try to map all the fields into the Solr schema, if the
 * field exists, it will be served by Solr. The fulltext will be served by
 * Invenio no matter if it is defined in schema. And communication between Java
 * and Invenio is done using bitset
 * 
 * If the query is written as:<code>inv_field:value</code> the search will be
 * always passed to Invenio.
 * 
 */
public class InvenioQParserPlugin extends QParserPlugin {
	public static String NAME = "iq";
	public static String FIELDNAME = "InvenioQuery";

	private String idField = null;

	@Override
	public void init(NamedList args) {

	}

	@Override
	public QParser createParser(String qstr, SolrParams localParams,
			SolrParams params, SolrQueryRequest req) {
		try {
			return new InvenioQParser(qstr, localParams, params, req, getIdField(req));
		} catch (QueryNodeParseException e) {
			throw new SolrException(SolrException.ErrorCode.SERVICE_UNAVAILABLE, e.getLocalizedMessage());
		}
	}

	
	private String getIdField(SolrQueryRequest req) {
		if (idField != null) {
			return idField;
		}

		SolrConfig config = req.getCore().getSolrConfig();
		PluginInfo pluginInfo = null;
		for (PluginInfo info : config.getPluginInfos(QParserPlugin.class.getCanonicalName())) {
			if (info.name.equals(NAME)) {
				pluginInfo = info;
				break;
			}
		}
		if (pluginInfo == null) {
			throw new SolrException(SolrException.ErrorCode.SERVICE_UNAVAILABLE,
					"Configuration error, InvenioQParserPlugin must be registered under the name: " + NAME);
		}
		Object o = pluginInfo.initArgs.get("defaults");
		SolrParams defaults;
		if (o != null && o instanceof NamedList) {
			defaults = SolrParams.toSolrParams((NamedList) o);
			idField = defaults.get("idfield");
			
			IndexSchema schema = req.getSchema();
			if (idField == null || !schema.hasExplicitField(idField)) {
				throw new SolrException(SolrException.ErrorCode.SERVICE_UNAVAILABLE,
						"Wrong configuration, the required idfield is missing or not present in schema. "
					  + "We cannot map external ids onto lucene docids without it.");
			}
			
			return idField;
		} else {
			throw new SolrException(SolrException.ErrorCode.SERVER_ERROR,
					"Wrong configuration, missing idfield");
		}
	}

}

class InvenioQParser extends QParser {

	public static final Logger log = LoggerFactory
			.getLogger(InvenioQParser.class);

	AqpQueryParser invParser;

	public InvenioQParser(String qstr, SolrParams localParams,
			SolrParams params, SolrQueryRequest req, String idField) throws QueryNodeParseException {
		super(qstr, localParams, params, req);

		if (getString() == null) {
			throw new SolrException(SolrException.ErrorCode.BAD_REQUEST, "The query is empty");
		}

		SolrParams solrParams = localParams == null ? params
				: new DefaultSolrParams(localParams, params);

		IndexSchema schema = req.getSchema();
		invParser = new AqpInvenioQueryParserSolr();

		// this is allowed to be only in the config (no locals possible)
		QueryConfigHandler config = invParser.getQueryConfigHandler();
		

		config.getAttribute(DefaultIdFieldAttribute.class).setDefaultIdField(
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
			return invParser.parse(getString());
		} catch (QueryNodeException e) {
			throw new ParseException(e.getLocalizedMessage());
		}
	}

	
	public AqpQueryParser getParser() {
		return invParser;
	}

}
