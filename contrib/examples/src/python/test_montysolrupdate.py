
"""

Testing correct functionality of the production
deployment/installation utility

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

TESTDIR = 'TESTDIR' in os.environ and os.environ['TESTDIR'] or '/tmp/montysolrupdate-test'
montysolrupdate.INSTDIR = TESTDIR

if 'ANT_HOME' not in os.environ:
    os.environ['ANT_HOME'] = '/usr/share/ant'
if 'JAVA_HOME' not in os.environ:
    os.environ['JAVA_HOME'] = '/usr/lib/jvm/java-7-openjdk-amd64/'



        

class NormalTest(TestCase):
        
    def tearDown(self):
        
        for tk in self.to_kill: 
            kill_solr(tk)
        cleanup("perpetuum/test-*")
        cleanup("perpetuum/next-release_test-*")
        
        montysolrupdate.get_latest_git_release_tag = self.old_get
        montysolrupdate.run_cmd = self.old_run_cmd
        for path, (orig_tag, new_tag) in self.tagRestorers.items():
            if os.path.exists(path):
                if os.path.isfile(path):
                    open(path, 'w').write(str(orig_tag))
                elif os.path.isdir(path):
                    open(path + "/RELEASE", 'w').write(str(orig_tag))
                    example = path + "/build/contrib/examples/%s/RELEASE" % montysolrupdate.INSTNAME
                    if os.path.exists(example):
                        open(example, 'w').write(str(orig_tag))
                        
        
    
    def isRunning(self, port):
        for i in range(3):
            try:
                rsp = montysolrupdate.req("http://localhost:%s/solr/admin/cores" % port)
                self.assertIn('status', rsp, "The instance was not started properly")
                return True
            except IOError, e:
                time.sleep(0.5)
        self.fail("The instance %s was not started properly" % port)
                
    
    def isNotRunning(self, port):
        try:
            rsp = montysolrupdate.req("http://localhost:%s/solr/admin/cores" % port)
        except:
            return True
        self.fail("The instance %s is runnning and should be dead!" % port)

    def setUp(self):
        TestCase.setUp(self)
        
        if os.path.exists('update.pid'):
            montysolrupdate.remove_lock('update.pid')
            time.sleep(0.2)
        
        self.to_kill = []
        self.old_get = montysolrupdate.get_latest_git_release_tag
        
        self.tagInterceptors = {}
        self.tagRestorers = {}
        def mocker(path):
            tag = self.old_get(path)
            p = os.path.abspath(path)
            for k,v in self.tagInterceptors.items():
                if k in p:
                    if p in self.tagRestorers:
                        v(self.tagRestorers[p][1])
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
            for pattern in self.interceptor:
                if pattern.match(cmd):
                    return self.interceptor[pattern](args, silent, strict)
            else:
                return self.old_run_cmd(args, silent, strict)
            
        montysolrupdate.run_cmd = run_cmd
        
    def registerTestInstance(self, name):
        self.to_kill.append(name)
        
    def intercept(self, args, func):
        self.interceptor[re.compile(args)] = func
        
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
                

class TotalNukeDuke(NormalTest):
    def setUp(self):
        NormalTest.setUp(self)
        if os.path.exists('perpetuum'):
            subprocess.call('rm -fR %s/perpetuum' % TESTDIR, shell=True, stdout=subprocess.PIPE)
        
    def tearDown(self):
        #subprocess.call('rm -fR %s/perpetuum' % TESTDIR, shell=True, stdout=subprocess.PIPE)
        for tk in self.to_kill: 
            kill_solr(tk)
        cleanup("perpetuum/test-*")
        cleanup("perpetuum/next-release_test-*")


class nuke_00_starting_from_scratch(TotalNukeDuke):
        
    def test_01_fresh_installation(self):
        
        self.registerTestInstance("test-7000")
        
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
            
        montysolrupdate.main(['foo', '-a', '-c', '-u', 'test-7000'])
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
        
        paths_exist(['test-7000',])
        self.isRunning("7000")
        
        

class test_01_major_upgrade(NormalTest):
    """
    This test assumes montysolr is already built locally and we can simply
    copy files around (without rebuilding them)
    """

    
    def test_major_upgrade(self):
        """
        Simulate the tag version was bumped from 40.x.x.x to 41.x.x.x
        or x.1.x.x to x.2.x.x
        """
        
        self.registerTestInstance('test-7000')
        self.registerTestInstance('next-release_test-7010')
        
        # instance is stopped, this should start it
        montysolrupdate.main(['foo', '-c', '-u', '-t', '10', 'test-7000'])
        self.isRunning(7000)
        old_tag = montysolrupdate.get_release_tag("test-7000/RELEASE")
        new_tag = copy.copy(old_tag)
        
        
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
            tag.solr_ver = new_tag.solr_ver
            tag.major = new_tag.major
            tag.minor = new_tag.minor
            tag.patch = new_tag.patch 
        self.interceptTag('perpetuum/montysolr', tag_up)
        
        if random.choice([0,1]) == 1:
            new_tag.major = new_tag.major + 1
        else:
            new_tag.solr_ver = new_tag.solr_ver + 1
                
        
        # but do not bother to re-compile
        self.intercept('./build-example.sh .*', lambda x,y,z: True)
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
                  'git checkout -f -b refs/tags/*', 
                  './build-montysolr.sh nuke',   # <-- build everything from scratch
                  'chmod u+x ./build-example.sh', 
                  './build-example.sh *', 
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
        
        
       
class test_02_minor_upgrade(NormalTest):
    """
    This test assumes montysolr is already built locally and we can simply
    copy files around (without rebuilding them)
    """
                

    def test_minor_upgrade(self):
        """
        Simulate the tag version was bumped from x.x.1.x to x.x.2.x
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
        self.intercept('./build-example.sh .*', lambda x,y,z: True)
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
                  'git checkout -f -b refs/tags/*', 
                  './build-montysolr.sh minor', # <- recompilation 
                  'chmod u+x ./build-example.sh', 
                  './build-example.sh *',  # <- assemble example
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
        

