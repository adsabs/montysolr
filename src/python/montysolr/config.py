
## Show error messages and log prints
MONTYSOLR_BUGDEBUG = True

## Reload handler code on every run
MONTYSOLR_KILLLOAD = True

## Default bridge that is used for projects
MONTYSOLR_HANDLER = 'montysolr.sequential_handler'

## List of modules where we load MontySolr targets
MONTYSOLR_TARGETS = ['montysolr.examples.twitter_test'] #'montysolr.inveniopie.targets', 'montysolr.examples.twitter_test']

# use the multiprocessing version of api_calls
MONTYSOLR_MULTIPROC = False

# will be appended to jvm args when starting the virtual machine (Java inside Python)
MONTYSOLR_JVMARGS_PYTHON = ''

# when multiprocessing is activated, how many workers shall be started
MONTYSOLR_MAX_WORKERS = 2




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
