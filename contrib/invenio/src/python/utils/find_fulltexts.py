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

log.root.setLevel(5)


def run(idsfile, src_dir, tgt_dir, mode, extensions):
    """Traverse the source folder, search for files, when found,
    copies them to other place - into the properly ordered file-system
    """

    assert os.path.exists(src_dir) is True
    assert os.path.exists(tgt_dir) is True

    extensions = extensions.split(',')
    assert len(extensions) > 0

    print 'we will search for these extensions:' + '|'.join(extensions)

    stack = {}
    stack['found'] = open(os.path.join('/tmp', 'found.txt'), 'w')
    stack['not-found'] = open(os.path.join('/tmp', 'not-found.txt'), 'w')

    if mode not in ('copy', '#copy', 'count'):
        raise Exception('Unknown mode ' + mode)

    ids_map = {}
    fi = open(idsfile, 'r')
    for line in fi:
        line = line.strip()
        if not line:
            continue
        recid, arxiv_id = line.split('\t')
        name , topdir = split_arxivid(arxiv_id)
        if name and topdir:
            ids_map[name] = (recid, topdir, line)

    total_counter = [0]
    created_target_dirs = []

    def copy_func(arg, dirname, fnames):

        log.info('inside: %s' % dirname)
        to_copy = {}
        for f in fnames:
            fullpath = os.path.join(dirname, f)
            basename, ext = os.path.splitext(f)
            if basename and ext:
                _e = ext[1:].lower() #remove the leading dot
                if extensions and _e in extensions:
                    name, topdir = split_arxivid(basename, err=False)
                    found = False
                    if basename in ids_map:
                        name = basename
                        topdir = ids_map[basename][1]
                        found = True
                    elif name in ids_map:
                        name = name
                        topdir = ids_map[name][1]
                        found = True

                    if found:
                        if mode[0] == '#':
                            continue
                    else:
                        if mode[0] == '#':
                            # we are looking for files not in the list
                            topdir = os.path.split(dirname)[1]
                        else:
                            continue

                    target = os.path.join(tgt_dir, _e, topdir)

                    if _e == 'txt' or _e == 'utf8':
                        to_copy[name] = (os.path.join(dirname, f), target, f)
                    elif _e == 'pdf':
                        if name not in to_copy: # txt files have preference
                            to_copy[name] = (os.path.join(dirname, f), target, f)
        log.info('identified: %s candidates' % len(to_copy))

        if mode == 'copy' or mode == '#copy':
            for name, (source, target, filename) in to_copy.items():
                if target not in created_target_dirs and not os.path.isdir(target):
                    os.makedirs(target)
                    created_target_dirs.append(target)
                try:
                    target_file = os.path.join(target, filename)
                    if not os.path.exists(target_file):
                        shutil.copy(source, target)
                    del ids_map[name]
                    total_counter[0] += 1
                except Exception, msg:
                    del to_copy[name]
                    if os.path.isdir(source):
                        pass
                    else:
                        print msg
            if len(to_copy):
                print 'copied: %s (in total so far: %s)' % (len(to_copy), total_counter[0])
        elif mode == 'count':
            for name, (source, target, filename) in to_copy.items():
                record = ids_map.pop(name)
                stack['found'].write('%s\t%s\n' % (record[2], source))

        print '%s: %s' % (dirname, len(to_copy))

    try:
        os.path.walk(src_dir, copy_func, None)
    except KeyboardInterrupt:
        pass

    print 'found: %s, not-found: %s' % (total_counter[0], len(ids_map))
    fo = stack['not-found']
    for k, (recid, topdir, line) in ids_map.items():
        fo.write('%s\n' % line)
    fo.close()
    stack['found'].close()

    target_f = os.path.join(tgt_dir, 'found.txt')
    target_nf = os.path.join(tgt_dir, 'not-found.txt')

    shutil.copyfile(stack['found'].name, target_f)
    shutil.copyfile(stack['found'].name, target_nf)


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
            exit('Usage: find_fulltexts.py <arxiv_ids> <src_dir> <target_dir> <mode:count|copy|#copy> <extensions:txt,pdf')
    print sys.argv[1:]
    run(*sys.argv[1:])
