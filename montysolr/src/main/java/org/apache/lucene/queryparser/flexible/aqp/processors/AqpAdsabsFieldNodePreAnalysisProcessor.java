package org.apache.lucene.queryparser.flexible.aqp.processors;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.queryparser.flexible.aqp.nodes.AqpConstantQueryNode;
import org.apache.lucene.queryparser.flexible.aqp.nodes.AqpNonAnalyzedQueryNode;
import org.apache.lucene.queryparser.flexible.core.QueryNodeException;
import org.apache.lucene.queryparser.flexible.core.nodes.QuotedFieldQueryNode;
import org.apache.lucene.queryparser.flexible.core.config.QueryConfigHandler;
import org.apache.lucene.queryparser.flexible.core.nodes.BoostQueryNode;
import org.apache.lucene.queryparser.flexible.core.nodes.FieldQueryNode;
import org.apache.lucene.queryparser.flexible.core.nodes.QueryNode;
import org.apache.lucene.queryparser.flexible.messages.MessageImpl;
import org.apache.lucene.queryparser.flexible.standard.config.StandardQueryConfigHandler.ConfigurationKeys;
import org.apache.lucene.queryparser.flexible.standard.nodes.TermRangeQueryNode;
import org.apache.solr.util.DateMathParser;
import org.apache.solr.util.SolrPluginUtils;

import java.io.IOException;
import java.io.StringReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Hand-made modifications that catch the exceptions or enhancements to the AQP
 * ADSABS parsing
 *
 * @see AqpFieldMapperProcessor
 * @see QueryConfigHandler
 */
public class AqpAdsabsFieldNodePreAnalysisProcessor extends AqpQueryNodeProcessorImpl {

    private final DateMathParser dmp;
    private Map<String, TargetField> dehumnzdDates;
    private Map<String, Float> staticFields;

    public AqpAdsabsFieldNodePreAnalysisProcessor() {
        super();
        dmp = new DateMathParser(DateMathParser.UTC);

    }

    private Map<String, Float> getStaticFields() {
        if (staticFields != null)
            return staticFields;
        if (hasConfigMap() && getConfigVal("aqp.constant_scoring", null) != null) {
            staticFields = SolrPluginUtils.parseFieldBoosts(getConfigVal("aqp.constant_scoring"));
        } else {
            staticFields = new HashMap<String, Float>();
        }
        return staticFields;
    }

    private Map<String, TargetField> getFields() {
        if (dehumnzdDates != null)
            return dehumnzdDates;

        dehumnzdDates = new HashMap<String, TargetField>();
        String pairs = getConfigVal("aqp.humanized.dates", null);
        if (pairs != null) {

            String datef = getConfigVal("aqp.dateFormat");
            String timef = getConfigVal("aqp.timestampFormat");

            SimpleDateFormat ddf = new SimpleDateFormat(datef, Locale.US);
            ddf.setTimeZone(TimeZone.getTimeZone("UTC"));

            SimpleDateFormat tdf = new SimpleDateFormat(timef, Locale.US);
            tdf.setTimeZone(TimeZone.getTimeZone("UTC"));

            for (String pair : pairs.split(",")) {
                String[] kv = pair.split(":");
                if (kv.length == 2) {
                    dehumnzdDates.put(kv[0], new TargetField(kv[1], ddf, ddf));
                } else if (kv.length == 3) {
                    dehumnzdDates.put(kv[0], new TargetField(kv[1], ddf, kv[2].equals("timestamp") ? tdf : ddf));
                } else {
                    throw new RuntimeException("Misconfiguratin in aqp.humanized.dates: " + pair);
                }
            }
        }
        return dehumnzdDates;
    }

    @Override
    protected QueryNode preProcessNode(QueryNode node) throws QueryNodeException {
        return node;
    }

