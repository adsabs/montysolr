package org.apache.solr.util;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.net.URLEncoder;

public class WebUtils {
	
	// http://stackoverflow.com/questions/1667278/parsing-query-strings-in-java
	public static Map<String, List<String>> getUrlParameters(String url)
			throws UnsupportedEncodingException {
		Map<String, List<String>> params = new HashMap<String, List<String>>();
		String[] urlParts = url.split("\\?");
		if (urlParts.length > 1) {
			String query = urlParts[1];
			return parseQuery(query);
		}
		return params;
	}
	
	
	public static Map<String, List<String>> parseQuery(String query) 
			throws UnsupportedEncodingException {
		Map<String, List<String>> params = new HashMap<String, List<String>>();
		// deal with encoded &
		query = URLDecoder.decode(query, "UTF-8");
		for (String param : query.split("&")) {
			String pair[] = param.split("=");
			String key = URLDecoder.decode(pair[0], "UTF-8");
			String value = "";
			if (pair.length > 1) {
				value = URLDecoder.decode(pair[1], "UTF-8");
			}
			List<String> values = params.get(key);
			if (values == null) {
				values = new ArrayList<String>();
				params.put(key, values);
			}
			values.add(value);
		}
		return params;
	}
	
	public static Map<String, String> parseQueryString(String encodedParams)
			throws UnsupportedEncodingException {
		final Map<String, String> qps = new HashMap<String, String>();
		final StringTokenizer pairs = new StringTokenizer(encodedParams, "&");
		while (pairs.hasMoreTokens()) {
			final String pair = pairs.nextToken();
			final StringTokenizer parts = new StringTokenizer(pair, "=");
			final String key = URLDecoder.decode(parts.nextToken(), "UTF-8");
			final String value = parts.hasMoreTokens() ? URLDecoder.decode(parts.nextToken(), "UTF-8") : "";

			qps.put(key, value);
		}
		return qps;
	}

}
