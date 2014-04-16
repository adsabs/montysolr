package org.apache.solr.handler.dataimport;


import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Properties;


import org.apache.solr.common.SolrException;
import org.apache.solr.update.InvenioDB;
import org.apache.solr.util.WebUtils;

public class InvenioDataSource extends URLDataSource {
	
	
	@Override
	public void init(Context context, Properties initProps) {
		super.init(context, initProps);
	}

	@Override
	public Reader getData(String query) {
		if (query != null && query.length() > 8 && query.substring(0, 9).equals("python://")) {
			URI uri;
			try {
				uri = new URI(query);
			} catch (URISyntaxException e1) {
				throw new SolrException(SolrException.ErrorCode.BAD_REQUEST,
						e1.getMessage());
			} 
			
			
			String q = uri.getQuery();
			
			if (q == null) {
				throw new SolrException(SolrException.ErrorCode.BAD_REQUEST, 
						"Wrong url parameter, no query specified: " + query);
			}
			LOG.debug("Python request: {}", q);
			
			Map<String, List<String>> params;
			try {
				params = WebUtils.parseQuery(q);
				
			} catch (UnsupportedEncodingException e) {
				throw new SolrException(SolrException.ErrorCode.BAD_REQUEST,
						e.getMessage());
			}
			
			String results;
      try {
        results = InvenioDB.INSTANCE.getMarcXML(params);
      } catch (SQLException e) {
        throw new SolrException(SolrException.ErrorCode.BAD_REQUEST, e);
      } catch (IOException e) {
        throw new SolrException(SolrException.ErrorCode.SERVER_ERROR, e);
      }
			
			if (results == null) {
				LOG.info("No new/updated/deleted records inside Invenio.");
				return new StringReader("");
			}
			else {
				return new StringReader(results);
			}
		}
		else {
			return super.getData(query);
		}
	}

}
