
import sys
import os

from montysolr import config 
from monty_examples.utils import req


log = config.get_logger("montysolr.examples.measure_qtime")

def run(solr_url, query, repetitions=1):
    
    repetitions = int(repetitions)
    
    if (os.path.exists(query)):
        queries = load_queries(query)
        log.info("Loaded %s queries from: %s" % (len(queries), query))
    else:
        queries = [query]
    
    results = {}
    
    for i in (range(repetitions)):
        log.info("Starting iteration: #%s" % i)
        for q in queries:
            log.info("%s" % q)
            rsp = req(solr_url, q=q, rows=0)
            
            if (not rsp['responseHeader'].has_key('status') or rsp['responseHeader']['status'] != 0):
                log.error("Error searching: %s" % str(rsp))
                continue
            
            if (results.has_key(q)):
                results[q].add(rsp['responseHeader']['QTime'], rsp['response']['numFound'])
            else:
                results[q] = DataPoint(q, rsp['responseHeader']['QTime'], rsp['response']['numFound'])
            
                
    print "%50s\t%10s\t\t%10s\t%10s\t%10s\t%10s\t%10s" % ("Query", "QTime", "numFound", "minQTime", "maxQTime", "#invocations", "return consistent")
            
    items = sorted(results.items(), key=lambda x: x[1].data[0])
    for k,v in items:
        print str(v)
                

class DataPoint(object):
    def __init__(self, query, qtime, numfound):
        self.points = []
        self.query = query
        self.add(qtime, numfound)
        self.data = ()
        
    def add(self, qtime, numfound):
        self.points.append((int(qtime), int(numfound)))
        self.get_avg()
        
    def get_avg(self):
        numlines = len(self.points)
        qtime = sum(x[0] for x in self.points)
        numfound = sum(x[1] for x in self.points)
        maxqtime = max(x[0] for x in self.points)
        minqtime = min(x[0] for x in self.points)
        consistent = max(x[1] for x in self.points) == min(x[1] for x in self.points)
        self.data = (qtime/numlines, numfound/numlines, minqtime, maxqtime, numlines, consistent)
        return self.data
    
    def __str__(self):
        return '%50s\t%s' % (self.query, ("%10s\t\t%10s\t%10s\t%10s\t%10s\t%10s" % self.data))
        
def load_queries(file):
    fo = open(file, 'r')
    queries = []
    for line in fo:
        if len(line.strip()) > 0 and line[0] != '#':
            queries.append(line.strip())
    return queries

if __name__ == '__main__':
    if (len(sys.argv) < 2):
        print """usage: python measure_qtime.py <solr-url> <file-with-queries|query> [repetition] 
        examples:
        python recreate_index.py http://localhost:8984/solr/select queries.txt 10
        """
        exit(0)
    
    run(*sys.argv[1:])