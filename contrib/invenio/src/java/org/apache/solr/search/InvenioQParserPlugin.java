package org.apache.solr.search;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.lucene.analysis.WhitespaceAnalyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.queryParser.aqp.AqpInvenioQueryParser;
import org.apache.lucene.queryParser.core.QueryNodeException;
import org.apache.lucene.queryParser.core.QueryNodeParseException;
import org.apache.lucene.queryParser.standard.config.DefaultOperatorAttribute.Operator;
import org.apache.solr.common.SolrException;
import org.apache.solr.common.params.CommonParams;
import org.apache.solr.common.params.DefaultSolrParams;
import org.apache.solr.common.params.SolrParams;
import org.apache.solr.common.util.NamedList;
import org.apache.solr.request.SolrQueryRequest;
import org.apache.solr.schema.FieldType;
import org.apache.solr.schema.IndexSchema;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.ConstantScoreQuery;
import org.apache.lucene.search.FuzzyQuery;
import org.apache.lucene.search.NumericRangeQuery;
import org.apache.lucene.search.PhraseQuery;
import org.apache.lucene.search.PrefixQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TermRangeQuery;
import org.apache.lucene.search.WildcardQuery;
import org.apache.lucene.util.Version;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.apache.solr.search.QueryParsing;



/**
 * Parse query that is made of the solr fields as well as Invenio query syntax,
 * the field that are prefixed using the special code <code>inv_</code> get
 * automatically passed to Invenio
 *
 * Other parameters:
 * <ul>
 * <li>q.op - the default operator "OR" or "AND"</li>
 * <li>df - the default field name</li>
 * </ul>
 * <br>
 * Example: <code>{!iq mode=maxinv xfields=fulltext}035:arxiv +bar -baz</code>
 *
 * The example above would query everything as Invenio field, but fulltext will
 * be served by Solr.
 *
 * Example:
 * <code>{!iq iq.mode=maxsolr iq.xfields=fulltext,abstract iq.channel=bitset}035:arxiv +bar -baz</code>
 *
 * The example above will try to map all the fields into the Solr schema, if the
 * field exists, it will be served by Solr. The fulltext will be served by
 * Invenio no matter if it is defined in schema. And communication between Java
 * and Invenio is done using bitset
 *
 * If the query is written as:<code>inv_field:value</code> the search will be
 * always passed to Invenio.
 *
 */
public class InvenioQParserPlugin extends QParserPlugin {
	public static String NAME = "iq";
	public static String FIELDNAME = "InvenioQuery";
	public static String PREFIX = "inv_";
	public static String IDFIELD = "id";

	@Override
	public void init(NamedList args) {
	}

	@Override
	public QParser createParser(String qstr, SolrParams localParams,
			SolrParams params, SolrQueryRequest req) {
		return new InvenioQParser(qstr, localParams, params, req);
	}

}

class InvenioQParser extends QParser {

	public static final Logger log = LoggerFactory
			.getLogger(InvenioQParser.class);

	public static Pattern fieldPattern = Pattern
			.compile("\\b([a-zA-Z_0-9]+)\\:");

	String sortStr;
	SolrQueryParser lparser;
	ArrayList<String> xfields = null;

	private String operationMode = "maxinvenio";
	private String exchangeType = "ints";
	private String querySyntax = "invenio";
	private IndexSchema schema = null;

	public InvenioQParser(String qstr, SolrParams localParams,
			SolrParams params, SolrQueryRequest req) {
		super(qstr, localParams, params, req);

		SolrParams solrParams = localParams == null ? params : new DefaultSolrParams(localParams, params);

		schema = req.getSchema();

		String m = solrParams.get("iq.mode");
		if (m != null ) {
			if (m.contains("maxinv") && !schema.hasExplicitField("*")) {
				throw new SolrException(
						SolrException.ErrorCode.SERVER_ERROR,
						"Query parser is configured to pass as many fields to Invenio as possible," +
						" for this to work, schema must contain a dynamic field declared as '*'" +
						"<dynamicField name=\"*\" type=\"text\" multiValued=\"true\" />");
			}
			operationMode = m;
		}


		xfields = new ArrayList<String>();
		String[] overriden_fields = solrParams.getParams("iq.xfields");
		if (overriden_fields != null) {
			for (String f: overriden_fields) {
				if (f.indexOf(",") > -1) {
					for (String x: f.split(",")) {
						xfields.add(x);
					}
				}
				else {
					xfields.add(f);
				}
			}
		}

		String eType = solrParams.get("iq.channel", "default");
		if (eType.contains("bitset")) {
			exchangeType = "bitset";
		}

		String sType = solrParams.get("iq.syntax", "invenio");
		if (sType.contains("lucene")) {
			querySyntax = "lucene";
		}
	}

