
'''
Created on June 2, 2011

@author: jluker

'''

import unittest
from montysolr_testcase import MontySolrTestCase, sj
import os
import time
import sys
import re

class Test(MontySolrTestCase):
    
    def setUp(self):
        self.setSolrHome(os.path.join(self.getBaseDir(), 'examples/adslabs/solr'))
        self.setDataDir(os.path.join(self.getBaseDir(), 'examples/adslabs/solr/data'))
        self.setHandler(self.loadHandler('montysolr.adslabs.targets'))
        MontySolrTestCase.setUp(self)
        
    def test_mongodb(self):
        
        message = sj.PythonMessage('workout_field_value')
        message.setSender('PythonTextField')
        message.setParam('field','fulltext')
        message.setParam('externalVal', 'id:2|bibcode:1989ApJ...345..245C|src_dir:mongo')
        
        self.bridge.receive_message(message)
        
        res = str(message.getResults())
        wanted = "THE RELATIONSHIP BETWEEN INFRARED, OPTICAL, AND ULTRAVIOLET EXTINCTION"
        assert re.search(wanted, res) != None
    
    def test_fulltext_file(self):
        ''''''

        message = sj.PythonMessage('workout_field_value') 
        message.setSender('PythonTextField')
        message.setParam('field', 'fulltext')
        message.setParam('externalVal','id:1|bibcode:foobar|src_dir:%s' % \
                         self.base_dir + '/test/test-files/adslabs')

        self.bridge.receive_message(message)

        res = sj.String.cast_(message.getResults())
        assert res.equals("this is a test fulltext file\n")

if __name__ == "__main__":
    #import sys;sys.argv = ['', 'Test.test_get_recids_changes4']
    unittest.main()
