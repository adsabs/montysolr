PYTHONPATH=${build.dir}/${example.name}/python${path.separator}$PYTHONPATH
MONTYSOLR_JVMARGS=-d64 -Xmx2048m -Djava.util.logging.config.file=${build.dir}/${example.name}/etc/logging.properties -DstoreAll=true
MONTYSOLR_ARGS=
