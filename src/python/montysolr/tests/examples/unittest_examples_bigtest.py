'''
Created on May 11, 2011

@author: rca

To run this unittest, you will need a lot of memory (if size is big)
You can do this:

export MONTYSOLR_JVMARGS_PYTHON='-Xmx800m -d32'
python unittest_example_bigtest.py Test.test_bigtest01
'''

import unittest
from montysolr.tests.montysolr_testcase import LuceneTestCase
from montysolr.initvm import JAVA as sj
import os
import time
import sys


class Test(LuceneTestCase):

    def setUp(self):
        self.size = 500000
        self.setHandler(self.loadHandler('montysolr.examples.bigtest'))
        LuceneTestCase.setUp(self)



    def test_bigtest01(self):
        '''Get int[]'''

        req = sj.QueryRequest()
        size = self.size
        hm = sj.HashMap().of_(sj.String, sj.String)
        hm.put('action', 'recids_int')
        hm.put('size', str(size))
        params = sj.MapSolrParams(hm)
        #req = sj.LocalSolrQueryRequest(self.core, params)

        rsp = sj.SolrQueryResponse()

        message = sj.PythonMessage('bigtest') \
                    .setParam('request', req)
                    #.setParam('response', rsp)
                    

        self.bridge.receive_message(message)

        res = sj.JArray_int.cast_(message.getResults())
        res = list(res)
        assert len(res) == size
        assert res[0] == 0
        assert res[5] == 5

    def test_bigtest02(self):
        '''Get String[]'''

        #req = sj.QueryRequest()
        size = self.size
        hm = sj.HashMap().of_(sj.String, sj.String)
        hm.put('action', 'recids_str')
        hm.put('size', str(size))
        params = sj.MapSolrParams(hm)
        req = sj.LocalSolrQueryRequest(self.core, params)

        rsp = sj.SolrQueryResponse()

        message = sj.PythonMessage('bigtest') \
                    .setParam('response', rsp) \
                    .setParam('request', req)

        self.bridge.receive_message(message)

        res = sj.JArray_string.cast_(message.getResults())
        assert len(res) == size
        assert res[0] == '0'
        assert res[5] == '5'


    def test_bigtest03(self):
        '''Get recids_hm_strstr'''

        #req = sj.QueryRequest()
        size = self.size
        hm = sj.HashMap().of_(sj.String, sj.String)
        hm.put('action', 'recids_hm_strstr')
        hm.put('size', str(size))
        params = sj.MapSolrParams(hm)
        req = sj.LocalSolrQueryRequest(self.core, params)

        rsp = sj.SolrQueryResponse()

        message = sj.PythonMessage('bigtest') \
                    .setParam('response', rsp) \
                    .setParam('request', req)

        self.bridge.receive_message(message)

        res = sj.HashMap.cast_(message.getResults())
        assert res.size() == size
        assert str(sj.String.cast_(res.get('0'))) == '0'
        assert str(sj.String.cast_(res.get('5'))) == '5'


    def test_bigtest04(self):
        '''Get recids_hm_strint'''

        #req = sj.QueryRequest()
        size = self.size
        hm = sj.HashMap().of_(sj.String, sj.String)
        hm.put('action', 'recids_hm_strint')
        hm.put('size', str(size))
        params = sj.MapSolrParams(hm)
        req = sj.LocalSolrQueryRequest(self.core, params)

        rsp = sj.SolrQueryResponse()

        message = sj.PythonMessage('bigtest') \
                    .setParam('response', rsp) \
                    .setParam('request', req)

        self.bridge.receive_message(message)

        res = sj.HashMap.cast_(message.getResults())
        assert res.size() == size
        assert sj.Integer.cast_(res.get('0')).equals(0)
        assert sj.Integer.cast_(res.get('5')).equals(5)


    def test_bigtest05(self):
        '''Get recids_hm_intint'''

        #req = sj.QueryRequest()
        size = self.size
        hm = sj.HashMap().of_(sj.String, sj.String)
        hm.put('action', 'recids_hm_intint')
        hm.put('size', str(size))
        params = sj.MapSolrParams(hm)
        req = sj.LocalSolrQueryRequest(self.core, params)

        rsp = sj.SolrQueryResponse()

        message = sj.PythonMessage('bigtest') \
                    .setParam('response', rsp) \
                    .setParam('request', req)

        self.bridge.receive_message(message)

        res = sj.HashMap.cast_(message.getResults())
        assert res.size() == size
        assert sj.Integer.cast_(res.get(0)).equals(0)
        assert sj.Integer.cast_(res.get(5)).equals(5)


    def test_bigtest06(self):
        '''Get recids_bitset - needs invenio.intbitset'''

        from invenio import intbitset

        #req = sj.QueryRequest()
        size = self.size
        hm = sj.HashMap().of_(sj.String, sj.String)
        hm.put('action', 'recids_bitset')
        hm.put('size', str(size))
        filled = int(size * 0.3)
        hm.put('filled', str(filled))
        params = sj.MapSolrParams(hm)
        req = sj.LocalSolrQueryRequest(self.core, params)

        rsp = sj.SolrQueryResponse()

        message = sj.PythonMessage('bigtest') \
                    .setParam('response', rsp) \
                    .setParam('request', req)

        self.bridge.receive_message(message)
        res = sj.JArray_byte.cast_(message.getResults())
        ibs = intbitset.intbitset()
        ibs = ibs.fastload(res.string_)

        assert len(ibs) > 0

    def timeit(self):
        for x in range(1, 7):
            testid = '%02d' % x
            times = 10
            test_name = 'test_bigtest%s' % testid
            if hasattr(self, test_name):
                print test_name,
                start = time.time()
                test_method = getattr(self, test_name)
                for x in xrange(times):
                    test_method()
                end =  time.time() - start
                print end / times, 's.'


if __name__ == "__main__":
    #import sys;sys.argv = ['', 'Test.test_get_recids_changes4']
    unittest.main()
