import urllib2
import time
import os
import sys
import subprocess

SOLRURL = 'http://localhost:8984/solr/waiting-dataimport?command=full-import&url=%(docaddr)s&%(extraparam)s'
DOCADDR = 'file://%(datadir)s/metadata/%(topdir)s/%(recid)s'
EXTRAPARAM = 'dirs=%(datadir)s/fulltexts/arXiv'
COMMIT = 1000000

BASKET_SIZE = 1000
CHECK_IN_ADVANCE = 1000
EXTRACT_CMD = 'tar -C %(datadir)s/metadata -xf %(datadir)s/metadata/%(topdir)s.tgz'
REMOVE_CMD = 'rm -fR %(datadir)s/metadata/%(topdir)s'

def run(recids_file, datadir):
    """This will call the indexer - passing recid on every call,
    if the passed in argument is an integer, then we can work 
    without recids, just using the range (0, recids)
    """
    commit_after = COMMIT

    # find the last recid, that we indexed
    recid_filepath = os.path.join(datadir, 'last-recid-%s' % os.path.split(str(recids_file))[1])
    #os.remove(recid_filepath)
    last_recid = 1
    if os.path.exists(recid_filepath):
        recid_file = open(recid_filepath, 'r')
        x = recid_file.read().strip()
        recid_file.close()
        if x:
            last_recid = x

    
    if isinstance(recids_file, int):
        try:
            recids = range(int(last_recid), recids_file)
        except:
            raise Exception('If you want to index a range, last-recid must be a number, not: "%s"' % last_recid)
    else:
        recids = []
        for r in open(recids_file, 'r'):
            recids.append(r.strip()) 
        if str(last_recid) in recids:
            _i = recids.index(str(last_recid))
            recids = recids[_i+1:]
    
    # we will write the last-id into this file
    recid_file = open(recid_filepath, 'w')
    
    
    start_time = time.time()
    last_extracted_topdir = None
    last_id = recids[-1]

    _for_removal = []
    i = 0
    _success = _failure = 0
    params = {'datadir': datadir}
    params['extraparam'] = EXTRAPARAM % params
    for recid in recids:
        params['topdir'] = int(int(recid) / BASKET_SIZE)
        params['recid'] = recid
        params['docaddr'] = DOCADDR % params

        u = SOLRURL % params
        if i % commit_after == 0 or recid == last_id:
            u += '&commit=true'
            print u
            
        # look at the files ahead and if necessary
        # extract the archive (without waiting)
        if EXTRACT_CMD:
            if datadir and last_extracted_topdir is None:
                args = EXTRACT_CMD % params
                pid = subprocess.Popen(args.split()).pid
                if REMOVE_CMD:
                    _for_removal.insert(0, REMOVE_CMD % params)
                last_extracted_topdir = params['topdir'] 
            _n = i+CHECK_IN_ADVANCE
            if _n < len(recids):
                next_topdir = int(int(recids[_n]) / BASKET_SIZE)
                if next_topdir != last_extracted_topdir:
                    old_topdir = params['topdir'] 
                    params['topdir'] = next_topdir
                    args = EXTRACT_CMD % params
                    # run extraction
                    pid = subprocess.Popen(args.split()).pid
                    last_extracted_topdir = next_topdir
                    if REMOVE_CMD:
                        _for_removal.insert(0, REMOVE_CMD % params)
                        if len(_for_removal) > 2:
                            #remove the folder again
                            subprocess.Popen(_for_removal.pop().split()).pid
                    params['topdir'] = old_topdir        
        
        while True:
            text = urllib2.urlopen(u).read()
            #print text
            if text.find('>idle</') > -1:
                if text.find('Rolledback') > -1:
                    print 'not indexed: %s/%s' % (params['topdir'], params['recid'])
                    _failure += 1
                else:
                    _success += 1
                break
            else:
                print 'sleeping'
                time.sleep(.1)
        i += 1
        total_time = time.time() - start_time
        avg_time = total_time / i
        if i % 100 == 0:
            print '%s.\t%s\t%s/%s\t%s\t%s' % (i, recid, _success, _failure, '%5.3f h.' % (total_time / 3600), avg_time)
        
        recid_file.seek(0)
        recid_file.write(str(recid))
        recid_file.flush()
    
    print '%s.\t%s\t%s/%s\t%s\t%s' % (i, recid, _success, _failure, '%5.3f h.' % (total_time / 3600), avg_time)    
    recid_file.close()
    for r in _for_removal:
        subprocess.Popen(r.split()).pid
    
    

if __name__ == '__main__':
    if len(sys.argv) < 2 or not (os.path.exists(sys.argv[1]) and os.path.exists(sys.argv[2])):
        try:
            sys.argv[1] = int(sys.argv[1])
        except:
            exit('Usage: run_index.py <recids.dat or number> <datadir>')
    
    run(*sys.argv[1:])
