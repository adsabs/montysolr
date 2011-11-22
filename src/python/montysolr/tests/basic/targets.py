
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
    
    
def montysolr_targets():
    targets = [
           MontySolrTarget(':diagnostic_test', diagnostic_test),
           MontySolrTarget(':receive_text', receive_text),
           MontySolrTarget(':receive_text_array', receive_text_array),
           ]
    return targets     