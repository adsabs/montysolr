
from montysolr.initvm import JAVA as j
from montysolr.utils import make_targets

from org.apache.solr.response import SolrQueryResponse
from org.apache.solr.request import SolrQueryRequest

#SolrQueryResponse = j.SolrQueryResponse #@UndefinedVariable 
JArray_string = j.JArray_string #@UndefinedVariable

def diagnostic_test(message):
    out = []
    message.setParam("string", "hello")
    #message.setParam("array_int", JArray_int.cast_((0,1,2)))
    message.setResults("Hello world")


def receive_text(message):
    text = str(message.getParam("arg"))
    message.setResults("%s%s" % (text, "x"))

def receive_text_array(message):
    array = JArray_string.cast_(message.getParam("arg"))
    new_a = JArray_string(len(array) + 1)
    i = 0
    max = len(array) + 1
    while i < len(array):
        new_a[i] = array[i]
        i += 1
    new_a[i] = 'z'
        
    message.setResults(new_a)
    
    
def handle_request_body(message):
    rsp = SolrQueryResponse.cast_(message.getParam("rsp"))
    rsp.add("python", 'says hello!')


def receive_field_value(message):
    val = message.getParam('value')
    val = JArray_string.cast_(val)
    val.append('z')
    
        
def montysolr_targets():
    return make_targets(receive_text=receive_text,
                        receive_text_array=receive_text_array,
                        receive_field_value=receive_field_value,
                        handle_request_body=handle_request_body)
