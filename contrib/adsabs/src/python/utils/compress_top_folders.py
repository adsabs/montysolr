import sys
import os

COMPRESS_CMD = 'tar -czf "%s.tgz" "%s"'
REMOVE_CMD = 'rm -fR "%s"'

def run(src_dir, delete=False):
    old_dir = os.getcwd()
    os.chdir(src_dir)
    files = os.listdir(src_dir)
    for f in files:
        if os.path.isdir(f):
            #fullname = os.path.abspath(os.path.join(src_dir, f))
            fullname = f
            cmd = COMPRESS_CMD % (fullname, fullname)
            os.system(cmd)
            if delete:
                cmd = REMOVE_CMD % fullname
                os.system(cmd)
            print f
    os.chdir(old_dir)
            
if __name__ == '__main__':
    if len(sys.argv) < 1:
        exit('usage: program <src_dir> <delete>')
    if len(sys.argv) == 2:
        sys.argv.append(False)
    run(*sys.argv[1:])