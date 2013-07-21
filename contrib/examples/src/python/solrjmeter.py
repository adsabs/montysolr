#!/usr/bin/env python

"""An assistant for measuring MontySolr releases performance.

This script will run jmeter, and distill certain performance
characteristics

Here are the assumptions under which we work:

   - PATH contains correct versions of ant, java, javac, python, gcc,
         git
   - we have internet access
   - the INSTDIR already exists
   - we run as user 'montysolr'
   
"""

import sys
import os
import optparse
import subprocess
import re
import glob
import csv
import simplejson
import datetime
from montysolrupdate import error, run_cmd, get_output, check_basics, changed_dir, Tag,\
     get_release_tag, get_latest_git_release_tag, check_pid_is_running, remove_lock, \
     get_pid, req, acquire_lock, INSTDIR
from pprint import pprint,pformat

COMMASPACE = ', '
SPACE = ' '
tag_cre = re.compile(r'v?(\d+)\.(\d+)\.(\d+)\.(\d+)$')


_NAME = 'solrjmeter'
_RELEASE = 1

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

    






def check_options(options, args):
    
    if not options.jmx_test or not os.path.exists(options.jmx_test):
        error('option jmx_test must point to a valid JMX file, we got: %s' % options.jmx_test )
    
    
    if not options.serverName and not options.serverPort:
        error("You must specify both server and port")
        
    options.query_endpoint = 'http://%s:%s/solr' % (options.serverName, options.serverPort)
    
    jmx_options = []
    for k, v in options.__dict__.items():
        if k.lower() != k and v:
            jmx_options.append('-D%s=%s' % (k, v))
    
    options.jmx_args = ' '.join(jmx_options)
    
    if options.google_spreadsheet and options.google_user and options.google_password:
        options.upload = True
    else:
        options.upload = False
        
    options.script_home = os.path.join(os.path.abspath(args[0] + '/..'))
    options.today = datetime.datetime.now().strftime("%a %Y-%m-%d %H:%M")
    

def get_arg_parser():
    usage = '%prog [options] example.queries example2.queries....'
    p = optparse.OptionParser(usage=usage)
    p.add_option('-a', '--setup_prerequisities',
                 default=False, action='store_true',
                 help='Install all prerequisites')
    p.add_option('-x', '--jmx_test',
                 default='%s/perpetuum/montysolr/contrib/examples/adsabs/jmeter/SolrQueryTest.jmx' % INSTDIR, 
                 action='store',
                 help='The configuration of the test (location of .jmx file)')
    p.add_option('-j', '--setup_jmeter',
                 default=False, action='store_true',
                 help='Install jmeter')
    p.add_option('-J', '--java',
                 default='java', action='store',
                 help='Java executable')
    p.add_option('-q', '--queries_pattern',
                 default='', action='store',
                 help='Pattern to use for retrieving jmeter queries')
    p.add_option('-u', '--update_command',
                 default='', action='store',
                 help='Invoke this command before running tests - use to restart/update instance')
    p.add_option('-g', '--generate_queries',
                 default=False, action='store_true',
                 help='Generate 500 queries for certain fields')
    p.add_option('-S', '--save',
                 default='', action='store',
                 help='Save results into a folder: x')
    p.add_option('--google_spreadsheet',
                 default='', action='store',
                 help='Upload results into a Google spread sheet: x')
    p.add_option('--google_user',
                 default='', action='store',
                 help='Upload results into a Google spread sheet: username')
    p.add_option('--google_password',
                 default='', action='store',
                 help='Upload results into a Google spread sheet: password')
    p.add_option('-P', '--purge',
                 default=False, action='store_true',
                 help='Remove the test folder before running the test (if already exists)')
    
    
    # JMeter options specific to our .jmx test
    p.add_option('-s', '--serverName',
                 default='adswhy', action='store',
                 help='Machine we run test against, eg. adswhy')
    p.add_option('-p', '--serverPort',
                 default='', action='store',
                 help='Port, eg. 9000')
    p.add_option('-i', '--durationInSecs',
                 default=5, action='store', type='int',
                 help='How many seconds to run each test')
    p.add_option('-r', '--rampUpInSecs',
                 default=0, action='store', type='int',
                 help='How many seconds to warm up')
    p.add_option('-U', '--noOfUsers',
                 default=0, action='store', type='int',
                 help='How many seconds to warm up')
    
    return p





          

