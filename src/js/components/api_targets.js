/**
 * Created by rchyla on 1/20/15
 */

define([
    'underscore',
    'backbone'
  ],
  function(
    _,
    Backbone
    ) {

    return {
      SEARCH: 'search/query',
      QTREE: 'search/qtree',
      BIGQUERY: 'search/bigquery',
      EXPORT: 'export/',
      SERVICE_AUTHOR_NETWORK: 'vis/author-network',
      SERVICE_PAPER_NETWORK: 'vis/paper-network',
      SERVICE_WORDCLOUD: 'vis/word-cloud',
      SERVICE_METRICS: 'metrics',
      MYADS_STORAGE: 'http://localhost:5000',

      CSRF : 'accounts/csrf',
      USER: 'accounts/user',
      USER_DATA: 'vault/user-data',
      OPENURL_CONFIGURATION : 'vault/configuration/link_servers',
      TOKEN:'accounts/token',
      LOGOUT: 'accounts/logout',
      REGISTER: 'accounts/register',
      VERIFY : 'accounts/verify',
      DELETE: 'accounts/user/delete',
      RESET_PASSWORD: 'accounts/reset-password',
      CHANGE_PASSWORD: 'accounts/change-password',
      CHANGE_EMAIL: 'accounts/change-email',

      RECOMMENDER : 'recommender',
      GRAPHICS: 'graphics',

      //library endpoints
      //can get info about all libraries, or list of bibcodes associated w/specific lib (libraries/id)
      //post to /libraries/ to create a library
      LIBRARIES : "biblib/libraries",
      //can post, put, and delete changes to individual libs using this endpoint
      DOCUMENTS : "biblib/documents",
      PERMISSIONS : "biblib/permissions"

    };
  });
