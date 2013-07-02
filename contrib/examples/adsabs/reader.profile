PYTHONPATH=${build.dir}/${example.name}/python${path.separator}$PYTHONPATH
MONTYSOLR_HANDLER=montysolr.sequential_handler
MONTYSOLR_TARGETS=monty_invenio.targets,monty_invenio.schema.targets
MONTYSOLR_MAX_WORKERS=4
MONTYSOLR_JVMARGS=-d64 -Xmx20480m -Dmontysolr.max_workers=4 -Dmontysolr.max_threads=400 -Dsolr.cache.size=0 -Dsolr.cache.initial=0 -DstoreAll=true -Dmontysolr.enable.write=false -Dmontysolr.enable.warming=true -Dmontysolr.locktype:single -Djava.util.logging.config.file=${build.dir}/${example.name}/etc/logging.properties   
MONTYSOLR_ARGS=--daemon --port 8984
