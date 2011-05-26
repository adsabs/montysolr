'''
Created on Feb 4, 2011

@author: rca
'''
#@PydevCodeAnalysisIgnore

import unittest
from montysolr_testcase import MontySolrTestCase, sj

import os



class Test(MontySolrTestCase):

    def setUp(self):
        self.setSolrHome(os.path.join(self.getBaseDir(), 'examples/invenio/solr'))
        self.setDataDir(os.path.join(self.getBaseDir(), 'examples/invenio/solr/data'))
        self.setHandler(self.loadHandler('montysolr.inveniopie.targets'))
        MontySolrTestCase.setUp(self)


    def test_dict_cache(self):

        message = sj.PythonMessage('get_citation_dict') \
                    .setSender('CitationQuery') \
                    .setParam('dictname', 'citationdict')
        self.bridge.receive_message(message)

        result = message.getParam('result')
        print 'got result'


    def test_workout_field_value(self):

        u = 'id:840017|arxiv_id:arXiv:0912.2620|src_dir:/Users/rca/work/indexing/fulltexts/arXiv'
        message = sj.PythonMessage('workout_field_value') \
                    .setSender('PythonTextField') \
                    .setParam('externalVal', u)
        self.bridge.receive_message(message)

        result = unicode(message.getParam('result'))
        print len(result)

    def test_handle_request_body(self):

        req = sj.QueryRequest()
        srp = sj.SolrQueryResponse()

        u = 'id:840017|arxiv_id:arXiv:0912.2620|src_dir:/Users/rca/work/indexing/fulltexts/arXiv'
        message = sj.PythonMessage('handleRequestBody') \
                    .setSender('rca.python.solr.handler.InvenioHandler') \
                    .setParam('externalVal', u)
        self.bridge.receive_message(message)

        result = unicode(message.getParam('result'))
        print len(result)

    def test_format_search_results(self):

        req = sj.QueryRequest()
        rsp = sj.SolrQueryResponse()

        message = sj.PythonMessage('format_search_results') \
                    .setSender('InvenioFormatter') \
                    .setSolrQueryResponse(rsp) \
                    .setParam('recids', sj.JArray_int(range(0, 93)))
        self.bridge.receive_message(message)

        result = unicode(rsp.getValues())
        assert 'inv_response' in result
        assert '<p>' in result

    def test_get_recids_changes(self):

        req = sj.QueryRequest()
        rsp = sj.SolrQueryResponse()

        message = sj.PythonMessage('get_recids_changes') \
                    .setSender('InvenioKeepRecidUpdated') \
                    .setSolrQueryResponse(rsp) \
                    .setParam('last_recid', 30)
        self.bridge.receive_message(message)

        results = message.getResults()
        out = sj.HashMap.cast_(results)

        added = sj.JArray_int.cast_(out.get('ADDED'))
        assert len(added) > 1

    def test_get_recids_changes2(self):

        req = sj.QueryRequest()
        rsp = sj.SolrQueryResponse()

        message = sj.PythonMessage('get_recids_changes') \
                    .setSender('InvenioKeepRecidUpdated') \
                    .setSolrQueryResponse(rsp) \
                    .setParam('last_recid', 0) #test we can deal with extreme cases
        self.bridge.receive_message(message)

        results = message.getResults()
        out = sj.HashMap.cast_(results)

        added = sj.JArray_int.cast_(out.get('ADDED'))
        assert len(added) > 1

    def test_get_recids_changes3(self):

        req = sj.QueryRequest()
        rsp = sj.SolrQueryResponse()

        message = sj.PythonMessage('get_recids_changes') \
                    .setSender('InvenioKeepRecidUpdated') \
                    .setSolrQueryResponse(rsp) \
                    .setParam('last_recid', 9999999)
        self.bridge.receive_message(message)

        results = message.getResults()
        assert results is None


    def test_get_recids_changes4(self):

        req = sj.QueryRequest()
        rsp = sj.SolrQueryResponse()

        message = sj.PythonMessage('get_recids_changes') \
                    .setSender('InvenioKeepRecidUpdated') \
                    .setSolrQueryResponse(rsp) \
                    .setParam('last_recid', -1)
        self.bridge.receive_message(message)

        results = message.getResults()
        out = sj.HashMap.cast_(results)

        added = sj.JArray_int.cast_(out.get('ADDED'))
        updated = sj.JArray_int.cast_(out.get('CHANGED'))
        deleted = sj.JArray_int.cast_(out.get('DELETED'))


        assert len(added) == 104
        assert len(updated) == 0
        assert len(deleted) == 0


    def test_perform_request_search_ints(self):

        req = sj.QueryRequest()
        rsp = sj.SolrQueryResponse()

        message = sj.PythonMessage('perform_request_search_ints') \
                    .setSender('InvenioQuery') \
                    .setSolrQueryResponse(rsp) \
                    .setParam('query', 'ellis')
        self.bridge.receive_message(message)

        results = message.getResults()
        out = sj.JArray_int.cast_(results)

        assert len(out) > 1

    def test_sort_and_format(self):

        req = sj.QueryRequest()
        rsp = sj.SolrQueryResponse()

        kwargs = sj.HashMap()
        kwargs.put("of", "hcs")
        #kwargs.put("sf", "year") #sort by year
        kwargs.put('colls_to_search', """['Articles & Preprints', 'Multimedia & Arts', 'Books & Reports']""")

        message = sj.PythonMessage('sort_and_format') \
                    .setSender('InvenioFormatter') \
                    .setSolrQueryResponse(rsp) \
                    .setParam('recids', sj.JArray_int(range(0, 93))) \
                    .setParam("kwargs", kwargs)

        self.bridge.receive_message(message)

        result = unicode(message.getResults())
        assert '<p>' in result

    def test_sort_and_format2(self):

        req = sj.QueryRequest()
        rsp = sj.SolrQueryResponse()

        kwargs = sj.HashMap()
        #kwargs.put("of", "hcs")
        kwargs.put("sf", "year") #sort by year
        kwargs.put('colls_to_search', """['Articles & Preprints', 'Multimedia & Arts', 'Books & Reports']""")

        message = sj.PythonMessage('sort_and_format') \
                    .setSender('InvenioFormatter') \
                    .setSolrQueryResponse(rsp) \
                    .setParam('recids', sj.JArray_int(range(0, 93))) \
                    .setParam("kwargs", kwargs)

        self.bridge.receive_message(message)

        result = sj.JArray_int.cast_(message.getResults())
        assert len(result) > 3
        assert result[0] == 77

    def test_diagnostic_test(self):

        req = sj.QueryRequest()
        rsp = sj.SolrQueryResponse()

        message = sj.PythonMessage('diagnostic_test') \
                    .setSolrQueryResponse(rsp) \
                    .setParam('recids', sj.JArray_int(range(0, 93)))

        self.bridge.receive_message(message)

        res = message.getResults()
        print res



if __name__ == "__main__":
    #import sys;sys.argv = ['', 'Test.test_get_recids_changes4']
    unittest.main()