    @Override
    protected QueryNode postProcessNode(QueryNode node) throws QueryNodeException {

        if (node instanceof FieldQueryNode) {

            Map<String, TargetField> humanizedDateFields = getFields();

            FieldQueryNode fieldNode = (FieldQueryNode) node;
            String field = fieldNode.getFieldAsString();

            // A quoted four-digit year range is still a range, not one
            // normalized year token (year's analyzer removes the dash).
            if (fieldNode instanceof QuotedFieldQueryNode && field.equals("year")
                    && isYearRange(fieldNode.getTextAsString())
                    && !(node.getParent() instanceof TermRangeQueryNode)) {
                String value = fieldNode.getTextAsString();
                int begin = fieldNode.getBegin();
                FieldQueryNode lowerBound = new FieldQueryNode(field, value.substring(0, 4), begin, begin + 4);
                FieldQueryNode upperBound = new FieldQueryNode(field, value.substring(5), begin + 5, begin + 9);
                return new TermRangeQueryNode(lowerBound, upperBound, true, true);
            }

            // we must detect pubdate:YYYY(-MM-DD) queries, and turn them into range
            // query if necessary
            // ie. rewrite (pub)date fieldquery into termrange query

            // Explicit year ranges may have a one-to-three-digit endpoint. Keep this
            // field-specific so identifiers such as title:09-2012 remain terms.
            if (field.equals("year") && isShortYearRange(fieldNode.getTextAsString())
                    && !(node.getParent() instanceof TermRangeQueryNode)) {
                node = rewriteShortYearRange(fieldNode);
            }
            if (humanizedDateFields.containsKey(field)) {
                node = rewriteDateRange(node, fieldNode, humanizedDateFields, field);
            }

            Map<String, Float> statFields = getStaticFields();
            if (statFields.containsKey(field) && node.getParent() != null && !(node.getParent() instanceof TermRangeQueryNode)) {
                node = new AqpConstantQueryNode(node);
                Float boost = statFields.get(field);
                if (boost != null && boost != 1.0f) {
                    node = new BoostQueryNode(node, boost);
                }
            }
        }
        return node;
    }

    private boolean isYearRange(String value) {
        if (value == null || value.length() != 9 || value.charAt(4) != '-') {
            return false;
        }
        for (int i = 0; i < value.length(); i++) {
            if (i == 4) {
                continue;
            }
            char c = value.charAt(i);
            if (c < '0' || c > '9') {
                return false;
            }
        }
        return true;
    }

