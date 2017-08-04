/**
 * Created by alex on 5/19/14.
 */
define([
    'marionette',
    'backbone',
    'jquery',
    'underscore',
    'cache',
    'js/widgets/base/base_widget',
    'hbs!./templates/abstract_template',
    'hbs!./templates/metadata_template',
    'js/components/api_query',
    'js/mixins/link_generator_mixin',
    'js/mixins/papers_utils',
    'mathjax',
    'bootstrap',
  ],
  function (
    Marionette,
    Backbone,
    $,
    _,
    Cache,
    BaseWidget,
    abstractTemplate,
    metadataTemplate,
    ApiQuery,
    LinkGeneratorMixin,
    PapersUtils,
    MathJax,
    Bootstrap

    ) {

    var AbstractModel = Backbone.Model.extend({
      defaults: function () {
        return {
          abstract: undefined,
          title: undefined,
          authorAff: undefined,
          page: undefined,
          pub: undefined,
          pubdate: undefined,
          keywords: undefined,
          bibcode: undefined,
          pub_raw: undefined,
          doi: undefined,
          citation_count: undefined,
          titleLink: undefined
        }
      },

      parse: function (doc, maxAuthors) {
        var maxAuthors = maxAuthors || 20;

        //add doi link
        if (doc.doi){
          doc.doi = { doi: doc.doi,  href: LinkGeneratorMixin.adsUrlRedirect("doi", doc.doi) }
        }
        //"aff" is the name of the affiliations array that comes from solr
        doc.aff = doc.aff || [];

        if (doc.aff.length) {
          doc.hasAffiliation = _.without(doc.aff, '-').length;
          // joining author and aff
          doc.authorAff = _.zip(doc.author, doc.aff);
        }
        else if (doc.author) {
          doc.hasAffiliation = false;
          doc.authorAff = _.zip(doc.author, _.range(doc.author.length));
        }

        if (doc.page && doc.page.length) {
          doc.page = doc.page[0];
        }

        //only true if there was an author array
        //now add urls
        if (doc.authorAff){

          _.each(doc.authorAff, function(el, index){
            doc.authorAff[index][2] = encodeURIComponent('"' +  el[0] + '"').replace(/%20/g, "+");
          });

          if (doc.authorAff.length > maxAuthors) {
            doc.authorAffExtra = doc.authorAff.slice(maxAuthors, doc.authorAff.length);
            doc.authorAff = doc.authorAff.slice(0, maxAuthors);
          }

          doc.hasMoreAuthors = doc.authorAffExtra && doc.authorAffExtra.length;
        }

        if (doc.pubdate){
          doc.formattedDate =  PapersUtils.formatDate(doc.pubdate, {format: 'MM d yy', missing: {day: 'MM yy', month: 'yy'}});
        }

        if (doc.title && doc.title.length) {
          doc.title = doc.title[0];
          var docTitleLink = doc.title.match(/<a.*href="(.*?)".*?>(.*)<\/a>/i);

          // Find any links that are buried in the text of the title
          // Parse it out and convert to BBB hash links, if necessary
          if (docTitleLink) {
            doc.title = doc.title.replace(docTitleLink[0], "").trim();
            doc.titleLink = {
              href: docTitleLink[1],
              text: docTitleLink[2]
            };

            if (doc.titleLink.href.match(/^\/abs/)) {
              doc.titleLink.href = "#" + doc.titleLink.href.slice(1);
            }
          }
        }

        return doc;
      }
    });

    var AbstractView = Marionette.ItemView.extend({

      tagName : "article",

      className : "s-abstract-metadata",

      modelEvents : {
        "change" : "render"
      },

      template: abstractTemplate,

      events: {
        "click #toggle-aff": "toggleAffiliation",
        "click #toggle-more-authors": "toggleMoreAuthors",
        'click a[data-target="more-authors"]': 'toggleMoreAuthors',
        'click a[target="prev"]': 'onClick',
        'click a[target="next"]': 'onClick'
      },

      toggleMoreAuthors: function (ev) {

        if (ev) {
          ev.stopPropagation();
        }

        this.$(".author.extra").toggleClass("hide");
        this.$(".author.extra-dots").toggleClass("hide");
        if (this.$(".author.extra").hasClass("hide")){
          this.$("#toggle-more-authors").text("Show all authors");
        } else {
          this.$("#toggle-more-authors").text("Hide authors");
        }

      },

      toggleAffiliation: function (ev) {

        if (ev) {
          ev.preventDefault();
        }

        this.$(".affiliation").toggleClass("hide");
        if (this.$(".affiliation").hasClass("hide")){
          this.$("#toggle-aff").text("Show affiliations")
        } else {
          this.$("#toggle-aff").text("Hide affiliations")
        }
      },

      onClick: function(ev) {
        ev.preventDefault();
        this.trigger($(ev.target).attr('target'));
      },

      onRender : function(){
        this.$(".icon-help").popover({trigger : "hover", placement : "right", html :true});

        if (MathJax) MathJax.Hub.Queue(["Typeset", MathJax.Hub, this.el]);

        this.model.set('url', Backbone.history.location.href);

        $("head").append(metadataTemplate(this.model.toJSON()));
        
        // and set the title, maintain tags
        document.title = $('<div>' + this.model.get("title") + '</div>').text();

        var ev = document.createEvent('HTMLEvents');
        ev.initEvent('ZoteroItemUpdated', true, true);
        document.dispatchEvent(ev);
      }
    });

    var AbstractWidget = BaseWidget.extend({
      initialize: function (options) {
        options = options || {};
        this.model = options.data ? new AbstractModel(options.data, {parse: true}) : new AbstractModel();
        this.view = new AbstractView({model: this.model});

        this.listenTo(this.view, 'all', this.onAllInternalEvents);

        BaseWidget.prototype.initialize.apply(this, arguments);
        this._docs = {};
        this.maxAuthors = 20;
      },

      activate: function (beehive) {
        this.setBeeHive(beehive);
        var pubsub = beehive.getService('PubSub');

        _.bindAll(this, ['onNewQuery', 'dispatchRequest', 'processResponse', 'onDisplayDocuments']);
        pubsub.subscribe(pubsub.START_SEARCH, this.onNewQuery);
        pubsub.subscribe(pubsub.INVITING_REQUEST, this.dispatchRequest);
        pubsub.subscribe(pubsub.DELIVERING_RESPONSE, this.processResponse);
        pubsub.subscribe(pubsub.DISPLAY_DOCUMENTS, this.onDisplayDocuments);

      },

      defaultQueryArguments: {
        fl: 'title,abstract,bibcode,author,keyword,id,citation_count,[citations],pub,aff,volume,pubdate,doi,pub_raw,page',
        rows: 40
      },

      mergeStashedDocs : function(docs) {
        _.each(docs, function(d){
          if (!this._docs[d.bibcode]) {
            this._docs[d.bibcode] = this.model.parse(d);
          }
        }, this);
      },

      onNewQuery: function (apiQuery) {

        //only empty docs array if it truly is a new query
        var newQueryJSON = apiQuery.toJSON();

        var currentStreamlined = _.pick(this.getCurrentQuery().toJSON(), _.keys(newQueryJSON))
        if (JSON.stringify(newQueryJSON) != JSON.stringify(currentStreamlined)){
          this._docs = {};
        }
      },

      dispatchRequest : function(apiQuery){
        this.setCurrentQuery(apiQuery);
        BaseWidget.prototype.dispatchRequest.apply(this, arguments);
      },

      //bibcode is already in _docs
      displayBibcode : function(bibcode){

        //wipe out the former values, because this new set of data
        // might not have every key
        this.model.clear({silent : true});
        this.model.set(this._docs[bibcode]);

        this._current = bibcode;
        // let other widgets know details
        var c = this._docs[bibcode]["[citations]"];
        var resolved_citations = c ? c.num_citations : 0;

        this.trigger('page-manager-event', 'broadcast-payload', {
          title: this._docs[bibcode].title,
          //this should be superfluous, widgets already subscribe to display_documents
          bibcode: bibcode,

          //used by citation list widget
          citation_discrepancy : this._docs[bibcode].citation_count - resolved_citations,
          citation_count : this._docs[bibcode].citation_count
        });

      },

      onDisplayDocuments: function (apiQuery) {

        //check to see if a query is already in progress (the way bbb is set up, it will be)
        //if so, auto fill with docs initially requested by results widget
        this.mergeStashedDocs(this.getBeeHive().getObject("DocStashController").getDocs());

        var bibcode =  apiQuery.get('q'), q;

        if (bibcode.length > 0 && bibcode[0].indexOf('bibcode:') > -1) {
          //redefine bibcode
          var bibcode = bibcode[0].replace('bibcode:', '');
        }
        if (this._docs[bibcode]) { // we have already loaded it
         this.displayBibcode(bibcode);
        }
        else {
          if (apiQuery.has('__show')) return; // cycle protection
            q = apiQuery.clone();
            q.set('__show', bibcode);
           //this will add required fields
            this.dispatchRequest(q);
        }
      },

      onAllInternalEvents: function(ev) {
        if ((ev == 'next' || ev == 'prev') && this._current) {
          var keys = _.keys(this._docs);
          var pubsub = this.getPubSub();
          var curr = _.indexOf(keys, this._current);
          if (curr > -1) {
            if (ev == 'next' && curr+1 < keys.length) {
              pubsub.publish(pubsub.DISPLAY_DOCUMENTS, keys[curr+1]);
            }
            else if (curr-1 > 0) {
              pubsub.publish(pubsub.DISPLAY_DOCUMENTS, keys[curr-1]);
            }
          }
        }
      },

      processResponse: function (apiResponse) {
        var r = apiResponse.toJSON();
        var d, bibcode;
        if (r.response && r.response.docs) {
          _.each(r.response.docs, function (doc) {
            d = this.model.parse(doc, this.maxAuthors);
            this._docs[d.bibcode] = d;
          }, this);

          if (apiResponse.has('responseHeader.params.__show')) {

            bibcode = apiResponse.get('responseHeader.params.__show');
            this.displayBibcode(bibcode);
          }
        }

        this.trigger('page-manager-event', 'widget-ready',
          {numFound: apiResponse.get("response.numFound")});

      }

    });

    return AbstractWidget;
  });