	public Query parse() throws ParseException {

		if (getString() == null) {
			throw new ParseException("The query parameter is empty");
		}

		setString(normalizeInvenioQuery(getString()));
		String qstr = getString();



		// detect field not in the schema, but only if we are not in the
		// all-tracking mode (because in that mode we can do it much smarter and
		// without regex)
		if (operationMode.equals("maxsolr") && !schema.hasExplicitField("*")) {
			String q2 = changeInvenioQuery(req, qstr);
			if (!q2.equals(qstr)) {
				log.info(qstr + "  -->  " + q2);
				setString(q2);
			}
		}


		String defaultField = getParam(CommonParams.DF);
		if (defaultField == null) {
			defaultField = getReq().getSchema().getDefaultSearchFieldName();
		}


		// Now use the specific parser to fight with the syntax
		Query mainq;

		if (querySyntax.equals("invenio")) {
			AqpInvenioQueryParser invParser;
			try {
				invParser = new AqpInvenioQueryParser();
			} catch (QueryNodeParseException e1) {
				e1.printStackTrace();
				throw new ParseException(e1.getLocalizedMessage());
			}
			invParser.setAnalyzer(schema.getAnalyzer());
			String opParam = getParam(QueryParsing.OP);
			if (opParam != null) {
				invParser.setDefaultOperator("AND".equals(opParam) ? Operator.AND : Operator.OR);
			} else {
				// try to get default operator from schema
				QueryParser.Operator operator = getReq().getSchema()
						.getSolrQueryParser(null).getDefaultOperator();
				invParser.setDefaultOperator(null == operator ? Operator.OR
						: (operator == QueryParser.AND_OPERATOR ? Operator.AND : Operator.OR));
			}
			try {
				mainq = invParser.parse(getString(), schema.getDefaultSearchFieldName());
			} catch (QueryNodeException e) {
				throw new ParseException(e.getMessage());
			}
		}
		else {

			lparser = new SolrQueryParser(this, defaultField);
			// these could either be checked & set here, or in the SolrQueryParser
			// constructor
			String opParam = getParam(QueryParsing.OP);
			if (opParam != null) {
				lparser.setDefaultOperator("AND".equals(opParam) ? QueryParser.Operator.AND
						: QueryParser.Operator.OR);
			} else {
				// try to get default operator from schema
				QueryParser.Operator operator = getReq().getSchema()
						.getSolrQueryParser(null).getDefaultOperator();
				lparser.setDefaultOperator(null == operator ? QueryParser.Operator.OR
						: operator);
			}
			mainq = lparser.parse(getString());
		}
		/**
		else {
			StandardQueryParser qpHelper = new StandardQueryParser();
			qpHelper.setAllowLeadingWildcard(true);
			qpHelper.setAnalyzer(new StandardAnalyzer());
			try {
				mainq = qpHelper.parse(getString(), schema.getDefaultSearchFieldName());
			} catch (QueryNodeException e) {
				throw new ParseException();
			}
		}
		**/

		Query mainq2;
		try {
			mainq2 = rewriteQuery(mainq, 0);
			if (!mainq2.equals(mainq)) {
				log.info(getString() + " --> " + mainq2.toString());
				mainq = mainq2;
			}
		} catch (IOException e) {
			throw new ParseException();
		}

		return mainq;
	}

	private Pattern weird_or = Pattern.compile("( \\|)([a-zA-Z\"])");
	private String normalizeInvenioQuery(String q) {
		try {
		Matcher matcher = weird_or.matcher(q);
			q = matcher.replaceAll(" || $2");
		}
		catch (Exception e) {
			System.out.println(q);
		}
		q = q.replace("refersto:", "refersto\\:");
		q = q.replace("citedby:", "citedby\\:");
		q = q.replace("cited:", "cited\\:");
		q = q.replace("cocitedwith:", "cocitedwith\\:");
		q = q.replace("reportnumber:", "reportnumber\\:");
		q = q.replace("reference:", "reference\\:");
		return q;

	}