def check_prerequisities(options):
    jmeter = None
    try:
        jmeter = get_output(['which', 'jmeter'])
    except subprocess.CalledProcessError:
        pass
    
    if options.setup_jmeter or options.setup_prerequisities:
        setup_jmeter(options)
        jmeter = os.path.join(INSTDIR, '%s/jmeter/bin/jmeter' % _NAME)
    
    if jmeter:
        options.jmeter = jmeter
    else:
        error('Cannot find executable jmeter (is $PATH set correctly?)')
    
    try:
        req(options.query_endpoint + "/select")
    except:
        error('Cannot contact: %s' % options.query_endpoint)


def setup_jmeter(options):
    """
    On old systems, such as CentOS, jmeter binaries are useless
    """
    
    if os.path.exists('jmeter/RELEASE') and str(get_pid('jmeter/RELEASE')) == str(_RELEASE):
        return # already installed
    
    with open("install_jmeter.sh", "w") as build_ant:
        build_ant.write("""#!/bin/bash -e
        
        if [ -f apache-jmeter-2.9.tgz ]; then
          rm apache-jmeter-2.9.tgz
        fi
        
        if [ -d jmeter ]; then
          rm -fr jmeter
        fi
        
        wget -nc http://mirrors.gigenet.com/apache/jmeter/binaries/apache-jmeter-2.9.tgz
        tar -xzf apache-jmeter-2.9.tgz
        mv apache-jmeter-2.9 jmeter
        
        wget -nc http://jmeter-plugins.org/downloads/file/JMeterPlugins-Standard-1.1.1.zip
        wget -nc http://jmeter-plugins.org/downloads/file/JMeterPlugins-Extras-1.1.1.zip
        
        unzip -o JMeterPlugins-Standard-1.1.1.zip -d jmeter
        unzip -o JMeterPlugins-Extras-1.1.1.zip -d jmeter
        
        echo "%(release)s" > jmeter/RELEASE
        
        """ % {'release': _RELEASE})
    
    run_cmd(['chmod', 'u+x', 'install_jmeter.sh'])
    run_cmd(['./install_jmeter.sh'])
    

def update_montysolr(options):
    if options.update_command:
        run_cmd([options.invoke_update, '>', 'running_update_command.log'])
        
        
def generate_queries(options):
    # TODO: run the lucene java to generate queries?
    return 

def find_tests(options):
    if options.queries_pattern:
        if os.path.exists(options.queries_pattern):
            with changed_dir(options.queries_pattern):
                return glob.glob('*.queries')
        else:
            return glob.glob(options.queries_pattern)
    else:
        return glob.glob(os.path.join(INSTDIR, 'perpetuum/montysolr/contrib/examples/adsabs/jmeter/*.queries'))
    

