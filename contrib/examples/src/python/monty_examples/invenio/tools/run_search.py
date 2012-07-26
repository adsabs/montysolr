import sys
import montysolr.python_bridge as pbridge

JVMBridge = pbridge.JVMBridge

reload(pbridge) 

JVMBridge2 = pbridge.JVMBridge

from montysolr.initvm import JAVA

JArray_int = JAVA.JArray_int


def run(q):
    print search_unit_solr(None, q)

def search_unit_solr(req=None, q=None):
    res = set()
    message = JVMBridge.createMessage("search_unit_solr") \
                        .setParam("query", q)
    JVMBridge.sendMessage(message)
    result = message.getParam("result")
    if result:
        sj = JVMBridge.getObjMontySolr()
        res = set(JArray_int.cast_(result))
    return res    

if __name__ == '__main__':
    if len(sys.argv) < 2:
        exit("Usage: %s <query>" % (sys.argv[0],))
    run(sys.argv[1])