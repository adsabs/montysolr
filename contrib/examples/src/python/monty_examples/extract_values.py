

from montysolr import config, optionparse
from lxml import etree

import sys 


log = config.get_logger('montysolr.extract_values')



def run(xpath, input=None, sep=None, verbose=None):
    '''
    Utility to extract text values from the xml

    usage: %prog [options]
    -i, --input = I: file input
    -e, --sep = SEP: character to use as a value separator for the data fields, default [|]
    -v, --verbose = VERBOSE: numeric value of the logging module [30]
    '''

    log.setLevel(int(verbose))
    
    
    root = etree.parse(input)
    tree = root.getroot()
    
    for xp in xpath:
        
        xp = xp.replace('\\n', '\n').replace('\\t', '\t')
        log.info(xp)
        
        elems = tree.xpath(xp)
        if len(elems) < 1:
            log.error("Nothing found in %s for xpath %s" % (input, xp))
            continue
        
        for e in elems:
            out = []
            for k,v in e.attrib.items():
                out.append('@%s=%s' % (k,v))
                break
            out.append(e.text.strip())
            print '\t'.join(out)
            #etree.tostring(e)
        
    log.info("Success!")


      
    
def main():
    if len(sys.argv) == 1 or (len(sys.argv) > 1 and sys.argv[1] == 'demo'):
        args = '''
          --input /tmp/stats.xml
          '''
        args = args.split()
        args.insert(0, '//entry/name[contains(string(.),"/invenio/import\n")]/..//stat')
        sys.argv[1:] = args

    options,args=optionparse.parse(run.__doc__)

    run(args, **options.__dict__)


if __name__ == '__main__':
    main()