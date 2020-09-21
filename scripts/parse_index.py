import sys
import pprint

def parse(lines, docid, fvalue):
    out = [[] for x in range(1000)]
    seqs = ['field', 'term', 'doc', 'freq', 'pos']
    ix = 0
    i = 0
    term = None
    maxpos = 0

    while i < len(lines):
        l = lines[i].strip()
        if l == '':
            i+= 1
            continue
        #print l
        parts = l.split(' ', 1)
        if len(parts) <= 1:
            i+= 1
            continue
        key, value = parts[0], parts[1]
        
        
        if key == seqs[ix]:
            if key == 'field':
                if value == fvalue:
                    ix += 1
                else:
                    ix = 0
            elif key == 'term':
                term = value
                ix += 1
            elif key == 'doc':
                doc = value
                if value != docid:
                    j = i
                    while j+1 < len(lines):
                        k = lines[j+1].strip().split(' ', 1)[0]
                        if k == 'pos' or k == 'freq':
                            i+=1
                            j+=1
                        elif k == 'term':
                            i += 1
                            ix -= 1
                            break
                        else:
                            break
                    i += 1
                    #ix -= 1
                    continue
                ix += 1
            elif key == 'freq':
                freq = value
                ix += 1
            elif key == 'pos':
                pos = int(value)
                if pos > maxpos:
                    maxpos = pos
                out[pos].append(term)
                j = i
                #print 'adding', term, 'position', pos
                while j+1 < len(lines) and lines[j+1].strip().split(' ', 1)[0] == 'pos':
                    pos = int(lines[j+1].strip().split(' ', 1)[1])
                    if pos > maxpos:
                        maxpos = pos
                    out[pos].append(term)
                    #print 'adding', term, 'position', pos
                    j += 1
                    i += 1

                ix -= 3
                term = None
                
        i += 1



    return out[0:maxpos+1]


#usage:
# make your test write index using text codec
# replaceInFile(newConfig, "solr.SchemaCodecFactory", "solr.SimpleTextCodecFactory");    
# then find the location of the _pst file and process it
# lines = open('/tmp/solr.analysis.TestAdsabsTypeFulltextParsing_3E990BE8BF267D4E-001/init-core-data-001/index/_0.pst', 'r').read().split('\n')
# list(enumerate(parse(lines, '41')))
"""
[(0, []),
 (1, ['acr::hubble']),
 (2, ['constant']),
 (3, ['acr::summary', 'summary']),
 (4, []),
 (5, ['acr::program', 'program']),
 (6, ['acr::luminosity', 'luminosity']),
 (7, ['acr::calibration', 'calibration']),
 (8, ['acr::type', 'type']),
 (9, ['ia']),
 (10, ['acr::supernovae', 'supernovae']),
 (11, ['acr::by', 'by']),
 (12, ['acr::means', 'means']),
 (13, ['acr::cepheids', 'cepheids']),
 (14, []),
 (15, []),
 (16, []),
 (17, []),
 (18, []),
 (19, [])]
 """

if __name__ == '__main__':
    if len(sys.argv) > 1:
        lines = open(sys.argv[1], 'r').read().split('\n')
        pprint.pprint(list(enumerate(parse(lines, sys.argv[2], sys.argv[3]))))
