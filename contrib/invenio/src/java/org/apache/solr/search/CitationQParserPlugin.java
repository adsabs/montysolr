package org.apache.solr.search;

import org.apache.solr.common.params.ModifiableSolrParams;
import org.apache.solr.common.params.SolrParams;
import org.apache.solr.common.util.NamedList;
import org.apache.solr.request.SolrQueryRequest;

/**
 * Parse Invenio's variant on the refersto citation <br>
 * Other parameters:
 * <ul>
 * <li>rel - the name of the relation: citedby, cites</li>
 * </ul>
 * <br>
 * Example:
 * <code>{!relation rel=citedby}coauthor:ellis +bar -baz</code>
 */
public class CitationQParserPlugin extends QParserPlugin {
	public static String NAME = "relation";
	private String defaultRel = "refersto";
	private String idField = "id";
	
	public void init(NamedList args) {
		NamedList defaults = (NamedList) args.get("defaults");
		if (defaults.get("defaultRel") != null) {
			defaultRel = (String) defaults.get("defaultRel");
		}
		if (defaults.get("idField") != null) {
			idField = (String) defaults.get("idField");
		}
	}

	
	public QParser createParser(String qstr, SolrParams localParams,
			SolrParams params, SolrQueryRequest req) {

		ModifiableSolrParams p = new ModifiableSolrParams(params);
		p.add("rel", defaultRel);
		p.add("idField", idField);
		params = p;
		
		return new CitationQParser(qstr, localParams, params, req);
	}

}
