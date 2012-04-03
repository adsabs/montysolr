'''
Created on Feb 4, 2011

@author: rca
'''
#@PydevCodeAnalysisIgnore

import unittest
from montysolr.tests.montysolr_testcase import sj
from monty_invenio.tests.invenio_demotestcase import InvenioDemoTestCaseLucene

import os
from invenio import intbitset

'''
Tests for the main Invenio API targets - we expect to run 
over the example which was built with ant. See contrib/examples
ant build-one

We also expect the invenio database with demo site loaded


'''


class Test(InvenioDemoTestCaseLucene):

    def setUp(self):
        self.setSolrHome(os.path.join(self.getBaseDir(), 'build/contrib/examples/invenio/solr'))
        self.setDataDir(os.path.join(self.getBaseDir(), 'build/contrib/examples/invenio/solr/data'))
        InvenioDemoTestCaseLucene.setUp(self)
        if not self.hasTarget("InvenioQuery:perform_request_search_ints"):
            self.addTargets('monty_invenio.targets')


    def test_dict_cache(self):
        '''Test we can retrieve citation dictionary'''
        message = self.bridge.createMessage('get_citation_dict') \
                    .setSender('CitationQuery') \
                    .setParam('dictname', 'citationdict')
        self.bridge.sendMessage(message)
        result = message.getResults()
        # TODO - check the Java converted version


    

    def test_format_search_results(self):
        '''Returns the citation summary for the given recids'''
        req = sj.QueryRequest()
        rsp = sj.SolrQueryResponse()

        message = self.bridge.createMessage('format_search_results') \
                    .setSender('InvenioFormatter') \
                    .setParam('response', rsp) \
                    .setParam('recids', sj.JArray_int(range(0, 93)))
        self.bridge.sendMessage(message)

        result = unicode(rsp.getValues())
        assert 'inv_response' in result
        assert '<p>' in result
        assert 'Average citations per paper:' in result



    def test_perform_request_search_ints(self):
        '''Basic search for ellis - commucating with ints'''
        req = sj.QueryRequest()
        rsp = sj.SolrQueryResponse()

        message = self.bridge.createMessage('perform_request_search_ints') \
                    .setSender('InvenioQuery') \
                    .setParam('response', rsp) \
                    .setParam('query', 'ellis')
        self.bridge.sendMessage(message)

        results = message.getResults()
        out = list(sj.JArray_int.cast_(results))
        assert len(out) > 1
        assert sorted(out) == [8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 47]


    def test_perform_request_search_bitset(self):
        '''Basic search for ellis - commucating with bitset'''
        req = sj.QueryRequest()
        rsp = sj.SolrQueryResponse()

        message = self.bridge.createMessage('perform_request_search_bitset') \
                    .setSender('InvenioQuery') \
                    .setParam('response', rsp) \
                    .setParam('query', 'ellis')
        self.bridge.sendMessage(message)

        results = message.getResults()
        out = sj.JArray_byte.cast_(results)
        bs = sj.JArray_byte(intbitset.intbitset([8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 47]).fastdump())
        assert bs == out
        

    def test_citation_summary(self):
        '''Generate citation summary'''
        
        req = sj.QueryRequest()
        rsp = sj.SolrQueryResponse()

        kwargs = sj.HashMap()
        kwargs.put("of", "hcs")
        #kwargs.put("sf", "year") #sort by year
        kwargs.put('colls_to_search', """['Articles & Preprints', 'Multimedia & Arts', 'Books & Reports']""")

        message = self.bridge.createMessage('sort_and_format') \
                    .setSender('InvenioFormatter') \
                    .setParam('response', rsp) \
                    .setParam('recids', sj.JArray_int(range(0, 93))) \
                    .setParam("kwargs", kwargs)

        self.bridge.sendMessage(message)

        result = unicode(message.getResults())
        assert 'Citation summary results' in result
        

    def test_sorting(self):
        '''Sort results by year'''
        req = sj.QueryRequest()
        rsp = sj.SolrQueryResponse()

        kwargs = sj.HashMap()
        #kwargs.put("of", "hcs")
        kwargs.put("sf", "year") #sort by year
        kwargs.put('colls_to_search', """['Articles & Preprints', 'Multimedia & Arts', 'Books & Reports']""")

        message = self.bridge.createMessage('sort_and_format') \
                    .setSender('InvenioFormatter') \
                    .setParam('response', rsp) \
                    .setParam('recids', sj.JArray_int(range(0, 93))) \
                    .setParam("kwargs", kwargs)

        self.bridge.sendMessage(message)

        result = sj.JArray_int.cast_(message.getResults())
        assert len(result) > 3
        assert result[0] == 77


    def test_invenio_search(self):
        kws = sj.HashMap().of_(sj.String, sj.List)
        p = sj.ArrayList().of_(sj.String)
        of = sj.ArrayList().of_(sj.String)
        rg = sj.ArrayList().of_(sj.String)
        
        p.add('recid:94')
        of.add('xm')
        
        kws.put('p', p)
        kws.put('of', of)
        
        message = self.bridge.createMessage('invenio_search') \
                    .setParam('kwargs', kws)

        self.bridge.sendMessage(message)
        result = str(sj.String.cast_(message.getResults()))
        assert '<controlfield tag="001">94</controlfield>' in result
        
        
        
        p.clear()
        p.add('recid:0->50')
        message = self.bridge.createMessage('invenio_search') \
                    .setParam('kwargs', kws)
        self.bridge.sendMessage(message)
        result = str(sj.String.cast_(message.getResults()))
        
        assert 'Search-Engine-Total-Number-Of-Results: 42' in result
        assert len(result) > 1000
        assert result.count('<record>') == 10
        
        rg.add('200')
        kws.put('rg', rg)
        
        message = self.bridge.createMessage('invenio_search') \
                    .setParam('kwargs', kws)
        self.bridge.sendMessage(message)
        result = str(sj.String.cast_(message.getResults()))
        
        assert 'Search-Engine-Total-Number-Of-Results: 42' in result
        assert len(result) > 1000
        assert result.count('<record>') == 42

if __name__ == "__main__":
    import sys;sys.argv = ['', 'Test.test_invenio_search']
    unittest.main()
