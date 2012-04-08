
import pstats
import cProfile
import sys

from montysolr.initvm import JAVA as j
from montysolr.python_bridge import JVMBridge
from monty_invenio import targets

bridge = JVMBridge()

def run_get_recids_changes(xtimes=10):
    f = targets.get_recids_changes
         
    for x in xrange(xtimes):
        message = bridge.createMessage("x").setParam("max_records", 10000)\
            .setParam("last_recid", x)
        f(message)
        #print 'round', x
        
        
if __name__ == '__main__':
    
    if len(sys.argv) > 1:
        cProfile.run('run_%s()' % sys.argv[1], 'profiler.output')
    else:
        cProfile.run('run_%s()' % 'get_recids_changes', 'profiler.output')
    p = pstats.Stats('profiler.output')
    p.strip_dirs().sort_stats('time', 'cum').print_stats()