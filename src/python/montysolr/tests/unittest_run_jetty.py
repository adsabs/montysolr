'''
Created on Jan 10, 2011

@author: rca
'''
import unittest
import montysolr


class Test(unittest.TestCase):


    def setUp(self):
        sorlpie.initVM()


    def tearDown(self):
        pass


    def test_jetty(self):
        '''Tests if we are able to start jetty inside python and request results from the index'''
        sorlpie.JettyRunner.main(('solr.home', '/x/dev/workspace/test-solr/solr'))


if __name__ == "__main__":
    #import sys;sys.argv = ['', 'Test.testName']
    unittest.main()