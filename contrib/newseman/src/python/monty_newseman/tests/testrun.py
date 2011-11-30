

from montysolr.tests.montysolr_testcase import MontySolrTestCase, sj
import bridge

class Test(MontySolrTestCase):


    def setUp(self):
        MontySolrTestCase.setUp(self)
        self.bridge = bridge.Bridge()
    
    def test_text(self):
        pass