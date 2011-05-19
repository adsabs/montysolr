import urllib2
import time
import os
import sys
import time

URLTEMPLATE = 'http://inspirebeta.net/record/%s/export/xm'
URLTEMPLATE = 'http://invenio-demo.cern.ch/record/%s/export/xm'
TARGETDIR = '/opt/rchyla/harvest/demo'
TARGETDIR = '/Users/rca/work/indexing/demo'
BUCKETSIZE = 1000
SLEEP = .05

def run(recids_file, overwrite_old=False):
    """This will call the indexer - passing recid on every call,
    if the passed in argument is an integer, then we can work
    without recids, just using the range (0, recids)
    """

    # find the last recid, that we indexed
    recid_filepath = os.path.join(os.path.dirname(__file__), 'last-recid-harvest%s' % os.path.split(str(recids_file))[1])
    last_recid = 0
    if os.path.exists(recid_filepath):
        recid_file = open(recid_filepath, 'r')
        x = recid_file.read().strip()
        if x:
            last_recid = x
        recid_file.close()


    if isinstance(recids_file, int):
        try:
            recids = range(int(last_recid), recids_file)
        except:
            raise Exception('If you want to index a range, last-recid must be a number, not: %s' % last_recid)
    else:
        recids = []
        for r in open(recids_file, 'r'):
            recids.append(r.strip())
        if str(last_recid) in recids:
            _i = recids.index(str(last_recid))
            recids = recids[_i+1:]

    # we will write the last-id into this file
    recid_file = open(recid_filepath, 'w')

    existing_dirs = {}
    start_time = time.time()
    i = 0
    for recid in recids:
        if SLEEP:
            time.sleep(SLEEP)

        u = URLTEMPLATE % recid
        newdir = '%s/%s' % (TARGETDIR, int(int(recid) / BUCKETSIZE))
        newfile = '%s/%s' % (newdir, recid)
        try:
            if not overwrite_old:
                if os.path.exists(newfile):
                    continue

            text = urllib2.urlopen(u).read()
            if text.find('tag="001">%s<' % recid) > -1:

                if newdir not in existing_dirs:
                    if not os.path.exists(newdir):
                        os.makedirs(newdir)
                    existing_dirs[newdir] = True

                fo = open(newfile, 'w')
                fo.write(text)
                fo.close()
                recid_file.seek(0)
                recid_file.write(recid)
                recid_file.flush()
        except:
            print 'error getting: %s' % u
            continue
        i += 1




if __name__ == '__main__':
    if len(sys.argv) == 1 or not os.path.exists(sys.argv[1]):
        try:
            sys.argv[1] = int(sys.argv[1])
        except:
            exit('Usage: run_index.py <recids.dat or number>')
    
    if not os.path.exists(str(sys.argv[1])):
        try:
            x = int(sys.argv[1])
            print 'Harvesting a range: 0-%s' % x
            run(x)
        except:
            run(sys.argv[1])
    else:
        run(sys.argv[1])



