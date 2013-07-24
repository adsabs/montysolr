#!/usr/bin/env python

"""An assistant for updating MontySolr releases.

This script will update the codebase of MontySolr ON the machine(s)
that run it. This script is to be executed unattended and very often.

Here are the assumptions under which we work:

   - PATH contains correct versions of ant, java, javac, python, gcc,
         git
   - PYTHONPATH contains jcc, lucene
   - we are executed and accept NO parameters
   - we have internet access
   - we have write access to INSTDIR (/var/lib/montysolr)
   - the INSTDIR already exists
   - we run as user 'montysolr'
   - we fetch tags from git
   - the tags MUST be in a special format 
   
       <SOLR-MAJOR>.<SOLR-MINOR>.<MAJOR>.<MINOR>.<PATCH-INCREMENT>
       
       We are tracking SOLR versions, therefore the first two numbers
       correspond to the version of SOLR that is currently used by 
       MONTYSOLR
       
       Here is what happens when respective numbers change:
       
       SOLR-VER  : version is made of Major+Minor, ie. 40, on change
                   everything is nuked, montysolr is completely rebuilt,
                   index is forgotten
       MAJOR     : major bump in MontySolr, the same behaviour as above
       MINOR     : only java modules are recompiled (no JCC recompilation)
                   the solr configs are replaced, index is forgotten
       PATCH     : all java modules are recompiled, solr configs replaced
                   with the new version, index is re-used
   
If all these conditions are met, we'll do the following

   1. use git to fetch the latest tags
   2. compare the latest tag against the current installed version
   3. rebuild montysolr, do the necessary compilation, and setup
   4. IFF we have the live-instance name
       4.1 stop it (if it runs)
       4.2 replace the symbolic link (live-5002 -> live-5002-<release-tag>)
       4.3 point the index (live-5002/index -> live-5002-index-<increment>)
       4.4 remove the index data IFF the difference in incr. version is > 2
       


Necessary tests:

 - live instance stopped
 - live instance running, but in failed state (stalled)
 - live instance running (responding)
 - live instance restarted (but is broken)

"""

import sys
import os
import hashlib
import optparse
import re
import subprocess
import shutil
import re
import simplejson
import urllib
import time
import socket 
import threading
import Queue
import traceback

from contextlib import contextmanager

COMMASPACE = ', '
SPACE = ' '
tag_cre = re.compile(r'v?(\d+)\.(\d+)\.(\d+)\.(\d+)$')

INSTDIR = 'MONTYSOLR_HOME' in os.environ and os.environ['MONTYSOLR_HOME'] or '/var/lib/montysolr'
INSTNAME = 'MONTYSOLR_EXAMPLE_NAME' in os.environ and os.environ['MONTYSOLR_EXAMPLE_NAME'] or 'adsabs'
GITURL = 'MONTYSOLR_GIT' in os.environ and os.environ['MONTYSOLR_GIT'] or 'https://github.com/romanchyla/montysolr.git' #where to get the latest code from
NEW_INSTANCE_PORT_GAP = 'MONTYSOLR_URL_GAP' in os.environ and os.environ['MONTYSOLR_URL_GAP'] or 10 #when we build a new release, it will be started as orig_port+GAP
JCC_SVN_TAG='MONTYSOLR_JCC_SVN_TAG' in os.environ and os.environ['MONTYSOLR_JCC_SVN_TAG'] or '1452473' #the version of JCC we rely on (Use the one from Jenkins)
PYLUCENE_SVN_TAG='MONTYSOLR_PYLUCENE_SVN_TAG' in os.environ and os.environ['MONTYSOLR_PYLUCENE_SVN_TAG'] or JCC_SVN_TAG
INVENIO_CONFIG='INVENIO_CONFIG' in os.environ and os.environ['INVENIO_CONFIG'] or ''
INVENIO_COMMIT='INVENIO_COMMIT' in os.environ and os.environ['INVENIO_COMMIT'] or 'master'

# some ideas stolen from python release script

if "check_output" not in dir( subprocess ): # duck punch it in!
    def f(*popenargs, **kwargs):
        if 'stdout' in kwargs:
            raise ValueError('stdout argument not allowed, it will be overridden.')
        process = subprocess.Popen(stdout=subprocess.PIPE, *popenargs, **kwargs)
        output, unused_err = process.communicate()
        retcode = process.poll()
        if retcode:
            cmd = kwargs.get("args")
            if cmd is None:
                cmd = popenargs[0]
            raise subprocess.CalledProcessError(retcode, cmd)
        return output
    subprocess.check_output = f
    
def error(*msgs):
    sys.stderr.write("**ERROR**\n")
    for msg in msgs:
        sys.stderr.write(msg)
        sys.stderr.write("\n")
    sys.exit(1)


def run_cmd(args, silent=False, strict=True):
    cmd = SPACE.join(map(str, args))
    if not silent:
        print('$ %s' % cmd)
    try:
        if silent:
            code = subprocess.call(cmd, shell=True, stdout=subprocess.PIPE)
        else:
            code = subprocess.call(cmd, shell=True)
    except OSError:
        error('failed: %s' % cmd)
    else:
        if strict and code != 0:
            error('failed: %s' % cmd)
        return code


def get_output(args):
    return subprocess.check_output(SPACE.join(args), shell=True)


def check_basics():
    if not os.path.exists(INSTDIR):
        error('INSTDIR does not exist: %s' % INSTDIR )
        
    if not os.access(INSTDIR, os.W_OK):
        error('Cannot write into INSTDIR: %s' % INSTDIR)

    

def check_options(options):
    for v in 'PATH,ANT_HOME,JAVA_HOME'.split(','):
        if v not in os.environ:
            error('%s not detected.' % v,
              'Please set your enviroment variable')
            
    if options.force_recompilation:
        options.update = True
    
    if options.test_scenario:
        if not re.compile(r"^(\+|\-)?(major|minor|patch)$").match(options.test_scenario):
            error("Wrong format in --test_scenario: " % options.test_scenario)
        if options.test_scenario[0] == '+':
            operator = lambda x: x+1
        else:
            operator = lambda x: x-1
        if 'major' in options.test_scenario:
            operation = lambda tag: tag.__setattr__('major', operator(tag.major))
        elif 'minor' in options.test_scenario:
            operation = lambda tag: tag.__setattr__('minor', operator(tag.minor))
        elif 'patch' in options.test_scenario:
            operation = lambda tag: tag.__setattr__('patch', operator(tag.patch))
        else:
            error("I'll never be executed :-) Or, will i?")
        
        options.test_scenario = operation

