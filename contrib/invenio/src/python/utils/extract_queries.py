import time
import os
import sys
import re

_d = '/opt/invenio/lib/python'
if _d not in sys.path:
    sys.path.insert(0, _d)

from invenio import search_engine_query_parser

invenio_qparser = search_engine_query_parser.SearchQueryParenthesisedParser()
invenio_qconverter = search_engine_query_parser.SpiresToInvenioSyntaxConverter()

def run(searchlog_file):
    """This will extract some known query patterns form the
    the search logs
    """

    # find the last recid, that we indexed
    out_filepath = os.path.join(os.path.dirname(searchlog_file), 'searchlog-%s' % os.path.split(str(searchlog_file))[1])
    
    fi = open(searchlog_file, 'r')
    fo = open(out_filepath, 'w')
    
    i = 0
    for line in fi:
        #   20110101004545#ss#find a trnka, jaroslav##HEP#16
        line = line.strip()
        parts = line.split('#')
        if len(parts) != 6:
            continue
        fdate, fform, fvalue, ffield, fcollection, fresults = parts
        q = convert_query(fvalue, ffield)
        if q:
            fo.write('%s\n' % q)
        
            i+= 1
            if 1 % 1000 == 0:
                print i
        continue
    fo.close()
    
    if 0:
        val = None
        if ffield: #the field was specified
            if ffield == 'author':
                val = format_author(fvalue)
            elif ffield == 'exactauthor':
                val = format_exactauthor(fvalue)
            elif ffield == 'fulltext':
                val = format_fulltext(fvalue)
            elif ffield == 'journal':
                val = format_journal(fvalue)
            elif ffield == 'title':
                val = format_title(fvalue)
            elif ffield == 'keyword':
                val = format_keyword(fvalue)
            elif ffield == 'year':
                val = format_year(fvalue)
            
        if val:
            fo.write('%s\n' % val)
            
_regexes = []        
def get_query_regexes():
    global _regexes
    if _regexes:
        return _regexes
    _regexes.extend([
         (re.compile(r'001\s*\:'), 'recid:'),
         (re.compile(r'980\s*\:'), 'status:'),
         
         (re.compile(r'100__u\:'), 'affiliation:'),
         (re.compile(r'700__u\:'), 'affiliation:'),
         (re.compile(r'902__a\:'), 'affiliation:'),
         
         (re.compile(r'100\s*\:'), 'author:'),
         (re.compile(r'700\s*\:'), 'author:'),
         
         (re.compile(r'710\s*\:'), 'corporation:'),
         
         (re.compile(r'773__a\:'), 'doi:'),
         (re.compile(r'773\s*\:'), 'publication:'),
         
         (re.compile(r'037*\:'), 'reportnumber:'),
         (re.compile(r'245_*\s*\:'), 'title:'),
         
         (re.compile(r'035__z\:'), 'other_id:'),
         #(re.compile(r':\s*\*'), ':'), #we don't allow asterisk at the start
                     ]
                    
    )
    inspire_fields = {
        'eprint':'reportnumber',
        'bb':'reportnumber',
        'bbn':'reportnumber',
        'bull':'reportnumber',
        'r':'reportnumber',
        'rn':'reportnumber',
        'cn':'collaboration',
        'a':'author',
        'au':'author',
        'name':'author',
        'ea':'exactauthor',
        'exp':'experiment',
        'expno':'experiment',
        'sd':'experiment',
        'se':'experiment',
        'j':'publication', #was journal
        'kw':'keyword', 
        'keywords':'keyword',
        'k':'keyword', 
        'au':'author', 
        'ti':'title',
        't':'title', 
        'irn':'970__a',
        'institution':'affiliation',
        'inst':'affiliation', 
        'affil':'affiliation',
        'aff':'affiliation', 
        'af':'affiliation',
        '902_*.*': 'affiliation',
        '695__a':'topic',
        'tp':'695__a',
        'dk':'695__a', #'topic':'695__a','tp':'695__a','dk':'695__a',
        'date':'year',
        'd':'year',
        'date-added':'datecreated',
        'da':'datecreated',
        'dadd':'datecreated',
        'date-updated':'datemodified',
        'dupd':'datemodified',
        'du':'datemodified'
    }
    for k, v in inspire_fields.items():
        _regexes.append(
            (re.compile('\W%s:' % k), '%s:' % v)
        )
    return _regexes

    
        
def convert_query(p, field=None):
    # if the pattern uses SPIRES search syntax, convert it to Invenio syntax
    if invenio_qconverter.is_applicable(p):
        p = invenio_qconverter.convert_query(p)
    p = p.strip()
    
    # do some basic transformations
    _transregex = get_query_regexes()
    
    field = field.strip()
    if field and p[0:len(field)] != field:
        p = '%s:%s' % (field, p)
        
    for regex, replacement in _transregex:
        p = regex.sub(replacement, p)
        
    return p
    
            
def format_author(s):
    s = s.replace('find author ', '').replace('find a ').replace('f k ')
    return 'author:(%s)' % s.strip()

def format_exactauthor(s):
    return 'author:"%s"' % format_author(s)

def format_fulltext(s):
    return 'text:%s' % s.strip()

def format_journal(s):
    s = s.replace('find journal ').replace('find f ').replace('f j ')
    return 'publication:%s' % s

def format_title(s):
    s = s.replace('find title ').replace('find t ').replace('f t ')
    return 'title:%s' % s

def format_keyword(s):
    s = s.replace('find keyword ').replace('find k ').replace('f k ')
    return 'title:%s' % s

def format_year(s):
    if s.isalnum():
        return ''
    return 'date:(%s)' % s.strip()


if __name__ == '__main__':
    if len(sys.argv) == 1 or not os.path.exists(sys.argv[1]):
        exit('Usage: extract_queries.py <searchlog>')
    run(sys.argv[1])
