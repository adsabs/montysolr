
/*
Subclass of the multi-param with a functionality specific for
SOLR queries. Do not use this class directly inside your app!
Instead, import 'api_query' and configure it properly

 * <p>For a list of possible parameters, please consult the links below.</p>
 *
 * @see http://wiki.apache.org/solr/CoreQueryParameters
 * @see http://wiki.apache.org/solr/CommonQueryParameters
 * @see http://wiki.apache.org/solr/SimpleFacetParameters
 * @see http://wiki.apache.org/solr/HighlightingParameters
 * @see http://wiki.apache.org/solr/MoreLikeThis
 * @see http://wiki.apache.org/solr/SpellCheckComponent
 * @see http://wiki.apache.org/solr/StatsComponent
 * @see http://wiki.apache.org/solr/TermsComponent
 * @see http://wiki.apache.org/solr/TermVectorComponent
 * @see http://wiki.apache.org/solr/LocalParams
 *
 * @param properties A map of fields to set. Refer to the list of public fields.
 * @class ParameterStore
 */


define(['js/components/multi_params', 'backbone', 'underscore', 'jquery'], function(MultiParams, Backbone, _, $) {


  var SolrParams = MultiParams.extend({

    fieldsToConcatenate: ['q'],
    defaultOperator: ' AND ',
    fieldProcessors: {
      '*': function(vals, self) {
        return [vals.join(self.defaultOperator)];
      }
    },


    url: function(resp, options) {

      // first massage the fields, but do not touch the original values
      // lodash has a parameter isDeep that can be set to true, but
      // for compatibility reasons with underscore, lets' not use it
      // the values should always be only one level deep
      var values = _.clone(this.attributes);

      var l = this.fieldsToConcatenate.length, k = '';

      for (var i=0; i < l; i++) {
        k = this.fieldsToConcatenate[i];

        if (this.has(k)) {
          if (this.fieldProcessors[k]) {
            values[k] = this.fieldProcessors[k](this.get(k), this);
          }
          else {
            values[k] = this.fieldProcessors['*'](this.get(k), this);
          }
        }
      }
      // then call the default implementation of the url handling
      return MultiParams.prototype.url.call(this, values);
    }

  });

  return SolrParams;
});
