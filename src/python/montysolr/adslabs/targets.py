
from cStringIO import StringIO
from invenio.intbitset import intbitset
from montysolr.initvm import montysolr_java as sj
from montysolr.utils import MontySolrTarget
import logging
import os
import montysolr.adslabs.api_calls as api_calls
#import montysolr.adslabs.multiprocess_api_calls as api_calls

import time
import ptree
import pymongo

def workout_field_value(message):
    sender = str(message.getSender())
    if sender in 'PythonTextField':
        value = message.getParam('externalVal')
        if not value:
            return
        value = str(value)
        message.threadInfo('searching for ' + value)
        vals = {}
        ret = None
        if value:
            parts = value.split('|')
            for p in parts:
                k, v = p.split(':', 1)
                if v[0] == '[' and v[-1] == ']':
                    v = v[1:-1]
                vals[k] = v
            if 'bibcode' in vals and 'src_dir' in vals:
                if vals['src_dir'] == "mongo":
                    mongo = pymongo.Connection('adszee')
                    docs = mongo['solr4ads']['docs']
                    bib = vals['bibcode']
                    doc = docs.find_one({'bibcode': bib}, {'body': 1})
                    if doc and doc.has_key('body'):
                        message.setResults(doc.get('body', ""))
                        return
                else:
                    dirs = vals['src_dir'].split(',')
                    bib = vals['bibcode'].split(',')[0].strip()
                    ptree_path = ptree.id2ptree(bib)
    
                    for d in dirs:
                        full_path = d + ptree_path + 'body.txt'
                        message.threadInfo('looking for ' + full_path)
                        if os.path.exists(full_path):
                            fo = open(full_path, 'r')
                            ret = fo.read()
                            message.setResults(ret.decode('utf-8'))
                            return
                        
def get_recids_changes(message):
    """Retrieves the recids of the last changed documents"""
    last_recid = int(sj.Integer.cast_(message.getParam("last_recid")).intValue())
    max_records = 10000
    if message.getParam('max_records'):
        mr = int(sj.Integer.cast_(message.getParam("max_records")).intValue())
        if mr < 100001:
            max_records = mr
    (wid, results) = api_calls.dispatch("get_recids_changes", last_recid, max_records)
    if results:
        out = sj.HashMap().of_(sj.String, sj.JArray_int)
        for k,v in results.items():
            out.put(k, sj.JArray_int(v))
        message.setResults(out)
        
def montysolr_targets():
    targets = [
           MontySolrTarget('PythonTextField:workout_field_value', workout_field_value),
#           MontySolrTarget('handleRequestBody', handle_request_body),
#           MontySolrTarget('rca.python.solr.handler.InvenioHandler:handleRequestBody', handle_request_body),
#           MontySolrTarget('CitationQuery:get_citation_dict', get_citation_dict),
#           MontySolrTarget('InvenioQuery:perform_request_search_ints', perform_request_search_ints),
#           MontySolrTarget('InvenioQuery:perform_request_search_bitset', perform_request_search_bitset),
#           MontySolrTarget('InvenioFormatter:format_search_results', format_search_results),
           MontySolrTarget('InvenioKeepRecidUpdated:get_recids_changes', get_recids_changes),
#           MontySolrTarget('InvenioFormatter:sort_and_format', sort_and_format),
#           MontySolrTarget('Invenio:diagnostic_test', diagnostic_test),
           ]

    return targets
