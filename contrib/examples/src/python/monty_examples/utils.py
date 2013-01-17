
import simplejson
import urllib

def req(url, **kwargs):
    kwargs['wt'] = 'json'
    params = urllib.urlencode(kwargs)
    page = ''
    conn = urllib.urlopen(url, params)
    page = conn.read()
    rsp = simplejson.loads(page)
    conn.close()
    return rsp
