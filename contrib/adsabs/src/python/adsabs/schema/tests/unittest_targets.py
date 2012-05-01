from montysolr.tests.montysolr_testcase import LuceneTestCase, sj


'''
Tests for the main Invenio API schema.targets

'''


class Test(LuceneTestCase):

    def setUp(self):
        LuceneTestCase.setUp(self)
        self.addTargets('adslabs.schema.targets')
    
    def test_get_field_value_using_bibcode(self):
        return "not implemented!"

