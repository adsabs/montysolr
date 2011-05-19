
def run():
    import unittest
    import unittest_solr
    #fo = open('/tmp/solr-test', 'w')
    #suite = unittest.TestLoader().loadTestsFromTestCase(unittest_solr.Test)
    #unittest.TextTestRunner(verbosity=2).run(suite)
    #fo.write('OK!')
    #fo.close()
    
    sj = unittest_solr.sj
    
    initializer = sj.CoreContainer.Initializer()
    conf = {'solr_home': '/x/dev/workspace/sandbox/montysolr/example/solr',
                 'data_dir': '/x/dev/workspace/sandbox/montysolr/example/solr/data-test'}
    
    sj.System.setProperty('solr.solr.home', conf['solr_home'])
    sj.System.setProperty('solr.data.dir', conf['data_dir'])
    core_container = initializer.initialize()
    server = sj.EmbeddedSolrServer(core_container, "")
    
    solr_config = sj.SolrConfig()
    index_schema = sj.IndexSchema(solr_config, None, None)
    q = sj.QueryParsing.parseQuery('*:*', index_schema)
            
    # create a query
    query = sj.SolrQuery()
    query.setQuery('*:*')
    
    query_response = server.query(query)
    
    head_part = query_response.getResponseHeader()
    res_part = query_response.getResults()
    qtime = query_response.getQTime()
    etime = query_response.getElapsedTime()
    
    print qtime, etime, head_part, res_part
    
if __name__ == '__main__':
    run()