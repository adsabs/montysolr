'''
Created on Jan 10, 2011

@author: rca
'''

import montysolr_java
import urllib2

def run():
    cp = '/x/dev/workspace/apache-solr-1.4.1/lib/commons-codec-1.3.jar:/x/dev/workspace/apache-solr-1.4.1/lib/commons-csv-1.0-SNAPSHOT-r609327.jar:/x/dev/workspace/apache-solr-1.4.1/lib/commons-fileupload-1.2.1.jar:/x/dev/workspace/apache-solr-1.4.1/lib/commons-httpclient-3.1.jar:/x/dev/workspace/apache-solr-1.4.1/lib/commons-io-1.4.jar:/x/dev/workspace/apache-solr-1.4.1/lib/easymock.jar:/x/dev/workspace/apache-solr-1.4.1/lib/geronimo-stax-api_1.0_spec-1.0.1.jar:/x/dev/workspace/apache-solr-1.4.1/lib/jcl-over-slf4j-1.5.5.jar:/x/dev/workspace/apache-solr-1.4.1/lib/junit-4.3.jar:/x/dev/workspace/apache-solr-1.4.1/lib/lucene-analyzers-2.9.3.jar:/x/dev/workspace/apache-solr-1.4.1/lib/lucene-core-2.9.3.jar:/x/dev/workspace/apache-solr-1.4.1/lib/lucene-highlighter-2.9.3.jar:/x/dev/workspace/apache-solr-1.4.1/lib/lucene-memory-2.9.3.jar:/x/dev/workspace/apache-solr-1.4.1/lib/lucene-misc-2.9.3.jar:/x/dev/workspace/apache-solr-1.4.1/lib/lucene-queries-2.9.3.jar:/x/dev/workspace/apache-solr-1.4.1/lib/lucene-snowball-2.9.3.jar:/x/dev/workspace/apache-solr-1.4.1/lib/lucene-spellchecker-2.9.3.jar:/x/dev/workspace/apache-solr-1.4.1/lib/servlet-api-2.4.jar:/x/dev/workspace/apache-solr-1.4.1/lib/slf4j-api-1.5.5.jar:/x/dev/workspace/apache-solr-1.4.1/lib/slf4j-jdk14-1.5.5.jar:/x/dev/workspace/apache-solr-1.4.1/lib/wstx-asl-3.2.7.jar:/x/dev/workspace/apache-solr-1.4.1/dist/apache-solr-cell-1.4.2-dev.jar:/x/dev/workspace/apache-solr-1.4.1/dist/apache-solr-cell-docs-1.4.2-dev.jar:/x/dev/workspace/apache-solr-1.4.1/dist/apache-solr-clustering-1.4.2-dev.jar:/x/dev/workspace/apache-solr-1.4.1/dist/apache-solr-clustering-docs-1.4.2-dev.jar:/x/dev/workspace/apache-solr-1.4.1/dist/apache-solr-core-1.4.2-dev.jar:/x/dev/workspace/apache-solr-1.4.1/dist/apache-solr-core-docs-1.4.2-dev.jar:/x/dev/workspace/apache-solr-1.4.1/dist/apache-solr-dataimporthandler-1.4.2-dev.jar:/x/dev/workspace/apache-solr-1.4.1/dist/apache-solr-dataimporthandler-docs-1.4.2-dev.jar:/x/dev/workspace/apache-solr-1.4.1/dist/apache-solr-dataimporthandler-extras-1.4.2-dev.jar:/x/dev/workspace/apache-solr-1.4.1/dist/apache-solr-solrj-1.4.2-dev.jar:/x/dev/workspace/apache-solr-1.4.1/dist/apache-solr-solrj-docs-1.4.2-dev.jar:/x/dev/workspace/apache-solr-1.4.1/dist/apache-solr-velocity-docs-1.4.2-dev.jar:/x/dev/workspace/apache-solr-1.4.1/dist/solrj-lib/commons-codec-1.3.jar:/x/dev/workspace/apache-solr-1.4.1/dist/solrj-lib/commons-httpclient-3.1.jar:/x/dev/workspace/apache-solr-1.4.1/dist/solrj-lib/commons-io-1.4.jar:/x/dev/workspace/apache-solr-1.4.1/dist/solrj-lib/geronimo-stax-api_1.0_spec-1.0.1.jar:/x/dev/workspace/apache-solr-1.4.1/dist/solrj-lib/jcl-over-slf4j-1.5.5.jar:/x/dev/workspace/apache-solr-1.4.1/dist/solrj-lib/slf4j-api-1.5.5.jar:/x/dev/workspace/apache-solr-1.4.1/dist/solrj-lib/wstx-asl-3.2.7.jar:/x/dev/workspace/apache-solr-1.4.1/example/lib/jetty-6.1.3.jar:/x/dev/workspace/apache-solr-1.4.1/example/lib/jetty-util-6.1.3.jar:/x/dev/workspace/apache-solr-1.4.1/example/lib/jsp-2.1/ant-1.6.5.jar:/x/dev/workspace/apache-solr-1.4.1/example/lib/jsp-2.1/core-3.1.1.jar:/x/dev/workspace/apache-solr-1.4.1/example/lib/jsp-2.1/jsp-2.1.jar:/x/dev/workspace/apache-solr-1.4.1/example/lib/jsp-2.1/jsp-api-2.1.jar:/x/dev/workspace/apache-solr-1.4.1/example/lib/servlet-api-2.5-6.1.3.jar'
    montysolr_java.initVM(montysolr_java.CLASSPATH + ':' + cp)
    montysolr_java.System.setProperty('solr.solr.home', '/x/dev/workspace/test-solr/solr')
    montysolr_java.System.setProperty('solr.data.dir', '/x/dev/workspace/test-solr/solr/data')
    # montysolr_java.JettyRunner.main(())
    jetty = montysolr_java.JettyRunner()
    jetty.start() 
    
    
    page = urllib2.urlopen('http://localhost:8983/test/select/?q=*%3A*&version=2.2&start=0&rows=10&indent=on&qt=recids').read()
    print page
    assert page.find('"numFound">6000</int>') > -1
    start = page.index('name="docs">')+12
    docs = page[start:page.index("</int>", start)].strip()
    results = montysolr_java.ResultsCacheSingleton.getInstance().getResults(int(docs))
    
    print 'this is printed by python but comes from java'
    print results
    
    jetty.stop()
    
if __name__ == '__main__':
    run()