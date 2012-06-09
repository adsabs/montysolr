package org.apache.solr.search;

import org.apache.lucene.queryParser.aqp.AqpAdsabsQueryParser;
import org.apache.lucene.queryParser.aqp.AqpQueryParser;
import org.apache.lucene.queryParser.core.QueryNodeParseException;
import org.apache.solr.common.SolrException;
import org.apache.solr.common.params.SolrParams;
import org.apache.solr.common.util.NamedList;
import org.apache.solr.request.SolrQueryRequest;

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
 * Example: <code>{!aqp mode=maxinv xfields=fulltext}035:arxiv +bar -baz</code>
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
public class AdsQParserPlugin extends QParserPlugin {
	public static String NAME = "aqp";
	
	public void init(NamedList args) {
		//System.out.println(args);
	}

	
	public QParser createParser(String qstr, SolrParams localParams,
			SolrParams params, SolrQueryRequest req) {
		try {
			// TODO: optimization -- keep the instance of the parser 
			// and instantiate only the syntax parser
			AqpQueryParser parser = AqpAdsabsQueryParser.init();
			return new AqpAdsabsQParser(parser, qstr, localParams, params, req);
		} catch (QueryNodeParseException e) {
			throw new SolrException(SolrException.ErrorCode.SERVICE_UNAVAILABLE, e.getLocalizedMessage());
		} catch (Exception e) {
			throw new SolrException(SolrException.ErrorCode.SERVICE_UNAVAILABLE, e.getLocalizedMessage());
		}
	}
}

