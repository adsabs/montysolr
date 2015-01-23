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
      SERVICE_AUTHOR_NETWORK: 'author-network',
      SERVICE_PAPER_NETWORK: 'paper-network',
      SERVICE_WORDCLOUD: 'word-cloud',
      SERVICE_METRICS: 'metrics'
    };
  });
