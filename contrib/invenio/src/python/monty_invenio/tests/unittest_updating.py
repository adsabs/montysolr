
from montysolr.initvm import JAVA as j
from montysolr.tests.montysolr_testcase import LuceneTestCase

from monty_invenio.tests.invenio_demotestcase import InvenioDemoTestCaseLucene
from montysolr.utils import make_targets

import monty_invenio.targets as targets

from invenio import dbquery, bibupload, bibupload_regression_tests

import sys
import time


# beware, granularity is in seconds!!!
        
def change_records(message):
    recids = list(j.JArray_int.cast_(message.getParam('recids')))
    diff = message.getParam('diff')
    if diff:
        diff = int(str(diff))
    else:
        diff = 5 # 5 secs older
    for r in recids:
        dbquery.run_sql("UPDATE bibrec SET modification_date=NOW() + %s WHERE id=%s", (diff, r))

def add_records(message):
    """We are not adding, but if creation_date == modification_date
    then recs are considerd new"""
    
    recids = list(j.JArray_int.cast_(message.getParam('recids')))
    diff = message.getParam('diff')
    if diff:
        diff = int(str(diff))
    else:
        diff = 5 # 5 secs older
    for r in recids:
        dbquery.run_sql("UPDATE bibrec SET modification_date=NOW() + %s, creation_date=NOW() + %s WHERE id=%s", (diff,diff, r))

def reset_records(message):
    dbquery.run_sql("UPDATE bibrec SET modification_date=NOW(), creation_date=NOW()")

def create_delete(message):
    """creates and deletes the record"""
    
    #rec_id = bibupload.create_new_record()
    #expected_rec_id = dbquery.run_sql("SELECT MAX(id) FROM bibrec")[0][0] + 1
    xml_to_delete = """
        <record>
        <datafield tag="100" ind1=" " ind2=" ">
         <subfield code="a">Test, Jane</subfield>
         <subfield code="u">Test Institute</subfield>
        </datafield>
        <datafield tag="100" ind1="4" ind2="7">
         <subfield code="a">Test, Johnson</subfield>
         <subfield code="u">Test University</subfield>
        </datafield>
        <datafield tag="980" ind1="" ind2="">
         <subfield code="c">DELETED</subfield>
        </datafield>
        </record>
        """ 
    
    recs = bibupload.xml_marc_to_records(xml_to_delete)
    err, recid, msg = bibupload.bibupload(recs[0], opt_mode='insert')
    
    message.setResults(recid)    
    
    
def wipeout_record(message):
    recid = int(str(message.getParam('recid')))
    bibupload_regression_tests.wipe_out_record_from_all_tables(recid)  
        
        
        
def montysolr_targets():
    return make_targets(change_records=change_records,
                        reset_records=reset_records,
                        add_records=add_records,
                        create_delete=create_delete,
                        wipeout_record=wipeout_record)



