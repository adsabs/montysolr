package org.apache.solr.search;


import org.apache.lucene.queryparser.flexible.core.QueryNodeParseException;
import org.apache.lucene.queryparser.flexible.aqp.AqpAdsabsQueryParser;
import org.apache.lucene.queryparser.flexible.aqp.AqpQueryParser;
import org.apache.solr.common.SolrException;
import org.apache.solr.common.params.SolrParams;
import org.apache.solr.common.util.NamedList;
import org.apache.solr.request.SolrQueryRequest;
import org.apache.solr.util.SolrPluginUtils;

/**
 * An instance of absolute crazy (but fun to work with) query parser.
 * Implements the ADS grammar together with lots of business logic.
 * 
 */
public class AdsQParserPlugin extends QParserPlugin {
	public static String NAME = "aqp";
	private SolrParserConfigParams defaultConfig;

	@SuppressWarnings("rawtypes")
  @Override
	public void init(NamedList args) {
		
		defaultConfig = new SolrParserConfigParams();
		
		NamedList defs = (NamedList) args.get("defaults");
		if (defs == null) {
			defs = new NamedList();
		}

		if (defs.get("virtual-fields") != null) {
			NamedList vf = (NamedList) defs.get("virtual-fields");
			for (int i=0; i<vf.size(); i++) {
				String fName = vf.getName(i);
				String fValue = (String) vf.getVal(i);
				defaultConfig.virtualFields.put(fName, SolrPluginUtils.parseFieldBoosts(fValue));
			}
		}
		
		for (int i=0; i<defs.size(); i++) {
		  String fName = defs.getName(i);
      if (fName.startsWith("aqp.")) {
        defaultConfig.params.put(fName, (String) defs.getVal(i));
      }
    }
		
	}


	public QParser createParser(String qstr, SolrParams localParams,
			SolrParams params, SolrQueryRequest req) {
		try {
			// TODO: optimization -- keep the instance of the parser 
			// and instantiate only the syntax parser
			AqpQueryParser parser = AqpAdsabsQueryParser.init();
			return new AqpAdsabsQParser(parser, qstr, localParams, params, req, defaultConfig);
		} catch (QueryNodeParseException e) {
			throw new SolrException(SolrException.ErrorCode.BAD_REQUEST, e);
		} catch (Exception e) {
			throw new SolrException(SolrException.ErrorCode.BAD_REQUEST, e);
		}
	}
	
}

