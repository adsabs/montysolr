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