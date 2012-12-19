
import sys
import time
import logging

import urllib2 as u2
import httplib
import simplejson
import urllib

from montysolr import config 

log = config.get_logger("montysolr.examples.adsabs.run_dump")

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

def run_dump(solr_url, 
             source_field='author',
             target_field='author_collector',
             max_wait=3600
             ):
    
    max_wait=int(max_wait)
    req(solr_url, command='dump', sourceField=source_field, targetField=target_field)
    req(solr_url, command="start")
    
    start = time.time()
    
    data = req(solr_url, command="info")
    if data['status'] == 'busy':
        while time.time() - start < max_wait:
            data = req(solr_url, command="info")
            if data['status'] == 'busy':
                time.sleep(10)
            else:
                break
    
    print 'finished in: %s' % (time.time() - start)
    
if __name__ == '__main__':
    if (len(sys.argv) < 2):
        print """usage: python recreate_index.py 
                    <solr-url> 
                    <source-field>
                    <target-field>
        examples:
        python run_dump.py http://localhost:8984/solr/dump-index author author_collector
        """
    run_dump(*sys.argv[1:])