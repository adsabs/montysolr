PYTHONPATH=${build.dir}/${example.name}/python${path.separator}$PYTHONPATH
MONTYSOLR_JVMARGS=-d64 -Xmx2048m -Dsolr.cache.size=0 -Dsolr.cache.initial=0 -agentlib:jdwp=transport=dt_socket,server=y,address=8000 -Dcom.sun.management.jmxremote.port=3000 -Djava.rmi.server.hostname=131.142.185.40 -Dcom.sun.management.jmxremote.authenticate=false -Dcom.sun.management.jmxremote.ssl=false -DstoreAll=true
MONTYSOLR_ARGS=
