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
      SERVICE_AUTHOR_NETWORK: '/vis/author-network',
      SERVICE_PAPER_NETWORK: '/vis/paper-network',
      SERVICE_WORDCLOUD: '/vis/word-cloud',
      SERVICE_METRICS: 'metrics'
    };
  });
