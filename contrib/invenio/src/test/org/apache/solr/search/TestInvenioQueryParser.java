package org.apache.solr.search;

/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import invenio.montysolr.util.MontySolrAbstractTestCase;
import invenio.montysolr.util.MontySolrSetup;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.SimpleAnalyzer;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.aqp.AqpQueryParser;
import org.apache.lucene.queryParser.standard.config.DefaultOperatorAttribute.Operator;
import org.apache.lucene.search.Query;
import org.apache.solr.common.params.CommonParams;
import org.apache.solr.common.params.ModifiableSolrParams;
import org.apache.solr.common.params.SolrParams;
import org.apache.solr.core.SolrCore;
import org.apache.solr.request.SolrQueryRequest;
import org.apache.solr.search.InvenioQParserPlugin;
import org.junit.BeforeClass;

public class TestInvenioQueryParser extends MontySolrAbstractTestCase {

	protected boolean debugParser = false;
	protected String grammarName = "Invenio";
	
	@BeforeClass
	public static void beforeClassMontySolrTestCase() throws Exception {
		MontySolrSetup.init("montysolr.java_bridge.SimpleBridge", 
				MontySolrSetup.getMontySolrHome() + "/src/python");
		MontySolrSetup.addToSysPath(MontySolrSetup.getMontySolrHome() + "/contrib/invenio/src/python");
		MontySolrSetup.addTargetsToHandler("monty_invenio.targets");
	}
	
	
	public String getSchemaFile() {
		return MontySolrSetup.getMontySolrHome() + 
		"/contrib/invenio/src/test-files/solr/conf/schema-minimal.xml";
	}

	
	public String getSolrConfigFile() {
		return MontySolrSetup.getMontySolrHome() + 
		"/contrib/invenio/src/test-files/solr/conf/solrconfig-invenio-query-parser.xml";
	}


	public String getCoreName() {
		return "basic";
	}

	public AqpQueryParser getParser(Analyzer a) throws Exception {
		if (a == null)
			a = new SimpleAnalyzer(TEST_VERSION_CURRENT);
		AqpQueryParser qp = getParser();
		qp.setAnalyzer(a);
		qp.setDefaultOperator(Operator.OR);
		qp.setDebug(this.debugParser);
		return qp;

	}

	public void setDebug(boolean d) {
		debugParser = d;
	}

	public void setGrammarName(String name) {
		grammarName = name;
	}

	public String getGrammarName() {
		return grammarName;
	}

	public AqpQueryParser getParser() throws Exception {
		return new AqpQueryParser(getGrammarName());
	}

	public Query getQuery(String query, Analyzer a) throws Exception {
		return getParser(a).parse(query, "field");
	}

	public void assertQueryEquals(SolrQueryRequest req, String result) throws Exception {
		assertQueryEquals(req, null, result);
	}

	public void assertQueryEquals(SolrQueryRequest req, Analyzer a, String result)
			throws Exception {
		SolrParams params = req.getParams();
		String query = params.get(CommonParams.Q);
		QParser qParser = QParser.getParser(query, "iq", req);
		
		if (qParser instanceof InvenioQParser) {
			AqpQueryParser p = ((InvenioQParser) qParser).getParser();
			p.setDebug(this.debugParser);
			if (a != null) {
				p.setAnalyzer(a);
			}
		}
		
		Query q = qParser.parse();
		
		String s = q.toString("field");
		if (!s.equals(result)) {
			fail("Query /" + query + "/ yielded /" + s + "/, expecting /"
					+ result + "/");
		}
	}
	
