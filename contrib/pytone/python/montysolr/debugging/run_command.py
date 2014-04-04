

import sys
import os
import runpy

def run_command(filepath):
    line = open(filepath, 'r').read()
    elements = line.split()
    startdir = os.getcwd()
    rootdir = os.path.abspath(os.path.join(os.path.dirname(__file__), '../../../..'))
    try:
        print 'executing jcc, cwd=%s' % rootdir
        os.chdir(rootdir)
        startup = []
        for x in elements[:2]:
            if x in ('python', '-m', 'jcc', 'jcc.__main__'):
                startup.append(x)
                elements.remove(x)
        
        mod = startup[startup.index('-m')+1]
        sys.argv[1:] = elements
        runpy.run_module(mod)
    finally:
        os.chdir(startdir)
    

if __name__ == "__main__":
    if len(sys.argv) <= 1 or not os.path.exists(sys.argv[1]):
        print '''
 A simple toold to run the compilation of the solr
 egg, the command is generated when you 
 1. edit build.properties
     python=echo 
 2. run "ant compile-solr-egg"
 3. copy paste the resulting text into some file
 4. run python run_command.py <path-to-the-file>        
        '''
        raise Exception("No command found, use python run_command.py <path>\nsys.argv=%s" % sys.argv)
        
    run_command(sys.argv[1])