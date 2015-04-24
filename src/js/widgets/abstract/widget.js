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
    'js/components/api_query',
    'js/mixins/link_generator_mixin',
    'js/mixins/papers_utils',
    'bootstrap'
  ],
  function (
    Marionette,
    Backbone,
    $,
    _,
    Cache,
    BaseWidget,
    abstractTemplate,
    ApiQuery,
    LinkGeneratorMixin,
    PapersUtils
    ) {

    var AbstractModel = Backbone.Model.extend({
      defaults: function () {
        return {
          abstract: undefined,
          title: undefined,
          authorAff: undefined,
          pub: undefined,
          pubdate: undefined,
          keywords: undefined,
          bibcode: undefined,
          pub_raw: undefined,
          doi: undefined,
          bibcode: undefined
        }
      },

      parse: function (doc, maxAuthors) {
        var authorAff, hasAffiliation, title, authorAffExtra;
        maxAuthors = maxAuthors || 20;

        authorAff = [], authorAffExtra = [];

        doc.aff = doc.aff || [];
        if (doc.aff.length) {
          hasAffiliation = _.without(doc.aff, '-').length;
          // joining author and aff
          authorAff = _.zip(doc.author, doc.aff);
        }
        else if (doc.author) {
          hasAffiliation = false;
          authorAff = _.zip(doc.author, _.range(doc.author.length));
        }

        _.each(authorAff, function(el, index){
          authorAff[index][2] = encodeURIComponent('"' +  el[0] + '"');
        });

        if (authorAff.length > maxAuthors) {
          authorAffExtra = authorAff.slice(maxAuthors, authorAff.length);
          authorAff = authorAff.slice(0, maxAuthors);
        }

        var formattedDate = doc.pubdate ? PapersUtils.formatDate(doc.pubdate, {format: 'MM d yy', missing: {day: 'MM yy', month: 'yy'}}) : undefined;

        title = $.isArray(doc.title)? doc.title[0] : undefined;

        return {
          hasAffiliation: hasAffiliation,
          abstract: doc.abstract,
          title: title,
          authorAff: authorAff,
          authorAffExtra: authorAffExtra,
          hasMoreAuthors: authorAffExtra.length,
          pub: doc.pub,
          pubdate: doc.pubdate,
          formattedDate: formattedDate,
          keyword: doc.keyword,
          bibcode: doc.bibcode,
          pub_raw: doc.pub_raw,
          doi: doc.doi,
          bibcode: doc.bibcode
        }
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

        if (ev) ev.stopPropagation();

        this.$(".author.extra").toggleClass("hide");
        this.$(".author.extra-dots").toggleClass("hide");
        if (this.$(".author.extra").hasClass("hide")){
          this.$("#toggle-more-authors").text("Show all authors");
        }
        else {
          this.$("#toggle-more-authors").text("Hide authors");
        }

      },

      toggleAffiliation: function (ev) {

        if (ev) ev.preventDefault();

        this.$(".affiliation").toggleClass("hide");
        if (this.$(".affiliation").hasClass("hide")){
          this.$("#toggle-aff").text("Show affiliations")
        }
        else {
          this.$("#toggle-aff").text("Hide affiliations")
        }

      },

      onClick: function(ev) {
        ev.preventDefault();
        this.trigger($(ev.target).attr('target'));
      },

      onRender : function(){
        this.$(".icon-help").popover({trigger : "hover", placement : "right", html :true});
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
        this.pubsub = beehive.Services.get('PubSub');

        _.bindAll(this, ['onNewQuery', 'dispatchRequest', 'processResponse', 'onDisplayDocuments']);
        this.pubsub.subscribe(this.pubsub.START_SEARCH, this.onNewQuery);
        this.pubsub.subscribe(this.pubsub.INVITING_REQUEST, this.dispatchRequest);
        this.pubsub.subscribe(this.pubsub.DELIVERING_RESPONSE, this.processResponse);
        this.pubsub.subscribe(this.pubsub.DISPLAY_DOCUMENTS, this.onDisplayDocuments);
      },

      defaultQueryArguments: {
        fl: 'title,abstract,bibcode,author,keyword,id,citation_count,pub,aff,volume,pubdate,doi,pub_raw',
        rows: 40
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

      onDisplayDocuments: function (apiQuery) {

        var bibcode = apiQuery.get('q');
        if (bibcode.length > 0 && bibcode[0].indexOf('bibcode:') > -1) {
          bibcode = bibcode[0].replace('bibcode:', '');
        }
        if (this._docs[bibcode]) { // we have already loaded it
          this.model.set(this._docs[bibcode]);
          this._current = bibcode;
          // let other widgets know details
          this.trigger('page-manager-event', 'broadcast-payload', {
              title: this._docs[bibcode].title,
              bibcode: bibcode
          });
        }
        else {
          if (apiQuery.has('__show')) return; // cycle protection
          var q = apiQuery.clone();
          q.set('__show', bibcode);
          this.dispatchRequest(q);
        }
      },

      onAllInternalEvents: function(ev) {
        if ((ev == 'next' || ev == 'prev') && this._current) {
          var keys = _.keys(this._docs);

          var curr = _.indexOf(keys, this._current);
          if (curr > -1) {
            if (ev == 'next' && curr+1 < keys.length) {
              this.pubsub.publish(this.pubsub.DISPLAY_DOCUMENTS, keys[curr+1]);
            }
            else if (curr-1 > 0) {
              this.pubsub.publish(this.pubsub.DISPLAY_DOCUMENTS, keys[curr-1]);
            }
          }
        }
      },

      processResponse: function (apiResponse) {
        var r = apiResponse.toJSON();
        var d;
        if (r.response && r.response.docs) {
          _.each(r.response.docs, function (doc) {
            //add doi link
            if (doc.doi){
              doc.doi = {doi: doc.doi,  href: this.adsUrlRedirect("doi", doc.doi)}
            }
            d = this.model.parse(doc, this.maxAuthors);
            this._docs[d.bibcode] = d;
          }, this);

          if (apiResponse.has('responseHeader.params.__show')) {
            this.onDisplayDocuments(apiResponse.getApiQuery());
          }
        }

        this.trigger('page-manager-event', 'widget-ready',
          {numFound: apiResponse.get("response.numFound"), widget: this});

      }

    });

    _.extend(AbstractWidget.prototype, LinkGeneratorMixin);


    return AbstractWidget;
  });