
"""

Poorman's unittest for running the montysolrupdate
utility

We are 'mimicking' unittests, but we do some very 
basic things only - like, run test, check for results,
clean up

"""

import sys
import optparse
import unittest
import os
from unittest import TestCase
import shutil
import glob
import time
import re
import copy
import random

import montysolrupdate
import subprocess
TESTDIR = '/tmp/montysolrupdate-test'
montysolrupdate.INSTDIR = TESTDIR

os.environ['ANT_HOME'] = '/usr/share/ant'
os.environ['JAVA_HOME'] = '/usr/lib/jvm/java-7-openjdk-amd64/'



class TotalNukeDuke(TestCase):
    def setUp(self):
        if os.path.exists('perpetuum'):
            subprocess.call('rm -fR %s/perpetuum' % TESTDIR, shell=True, stdout=subprocess.PIPE)
        
    def tearDown(self):
        subprocess.call('rm -fR %s/perpetuum' % TESTDIR, shell=True, stdout=subprocess.PIPE)
        

class nuke_00_starting_from_scratch(TotalNukeDuke):
    def test_00_everything_is_built(self):
        montysolrupdate.main(['foo', '-a'])
        paths_exist(['python', 
                     'python/lib/python*/site-packages/invenio',
                     'python/lib/python*/site-packages/JCC*',
                     'python/lib/python*/site-packages/MySQLdb',
                     'python/lib/python*/site-packages/numpy',
                     'python/lib/python*/site-packages/lucene*',
                     'python/lib/python*/site-packages/simplejson',
                     'python/lib/python*/site-packages/sqlalchemy',
                     'python/lib/python*/site-packages/lxml',
                     ])
        
    def test_01_fresh_installation(self):
        
        try:
            montysolrupdate.req("http://localhost:7000/solr/admin/cores")
        except:
            pass
        else:
            self.fail("There is something running on port 7000")
            
        try:
            paths_exist(['test-7000',])
        except:
            pass
        else:
            self.fail("There is something at test-7000")
        
        try:
            paths_exist(['montysolr', 'montysolr/build/contrib/examples/adsabs'])
        except:
            pass
        else:
            self.fail("There is already 'adsabs' deploy target")
            
        montysolrupdate.main(['foo', '-c', '-u', 'test-7000'])
        
        paths_exist(['test-7000',])
        rsp = montysolrupdate.req("http://localhost:7000/solr/admin/cores")
        self.assertIn('status', rsp, "The instance was not started properly")
        kill_solr('test-7000')
        

class NormalTest(TestCase):
    def setUp(self):
        self.to_kill = []
        
    def tearDown(self):
        
        for tk in self.to_kill: 
            kill_solr(tk)
        cleanup("perpetuum/test-*")
    
    def isRunning(self, port):
        rsp = montysolrupdate.req("http://localhost:%s/solr/admin/cores" % port)
        self.assertIn('status', rsp, "The instance was not started properly")
    
    def isNotRunning(self, port):
        try:
            rsp = montysolrupdate.req("http://localhost:%s/solr/admin/cores" % port)
        except:
            return True
        self.fail("The instance %s is runnning and should be dead!" % port)
        
