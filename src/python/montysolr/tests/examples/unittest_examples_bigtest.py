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
from montysolr.initvm import JAVA as j
import os
import time
import sys

JArray_string = j.JArray_string #@UndefinedVariable
JArray_int = j.JArray_int #@UndefinedVariable
JArray_byte = j.JArray_byte #@UndefinedVariable
Integer = j.Integer #@UndefinedVariable
HashMap = j.HashMap #@UndefinedVariable
String = j.String #@UndefinedVariable
MapSolrParams = j.MapSolrParams #@UndefinedVariable
SolrQueryResponse = j.SolrQueryResponse #@UndefinedVariable
LocalSolrQueryRequest = j.LocalSolrQueryRequest #@UndefinedVariable

class Test(LuceneTestCase):

    def setUp(self):
        LuceneTestCase.setUp(self)
        self.size = 500000
        if not self.hasTarget('*:bigtest'):
            self.addTargets(['montysolr.examples.bigtest'])
        

    def test_bigtest01(self):
        '''Get int[]'''

        message = self.bridge.createMessage('bigtest') \
                    .setParam('action', 'recids_int') \
                    .setParam('size', self.size)

        self.bridge.sendMessage(message)

        res = JArray_int.cast_(message.getResults())
        res = list(res)
        assert len(res) == self.size
        assert res[0] == 0
        assert res[5] == 5


    def test_bigtest02(self):
        '''Get String[]'''

        message = self.bridge.createMessage('bigtest') \
                    .setParam('action', 'recids_str') \
                    .setParam('size', self.size)
        self.bridge.sendMessage(message)
        res = JArray_string.cast_(message.getResults())
        assert len(res) == self.size
        assert res[0] == '0'
        assert res[5] == '5'


    def test_bigtest03(self):
        '''Get recids_hm_strstr'''

        message = self.bridge.createMessage('bigtest') \
                    .setParam('action', 'recids_hm_strstr') \
                    .setParam('size', self.size)

        self.bridge.sendMessage(message)
        res = HashMap.cast_(message.getResults())
        assert res.size() == self.size
        assert str(String.cast_(res.get('0'))) == '0'
        assert str(String.cast_(res.get('5'))) == '5'



    def test_bigtest04(self):
        '''Get recids_hm_strint'''
        message = self.bridge.createMessage('bigtest') \
                    .setParam('action', 'recids_hm_strint') \
                    .setParam('size', self.size)

        self.bridge.sendMessage(message)

        res = HashMap.cast_(message.getResults())
        assert res.size() == self.size
        assert Integer.cast_(res.get('0')).equals(0)
        assert Integer.cast_(res.get('5')).equals(5)


    def test_bigtest05(self):
        '''Get recids_hm_intint'''

        message = self.bridge.createMessage('bigtest') \
                    .setParam('action', 'recids_hm_intint') \
                    .setParam('size', self.size)
        self.bridge.sendMessage(message)
        res = HashMap.cast_(message.getResults())
        assert res.size() == self.size
        assert Integer.cast_(res.get(0)).equals(0)
        assert Integer.cast_(res.get(5)).equals(5)



    
    def xtest_bigtest05(self):
        '''Get recids_hm_intint -- TODO: move to demotest,
        it will not work, because it needs solr instance'''

        #req = QueryRequest()
        size = self.size
        hm = HashMap().of_(String, String)
        hm.put('action', 'recids_hm_intint')
        hm.put('size', str(size))
        params = MapSolrParams(hm)
        req = LocalSolrQueryRequest(self.core, params)
        rsp = SolrQueryResponse()

        message = self.bridge.createMessage('bigtest_www') \
                    .setParam('response', rsp) \
                    .setParam('request', req)

        self.bridge.sendMessage(message)

        res = HashMap.cast_(message.getResults())
        assert res.size() == self.size
        assert Integer.cast_(res.get(0)).equals(0)
        assert Integer.cast_(res.get(5)).equals(5)


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