def get_arg_parser():
    usage = '%prog [options] tagname'
    p = optparse.OptionParser(usage=usage)
    p.add_option('-a', '--setup_prerequisites',
                 default=False, action='store_true',
                 help='Install all prerequisites')
    p.add_option('-p', '--setup_python',
                 default=False, action='store_true',
                 help='Setup Python virtualenv')
    p.add_option('--setup_ant',
                 default=False, action='store_true',
                 help='Install Ant (1.8.3) into perpetuum folder, this must be called with ANT_HOME=<perpetuum>/ant')
    p.add_option('-j', '--setup_jcc',
                 default=False, action='store_true',
                 help='Install JCC')
    p.add_option('-y', '--setup_pylucene',
                 default=False, action='store_true',
                 help='Install pylucene')
    p.add_option('-i', '--setup_invenio',
                 default=False, action='store_true',
                 help='Install invenio, you need INVENIO_CONFIG variable')
    p.add_option('-d', '--check_diagnostics',
                 default=False, action='store_true',
                 help='Invokes /solr/montysolr_diagnostics when checking MontySolr health')
    p.add_option('-u', '--update',
                 default=False, action='store_true',
                 help='Update live instances. You must supply their names as arguments')
    p.add_option('-c', '--create',
                 default=False, action='store_true',
                 help='Create the live instance if it doesn\'t exist')
    p.add_option('-x', '--start_indexing',
                 default=False, action='store_true',
                 help='Call invenio-doctor?command=discover for major/minor version upgrades')
    p.add_option('-f', '--force_recompilation',
                 default=False, 
                 action='store_true',
                 help='Force recompilation (even if no tag upgrade)',)
    p.add_option('-t', '--timeout',
                 default=5*60, action='store',
                 help='Seconds after which live instance is declared dead',
                 type='float')
    p.add_option('-o', '--stop',
                 default=False, action='store_true',
                 help='Stop running instances')
    p.add_option('-s', '--start',
                 default=False, action='store_true',
                 help='Start instances')
    p.add_option('-r', '--restart',
                 default=False, action='store_true',
                 help='Restart running instances')
    p.add_option('-b', '--test_branch',
                 action='store',
                 help='Instead of a tag, checkout a branch (latest code) instead of a tag. Use only for testing!')
    p.add_option('-S', '--test_scenario',
                 action='store',
                 default=None,
                 help='Change the existing installation tag - the script will think in needs to rebuild things. values: [+/-](major,minor,patch) Use only for testing! ')
    p.add_option('-B', '--run_command_before',
                 default='', action='store',
                 help='Invoke this command BEFORE run - use to restart/update instance')
    p.add_option('-A', '--run_command_after',
                 default='', action='store',
                 help='Invoke this command AFTER run - use to restart/update instance')
    return p





def manual_edit(fn):
    run_cmd([os.environ["EDITOR"], fn])


@contextmanager
def changed_dir(new):
    print('$ cd %s' % new)
    old = os.getcwd()
    os.chdir(new)
    try:
        yield
    finally:
        print('$ cd %s' % old)
        os.chdir(old)

def make_dist(name):
    try:
        os.mkdir(name)
    except OSError:
        if os.path.isdir(name):
            sys.stderr.write('WARNING: dist dir %s already exists\n' % name)
        else:
            error('%s/ is not a directory' % name)
    else:
        print('created dist directory %s' % name)

def tarball(source):
    """Build tarballs for a directory."""
    print('Making .tgz')
    base = os.path.basename(source)
    tgz = base + '.tgz'
    bz = base + '.tar.bz2'
    xz = base + '.tar.xz'
    run_cmd(['tar cf - %s | gzip -9 > %s' % (source, tgz)])
    print("Making .tar.bz2")
    run_cmd(['tar cf - %s | bzip2 -9 > %s' % (source, bz)])
    print("Making .tar.xz")
    run_cmd(['tar cf - %s | xz > %s' % (source, xz)])
    print('Calculating md5 sums')
    checksum_tgz = hashlib.md5()
    with open(tgz, 'rb') as data:
        checksum_tgz.update(data.read())
    checksum_bz2 = hashlib.md5()
    with open(bz, 'rb') as data:
        checksum_bz2.update(data.read())
    checksum_xz = hashlib.md5()
    with open(xz, 'rb') as data:
        checksum_xz.update(data.read())
    print('  %s  %8s  %s' % (
        checksum_tgz.hexdigest(), int(os.path.getsize(tgz)), tgz))
    print('  %s  %8s  %s' % (
        checksum_bz2.hexdigest(), int(os.path.getsize(bz)), bz))
    print('  %s  %8s  %s' % (
        checksum_xz.hexdigest(), int(os.path.getsize(xz)), xz))
    with open(tgz + '.md5', 'w', encoding="ascii") as fp:
        fp.write(checksum_tgz.hexdigest())
    with open(bz + '.md5', 'w', encoding="ascii") as fp:
        fp.write(checksum_bz2.hexdigest())
    with open(xz + '.md5', 'w', encoding="ascii") as fp:
        fp.write(checksum_xz.hexdigest())






class Tag(object):

    def __init__(self, tag_name):
        parts = tag_name.split('/')
        result = tag_cre.match(parts[-1])
        
        if result is None:
            error('tag %s is not valid' % tag_name)
        data = list(result.groups())
        
        if len(parts) == 3:
            self.ref = tag_name
        else:
            self.ref = 'not-valid-ref:%s' % tag_name
            
        if data[3] is None:
            # A final release.
            self.is_final = True
            data[3] = "f"
        else:
            self.is_final = False
        # For everything else, None means 0.
        for i, thing in enumerate(data):
            if thing is None:
                data[i] = 0
        self.solr_ver = int(data[0])
        self.major = int(data[1])
        self.minor = int(data[2])
        self.patch = int(data[3])
        

    def __str__(self):
        return "%d.%d.%d.%d" % (self.solr_ver, 
                               self.major, self.minor, self.patch)

    def __cmp__(self, other):
        for att in ['solr_ver', 'major', 'minor', 'patch']:
            a = getattr(self, att)
            b = getattr(other, att)
            if a < b:
                return -1
            elif a > b:
                return 1
            else:
                continue
        return 0 
    


