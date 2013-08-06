PYTHONPATH=${build.dir}/${example.name}/python${path.separator}$PYTHONPATH
MONTYSOLR_HANDLER=montysolr.sequential_handler
MONTYSOLR_TARGETS=monty_invenio.targets,monty_invenio.schema.targets
MONTYSOLR_MAX_WORKERS=4
MONTYSOLR_JVMARGS=-d64 -Xmx20480m -Dmontysolr.max_workers=4 -Dmontysolr.max_threads=400 -DstoreAll=true -Dmontysolr.enable.write=false -Dmontysolr.enable.warming=true -Djava.util.logging.config.file=${build.dir}/${example.name}/etc/logging.properties -Dmontysolr.locktype=native -XX:+AggressiveOpts -XX:+UseG1GC -XX:+UseStringCache -XX:+OptimizeStringConcat -XX:-UseSplitVerifier -XX:+UseNUMA -XX:MaxGCPauseMillis=50 -XX:GCPauseIntervalMillis=1000   
MONTYSOLR_ARGS=--daemon --port 8984
