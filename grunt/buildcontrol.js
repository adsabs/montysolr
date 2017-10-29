'use strict';
/**
 * Options for the `buildcontrol` grunt task
 * This task pushes compiled assets to the bbb-builds repo
 *
 * @module grunt/buildcontrol
 */
module.exports = {
  options: {
    dir: 'dist',
    commit: true,
    push: true,
    force: true,
    connectCommits: false,
    shallowFetch: true,
    message: 'Built %sourceName% from commit %sourceCommit% on branch %sourceBranch%'
  },
  kubernetes: {
    options: {
      remote: 'git@github.com:adsabs/bbb-builds.git',
      branch: 'kubernetes',
      message: 'Built %sourceName% from commit %sourceCommit% on branch kubernetes'
    }
  },
  master: {
    options: {
      remote: 'git@github.com:adsabs/bbb-builds.git',
      branch: 'master'
    }
  }
};