def make_tag(tag):
    # make sure we're on the correct branch
    if tag.patch > 0:
        if get_output(['hg', 'branch']).strip().decode() != tag.basic_version:
            print('It doesn\'t look like you\'re on the correct branch.')
            if input('Are you sure you want to tag?') != "y":
                return
    run_cmd(['hg', 'tag', tag.hgname])


def get_release_tag(path=os.path.join(INSTDIR, 'RELEASE')):
    if os.path.exists(path):
        fo = open(path, 'r')
        tag = Tag(fo.read().strip())
        fo.close()
    else:
        tag = Tag('v0.0.0.0')
    return tag
            

def get_latest_git_release_tag(path=os.path.join(INSTDIR, 'montysolr')):
    """calls git to find the latest tagged commit using:
    git for-each-ref refs/tags --sort=-taggerdate --format='%(refname)' --count=1
    """
    with changed_dir(path):
        tag = get_output(['git', 'for-each-ref', 'refs/tags', '--sort=-taggerdate', '--format=\'%(refname)\'', '--count=1'])
        if tag is None or tag.strip() == '':
            error("Git returned no tagged reference")
        return Tag(tag.strip())

  
          
def check_live_instance(options, instance_names):
    
    git_tag = get_release_tag(path='montysolr/RELEASE')
    example_tag = get_release_tag(path='montysolr/build/contrib/examples/%s/RELEASE' % INSTNAME)
    
    # pretend some change has happened (used for testing)
    if options.test_scenario:
        options.test_scenario(example_tag)
    
    if example_tag != git_tag:
        build_example(git_tag)
        example_tag = get_release_tag(path='montysolr/build/contrib/examples/%s/RELEASE' % INSTNAME)
    base_path = os.path.realpath('.') # git_tag.minor =  1; git_tag.text = '40.1.1.2'
    
    writer_counter = 0
    list_of_reader_instances = []
    writer_instance_name = None
    for instance_name in instance_names:
        if instance_name[-2:] == '#r':
            list_of_reader_instances.append(instance_name[0:-2])
        elif instance_name[-2:] == '#w':
            writer_counter +=1
            writer_instance_name = instance_name
            
    if writer_counter > 1:
        error("This script does not know how to handle sitatuion\n" + \
              "when you have more than 1 writer, you should invoke\n" + \
              "the script for each set of writer+readers")
    if len(list_of_reader_instances) > 0 and writer_instance_name is None:
        error("When you use #r, you must specify also a writer")
        
    # make sure the writer is first in the list of processed instances
    if writer_instance_name is not None:
        instance_names.insert(0, instance_names.pop(instance_names.index(writer_instance_name)))
        writer_instance_name = writer_instance_name[0:-2]
    
    writer_finished = False
    
    for instance_name in instance_names:
        
        if '#' in instance_name:
            instance_name, instance_mode = instance_name.split('#')
        else:
            instance_mode = ''
        
        symbolic_name = instance_name                      # live-9001
        symbolic_name_data = '%s_data' % instance_name    # live-9001_data
        
        real_name = '%s_%s' % (instance_name, str(git_tag)) #live-9001_40.1.0.1
        
        # if we are a 'reader', we need to point at the index of a writer
        if instance_mode == 'r':
            real_name_data = '%s_%s_data' % (writer_instance_name, str(git_tag)) #live-9000_40.1.0.1_data
        else:
            real_name_data = '%s_%s_data' % (instance_name, str(git_tag)) #live-9001_40.1.0.1_data
        
        next_release = 'next-release_%s' % (instance_name)  # next-release_live-9001
        next_release_data = 'next-release_%s_data' % (instance_name) #next-release_live-9001_data
        
        port = extract_port(symbolic_name)
        
        if not os.path.exists(symbolic_name):
            # instance does not exist yet
            # status: OK, unittest OK
            if options.create:
                run_cmd(['cp', '-r', 'montysolr/build/contrib/examples/%s' % INSTNAME, real_name])
                run_cmd(['ln', '-s', real_name, symbolic_name])
                if instance_mode != 'r':
                    run_cmd(['mkdir', real_name_data])
                else:
                    assert os.path.exists(real_name_data)
                run_cmd(['ln', '-s', '%s/%s' % (base_path, real_name_data), "%s/%s/solr/data" % (base_path, symbolic_name)])
                run_cmd(['ln', '-s', real_name_data, symbolic_name_data])
                assert start_live_instance(options, symbolic_name, 
                                           port=port, 
                                           max_wait=options.timeout,
                                           list_of_readers=list_of_reader_instances,
                                           instance_mode=instance_mode)
                
                if options.start_indexing:
                    assert start_indexing(symbolic_name, port=port)
            else:
                print('WARNING - instance name does not exist, skipping: %s' % symbolic_name)
        
            continue
        
        else:
            if not os.path.islink(symbolic_name):
                error('The live instance must be a symbolic link: %s ->' % (real_name, symbolic_name))
            
            
        # some previous run has already created a candidate for the next run, this instance
        # is indexing data, once it has finished, we can stop it and move upfront
        if os.path.exists(next_release):
            
            # status: OK, unittest OK
            
            assert os.path.exists(next_release_data)
            port = get_pid(os.path.join(next_release, 'port')) # it will fail if it doesn't exist
            next_pid = get_pid(next_release + "/montysolr.pid")
            
            if check_pid_is_running(next_pid)==False:
                error("The next-release is present, but dead - we do not expect this!!!") 
            
            if instance_mode == 'r' and not writer_finished:
                    continue 
                
            if instance_mode == 'r' or is_invenio_doctor_idle(port):
                
                assert stop_live_instance(next_release, max_wait=options.timeout)
                assert stop_live_instance(symbolic_name, max_wait=options.timeout)
                orig_port = get_pid(os.path.join(symbolic_name, 'port'))
                run_cmd(['rm', symbolic_name])
                run_cmd(['rm', symbolic_name_data])
                run_cmd(['ln', '-s', os.path.realpath(next_release), symbolic_name])
                run_cmd(['ln', '-s', os.path.realpath(next_release_data), symbolic_name_data])
                run_cmd(['rm', next_release])
                run_cmd(['rm', next_release_data])
                writer_finished=True
                assert start_live_instance(options, symbolic_name, orig_port, 
                                           max_wait=options.timeout,
                                           list_of_readers=list_of_reader_instances,
                                           instance_mode=instance_mode)
            else:
                print ('%s still getting itself ready, nothing to do yet...' % next_release)
            continue 
                
                
        live_tag = get_release_tag(path='%s/RELEASE' % symbolic_name)
        
        
        if live_tag == git_tag:
            
            # status: OK, unittest missing
            
            if options.force_recompilation or options.test_scenario:
                run_cmd(['cp', '-fr', 'montysolr/build/contrib/examples/%s/*' % INSTNAME, symbolic_name])
            
            # just check if the instance is in a healthy state
            kwargs = dict(max_wait=options.timeout)
            if options.check_diagnostics:
                kwargs['tmpl'] ='http://localhost:%s/solr/montysolr_diagnostics'
                
            if not check_instance_health(port, **kwargs) or options.test_scenario or options.force_recompilation:
                if stop_live_instance(symbolic_name, max_wait=options.timeout):
                    start_live_instance(options, symbolic_name, port, 
                                        max_wait=options.timeout,
                                        list_of_readers=list_of_reader_instances,
                                        instance_mode=instance_mode)
                else:
                    error("Can't restart: %s" % symbolic_name)
            else:
                
                print ('%s is HEALTHY' % symbolic_name)
            
            continue # nothing to do
        
        
        if live_tag.major != git_tag.major or live_tag.solr_ver != git_tag.solr_ver:
            # major upgrade, we start a new instance and indexing
            # status: OK, unittest: OK
            
            if os.path.exists(real_name) or os.path.exists(real_name_data):
                
                if os.path.exists(os.path.join(real_name, 'montysolr.pid')):
                    pid = get_pid(os.path.join(real_name, 'montysolr.pid'))
                    if pid != -1 and check_pid_is_running(pid):
                        error("The live instance at %s is already running, we cannot create new-release with the same name" % real_name)
                
                run_cmd(['rm', '-fr', real_name])
                if instance_mode != 'r':
                    run_cmd(['rm', '-fr', real_name_data])
                
            
            run_cmd(['cp', '-r', 'montysolr/build/contrib/examples/%s' % INSTNAME, real_name])
            
            if instance_mode != 'r':
                run_cmd(['mkdir', real_name_data])
            else:
                assert os.path.exists(real_name_data)
            
            run_cmd(['ln', '-s', real_name, next_release])
            run_cmd(['ln', '-s', real_name_data, next_release_data])
            
            run_cmd(['rm', '-fr', "%s/solr/data" % real_name], strict=False)
            run_cmd(['ln', '-s', '%s/%s' % (base_path, real_name_data), "%s/%s/solr/data" % (base_path, next_release)])
            
            
            
            temporary_port = port+NEW_INSTANCE_PORT_GAP
            assert start_live_instance(options, next_release, port=temporary_port, 
                                       max_wait=options.timeout,
                                       list_of_readers=list_of_reader_instances,
                                       instance_mode=instance_mode)
            
            if options.start_indexing:
                assert start_indexing(next_release, port=temporary_port)
                
        elif live_tag.minor != git_tag.minor:
            # minor upgrade, we can re-use the index (we create a copy and keep)
            # the old index in place
            
            # status: OK, unittest: OK
            
            if options.test_scenario and os.path.exists(real_name):
                run_cmd(['cp', '-r', 'montysolr/build/contrib/examples/%s/*' % INSTNAME, real_name])
            else:
                run_cmd(['cp', '-r', 'montysolr/build/contrib/examples/%s' % INSTNAME, real_name])
            run_cmd(['rm', '-fr', "%s/solr/data" % real_name], strict=False)
            
            if instance_mode != 'r':
                run_cmd(['mkdir', real_name_data])
                run_cmd(['cp', '-fR', '%s/*' % symbolic_name_data, real_name_data])
            else:
                assert os.path.exists(real_name_data)
            
            stop_live_instance(symbolic_name, max_wait=options.timeout)
            
            run_cmd(['rm', symbolic_name])
            run_cmd(['rm', symbolic_name_data])
            
            run_cmd(['ln', '-s', real_name, symbolic_name])
            run_cmd(['ln', '-s', real_name_data, symbolic_name_data])            
            run_cmd(['ln', '-s', '%s/%s' % (base_path, symbolic_name_data), "%s/solr/data" % real_name])
            
            
            assert start_live_instance(options, symbolic_name, 
                                       port=port, 
                                       max_wait=options.timeout,
                                       list_of_readers=list_of_reader_instances,
                                       instance_mode=instance_mode)
        else:
            # just a patch, we will re-use index 
            # status: OK, unittest: OK
            
            stop_live_instance(symbolic_name, max_wait=options.timeout)
            
            if options.test_scenario and os.path.exists(real_name):
                run_cmd(['cp', '-r', 'montysolr/build/contrib/examples/%s/*' % INSTNAME, real_name])
            else:
                run_cmd(['cp', '-r', 'montysolr/build/contrib/examples/%s' % INSTNAME, real_name])
            run_cmd(['rm', '-fr', "%s/solr/data" % real_name], strict=False)
            run_cmd(['ln', '-s', '%s/%s' % (base_path, symbolic_name_data), "%s/solr/data" % real_name])
            
            run_cmd(['rm', symbolic_name])
            run_cmd(['ln', '-s', real_name, symbolic_name])
            assert start_live_instance(options, symbolic_name, 
                                       port=port, 
                                       max_wait=options.timeout,
                                       list_of_readers=list_of_reader_instances,
                                       instance_mode=instance_mode)
            
            
