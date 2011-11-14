
from montysolr.initvm import montysolr_java as sj
from montysolr.utils import MontySolrTarget

def diagnostic_test(message):
    out = []
    message.setParam("string", "hello")
    #message.setParam("array_int", sj.JArray_int.cast_((0,1,2)))
    message.setResults("Hello world")


def montysolr_targets():
    targets = [
           MontySolrTarget('tests:diagnostic_test', diagnostic_test),
           ]
    return targets     