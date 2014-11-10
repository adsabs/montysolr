PYTHONPATH=${build.dir}/${example.name}/python${path.separator}$PYTHONPATH
MONTYSOLR_JVMARGS=-d64 -Xmx20480m -DstoreAll=true -Dmontysolr.enable.write=false -Dmontysolr.enable.warming=true -Djetty.port=8984 -Djava.util.logging.config.file=${build.dir}/${example.name}/etc/logging.properties -Dmontysolr.locktype=native -XX:+AggressiveOpts -XX:+UseG1GC -XX:+UseStringCache -XX:+OptimizeStringConcat -XX:-UseSplitVerifier -XX:+UseNUMA -XX:MaxGCPauseMillis=50 -XX:GCPauseIntervalMillis=1000   
MONTYSOLR_ARGS=