	public void testLocalParams() throws Exception {
		
		assertQueryEquals(req("q", "#title:invenio AND fulltext:solr",
				"iq.channel", "intbit"), 
				"+<(intbitset,recid)title:invenio> +fulltext:solr");
		assertQueryEquals(req("q", "#title:invenio AND fulltext:solr",
				"iq.channel", "default"), 
				"+<(ints,recid)title:invenio> +fulltext:solr");
		assertQueryEquals(req("q", "#title:invenio AND fulltext:solr"), 
				"+<(ints,recid)title:invenio> +fulltext:solr");
		assertQueryEquals(req("q", "title:invenio OR fulltext:solr",
				"iq.channel", "intbitset"), 
				"title:invenio fulltext:solr");
		
		
		assertQueryEquals(req("q", "title:invenio AND fulltext:solr",
				"iq.mode", "maxinv"), 
				"+<(ints,recid)title:invenio> +<(ints,recid)fulltext:solr>");
		assertQueryEquals(req("q", "title:invenio AND fulltext:solr",
				"iq.mode", "maxinv", "iq.xfields", "fulltext,text"), 
				"+<(ints,recid)title:invenio> +fulltext:solr");
		assertQueryEquals(req("q", "title:invenio AND fulltext:solr",
				"iq.mode", "maxsolr", "iq.xfields", "fulltext,text"), 
				"+title:invenio +<(ints,recid)fulltext:solr>");
		assertQueryEquals(req("q", "title:invenio AND fulltext:solr",
				"iq.mode", "maxsolr", "iq.xfields", "foo,bar"), 
				"+title:invenio +fulltext:solr");
		assertQueryEquals(req("q", "title:invenio AND fulltext:solr",
				"iq.mode", "maxinv", "iq.xfields", "fulltext,text",
				"iq.channel", "intbit"), 
				"+<(intbitset,recid)title:invenio> +fulltext:solr");
		
		setDebug(true);
		assertQueryEquals(req("q", "invenio AND fulltext:solr",
				"iq.mode", "maxinv"), 
				"+<(ints,recid)invenio> +<(ints,recid)fulltext:solr>");
		assertQueryEquals(req("q", "invenio AND fulltext:solr",
				"iq.mode", "maxinv", "iq.xfields", "fulltext,foo"), 
				"+<(ints,recid)invenio> +fulltext:solr");
	}

	
	
