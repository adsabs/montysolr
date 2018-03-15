'use strict';
/**
 * Options for the `exec` grunt task
 *
 * @module grunt/exec
 */
module.exports = {
  // this is necessary to make the library AMD compatible
  convert_dsjslib: {
    cmd: 'node node_modules/requirejs/bin/r.js -convert src/libs/dsjslib src/libs/dsjslib'
  },
  latest_commit: {
    cmd: 'git rev-parse --short=7 --verify HEAD | cat > git-latest-commit'
  },
  latest_tag: {
    cmd: 'git describe --abbrev=0 | cat > git-latest-release-tag'
  },
  git_describe: {
    cmd: 'git describe | cat > git-describe'
  },
  npm_install: {
    cmd: 'npm install --no-package-lock --no-shrinkwrap'
  },
  convert_enzyme: {
    cmd: "mkdir src/libs/enzyme && ./node_modules/.bin/browserify --standalone enzyme -x 'react/addons' -x 'react/lib/ReactContext' -x 'react/lib/ExecutionEnvironment' node_modules/enzyme/build/index.js > src/libs/enzyme/enzyme.js"
  },
  'nyc-instrument': {
    cmd: 'node_modules/.bin/nyc instrument src/js/ test/coverage/instrument'
  },
  'coveralls-report': {
    cmd: 'cat test/coverage/reports/lcov/lcov.info | ./node_modules/coveralls/bin/coveralls.js'
  },
  'server': 'node server'
};