def extract_port(symbolic_name):
    digits = []
    for l in reversed(symbolic_name):
        if l.isdigit():
            digits.insert(0, l)
        else:
            break
    if len(digits) == 0:
        error("The instance name must end with the port, eg. live-9002. We got: %s" % symbolic_name)
    return int(''.join(digits))



def save_into_file(path, value):
    assert isinstance(value, int)
    fo = open(path, 'w')
    fo.write(str(value))
    fo.close()

def start_indexing(instance_dir, port):
    url = 'http://localhost:%s/solr/invenio-doctor' % port
    rsp = req(url, command='status')
    
    if rsp['status'] != 'idle':
        print ('WARNING: live instance is reporting to be already busy: %s' % instance_dir)
    
    rsp = req(url, command='discover')
    rsp = req(url, command='start')
    time.sleep(3)
    
    if is_invenio_doctor_idle(port):
        error('something is wrong, indexing finished too fast %s' % instance_dir)
    return True
        

def check_instance_health(port, max_wait=30, tmpl='http://localhost:%s/solr/admin/ping'):
    url = tmpl % port
    
    max_time = time.time() + max_wait
    rsp = None
    while time.time() < max_time:
        try:
            rsp = req(url)
            if rsp['status'] == '0' or rsp['status'] == 'OK':
                return True
        except Exception, e:
            if rsp is None:
                #traceback.print_exc(e)
                print "Waiting for instance to come up: %d sec." % (max_time - time.time(),) 
            if rsp is not None and 'error' in rsp:
                error(str(rsp['error']).replace('\\n', "\n"))
        time.sleep(1)
    return False