	public void xtestQueryTypes() {
		assertU(adoc("id", "1", "v_t", "Hello Dude"));
		assertU(adoc("id", "2", "v_t", "Hello Yonik"));
		assertU(adoc("id", "3", "v_s", "{!literal}"));
		assertU(adoc("id", "4", "v_s", "other stuff"));
		assertU(adoc("id", "5", "v_f", "3.14159"));
		assertU(adoc("id", "6", "v_f", "8983"));
		assertU(adoc("id", "7", "v_f", "1.5"));
		assertU(adoc("id", "8", "v_ti", "5"));
		assertU(commit());

		Object[] arr = new Object[] { "id", 999.0, "v_s", "wow dude", "v_t",
				"wow", "v_ti", -1, "v_tis", -1, "v_tl", -1234567891234567890L,
				"v_tls", -1234567891234567890L, "v_tf", -2.0f, "v_tfs", -2.0f,
				"v_td", -2.0, "v_tds", -2.0, "v_tdt", "2000-05-10T01:01:01Z",
				"v_tdts", "2002-08-26T01:01:01Z" };

		SolrCore core = h.getCore();
		SolrQueryRequest req = lrf.makeRequest("q", "*:*");
		SolrParams params = req.getParams();
		ModifiableSolrParams localParams = new ModifiableSolrParams();
		localParams.add("iq.mode", "maxinv");
		localParams.add("iq.xfields", "fulltext,abstract");
		localParams.add("iq.syntax", "lucene");

		ModifiableSolrParams localParams_2 = new ModifiableSolrParams();
		localParams_2.add("iq.mode", "maxinv");
		localParams_2.add("iq.xfields", "fulltext,abstract");
		localParams_2.add("iq.syntax", "invenio");

		assertTrue("core is null and it shouldn't be", core != null);
		QParserPlugin parserPlugin = core
				.getQueryPlugin(InvenioQParserPlugin.NAME);

		String[] queries = {

				"author:hawking  and affiliation:\"cambridge u., damtp\" and year:2004->9999",
				"<author|hawking> +<affiliation|\"cambridge u., damtp\"> +<year|2004->9999>",

				// test cases
				"hey |muon",
				"<hey> <muon>",
				"hey |\"muon muon\"",
				"<hey> <\"muon muon\">",
				"\"and or not AND OR NOT\"  and phrase",
				"<\"and or not and or not\"> +<phrase>",

				// http://inspirebeta.net/help/search-tips

				// find a hawking and aff "cambridge u., damtp" and date > 2004
				"author:hawking  and affiliation:\"cambridge u., damtp\" and year:2004->9999",
				"<author|hawking> +<affiliation|\"cambridge u., damtp\"> +<year|2004->9999>",
				"thomas crewther quark 2002",
				"<thomas> <crewther> <quark> <2002>",
				// find j phys.rev.lett.,62,1825
				"journal:phys.rev.lett.,62,1825",
				"<journal|phys.rev.lett.,62,1825>",
				// find j "Phys.Rev.Lett.,105*" or j Phys.Lett. and a thomas
				"journal:\"Phys.Rev.Lett.,105*\" or journal:Phys.Lett. and author:thomas",
				"<journal|phys.rev.lett.,105*> <journal|phys.lett.> +<author|thomas>",
				// find d 1997-11-18
				"year:1997-11-18",
				"<year|1997-11-18>",
				// find da 2011-01-26 and title neutrino*
				"datecreated:2011-01-26  and title:neutrino*",
				"<datecreated|2011-01-26> +<title|neutrino*>",
				// find eprint arxiv:0711.2908 or arxiv:0705.4298 or eprint
				// hep-ph/0504227
				"reportnumber:arxiv:0711.2908 or arxiv:0705.4298 or reportnumber:hep-ph/0504227",
				"",
				// find a unruh or t cauchy not t problem and primarch gr-qc
				"author:unruh   or title:cauchy  not title:problem and 037__c:gr-qc",
				"<author|unruh> <title|cauchy> -<title|problem> +<037__c|gr-qc>",
				// find a m albrow and j phys.rev.lett. and t quark*
				// cited:200->99999
				"(author:\"albrow, m*\")  and journal:phys.rev.lett.  and (title:quark* and title:cited:200->99999)",
				"",
				// find c Phys.Rev.Lett.,28,1421 or c arXiv:0711.4556
				"reference:Phys.Rev.Lett.,28,1421 or reference:arXiv:0711.4556",
				"",
				// find c "Phys.Rev.Lett.,*"
				"reference:\"Phys.Rev.Lett.,*\"",
				"<reference|phys.rev.lett.,*>",
				// citedby:hep-th/9711200 author:cvetic
				"citedby:hep-th/9711200 author:cvetic",
				"<citedby|hep-th/9711200> <author|cvetic>",
				"author:parke citedby:author:witten",
				"",
				"refersto:hep-th/9711200 title:nucl*",
				"<refersto|hep-th/9711200> <title|nucl*>",
				"author:witten refersto:author:\"parke, s j\"",
				"",
				"refersto:author:parke or refersto:author:lykken author:witten",
				"",
				"affiliation:\"oxford u.\" refersto:title:muon*",
				"",
				// find af "harvard u."
				"affiliation:\"harvard u.\"",
				"<affiliation|\"harvard u.\">",

				// http://inspirebeta.net/help/search-guide

				"\"Ellis, J\"",
				"<\"ellis, j\">",
				"'muon decay'",
				"<'muon decay'>",
				"'Ellis, J'",
				"<'ellis, j'>",
				"ellis +muon",
				"<ellis> +<muon>",
				"ellis muon",
				"<ellis> <muon>",
				"ellis and muon",
				"<ellis> +<muon>",
				"ellis -muon",
				"<ellis> -<muon>",
				"ellis not muon",
				"<ellis> -<muon>",
				"ellis |muon",
				"<ellis> <muon>",
				"ellis or muon",
				"<ellis> <muon>",
				"muon or kaon and ellis",
				"<muon> <kaon> +<ellis>",
				"ellis and muon or kaon",
				"<ellis> +<muon> <kaon>",
				"muon or kaon and ellis -decay",
				"<muon> <kaon> +<ellis> -<decay>",
				"(gravity OR supergravity) AND (ellis OR perelstein)",
				"(<gravity> <supergravity>) +(<ellis> <perelstein>)",
				"C++",
				"<c++>",
				"O'Shea",
				"<o'shea>",
				"$e^{+}e^{-}$",
				"<$e^{+}e^{-}$>",
				"hep-ph/0204133",
				"<hep-ph/0204133>",
				"BlaCK hOlEs",
				"<black> <holes>",
				"пушкин",
				"<пушкин>",
				"muon*",
				"<muon*>",
				"CERN-TH*31",
				"<cern-th*31>",
				"a*",
				"<a*>",
				"\"Neutrino mass*\"",
				"<\"neutrino mass*\">",
				"author:ellis",
				"<author|ellis>",
				"author:ellis title:muon*",
				"<author|ellis> <title|muon*>",
				"experiment:NA60 year:2001",
				"<experiment|na60> <year|2001>",
				"title:/^E.*s$/",
				"<title|/^e.*s$/>",
				"author:/^Ellis, (J|John)$/",
				"<author|\"/^ellis, (j|john)$/\">",
				"title:/dense ([^ l]* )?matter/",
				"<title|\"/dense ([^ l]* )?matter/\">", // TODO: remove the
														// quotation marks
				"collection:PREPRINT -year:/^[0-9]{4}([\\?\\-]|\\-[0-9]{4})?$/",
				"<collection|preprint> -<year|/^[[:digit:]]{4}([?-]|-[[:digit:]]{4})?$/>",
				"collection:PREPRINT -year:/^[[:digit:]]{4}([\\?\\-]|\\-[[:digit:]]{4})?$/",
				"<collection|preprint> -<year|/^[[:digit:]]{4}([\\?\\-]|\\-[[:digit:]]{4})?$/>",
				"muon decay year:1983->1992",
				"<muon> <decay> <year|1983->1992>",
				"author:\"Ellis, J\"->\"Ellis, Qqq\"",
				"<author|\"ellis, j\"->\"ellis, qqq\">",
				"refersto:reportnumber:hep-th/0201100",
				"",
				"citedby:author:klebanov",
				"",
				"refersto:author:\"Klebanov, I\"",
				"<refersto|\"/author:\"klebanov, i\" title:o(n)/\">",
				"refersto:keyword:gravitino",
				"",
				"author:klebanov AND citedby:author:papadimitriou NOT refersto:author:papadimitriou",
				"",
				"refersto:/author:\"Klebanov, I\" title:O(N)/",
				"<refersto|\"/author:\"klebanov, i\" title:o(n)/\">",
				"author:ellis -muon* +abstract:'dense quark matter' year:200*",
				"<author|ellis> -<muon*> +abstract:\"dense quark matter\"~2 <year|200*>",
				"author:ellis -muon* +title:'dense quark matter' year:200*",
				"<author|ellis> -<muon*> +<title|'dense quark matter'> <year|200*>",
				"higgs or reference:higgs or fulltext:higgs",
				"<higgs> <reference|higgs> fulltext:higgs",
				"author:lin fulltext:Schwarzschild fulltext:AdS reference:\"Adv. Theor. Math. Phys.\"",
				"<author|lin> fulltext:schwarzschild fulltext:ads <reference|\"adv. theor. math. phys.\">",
				"author:/^Ellis, (J|John)$/",
				"<author|\"/^ellis, (j|john)$/\">",
				"fulltext:e-",
				"fulltext:e-",
				"muon or fulltext:muon and author:ellis",
				"<muon> fulltext:muon +<author|ellis>",
				"reference:hep-ph/0103062",
				"<reference|hep-ph/0103062>",
				"reference:giddings reference:ross reference:\"Phys. Rev., D\" reference:61 reference:2000",
				"<reference|giddings> <reference|ross> <reference|\"phys. rev., d\"> <reference|61> <reference|2000>",
				"standard model -author:ellis reference:ellis",
				"<standard> <model> -<author|ellis> <reference|ellis>",

		};

		Query q;
		Query q2;
		QParser qp;
		QParser qp2;
		int success = 0;
		for (int i = 0; i < queries.length; i += 2) {
			String inv_q = queries[i];
			String expected = queries[i + 1];
			try {

				qp = parserPlugin.createParser(inv_q, localParams, params, req);
				qp2 = parserPlugin.createParser(inv_q, localParams_2, params,
						req);

				q2 = qp2.parse();
				try {
					q = qp.parse();
				} catch (ParseException e) {
					q = null;
				}

				if (expected.equals(q2.toString())) {
					success += 1;
				}

				System.out.println("--- " + i + ".");
				System.out.println("query=" + inv_q);
				System.out.println("expected=" + expected);
				System.out.println(localParams.get("iq.syntax") + "="
						+ (q != null ? q.toString() : ""));
				System.out.println(localParams_2.get("iq.syntax") + "="
						+ q2.toString());

			} catch (ParseException e) {
				System.out.println("--- " + i + ".");
				System.err.println("query=" + inv_q);
				System.err.println("expected=" + expected);
				e.printStackTrace();
			}

		}
		System.out.println("Total=" + queries.length / 2 + " success="
				+ success);

	}
	
	// Uniquely for Junit 3
	public static junit.framework.Test suite() {
        return new junit.framework.JUnit4TestAdapter(TestInvenioQueryParser.class);
    }


}