class test_03_patch_level_upgrade(NormalTest):
    """
    This test assumes montysolr is already built locally and we can simply
    copy files around (without rebuilding them)
    """


    def test_patch_upgrade(self):
        """
        Simulate the tag version was bumped from x.x.x.1 to x.x.x.2
        """
        
        self.registerTestInstance('test-7000')
        
        self.assertTrue(not os.path.exists("test-7000"), "booork")
        self.assertTrue(not os.path.exists("test-7000_data"), "booork")
        
        # instance is stopped, this should start it
        montysolrupdate.main(['foo', '-c', '-u', '-t', '10', 'test-7000'])
        old_tag = montysolrupdate.get_release_tag("test-7000/RELEASE")
        new_tag = copy.copy(old_tag)
        new_tag.patch = new_tag.patch + 1
        
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
            tag.patch = tag.patch + 1
        self.interceptTag('perpetuum/montysolr', tag_up)
        
        # but do not bother to re-compile
        self.intercept('./build-example.sh .*', lambda x,y,z: True)
        self.intercept('./build-montysolr.sh patch', lambda x,y,z: True)
        
        
        self.cmd_collector = []
        self.isRunning(7000)
        montysolrupdate.main(['foo', '-c', '-u', '-t', '10', 'test-7000'])
        self.isRunning(7000)
        self.isNotRunning(7010)
        
        
        
        self.assertTrue(os.path.exists("test-7000_%s" % str(old_tag)), 
                        "The old instance must exist")
        self.assertTrue(os.path.abspath(os.path.realpath("test-7000_data")) ==
                        os.path.abspath("test-7000_%s_data" % str(old_tag)), 
                        "The new instance data should still point to old instance data folder")
        self.assertTrue(os.path.abspath(os.path.realpath("test-7000")) ==
                        os.path.abspath("test-7000_%s" % str(new_tag)), 
                        "The new instance should point to new instance folder")
        self.assertTrue(os.path.abspath(os.path.realpath("test-7000_data/solr/data")) ==
                        os.path.abspath("test-7000_%s_data/solr/data" % str(old_tag)), 
                        "The new instance solr/data should point to old instance data folder")
        
        
        self.checkCommandSequence( 
                  'git fetch', 
                  'git reset --hard origin/master', 
                  'git checkout master', 
                  'chmod u+x build-montysolr.sh', 
                  'git checkout -f -b refs/tags/*', 
                  './build-montysolr.sh patch', # <- recompilation 
                  'chmod u+x ./build-example.sh', 
                  './build-example.sh *',  # <- assemble example
                  'kill *',
                  'cp -r montysolr/build/contrib/examples/adsabs test-7000_%s' % str(new_tag), 
                  'rm -fr test-7000_%s/solr/data' % str(new_tag), 
                  'ln -s */perpetuum/test-7000_data test-7000_%s/solr/data' % str(new_tag), 
                  'rm test-7000', 
                  'ln -s test-7000_%s test-7000' % str(new_tag), 
                  'chmod u+x automatic-run.sh', 
                  'bash -e ./automatic-run.sh &'
                 )
        
        
        # run it again
        previous_tag = str(new_tag)
        new_tag.patch = new_tag.patch + 1

        
        self.cmd_collector = []
        self.isRunning(7000)
        montysolrupdate.main(['foo', '-c', '-u', '-t', '10', 'test-7000'])
        self.isRunning(7000)
        self.isNotRunning(7010)
        
        
        
        self.assertTrue(os.path.exists("test-7000_%s" % str(previous_tag)), 
                        "The old instance must exist")
        self.assertTrue(os.path.abspath(os.path.realpath("test-7000_data")) ==
                        os.path.abspath("test-7000_%s_data" % str(old_tag)), 
                        "The new instance data should still point to old instance data folder")
        self.assertTrue(os.path.abspath(os.path.realpath("test-7000")) ==
                        os.path.abspath("test-7000_%s" % str(new_tag)), 
                        "The new instance should point to new instance folder")
        self.assertTrue(os.path.abspath(os.path.realpath("test-7000_data/solr/data")) ==
                        os.path.abspath("test-7000_%s_data/solr/data" % str(old_tag)), 
                        "The new instance solr/data should point to old instance data folder")
        
        
        self.checkCommandSequence( 
                  'git fetch', 
                  'git reset --hard origin/master', 
                  'git checkout master', 
                  'chmod u+x build-montysolr.sh', 
                  'git checkout -f -b refs/tags/*', 
                  './build-montysolr.sh patch', # <- recompilation 
                  'chmod u+x ./build-example.sh', 
                  './build-example.sh *',  # <- assemble example
                  'kill *',
                  'cp -r montysolr/build/contrib/examples/adsabs test-7000_%s' % str(new_tag), 
                  'rm -fr test-7000_%s/solr/data' % str(new_tag), 
                  'ln -s */perpetuum/test-7000_data test-7000_%s/solr/data' % str(new_tag), 
                  'rm test-7000', 
                  'ln -s test-7000_%s test-7000' % str(new_tag), 
                  'chmod u+x automatic-run.sh', 
                  'bash -e ./automatic-run.sh &'
                 )
        
        # and now check that no-change does not trigger recompilation
        self.cmd_collector = []
        self.interceptTag('perpetuum/montysolr', lambda t: t)
        self.intercept('./build-example.sh .*', lambda x,y,z: 1/0)
        self.intercept('./build-montysolr.sh patch', lambda x,y,z: 1/0)
        montysolrupdate.main(['foo', '-c', '-u', '-t', '10', 'test-7000'])
        self.checkCommandSequence( 
                  'git fetch', 
                  'git reset --hard origin/master', 
                  'git checkout master', 
                  )
        

