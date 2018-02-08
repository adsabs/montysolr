/**
 * Widget to display list of result hits - it allows to paginate through them
 * and display details
 *
 */

define([
    'js/widgets/list_of_things/widget',
    'js/widgets/abstract/widget',
    'js/mixins/add_stable_index_to_collection',
    'js/mixins/link_generator_mixin',
    'js/mixins/formatter',
    'hbs!js/widgets/results/templates/container-template',
    'js/mixins/papers_utils',
    'js/modules/orcid/extension',
    'js/mixins/dependon',
    'analytics'
  ],

  function (
    ListOfThingsWidget,
    AbstractWidget,
    PaginationMixin,
    LinkGenerator,
    Formatter,
    ContainerTemplate,
    PapersUtilsMixin,
    OrcidExtension,
    Dependon,
    analytics

    ) {

    var ResultsWidget = ListOfThingsWidget.extend({
      initialize : function(options){
        ListOfThingsWidget.prototype.initialize.apply(this, arguments);
        //now adjusting the List Model
        this.view.template = ContainerTemplate;

        this.view.model.defaults =  function () {
          return {
            mainResults: true,
            title : undefined,
            //assuming there will always be abstracts
            showAbstract: "closed",
            //often they won't exist
            showHighlights: false,
            pagination: true
          }
        };
        this.model.set(this.model.defaults(), {silent : true});

        //also need to add an event listener for the "toggle all" action
        this.view.toggleAll = function(e){
          var flag = e.target.checked ? "add" : "remove";
          this.trigger("toggle-all", flag);
        }

        _.extend(this.view.events, {
          'click input#select-all-docs': 'toggleAll'
        });

        this.view.resultsWidget = true;
        this.view.delegateEvents();
        //this must come after the event delegation!
        this.listenTo(this.collection, "reset", this.checkDetails);
        //finally, listen
        // to this event on the view
        this.listenTo(this.view, "toggle-all", this.triggerBulkAction);

        //to facilitate sharing records with abstract, extend defaultQueryFields to include any extra abstract fields
        var abstractFields = AbstractWidget.prototype.defaultQueryArguments.fl.split(",");
        var resultsFields = this.defaultQueryArguments.fl.split(",");
        resultsFields = _.union(abstractFields, resultsFields);
        this.defaultQueryArguments.fl = resultsFields.join(",");

      },

      defaultQueryArguments: {
        hl     : "true",
        "hl.fl": "title,abstract,body,ack",
        'hl.maxAnalyzedChars': '150000',
        'hl.requireFieldMatch': 'true',
        'hl.usePhraseHighlighter': 'true',
        fl     : 'title,abstract,bibcode,author,keyword,id,links_data,property,citation_count,[citations],pub,aff,email,volume,pubdate,doi',
        rows : 25,
        start : 0
      },

      activate: function (beehive) {

        ListOfThingsWidget.prototype.activate.apply(this, [].slice.apply(arguments));
        var pubsub = beehive.getService('PubSub');
        _.bindAll(this, 'dispatchRequest', 'processResponse', 'onUserAnnouncement', 'onStoragePaperUpdate', 'onCustomEvent');
        pubsub.subscribe(pubsub.INVITING_REQUEST, this.dispatchRequest);
        pubsub.subscribe(pubsub.DELIVERING_RESPONSE, this.processResponse);
        pubsub.subscribe(pubsub.USER_ANNOUNCEMENT, this.onUserAnnouncement);
        pubsub.subscribe(pubsub.STORAGE_PAPER_UPDATE, this.onStoragePaperUpdate);
        pubsub.subscribe(pubsub.CUSTOM_EVENT, this.onCustomEvent);

      },

      onUserAnnouncement: function(message, data){

        if ( message == "user_info_change" && _.has(data, "isOrcidModeOn") ){
          //make sure to reset orcid state of all cached records, not just currently
          //visible ones
          var collection = this.hiddenCollection.toJSON();
          var docs = _.map(collection, function(x) {
            delete x.orcid;
            return x;
          });

          if (data.isOrcidModeOn){ this.addOrcidInfo(docs); }

          this.hiddenCollection.reset(docs);
          this.view.collection.reset(this.hiddenCollection.getVisibleModels());
        }
      },

      onCustomEvent : function(event){
        if (event == "add-all-on-page"){
          var bibs = this.collection.pluck("bibcode");
          var pubsub = this.getPubSub();
          pubsub.publish(pubsub.BULK_PAPER_SELECTION, bibs);
        }
      },

      dispatchRequest: function(apiQuery) {
        this.reset();
        this.setCurrentQuery(apiQuery);
        ListOfThingsWidget.prototype.dispatchRequest.call(this, apiQuery);
      },

      customizeQuery: function (apiQuery) {
        var q = apiQuery.clone();
        q.unlock();

        if (this.defaultQueryArguments) {
          q = this.composeQuery(this.defaultQueryArguments, q);
        }

        /*
         right now we're only showing a highlight query if there are no
         unbounded wildcards (e.g. title:*). This is not ideal bc some queries
         may be complex and after dropping the wildcard you could still want to
         highlight things. But, that may need to be fixed on the solr side.
         */
        var hq = q.get('q')[0];
        if (!hq.match(/\W\*\W/)) q.set('hl.q', hq);

        return q;
      },

      checkDetails: function(){
        var hExists = false;
        for (var i=0; i<this.collection.models.length; i++) {
          var m = this.collection.models[i];
          if (m.attributes.highlights) {
            hExists = true;
            break;
          }
        }

        if (hExists) {
          this.model.set("showHighlights", 'open'); // default is to be open
        }
        else {
          this.model.set("showHighlights", false); // will make it non-clickable
        }
      },

      processDocs: function(apiResponse, docs, paginationInfo) {
        var params = apiResponse.get("responseHeader.params");
        var start = params.start || 0;
        var docs = PaginationMixin.addPaginationToDocs(docs, start);
        var highlights = apiResponse.has("highlighting") ? apiResponse.get('highlighting') : {};
        var self = this;
        var link_server = this.getBeeHive().getObject("User").getUserData("USER_DATA").link_server;

        var appStorage = null;
        if (this.hasBeeHive() && this.getBeeHive().hasObject('AppStorage')) {
          appStorage = this.getBeeHive().getObject('AppStorage');
        }

        //stash docs so other widgets can access them
        this.getBeeHive().getObject("DocStashController").stashDocs(docs);

        //any preprocessing before adding the resultsIndex is done here
        docs = _.map(docs, function (d) {
          //used by link generator mixin
          d.link_server = link_server;
          d.identifier = d.bibcode;
          d.encodedIdentifier = encodeURIComponent(d.identifier);
          var h = {};

          if (_.keys(highlights).length) {

            h = (function () {

              var hl = highlights[d.id];
              var finalList = [];

              //adding abstract,title, etc highlights to one big list
              _.each(_.pairs(hl), function (pair) {
                finalList = finalList.concat(pair[1]);
              });

              if (finalList.length === 1 && finalList[0].trim() === "") {
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
            var format = function (d, i, arr) {
              var l = arr.length - 1;
              if (i === l || l === 0) {
                return d; //last one, or only one
              } else {
                return d + ";";
              }
            };
            d.authorFormatted = _.map(shownAuthors, format);
            d.allAuthorFormatted = _.map(d.author, format);
          }

          if (h.highlights && h.highlights.length > 0){
            d.highlights = h.highlights;
          }

          d.formattedDate = d.pubdate ? self.formatDate(d.pubdate, {format: 'yy/mm', missing: {day: 'yy/mm', month: 'yy'}}) : undefined;
          d.shortAbstract = d.abstract? self.shortenAbstract(d.abstract) : undefined;

          if (appStorage && appStorage.isPaperSelected(d.identifier)) {
            d.chosen = true;
          }

          return d;
        });

        docs = this.parseLinksData(docs);

        return docs;
      },

      onStoragePaperUpdate : function(){
        var appStorage;
        if (this.hasBeeHive() && this.getBeeHive().hasObject('AppStorage')) {
          appStorage = this.getBeeHive().getObject('AppStorage');
        }
        else {
          console.warn('AppStorage object disapperared!');
          return;
        }
        this.collection.each(function(m){
          if (appStorage.isPaperSelected(m.get("identifier"))) {
            m.set("chosen", true);
          } else {
            m.set("chosen", false);
          }
        });
        this.hiddenCollection.each(function(m){
          if (appStorage.isPaperSelected(m.get("identifier"))) {
            m.set("chosen", true);
          } else {
            m.set("chosen", false);
          }
        });
        if (this.collection.where({"chosen": true}).length == 0){
          //make sure the "selectAll" button is unchecked
          this.view.$("input#select-all-docs")[0].checked = false;
        }
      },

      triggerBulkAction : function(flag){
        var bibs = this.collection.pluck("bibcode");
        this.getPubSub().publish(this.getPubSub().BULK_PAPER_SELECTION, bibs ,flag);
      }

    });

    _.extend(ResultsWidget.prototype, LinkGenerator);
    _.extend(ResultsWidget.prototype, Formatter);
    _.extend(ResultsWidget.prototype, PapersUtilsMixin, Dependon.BeeHive);
    return OrcidExtension(ResultsWidget);

  });
