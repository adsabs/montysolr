
from cStringIO import StringIO
from invenio.intbitset import intbitset
from montysolr.initvm import montysolr_java as sj
from montysolr.utils import MontySolrTarget
import logging
import os

from montysolr import config

if config.MSMULTIPROC:
    import montysolr.adslabs.multiprocess_api_calls as api_calls
else:
    import montysolr.adslabs.api_calls as api_calls

import time

def workout_field_value(message):
    sender = str(message.getSender())
    if sender in 'PythonTextField':
        value = message.getParam('externalVal')
        if not value:
            return
        bibcode = str(value)
        message.threadInfo('got bibcode ' + bibcode)
        ret = None
        if bibcode:
            start = time.time()
            (wid, results) = api_calls.dispatch('load_fulltext', bibcode)
            t = time.time() - start
            message.threadInfo("Fulltext load took: %s s. len=%s and was executed by: %s" % (t, len(results), wid))
            message.setResults(results)
        else:
            message.threadInfo("hey, i didn't get a bibcode!")
            message.setResults("")
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

    # start multiprocessing with that many processes in the pool
    if config.MSMULTIPROC and hasattr(api_calls, "start_multiprocessing"):
        if os.getenv('MONTYSOLR_MAX_WORKERS'):
            api_calls.start_multiprocessing(int(os.getenv('MONTYSOLR_MAX_WORKERS')))
        else:
            api_calls.start_multiprocessing()
            
    return targets