	/**
	 * Help method to change query into invenio fields (if the field is not defined
	 * in the schema, it is considered to be Invenio). However we use this simplistic
	 * rewriting only when '*' is not activated and when iq.mode=maxsolr
	 * @param req
	 * @param q
	 * @return
	 */
	private String changeInvenioQuery(SolrQueryRequest req, String q) {
		IndexSchema schema = req.getSchema();
		// SolrQueryParser qparser = new SolrQueryParser(schema, "all");
		// log.info(qparser.escape(q));

		// leave this to invenio
		// q = q.replace("refersto:", "{!relation rel=refersto}");
		// q = q.replace("citedby:", "{!relation rel=citedby}");
		q = q.replace("journal:", "publication:");
		q = q.replace("arXiv:", "reportnumber:");

		String q2 = q;

		Matcher matcher = fieldPattern.matcher(q);
		while (matcher.find()) {
			String field = q.substring(matcher.start(), matcher.end() - 1);
			try {
				if (schema.getFieldType(field) != null) {
					continue;
				}
			} catch (SolrException e) {
				// pass - not serious
			}
			q2 = q2.replace(field + ":", InvenioQParserPlugin.PREFIX + field
					+ ":");
		}
		return q2;
	}

	/**
	 * Returns a field (string) IFF we should pass the query to Invenio.
	 *
	 * @param field
	 * @return
	 * @throws ParseException
	 */
	private String getInvField(String field) throws ParseException {
		String v = null;
		// always consider it as Invenio field if the prefix is present
		if (field.startsWith(InvenioQParserPlugin.PREFIX)) {
			v = field.substring(InvenioQParserPlugin.PREFIX.length());
			return v;
		}


		// consider it as solr field if it is in the schema
		if (operationMode.equals("maxsolr")) {
			if(schema.hasExplicitField(field) && xfields.indexOf(field) == -1) {
				return null;
			}
			return field; // consider it Invenio field
		}
		else { // pass all fields to Invenio
			if (xfields.indexOf(field) > -1) { // besides explicitly solr fields
				if (!schema.hasExplicitField(field)) {
					throw new ParseException("The field '" + field + "' is not defined for Solr.");
				}
				return null;
			}
			return field;
		}

	}


	private Query createInvenioQuery(String field, String value, String idField) {
		Query newQuery = null;
		String newField = field;
		if (field.equals(schema.getDefaultSearchFieldName())) {
			newField = "";
		}
		
		//if (exchangeType.equals("bitset")) { // TODO: check
		return new SolrInvenioQuery(new TermQuery(new Term(newField, value)), req, localParams, idField);
	}


