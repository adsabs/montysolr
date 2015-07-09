/**
 * Helper class that extends LoT - it is used by widgets that display details
 * of a paper (one identifier search)
 */

define([
  './widget',
  'js/mixins/add_stable_index_to_collection',
  'js/mixins/link_generator_mixin',
  'js/mixins/papers_utils'
  ],
  function(
    ListOfThings,
    PaginationMixin,
    LinkGenerator,
    PapersUtilsMixin
    ) {
    var DetailsWidget = ListOfThings.extend({
      defaultQueryArguments: {
        fl: 'title,bibcode,author,keyword,pub,aff,volume,year,links_data,[citations],property,pubdate,abstract',
        rows : 20,
        start : 0
      },

      initialize : function(options) {
        // other widgets can send us data through page manager
        this.on('page-manager-message', function(event, data){
          if (event === "broadcast-payload"){
            this.ingestBroadcastedPayload(data);
          }
        });

        ListOfThings.prototype.initialize.call(this, options);
      },

      ingestBroadcastedPayload: function(data) {
        this.model.set(data);
      },

      customizeQuery: function() {
        var q = ListOfThings.prototype.customizeQuery.apply(this, arguments);
        if (this.sortOrder){
          q.set("sort", this.sortOrder);
        }
        if (this.queryOperator) {
          q.set('q', this.queryOperator + '(' + q.get('q').join(' ') + ')');
        }
        return q;
      },

      extractValueFromQuery: function(apiQuery, key, indexName) {
        if (apiQuery.has(key)) {
          var v = apiQuery.get(key);
          for (var i=0; i< v.length; i++) {
            if (v[i].indexOf(indexName + ':') > -1) {
              var w = v[i].replace(new RegExp(indexName + ':', 'g'), '');
              return w.replace(/\\?\"/g, '');
            }
          }
        }
      },

      processDocs: function(apiResponse, docs, paginationInfo) {

        var self = this;

        var params = apiResponse.get("response");
        var start = params.start || (paginationInfo.start || 0);

        _.each(docs, function(d,i){
          docs[i] = self.prepareDocForViewing(d);
        });

        docs = this.parseLinksData(docs);

        return PaginationMixin.addPaginationToDocs(docs, start);
      }
    });

    _.extend(DetailsWidget.prototype, LinkGenerator);
    _.extend(DetailsWidget.prototype, PapersUtilsMixin);

    return DetailsWidget;
  });