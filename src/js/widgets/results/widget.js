/**
 * Widget to display list of result hits - it allows to paginate through them
 * and display details
 *
 */

define([
    'underscore',
    'js/widgets/list_of_things/widget',
    'js/widgets/base/base_widget',
    'js/widgets/sort/widget'
    ],

  function (_,
    ListOfThingsWidget,
    BaseWidget,
    SortWidget) {

    var ResultsWidget = ListOfThingsWidget.extend({


      initialize : function(options){

        ListOfThingsWidget.prototype.initialize.apply(this, arguments);

        //now adjusting the List Model

        this.view.model.set("mainResults", true);

        this.listenTo(this.visibleCollection, "reset", this.notifyModelIfHighlights)

      },

      activate: function (beehive) {

        _.bindAll(this, "dispatchInitialRequest", "processResponse");

        this.pubsub = beehive.Services.get('PubSub');

        //custom dispatchRequest function goes here
        this.pubsub.subscribe(this.pubsub.INVITING_REQUEST, this.dispatchInitialRequest);

        //custom handleResponse function goes here
        this.pubsub.subscribe(this.pubsub.DELIVERING_RESPONSE, this.processResponse);
      },


      dispatchInitialRequest  : function(){

        this.resetWidget();

        BaseWidget.prototype.dispatchRequest.apply(this, arguments)
      },

      defaultQueryArguments: function(){
        return {
          hl     : "true",
          "hl.fl": "title,abstract,body",
          fl     : 'title,abstract,bibcode,author,keyword,id,citation_count,pub,aff,email,volume,year',
          rows : 25,
          start : 0
        }
      },

      checkIfHighlightsExist: function(){

        //check for highlights in the visible collection;

        var highlights = _.map(this.visibleCollection.toJSON(), function(m){

          var d = m.details;
          //returns an object like {details : object}
         if (d){
           return d.highlights;
         }

        });

        var agg = _.flatten(_.map(
          _.values(highlights), function(d){return _.values(d)}
        ));
        //check to make sure that highlights exist
        //and they are not all empty strings
        if (agg.length && agg.join("") !== ""){
          return true
        }

      },

      notifyModelIfHighlights : function(highlights){

        if (this.checkIfHighlightsExist(highlights)){
          this.view.model.set("showDetailsButton", true)
        }
        else {
          this.view.model.set("showDetailsButton", false)

        }

      },

      customResponseProcessing: function (docs, apiResponse) {

        var highlights = apiResponse.get("highlighting");

        //any preprocessing before adding the resultsIndex is done here
        docs = _.map(docs, function (d) {
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

        return docs
      }


  });

    return ResultsWidget;

  });
