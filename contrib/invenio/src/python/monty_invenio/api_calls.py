

import os
import thread

from invenio import search_engine
from invenio import search_engine_summarizer
from invenio import dbquery
from invenio import bibrecord
from invenio.intbitset import intbitset

from invenio.bibrank_citation_searcher import get_citation_dict



from cStringIO import StringIO

def dispatch(func_name, *args, **kwargs):
    """Dispatches the call to the *local* worker
    It returns a tuple (ThreadID, result)
    """
    tid = thread.get_ident()
    out = globals()[func_name](*args, **kwargs)
    return [tid, out]

def get_recids_changes(last_recid, max_recs=10000):
    """
    Retrieves the sets of records that were added/updated/deleted
    
    The time is selected according to some know recid, ie. 
    we retrieve the modification time of one record and look
    at those that are older.
    
    added => bibrec.modification_date == bibrec.creation_date
    updated => bibrec.modification_date >= bibrec.creation_date
    deleted => bibrec.status == DELETED
    """
    
    search_op = '>'

    if last_recid == -1:
        l = list(dbquery.run_sql("SELECT id FROM bibrec ORDER BY creation_date ASC LIMIT 1"))
        search_op = '>='
    else:
        # let's make sure we have a valid recid (or get the close valid one)
        l = list(dbquery.run_sql("SELECT id FROM bibrec WHERE id >= %s LIMIT 1", (last_recid,)))
        if not len(l):
            return
        
    last_recid = l[0][0]

    # there is not api to get this (at least i haven't found it)
    mod_date = search_engine.get_modification_date(last_recid, fmt="%Y-%m-%d %H:%i:%S")
    if not mod_date:
        return
    modified_records = list(dbquery.run_sql("SELECT id,modification_date, creation_date FROM bibrec "
                    "WHERE modification_date " + search_op + "%s ORDER BY id ASC LIMIT %s", (mod_date, max_recs )))
    
    print "SELECT id,modification_date, creation_date FROM bibrec WHERE modification_date " + search_op + "%s ORDER BY id ASC LIMIT %s" % (mod_date, max_recs )
    print len(modified_records)
    
    if not len(modified_records):
        return
    
    added = []
    updated = []
    deleted = []
    
    
    changed = 0
    for recid, mod_date, create_date in modified_records:
        
        #rec = search_engine.get_record(recid)
        #status = bibrecord.record_get_field_value(rec, tag='980', code='c')
        status = search_engine.record_exists(recid)
        
        if status == -1:
            deleted.append(recid)
        elif mod_date == create_date:
            added.append(recid)
        else:
            updated.append(recid)
            
    return {'DELETED': deleted, 'UPDATED': updated, 'ADDED': added}

def citation_summary(recids, of, ln, p, f):
    out = StringIO()
    x = search_engine_summarizer.summarize_records(recids, of, ln, p, f, out)
    if x:
        output = x
    else:
        out.seek(0)
        output = out.read()
    return output

def search(q, max_len=25):
    offset = 0
    #hits = search_engine.search_pattern_parenthesised(None, q)
    hits = search_engine.perform_request_search(None, p=q)
    total_matches = len(hits)

    if max_len:
        return [offset, hits[:max_len], total_matches]
    else:
        return [offset, hits, total_matches]

def sort_and_format(hits, kwargs):

    kwargs = search_engine._cleanup_arguments(**kwargs)
    t1 = os.times()[4]
    req = StringIO()
    kwargs['req'] = req

    if 'hosted_colls_actual_or_potential_results_p' not in kwargs:
        kwargs['hosted_colls_actual_or_potential_results_p'] = True # this prevents display of the nearest-term box

    # search stage 4 and 5: intersection with collection universe and sorting/limiting
    output = search_engine._collect_sort_display(hits, kwargs=kwargs, **kwargs)
    if output is not None:
        req.seek(0)
        return req.read() + output

    t2 = os.times()[4]
    cpu_time = t2 - t1
    kwargs['cpu_time'] = cpu_time

    recids = search_engine._rank_results(kwargs=kwargs, **kwargs)

    if 'of' in kwargs and kwargs['of'].startswith('hc'):
        output = citation_summary(intbitset(recids), kwargs['of'], kwargs['ln'], kwargs['p'], kwargs['f'])
        if output:
            return output

    return recids


def get_citation_dict(message):
    dictname = sj.String.cast_(message.getParam('dictname'))
    cd = bcs.get_citation_dict(dictname)
    if cd:
        hm = sj.HashMap().of_(sj.String, sj.JArray_int)

        for k,v in cd.items():
            j_array = sj.JArray_int(v)
            hm.put(k, j_array)

        message.put('result', hm)


if __name__ == '__main__':
    print dispatch("get_recids_changes", 85)
