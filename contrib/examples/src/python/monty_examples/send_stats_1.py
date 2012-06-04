

import sys 
import os
import time

def run(workdir, solrurl, user, passw, ):
   
   ourdir = os.path.dirname(sys.argv[0]) or '.'
   
   print '''
   ourdir=%s
   workdir=%s
   solrurl=%s
   user=%s
   ''' % (ourdir, workdir, solrurl, user)
   
   os.system('wget -O %s/stats.xml %s/admin/stats.jsp' % (workdir, solrurl))
   
   cmd = '''%s %s/extract_values.py '//entry/name[contains(string(.),"/invenio/import\\n")]/..//stat' --input %s/stats.xml > %s/extracted.txt''' \
         % (sys.executable, ourdir, workdir, workdir)
   print cmd
   
   x = os.system(cmd)
   if x != 0:
       print 'error, so i am giving up...'
   
   sf = ('@name=Total Documents Processed', '@name=totalTime')
   
   fi = open('%s/extracted.txt' % workdir, 'r')
   out = [time.strftime('%m/%d/%y %H:%M')]
   
   data = []
   for line in fi:
       vals = line.strip().split("\t")
       if vals[0] in sf:
           out.append(vals[1])
   
   
   out[-1] = out[-1][0:-3] # remove milliseconds
   out.append(str(int(out[1])/int(out[2]))) # compute docs per sec
              
   data.append('|'.join(out))
   data.append('')
   
   cmd = '''%s %s/gd_add_row.py --user %s --password %s --spreadsheet ADSIndexingTest --keys IndexingDate,TotalDocs,TotalSecs,DocsPerSec --data "%s"''' \
         % (sys.executable, ourdir, user, passw, ','.join(data))
   
   print cmd
   os.system(cmd)


      
    
def main():
    
    if (len(sys.argv) == 1):
        sys.argv[1:] = '.|http://adsate:8984/solr|montysolr|<pass>'.split('|')
    run(*sys.argv[1:])


if __name__ == '__main__':
    main()