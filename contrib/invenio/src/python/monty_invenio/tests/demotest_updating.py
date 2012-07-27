
from montysolr.initvm import JAVA as j
from montysolr.tests.montysolr_testcase import LuceneTestCase

from monty_invenio.tests.invenio_demotestcase import InvenioDemoTestCaseLucene
from montysolr.utils import make_targets

import monty_invenio.targets as targets

from invenio import dbquery, bibupload, bibupload_regression_tests, search_engine, bibrecord

import sys
import time
import unittest

from org.apache.solr.response import SolrQueryResponse
from org.apache.solr.client.solrj.request import QueryRequest
from java.lang import Integer
from java.util import HashMap

JArray_string = j.JArray_string #@UndefinedVariable
JArray_int = j.JArray_int #@UndefinedVariable
JArray_byte = j.JArray_byte #@UndefinedVariable
#QueryRequest = j.QueryRequest #@UndefinedVariable
#SolrQueryResponse = j.SolrQueryResponse #@UndefinedVariable
#Integer = j.Integer #@UndefinedVariable
#HashMap = j.HashMap #@UndefinedVariable


'''Tests the Python side of the InvenioKeepRecidUpdated - 
this is a very comprehensive suite, it requires demo site
'''

# beware, granularity is in seconds!!!
def change_date(recid, diff=1, cdiff=0):
    def change(d, x):
        t = time.mktime(d.timetuple())
        t += x
        n = d.fromtimestamp(t)
        return n.strftime(format="%Y-%m-%d %H:%M:%S")
            
    recs = dbquery.run_sql("SELECT id, creation_date, modification_date FROM bibrec WHERE id=%s" % recid)
    c = recs[0][1]
    c_date = change(c, cdiff)
    
    m = recs[0][2]
    m_date = change(m, diff)
    #sys.stderr.write('cdate: %s x %s; mdate: %s x %s --> ' % (c, c_date, m, m_date))
    
    #sys.stderr.write("UPDATE bibrec SET creation_date='%s', modification_date='%s' WHERE id=%s\n" % (c_date,m_date,recid))
    return dbquery.run_sql("UPDATE bibrec SET creation_date='%s', modification_date='%s' WHERE id=%s" % (c_date,m_date,recid))

        
def change_records(message):
    recids = list(JArray_int.cast_(message.getParam('recids')))
    diff = message.getParam('diff')
    if diff:
        diff = int(str(diff))
    else:
        diff = 5 # 5 secs older
    for r in recids:
        change_date(r, diff=diff)
        #dbquery.run_sql("UPDATE bibrec SET modification_date=NOW() + %s WHERE id=%s" % (diff, r))

def add_records(message):
    """We are not adding, but if creation_date == modification_date
    then recs are considerd new"""
    
    recids = list(JArray_int.cast_(message.getParam('recids')))
    diff = message.getParam('diff')
    if diff:
        diff = int(str(diff))
    else:
        diff = 5 # 5 secs older
    for r in recids:
        dbquery.run_sql("UPDATE bibrec SET modification_date=NOW() + %s, creation_date=NOW() + %s WHERE id=%s" % (diff,diff, r))


def reset_records(message):
    dbquery.run_sql("UPDATE bibrec SET modification_date=NOW(), creation_date=NOW()")
    
    recs = dbquery.run_sql("SELECT id FROM bibrec WHERE id>104")
    for x in recs:
        rid = x[0]
        bibupload_regression_tests.wipe_out_record_from_all_tables(rid)

def create_delete(message):
    """creates and deletes the record"""
    diff = message.getParam('diff')
    if diff:
        diff = int(str(diff))
    else:
        diff = 5 # 5 secs older
    recid = bibupload.create_new_record()
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
    ret = bibupload.bibupload(recs[0], opt_mode='insert')
    recid = ret[1]
    message.setResults(Integer(recid))
    change_date(recid, diff=diff)
    #dbquery.run_sql("UPDATE bibrec SET modification_date=NOW()+%s, creation_date=NOW() + %s WHERE id=%s" % (diff, diff,recid))

def create_record(message):
    """creates record"""
    diff = message.getParam('diff')
    if diff:
        diff = int(str(diff))
    else:
        diff = 5 # 5 secs older
    recid = bibupload.create_new_record()
    message.setResults(Integer(int(recid)))
    #dbquery.run_sql("UPDATE bibrec SET modification_date=NOW() + %s, creation_date=NOW() + %s WHERE id=%s" % (diff, diff,recid))
    change_date(recid, diff=diff, cdiff=diff)    
    