def harvest_details_about_montysolr(options):
    system_data = req('%s/admin/system' % options.query_endpoint)
    mbeans_data = req('%s/admin/mbeans' % options.query_endpoint, stats='true')
    cores_data = req('%s/admin/cores' % options.query_endpoint, stats='true')
    
    cn = cores_data['defaultCoreName']
    ci = mbeans_data['solr-mbeans'].index('CORE')+1
    ch = mbeans_data['solr-mbeans'].index('QUERYHANDLER')+1
    cc = mbeans_data['solr-mbeans'].index('CACHE')+1
    
    
    out = dict(
        host = system_data['core']['host'],
        now = system_data['core']['now'],
        start = system_data['core']['start'],
        
        jvmVersion = system_data['jvm']['version'],
        jvmName = system_data['jvm']['name'],
        jvmProcessors = system_data['jvm']['processors'],
        jvmFree = system_data['jvm']['memory']['free'],
        jvmTotal = system_data['jvm']['memory']['total'],
        jvmMax = system_data['jvm']['memory']['max'],
        jvmUsed = system_data['jvm']['memory']['used'],
        jvmUsedRaw = system_data['jvm']['memory']['raw']['used'],
        jvmCommandLineArgs = ' '.join(system_data['jvm']['jmx']['commandLineArgs']),
        
        systemName = system_data['system']['name'],
        systemVersion = system_data['system']['version'],
        systemArch = system_data['system']['arch'],
        systemLoadAverage = system_data['system']['systemLoadAverage'],
        systemCommittedVirtualMemorySize = system_data['system']['committedVirtualMemorySize'],
        systemFreePhysicalMemorySize = system_data['system']['freePhysicalMemorySize'],
        systemFreeSwapSpaceSize = system_data['system']['freeSwapSpaceSize'],
        systemProcessCpuTime = system_data['system']['processCpuTime'],
        systemTotalPhysicalMemorySize = system_data['system']['totalPhysicalMemorySize'],
        systemTotalSwapSpaceSize = system_data['system']['totalSwapSpaceSize'],
        systemOpenFileDescriptorCount = system_data['system']['openFileDescriptorCount'],
        systemMaxFileDescriptorCount = system_data['system']['maxFileDescriptorCount'],
        systemUname = system_data['system']['uname'],
        systemUptime = system_data['system']['uptime'],
        
        
        solrSpecVersion = system_data['lucene']['solr-spec-version'],
        solrImplVersion = system_data['lucene']['solr-impl-version'],
        luceneSpecVersion = system_data['lucene']['lucene-spec-version'],
        luceneImplVersion = system_data['lucene']['lucene-impl-version'],
        
        instanceDir=cores_data['status'][cn]['instanceDir'],
        dataDir=cores_data['status'][cn]['dataDir'],
        startTime = cores_data['status'][cn]['startTime'],
        uptime = cores_data['status'][cn]['uptime'],
        indexNumDocs = cores_data['status'][cn]['index']['numDocs'],
        indexMaxDoc = cores_data['status'][cn]['index']['maxDoc'],
        indexVersion = cores_data['status'][cn]['index']['version'],
        indexSegmentCount = cores_data['status'][cn]['index']['segmentCount'],
        indexCurrent = cores_data['status'][cn]['index']['current'],
        indexHasDeletions = cores_data['status'][cn]['index']['hasDeletions'],
        indexDirectory = cores_data['status'][cn]['index']['directory'],
        indexLstModified = cores_data['status'][cn]['index']['lastModified'],
        indexSizeInBytes = cores_data['status'][cn]['index']['sizeInBytes'],
        indexSize = cores_data['status'][cn]['index']['size'],
        
        coreRefCount = mbeans_data['solr-mbeans'][ci]['core']['stats']['refCount'],
        
        searcherClass = mbeans_data['solr-mbeans'][ci]['searcher']['class'],
        searcherCaching = mbeans_data['solr-mbeans'][ci]['searcher']['stats']['caching'],
        searcherReader = mbeans_data['solr-mbeans'][ci]['searcher']['stats']['reader'],
        searcherOpenedAt = mbeans_data['solr-mbeans'][ci]['searcher']['stats']['openedAt'],
        searcherRegisteredAt = mbeans_data['solr-mbeans'][ci]['searcher']['stats']['registeredAt'],
        searcherWarmupTime = mbeans_data['solr-mbeans'][ci]['searcher']['stats']['warmupTime'],
        
        selectClass = mbeans_data['solr-mbeans'][ch]['/select']['class'],
        selectVersion = mbeans_data['solr-mbeans'][ch]['/select']['version'],
        selectDescription = mbeans_data['solr-mbeans'][ch]['/select']['description'],
        selectRequests = mbeans_data['solr-mbeans'][ch]['/select']['stats']['requests'],
        selectErrors = mbeans_data['solr-mbeans'][ch]['/select']['stats']['errors'],
        selectTimeouts = mbeans_data['solr-mbeans'][ch]['/select']['stats']['timeouts'],
        selectTotalTime = mbeans_data['solr-mbeans'][ch]['/select']['stats']['totalTime'],
        selectAvgTimePerRequest = mbeans_data['solr-mbeans'][ch]['/select']['stats']['avgTimePerRequest'],
        selectAvgRequestsPerSecond = mbeans_data['solr-mbeans'][ch]['/select']['stats']['avgRequestsPerSecond'],
        
        cacheQueryClass = mbeans_data['solr-mbeans'][cc]['queryResultCache']['class'],
        cacheQueryVersion = mbeans_data['solr-mbeans'][cc]['queryResultCache']['version'],
        cacheQueryDescription = mbeans_data['solr-mbeans'][cc]['queryResultCache']['description'],
        cacheQueryLookups = mbeans_data['solr-mbeans'][cc]['queryResultCache']['stats']['lookups'],
        cacheQueryHits = mbeans_data['solr-mbeans'][cc]['queryResultCache']['stats']['hits'],
        cacheQueryHitRatio = mbeans_data['solr-mbeans'][cc]['queryResultCache']['stats']['hitratio'],
        cacheQueryEvictions = mbeans_data['solr-mbeans'][cc]['queryResultCache']['stats']['evictions'],
        cacheQuerySize = mbeans_data['solr-mbeans'][cc]['queryResultCache']['stats']['size'],
        cacheQueryWarmupTime = mbeans_data['solr-mbeans'][cc]['queryResultCache']['stats']['warmupTime'],
        cacheQueryCumulativeLookups = mbeans_data['solr-mbeans'][cc]['queryResultCache']['stats']['cumulative_lookups'],
        cacheQueryCumulativeHits = mbeans_data['solr-mbeans'][cc]['queryResultCache']['stats']['cumulative_hits'],
        cacheQueryCumulativeHitRatio = mbeans_data['solr-mbeans'][cc]['queryResultCache']['stats']['cumulative_hitratio'],
        cacheQueryCumulativeInserts = mbeans_data['solr-mbeans'][cc]['queryResultCache']['stats']['cumulative_inserts'],
        cacheQueryCumulativeEvictions = mbeans_data['solr-mbeans'][cc]['queryResultCache']['stats']['cumulative_evictions'],
        
        cacheFieldClass = mbeans_data['solr-mbeans'][cc]['fieldCache']['class'],
        cacheFieldVersion = mbeans_data['solr-mbeans'][cc]['fieldCache']['version'],
        cacheFieldDescription = mbeans_data['solr-mbeans'][cc]['fieldCache']['description'],
        cacheFieldEntriesCount = mbeans_data['solr-mbeans'][cc]['fieldCache']['stats']['entries_count'],
        
        cacheDocumentClass = mbeans_data['solr-mbeans'][cc]['documentCache']['class'],
        cacheDocumentVersion = mbeans_data['solr-mbeans'][cc]['documentCache']['version'],
        cacheDocumentDescription = mbeans_data['solr-mbeans'][cc]['documentCache']['description'],
        cacheDocumentLookups = mbeans_data['solr-mbeans'][cc]['documentCache']['stats']['lookups'],
        cacheDocumentHits = mbeans_data['solr-mbeans'][cc]['documentCache']['stats']['hits'],
        cacheDocumentHitRatio = mbeans_data['solr-mbeans'][cc]['documentCache']['stats']['hitratio'],
        cacheDocumentEvictions = mbeans_data['solr-mbeans'][cc]['documentCache']['stats']['evictions'],
        cacheDocumentSize = mbeans_data['solr-mbeans'][cc]['documentCache']['stats']['size'],
        cacheDocumentWarmupTime = mbeans_data['solr-mbeans'][cc]['documentCache']['stats']['warmupTime'],
        cacheDocumentCumulativeLookups = mbeans_data['solr-mbeans'][cc]['documentCache']['stats']['cumulative_lookups'],
        cacheDocumentCumulativeHits = mbeans_data['solr-mbeans'][cc]['documentCache']['stats']['cumulative_hits'],
        cacheDocumentCumulativeHitRatio = mbeans_data['solr-mbeans'][cc]['documentCache']['stats']['cumulative_hitratio'],
        cacheDocumentCumulativeInserts = mbeans_data['solr-mbeans'][cc]['documentCache']['stats']['cumulative_inserts'],
        cacheDocumentCumulativeEvictions = mbeans_data['solr-mbeans'][cc]['documentCache']['stats']['cumulative_evictions'],
        
        cacheFieldValueClass = mbeans_data['solr-mbeans'][cc]['fieldValueCache']['class'],
        cacheFieldValueVersion = mbeans_data['solr-mbeans'][cc]['fieldValueCache']['version'],
        cacheFieldValueDescription = mbeans_data['solr-mbeans'][cc]['fieldValueCache']['description'],
        cacheFieldValueLookups = mbeans_data['solr-mbeans'][cc]['fieldValueCache']['stats']['lookups'],
        cacheFieldValueHits = mbeans_data['solr-mbeans'][cc]['fieldValueCache']['stats']['hits'],
        cacheFieldValueHitRatio = mbeans_data['solr-mbeans'][cc]['fieldValueCache']['stats']['hitratio'],
        cacheFieldValueEvictions = mbeans_data['solr-mbeans'][cc]['fieldValueCache']['stats']['evictions'],
        cacheFieldValueSize = mbeans_data['solr-mbeans'][cc]['fieldValueCache']['stats']['size'],
        cacheFieldValueWarmupTime = mbeans_data['solr-mbeans'][cc]['fieldValueCache']['stats']['warmupTime'],
        cacheFieldValueCumulativeLookups = mbeans_data['solr-mbeans'][cc]['fieldValueCache']['stats']['cumulative_lookups'],
        cacheFieldValueCumulativeHits = mbeans_data['solr-mbeans'][cc]['fieldValueCache']['stats']['cumulative_hits'],
        cacheFieldValueCumulativeHitRatio = mbeans_data['solr-mbeans'][cc]['fieldValueCache']['stats']['cumulative_hitratio'],
        cacheFieldValueCumulativeInserts = mbeans_data['solr-mbeans'][cc]['fieldValueCache']['stats']['cumulative_inserts'],
        cacheFieldValueCumulativeEvictions = mbeans_data['solr-mbeans'][cc]['fieldValueCache']['stats']['cumulative_evictions'],
        
        cacheFilterClass = mbeans_data['solr-mbeans'][cc]['filterCache']['class'],
        cacheFilterVersion = mbeans_data['solr-mbeans'][cc]['filterCache']['version'],
        cacheFilterDescription = mbeans_data['solr-mbeans'][cc]['filterCache']['description'],
        cacheFilterLookups = mbeans_data['solr-mbeans'][cc]['filterCache']['stats']['lookups'],
        cacheFilterHits = mbeans_data['solr-mbeans'][cc]['filterCache']['stats']['hits'],
        cacheFilterHitRatio = mbeans_data['solr-mbeans'][cc]['filterCache']['stats']['hitratio'],
        cacheFilterEvictions = mbeans_data['solr-mbeans'][cc]['filterCache']['stats']['evictions'],
        cacheFilterSize = mbeans_data['solr-mbeans'][cc]['filterCache']['stats']['size'],
        cacheFilterWarmupTime = mbeans_data['solr-mbeans'][cc]['filterCache']['stats']['warmupTime'],
        cacheFilterCumulativeLookups = mbeans_data['solr-mbeans'][cc]['filterCache']['stats']['cumulative_lookups'],
        cacheFilterCumulativeHits = mbeans_data['solr-mbeans'][cc]['filterCache']['stats']['cumulative_hits'],
        cacheFilterCumulativeHitRatio = mbeans_data['solr-mbeans'][cc]['filterCache']['stats']['cumulative_hitratio'],
        cacheFilterCumulativeInserts = mbeans_data['solr-mbeans'][cc]['filterCache']['stats']['cumulative_inserts'],
        cacheFilterCumulativeEvictions = mbeans_data['solr-mbeans'][cc]['filterCache']['stats']['cumulative_evictions'],
        )
    return out
    

