PYTHONPATH=${build.dir}/${example.name}/python${path.separator}$PYTHONPATH${path.separator}/opt/invenio/lib/python
MONTYSOLR_HANDLER=montysolr.sequential_handler
MONTYSOLR_TARGETS=monty_invenio.targets,monty_invenio.schema.targets,adsabs.targets
MONTYSOLR_MAX_WORKERS=4
MONTYSOLR_JVMARGS=-d64 -Xmx2048m -Dmontysolr.max_workers=4 -Dmontysolr.max_threads=200 -Dsolr.cache.size=0 -Dsolr.cache.initial=0 -DstoreAll=true  
MONTYSOLR_ARGS=--daemon --port 8984
