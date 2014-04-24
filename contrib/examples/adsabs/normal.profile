PYTHONPATH=${build.dir}/${example.name}/python${path.separator}$PYTHONPATH
MONTYSOLR_JVMARGS=-d64 -Xmx2048m -Dsolr.cache.size=0 -Dsolr.cache.initial=0 -DstoreAll=true -Djetty.port=8984  
MONTYSOLR_ARGS=
