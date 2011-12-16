'''
Created on Feb 4, 2011

@author: rca
'''

from cStringIO import StringIO
from invenio.intbitset import intbitset
from montysolr.initvm import montysolr_java as sj
from montysolr.utils import MontySolrTarget, make_targets
import logging
import os
#import monty_invenio.multiprocess_api_calls as api_calls
import monty_invenio.api_calls as api_calls

import time



def format_search_results(message):
    req = message.getSolrQueryRequest()
    rsp = message.getSolrQueryResponse()
    recids = message.getParamArray_int("recids")
    start = time.time()
    message.threadInfo("start: citation_summary")
    c_time = time.time()
    iset = intbitset(recids)
    message.threadInfo("int[] converted to intbitset in: %s, size=%s" % (time.time() - c_time, len(iset)))
    (wid, (output)) = api_calls.dispatch('citation_summary', iset, 'hcs', 'en', '', '')
    message.threadInfo("end: citation_summary pid=%s, finished in %s" % (wid, time.time() - start))
    rsp.add("inv_response", output)

def format_search_results_local(message):
    req = message.getSolrQueryRequest()
    rsp = message.getSolrQueryResponse()

    recids = message.getParamArray_int("recids")
    out = StringIO()
    # TODO: pass the ln and other arguments
    (wid, (output,)) = api_calls.dispatch("sumarize_records", intbitset(recids), 'hcs', 'en', '', '', out)
    if not output:
        out.seek(0)
        output = out.read()
    del out
    rsp.add("inv_response", output)



def perform_request_search_bitset(message):
    query = unicode(message.getParam("query")).encode("utf8")
    #offset, hit_dump, total_matches, searcher_id = searching.multiprocess_search(query, 0)
    (wid, (offset, hits, total_matches)) = api_calls.dispatch('search', query, 0)
    #message.threadInfo("query=%s, total_hits=%s" % (query, total_matches))
    message.setResults(sj.JArray_byte(intbitset(hits).fastdump()))

def perform_request_search_ints(message):
    query = unicode(message.getParam("query")).encode("utf8")
    #offset, hit_list, total_matches, searcher_id = searching.multiprocess_search(query, 0)
    (wid, (offset, hits, total_matches)) = api_calls.dispatch('search', query, 0)
    if len(hits):
        message.setResults(sj.JArray_int(hits))
    else:
        message.setResults(sj.JArray_int([]))

    message.setParam("total", total_matches)

def handle_request_body(message):
    req = message.getSolrQueryRequest()
    rsp = message.getSolrQueryResponse()
    params = message.getParams()

    start = time.time()
    q = params.get("q").encode('utf8') #TODO: sj.CommonParams.Q is overshadowed by solr.util.CommonParams or is not wrapped at all

    #offset, hit_list, total_matches, searcher_id = searching.multiprocess_search(str(q))
    (wid, (offset, hit_list, total_matches)) = api_calls.dispatch('search', str(q))

    t = time.time() - start
    #message.threadInfo("Query took: %s s. hits=%s and was executed by: %s" % (t, total_matches, searcher_id))

    reader = req.getSearcher().getReader();

    # translate invenio recids into lucene docids
    transl_table = sj.DictionaryCache.INSTANCE.getTranslationCache(reader, "id")
    res = []
    for h in hit_list:
        if transl_table.containsKey(h):
            res.append(transl_table.get(h))

    #logging.error(transl_table.size())

    ds = sj.DocSlice(offset,len(res),res, None, total_matches, 1.0)
    rsp.add("response", ds)

def get_recids_changes(message):
    """Retrieves the recids of the last changed documents"""
    last_recid = None
    if message.getParam("last_recid"):
        last_recid = int(sj.Integer.cast_(message.getParam("last_recid")).intValue())
    mod_date = None
    if message.getParam("mod_date"):
        mod_date = str(message.getParam("mod_date"))
    max_records = 10000
    if message.getParam('max_records'):
        mr = int(sj.Integer.cast_(message.getParam("max_records")).intValue())
        if mr < 100001:
            max_records = mr
    (wid, results) = api_calls.dispatch("get_recids_changes", last_recid, max_records, mod_date=mod_date)
    if results:
        data, mod_date = results
        out = sj.HashMap().of_(sj.String, sj.JArray_int)
        for k,v in data.items():
            out.put(k, sj.JArray_int(v))
        message.setResults(out)
        message.setParam('mod_date', mod_date)




def get_citation_dict(message):

    dictname = str(message.getParam('dictname'))
    hm = sj.HashMap.cast_(message.getParam('result'))

    # we will call the local module (not dispatched remotely)
    cd = api_calls._dispatch("get_citation_dict", dictname)
    message.threadInfo("%s: %s" % (dictname, str(len(cd))))
    if cd:
        #hm = sj.HashMap().of_(sj.Integer, sj.JArray_int)

        message.threadInfo('creating hashmap')
        for k,v in cd.items():
            j_array = sj.JArray_int(v)
            hm.put(int(k), j_array)
        message.threadInfo('finished')


        


