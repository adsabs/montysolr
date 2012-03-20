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
from invenio import intbitset
import time


class Test(LuceneTestCase):

    def setUp(self):
        LuceneTestCase.setUp(self)
        self.size = 500000
        if not self.hasTarget('*:bigtest'):
            self.addTargets(['montysolr.examples.bigtest'])
        

    def test_bigtest01(self):
        '''Get recids_bitset - needs invenio.intbitset'''


        message = self.bridge.createMessage('bigtest') \
                    .setParam('action', 'recids_bitset') \
                    .setParam('size', self.size) \
                    .setParam('filled', int(self.size * 0.3))

        self.bridge.sendMessage(message)
        res = sj.JArray_byte.cast_(message.getResults())
        ibs = intbitset.intbitset()
        ibs = ibs.fastload(res.string_)

        assert len(ibs) > 0
        assert len(ibs) > int(self.size * 0.3)

    
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
