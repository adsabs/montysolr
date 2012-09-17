
import sys
import time
import logging

import urllib2 as u2
import httplib
import simplejson
import urllib

from montysolr import config 

log = config.get_logger("montysolr.examples.adsabs.recreate_index")

def req(url, **kwargs):
    kwargs['wt'] = 'json'
    params = urllib.urlencode(kwargs)
    page = ''
    try:
        conn = urllib.urlopen(url, params)
        page = conn.read()
        rsp = simplejson.loads(page)
        conn.close()
        return rsp
    except Exception, e:
        log.error(str(e))
        log.error("url: %s, params: %s" % (url, str(params)))
        log.error(page)
        raise e
    
    
    

def recreate_index(solr_url, 
                   max_time=3600,
                   delay=5,
                   handler_name='/invenio/update',
                   maximport=500,
                   batchsize=2000,
                   startfrom=-1,
                   inveniourl='python://search',
                   importurl='/invenio/import?command=full-import&amp;dirs=',
                   updateurl='/invenio/import?command=full-import&amp;dirs=',
                   deleteurl='blankrecords', 
                   ):
    
    up_url = solr_url + handler_name
    delay = int(delay)
    max_time=int(max_time)
    batchsize=int(batchsize)
    
    start = time.time()
    
    log.info("Starting index (re)build from the scratch")
    log.info("""
    solr_url=%s
    max_time=%s
    delay=%s
    handler_name=%s
    maximport=%s
    batchsize=%s
    inveniourl=%s
    importurl=%s
    updateurl=%s
    deleteurl=%s
    startfrom=%s
    """ % (solr_url, max_time, delay, handler_name, maximport, batchsize, inveniourl, importurl, updateurl,
           deleteurl, startfrom))
    
    params = dict(maximport=maximport, batchsize=batchsize,
                  inveniourl=inveniourl, importurl=importurl,
                  updateurl=updateurl, deleteurl=deleteurl)
    
    rsp = req(up_url, last_recid=startfrom, **params)
    
    round = 0
    recs = 0
    last_round = time.time()
    now = time.time()
    idtoken = '#0'
    i = 0
    
    while (now - start) < max_time: 
        i = i + 1
        idtoken = '#%s' % i
        
        rsp = req(up_url, idtoken=idtoken, **params)
        now = time.time()
        
        if 'idtoken' not in rsp:
            break
        
        if rsp['idtoken'] != idtoken:
            time.sleep(delay)
            continue
        
        
        round = round + 1
        
        recs = recs + batchsize
         
        log.info('Indexing (round/recs/last-round-ms/total-s): %s./%s/%s/%ss' 
                 % (round, recs, time.time() - last_round, (time.time()-start) / 1000))
        
        last_round = time.time()
            
        
    log.info('Stopped at round: %s, total time: %s' % (round, time.time() - start))
    
    
    
    



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
        python recreate_index.py http://localhost:8984/solr 3600 30 /invenio-updater 200
        """
    recreate_index(*sys.argv[1:])