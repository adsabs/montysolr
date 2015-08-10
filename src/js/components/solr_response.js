/**
 * Created by rchyla on 3/10/14.
 */

/*
 * Subclass of the JSON response - it understands the JSON object as returned
 * by SOLR.
 */


define([
  'js/components/json_response',
  'js/components/solr_params',
  'backbone',
  'underscore',
  'jquery'], function(
  JsonResponse,
  SolrParams,
  Backbone,
  _,
  $) {


  var SolrResponse = JsonResponse.extend({

    initialize: function() {
      if (!this.has('responseHeader.params')) {
        throw new Error("SOLR data error - missing: responseHeader.params");
      }
      if (_.isString(this._url)) { //TODO: this seems ugly, relying on the parent for values
        var p = new SolrParams();
        this._url = new SolrParams(p.parse(this._url)).url();
      }
      else {
        var queryParams = this.get('responseHeader.params');
        this._url = new SolrParams(queryParams).url();
      }
    },
    url: function(resp, options) {
      return this._url;
    }

  });

  return SolrResponse;
});

