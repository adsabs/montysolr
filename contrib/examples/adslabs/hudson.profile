PYTHONPATH=$PYTHONPATH
MONTYSOLR_HANDLER=montysolr.sequential_handler
MONTYSOLR_TARGETS=adslabs.targets,adslabs.schema.targets
MONTYSOLR_MAX_WORKERS=4
MONTYSOLR_JVMARGS=-d64 -Xmx2048m -Dmontysolr.max_workers=4 -Dmontysolr.max_threads=200 -Dsolr.cache.size=0 -Dsolr.cache.initial=0 -Djava.util.logging.config.file=${build.dir}/${example.name}/etc/logging.properties 
MONTYSOLR_ARGS=--port 8984
