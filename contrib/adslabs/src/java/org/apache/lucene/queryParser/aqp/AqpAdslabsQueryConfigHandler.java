package org.apache.lucene.queryParser.aqp;

import org.apache.lucene.queryParser.aqp.builders.AqpAdslabsFunctionProvider;
import org.apache.lucene.queryParser.aqp.config.AqpAdslabsLoggingHandler;
import org.apache.lucene.queryParser.aqp.config.AqpFeedback;
import org.apache.lucene.queryParser.aqp.config.AqpFunctionQueryBuilderConfig;
import org.apache.lucene.queryParser.aqp.config.DefaultDateRangeField;
import org.apache.lucene.queryParser.aqp.config.DefaultFieldAttribute;
import org.apache.lucene.queryParser.aqp.config.DefaultIdFieldAttribute;
import org.apache.lucene.queryParser.aqp.config.DefaultProximityAttribute;
import org.apache.lucene.queryParser.aqp.config.InvenioQueryAttribute;
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
import org.apache.lucene.queryParser.standard.config.MultiFieldAttribute;
import org.apache.lucene.queryParser.standard.config.MultiTermRewriteMethodAttribute;
import org.apache.lucene.queryParser.standard.config.PositionIncrementsAttribute;
import org.apache.lucene.queryParser.standard.config.RangeCollatorAttribute;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class AqpAdslabsQueryConfigHandler extends QueryConfigHandler {
	public static final Logger log = LoggerFactory
			.getLogger(AqpAdslabsQueryConfigHandler.class);
	
	public AqpAdslabsQueryConfigHandler() {
		// Add listener that will build the FieldConfig attributes.
		addFieldConfigListener(new FieldBoostMapFCListener(this));
		addFieldConfigListener(new FieldDateResolutionFCListener(this));

		// Default Values
		addAttribute(DefaultFieldAttribute.class);
		addAttribute(DefaultIdFieldAttribute.class);
		addAttribute(RangeCollatorAttribute.class);
		addAttribute(DefaultOperatorAttribute.class);
		addAttribute(AnalyzerAttribute.class);
		addAttribute(FuzzyAttribute.class);
		addAttribute(LowercaseExpandedTermsAttribute.class);
		addAttribute(MultiTermRewriteMethodAttribute.class);
		addAttribute(AllowLeadingWildcardAttribute.class);
		addAttribute(PositionIncrementsAttribute.class);
		addAttribute(LocaleAttribute.class);
		addAttribute(DefaultPhraseSlopAttribute.class);
		addAttribute(MultiTermRewriteMethodAttribute.class);
		addAttribute(InvenioQueryAttribute.class);
		addAttribute(MultiFieldAttribute.class);
		addAttribute(DefaultProximityAttribute.class);
		addAttribute(BoostAttribute.class);
		addAttribute(DefaultDateRangeField.class);
		addAttribute(AqpFunctionQueryBuilderConfig.class);
		
		addAttribute(AqpFeedback.class); // to collect/work with exceptions
		addAttribute(AqpFeedback.class).registerEventHandler(new AqpAdslabsLoggingHandler());
		
		getAttribute(DefaultIdFieldAttribute.class).setDefaultIdField("recid");
		getAttribute(DefaultDateRangeField.class).setField("date");
		getAttribute(AllowLeadingWildcardAttribute.class).setAllowLeadingWildcard(true);
		
		getAttribute(AqpFunctionQueryBuilderConfig.class).addProvider(new AqpAdslabsFunctionProvider());
	}
}
