'use strict';
/**
 * Options for the `env` grunt task
 *
 * Sets up some environmental variables
 * These are important only for the express task (webserver)
 *
 * @module grunt/env
 */
module.exports = {
  options: {
    SOLR_ENDPOINT: '<%= local.solr_endpoint || "http://localhost:9000/solr/select" %>',
    API_ENDPOINT: '<%= local.api_endpoint || "http://localhost:5000/api/1" %>',
    ORCID_OAUTH_CLIENT_ID: '<%= local.orcid_oauth_cliend_id || "" %>',
    ORCID_OAUTH_CLIENT_SECRET:'<%= local.orcid_oauth_client_secret || "" %>',
    ORCID_API_ENDPOINT :'<%= local.orcid_api_endpoint || "" %>'
  },
  dev: {
    HOMEDIR: 'src'
  },
  release: {
    HOMEDIR: 'dist'
  }
};