def is_invenio_doctor_idle(port):
    url = 'http://localhost:%s/solr/invenio-doctor' % port
    rsp = req(url, command='status')
    if rsp['status'] == 'busy':
        return False
    elif rsp['status'] == 'idle':
        return True
    else:
        error('something is wrong, unxpected reply: %s' % rsp)

def reload_core(port):
    url = 'http://localhost:%s/solr/admin/cores?action=RELOAD&core=collection1' % port
    
    rsp = req(url, command='status')
    if 'status' in rsp and rsp['status'] == '0':
        return True
    else:
        error('something is wrong, unexpected reply: %s' % rsp)


def make_request(q, url, kwargs):
    try:
        kwargs['wt'] = 'json'
        params = urllib.urlencode(kwargs)
        page = ''
        conn = urllib.urlopen(url, params)
        page = conn.read()
        rsp = simplejson.loads(page)
        conn.close()
        q.put(rsp)
    except Exception, e:
        q.put(e)
    
        
def req(url, **kwargs):
    q = Queue.Queue()
    t = threading.Thread(target=make_request, args = (q, url, kwargs))
    t.start()
    t.join(3.0)
    r = q.get()
    if isinstance(r, Exception):
        raise r
    elif r is None:
        raise Exception("Timeout getting url=%s & %s" % (url, kwargs))
    return r

def get_pid(pidpath):
    if os.path.exists(pidpath):
        with open(pidpath, 'r') as pidfile:
            r_pid = pidfile.read().strip()
        try:
            return int(r_pid)
        except ValueError:
            return -1
    return -1


def acquire_lock(pidpath):
    fo = open(pidpath, 'w')
    fo.write(str(os.getpid()))
    fo.close()

def remove_lock(pidpath):
    os.remove(pidpath)
            
def check_pid_is_running(pid):
    if os.path.exists('/proc/%s' % pid):
        return True
    return False        
            

def check_prerequisites(options):
    
    if options.setup_ant:
        setup_ant(options)
    if options.setup_prerequisites or options.setup_python:
        setup_python(options)
    if options.setup_prerequisites or options.setup_jcc:
        setup_jcc(options)
    if options.setup_prerequisites or options.setup_pylucene:
        setup_pylucene(options)
    if options.setup_prerequisites or options.setup_invenio:
        setup_invenio(options)
    
    check_ant(options)
    
    if not os.path.exists('montysolr'):
        run_cmd(['git', 'clone', GITURL, 'montysolr'])
        
    with changed_dir('montysolr'):
        run_cmd(['git', 'fetch'])
        run_cmd(['git', 'reset', '--hard', 'origin/master'])
        run_cmd(['git', 'checkout', 'master'])


def check_ant(options):
    version = get_output(["ant -version"])
    if ' version ' in version:
        elements = version.split()
        version = elements[elements.index('version')+1]
        version = int(version.replace('.', '')[0:3])
        if version < 182:
            error("""
                Your installation of ant is too old: %s
                You can run: montysolrupdate.py --setup_ant 
                and set ANT_HOME=%s""" % 
                (version, os.path.join(INSTDIR, "perpetuum/ant")))
        
def setup_ant(options):
    """
    On old systems, such as CentOS, the ant binaries are useless
    """
    
    if options.force_recompilation and os.path.exists('ant'):
        run_cmd(['rm', '-fr', 'ant'])
    elif os.path.exists('ant/RELEASE') and str(get_pid('ant/RELEASE')) == str(JCC_SVN_TAG):
        return # already installed
    
    with open("install_ant.sh", "w") as build_ant:
        build_ant.write("""#!/bin/bash -e
        
        export JAVA_HOME=%(java_home)s
        export ANT_HOME=%(ant_home)s
        
        
        wget -nc http://archive.apache.org/dist/ant/binaries/apache-ant-1.8.4-bin.tar.gz
        tar -xzf apache-ant-1.8.4-bin.tar.gz
        mv apache-ant-1.8.4 ant
        cd ant
        
        ant -f fetch.xml -Ddest=system
        echo "%(release)s" > RELEASE
        
        """ % {'java_home': os.environ['JAVA_HOME'], 
               'ant_home': os.path.join(INSTDIR, "perpetuum/ant"),
               'release': JCC_SVN_TAG})
    
    run_cmd(['chmod', 'u+x', 'install_ant.sh'])
    run_cmd(['./install_ant.sh'])
    
    
        

def setup_python(options):
    
    if options.force_recompilation and os.path.exists('python'):
        run_cmd(['rm', '-fr', 'python'])
    elif os.path.exists('python/RELEASE') and str(get_pid('python/RELEASE')) == str(JCC_SVN_TAG):
        return # python already installed

    with open("install_python.sh", "w") as inpython:
        inpython.write("""#!/bin/bash -e

echo "using python: %(python)s"

virtualenv --unzip-setuptools -p %(python)s python
echo "done creating python virtualenv"

echo "0" > python/RELEASE
source python/bin/activate

easy_install -U distribute==0.6.30

# install needed modules
pip install sqlalchemy
pip install mysql-python

echo "Installing numpy, make sure you have the package libatlas-base-dev if you get errors"
pip install numpy
pip install lxml
pip install simplejson
pip install configobj
pip install pyparsing==1.5.7

# verify installation
python -c "import numpy,lxml,simplejson,configobj,pyparsing, MySQLdb, sqlalchemy"

deactivate
echo "%(release)s" > python/RELEASE

exit 0
""" % {'python': sys.executable, 'release': JCC_SVN_TAG} )
        
    run_cmd(['chmod', 'u+x', 'install_python.sh'])
    run_cmd(['./install_python.sh'])

