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
    'js/mixins/formatter',
    'hbs!./templates/container-template',
    'js/mixins/papers_utils',
    'js/modules/orcid/extension'
  ],

  function (
    _,
    ListOfThingsWidget,
    BaseWidget,
    PaginationMixin,
    LinkGenerator,
    Formatter,
    ContainerTemplate,
    PapersUtilsMixin,
    OrcidExtension
    ) {

    var ResultsWidget = ListOfThingsWidget.extend({
      initialize : function(options){
        ListOfThingsWidget.prototype.initialize.apply(this, arguments);
        //now adjusting the List Model
        this.view.template = ContainerTemplate;
        this.view.model.set({"mainResults": true}, {silent : true});
        this.listenTo(this.collection, "reset", this.checkDetails);
      },

      defaultQueryArguments: {
          hl     : "true",
          "hl.fl": "title,abstract,body",
          fl     : 'title,abstract,bibcode,author,keyword,id,links_data,property,[citations],pub,aff,email,volume,pubdate',
          rows : 25,
          start : 0
      },

      activate: function (beehive) {
        this.pubsub = beehive.Services.get('PubSub');
        _.bindAll(this, 'onDisplayDocuments', 'processResponse');
        this.pubsub.subscribe(this.pubsub.INVITING_REQUEST, this.onDisplayDocuments);
        this.pubsub.subscribe(this.pubsub.DELIVERING_RESPONSE, this.processResponse);

        if (this.activateResultsExtension)
        {
          this.activateResultsExtension(beehive);
        } // also current this is passed
      },

      onDisplayDocuments: function(apiQuery) {
        this.reset();
        ListOfThingsWidget.prototype.dispatchRequest.call(this, apiQuery);
      },

      checkDetails: function(){
        var hExists = false;
        for (var i=0; i<this.collection.models.length; i++) {
          var m = this.collection.models[i];
          if (m.attributes.details && m.attributes.details.highlights) {
            hExists = true;
            break;
          }
          if (m.attributes.abstract) {
            hExists = true;
            break;
          }
        }

        if (hExists) {
          this.model.set("showDetails", 'closed'); // default is to be closed (openable)
        }
        else {
          this.view.model.set("showDetails", false); // will make it non-clickable
        }
      },

      processDocs: function(apiResponse, docs, paginationInfo) {
        var params = apiResponse.get("responseHeader.params");
        var start = params.start || 0;
        var docs = PaginationMixin.addPaginationToDocs(docs, start);
        var highlights = apiResponse.has("highlighting") ? apiResponse.get('highlighting') : {};
        var self = this;

        //any preprocessing before adding the resultsIndex is done here
        docs = _.map(docs, function (d) {
          d.identifier = d.bibcode;
          d.details = {};
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

          var maxAuthorNames = 3;

          if (d.author && d.author.length > maxAuthorNames) {
            d.extraAuthors = d.author.length - maxAuthorNames;
            shownAuthors = d.author.slice(0, maxAuthorNames);
          } else if (d.author) {
            shownAuthors = d.author
          }

          if (d.author) {
            var l = shownAuthors.length - 1;
            d.authorFormatted = _.map(shownAuthors, function (d, i) {
              if (i == l || l == 0) {
                return d; //last one, or only one
              } else {
                return d + ";";
              }
            })
          }

          if (h.highlights && h.highlights.length > 0){
            _.extend(d.details, h);
        }

          d.details.pub = d.pub;

          if(d["[citations]"] && d["[citations]"]["num_citations"]>0){
            d.num_citations = self.formatNum(d["[citations]"]["num_citations"]);
          }
          else {
            //formatNum would return "0" for zero, which would then evaluate to true in the template
            d.num_citations = 0;
          }

          d.formattedDate = d.pubdate ? self.formatDate(d.pubdate, {format: 'yy/mm', missing: {day: 'yy/mm', month: 'yy'}}) : undefined;

          d.details.shortAbstract = d.abstract? self.shortenAbstract(d.abstract) : undefined;

          return d;
        });

        docs = this.parseLinksData(docs);
        return docs;
      }
    });

    _.extend(ResultsWidget.prototype, LinkGenerator);
    _.extend(ResultsWidget.prototype, Formatter);
    _.extend(ResultsWidget.prototype, PapersUtilsMixin);
    return OrcidExtension(ResultsWidget);
    //return ResultsWidget;

  });