class test_04_dual_mode_upgrade(NormalTest):
    """
    This test assumes montysolr is already built locally and we can simply
    copy files around (without rebuilding them)
    """


    def test_reader_writer_upgrade(self):
        """
        Simulate having one read-write masters and two read-only (searchers) 
        """
        
        self.registerTestInstance('test-7000')
        self.registerTestInstance('test-7001')
        self.registerTestInstance('test-7002')
        self.registerTestInstance('next-release_test-7000')
        self.registerTestInstance('next-release_test-7001')
        self.registerTestInstance('next-release_test-7002')
        
        self.assertTrue(not os.path.exists("test-7000"), "booork")
        self.assertTrue(not os.path.exists("test-7000_data"), "booork")
        self.assertTrue(not os.path.exists("test-7001"), "booork")
        self.assertTrue(not os.path.exists("test-7001_data"), "booork")
        self.assertTrue(not os.path.exists("test-7002"), "booork")
        self.assertTrue(not os.path.exists("test-7002_data"), "booork")
        
        
        # instance is stopped, this should start it
        montysolrupdate.main(['foo', '-c', '-a', '-u', '-t', '10', 'test-7000#w', 'test-7001#r', 'test-7002#r'])
        
        old_tag = montysolrupdate.get_release_tag("test-7000/RELEASE")
        new_tag = copy.copy(old_tag)

        
        self.assertTrue(os.path.exists("test-7000_%s" % str(old_tag)), 
                        "The writer instance must exist")
        self.assertTrue(os.path.exists("test-7001_%s" % str(old_tag)), 
                        "The reader#1 instance must exist")
        self.assertTrue(os.path.exists("test-7002_%s" % str(old_tag)), 
                        "The reader#2 instance must exist")
        
        self.assertTrue(os.path.exists("test-7000_%s_data" % str(old_tag)), 
                        "The writer instance data must exist")
        self.assertTrue(os.path.abspath(os.path.realpath("test-7001_data")) ==
                        os.path.abspath("test-7000_%s_data" % str(old_tag)), 
                        "The reader#1 instance data should point to writer instance data folder")
        self.assertTrue(os.path.abspath(os.path.realpath("test-7002_data")) ==
                        os.path.abspath("test-7000_%s_data" % str(old_tag)), 
                        "The reader#2 instance data should point to writer instance data folder")
        
        self.isRunning(7000)
        self.isRunning(7001)
        self.isRunning(7002)
        
        open("test-7000_data/foo", 'w').write('index')
        
        
        # now simulate update
        def tag_up(tag):
            tag.solr_ver = new_tag.solr_ver
            tag.major = new_tag.major
            tag.minor = new_tag.minor
            tag.patch = new_tag.patch 
        self.interceptTag('perpetuum/montysolr', tag_up)
        
        # but do not bother to re-compile
        self.intercept('./build-example.sh', lambda x,y,z: True)
        self.intercept('./build-montysolr.sh patch', lambda x,y,z: True)
        self.intercept('./build-montysolr.sh minor', lambda x,y,z: True)
        self.intercept('./build-montysolr.sh nuke', lambda x,y,z: True)
        
        
        # OK, let's go!
        new_tag.patch = new_tag.patch + 1
        
        montysolrupdate.main(['foo', '-c', '-u', '-t', '10', 'test-7000#w', 'test-7001#r', 'test-7002#r'])
        
        self.isRunning(7000)
        self.isRunning(7001)
        self.isRunning(7002)
        
        
        self.assertTrue(os.path.exists("test-7000_%s" % str(new_tag)), 
                        "The writer instance must exist")
        self.assertTrue(os.path.exists("test-7001_%s" % str(new_tag)), 
                        "The reader#1 instance must exist")
        self.assertTrue(os.path.exists("test-7002_%s" % str(new_tag)), 
                        "The reader#2 instance must exist")
        
        self.assertTrue(os.path.exists("test-7000_%s_data" % str(old_tag)), 
                        "The writer instance data must exist")
        # and still point to the same folder
        self.assertTrue(os.path.abspath(os.path.realpath("test-7000_data")) ==
                        os.path.abspath("test-7000_%s_data" % str(old_tag)), 
                        "The writer instance data should point to the old writer instance data folder")
        self.assertTrue(os.path.abspath(os.path.realpath("test-7001_data")) ==
                        os.path.abspath("test-7000_%s_data" % str(old_tag)), 
                        "The reader#1 instance data should point to writer instance data folder")
        self.assertTrue(os.path.abspath(os.path.realpath("test-7002_data")) ==
                        os.path.abspath("test-7000_%s_data" % str(old_tag)), 
                        "The reader#2 instance data should point to writer instance data folder")
        
        
        # major upgrade
        new_tag.major = new_tag.major + 1
        # this will create a next-release instances that run alongside the old
        montysolrupdate.main(['foo', '-c', '-u', '-t', '10', 'test-7000#w', 'test-7001#r', 'test-7002#r'])
        
        self.isRunning(7000)
        self.isRunning(7001)
        self.isRunning(7002)
        self.isRunning(7010)
        self.isRunning(7011)
        self.isRunning(7012)
        
        
                                      
        self.assertTrue(os.path.abspath(os.path.realpath("next-release_test-7000")) ==
                        os.path.abspath("test-7000_%s" % str(new_tag)), 
                        "The writer instance should point to the old writer instance data folder")
        self.assertTrue(os.path.abspath(os.path.realpath("next-release_test-7001")) ==
                        os.path.abspath("test-7001_%s" % str(new_tag)), 
                        "The reader#1 instance should point to writer instance data folder")
        self.assertTrue(os.path.abspath(os.path.realpath("next-release_test-7002")) ==
                        os.path.abspath("test-7002_%s" % str(new_tag)), 
                        "The reader#2 instance should point to writer instance data folder")
        
        # data and still point to the same folder
        self.assertTrue(os.path.abspath(os.path.realpath("next-release_test-7000_data")) ==
                        os.path.abspath("test-7000_%s_data" % str(new_tag)), 
                        "The writer instance data should point to the old writer instance data folder")
        self.assertTrue(os.path.abspath(os.path.realpath("next-release_test-7001_data")) ==
                        os.path.abspath("test-7000_%s_data" % str(new_tag)), 
                        "The reader#1 instance data should point to writer instance data folder")
        self.assertTrue(os.path.abspath(os.path.realpath("next-release_test-7002_data")) ==
                        os.path.abspath("test-7000_%s_data" % str(new_tag)), 
                        "The reader#2 instance data should point to writer instance data folder")
        
        
        # let's confuse it and upgrade while the next branch is still building
        # it should refuse to prepare a new instance with the latest patch alongside the next-
        new_tag.major = new_tag.major - 1
        new_tag.patch = new_tag.patch + 1
        old_idle = montysolrupdate.is_invenio_doctor_idle
        try:
            montysolrupdate.is_invenio_doctor_idle = lambda x: False
            montysolrupdate.main(['foo', '-c', '-u', '-t', '10', 'test-7000#w', 'test-7001#r', 'test-7002#r'])
        except:
            montysolrupdate.remove_lock('update.pid')
            time.sleep(0.2)
        finally:
            montysolrupdate.is_invenio_doctor_idle = old_idle 
        
        self.isRunning(7000)
        self.isRunning(7001)
        self.isRunning(7002)
        self.isRunning(7010)
        self.isRunning(7011)
        self.isRunning(7012)
        
        
        
        # and the next invocation should replace the old release with the next-release
        new_tag.major = new_tag.major + 1
        new_tag.patch = new_tag.patch - 1
        montysolrupdate.main(['foo', '-c', '-u', '-t', '10', 'test-7000#w', 'test-7001#r', 'test-7002#r'])
        
        self.isRunning(7000)
        self.isRunning(7001)
        self.isRunning(7002)
        self.isNotRunning(7010)
        self.isNotRunning(7011)
        self.isNotRunning(7012)
        
        
        
        self.assertFalse(os.path.exists("next-release_test-7000"), "It must be removed")
        self.assertFalse(os.path.exists("next-release_test-7000_data"), "It must be removed")
        self.assertFalse(os.path.exists("next-release_test-7001"), "It must be removed")
        self.assertFalse(os.path.exists("next-release_test-7001_data"), "It must be removed")
        self.assertFalse(os.path.exists("next-release_test-7002"), "It must be removed")
        self.assertFalse(os.path.exists("next-release_test-7002_data"), "It must be removed")
                                              
        self.assertTrue(os.path.abspath(os.path.realpath("test-7000")) ==
                        os.path.abspath("test-7000_%s" % str(new_tag)), 
                        "The writer instance should point to the old writer instance data folder")
        self.assertTrue(os.path.abspath(os.path.realpath("test-7001")) ==
                        os.path.abspath("test-7001_%s" % str(new_tag)), 
                        "The reader#1 instance should point to writer instance data folder")
        self.assertTrue(os.path.abspath(os.path.realpath("test-7002")) ==
                        os.path.abspath("test-7002_%s" % str(new_tag)), 
                        "The reader#2 instance should point to writer instance data folder")
        
        # data and still point to the same folder
        self.assertTrue(os.path.abspath(os.path.realpath("test-7000_data")) ==
                        os.path.abspath("test-7000_%s_data" % str(new_tag)), 
                        "The writer instance data should point to the old writer instance data folder")
        self.assertTrue(os.path.abspath(os.path.realpath("test-7001_data")) ==
                        os.path.abspath(os.path.realpath("test-7000_data")), 
                        "The reader#1 instance data should point to writer instance data folder")
        self.assertTrue(os.path.abspath(os.path.realpath("test-7002_data")) ==
                        os.path.abspath(os.path.realpath("test-7000_data")), 
                        "The reader#2 instance data should point to writer instance data folder")
        
        