def delete_record(message):
    """deletes record"""
    diff = message.getParam('diff')
    if diff:
        diff = int(str(diff))
    else:
        diff = 5 # 5 secs older
    recid = int(Integer.cast_(message.getParam('recid')).intValue())
    record = search_engine.get_record(recid)
    bibrecord.record_add_field(record, "980", subfields=[("c", "DELETED")])
    ret = bibupload.bibupload(record, opt_mode='replace')
    recid = ret[1]
    message.setResults(Integer(recid))
    
    # extra query needed because invenio sets modification date=NOW()
    # and so the deleted recs have modification_date<creation_date
    #dbquery.run_sql("UPDATE bibrec SET modification_date=NOW() + %s WHERE id=%s" % (diff, recid))
    change_date(recid, diff=diff)
    
        
    
def wipeout_record(message):
    recid = int(Integer.cast_(message.getParam('recid')).intValue())
    bibupload_regression_tests.wipe_out_record_from_all_tables(recid)  
        
        
        
def montysolr_targets():
    return make_targets("MyInvenioKeepRecidUpdated:get_recids_changes", targets.get_recids_changes, # register for the Java unittest
                        get_astro_changes=targets.get_astro_changes,
                        change_records=change_records,
                        reset_records=reset_records,
                        add_records=add_records,
                        create_delete=create_delete,
                        wipeout_record=wipeout_record,
                        create_record=create_record,
                        delete_record=delete_record,
                        
                        )



