PYTHONPATH=/opt/invenio/lib/python:${common.dir}/build/dist:${common.dir}/src/python:${common.dir}/contrib/invenio/src/python
MONTYSOLR_HANDLER=montysolr.sequential_handler
MONTYSOLR_TARGETS=monty_invenio.targets,monty_invenio.schema.targets
MONTYSOLR_MAX_WORKERS=4
MONTYSOLR_JVMARGS=-d64 -Xmx1024m -Dmontysolr.max_workers=4 -Dmontysolr.max_threads=200 -Dsolr.cache.size=0 -Dsolr.cache.initial=0
MONTYSOLR_ARGS=--daemon