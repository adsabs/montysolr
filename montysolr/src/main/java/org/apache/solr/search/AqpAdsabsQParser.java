package org.apache.solr.search;

import org.apache.lucene.queries.function.FunctionQuery;
import org.apache.lucene.queries.function.FunctionScoreQuery;
import org.apache.lucene.queries.function.ValueSource;
import org.apache.lucene.queries.function.valuesource.ProductFloatFunction;
import org.apache.lucene.queries.function.valuesource.QueryValueSource;
import org.apache.lucene.queryparser.flexible.aqp.AqpAdsabsQueryTreeBuilder;
import org.apache.lucene.queryparser.flexible.aqp.AqpQueryParser;
import org.apache.lucene.queryparser.flexible.aqp.builders.AqpAdsabsFunctionProvider;
import org.apache.lucene.queryparser.flexible.aqp.builders.AqpAdsabsSubQueryProvider;
import org.apache.lucene.queryparser.flexible.aqp.builders.AqpSolrFunctionProvider;
import org.apache.lucene.queryparser.flexible.aqp.config.AqpAdsabsQueryConfigHandler;
import org.apache.lucene.queryparser.flexible.aqp.config.AqpAdsabsQueryConfigHandler.ConfigurationKeys;
import org.apache.lucene.queryparser.flexible.aqp.config.AqpRequestParams;
import org.apache.lucene.queryparser.flexible.aqp.parser.AqpStandardQueryConfigHandler;
import org.apache.lucene.queryparser.flexible.core.QueryNodeException;
import org.apache.lucene.queryparser.flexible.core.QueryNodeParseException;
import org.apache.lucene.queryparser.flexible.core.config.QueryConfigHandler;
import org.apache.lucene.queryparser.flexible.standard.config.PointsConfig;
import org.apache.lucene.queryparser.flexible.standard.config.StandardQueryConfigHandler;
import org.apache.lucene.queryparser.flexible.standard.config.StandardQueryConfigHandler.Operator;
import org.apache.lucene.search.*;
import org.apache.solr.common.SolrException;
import org.apache.solr.common.params.CommonParams;
import org.apache.solr.common.params.DisMaxParams;
import org.apache.solr.common.params.SolrParams;
import org.apache.solr.common.util.NamedList;
import org.apache.solr.request.SolrQueryRequest;
import org.apache.solr.schema.IndexSchema;
import org.apache.solr.util.DateMathParser;
import org.apache.solr.util.SolrPluginUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.*;
import java.util.*;
import java.util.Map.Entry;

/**
 * This is the MAIN solr entry point - this instantiates 'aqp' query
 * parser - it sets some default parameters from the config and prepares
 * ulr parameters.
 *
 * @see AdsQParserPlugin
 * @see AqpAdsabsQueryConfigHandler
 * @see AqpAdsabsQueryTreeBuilder
 * @see AqpAdsabsQParser
 */
public class AqpAdsabsQParser extends QParser {

    public static TimeZone UTC = TimeZone.getTimeZone("UTC");
    public static final Logger log = LoggerFactory
            .getLogger(AqpAdsabsQParser.class);


    private final AqpQueryParser qParser;

    private String[] boostParams;
    private String[] boostFuncs;
    private String[] multBoosts;