def sort_and_format(message):
    req = message.getSolrQueryRequest()
    rsp = message.getSolrQueryResponse()

    recids = intbitset(message.getParamArray_int("recids"))
    kwargs = sj.HashMap.cast_(message.getParam('kwargs'))

    kws = {}

    kset = kwargs.keySet().toArray()
    vset = kwargs.values().toArray()
    max_size = len(vset)
    i = 0
    while i < max_size:
        v = str(vset[i])
        if v[0:1] in ["'", '[', '{'] :
            try:
                v = eval(v)
            except:
                pass
        kws[str(kset[i])] = v
        i += 1

    start = time.time()
    message.threadInfo("start: citation_summary")
    c_time = time.time()

    message.threadInfo("int[] converted to intbitset in: %s, size=%s" % (time.time() - c_time, len(recids)))
    (wid, (output)) = api_calls._dispatch('sort_and_format', recids, kws)

    message.threadInfo("end: citation_summary pid=%s, finished in %s" % (wid, time.time() - start))

    if isinstance(output, list):
        message.setResults(sj.JArray_int(output))
        message.setParam("rtype", "int")
    else:
        message.setResults(output)
        message.setParam("rtype", "string")

def diagnostic_test(message):
    out = []
    message.setParam("query", "boson")
    perform_request_search_ints(message)
    res = sj.JArray_int.cast_(message.getResults())
    out.append('Search for "boson" retrieved: %s hits' % len(res) )
    out.append('Total hits: %s' % sj.Integer.cast_(message.getParam("total")))
    message.setResults('\n'.join(out))

'''
def _get_solr():
    # HACK: this should be lazy loaded and in a separate module
    from montysolr.python_bridge import JVMBridge
    if not hasattr(sj, '__server') and not JVMBridge.hasObj("solr.server"):
        initializer = sj.CoreContainer.Initializer()
        conf = {'solr_home': '/x/dev/workspace/sandbox/montysolr/example/solr',
                 'data_dir': '/x/dev/workspace/sandbox/montysolr/example/solr/data'}

        sj.System.setProperty('solr.solr.home', conf['solr_home'])
        sj.System.setProperty('solr.data.dir', conf['data_dir'])
        core_container = initializer.initialize()
        server = sj.EmbeddedSolrServer(core_container, "")
        JVMBridge.setObj("solr.server", server)
        JVMBridge.setObj("solr.container", core_container)
        sj.__server = server
        return server
    return sj.__server
    return JVMBridge.getObj("solr.server")


def search_unit_solr(message):
    """Called from search_engine"""
    from montysolr.python_bridge import JVMBridge

    sj = JVMBridge.getObjMontySolr()
    server = _get_solr()
    q = str(message.getParam("query")) #String

    query = sj.SolrQuery()
    query.setQuery(q)
    query.setParam("fl", ("id",))
    query_response = server.query(query)

    head_part = query_response.getResponseHeader()
    res_part = query_response.getResults()
    qtime = query_response.getQTime()
    etime = query_response.getElapsedTime()
    nf = res_part.getNumFound()

    a_size = res_part.size()
    res = sj.JArray_int(a_size)
    res_part = res_part.toArray()
    if a_size:
        #it = res_part.iterator()
        #i = 0
        #while it.hasNext():
        for i in xrange(a_size):
            #x = it.next()
            doc = sj.SolrDocument.cast_(res_part[i])
            # we must do this gymnastics because of the tests
            s = str(doc.getFieldValue("id"))  # 002800500
            if s[0] == '0':
                s = s[3:]  # 800500
            res[i] = int(s)
            #i += 1

    message.setParam("QTime", qtime)
    message.setParam("ElapsedTime", etime)
    message.setResults(res)
'''

def montysolr_targets():
    targets = make_targets('handleRequestBody', handle_request_body,
           'rca.python.solr.handler.InvenioHandler:handleRequestBody', handle_request_body,
           'CitationQuery:get_citation_dict', get_citation_dict,
           'InvenioQuery:perform_request_search_ints', perform_request_search_ints,
           'InvenioQuery:perform_request_search_bitset', perform_request_search_bitset,
           'InvenioFormatter:format_search_results', format_search_results,
           'InvenioKeepRecidUpdated:get_recids_changes', get_recids_changes,
           'InvenioFormatter:sort_and_format', sort_and_format,
           'Invenio:diagnostic_test', diagnostic_test,
           )


    # start multiprocessing with that many processes in the pool
    if hasattr(api_calls, "start_multiprocessing"):
        if os.getenv('MONTYSOLR_MAX_WORKERS'):
            api_calls.start_multiprocessing(int(os.getenv('MONTYSOLR_MAX_WORKERS')))
        else:
            api_calls.start_multiprocessing()
    return targets
