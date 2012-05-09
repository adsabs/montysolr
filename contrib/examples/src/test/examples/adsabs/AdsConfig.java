package examples.adsabs;

public class AdsConfig {
	public static String FIELD_ID = "id";
	public static String FIELD_RECID = "recid"; // Invenio recid, used for mapping Invenio<->Lucene
	public static String FIELD_BIBCODE = "bibcode";
	
	// author has a special analyzer/tokenizer chain
	public static String FIELD_AUTHOR = "author";
	
	// defType query parser
	public static String DEF_TYPE = "aqp";
}
