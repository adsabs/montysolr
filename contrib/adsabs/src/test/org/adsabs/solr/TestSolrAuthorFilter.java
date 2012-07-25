package org.adsabs.solr;

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

import montysolr.util.MontySolrAbstractTestCase;
import montysolr.util.MontySolrSetup;

import org.apache.solr.schema.IndexSchema;



public class TestSolrAuthorFilter extends MontySolrAbstractTestCase {

	String AUTHOR_FIELD = "author";
	
	public String getSchemaFile() {
		
	    makeResourcesVisible(this.solrConfig.getResourceLoader(),
	    		new String[] {MontySolrSetup.getMontySolrHome() + "/contrib/examples/adsabs/solr/collection1/conf",
	    				      MontySolrSetup.getSolrHome() + "/example/solr/collection1/conf"
	    	});
		
		return MontySolrSetup.getMontySolrHome()
				+ "/contrib/examples/adsabs/solr/collection1/conf/schema.xml";
	}

	public String getSolrConfigFile() {
		return MontySolrSetup.getMontySolrHome()
				+ "/contrib/examples/adsabs/solr/collection1/conf/solrconfig.xml";
	}

	public void testAuthorParsing() throws Exception {
		adoc("recid", "1", AUTHOR_FIELD, "Jim Jay Clavell");
		adoc("recid", "2", AUTHOR_FIELD, "Jim Jay Clavell");
		adoc("recid", "3", AUTHOR_FIELD, "Jim Jay Clavell");
		
		IndexSchema schema = h.getCore().getSchema();
		schema.getField(AUTHOR_FIELD);
	}
}


