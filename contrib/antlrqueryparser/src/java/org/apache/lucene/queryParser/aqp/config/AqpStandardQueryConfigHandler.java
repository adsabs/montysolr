package org.apache.lucene.queryParser.aqp.config;

import org.apache.lucene.queryParser.core.config.QueryConfigHandler;
import org.apache.lucene.queryParser.standard.config.AllowLeadingWildcardAttribute;
import org.apache.lucene.queryParser.standard.config.AnalyzerAttribute;
import org.apache.lucene.queryParser.standard.config.BoostAttribute;
import org.apache.lucene.queryParser.standard.config.DefaultOperatorAttribute;
import org.apache.lucene.queryParser.standard.config.DefaultPhraseSlopAttribute;
import org.apache.lucene.queryParser.standard.config.FieldBoostMapFCListener;
import org.apache.lucene.queryParser.standard.config.FieldDateResolutionFCListener;
import org.apache.lucene.queryParser.standard.config.FuzzyAttribute;
import org.apache.lucene.queryParser.standard.config.LocaleAttribute;
import org.apache.lucene.queryParser.standard.config.LowercaseExpandedTermsAttribute;
import org.apache.lucene.queryParser.standard.config.MultiTermRewriteMethodAttribute;
import org.apache.lucene.queryParser.standard.config.PositionIncrementsAttribute;
import org.apache.lucene.queryParser.standard.config.RangeCollatorAttribute;

public class AqpStandardQueryConfigHandler extends QueryConfigHandler {
	
	public AqpStandardQueryConfigHandler() {
	    // Add listener that will build the FieldConfig attributes.
	    addFieldConfigListener(new FieldBoostMapFCListener(this));
	    addFieldConfigListener(new FieldDateResolutionFCListener(this));

	    // Default Values
	    addAttribute(DefaultFieldAttribute.class);
	    addAttribute(RangeCollatorAttribute.class);
	    addAttribute(DefaultOperatorAttribute.class);
	    addAttribute(AnalyzerAttribute.class);
	    addAttribute(FuzzyAttribute.class);
	    addAttribute(BoostAttribute.class);
	    addAttribute(LowercaseExpandedTermsAttribute.class);
	    addAttribute(MultiTermRewriteMethodAttribute.class);
	    addAttribute(AllowLeadingWildcardAttribute.class);
	    addAttribute(PositionIncrementsAttribute.class);
	    addAttribute(LocaleAttribute.class);
	    addAttribute(DefaultPhraseSlopAttribute.class);
	    addAttribute(MultiTermRewriteMethodAttribute.class);   
	    
	  }

}
