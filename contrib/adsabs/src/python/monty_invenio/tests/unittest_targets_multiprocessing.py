'''
Created on Feb 4, 2011

@author: rca
'''
#@PydevCodeAnalysisIgnore

import unittest
from montysolr.tests.montysolr_testcase import sj
from monty_invenio.tests.invenio_demotestcase import InvenioDemoTestCaseLucene
from monty_invenio.tests import unittest_targets
from monty_invenio import targets
from montysolr import config 

import os
from invenio import intbitset

'''
Tests for the main Invenio API targets - we expect to run 
over the example which was built with ant. See contrib/examples
ant build-one

This checks we have run in the multiprocessing setup

'''


class Test(unittest_targets.Test):

    def setUp(self):
        config.MONTYSOLR_MAX_WORKERS = 2
        unittest_targets.Test.setUp(self)
        assert hasattr(targets.api_calls, "POOL")
        pool = targets.api_calls.POOL
        assert len(pool._pool) == 2

        
    




if __name__ == "__main__":
    #import sys;sys.argv = ['', 'Test.test_sorting']
    unittest.main()
