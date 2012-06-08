PYTHONPATH=${build.dir}/${example.name}/python${path.separator}$PYTHONPATH:/opt/invenio/lib/python
MONTYSOLR_HANDLER=montysolr.sequential_handler
MONTYSOLR_TARGETS=monty_invenio.targets,monty_invenio.schema.targets
MONTYSOLR_MAX_WORKERS=4
MONTYSOLR_JVMARGS=-d64 -Xmx2048m -Dmontysolr.max_workers=4 -Dmontysolr.max_threads=200 -Dsolr.cache.size=0 -Dsolr.cache.initial=0 -agentlib:jdwp=transport=dt_socket,server=y,address=8000 -Djava.library.path=/proj/adsate/hudson/hudson/jobs/00-JCC/workspace/dist -Dcom.sun.management.jmxremote.port=3000 -Djava.rmi.server.hostname=131.142.185.40 -Dcom.sun.management.jmxremote.authenticate=false -Dcom.sun.management.jmxremote.ssl=false -DstoreAll=true
MONTYSOLR_ARGS=--daemon --port 8984