	/** @see #QueryParsing.toString(Query,IndexSchema) */
	public Query rewriteQuery(Query query, int flags) throws IOException,
			ParseException {

		boolean writeBoost = true;

		Query newQuery = null;

		SolrIndexReader reader = req.getSearcher().getReader();

		StringBuffer out = new StringBuffer();
		if (query instanceof TermQuery) {
			TermQuery q = (TermQuery) query;
			Term t = q.getTerm();
			String invf = getInvField(t.field());
			if (invf != null) {
				newQuery = createInvenioQuery(invf, t.text(), InvenioQParserPlugin.IDFIELD);
			}

		} else if (query instanceof TermRangeQuery) {
			TermRangeQuery q = (TermRangeQuery) query;
			String invf = getInvField(q.getField());
			if (invf != null) {
				String fname = q.getField();
				FieldType ft = QueryParsing.writeFieldName(invf, schema, out,
						flags);
				out.append(q.includesLower() ? '[' : '{');
				String lt = q.getLowerTerm();
				String ut = q.getUpperTerm();
				if (lt == null) {
					out.append('*');
				} else {
					QueryParsing.writeFieldVal(lt, ft, out, flags);
				}

				out.append(" TO ");

				if (ut == null) {
					out.append('*');
				} else {
					QueryParsing.writeFieldVal(ut, ft, out, flags);
				}

				out.append(q.includesUpper() ? ']' : '}');

				// newQuery = new
				// TermRangeQuery(q.getField().replaceFirst(PREFIX, ""),
				// q.getLowerTerm(), q.getUpperTerm(),
				// q.includesLower(), q.includesUpper());
				newQuery = createInvenioQuery(invf, out.toString(), InvenioQParserPlugin.IDFIELD);
			}

		} else if (query instanceof NumericRangeQuery) {
			NumericRangeQuery q = (NumericRangeQuery) query;
			String invf = getInvField(q.getField());
			if (invf != null) {
				String fname = q.getField();
				FieldType ft = QueryParsing.writeFieldName(invf, schema, out,
						flags);
				out.append(q.includesMin() ? '[' : '{');
				Number lt = q.getMin();
				Number ut = q.getMax();
				if (lt == null) {
					out.append('*');
				} else {
					out.append(lt.toString());
				}

				out.append(" TO ");

				if (ut == null) {
					out.append('*');
				} else {
					out.append(ut.toString());
				}

				out.append(q.includesMax() ? ']' : '}');
				newQuery = createInvenioQuery(invf, out.toString(), InvenioQParserPlugin.IDFIELD);


				// TODO: Invneio is using int ranges only, i think, but we shall
				// not hardcode it here
				// SchemaField ff =
				// schema.getField(q.getField().substring(PREFIX.length()));
				// newQuery = NumericRangeQuery.newIntRange(ff.getName(),
				// (Integer)q.getMin(), (Integer)q.getMax(),
				// q.includesMin(), q.includesMax());
			}

		} else if (query instanceof BooleanQuery) {
			BooleanQuery q = (BooleanQuery) query;
			newQuery = new BooleanQuery();

			List<BooleanClause>clauses = (List<BooleanClause>) q.clauses();

			Query subQuery;
			for (int i=0;i<clauses.size();i++) {
				BooleanClause c = clauses.get(i);
				Query qq = c.getQuery();
				subQuery = rewriteQuery(qq, flags);
				BooleanClause newClause = new BooleanClause(subQuery,
						c.getOccur());
				((BooleanQuery) newQuery).add(newClause);
			}
			((BooleanQuery) newQuery).setMinimumNumberShouldMatch(q
					.getMinimumNumberShouldMatch());

		} else if (query instanceof PrefixQuery) {
			PrefixQuery q = (PrefixQuery) query;
			Term prefix = q.getPrefix();
			String invf = getInvField(prefix.field());
			if (invf != null) {
				//FieldType ft = QueryParsing.writeFieldName(invf, schema, out,
				//		flags);
				out.append(prefix.text());
				out.append('*');
				newQuery = createInvenioQuery(invf, out.toString(), InvenioQParserPlugin.IDFIELD);

			}

		} else if (query instanceof ConstantScorePrefixQuery) {
			ConstantScorePrefixQuery q = (ConstantScorePrefixQuery) query;
			Term prefix = q.getPrefix();
			String invf = getInvField(prefix.field());
			if (invf != null) {
				//FieldType ft = QueryParsing.writeFieldName(invf, schema, out,
				//		flags);
				out.append(prefix.text());
				out.append('*');
				newQuery = createInvenioQuery(invf, out.toString(), InvenioQParserPlugin.IDFIELD);

			}
		} else if (query instanceof WildcardQuery) {
			WildcardQuery q = (WildcardQuery) query;
			Term t = q.getTerm();
			String invf = getInvField(t.field());
			if (invf != null) {
				//FieldType ft = QueryParsing.writeFieldName(invf, schema, out,
				//		flags);
				out.append(t.text());
				newQuery = createInvenioQuery(invf, out.toString(), InvenioQParserPlugin.IDFIELD); //TODO: is this correct?
			}
		} else if (query instanceof PhraseQuery) {
			PhraseQuery q = (PhraseQuery) query;
			Term[] terms = q.getTerms();
			String invf = getInvField(terms[0].field());
			if (invf != null) {
				out.append(q.getSlop() > 0 ? "'" : "\"");
				for (int i=0;i<terms.length;i++) {
					if (i != 0) {
						out.append(" ");
					}
					out.append(((Term)terms[i]).text());
				}
				out.append(q.getSlop() > 0 ? "'" : "\"");
				newQuery = createInvenioQuery(invf, out.toString(), InvenioQParserPlugin.IDFIELD); //TODO: is this correct?
			}

		} else if (query instanceof FuzzyQuery) {
			// do nothing
		} else if (query instanceof ConstantScoreQuery) {
			// do nothing
		} else {
			// do nothing
		}

		if (newQuery != null) {
			if (writeBoost && query.getBoost() != 1.0f) {
				newQuery.setBoost(query.getBoost());
			}
			return newQuery;
		} else {
			return query;
		}

	}

	public String[] getDefaultHighlightFields() {
		return new String[] { lparser.getField() };
	}

}
