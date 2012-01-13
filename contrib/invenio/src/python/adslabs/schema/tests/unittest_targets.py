from montysolr.tests.montysolr_testcase import LuceneTestCase, sj

import os

'''
Tests for the main Invenio API schema.targets

'''


class Test(LuceneTestCase):

    def setUp(self):
        LuceneTestCase.setUp(self)
        self.addTargets('monty_invenio.schema.targets')
    
    def test_workout_field_value(self):
        u = self.getBaseDir() + "/contrib/invenio/src/test-files/data/text1.txt"
        message = self.bridge.createMessage('get_field_value') \
                    .setSender('PythonTextField') \
                    .setParam('externalVal', u)
        self.bridge.sendMessage(message)

        result = unicode(message.getResults())
        
        assert 'PythonTextField' in result
        assert 'Hi,' in result

