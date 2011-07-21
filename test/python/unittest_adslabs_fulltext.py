
'''
Created on June 2, 2011

@author: jluker

'''

import unittest
from montysolr_testcase import MontySolrTestCase, sj
from montysolr import config
import os
import time
import sys
import re

class Test(MontySolrTestCase):
    
    def setUp(self):
        self.setSolrHome(os.path.join(self.getBaseDir(), 'examples/adslabs/solr'))
        self.setDataDir(os.path.join(self.getBaseDir(), 'examples/adslabs/solr/data'))
        self.setHandler(self.loadHandler('montysolr.adslabs.targets'))
        self.old_MSFTBASEPATH = config.MSFTBASEPATH
        config.MSFTBASEPATH = self.base_dir + '/test/test-files/adslabs'
        MontySolrTestCase.setUp(self)
    
    def tearDown(self):
        config.MSFTBASEPATH = self.old_MSFTBASEPATH
        MontySolrTestCase.tearDown(self)
        
    def test_fulltext_file(self):
        ''''''

        message = sj.PythonMessage('workout_field_value') 
        message.setSender('PythonTextField')
        message.setParam('field', 'fulltext')
        message.setParam('externalVal', 'foobar')

        self.bridge.receive_message(message)

        res = sj.String.cast_(message.getResults())
        assert res.equals("this is a test fulltext file\n")

if __name__ == "__main__":
    #import sys;sys.argv = ['', 'Test.test_get_recids_changes4']
    unittest.main()
