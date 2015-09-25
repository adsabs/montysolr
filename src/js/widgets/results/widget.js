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
    'js/modules/orcid/extension',
    'js/mixins/dependon'
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
    OrcidExtension,
    Dependon
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

        _.extend(this.view.events, {'click input#select-all-docs': 'toggleAll'});

        this.view.delegateEvents();

        //this must come after the event delegation!
        this.listenTo(this.collection, "reset", this.checkDetails);
        //finally, listen to this event on the view
        this.listenTo(this.view, "toggle-all", this.triggerBulkAction);

      },

      defaultQueryArguments: {
        hl     : "true",
        "hl.fl": "title,abstract,body,ack",
        'hl.maxAnalyzedChars': '150000',
        'hl.requireFieldMatch': 'true',
        'hl.usePhraseHighlighter': 'true',
        fl     : 'title,abstract,bibcode,author,keyword,id,links_data,property,[citations],pub,aff,email,volume,pubdate,doi',
        rows : 20,
        start : 0
      },

      activate: function (beehive) {
        this.setBeeHive(beehive);
        var pubsub = beehive.getService('PubSub');
        _.bindAll(this, 'dispatchRequest', 'processResponse', 'onUserAnnouncement', 'onStoragePaperUpdate', 'onCustomEvent');
        pubsub.subscribe(pubsub.INVITING_REQUEST, this.dispatchRequest);
        pubsub.subscribe(pubsub.DELIVERING_RESPONSE, this.processResponse);
        pubsub.subscribe(pubsub.USER_ANNOUNCEMENT, this.onUserAnnouncement);
        pubsub.subscribe(pubsub.STORAGE_PAPER_UPDATE, this.onStoragePaperUpdate);
        pubsub.subscribe(pubsub.CUSTOM_EVENT, this.onCustomEvent);

      },

      onUserAnnouncement: function(key, val){
        if (key == "orcidUIChange"){
          var data = this.view.collection.toJSON();
          var docs = _.map(data, function(x) {
            delete x.orcid;
            return x;
          });
          if (val)
            this.addOrcidInfo(docs);
          this.view.collection.reset(docs);
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
          ListOfThingsWidget.prototype.dispatchRequest.call(this, apiQuery);
      },

      customizeQuery: function (apiQuery) {
        var q = apiQuery.clone();
        q.unlock();

        if (this.defaultQueryArguments) {
          q = this.composeQuery(this.defaultQueryArguments, q);
        }

        // remove some stupid cases for highlight query
        var hq = q.get('q')[0];
        hq = hq.replace(/\b\w+\:\*/g, '');
        hq = hq.replace(/\*:\*/g, '');
        hq = hq.trim();

        if (hq == "") {
          _.each(q.keys(), function(f) {
            if (f.indexOf('hl.') == 0)
              q.unset(f);
          })
        }
        else {
          q.set('hl.q', hq);
        }

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
            d.highlights = h.highlights;
          }

          if(d["[citations]"] && d["[citations]"]["num_citations"]>0){
            d.num_citations = self.formatNum(d["[citations]"]["num_citations"]);
          }
          else {
            //formatNum would return "0" for zero, which would then evaluate to true in the template
            d.num_citations = 0;
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
        var appStorage = null;
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
        this.getPubSub().publish(this.getPubSub().BULK_PAPER_SELECTION, flag, bibs);
      }

    });

    _.extend(ResultsWidget.prototype, LinkGenerator);
    _.extend(ResultsWidget.prototype, Formatter);
    _.extend(ResultsWidget.prototype, PapersUtilsMixin, Dependon.BeeHive);
    return OrcidExtension(ResultsWidget);

  });
