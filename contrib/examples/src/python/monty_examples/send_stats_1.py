

import sys 
import os
import time
import urllib
import simplejson

def run(workdir, solrurl, user, passw, spreadsheet):
   
   ourdir = os.path.dirname(sys.argv[0]) or '.'
   
   print '''
   ourdir=%s
   workdir=%s
   solrurl=%s
   user=%s
   ''' % (ourdir, workdir, solrurl, user)
   
   
   jsonpage = urllib.urlopen('%s/admin/mbeans?stats=true\&wt=json' % solrurl)
   jsondata = simplejson.load(jsonpage)
   
   stats = jsondata['solr-mbeans'][3]['/invenio/import']['stats']
   
   jsonpage = urllib.urlopen('%s/admin/cores?wt=json' % solrurl)
   jsondata = simplejson.load(jsonpage)
   
   core_stat = jsondata['status'][jsondata['defaultCoreName']]
   
   sf = ('Total Documents Processed', 'totalTime')
   
   out = [time.strftime('%m/%d/%y %H:%M')]
   
   data = []
   i = 0
   while i < len(stats):
       key = stats[i]
       value = str(stats[i+1])
       if key in sf:
           out.append(value.split(':')[-1])
       i += 2
   
   
   out[-1] = out[-1][0:-3] # remove milliseconds
   out.append(str(int(out[1])/int(out[2]))) # compute docs per sec
              
   
   if os.path.exists(os.path.join(workdir, 'GIT_COMMIT')):
       git_commit = open(os.path.join(workdir, 'GIT_COMMIT'), 'r').read().strip()
   else:
       git_commit = 'unknown'
   
   out.append(git_commit)
   
   out.append('%s' % core_stat['index']['sizeInBytes'])
   out.append('%s' % core_stat['index']['size'])
   
   data.append('|'.join(out))
   data.append('')
   
   tmpl = '''%s %s/gd_add_row.py --user %s --password %s --spreadsheet %s --keys IndexingDate,TotalDocs,TotalSecs,DocsPerSec,GitCommit,IndexSizeBytes,IndexSize --data "%s"'''
   cmd = tmpl % (sys.executable, ourdir, user, passw, spreadsheet, ','.join(data))
   
   print tmpl % (sys.executable, ourdir, user, '<passw>', spreadsheet, ','.join(data))
   os.system(cmd)


      
    
def main():
    
    if (len(sys.argv) == 1):
        sys.argv[1:] = '.|http://adsate:8984/solr|montysolr|<pass>|AdsIndexingTest'.split('|')
    run(*sys.argv[1:])


if __name__ == '__main__':
    main()