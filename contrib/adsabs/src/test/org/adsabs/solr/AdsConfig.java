package org.adsabs.solr;

/*
 * This class is used only by tests, it follows the setup of the schema.xml
 * 
 */
public final class AdsConfig {
	
	public static class F {
		public static String ID = "id";
		public static String BIBCODE = "bibcode";
		public static String RECID = "recid";
		public static String AUTHOR = "author";
		
		// a field using type='ads_text'
		public static String ADS_TEXT_TYPE = "all";
	};
	
	// defType query parser
	public static String DEF_TYPE = "aqp";
}
