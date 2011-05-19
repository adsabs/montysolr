'''
Created on Feb 4, 2011

@author: rca
'''

import os
# "-Djava.util.logging.config.file=/x/dev/workspace/sandbox/montysolr/example/etc/test.logging.properties"
os.environ['MONTYSOLR_JVMARGS_PYTHON'] = ""

import unittest
from montysolr import initvm


sj = initvm.montysolr_java
solr = initvm.solr_java
lu = initvm.lucene


class Test(unittest.TestCase):


    def setUp(self):
        
        self.initializer = sj.CoreContainer.Initializer()
        self.conf = {'solr_home': '/x/dev/workspace/sandbox/montysolr/example/solr',
                     'data_dir': '/x/dev/workspace/sandbox/montysolr/example/solr/data-jtest'}
        
        sj.System.setProperty('solr.solr.home', self.conf['solr_home'])
        sj.System.setProperty('solr.data.dir', self.conf['data_dir'])
        self.core_container = self.initializer.initialize()
        self.server = sj.EmbeddedSolrServer(self.core_container, "")
        
        solr_config = sj.SolrConfig()
        index_schema = sj.IndexSchema(solr_config, None, None)
        q = sj.QueryParsing.parseQuery('*:*', index_schema)
        
    def tearDown(self):
        self.core_container.shutdown()
    

    def test_solr_all(self):
        
        server = self.server
        
        # create a query
        query = sj.SolrQuery()
        query.setQuery('*:*')
        
        query_response = server.query(query)
        
        head_part = query_response.getResponseHeader()
        res_part = query_response.getResults()
        qtime = query_response.getQTime()
        etime = query_response.getElapsedTime()
        
        print qtime, etime, head_part, res_part
        
        
if __name__ == "__main__":
    #import sys;sys.argv = ['', 'Test.testName']
    unittest.main()