# We are assuming the montysolr is already built locally
class test_01_various_tag_changes(NormalTest):
    
    def setUp(self):
        NormalTest.setUp(self)
        self.old_get = montysolrupdate.get_latest_git_release_tag
        
        self.tagInterceptors = {}
        self.tagRestorers = {}
        def mocker(path):
            tag = self.old_get(path)
            p = os.path.abspath(path)
            for k,v in self.tagInterceptors.items():
                if k in p:
                    if p in self.tagRestorers:
                        return self.tagRestorers[p][1]
                    else:
                        new_tag = copy.copy(tag)
                        v(new_tag)
                        self.tagRestorers[p] = (tag, new_tag)
                        return new_tag
            return tag
        
        
        montysolrupdate.get_latest_git_release_tag = mocker
        
        self.old_run_cmd = montysolrupdate.run_cmd
        self.interceptor = {}
        self.cmd_collector = []
        def run_cmd(args, silent=False, strict=True):
            cmd = ' '.join(map(str, args))
            self.cmd_collector.append(cmd)
            if cmd in self.interceptor:
                return self.interceptor[cmd](args, silent, strict)
            else:
                return self.old_run_cmd(args, silent, strict)
        montysolrupdate.run_cmd = run_cmd
        
    def registerTestInstance(self, name):
        self.to_kill.append(name)
        
    def intercept(self, args, func):
        self.interceptor[args] = func
        
    def interceptTag(self, path, func):
        self.tagInterceptors[path] = func
        
    def checkCommandSequence(self, *commands):
        for desired, present, i in zip(commands, self.cmd_collector, range(len(commands))):
            if '*' in desired:
                if not re.compile(re.escape(desired).replace('\\*', '.*')).match(present):
                    raise self.failureException("sequence %i\nwant=%s\n got=%s\nfull=\n%s" %  
                                                (i, desired, present, "\n".join(self.cmd_collector)))
            else:
                if not desired == present:
                    raise self.failureException("sequence %i\nwant=%s\n got=%s\nfull=\n%s" %  
                                                (i, desired, present, "\n".join(self.cmd_collector)))
                
        
    def tearDown(self):
        NormalTest.tearDown(self)
        montysolrupdate.get_latest_git_release_tag = self.old_get
        montysolrupdate.run_cmd = self.old_run_cmd
        for path, (orig_tag, new_tag) in self.tagRestorers.items():
            if os.path.exists(path):
                if os.path.isfile(path):
                    open(path, 'w').write(str(orig_tag))
                elif os.path.isdir(path):
                    open(path + "/RELEASE", 'w').write(str(orig_tag))
    
    def xtest_major_upgrade(self):
        """
        Simulate the tag version was bumped from 40.x.x.x to 41.x.x.x
        """
        
        self.registerTestInstance('test-7000')
        self.registerTestInstance('next-release_test-7010')
        
        # instance is stopped, this should start it
        montysolrupdate.main(['foo', '-c', '-u', '-t', '10', 'test-7000'])
        self.isRunning(7000)
        
        self.testCommandSequence( 
                          'git fetch', 
                          'git reset --hard origin/master', 
                          'git checkout master', 
                          'cp -r montysolr/build/contrib/examples/adsabs test-7000_*', 
                          'ln -s test-7000_* test-7000', 
                          'mkdir test-7000_*_data', 
                          'ln -s */perpetuum/test-7000_*_data */perpetuum/test-7000/solr/data', 
                          'ln -s test-7000_*_data test-7000_data', 
                          'chmod u+x automatic-run.sh', 
                          'bash -e ./automatic-run.sh &'
                         )
        
        # now simulate update
        def tag_up(tag):
            if random.choice([0,1]) == 1:
                tag.major = tag.major + 1
            else:
                tag.solr_ver = tag.solr_ver + 1
                
        self.interceptTag('perpetuum/montysolr', tag_up)
        
        # but do not bother to re-compile
        self.intercept('./build-example.sh', lambda x,y,z: True)
        self.intercept('./build-montysolr.sh nuke', lambda x,y,z: True)
        
        
        self.cmd_collector = []
        montysolrupdate.main(['foo', '-c', '-u', '-t', '10', 'test-7000'])
        self.isRunning(7000)
        self.isRunning(7010)
        
        self.assertTrue(os.path.exists("next-release_test-7000"), "New major upgrade did not create next-release")
        open("next-release_test-7000/foo", 'w').write('foo')
        
        self.checkCommandSequence( 
                  'git fetch', 
                  'git reset --hard origin/master', 
                  'git checkout master', 
                  'chmod u+x build-montysolr.sh', 
                  'rm RELEASE', 
                  'git checkout -f -b refs/tags/*', 
                  './build-montysolr.sh nuke',   # <-- build everything from scratch
                  'chmod u+x ./build-example.sh', 
                  './build-example.sh', 
                  'cp -r montysolr/build/contrib/examples/adsabs test-7000_*', 
                  'mkdir test-7000_*_data', 
                  'ln -s test-7000_* next-release_test-7000', 
                  'ln -s test-7000_*_data next-release_test-7000_data', 
                  'rm -fr test-7000_*/solr/data', 
                  'ln -s */perpetuum/test-7000_*_data */perpetuum/next-release_test-7000/solr/data', 
                  'chmod u+x automatic-run.sh', 
                  'bash -e ./automatic-run.sh &'
                 )

        
        # the next invocation of the same command should replace the existing instance
        # with the next-release, TODO: check index is rebuilt
        self.cmd_collector = []
        montysolrupdate.main(['foo', '-c', '-u', '-t', '10', 'test-7000'])
        
        self.assertTrue(not os.path.exists("next-release_test-7000"), "this invocation should have replace existing with the next-release")
        
        
        self.checkCommandSequence(
            'git fetch', 
            'git reset --hard origin/master', 
            'git checkout master', 
            'kill *', # it stops next-release 
            'kill *', # stops test-7000
            'rm test-7000', # removes symlinks
            'rm test-7000_data', 
            'ln -s /tmp/montysolrupdate-test/perpetuum/test-7000_* test-7000', 
            'ln -s /tmp/montysolrupdate-test/perpetuum/test-7000_*_data test-7000_data', 
            'rm next-release_test-7000', # removes next-release 
            'rm next-release_test-7000_data', 
            'chmod u+x automatic-run.sh', 
            'bash -e ./automatic-run.sh &' # starts the service again
            )
        
        self.isRunning(7000)
        self.isNotRunning(7010)
        self.assertTrue(os.path.exists("test-7000/foo"), "Something was improperly copied during upgrade")


    def test_minor_upgrade(self):
        """
        Simulate the tag version was bumped from 40.x.1.x to 40.x.2.x
        """
        
        self.registerTestInstance('test-7000')
        
        # instance is stopped, this should start it
        montysolrupdate.main(['foo', '-c', '-u', '-t', '10', 'test-7000'])
        old_tag = montysolrupdate.get_release_tag("test-7000/RELEASE")
        new_tag = copy.copy(old_tag)
        new_tag.minor = new_tag.minor + 1
        
        self.assertTrue(os.path.exists("test-7000_%s" % str(old_tag)), "booork")
        self.assertTrue(os.path.exists("test-7000_%s_data" % str(old_tag)), "booork")
        self.assertTrue(not os.path.exists("test-7000_%s" % str(new_tag)), "booork")
        self.assertTrue(not os.path.exists("test-7000_%s_data" % str(new_tag)), "booork")
        self.assertTrue(os.path.abspath(os.path.realpath("test-7000_data")) == os.path.abspath("test-7000_%s_data" % old_tag))
        
        self.isRunning(7000)
        open("test-7000_data/foo", 'w').write('index')
        
        self.checkCommandSequence( 
                          'git fetch', 
                          'git reset --hard origin/master', 
                          'git checkout master', 
                          'cp -r montysolr/build/contrib/examples/adsabs test-7000_*', 
                          'ln -s test-7000_* test-7000', 
                          'mkdir test-7000_*_data', 
                          'ln -s */perpetuum/test-7000_*_data */perpetuum/test-7000/solr/data', 
                          'ln -s test-7000_*_data test-7000_data', 
                          'chmod u+x automatic-run.sh', 
                          'bash -e ./automatic-run.sh &'
                         )
        
        
        # now simulate update
        def tag_up(tag):
            tag.minor = tag.minor + 1
        self.interceptTag('perpetuum/montysolr', tag_up)
        
        # but do not bother to re-compile
        self.intercept('./build-example.sh', lambda x,y,z: True)
        self.intercept('./build-montysolr.sh minor', lambda x,y,z: True)
        
        
        self.cmd_collector = []
        montysolrupdate.main(['foo', '-c', '-u', '-t', '10', 'test-7000'])
        self.isRunning(7000)
        self.isNotRunning(7010)
        
        
        self.assertTrue(os.path.exists("test-7000_%s" % str(old_tag)), "booork")
        self.assertTrue(os.path.exists("test-7000_%s_data" % str(old_tag)), "booork")
        self.assertTrue(os.path.exists("test-7000_%s" % str(new_tag)), 
                        "missing test-7000_%s" % str(new_tag))
        self.assertTrue(os.path.exists("test-7000_%s_data" % str(new_tag)), 
                        "test-7000_%s_data" % str(new_tag))
        self.assertTrue(os.path.exists("test-7000_data/foo"), 
                        "Something was improperly copied during upgrade")
        self.assertTrue(os.path.exists("test-7000_%s_data/foo" % str(old_tag)), 
                        "Something was improperly copied during upgrade")
        self.assertTrue(os.path.exists("test-7000_%s_data/foo" % str(new_tag)), 
                        "Something was improperly copied during upgrade")
        
        open("test-7000_data/bar", 'w').write('index')
        
        self.assertTrue(os.path.abspath(os.path.realpath("test-7000_data")) == os.path.abspath("test-7000_%s_data" % new_tag))
        self.assertTrue(os.path.exists("test-7000_%s_data/bar" % new_tag))
        self.assertTrue(not os.path.exists("test-7000_%s_data/bar" % old_tag))
        self.assertTrue(os.path.exists("test-7000_data/foo"), "Something was improperly copied during upgrade")
        
        self.checkCommandSequence( 
                  'git fetch', 
                  'git reset --hard origin/master', 
                  'git checkout master', 
                  'chmod u+x build-montysolr.sh', 
                  'rm RELEASE', 
                  'git checkout -f -b refs/tags/*', 
                  './build-montysolr.sh minor', # <- recompilation 
                  'chmod u+x ./build-example.sh', 
                  './build-example.sh',  # <- assemble example
                  'cp -r montysolr/build/contrib/examples/adsabs test-7000_*', 
                  'rm -fr test-7000_%s/solr/data' % str(new_tag), 
                  'mkdir test-7000_%s_data' % str(new_tag), 
                  'cp -fR test-7000_data/* test-7000_%s_data' % str(new_tag), 
                  'kill *', 
                  'rm test-7000', 
                  'rm test-7000_data', 
                  'ln -s test-7000_%s test-7000' % str(new_tag), 
                  'ln -s test-7000_%s_data test-7000_data' % str(new_tag), 
                  'ln -s */perpetuum/test-7000_data test-7000_%s/solr/data' % str(new_tag), 
                  'chmod u+x automatic-run.sh', 
                  'bash -e ./automatic-run.sh &'
                 )

        self.isRunning(7000)
        



