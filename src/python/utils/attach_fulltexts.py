'''
Created on Feb 3, 2011

Search the folders starting at point X, finds all the files of a certain
pattern and copy them to a different folders.

@author: rca
'''
import sys
import os
import shutil
import logging as log
import subprocess


from invenio import search_engine, bibdocfile, bibdocfilecli
log.root.setLevel(5)

MSG_AFTER = 100
BATCH_SIZE = 1000


def run(ids_file, src_dir, mode='append', suffix=".pdf"):
    """Traverse the source folder, search for files, when found,
    copies them to other place - into the properly ordered file-system
    """

    assert os.path.exists(src_dir) is True


    if mode not in ('append', 'replace'):
        raise Exception('Unknown mode ' + mode)

    ids_map, has_filepath = get_prescription(ids_file)
    total_counter = [0, 0, 0]

    if has_filepath:
        process_afs_folders(total_counter, ids_map, src_dir, append=(mode == 'append'))
    else:
        process_harvests(total_counter, ids_map, src_dir, append=(mode == 'append'))


    print 'uploaded: %s, skipped: %s, not-found: %s' % (total_counter[0], total_counter[1], total_counter[2])


def process_harvests(total_counter, ids_map, src_dir, suffix='.pdf', append=True):
    files = filter(lambda x: x not in ['.', '..'], os.listdir(src_dir))
    i = 0
    ffts = {}
    for name, (recid, arxiv_id) in ids_map.items():
        fullname = name + suffix
        if fullname in files:
            fullpath = os.path.join(src_dir, fullname)
            res = prepare_ffts(ffts, recid, arxiv_id, fullpath, append=append)
            if res == False:
                total_counter[1] += 1
            else:
                total_counter[0] += 1
        else:
            total_counter[2] += 1

        i += 1
        if i % MSG_AFTER == 0:
            print 'processed %s out of %s' % (i, len(ids_map))
        if len(ffts) % BATCH_SIZE == 0:
            upload_file(ffts)

    if len(ffts):
        upload_file(ffts)



def process_afs_folders(total_counter, ids_map, src_dir, suffix='.pdf', append=True):
    i = 0
    ffts = []
    for name, (recid, topdir, arxiv_id) in ids_map.items():
        fullpath = os.path.join(src_dir, topdir, name + suffix)
        if not os.path.exists(fullpath):
            log.error("The file %s not exists" % fullpath)
            total_counter[2] += 1
            continue

        res = prepare_ffts(ffts, recid, arxiv_id, fullpath, append=append)
        if res == False:
            total_counter[1] += 1
        else:
            total_counter[0] += 1

        i += 1
        if i % MSG_AFTER == 0:
            print 'processed %s out of %s' % (i, len(ids_map))

        if len(ffts) % BATCH_SIZE == 0:
            upload_file(ffts)

    if len(ffts):
        upload_file(ffts)



def get_prescription(ids_file):
    ids_map = {}
    fi = open(ids_file, 'r')
    # read the first line (find it contains filepath)
    elems = fi.readline().strip().split('\t')
    has_filepath = False
    if len(elems) > 2:
        has_filepth = True
    fi.seek(0)

    if has_filepath:
        for line in fi:
            line = line.strip()
            if not line:
                continue
            recid, arxiv_id, path = line.split('\t')
            name , topdir = split_arxivid(arxiv_id)
            if name and topdir:
                ids_map[name] = (recid, topdir, arxiv_id)
    else:
        for line in fi:
            line = line.strip()
            if not line:
                continue
            recid, arxiv_id = line.split()
            ids_map[arxiv_id.replace('/', '_')] = (recid, arxiv_id)
    return (ids_map, has_filepath)



def prepare_ffts(ffts, recid, docname, fullpath, doctype='arXiv', append=False, format='.pdf', options=['HIDDEN']):
    recid = int(recid)
    docname = 'arXiv:%s' % docname.replace('/', '_')
    bibdoc = bibdocfile.BibRecDocs(recid)

    res = subprocess.Popen(['file', fullpath], stdout=subprocess.PIPE).communicate()[0]
    if not ('PDF' in res or 'pdf' in res.lower()):
        return False

    # check it is an existing recod
    if len(bibdoc.display()) and (bibdoc.has_docname_p(docname) and append is not False):
        return False

    ffts[recid] = [{
            'docname' : docname,
            'format' : format,
            'url' : fullpath,
            'doctype': doctype,
            'options': options,
            }]


def upload_file(ffts, append=True):
    try:
        sys.argv.append('--yes-i-know')
        out = bibdocfilecli.bibupload_ffts(ffts, append=append, debug=False)
    finally:
        sys.argv.pop(-1)
        ffts.clear()


def split_arxivid(arxiv_id, err=True):
    name = topdir = None
    if arxiv_id.find('/') > -1:
        arx_parts = arxiv_id.split('/') #math-gt/060889
        name = ''.join(arx_parts)
        topdir = arx_parts[1][:4]
    elif arxiv_id.find('.') > -1:
        arx_parts = arxiv_id.split('.', 1) #0712.0712
        topdir = arx_parts[0]
        name = ''.join(arx_parts)
    else:
        if err:
            print 'error parsing:', arxiv_id
    return name, topdir



if __name__ == '__main__':
    if len(sys.argv) == 1 or not os.path.exists(sys.argv[1]):
        try:
            sys.argv[1] = int(sys.argv[1])
        except:
            exit('Usage: find_fulltexts.py <arxiv_ids> <src_dir> <mode:append|replace>')
    print sys.argv[1:]
    run(*sys.argv[1:])
