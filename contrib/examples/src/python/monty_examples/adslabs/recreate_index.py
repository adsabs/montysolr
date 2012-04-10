
import solr
import sys
import time
import logging

#log = logging.

def recreate_index(solr_url, max_time):
    
    server = solr.SolrConnection(solr_url)
    
    
    
    



if __name__ == '__main__':
    if (len(sys.argv) < 2):
        print """usage: python recreate_index.py <solr-url> <solr-port>
        examples:
        python recreate_index.py http://localhost:8984/solr/invenio-updater 3600
        """
        
    recreate_index(*sys.argv[1:])