def run_test(test, options):
    
    save_into_file('before-test.json', simplejson.dumps(harvest_details_about_montysolr(options)))
    
    # run the test, results will be summary_report.data
    run_cmd(['%(jmeter)s -n -t %(jmx_test)s %(jmx_args)s -l results.jtl -DqueryFile=%(query_file)s ' \
             '-DbaseDir=%(basedir)s'  % 
             dict(jmeter=options.jmeter,
                  jmx_test=options.jmx_test, jmx_args=options.jmx_args, query_file=test,
                  basedir=os.path.abspath('.'))])
    
    runtime = {}
    runtime.update(options.__dict__)
    runtime['google_password'] = 'XXX'
    
    save_into_file('runtime-env.json', simplejson.dumps(runtime))
    
    save_into_file('after-test.json', simplejson.dumps(harvest_details_about_montysolr(options)))
    
    
    
def tablify(csv_filepath):    
    with open(csv_filepath, 'r') as f:
        data = csv.reader(f)
        labels = data.next()
        return Table(*[Table.Column(x[0], tuple(x[1:])) for x in zip(labels, *list(data))])        
    
def generate_graphs(options):    
    # now generate various metrics/graphs from the summary
    reporter = '%(java)s -jar %(jmeter_base)s/lib/ext/CMDRunner.jar --tool Reporter' \
               ' --input-jtl summary_report.data' % dict(java=options.java, 
                                                     jmeter_base=os.path.abspath(options.jmeter + '/../..'))
               
    
    ## 
    run_cmd([reporter, '--plugin-type AggregateReport --generate-csv aggregate-report.csv'])
    
    
    run_cmd([reporter, '--plugin-type BytesThroughputOverTime --generate-png bytes-throughput-over-time.png'])
    run_cmd([reporter, '--plugin-type BytesThroughputOverTime --generate-csv bytes-throughput-over-time.csv'])
    
    # the same info is at response-codes-per-sec including the number of failed requests
    #run_cmd([reporter, '--plugin-type HitsPerSecond --generate-png hits-per-sec.png'])
    #run_cmd([reporter, '--plugin-type HitsPerSecond --generate-csv hits-per-sec.csv'])
    
    run_cmd([reporter, '--plugin-type LatenciesOverTime --generate-png latencies-over-time.png'])
    run_cmd([reporter, '--plugin-type LatenciesOverTime --generate-csv latencies-over-time.csv'])
    
    run_cmd([reporter, '--plugin-type ResponseCodesPerSecond --generate-png response-codes-per-sec.png'])
    run_cmd([reporter, '--plugin-type ResponseCodesPerSecond --generate-csv response-codes-per-sec.csv'])
    
    # histogram of number of responses that fit in 100ms, 1s, 10s,
    run_cmd([reporter, '--plugin-type ResponseTimesDistribution --generate-png response-times-distribution-10.png --granulation 10'])
    run_cmd([reporter, '--plugin-type ResponseTimesDistribution --generate-png response-times-distribution-100.png --granulation 100'])
    run_cmd([reporter, '--plugin-type ResponseTimesDistribution --generate-png response-times-distribution-1000.png --granulation 1000'])
    
    run_cmd([reporter, '--plugin-type ResponseTimesDistribution --generate-csv response-times-distribution-10.csv --granulation 10'])
    run_cmd([reporter, '--plugin-type ResponseTimesDistribution --generate-csv response-times-distribution-100.csv --granulation 100'])
    run_cmd([reporter, '--plugin-type ResponseTimesDistribution --generate-csv response-times-distribution-1000.csv --granulation 1000'])
    
    # time series of #no of responses during test
    run_cmd([reporter, '--plugin-type ResponseTimesOverTime  --generate-png response-times-over-time-10.png --granulation 100'])
    run_cmd([reporter, '--plugin-type ResponseTimesOverTime  --generate-png response-times-over-time-100.png --granulation 1000'])
    run_cmd([reporter, '--plugin-type ResponseTimesOverTime  --generate-png response-times-over-time-1000.png --granulation 10000'])
    
    run_cmd([reporter, '--plugin-type ResponseTimesOverTime  --generate-csv response-times-over-time-10.csv --granulation 100'])
    run_cmd([reporter, '--plugin-type ResponseTimesOverTime  --generate-csv response-times-over-time-100.csv --granulation 1000'])
    run_cmd([reporter, '--plugin-type ResponseTimesOverTime  --generate-csv response-times-over-time-1000.csv --granulation 10000'])
    
    
    run_cmd([reporter, '--plugin-type ResponseTimesPercentiles  --generate-png response-times-percentiles.png'])
    run_cmd([reporter, '--plugin-type ResponseTimesPercentiles  --generate-csv response-times-percentiles.csv'])
    
    #run_cmd([reporter, '--plugin-type ThroughputOverTime  --generate-png throughput-over-time.png'])
    #run_cmd([reporter, '--plugin-type ThroughputOverTime  --generate-csv throughput-over-time.csv'])
    
    run_cmd([reporter, '--plugin-type ThroughputVsThreads  --generate-png throughput-vs-threads.png'])
    run_cmd([reporter, '--plugin-type ThroughputVsThreads  --generate-csv throughput-vs-threads.csv'])
    
    run_cmd([reporter, '--plugin-type TimesVsThreads  --generate-png times-vs-threads.png'])
    run_cmd([reporter, '--plugin-type TimesVsThreads  --generate-csv times-vs-threads.csv'])
    
    run_cmd([reporter, '--plugin-type TransactionsPerSecond  --generate-png transactions-per-sec.png'])
    run_cmd([reporter, '--plugin-type TransactionsPerSecond  --generate-csv transactions-per-sec.csv'])
    
    run_cmd([reporter, '--plugin-type PageDataExtractorOverTime  --generate-png page-data-extractor-over-time.png'])
    run_cmd([reporter, '--plugin-type PageDataExtractorOverTime  --generate-csv page-data-extractor-over-time.csv'])
    
    
    
    

