PYTHONPATH=${build.dir}/${example.name}/python${path.separator}$PYTHONPATH
MONTYSOLR_JVMARGS=-d64 -Xmx16084m -Dsolr.cache.size=12 -Dsolr.cache.initial=0 -DstoreAll=true -Dmontysolr.enable.write=true -Dmontysolr.enable.warming=false -Djetty.port=8984 -Djava.util.logging.config.file=${build.dir}/${example.name}/etc/logging.properties  
MONTYSOLR_ARGS=