    public AqpAdsabsQParser(AqpQueryParser parser, String qstr, SolrParams localParams,
                            SolrParams params, SolrQueryRequest req, SolrParserConfigParams defaultConfig)
            throws QueryNodeParseException {

        super(qstr, localParams, params, req);
        qParser = parser;

        if (getString() == null) {
            throw new SolrException(SolrException.ErrorCode.BAD_REQUEST,
                    "The query is empty");
        }


        IndexSchema schema = req.getSchema();


        // now configure the parser using the arguments given in config
        // and also in the request

        QueryConfigHandler config = qParser.getQueryConfigHandler();


        Map<String, String> namedParams = config.get(AqpStandardQueryConfigHandler.ConfigurationKeys.NAMED_PARAMETER);
        SolrParams solrParams = SolrParams.wrapDefaults(localParams,
                SolrParams.wrapDefaults(params, new NamedList<>(namedParams).toSolrParams()));

        // get the parameters from the parser configuration (and pass them on)
        for (Entry<String, String> par : defaultConfig.params.entrySet()) {
            String k = par.getKey();
            if (k.startsWith("aqp.")) {
                namedParams.put(k, par.getValue());
            }
        }

        // get the named parameters from solr request object (they will be passed further on)
        if (params != null) {
            for (Entry<String, Object> par : params.toNamedList()) {
                String k = par.getKey();
                if (k.startsWith("aqp.")) {
                    namedParams.put(k, (String) par.getValue());
                }
            }
        }
        if (localParams != null) {
            for (Entry<String, Object> par : localParams.toNamedList()) {
                String k = par.getKey();
                if (k.startsWith("aqp.")) {
                    namedParams.put(k, (String) par.getValue());
                }
            }
        }


        qParser.setAnalyzer(schema.getQueryAnalyzer());

        String defaultField = getParam(CommonParams.DF);
        if (defaultField == null) {
            if (namedParams.containsKey("aqp.defaultField")) {
                defaultField = namedParams.get("aqp.defaultField");
            }
        }

        if (defaultField != null) {
            config.set(AqpStandardQueryConfigHandler.ConfigurationKeys.DEFAULT_FIELD, defaultField);
        }

        // if defaultField was set, this will be useless
        if (namedParams.containsKey("aqp.unfieldedSearchField"))
            config.set(AqpAdsabsQueryConfigHandler.ConfigurationKeys.UNFIELDED_SEARCH_FIELD, namedParams.get("aqp.unfieldedSearchField"));

        // default operator
        String opParam = getParam(QueryParsing.OP);
        if (opParam == null) {
            if (namedParams.containsKey("aqp.defaultOperator")) {
                opParam = namedParams.get("aqp.defaultOperator");
            }
        }

        if (opParam != null) {
            qParser.setDefaultOperator("AND".equalsIgnoreCase(opParam) ? Operator.AND
                    : Operator.OR);
        } else {
            throw new SolrException(SolrException.ErrorCode.BAD_REQUEST,
                    "The defaultOperator is set to null");
        }


        Map<String, String> fieldMap;
        for (String fName : new String[]{"aqp.fieldMap", "aqp.fieldMapPostAnalysis"}) {
            if (fName.equals("aqp.fieldMap")) { // booo
                fieldMap = config.get(AqpStandardQueryConfigHandler.ConfigurationKeys.FIELD_MAPPER);
            } else {
                fieldMap = config.get(AqpStandardQueryConfigHandler.ConfigurationKeys.FIELD_MAPPER_POST_ANALYSIS);
            }

            if (namedParams.containsKey(fName)) {
                String[] fields = namedParams.get(fName).split(";");
                String[] ffs;
                for (String f : fields) {
                    ffs = f.split("\\s+");
                    if (ffs.length < 2) {
                        throw new SolrException(SolrException.ErrorCode.SERVER_ERROR,
                                "Configuration error in the section: " + fName);
                    }
                    String target = ffs[ffs.length - 1];
                    for (int i = 0; i < ffs.length - 1; i++) {
                        fieldMap.put(ffs[i], target);
                    }
                }
            }
        }

        AqpRequestParams reqAttr = config.get(AqpAdsabsQueryConfigHandler.ConfigurationKeys.SOLR_REQUEST);
        reqAttr.setQueryString(getString());
        reqAttr.setRequest(req);
        reqAttr.setLocalParams(localParams);
        reqAttr.setParams(params);


        // now add the special analyzer that knows to use solr token chains
        config.set(StandardQueryConfigHandler.ConfigurationKeys.ANALYZER, req.getSchema().getQueryAnalyzer());
        config.set(AqpAdsabsQueryConfigHandler.ConfigurationKeys.SOLR_READY, true);


        if (namedParams.containsKey("aqp.df.fields")) {
            qParser.setMultiFields(namedParams.get("aqp.df.fields").split(","));
        }

        // special analyzers
        config.get(ConfigurationKeys.FUNCTION_QUERY_BUILDER_CONFIG).addProvider(0, new AqpSolrFunctionProvider());
        config.get(ConfigurationKeys.FUNCTION_QUERY_BUILDER_CONFIG).addProvider(1, new AqpAdsabsSubQueryProvider());
        config.get(ConfigurationKeys.FUNCTION_QUERY_BUILDER_CONFIG).addProvider(2, new AqpAdsabsFunctionProvider());

        if (namedParams.containsKey("aqp.authorFields")) {
            for (String f : namedParams.get("aqp.authorFields").split(",")) {
                config.get(AqpAdsabsQueryConfigHandler.ConfigurationKeys.AUTHOR_FIELDS).put(f, new int[0]);
            }
        }

        if (params.getBool("debugQuery", false)) {
            try {
                qParser.setDebug(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


        HashMap<String, PointsConfig> ncm = new HashMap<String, PointsConfig>();
        config.set(StandardQueryConfigHandler.ConfigurationKeys.POINTS_CONFIG_MAP, ncm);

        if (namedParams.containsKey("aqp.floatFields")) {
            for (String f : namedParams.get("aqp.floatFields").split(",")) {
                ncm.put(f, new PointsConfig(new MaxNumberFormat(Float.MAX_VALUE), Float.class));
            }
        }

        if (namedParams.containsKey("aqp.dateFields")) {
            SimpleDateFormat sdf = new SimpleDateFormat(namedParams.containsKey("aqp.dateFormat")
                    ? namedParams.get("aqp.dateFormat") : "yyyy-MM-dd'T'HH:mm:ss", Locale.ROOT);
            sdf.setTimeZone(UTC);
            for (String f : namedParams.get("aqp.dateFields").split(",")) {
                ncm.put(f, new PointsConfig(new MaxNumberFormat(new NumberDateFormat(sdf), Long.MAX_VALUE), Long.class));
            }
        }

        if (namedParams.containsKey("aqp.timestampFields")) {
            SimpleDateFormat sdf = new SimpleDateFormat(namedParams.containsKey("aqp.timestampFormat")
                    ? namedParams.get("aqp.timestampFormat") : "yyyy-MM-dd'T'HH:mm:ss.SSS", Locale.ROOT);
            sdf.setTimeZone(UTC);
            for (String f : namedParams.get("aqp.timestampFields").split(",")) {
                ncm.put(f, new PointsConfig(new MaxNumberFormat(new NumberDateFormat(sdf), Long.MAX_VALUE), Long.class));
            }
        }

        // when precision step=0 (ie use the default solr value), then it is Integer.MAX_VALUE
        if (namedParams.containsKey("aqp.intFields")) {
            for (String f : namedParams.get("aqp.intFields").split(",")) {
                ncm.put(f, new PointsConfig(new MaxNumberFormat(Integer.MAX_VALUE), Integer.class));
            }
        }

        config.get(AqpAdsabsQueryConfigHandler.ConfigurationKeys.VIRTUAL_FIELDS).putAll(defaultConfig.virtualFields);

        if (params.get("aqp.allow.leading_wildcard", null) != null) {
            config.set(StandardQueryConfigHandler.ConfigurationKeys.ALLOW_LEADING_WILDCARD,
                    params.getBool("aqp.allow.leading_wildcard", false));
        }

        // Boost factors
        boostParams = solrParams.getParams(DisMaxParams.BQ);

        boostFuncs = solrParams.getParams(DisMaxParams.BF);

        multBoosts = solrParams.getParams(AqpExtendedDismaxQParser.DMP.MULT_BOOST);
    }

    public class NumberDateFormat extends NumberFormat {

        private static final long serialVersionUID = -4334084585068547470L;
        final private DateFormat dateFormat;

        /**
         * Constructs a {@link NumberDateFormat} object using the given {@link DateFormat}.
         *
         * @param dateFormat {@link DateFormat} used to parse and format dates
         */
        public NumberDateFormat(DateFormat dateFormat) {
            this.dateFormat = dateFormat;
        }

        @Override
        public StringBuffer format(double number, StringBuffer toAppendTo,
                                   FieldPosition pos) {
            return dateFormat.format(new Date((long) number), toAppendTo, pos);
        }

        @Override
        public StringBuffer format(long number, StringBuffer toAppendTo,
                                   FieldPosition pos) {
            return dateFormat.format(new Date(number), toAppendTo, pos);
        }

        @Override
        public Number parse(String source, ParsePosition parsePosition) {
            final Date date = dateFormat.parse(source, parsePosition);
            return (date == null) ? null : date.getTime();
        }

        @Override
        public StringBuffer format(Object number, StringBuffer toAppendTo,
                                   FieldPosition pos) {
            return dateFormat.format(number, toAppendTo, pos);
        }

        public DateFormat getFormatter() {
            return dateFormat;
        }

    }

    /**
     * Internal class that allows us to accept '*' (lucene's way of saying 'anything')
     */
    private class MaxNumberFormat extends NumberFormat {
        private static final long serialVersionUID = -407706279343648005L;
        private NumberFormat parser = null;
        private Number max = null;
        private final DateMathParser dParser = new DateMathParser(UTC);

        public MaxNumberFormat(Number max) {
            this(NumberFormat.getNumberInstance(Locale.US), max);

        }

        public MaxNumberFormat(NumberFormat parser, Number max) {
            this.parser = parser;
            this.max = max;
        }


        @Override
        public StringBuffer format(double number, StringBuffer toAppendTo, FieldPosition pos) {
            return parser.format(number, toAppendTo, pos);
        }

        @Override
        public StringBuffer format(long number, StringBuffer toAppendTo, FieldPosition pos) {
            return parser.format(number, toAppendTo, pos);
        }

        @Override
        public Number parse(String source, ParsePosition parsePosition) {
            if (source.contains("*")) {
                parsePosition.setIndex(1);
                return max;
            }
            // it might be a symbolic date math
            try {
                Date date = DateMathParser.parseMath(null, source);
                DateFormat formatter = ((NumberDateFormat) parser).getFormatter();
                source = formatter.format(date);
            } catch (SolrException e) {
                // pass
            }

            return parser.parse(source, parsePosition);
        }

    }

    public Query parse() throws SyntaxError {
        try {
            //if (qstr.trim().endsWith(",") && !qstr.trim().endsWith("\\,")) {
            //  QueryConfigHandler config = qParser.getQueryConfigHandler();
            //  return qParser.parse(getString() + config.get(AqpAdsabsQueryConfigHandler.ConfigurationKeys.DUMMY_VALUE), null);
            //}

            Query userQuery = qParser.parse(getString(), null);
            Query topQuery = userQuery;

            if (boostParams != null || boostFuncs != null) {
                BooleanQuery.Builder builder = new BooleanQuery.Builder();
                builder.add(userQuery, BooleanClause.Occur.MUST);

                for (Query q : getBoostQueries()) {
                    builder.add(q, BooleanClause.Occur.SHOULD);
                }

                topQuery = builder.build();
            }

            if (multBoosts != null) {
                List<ValueSource> boosts = getMultiplicativeBoosts();
                DoubleValuesSource multiplicativeBoostSource =
                        boosts.size() > 1
                                ? new ProductFloatFunction(boosts.toArray(new ValueSource[0])).asDoubleValuesSource()
                                : boosts.get(0).asDoubleValuesSource();

                topQuery = FunctionScoreQuery.boostByValue(topQuery, multiplicativeBoostSource);
            }

            return topQuery;
        } catch (QueryNodeException e) {
            throw new SyntaxError(e);
        } catch (SolrException e1) {
            throw new SyntaxError(e1);
        }
    }

    public AqpQueryParser getParser() {
        return qParser;
    }

    /**
     * Parses all multiplicative boosts
     */
    protected List<ValueSource> getMultiplicativeBoosts() throws SyntaxError {
        List<ValueSource> boosts = new ArrayList<>();

        for (String boostStr : multBoosts) {
            if (boostStr == null || boostStr.isBlank()) continue;

            Query boost = subQuery(boostStr, FunctionQParserPlugin.NAME).getQuery();

            ValueSource vs;
            if (boost instanceof FunctionQuery) {
                vs = ((FunctionQuery) boost).getValueSource();
            } else {
                vs = new QueryValueSource(boost, 1.0f);
            }

            boosts.add(vs);
        }

        return boosts;
    }

    /**
     * Parses all additive boost fields
     */
    protected List<Query> getBoostQueries() throws SyntaxError {
        List<Query> boostQueries = new LinkedList<>();

        for (String boostParam : boostParams) {
            if (boostParam == null || boostParam.trim().isBlank()) continue;

            Query q = subQuery(boostParam, null).getQuery();
            boostQueries.add(q);
        }

        return boostQueries;
    }
}
