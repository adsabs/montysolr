/**
 * Widget to display list of result hits - it allows to paginate through them
 * and display details
 *
 */

define([
    'underscore',
    'js/widgets/list_of_things/widget',
    'js/widgets/base/base_widget',
    'js/mixins/add_stable_index_to_collection',
    'js/mixins/link_generator_mixin'
  ],

  function (
    _,
    ListOfThingsWidget,
    BaseWidget,
    PaginationMixin,
    LinkGenerator
    ) {

    var ResultsWidget = ListOfThingsWidget.extend({
      initialize : function(options){
        ListOfThingsWidget.prototype.initialize.apply(this, arguments);
        //now adjusting the List Model
        this.view.model.set({"mainResults": true}, {silent : true});
        this.listenTo(this.collection, "before:reset", this.checkHighlights);
      },

      defaultQueryArguments: {
          hl     : "true",
          "hl.fl": "title,abstract,body",
          fl     : 'title,abstract,bibcode,author,keyword,id,citation_count,pub,aff,email,volume,year',
          rows : 25,
          start : 0
      },

      activate: function (beehive) {
        this.pubsub = beehive.Services.get('PubSub');

        _.bindAll(this, 'onStartSearch', 'onDisplayDocuments', 'processResponse');
        this.pubsub.subscribe(this.pubsub.START_SEARCH, this.onDisplayDocuments);
        this.pubsub.subscribe(this.pubsub.DELIVERING_RESPONSE, this.processResponse);
      },

      checkHighlights: function(){
        var hExists = false;
        this.collection.each(function(m) {
          if (m.highlights)
            hExists = true;
        });
        if (hExists) {
          this.model.set("showDetailsButton", true);
        }
        else {
          this.view.model.set("showDetailsButton", false);
        }
      },



      processDocs: function(apiResponse, docs, paginationInfo) {
        var params = apiResponse.get("responseHeader.params");
        var start = params.start || 0;
        var docs = PaginationMixin.addPaginationToDocs(docs, start);
        var highlights = apiResponse.get("highlighting");

        //any preprocessing before adding the resultsIndex is done here
        docs = _.map(docs, function (d) {
          d.identifier = d.bibcode;
          var h = {};

          if (_.keys(highlights).length) {

            h = (function () {

              var hl = highlights[d.id];
              var finalList = [];
              //adding abstract,title, etc highlights to one big list
              _.each(_.pairs(hl), function (pair) {
                finalList = finalList.concat(pair[1]);
              });
              finalList = finalList;

              return {
                "highlights": finalList
              }
            }());
          }
          if (h.highlights && h.highlights.length > 0)
            d['details'] = h;
          return d;
        });
        docs = this.parseLinksData(docs);
        return docs;
      }
    });

    _.extend(ResultsWidget.prototype, LinkGenerator);
    return ResultsWidget;

  });
