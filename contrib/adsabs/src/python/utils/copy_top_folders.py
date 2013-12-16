import sys
import os

COPY_CMD = 'cp -fR %s %s'
REMOVE_CMD = 'rm -fR %s'

def run(src_dir, tgt_dir, delete=False):
    files = os.listdir(src_dir)
    tgt = os.path.abspath(tgt_dir)
    for f in files:
        if f != '.' or f != '..':
            fullname = os.path.abspath(os.path.join(src_dir, f))
            
            if delete:
                existing = os.path.abspath(os.path.join(src_dir, f))
                if os.path.exists(existing):
                    print 'remove ' + existing
                    cmd = REMOVE_CMD % existing 
                    os.system(cmd)
            
            cmd = COPY_CMD % (fullname, tgt)
            os.system(cmd)
            
            print f
            
if __name__ == '__main__':
    if len(sys.argv) < 1:
        exit('usage: program <src_dir> <tgt_dir> <delete_tgt_before>')
    if len(sys.argv) == 3:
        sys.argv.append(False)
    run(*sys.argv[1:])