
from montysolr.initvm import montysolr_java as sj
from montysolr.utils import MontySolrTarget

def diagnostic_test(message):
    out = []
    message.setParam("string", "hello")
    #message.setParam("array_int", sj.JArray_int.cast_((0,1,2)))
    message.setResults("Hello world")


def receive_text(message):
    text = str(message.getParam("arg"))
    message.setResults("%s%s" % (text, "x"))

def receive_text_array(message):
    array = sj.JArray_string.cast_(message.getParam("arg"))
    new_a = sj.JArray_string(len(array) + 1)
    i = 0
    max = len(array) + 1
    while i < len(array):
        new_a[i] = array[i]
        i += 1
    new_a[i] = 'z'
        
    message.setResults(new_a)
    
    
def handle_request_body(message):
    rsp = message.getSolrQueryResponse()
    rsp.add("python", 'says hello!')


def receive_field_value(message):
    val = message.getParam('value')
    val = sj.JArray_string.cast_(val)
    val.append('z')
    
        
def montysolr_targets():
    targets = [
           MontySolrTarget(':diagnostic_test', diagnostic_test),
           MontySolrTarget(':receive_text', receive_text),
           MontySolrTarget(':receive_text_array', receive_text_array),
           MontySolrTarget('receive_field_value', receive_field_value),
           MontySolrTarget('handleRequestBody', handle_request_body),
           ]
    return targets     