class Test(InvenioDemoTestCaseLucene):
    """This test requires Invenio instance with the demo
    records loaded
    """

    def setUp(self):
        LuceneTestCase.setUp(self)
        handler = self.bridge.getHandler()
        if not handler.has_target('InvenioKeepRecidUpdated:get_recids_changes'):
            self.addTargets(targets)
            self.addTargets(sys.modules[__name__])
        reset_records(None)
        self.max_recs = dbquery.run_sql("SELECT COUNT(*) FROM bibrec")[0][0]
        self.max_id = dbquery.run_sql("SELECT MAX(id) FROM bibrec")[0][0]
    
    def test_get_recids_added(self):
        
        message = self.bridge.createMessage('add_records') \
                    .setParam('recids', j.JArray_int(range(1, 11)))
        self.bridge.sendMessage(message)
        
        req = j.QueryRequest()
        rsp = j.SolrQueryResponse()

        message = self.bridge.createMessage('get_recids_changes') \
                    .setSender('InvenioKeepRecidUpdated') \
                    .setSolrQueryResponse(rsp) \
                    .setParam('last_recid', 30)
        self.bridge.sendMessage(message)

        results = message.getResults()
        out = j.HashMap.cast_(results)

        added = j.JArray_int.cast_(out.get('ADDED'))
        assert len(added) == 10

    def test_get_recids_added_all(self):
        
        # make all records appear as old
        message = self.bridge.createMessage('change_records') \
                    .setParam('diff', -1) \
                    .setParam('recids', j.JArray_int([1]))
        self.bridge.sendMessage(message)
                    
        req = j.QueryRequest()
        rsp = j.SolrQueryResponse()

        message = self.bridge.createMessage('get_recids_changes') \
                    .setSender('InvenioKeepRecidUpdated') \
                    .setSolrQueryResponse(rsp) \
                    .setParam('last_recid', 0) #test we can deal with extreme cases
        self.bridge.sendMessage(message)

        results = message.getResults()
        out = j.HashMap.cast_(results)

        added = j.JArray_int.cast_(out.get('ADDED'))
        updated = j.JArray_int.cast_(out.get('UPDATED'))
        deleted = j.JArray_int.cast_(out.get('DELETED'))
        
        assert (len(added)+len(updated)+len(deleted)) == self.max_recs -1
        
        message = self.bridge.createMessage('get_recids_changes') \
                    .setSender('InvenioKeepRecidUpdated') \
                    .setSolrQueryResponse(rsp) \
                    .setParam('max_records', 10) \
                    .setParam('last_recid', 0) #test we can deal with extreme cases
        self.bridge.sendMessage(message)

        results = message.getResults()
        out = j.HashMap.cast_(results)

        added = j.JArray_int.cast_(out.get('ADDED'))
        updated = j.JArray_int.cast_(out.get('UPDATED'))
        deleted = j.JArray_int.cast_(out.get('DELETED'))
        
        assert (len(added) == 10)


    def test_get_recids_nothing_changed(self):

        req = j.QueryRequest()
        rsp = j.SolrQueryResponse()

        message = self.bridge.createMessage('get_recids_changes') \
                    .setSender('InvenioKeepRecidUpdated') \
                    .setSolrQueryResponse(rsp) \
                    .setParam('last_recid', 9999999)
        self.bridge.sendMessage(message)

        results = message.getResults()
        assert results is None
        
        message = self.bridge.createMessage('get_recids_changes') \
                    .setSender('InvenioKeepRecidUpdated') \
                    .setSolrQueryResponse(rsp) \
                    .setParam('last_recid', int(self.max_id))
        self.bridge.sendMessage(message)

        results = message.getResults()
        assert results is None

    def test_get_recids_minus_one(self):

        req = j.QueryRequest()
        rsp = j.SolrQueryResponse()

        message = self.bridge.createMessage('get_recids_changes') \
                    .setSender('InvenioKeepRecidUpdated') \
                    .setSolrQueryResponse(rsp) \
                    .setParam('last_recid', -1)
        self.bridge.sendMessage(message)

        results = message.getResults()
        out = j.HashMap.cast_(results)

        added = j.JArray_int.cast_(out.get('ADDED'))
        updated = j.JArray_int.cast_(out.get('UPDATED'))
        deleted = j.JArray_int.cast_(out.get('DELETED'))


        assert (len(added)+len(updated)+len(deleted)) == self.max_recs
        assert len(updated) == 0
        self.assertTrue(len(deleted) == 0, msg="If only one test fails, then recreate your demo site")
        
        
    def test_get_recids_deleted(self):
        
        time.sleep(1) # otherwise the new record has the same creation time as the old recs
        message = self.bridge.createMessage('create_delete')
        self.bridge.sendMessage(message)
        
        deleted_recid = int(str(j.Integer.cast_(message.getResults())))
        
        
        req = j.QueryRequest()
        rsp = j.SolrQueryResponse()

        message = self.bridge.createMessage('get_recids_changes') \
                    .setSender('InvenioKeepRecidUpdated') \
                    .setSolrQueryResponse(rsp) \
                    .setParam('last_recid', 30)
        self.bridge.sendMessage(message)

        results = message.getResults()
        out = j.HashMap.cast_(results)
        
        added = j.JArray_int.cast_(out.get('ADDED'))
        updated = j.JArray_int.cast_(out.get('UPDATED'))
        deleted = j.JArray_int.cast_(out.get('DELETED'))
        
        
        message = self.bridge.createMessage('wipeout_record') \
                  .setParam('recid', deleted_recid)
        self.bridge.sendMessage(message)
        
        assert len(added) == 0
        assert len(updated) == 0
        assert len(deleted) == 1
        
        assert int(str(deleted[0])) == deleted_recid 
        