class test_05_exceptions(NormalTest):
    """
    This test assumes montysolr is already built locally and we can simply
    copy files around (without rebuilding them)
    """
        
    
    def test_exceptions(self):
        
        self.registerTestInstance('test-7000')
        self.registerTestInstance('test-7001')
        self.registerTestInstance('test-7002')
        
        try:
            montysolrupdate.main(['foo', '-c', '-u', '-t', '10', 'test-7000#r', 'test-7001#w', 'test#7002#w'])
        except:
            pass
        else:
            self.fail("We should not allow two writer instances")
            
        
        try:
            montysolrupdate.main(['foo', '-c', '-u', '-t', '10', 'test-7000#r', 'test#7001#r', 'test#7002#w'])
        except:
            pass
        else:
            self.fail("We should test wrong instance name")
            

class test_06_no_changes(NormalTest):
    """
    This test assumes montysolr is already built locally and we can simply
    copy files around (without rebuilding them)
    """
        
    
    def test_no_changes(self):
        
        self.registerTestInstance('test-7000')
        
        # first invocation should take time
        montysolrupdate.main(['foo', '-c', '-u', '-a', '-t', '10', 'test-7000'])
        
        # but next must be fast
        start = time.time()
        montysolrupdate.main(['foo', '-c', '-u', '-t', '10', 'test-7000'])
        self.assertTrue(time.time() - start < 3, "Invocation took too long")
                    

        start = time.time()
        montysolrupdate.main(['foo', '-c', '-u', '-a', '-t', '10', 'test-7000'])
        self.assertTrue(time.time() - start < 3, "Invocation took too long")
        