def cleanup(path):
    for p in glob.glob(TESTDIR + "/" + path):
        subprocess.call("rm -fR %s" % p, shell=True, stdout=subprocess.PIPE)
        
    
    
def check_pid_is_running(pid):
    if os.path.exists('/proc/%s' % pid):
        return True
    return False 

def get_pid(pidpath):
    if os.path.exists(pidpath):
        with open(pidpath, 'r') as pidfile:
            r_pid = pidfile.read().strip()
        try:
            return int(r_pid)
        except ValueError:
            return -1
    return -1

def kill_solr(path):
    path = path + '/montysolr.pid'
    pid = get_pid(path)
    if check_pid_is_running(pid):
        os.system('kill %s' % pid)
    time.sleep(0.5)
    if check_pid_is_running(pid):
        os.system('kill %s' % pid)
    time.sleep(0.5)
    if check_pid_is_running(pid):
        os.system('kill -9 %s' % pid)


def run_tests(options, tests=[]):
    if tests == None or len(tests) == 0:
        tests = get_tests()
        
        if not options.nuke:
            tests = filter(lambda x: 'nuke_' not in x, tests)
    
    this_module = sys.modules['__main__']
    for test in tests:
        test_object = getattr(this_module, test) #XXX: i do not checking
        test_suite = unittest.defaultTestLoader.loadTestsFromTestCase(test_object)
        runner = unittest.TextTestRunner(verbosity=options.verbosity)
        runner.run(test_suite)


