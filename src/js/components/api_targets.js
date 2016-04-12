/**
 * Created by rchyla on 1/20/15
 *
 * contains api targets
 * and any related limits
 */

define([
    'underscore',
    'backbone'
  ],
  function(
    _,
    Backbone
    ) {

   var config =  {
      BOOTSTRAP : '/accounts/bootstrap',
      SEARCH: 'search/query',
      QTREE: 'search/qtree',
      BIGQUERY: 'search/bigquery',
      EXPORT: 'export/',
      SERVICE_AUTHOR_NETWORK: 'vis/author-network',
      SERVICE_PAPER_NETWORK: 'vis/paper-network',
      SERVICE_WORDCLOUD: 'vis/word-cloud',
      SERVICE_METRICS: 'metrics',
      MYADS_STORAGE: 'vault',

      CSRF : 'accounts/csrf',
      USER: 'accounts/user',
      USER_DATA: 'vault/user-data',
      SITE_CONFIGURATION : 'vault/configuration',
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
      FEEDBACK : 'feedback/slack',

     //library import from classic
     LIBRARY_IMPORT_CLASSIC_AUTH : 'harbour/auth/classic',
     LIBRARY_IMPORT_CLASSIC_MIRRORS : 'harbour/mirrors',
     LIBRARY_IMPORT_CLASSIC_TO_BBB: 'biblib/classic',

     //library import from 2.0
     LIBRARY_IMPORT_ADS2_AUTH : 'harbour/auth/twopointoh',
     LIBRARY_IMPORT_ADS2_TO_BBB: 'biblib/twopointoh',

     LIBRARY_IMPORT_ZOTERO: "harbour/export/twopointoh/zotero",
     LIBRARY_IMPORT_MENDELEY: "harbour/export/twopointoh/mendeley",

     //returns credentials from both classic and 2.0 if they exist
     LIBRARY_IMPORT_CREDENTIALS: 'harbour/user',

     //store ADS information connected with ORCID here
      ORCID_PREFERENCES : 'orcid/preferences',

      //library endpoints
      //can get info about all libraries, or list of bibcodes associated w/specific lib (libraries/id)
      //post to /libraries/ to create a library
      LIBRARIES : "biblib/libraries",
      //can post, put, and delete changes to individual libs using this endpoint
      DOCUMENTS : "biblib/documents",
      PERMISSIONS : "biblib/permissions",


      /*
      * this is used by the mixin 'user_change_rows' to set max allowed/default requested
      */


     _limits  : {
      //use the same name from discovery.config.js

        ExportWidget : {
          default : 500, limit : 3000
        },

        Metrics : {
          default : 1000, limit : 2000
        },

        AuthorNetwork : {
          default : 400, limit : 1000
        },

        PaperNetwork : {
          default : 400, limit : 1000
        },

        ConceptCloud : {
          default : 150, limit : 150
        },

        BubbleChart : {
          //default == limit
          default : 1500
        }
      }

    };

    //add credential info

    //doesn't require cross domain cookies
    config._doesntNeedCredentials = [

        config.SEARCH,
        config.QTREE,
        config.BIGQUERY,
        config.EXPORT,
        config.SERVICE_AUTHOR_NETWORK,
        config.SERVICE_PAPER_NETWORK,
        config.SERVICE_WORDCLOUD,
        config.SERVICE_METRICS,
        config.RECOMMENDER,
        config.GRAPHICS,
        config.FEEDBACK

    ];


    return config;



  });