def harvest_results(test_name, results):
    aggregate = tablify('aggregate-report.csv')
    percentiles = tablify('response-times-percentiles.csv')
    print aggregate.get_col_byname('average')[0]
    print aggregate.get_col_byname('aggregate_report_stddev')[0]
    print aggregate.get_col_byname('aggregate_report_count')[0]
    print percentiles.get_row_byvalue("Percentiles", "95.0")[1]
    print percentiles.get_row_byvalue("Percentiles", "98.0")[1]
    print percentiles.get_row_byvalue("Percentiles", "99.0")[1]
    
    
     


def save_results(options, results):
    pass


def upload_results(options, results):
    pass

def save_into_file(path, value):
    fo = open(path, 'w')
    fo.write(str(value))
    fo.close()

def generate_html(test_name, options, ):
    with open(options.script_home + '/test-view.tmpl', 'r') as t:
        tmpl = t.read()
    kwargs = {}
    kwargs.update(options.__dict__)
    kwargs['test_name'] = test_name
    
    before_test = simplejson.load(open('before-test.json', 'r'))
    after_test = simplejson.load(open('after-test.json', 'r'))
    
    for k,v in after_test.items():
        if before_test[k] != v:
            after_test[k] = '<b>%s</b>' % v
    
    runtime_env = simplejson.load(open('runtime-env.json', 'r'))
    
    kwargs.update(before_test)
    kwargs['before_test'] = pformat(before_test)
    kwargs['after_test'] = pformat(after_test)
    kwargs['runtime_env'] = pformat(runtime_env)
    kwargs['aggregate_report'] = str(tablify('aggregate-report.csv'))
    kwargs['transactions_per_sec'] = str(tablify('transactions-per-sec.csv'))
    kwargs['response_codes_per_sec'] = str(tablify('response-codes-per-sec.csv'))
    kwargs['bytes_throughput_over_time'] = str(tablify('bytes-throughput-over-time.csv'))
    kwargs['latencies_over_time'] = str(tablify('latencies-over-time.csv'))
    kwargs['response_times_distribution_10'] = str(tablify('response-times-distribution-10.csv'))
    kwargs['response_times_distribution_100'] = str(tablify('response-times-distribution-100.csv'))
    kwargs['response_times_distribution_1000'] = str(tablify('response-times-distribution-1000.csv'))
    kwargs['response_times_over_time_10'] = str(tablify('response-times-over-time-10.csv'))
    kwargs['response_times_over_time_100'] = str(tablify('response-times-over-time-100.csv'))
    kwargs['response_times_over_time_1000'] = str(tablify('response-times-over-time-1000.csv'))
    kwargs['response_times_percentiles'] = str(tablify('response-times-percentiles.csv'))
    kwargs['throughput_vs_threads'] = str(tablify('throughput-vs-threads.csv'))
    kwargs['times_vs_threads'] = str(tablify('times-vs-threads.csv'))
    
    kwargs['jvmCommandLineArgs'] = '\n'.join(kwargs['jvmCommandLineArgs'].split())
    
    with open('index.html', 'w') as w:
        w.write(tmpl % kwargs)
    
    
    
    

