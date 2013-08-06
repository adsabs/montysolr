PYTHONPATH=${build.dir}/${example.name}/python${path.separator}$PYTHONPATH
MONTYSOLR_HANDLER=montysolr.sequential_handler
MONTYSOLR_TARGETS=monty_invenio.targets,monty_invenio.schema.targets
MONTYSOLR_MAX_WORKERS=4
MONTYSOLR_JVMARGS=-d64 -Xmx6084m -Dmontysolr.max_workers=4 -Dmontysolr.max_threads=200 -Dsolr.cache.size=12 -Dsolr.cache.initial=0 -DstoreAll=true -Dmontysolr.enable.write=true -Dmontysolr.enable.warming=false -Djava.util.logging.config.file=${build.dir}/${example.name}/etc/logging.properties -XX:+AggressiveOpts -XX:+UseG1GC -XX:+UseStringCache -XX:+OptimizeStringConcat -XX:-UseSplitVerifier -XX:+UseNUMA -XX:MaxGCPauseMillis=50 -XX:GCPauseIntervalMillis=1000  
MONTYSOLR_ARGS=--daemon --port 8984