def paths_exist(paths):
    for p in paths:
        if len(glob.glob(p)) <= 0:
            raise Exception(p)        

def get_tests():
    tests = filter(lambda x: 'test_' in x or 'nuke_' in x, dir(sys.modules['__main__']))
    tests = sorted(tests)
    return tests

def print_tests():
    print '\n'.join(get_tests())

def get_arg_parser():
    usage = '%prog [options] test1 test2....'
    p = optparse.OptionParser(usage=usage)
    p.add_option('-s', '--start_from',
                 default=1, action='store',
                 help='What test to start from',
                 type='int')
    p.add_option('-p', '--print_tests',
                 action='store_true',
                 help='Print the list of tests')
    
    p.add_option('-n', '--nuke',
                 action='store_true',
                 help='Run also the nuke tests')
    
    p.add_option('-v', '--verbosity',
                 default=3, action='store',
                 help='Verbosity', type='int')
    
    return p


def main(argv):
    
    old_cwd = os.getcwd()
    try:
        if not os.path.exists("%s/perpetuum" % TESTDIR):
            os.makedirs("%s/perpetuum" % TESTDIR)
            
        os.chdir(TESTDIR + "/perpetuum")
        parser = get_arg_parser()
        options, args = parser.parse_args(argv)
        
        if options.print_tests:
            print_tests()
        else:
            run_tests(options, args[1:])
    finally:
        os.chdir(old_cwd)


if __name__ == "__main__":
    main(sys.argv)