package org.apache.solr.search;

import org.apache.lucene.queryParser.core.QueryNodeParseException;
import org.apache.solr.common.SolrException;
import org.apache.solr.common.params.SolrParams;
import org.apache.solr.common.util.NamedList;
import org.apache.solr.core.PluginInfo;
import org.apache.solr.core.SolrConfig;
import org.apache.solr.request.SolrQueryRequest;
import org.apache.solr.schema.IndexSchema;

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

