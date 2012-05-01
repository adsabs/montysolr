
import sys
import time
import logging

import urllib2 as u2
import httplib
import simplejson
import urllib

logging_level = logging.DEBUG
log = None
_loggers = []

def get_logger(name):
    """Creates a logger for you - with the parent newseman logger and
    common configuration"""
    if name[0:8] != 'montysol' and len(name) > 8:
        sys.stderr.write("Warning: you are creating a logger without 'montysolr' as a root (%s),"
        "this means that it will not share montysolr settings and cannot be administered from one place" % name)
    if log:
        logger = log.manager.getLogger(name)
    else:
        logger = logging.getLogger(name)
        hdlr = logging.StreamHandler(sys.stderr)
        formatter = logging.Formatter('%(levelname)s %(asctime)s %(name)s:%(lineno)d    %(message)s')
        hdlr.setFormatter(formatter)
        logger.addHandler(hdlr)
        logger.setLevel(logging_level)
        logger.propagate = 0
    if logger not in _loggers:
        _loggers.append(logger)
    return logger

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
        log.error(page)
        raise e
    
    
    

def recreate_index(solr_url, 
                   max_time=3600,
                   delay=15,
                   handler_name='/invenio/update',
                   maximport=200,
                   batchsize=1000,
                   inveniourl='python://search',
                   importurl='/invenio/import?command=full-import&amp;dirs=',
                   updateurl='/invenio/import?command=full-import&amp;dirs=',
                   deleteurl='blankrecords', 
                   startfrom=-1):
    
    up_url = solr_url + handler_name
    delay = int(delay)
    max_time=int(max_time)
    batchsize=int(batchsize)
    
    start = time.time()
    
    log.info("Starting index (re)build from the scratch")
    
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
    
    while (start - now) < max_time: 
        i = i + 1
        idtoken = '#%s' % i
        
        rsp = req(up_url, idtoken=idtoken, **params)
        now = time.time()
        
        if rsp['idtoken'] != idtoken:
            time.sleep(delay)
            continue
        
        
        round = round + 1
        
        recs = recs + batchsize
         
        log.info('Indexing (round/recs/last-round-ms/total-s): %s./%s/%sms/%ss' 
                 % (round, recs, time.time() - last_round, (time.time()-start) / 1000))
        
        last_round = time.time()
            
        
    
    
    
    
    



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
    log = get_logger("montysolr.example.adsabs")
    recreate_index(*sys.argv[1:])