class test_07_checkout_branch(NormalTest):
    """
    This test assumes montysolr is already built locally and we can simply
    copy files around (without rebuilding them)
    """
        
    
    def test_master_branch(self):
        
        self.registerTestInstance('test-7000')
        self.registerTestInstance('test-7001')
        
        # first invocation should take time
        montysolrupdate.main(['foo', '-c', '-u', '-a', '-t', '10', '-b', 'master', 'test-7000#w', 'test-7000#r'])
        
        with montysolrupdate.changed_dir('montysolr'):
            '* master' in montysolrupdate.get_output(['git', 'branch'])
        
        montysolrupdate.main(['foo', '-c', '-u', '-a', '-t', '10', '-b', 'refs/tags/v40.1.0.5', 'test-7000#w', 'test-7000#r'])
        
        with montysolrupdate.changed_dir('montysolr'):
            '* refs/tags/v40.1.0.5' in montysolrupdate.get_output(['git', 'branch'])
            
        
            

class test_08_different_startups(NormalTest):
    """
    This test assumes montysolr is already built locally and we can simply
    copy files around (without rebuilding them)
    """
        
    
    def test_written_profiles(self):
        
        self.registerTestInstance('test-7000')
        self.registerTestInstance('test-7001')
        self.registerTestInstance('test-7002')
        
        # first invocation should take time
        montysolrupdate.main(['foo', '-c', '-u', '-a', '-t', '10', 'test-7000'])
        
        
        # check the built demo contains proper startup files
        
        self.assertTrue(os.path.exists('test-7000/reader.run.sh'), "Missing startup")
        self.assertTrue(os.path.exists('test-7000/writer.run.sh'), "Missing startup")
        self.assertTrue(os.path.exists('test-7000/normal.run.sh'), "Missing startup")
        self.assertTrue(os.path.exists('test-7000/silent.run.sh'), "Missing startup")
        
        
        # you should not be doing this normally - to invoke a build of a new instance
        # with changed parameters, because t7000 is already running, the script will
        # just check it is healthy (it won't change its config)
        
        montysolrupdate.main(['foo', '-c', '-u', '-a', '-t', '10', 'test-7000#w', 'test-7001#r', 'test-7002#r'])
        
        t7000 = open('test-7000/automatic-run.sh', 'r').read()
        t7001 = open('test-7001/automatic-run.sh', 'r').read()
        t7002 = open('test-7002/automatic-run.sh', 'r').read()
         
        self.assertTrue('Base profile: normal.run.sh' in t7000, "The instance was not based on correct profile")
        self.assertTrue('Base profile: reader.run.sh' in t7001, "The instance was not based on correct profile")
        self.assertTrue('Base profile: reader.run.sh' in t7002, "The instance was not based on correct profile")
        
        # now it should have a correct profile applied
        montysolrupdate.stop_live_instance('test-7000', max_wait=10)
            
        montysolrupdate.main(['foo', '-c', '-u', '-a', '-t', '10', 'test-7000#w', 'test-7001#r', 'test-7002#r'])
        t7000 = open('test-7000/automatic-run.sh', 'r').read()
        self.assertTrue('Base profile: writer.run.sh' in t7000, "The instance was not based on correct profile")
        

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
    test_suite = None 
    for test in tests:
        test_object = getattr(this_module, test) #XXX: i do not checking
        if test_suite is None:
            test_suite = unittest.defaultTestLoader.loadTestsFromTestCase(test_object)
        else:
            test_suite.addTests(unittest.defaultTestLoader.loadTestsFromTestCase(test_object))
    
    if test_suite is not None:
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