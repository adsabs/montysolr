
import sys
import os
import solr

from invenio import search_engine

s = solr.SolrConnection('http://localhost:8983/solr')
s.select = solr.SearchHandler(s, '/invenio')


def run(query_file):

    fi = open(query_file, 'r')
    queries = filter(len, map(lambda x: x.strip(), fi.readlines()))
    fi.close()

    success = failure = error = 0
    for q in queries:
        print '---'
        print q
        inv_res = len(search_engine.perform_request_search(None, p=q))
        msg = 'NO'
        inv_query = '\t\t'
        try:
            (solr_res, inv_query) = ask_solr(q)
        except Exception, e:
            solr_res = None
            #print e
            msg = 'ER'
            error += 1
            failure -= 1

        print inv_query
        if inv_res == solr_res:
            success += 1
            msg = 'OK'
        else:
            failure += 1


        print "%s  invenio=%s  montysolr=%s" % (msg, inv_res, solr_res)

    print 'total=%s, success/mismatch/error=%s/%s/%s' % (len(queries), success, failure, error)

def ask_solr(q):
    response = s.query(q, fields=['id'])
    num_found = response.numFound
    inv_query = response.inv_query
    return (num_found, inv_query)

if __name__ == '__main__':
    if len(sys.argv) < 2:
        exit('Usage: <program> <queries.txt>')
    run(*sys.argv[1:])