    private boolean isShortYearRange(String value) {
        int separator = value.indexOf('-');
        if (separator <= 0 || separator != value.lastIndexOf('-')) {
            return false;
        }

        int lowerLength = separator;
        int upperLength = value.length() - separator - 1;
        if (!((lowerLength <= 3 && upperLength == 4)
                || (lowerLength == 4 && upperLength >= 1 && upperLength <= 3))) {
            return false;
        }

        for (int i = 0; i < value.length(); i++) {
            if (i == separator) {
                continue;
            }
            if (!Character.isDigit(value.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    private QueryNode rewriteShortYearRange(FieldQueryNode fieldNode) {
        String value = fieldNode.getTextAsString();
        int separator = value.indexOf('-');
        String lower = canonicalYear(value.substring(0, separator));
        String upper = canonicalYear(value.substring(separator + 1));
        int start = fieldNode.getBegin();
        FieldQueryNode lowerBound = new FieldQueryNode(fieldNode.getFieldAsString(), lower,
                start, start + separator);
        FieldQueryNode upperBound = new FieldQueryNode(fieldNode.getFieldAsString(), upper,
                start + separator + 1, fieldNode.getEnd());
        return new TermRangeQueryNode(lowerBound, upperBound, true, true);
    }
    private String canonicalYear(String value) {
        return value.length() == 4 ? value : "0000".substring(value.length()) + value;
    }

    private QueryNode rewriteDateRange(QueryNode node, FieldQueryNode fieldNode, Map<String, TargetField> hFields,
                                       String field) throws QueryNodeException {
        TargetField targetDateField = hFields.get(field);
        // first parse the date with the appropriate analyzer
        String value = fieldNode.getTextAsString();
        boolean dateGuessed = false;

        if (value.equals("*") && node.getParent() instanceof TermRangeQueryNode) {
            QueryNode theOtherNode = null;
            for (QueryNode ch : node.getParent().getChildren()) {
                if (ch != fieldNode) {
                    theOtherNode = ch;
                    break;
                }
            }
            if (theOtherNode != null) {
                String theOtherValue = ((FieldQueryNode) theOtherNode).getTextAsString();
                value = theOtherValue;
                dateGuessed = true;
            }
        }

        Analyzer analyzer = getQueryConfigHandler().get(ConfigurationKeys.ANALYZER);
        TokenStream source = null;
        try {
            source = analyzer.tokenStream(field, new StringReader(value));
            source.reset();
            source.incrementToken();
        } catch (IOException e1) {
            throw new RuntimeException(e1);
        } finally {
            if (source != null) {
                try {
                    source.close();
                } catch (IOException e) {
                    // ignore
                }
            }
        }

        Date dateWithoutOffset = null;
        String normalizedDate = source.getAttribute(CharTermAttribute.class).toString();
        try {
            dateWithoutOffset = targetDateField.parse(normalizedDate);
            dmp.setNow(dateWithoutOffset);

        } catch (ParseException e) {
            throw new QueryNodeException(new MessageImpl(e.getMessage()));
        }

        // if we are already inside TermRangeQuery, we just need
        // to change the field and value
        if (node.getParent() instanceof TermRangeQueryNode) {
            fieldNode.setField(targetDateField.fieldname);
            if (node.getParent().getChildren().get(0) == fieldNode) { // lower bound
                if (dateGuessed) {
                    if (fieldNode.getBegin() > 0) { // user typed '*'
                        fieldNode.setValue(moveDate(targetDateField, value, dateWithoutOffset, "/YEAR-1000YEAR+1SECOND",
                                "/MONTH-1000YEAR+1SECOND", "/DAY-1000YEAR+1SECOND"));
                    } else {
                        fieldNode.setValue(moveDate(targetDateField, value, dateWithoutOffset, "/YEAR-1YEAR+1SECOND",
                                "/MONTH-1MONTH+1SECOND", "/DAY-1DAY+1SECOND"));
                    }
                } else {
                    fieldNode.setValue(moveDate(targetDateField, normalizedDate, dateWithoutOffset, "", "", "+0SECOND"));
                }
            } else { // upper bound
                if (dateGuessed) {
                    if (fieldNode.getBegin() > 0) { // user typed '*'
                        fieldNode.setValue(moveDate(targetDateField, value, dateWithoutOffset, "/YEAR+1000YEAR-1SECOND",
                                "/MONTH+1000YEAR-1SECOND", "/DAY+1000YEAR-1SECOND"));
                    } else {
                        fieldNode.setValue(moveDate(targetDateField, value, dateWithoutOffset, "/YEAR+1YEAR-1SECOND",
                                "/MONTH+1MONTH-1SECOND", "/DAY+1DAY-1SECOND"));
                    }
                } else {
                    fieldNode.setValue(moveDate(targetDateField, value, dateWithoutOffset, "/YEAR+1YEAR-1SECOND",
                            "/MONTH+1MONTH-1SECOND", "/DAY+1DAY-1SECOND"));
                }

            }

            return new AqpNonAnalyzedQueryNode(fieldNode);
        }

        // make a copy of the pubdate node
        FieldQueryNode upperBound;
        try {
            upperBound = fieldNode.cloneTree();
        } catch (CloneNotSupportedException e) {
            throw new QueryNodeException(new MessageImpl(e.getMessage()));
        }

        FieldQueryNode lowerBound = fieldNode;
        lowerBound.setField(targetDateField.fieldname);
        lowerBound.setValue(targetDateField.format.format(dateWithoutOffset));

        upperBound.setField(targetDateField.fieldname);
        upperBound
                .setValue(moveDate(targetDateField, value, dateWithoutOffset, "/YEAR+1YEAR", "/MONTH+1MONTH", "/DAY+1DAY"));

        return new TermRangeQueryNode(new AqpNonAnalyzedQueryNode(lowerBound), new AqpNonAnalyzedQueryNode(upperBound),
                true, false); // upper bound non-inclusive
    }

    @SuppressWarnings("deprecation")
    private String moveDate(TargetField targetField, String originalDate, Date parsedDate, String... moveBy)
            throws QueryNodeException {
        String[] dateParts = originalDate.split("-|/");
        Date dateWithOffset = (Date) parsedDate.clone();
        dmp.setNow(parsedDate);
        try {
            if (dateParts.length == 1) { // just a year
                assert moveBy.length >= 1;
                dateWithOffset = dmp.parseMath(moveBy[0]); // "+1YEAR" = move to the
                // next year
            } else if (dateParts.length == 2) {
                assert moveBy.length >= 2;
                dateWithOffset = dmp.parseMath(moveBy[1]); // "+1MONTH"
            } else {
                assert moveBy.length == 3;
                dateWithOffset = dmp.parseMath(moveBy[2]); // "+1DAY"
            }
        } catch (ParseException e) {
            throw new QueryNodeException(new MessageImpl(e.getMessage()));
        }

        return targetField.format(dateWithOffset);

    }

    @Override
    protected List<QueryNode> setChildrenOrder(List<QueryNode> children) throws QueryNodeException {
        return children;
    }

    class TargetField {
        public String fieldname;
        private final SimpleDateFormat format;
        private final SimpleDateFormat parser;

        public TargetField(String n, SimpleDateFormat p, SimpleDateFormat f) {
            fieldname = n;
            parser = p; // this is a parser that understand output from the
            // tokenization
            format = f; // this is a parser that knows how to properly output
            // 'fieldname'
        }

        public Date parse(String source) throws ParseException {
            return parser.parse(source);
        }

        public String format(Date source) {
            return format.format(source);
        }
    }

}
