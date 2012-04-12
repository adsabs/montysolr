
import tempfile
import pstats
import cProfile
import sys
import time

from montysolr.initvm import JAVA as j
from montysolr.python_bridge import JVMBridge
from monty_invenio import targets

bridge = JVMBridge()

String = j.String #@UndefinedVariable
HashMap = j.HashMap #@UndefinedVariable
ArrayList = j.ArrayList #@UndefinedVariable
List = j.List #@UndefinedVariable
StringBuffer = j.StringBuffer #@UndefinedVariable

def run_get_recids_changes(xtimes=10):
    f = targets.get_recids_changes
         
    for x in xrange(xtimes):
        message = bridge.createMessage("x").setParam("max_records", 10000)\
            .setParam("last_recid", x)
        f(message)
        #print 'round', x

def run_invenio_search(xtimes=10):
    f = targets.invenio_search
    kwargs = HashMap().of_(String, List)
    p = ArrayList(1)
    p.add('recid:0->100 OR recid:101->500 OR recid:501->1000')
    
    kwargs.put('p', p)
    for x in xrange(xtimes):
        message = bridge.createMessage("x") \
            .setParam("kwargs", kwargs)
        f(message)
        y = len(str(String.cast_(message.getResults())))
        print 'round', x, ((y * float(8)) / (1024*1024*8)), "MB"
        message.clear()
        y = None

def run_test():
    for x in xrange(10):
        d = create_string()
        y = len(str(String.cast_(d)))
        print 'round', x, ((y * float(8)) / (1024*1024*8)), "MB"

def create_string():
    sb = StringBuffer()
    sb.append('x' * ((1024*1024*8) * 55))
    return sb.toString()
        
if __name__ == '__main__':
    output = tempfile.gettempdir() + '/profiler.output'
    if len(sys.argv) > 1:
        cProfile.run('run_%s()' % sys.argv[1], output)
    else:
        cProfile.run('run_%s()' % 'get_recids_changes', output)
        
    p = pstats.Stats(output)
    p.strip_dirs().sort_stats('time', 'cum').print_stats()