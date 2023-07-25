
import sys
import time
import logging

import urllib2 as u2
import httplib
import simplejson
import urllib

from montysolr import config 

log = config.get_logger("montysolr.examples.adsabs.reindex")

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
             wait=10,
             max_wait=3600
             ):
    
    wait = float(wait)
    max_wait=float(max_wait)
    req(solr_url, command='discover')
    req(solr_url, command="start")
    
    start = time.time()
    
    data = req(solr_url, command="info")
    if data['status'] == 'busy':
        while time.time() - start < max_wait:
            data = req(solr_url, command="info")
            if data['status'] == 'busy':
                time.sleep(wait)
            else:
                print 'finished', data
                break

        if time.time() - start > max_wait:
            print 'Exiting, max time allowed reached'
            exit(1)
    
    print 'finished in: %s' % (time.time() - start)
    exit(0)
    
if __name__ == '__main__':
    if (len(sys.argv) < 2):
        print """usage: python reindex.py 
                    <solr-url> 
                    <wait-in-sec-between-checks>
                    <total-wait-in-sec>
        examples:
        python run_dump.py http://localhost:8984/solr/invenio-doctor 10 3600
        """
    run_dump(*sys.argv[1:])