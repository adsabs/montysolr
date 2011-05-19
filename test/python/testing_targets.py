'''
Created on Feb 4, 2011

@author: rca
'''

from montysolr.utils import MontySolrTarget
import os
from montysolr import initvm

sj = initvm.montysolr_java


from invenio import bibrank_citation_searcher as bcs
    

def handle_request_body(message):
    rsp = message.getSolrQueryResponse()
    rsp.add("python", 'says hello!')
    
def receive_field_value(message):
    val = message.getParam('value')
    val = sj.JArray_string.cast_(val)
    val.append('z')
    
def get_citation_dict(message):
    dictname = sj.String.cast_(message.getParam('dictname'))
    cd = bcs.get_citation_dict(dictname)
    if cd:
        hm = sj.HashMap().of_(sj.String, sj.JArray_int)
        
        for k,v in cd.items():
            j_array = sj.JArray_int(v)
            hm.put(k, j_array)
        
        message.put('result', hm)
    
    




def montysolr_targets():
    targets = [
           MontySolrTarget('receive_field_value', receive_field_value),
           MontySolrTarget('handleRequestBody', handle_request_body),
           MontySolrTarget('CitationQuery:get_citation_dict', get_citation_dict),
           ]
    
    return targets
    