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
    'js/mixins/link_generator_mixin',
    'js/mixins/formatter'
  ],

  function (
    _,
    ListOfThingsWidget,
    BaseWidget,
    PaginationMixin,
    LinkGenerator,
    Formatter
    ) {

    var ResultsWidget = ListOfThingsWidget.extend({
      initialize : function(options){
        ListOfThingsWidget.prototype.initialize.apply(this, arguments);
        //now adjusting the List Model
        this.view.model.set({"mainResults": true}, {silent : true});
        this.listenTo(this.view.collection, "reset", this.checkHighlights);
      },

      defaultQueryArguments: {
          hl     : "true",
          "hl.fl": "title,abstract,body",
          fl     : 'title,abstract,bibcode,author,keyword,id,[citations],pub,aff,email,volume,year',
          rows : 25,
          start : 0
      },

      activate: function (beehive) {
        this.pubsub = beehive.Services.get('PubSub');

        _.bindAll(this, 'onStartSearch', 'onDisplayDocuments', 'processResponse');
        this.pubsub.subscribe(this.pubsub.INVITING_REQUEST, this.onDisplayDocuments);
        this.pubsub.subscribe(this.pubsub.DELIVERING_RESPONSE, this.processResponse);
      },

      checkHighlights: function(){
        var hExists = false;
        for (var i=0; i<this.collection.models.length; i++) {
          var m = this.collection.models[i];
          if (m.attributes.details && m.attributes.details.highlights) {
            hExists = true;
            break;
          }
        }

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
        var self = this;

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

              if (finalList.length == 1 && finalList[0].trim() == "") {
                return {};
              }
              
              return {
                "highlights": finalList
              }
            }());
          }
          if (h.highlights && h.highlights.length > 0)
            d['details'] = h;

          if(d["[citations]"] && d["[citations]"]["num_citations"]>0){
            d.num_citations = self.formatNum(d["[citations]"]["num_citations"]);
          }
          else {
            //formatNum would return "0" for zero, which would then evaluate to true in the template
            d.num_citations = 0;
          }

          return d;
        });

        docs = this.parseLinksData(docs);
        return docs;
      }
    });

    _.extend(ResultsWidget.prototype, LinkGenerator);
    _.extend(ResultsWidget.prototype, Formatter);
    return ResultsWidget;

  });