class Test(InvenioDemoTestCaseLucene):
    """This test requires Invenio instance with the demo
    records loaded
    """

    def setUp(self):
        LuceneTestCase.setUp(self)
        handler = self.bridge.getHandler()
        if not handler.has_target('*:wipeout_record'):
            self.addTargets(sys.modules[__name__])
        if not handler.has_target('InvenioQuery:perform_request_search_ints'):
            self.addTargets(targets)
        reset_records(None)
        self.max_recs = dbquery.run_sql("SELECT COUNT(*) FROM bibrec")[0][0]
        self.max_id = dbquery.run_sql("SELECT MAX(id) FROM bibrec")[0][0]
        time.sleep(0.5)
        
    def test_get_recids_added(self):
        
        message = self.bridge.createMessage('add_records') \
                    .setParam('diff', 6) \
                    .setParam('recids', JArray_int(range(1, 11)))
        self.bridge.sendMessage(message)
        
        req = QueryRequest()
        rsp = SolrQueryResponse()

        message = self.bridge.createMessage('get_recids_changes') \
                    .setSender('InvenioKeepRecidUpdated') \
                    .setParam('response', rsp) \
                    .setParam('last_recid', 30)
        self.bridge.sendMessage(message)

        results = message.getResults()
        out = HashMap.cast_(results)

        added = JArray_int.cast_(out.get('ADDED'))
        assert len(added) == 10

    def test_get_recids_added_all(self):
        
        # make all records appear as old
        message = self.bridge.createMessage('change_records') \
                    .setParam('diff', -1) \
                    .setParam('recids', JArray_int([1]))
        self.bridge.sendMessage(message)
                    
        req = QueryRequest()
        rsp = SolrQueryResponse()

        message = self.bridge.createMessage('get_recids_changes') \
                    .setSender('InvenioKeepRecidUpdated') \
                    .setParam('response', rsp) \
                    .setParam('last_recid', 0) #test we can deal with extreme cases
        self.bridge.sendMessage(message)

        results = message.getResults()
        out = HashMap.cast_(results)

        added = JArray_int.cast_(out.get('ADDED'))
        updated = JArray_int.cast_(out.get('UPDATED'))
        deleted = JArray_int.cast_(out.get('DELETED'))
        
        assert (len(added)+len(updated)+len(deleted)) == self.max_recs -1
        
        message = self.bridge.createMessage('get_recids_changes') \
                    .setSender('InvenioKeepRecidUpdated') \
                    .setParam('response', rsp) \
                    .setParam('max_records', 10) \
                    .setParam('last_recid', 0) #test we can deal with extreme cases
        self.bridge.sendMessage(message)

        results = message.getResults()
        out = HashMap.cast_(results)

        added = JArray_int.cast_(out.get('ADDED'))
        updated = JArray_int.cast_(out.get('UPDATED'))
        deleted = JArray_int.cast_(out.get('DELETED'))
        
        assert (len(added) == 10)


    def test_get_recids_nothing_changed(self):

        req = QueryRequest()
        rsp = SolrQueryResponse()

        message = self.bridge.createMessage('get_recids_changes') \
                    .setSender('InvenioKeepRecidUpdated') \
                    .setParam('response', rsp) \
                    .setParam('last_recid', 9999999)
        self.bridge.sendMessage(message)

        results = message.getResults()
        assert results is None
        
        message = self.bridge.createMessage('get_recids_changes') \
                    .setSender('InvenioKeepRecidUpdated') \
                    .setParam('response', rsp) \
                    .setParam('last_recid', int(self.max_id))
        self.bridge.sendMessage(message)

        results = message.getResults()
        assert results is None

    def test_get_recids_minus_one(self):

        req = QueryRequest()
        rsp = SolrQueryResponse()

        message = self.bridge.createMessage('get_recids_changes') \
                    .setSender('InvenioKeepRecidUpdated') \
                    .setParam('response', rsp) \
                    .setParam('last_recid', -1)
        self.bridge.sendMessage(message)

        results = message.getResults()
        out = HashMap.cast_(results)

        added = JArray_int.cast_(out.get('ADDED'))
        updated = JArray_int.cast_(out.get('UPDATED'))
        deleted = JArray_int.cast_(out.get('DELETED'))


        assert (len(added)+len(updated)+len(deleted)) == self.max_recs
        assert len(updated) == 0
        #self.assertTrue(len(deleted) == 0, msg="If this test fails, then check your demo site is freshly created")
        
        
    def test_get_recids_deleted(self):
        
        #time.sleep(1) # otherwise the new record has the same creation time as the old recs
        #message = self.bridge.createMessage('create_delete')
        #self.bridge.sendMessage(message)
        message = self.bridge.createMessage('create_record')
        self.bridge.sendMessage(message)
        created_recid = int(Integer.cast_(message.getResults()).intValue())
        
        message = self.bridge.createMessage('delete_record').setParam('recid', created_recid)
        self.bridge.sendMessage(message)
        deleted_recid = int(str(message.getResults()))
        
        
        req = QueryRequest()
        rsp = SolrQueryResponse()

        message = self.bridge.createMessage('get_recids_changes') \
                    .setSender('InvenioKeepRecidUpdated') \
                    .setParam('response', rsp) \
                    .setParam('last_recid', 30)
        self.bridge.sendMessage(message)

        results = message.getResults()
        out = HashMap.cast_(results)
        
        added = JArray_int.cast_(out.get('ADDED'))
        updated = JArray_int.cast_(out.get('UPDATED'))
        deleted = JArray_int.cast_(out.get('DELETED'))
        
        
        message = self.bridge.createMessage('wipeout_record') \
                  .setParam('recid', deleted_recid)
        self.bridge.sendMessage(message)
        assert created_recid == deleted_recid
        assert len(added) == 0
        assert len(updated) == 0
        assert len(deleted) == 1
        
        assert int(str(deleted[0])) == deleted_recid 

    def test_get_recids_add_change(self):
        
        #time.sleep(1) # otherwise the new record has the same creation time as the old recs
        #message = self.bridge.createMessage('create_delete')
        #self.bridge.sendMessage(message)
        message = self.bridge.createMessage('create_record')
        self.bridge.sendMessage(message)
        created_recid = int(Integer.cast_(message.getResults()).intValue())
        
        
        req = QueryRequest()
        rsp = SolrQueryResponse()

        message = self.bridge.createMessage('get_recids_changes') \
                    .setSender('InvenioKeepRecidUpdated') \
                    .setParam('response', rsp) \
                    .setParam('last_recid', 30)
        self.bridge.sendMessage(message)
        
        last_updated = str(message.getParam('mod_date'))

        results = message.getResults()
        out = HashMap.cast_(results)
        
        added = JArray_int.cast_(out.get('ADDED'))
        updated = JArray_int.cast_(out.get('UPDATED'))
        deleted = JArray_int.cast_(out.get('DELETED'))
        
        
        
        assert len(added) == 1
        assert len(updated) == 0
        assert len(deleted) == 0
        
        
        message = self.bridge.createMessage('delete_record') \
            .setParam('recid', created_recid) \
            .setParam('diff', 10)
        self.bridge.sendMessage(message)
        deleted_recid = int(str(message.getResults()))
        
        
        message = self.bridge.createMessage('get_recids_changes') \
                    .setSender('InvenioKeepRecidUpdated') \
                    .setParam('response', rsp) \
                    .setParam('last_recid', deleted_recid) \
                    .setParam('mod_date', last_updated)
                    
        self.bridge.sendMessage(message)
        

        results = message.getResults()
        out = HashMap.cast_(results)
        
        added = JArray_int.cast_(out.get('ADDED'))
        updated = JArray_int.cast_(out.get('UPDATED'))
        deleted = JArray_int.cast_(out.get('DELETED'))
        
        
        
        assert len(added) == 0
        assert len(updated) == 0
        assert len(deleted) == 1
        
        
        message = self.bridge.createMessage('wipeout_record') \
                  .setParam('recid', deleted_recid)
        self.bridge.sendMessage(message)
        assert created_recid == deleted_recid
        
        assert int(str(deleted[0])) == deleted_recid 
        

if __name__ == "__main__":
    #import sys;sys.argv = ['', 'Test.test_get_recids_add_change']
    unittest.main()