def setup_jcc(options):

    if options.force_recompilation and os.path.exists('jcc'):
        run_cmd(['rm', '-fr', 'jcc'])
    
    if os.path.exists('jcc') and str(get_pid('jcc/RELEASE')) == str(JCC_SVN_TAG):
        return # already there
    
    with open("install_jcc.sh", "w") as inpython:
        inpython.write("""#!/bin/bash -e

source python/bin/activate

svn co http://svn.apache.org/repos/asf/lucene/pylucene/trunk/jcc@%(JCC_SVN_TAG)s
cd jcc
echo "0" > RELEASE
export USE_DISTUTILS
export JCC_JDK=%(JAVA_HOME)s
python setup.py build
python setup.py bdist_egg
python setup.py install
echo "%(JCC_SVN_TAG)s" > RELEASE


# verify installation
cd ..
python -c "import jcc;jcc.initVM();print jcc.__file__"


deactivate
exit 0
""" % {'JAVA_HOME': os.environ['JAVA_HOME'], 'JCC_SVN_TAG' : JCC_SVN_TAG})
        
    run_cmd(['chmod', 'u+x', 'install_jcc.sh'])
    run_cmd(['bash', '-e', './install_jcc.sh'])
    


def setup_pylucene(options):

    if options.force_recompilation and os.path.exists('pylucene'):
        run_cmd(['rm', '-fr', 'pylucene'])
        
    if os.path.exists('pylucene') and str(get_pid('pylucene/RELEASE')) == str(PYLUCENE_SVN_TAG):
        return # already there

    
    run_cmd(['rm', 'pylucene/Makefile*'], strict=False)
    
    with open("install_pylucene.sh", "w") as infile:
        infile.write("""#!/bin/bash -xe

source python/bin/activate

export ANT_HOME=%(ant_home)s
export JAVA_HOME=%(java_home)s

svn co http://svn.apache.org/repos/asf/lucene/pylucene/trunk@%(PYLUCENE_SVN_TAG)s pylucene
cd pylucene

echo "0" > RELEASE

if [ ! -f Makefile.copy ]; then
    cp Makefile Makefile.copy
    
    echo "VERSION=4.0-0
LUCENE_SVN_VER=HEAD
LUCENE_VER=4.0
LUCENE_SVN=http://svn.apache.org/repos/asf/lucene/dev/branches/lucene_solr_4_0
PYLUCENE:=\$(shell pwd)
LUCENE_SRC=lucene-java-\$(LUCENE_VER)
LUCENE=\$(LUCENE_SRC)/lucene
PREFIX_PYTHON=/usr
ANT=%(ant_home)s/bin/ant
PYTHON=python
JCC=\$(PYTHON) -m jcc.__main__ --shared --use_full_names
NUM_FILES=3
" > Makefile
    
    tail -n +25 Makefile.copy >> Makefile
    
    # on stupid old centos, icupkg is outdated and since it took 2 hours of my life
    # i deactivate it from the build (we don't use it anyways....)
    
    sed 's/shell which icupkg/shell which icupkgooooo/' Makefile > Makefile.tmp
    mv Makefile.tmp Makefile

fi


make 
make install

echo "%(PYLUCENE_SVN_TAG)s" > RELEASE

# verify installation
cd ..
python -c "import lucene;lucene.initVM();print lucene.__file__"


deactivate
exit 0
""" % {'ant_home': os.environ['ANT_HOME'], 'PYLUCENE_SVN_TAG': PYLUCENE_SVN_TAG,
       'java_home': os.environ['JAVA_HOME']})
        
    run_cmd(['chmod', 'u+x', 'install_pylucene.sh'])
    run_cmd(['./install_pylucene.sh'])


def setup_invenio(options):

    if options.force_recompilation and os.path.exists('invenio'):
        run_cmd(['rm', '-fr', 'invenio'])
        
    if os.path.exists('invenio.git') and os.path.exists('invenio'):
        with changed_dir('invenio.git'):
            commit = get_output(["git", "log", "--pretty=oneline", "--abbrev-commit", "-n 1"])
            run_cmd(['git', 'fetch'])
            run_cmd(['git', 'reset', '--hard', 'origin/master'])
            run_cmd(['git', 'checkout', 'master'])
            commit2 = get_output(["git", "log", "--pretty=oneline", "--abbrev-commit", "-n 1"])
            if commit == commit2 and len(commit2) > 0:
                return # already installed, no changes there
        
    
    with open("install_invenio.sh", "w") as inpython:
        inpython.write("""#!/bin/bash -e

source python/bin/activate

target=%(INSTDIR)s/perpetuum/invenio

if [ ! -d invenio.git ]; then
   git clone git://github.com/tiborsimko/invenio.git invenio.git
else
   cd invenio.git
   git fetch
   git reset --hard origin/master
   git checkout master
   cd ..
fi

site_packages=`python -c "import os,sys;print '%%s/lib/python%%s.%%s/site-packages' %% (os.path.realpath('python'), sys.version_info[0], sys.version_info[1])"`

cd invenio.git
git checkout master

aclocal && automake -a && autoconf

export PYTHONPATH=%(INSTDIR)s/perpetuum/invenio/lib/python:$PYTHONPATH
CONFIGURE_OPTS="--with-python=`which python` --prefix=$target" 
./configure $CONFIGURE_OPTS  0</dev/null

rm -fR $target
make clean
make
make install

if [ -f $site_packages/invenio ]; then
  rm $site_packages/invenio
fi

ln -s %(INSTDIR)s/perpetuum/invenio/lib/python/invenio $site_packages/invenio

echo "%(invenio_config)s" > %(INSTDIR)s/perpetuum/invenio/etc/invenio-local.conf

# this actually generates invnenio module (inside invenio lib)
python %(INSTDIR)s/perpetuum/invenio/bin/inveniocfg --update-all

deactivate
exit 0
""" % {'INSTDIR':INSTDIR, 'invenio_config': INVENIO_CONFIG.replace("$", "\\$")})
        
    run_cmd(['chmod', 'u+x', 'install_invenio.sh'])
    run_cmd(['bash', '-e', './install_invenio.sh'])


