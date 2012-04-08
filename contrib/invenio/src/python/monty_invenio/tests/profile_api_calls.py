import pstats
import cProfile
import sys

from monty_invenio import api_calls

def run_get_recids_changes(xtimes=10):
    f = api_calls.get_recids_changes
    for x in xrange(xtimes):
        recids = f(last_recid=x, max_recs=10000)
        #print 'round', x
        
        
if __name__ == '__main__':
    if len(sys.argv) > 1:
        cProfile.run('run_%s()' % sys.argv[1], 'profiler.output')
    else:
        cProfile.run('run_%s()' % 'get_recids_changes', 'profiler.output')
    p = pstats.Stats('profiler.output')
    p.strip_dirs().sort_stats('time', 'cum').print_stats()