class JMeterResults(object):
    def __init__(self):
        pass              

class Table:
    def __init__(self, *columns):
        self.columns = columns
        self.length = max(len(col.data) for col in columns)
    def get_col_byname(self, name):
        for col in self.columns:
            if col.name == name:
                return col.data
    def get_row_byvalue(self, column_name, value):
        data = []
        for col in self.columns:
            if col.name == column_name:
                for val, i in zip(col.data, range(len(col.data))):
                    if val == value:
                        for col in self.columns:
                            data.append(col.data[i])
        return data
                            
    def get_row(self, rownum=None):
        for col in self.columns:
            if rownum is None:
                yield col.format % col.name
            else:
                yield col.format % col.data[rownum]
    def get_line(self):
        for col in self.columns:
            yield '-' * (col.width + 2)
    def join_n_wrap(self, char, elements):
        return ' ' + char + char.join(elements) + char
    def get_rows(self):
        yield self.join_n_wrap('+', self.get_line())
        yield self.join_n_wrap('|', self.get_row(None))
        yield self.join_n_wrap('+', self.get_line())
        for rownum in range(0, self.length):
            yield self.join_n_wrap('|', self.get_row(rownum))
        yield self.join_n_wrap('+', self.get_line())
    def __str__(self):
        return '\n'.join(self.get_rows())
    class Column():
        LEFT, RIGHT = '-', ''
        def __init__(self, name, data, align=RIGHT):
            self.data = data
            self.name = name
            self.width = max(len(name), max(len(x) for x in data))
            self.format = ' %%%s%ds ' % (align, self.width)


