import pstats
import cProfile
import sys
import tempfile

from monty_invenio import api_calls

def run_get_recids_changes(xtimes=10):
    f = api_calls.get_recids_changes
    for x in xrange(xtimes):
        recids = f(last_recid=x, max_recs=10000)
        #print 'round', x

def run_invenio_search_xml(xtimes=10):
    kwargs = {'p':'recid:0->100 OR recid:101->500 OR recid:501->1000'}
    f = api_calls.invenio_search_xml
    for x in xrange(xtimes):
        data = f(kwargs)
        y = len(data)
        print 'round', x, ((y * float(8)) / (1024*1024*8)), "MB"

def run_invenio_search_xml2(xtimes=10):
    kwargs = {'p':'recid:0->100 OR recid:101->500 OR recid:501->1000'}
    f = api_calls.invenio_search_xml2
    for x in xrange(xtimes):
        data = f(kwargs)
        y = len(data)
        print 'round', x, ((y * float(8)) / (1024*1024*8)), "MB"        
        
if __name__ == '__main__':
    output = tempfile.gettempdir() + '/profiler.output'
    if len(sys.argv) > 1:
        cProfile.run('run_%s()' % sys.argv[1], output)
    else:
        cProfile.run('run_%s()' % 'get_recids_changes', output)
        
    p = pstats.Stats(output)
    p.strip_dirs().sort_stats('time', 'cum').print_stats()