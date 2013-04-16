package org.adsabs.solr;

/*
 * This class is used only by tests, it follows the setup of the schema.xml
 * 
 */
public final class AdsConfig {
	
	public static class F {
		public static String ID = "id";
		public static String BIBCODE = "bibcode";
		public static String DEF_FIELD = "all";
		public static String RECID = "recid";
		public static String AUTHOR = "author";
		
		// a field using type='ads_text'
		public static String TYPE_ADS_TEXT = "title";
		public static String[] TYPE_ADS_TEXT_FIELDS = new String[]{"title", "alternate_title", "abstract"};
		
		public static String TYPE_NORMALIZED_TEXT_ASCII = "keyword";
    public static String[] TYPE_NORMALIZED_TEXT_ASCII_FIELDS = new String[]{"pub", "pub_raw", "keyword", "keyword_norm"};
    
    public static String TYPE_NORMALIZED_STRING_ASCII = "bibcode";
    public static String[] TYPE_NORMALIZED_STRING_ASCII_FIELDS = new String[]{"bibcode", "volume", 
    	"issue", "lang", "issn", "isbn", "property", "database", "data", "bibgroup", "vizier"};
    
    public static String[] TYPE_DATE_FIELDS = new String[]{"date"};
    
	};
	
	// defType query parser
	public static String DEF_TYPE = "aqp";
	
	
}
