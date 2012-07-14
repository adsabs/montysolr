
## Show error messages and log prints
MONTYSOLR_BUGDEBUG = True

## Reload handler code on every invocation
MONTYSOLR_KILLLOAD = False

## Default bridge that is used for projects
MONTYSOLR_HANDLER = 'montysolr.sequential_handler'

## List of modules where we load MontySolr targets
MONTYSOLR_TARGETS = [] #'montysolr.inveniopie.targets', 'montysolr.examples.twitter_test']

# use the multiprocessing version of api_calls, value -1 means: detect number of cpus
MONTYSOLR_MAX_WORKERS = 0

# will be appended to jvm args when starting the virtual machine (Java inside Python)
MONTYSOLR_JVMARGS_PYTHON = ''






# End of the configuration; below is code that updates the current
# config based on the environmental variables. This is the mechanism
# used for communication of settings between Java<->Python


import sys 
import os 

def update_values():
    '''We'll check all MONTYSOLR variables for their counterparts in
    the environment and update the config if they are found. Note, that 
    doing this, we include even variables that are not defined in
    config
    '''
    main = sys.modules[__name__]
    for var in dir(main):
        if 'MONTYSOLR' in var:
            if os.getenv(var, None):
                val = os.getenv(var) # always a string
                if ',' in val:
                    val = val.split(',')
                elif val.lower() == 'true':
                    val = True
                elif val.lower() == 'false':
                    val = False
                    
                setattr(main, var, val)
                
update_values()

#sys.stderr.write("\n".join([str(sys.path), 'prefix', sys.prefix, 'executable', sys.executable, 'exec_prefix', sys.exec_prefix]))

import logging 

logging_level = logging.DEBUG
log = None
_loggers = []

def get_logger(name):
    """Creates a logger for you - with the parent newseman logger and
    common configuration"""
    if name[0:8] != 'montysol' and len(name) > 8:
        sys.stderr.write("Warning: you are creating a logger without 'montysolr' as a root (%s),"
        "this means that it will not share montysolr settings and cannot be administered from one place" % name)
    if log:
        logger = log.manager.getLogger(name)
    else:
        logger = logging.getLogger(name)
        hdlr = logging.StreamHandler(sys.stderr)
        formatter = logging.Formatter('%(levelname)s %(asctime)s %(name)s:%(lineno)d    %(message)s')
        hdlr.setFormatter(formatter)
        logger.addHandler(hdlr)
        logger.setLevel(logging_level)
        logger.propagate = 0
    if logger not in _loggers:
        _loggers.append(logger)
    return logger