def setup_build_properties():
    lines = []
    with open('build.properties.default', 'r') as infile:
        for line in infile:
            line = line.strip()
            if len(line) > 0 and line[0] != '#':
                parts = line.split('=', 1)
                if parts[0] == 'python':
                    lines.append('python=%s' % os.path.realpath(os.path.join(INSTDIR, "perpetuum", "python/bin/python")))
                elif parts[0] == 'jcc':
                    lines.append('jcc=%s' % {5:'-m jcc',6:'-m jcc.__main__',7:'-m jcc'}[sys.version_info[1]])
                elif parts[0] == 'ant':
                    lines.append('ant=%s' % 'ant')
                else:
                    lines.append(line)
    fo = open('build.properties', 'w')
    fo.write("\n".join(lines))
    fo.close()
    
    
    
        

def upgrade_montysolr(curr_tag, git_tag):
    
    
    with changed_dir('montysolr'):
        
        with open('build-montysolr.sh', 'w') as build_script:
            build_script.write("""#!/bin/bash -e

export JAVA_HOME=%(java_home)s
export ANT_HOME=%(ant_home)s

source ../python/bin/activate


case "$1" in
"nuke")
    if [ -f RELEASE ]; then
       rm RELEASE
    fi
    ant clean
    ant get-solr build-all
    ant test-python
    ;;
"minor" | "3")
    if [ -f RELEASE ]; then
       rm RELEASE
    fi
    ant get-solr build-all
    ant test-python
    ;;
esac

ant build-contrib
ant -file contrib/examples/build.xml clean build-one -Dename=%(example)s

deactivate
        """ % {'example': INSTNAME, 'java_home': os.environ['JAVA_HOME'], 'ant_home': os.environ['ANT_HOME']}
        )
    
        run_cmd(['chmod', 'u+x', 'build-montysolr.sh'])
    
        #if os.path.exists('RELEASE'):
        #    run_cmd(['rm', 'RELEASE'], strict=False)
        
        # get the target tag
        run_cmd(['git', 'checkout', '-f', '-b', git_tag.ref], strict=False)
        setup_build_properties()
        
        # nuke everything, start from scratch
        if curr_tag.solr_ver != git_tag.solr_ver or curr_tag.major != git_tag.major:
            #run_cmd(['ant', 'clean'])
            #run_cmd(['ant', 'get-solr', 'build-solr'])
            # maybe i could make this to work, right now: upgrades must be  invoked after source was called
            #run_cmd(['bash', '-c', 'source %s/python/bin/activate; ant get-solr build-solr; deactivate' % os.path.realpath('..')])
            #run_cmd(['ant', 'build-all'])
            run_cmd(['./build-montysolr.sh', 'nuke'])
        elif curr_tag.minor != git_tag.minor:
            #run_cmd(['ant', 'get-solr', 'build-solr'])
            #run_cmd(['ant', 'build-all'])
            run_cmd(['./build-montysolr.sh', 'minor'])
        else:
            run_cmd(['./build-montysolr.sh', 'patch'])
            
        # always re-compile, this is not too expensive
        #run_cmd(['ant', 'build-contrib'])
        
        # assemble the deployment target
        #run_cmd(['ant', '-file', 'contrib/examples/build.xml', 'clean', 'build-one', '-Dename=%s' % INSTNAME])
        #run_cmd(['ant', '-file', 'contrib/examples/build.xml', 'run-configured', '-Dename=%s' % INSTNAME,
        #                '-Dtarget=generate-run.sh', '-Dprofile=silent.profile'])
            
        # update the RELEASE file
        with open('RELEASE', 'w') as release:
            release.write(str(git_tag))
        
            

def build_example(git_tag):
    
    with changed_dir('montysolr'):
        with open('build-example.sh', 'w') as build_script:
            build_script.write("""#!/bin/bash -e

example=${1:%(example)s}
profile_names=${2:silent}
export JAVA_HOME=%(java_home)s
export ANT_HOME=%(ant_home)s

source ../python/bin/activate

ant -file contrib/examples/build.xml clean build-one -Dename=$example

# generate run.sh for every profile 
for profile_name in $profile_names
do
  ant -file contrib/examples/build.xml run-configured -Dename=$example -Dtarget=generate-run.sh -Dprofile=${profile_name}.profile
  mv build/contrib/examples/${example}/run.sh build/contrib/examples/${example}/${profile_name}.run.sh
done

deactivate
        """ % {'example': INSTNAME, 'java_home': os.environ['JAVA_HOME'], 'ant_home': os.environ['ANT_HOME']}
        )
        
        run_cmd(['chmod', 'u+x', './build-example.sh'])
        profiles = map(lambda x: x.replace('.profile',''), filter(lambda x: '.profile' in x, os.listdir('build/contrib/examples/%s' % INSTNAME)))
        
        run_cmd(['./build-example.sh', INSTNAME, '"%s"' % " ".join(profiles)])
        
        with open('build/contrib/examples/%s/RELEASE' % INSTNAME, 'w') as release:
            release.write(str(git_tag))
            
                 
def stop_live_instance(instance_dir, max_wait=30):
    with changed_dir(instance_dir):
        pid = get_pid('montysolr.pid')
        if check_pid_is_running(pid) == False:
            print('Warning: wanted to stop live instance which is not running: %s' % instance_dir)
            return True
        
        port = None
        if os.path.exists('port'):
            fo = open('port', 'r')
            port = fo.read().strip()
            fo.close()
        
        wait_no_more = time.time() + max_wait/2
        
        while time.time() < wait_no_more:
            if check_pid_is_running(pid):
                run_cmd(['kill', pid])
                time.sleep(1)
            else:
                continue
        
        if check_pid_is_running(pid):
            wait_no_more = time.time() + max_wait/2
            while time.time() < wait_no_more:
                if check_pid_is_running(pid):
                    run_cmd(['kill', '-9', pid])
                    time.sleep(1)
                else:
                    continue
        
        if check_pid_is_running(pid):
            error("We cannot stop %s pid=%s" % (instance_dir, pid))
        
        return True    
        
        

