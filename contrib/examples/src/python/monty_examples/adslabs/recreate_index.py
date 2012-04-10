
import solr
import sys
import time
import logging

import urllib2 as u2
import simplejson
import urllib

log = logging.getLogger("montysolr.examples.adslabs")


def req(url, **kwargs):
    kwargs['wt'] = 'json'
    params = urllib.urlencode(kwargs)
    page = ''
    try:
        conn = urllib.urlopen(url, params)
        page = conn.read()
        rsp = simplejson.load(page)
        conn.close()
        return rsp
    except Exception, e:
        log.error(str(e))
        log.error(page)
        raise e
    
    
    

def recreate_index(solr_url, 
                   max_time=3600,
                   delay=60,
                   handler_name='/invenio/update',
                   maximport=20,
                   batchsize=100,
                   inveniourl='python://search',
                   importurl='/invenio/import?command=full-import&amp;dirs=',
                   updateurl='/invenio/import?command=full-import&amp;dirs=',
                   deleteurl='blankrecords'):
    
    up_url = solr_url + handler_name
    delay = int(delay)
    max_time=int(max_time)
    batchsize=int(batchsize)
    
    start = time.time()
    
    log.info("Starting index (re)build from the scratch")
    
    params = dict(maximport=maximport, batchsize=batchsize,
                  inveniourl=inveniourl, importurl=importurl,
                  updateurl=updateurl, deleteurl=deleteurl)
    
    rsp = req(up_url, last_recid=-1, **params)
    
    round = 0
    recs = 0
    last_round = time.time()
    while (start - now) < max_time: 
        
        rsp = req(up_url, **params)
        now = time.time()
        
        if rsp['importStatus'] == 'idle':
            time.sleep(delay)
            continue
        
        round = round + 1
        recs = recs + batchsize
         
        log.info('Indexing (round/recs/last-round-ms/total-s): %s./%s/%sms/%ss' 
                 % (round, recs, time.time() - last_round, (start - now) / 1000))
        
            
        
    
    
    
    
    



if __name__ == '__main__':
    if (len(sys.argv) < 2):
        print """usage: python recreate_index.py 
                    <solr-url> 
                    [max-time=3600s]
                    [delay=60]
                    [handler-name=/invenio/update]
                    [maximport=200]
                    [batchsize=1000]
                    [inveniourl=python://search]
        examples:
        python recreate_index.py http://localhost:8984/solr/invenio-updater 3600
        """
        
    recreate_index(*sys.argv[1:])