def main(argv):
    
    check_basics()
    
    if not os.path.exists(os.path.join(INSTDIR, _NAME)):
        run_cmd(['mkdir', os.path.join(INSTDIR, _NAME)])
    
    
    with changed_dir(os.path.join(INSTDIR, _NAME)):
        
        update_pid = get_pid('update.pid')
        if update_pid != -1 and check_pid_is_running(update_pid):
            error("The script is already running with pid: %s" % update_pid)
        
        acquire_lock('update.pid')
        
        parser = get_arg_parser()
        options, args = parser.parse_args(argv)
        check_options(options, args)
        
        print "============="
        for k,v in options.__dict__.items():
            if 'password' in k:
                print '%s=%s' % (k, 'xxx')
            else:
                print '%s=%s' % (k, v)
        print 'args=', args
        print "============="
        
        # install pre-requisities if requested
        check_prerequisities(options)
        
        if options.update_command:
            update_montysolr(options)
        
        if options.generate_queries:
            generate_queries(options)
        
        if len(args) > 1:
            tests = args[1:]
        else:
            tests = find_tests(options)
        
        if len(tests) == 0: 
            error('no test name(s) supplied nor found in: %s' % options.queries_pattern)
            
        results = JMeterResults()
        
        before_test = harvest_details_about_montysolr(options)
        
        if options.save:
            save_into_file('before-test.json', simplejson.dumps(before_test))
        
        for test in tests:
            
            test_name = os.path.basename(test)
            test_dir = test_name + '_results'
            
            if options.purge and os.path.exists(test_dir):
                run_cmd(['rm', '-fr', test_dir])
                
            if not os.path.exists(test_dir):
                run_cmd(['mkdir', test_dir])
            
            with changed_dir(test_dir):
                
                
                #run_test(test, options)
                #generate_graphs(options)
                generate_html(test_name, options)
                harvest_results(test_name, results)
            
            
            if options.save:
                save_results(options, results)
                
            if options.upload:
                upload_results(options, results)
            
        
        remove_lock('update.pid')    
    

if __name__ == '__main__':
    main(sys.argv)
