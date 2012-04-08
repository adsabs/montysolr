'''
Created on Feb 4, 2011

@author: rca
'''

from cStringIO import StringIO
from invenio.intbitset import intbitset
from montysolr import config
from montysolr.initvm import JAVA as sj
from montysolr.utils import MontySolrTarget, make_targets
import logging
import os
import sys

import adslabs.multiprocess_api_calls as multi_api_calls
import adslabs.api_calls as api_calls

import time



def format_search_results(message):
    '''Returns the citation summary for the given recids. This method
    inserts the Invenio formatted citation summary directly into the
    response object
    
    @var message: 
        recids: Java int[]
        req: SolrQueryRequest
        rsp: SolrQueryResponse
    
    '''
    rsp = sj.SolrQueryResponse.cast_(message.getParam('response'))
    
    recids = message.getParamArray_int("recids")
    start = time.time()
    if config.MONTYSOLR_BUGDEBUG:
        message.threadInfo("start: citation_summary")
    c_time = time.time()
    iset = intbitset(recids)
    if config.MONTYSOLR_BUGDEBUG:
        message.threadInfo("int[] converted to intbitset in: %s, size=%s" % (time.time() - c_time, len(iset)))
    (wid, (output)) = api_calls.dispatch('citation_summary', iset, 'hcs', 'en', '', '')
    if config.MONTYSOLR_BUGDEBUG:
        message.threadInfo("end: citation_summary pid=%s, finished in %s" % (wid, time.time() - start))
    rsp.add("inv_response", output)




def perform_request_search_bitset(message):
    '''Search and use bitset for exchange of data
    @param query: (str) query string
    @param max_len: (int) how many max results to return
    @param offset: (int) start from the xth element
    '''
    query = unicode(message.getParam("query")).encode("utf8")
    max_len = message.getParam_int('max_len', 25)
    offset = message.getParam_int('offset', 0)
    
    #offset, hit_dump, total_matches, searcher_id = searching.multiprocess_search(query, 0)
    (wid, (offset, hits, total_matches)) = api_calls.dispatch('search', query, max_len=max_len, offset=offset)
    #message.threadInfo("query=%s, total_hits=%s" % (query, total_matches))
    message.setResults(sj.JArray_byte(intbitset(hits).fastdump()))



def perform_request_search_ints(message):
    '''Search and use ints for exchange of data
    @param query: (str) query string
    @param max_len: (int) how many max results to return
    @param offset: (int) start from the xth element
    '''
    query = unicode(message.getParam("query")).encode("utf8")
    max_len = message.getParam_int('max_len', 25)
    offset = message.getParam_int('offset', 0)
        
    #offset, hit_list, total_matches, searcher_id = searching.multiprocess_search(query, 0)
    (wid, (offset, hits, total_matches)) = api_calls.dispatch('search', query, max_len=max_len, offset=offset)
    if len(hits):
        message.setResults(sj.JArray_int(hits))
    else:
        message.setResults(sj.JArray_int([]))

    message.setParam("total", total_matches)




def get_citation_dict(message):
    '''TODO: unittest'''
    dictname = sj.String.cast_(message.getParam('dictname'))
    
    # we will call the local module (not dispatched remotely)
    if hasattr(api_calls, '_dispatch'):
        wid, cd = api_calls._dispatch("get_citation_dict", dictname)
    else:
        wid, cd = api_calls.dispatch("get_citation_dict", dictname)
    
    if cd:
        hm = sj.HashMap().of_(sj.String, sj.JArray_int)
        for k,v in cd.items():
            j_array = sj.JArray_int(v)
            hm.put(k, j_array)
        message.put('result', hm)
        
    #message.threadInfo("%s: %s" % (dictname, str(len(cd))))


def sort_and_format(message):
    '''Calls sorting XOR formatting over the result set'''
    
    start = time.time()
    
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
    
    
    if config.MONTYSOLR_BUGDEBUG:
        message.threadInfo("start: citation_summary")
        message.threadInfo("int[] converted to intbitset in: %s, size=%s" % (time.time() - start, len(recids)))
        
    (wid, (output)) = api_calls.dispatch('sort_and_format', recids, kws)
    
    if config.MONTYSOLR_BUGDEBUG:
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
    targets = make_targets(
           'CitationQuery:get_citation_dict', get_citation_dict,
           'InvenioQuery:perform_request_search_ints', perform_request_search_ints,
           'InvenioQuery:perform_request_search_bitset', perform_request_search_bitset,
           'InvenioFormatter:format_search_results', format_search_results,
           'InvenioFormatter:sort_and_format', sort_and_format,
           'Invenio:diagnostic_test', diagnostic_test,
           )

    num_cpus = 0
    global api_calls
    # start multiprocessing with that many processes in the pool
    if str(config.MONTYSOLR_MAX_WORKERS) == '-1':
        num_cpus = multi_api_calls.start_multiprocessing(None) # detect no of cpus automatically
    elif int(config.MONTYSOLR_MAX_WORKERS) > 1:
        num_cpus = multi_api_calls.start_multiprocessing(int(config.MONTYSOLR_MAX_WORKERS))
    
    if num_cpus > 1:
         api_calls = multi_api_calls # swap the providers
    return targets