def start_live_instance(options, instance_dir, port, 
                        max_attempts = 3, 
                        max_wait=30,
                        instance_mode='',
                        list_of_readers=[]):
    
    with changed_dir(instance_dir):
        
        if options.run_command_before:
            run_cmd([options.run_command_before])
                    
        pid = get_pid('montysolr.pid')
        if pid != -1 and check_pid_is_running(pid):
            error("The live instance at %s is still running" % instance_dir)
            
        fo = open('port', 'w')
        fo.write(str(port))
        fo.close()
        
        #i am seeing the socket is not closed on time
        #s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
        #try:
        #    s.bind(('', port)) # will fail if the socket is already used
        #finally:
        #    s.close()
        
        failed = get_pid('FAILED.counter')
        if failed > max_attempts:
            error("The live instance is a zombie (probably compilation failed), call a doctor!")
        
        profile_name = 'normal'
        if instance_mode == 'w':
            profile_name = 'writer'
        elif instance_mode == 'r':
            profile_name = 'reader'
        else:
            profile_name = 'normal'
        
        if not os.path.exists('%s.run.sh' % profile_name):
            error("Missing %s.run.sh - (you must have a %s.profile to generate this file)" % 
                  (profile_name, profile_name))
        
        fi = open('%s.run.sh' % profile_name, 'r')
        start = fi.read()
        fi.close()
        
        lines = start.split("\n")
        lines.insert(1, """
        
        # File modified by: montysolrupdate.py
        # Base profile: %(profile)s.run.sh
        
        source ../python/bin/activate
        export PYTHONPATH=`python -c "import sys;print ':'.join(sys.path)"`:$PYTHONPATH
        """ % {'profile': profile_name}
        )
        start = '\n'.join(lines)
        
        start = re.sub(r'HOMEDIR=.*\n', 'HOMEDIR=%s\n' % os.path.realpath('.'), start)
        start = re.sub(r'--port\s+\d+', '--port %s' % port, start)
        start = re.sub('\n([\t\s]+)(java -cp )', '\\1export PATH=%s/bin:$PATH\n\\1\\2' % os.environ['JAVA_HOME'], start)
        
        # this is necessary only when in test run (and there we can be sure that the files were
        # overwritten when a new code was installed)
        if options.test_scenario or not os.path.exists('solr/collection1/conf/solrconfig.xml.orig'):
            run_cmd(['cp', 'solr/collection1/conf/solrconfig.xml', 'solr/collection1/conf/solrconfig.xml.orig'])
            
        if instance_mode =='w': # for master-writers
            # we must change also the solrconfig
            if len(list_of_readers) <= 0:
                error("When you use write-master, you must also specify a reader node")
                 
            list_of_nodes = []
            for n in list_of_readers:
                reader_port = extract_port(n.split('#')[0])
                list_of_nodes.append(' <str>http://localhost:%s/solr/ads-config?command=reopenSearcher</str>' % reader_port)
                
            solrconfig = open('solr/collection1/conf/solrconfig.xml.orig', 'r').read()
            
            solrconfig = solrconfig.replace('</updateHandler>',
                              """
                              <!-- automatically generated by montysolr-update.py -->
                              
                              <listener event="postCommit" 
                                  class="solr.RunExecutableListener">
                                  <str name="exe">curl</str>
                                  <str name="dir">.</str>
                                  <bool name="wait">false</bool>
                                  <arr name="args"> %s </arr>
                                </listener>
                              </updateHandler>
                              """ % ' '.join(list_of_nodes))
            
            with open('solr/collection1/conf/solrconfig.xml.new', 'w') as fi_solrconfig:
                fi_solrconfig.write(solrconfig)
            with open('solr/collection1/conf/solrconfig.xml', 'w') as fi_solrconfig:
                fi_solrconfig.write(solrconfig)

        fo = open('automatic-run.sh', 'w')
        fo.write(start)
        fo.close()
        
        run_cmd(['chmod', 'u+x', 'automatic-run.sh'])
        run_cmd(['bash', '-e', './automatic-run.sh', '&'])
        
        kwargs = dict(max_wait=options.timeout)
        if options.check_diagnostics:
            kwargs['tmpl'] ='http://localhost:%s/solr/montysolr_diagnostics'
        if not check_instance_health(port, **kwargs):
            run_cmd(['kill', '-9', str(get_pid('montysolr.pid'))])
            time.sleep(3)
            failed += 1
            save_into_file('FAILED.counter', failed)
            error("Instance is not in a healthy state %s" % instance_dir)
            return False
        
        if os.path.exists('FAILED.counter'):
            run_cmd(['rm', 'FAILED.counter'])
        
        if options.run_command_after:
            run_cmd([options.run_command_after])
            
        return True

              

def main(argv):
    
    check_basics()
    
    if not os.path.exists(os.path.join(INSTDIR, 'perpetuum')):
        run_cmd(['mkdir', os.path.join(INSTDIR, 'perpetuum')])
    
    
    with changed_dir(os.path.join(INSTDIR, 'perpetuum')):
        
        update_pid = get_pid('update.pid')
        if update_pid != -1 and check_pid_is_running(update_pid):
            error("The script is already running with pid: %s" % update_pid)
        
        acquire_lock('update.pid')
        
        parser = get_arg_parser()
        options, args = parser.parse_args(argv)
        check_options(options)
        
        print "============="
        for k,v in options.__dict__.items():
            print '%s=%s' % (k, v)
        print 'args=', args
        print "============="
        
        # install pre-requisities if requested
        check_prerequisites(options)
        
        instance_names = []
        if len(args) > 1:
            instance_names = args[1:]
        else:
            print('WARNING: no live instance name(s) supplied')
        
        if options.update:
            if options.force_recompilation:
                run_cmd(['rm', 'montysolr/RELEASE'], strict=False)
                
            # check the repo and find out if there are changes
            git_tag = get_latest_git_release_tag('montysolr')
            curr_tag = get_release_tag('montysolr/RELEASE')
            
            # for testing, we may want to use the latest code
            if options.test_branch:
                git_tag.ref = options.test_branch
                
            # pretend some change has happened (used for testing)
            if options.test_scenario:
                options.test_scenario(curr_tag)
            
            if curr_tag > git_tag:
                error("whaaat!?! The current instance has a higher tag than montysolr.git!? %s > %s" % (curr_tag, git_tag))
            if curr_tag == git_tag:
                if len(instance_names) > 0:
                    print("Compiled version is the latest, we'll just check the live instance(s)")
            elif curr_tag != git_tag:
                upgrade_montysolr(curr_tag, git_tag)
        
        if len(instance_names) > 0:
            if options.stop or options.restart:
                for iname in instance_names:
                    parts = iname.split('#')
                    stop_live_instance(instance_dir=parts[0], max_wait=60) # when killing, we can be nasty
            if options.update or options.start or options.restart:
                check_live_instance(options, instance_names)
            
            
        remove_lock('update.pid')    
    

if __name__ == '__main__':
    main(sys.argv)
