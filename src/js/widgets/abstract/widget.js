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
    'js/mixins/link_generator_mixin'],
  function (
    Marionette,
    Backbone,
    $,
    _,
    Cache,
    BaseWidget,
    abstractTemplate,
    ApiQuery,
    LinkGeneratorMixin) {

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

      parse: function (doc) {
        var authorAff, hasAffiliation, title;

       doc.aff = doc.aff || [];
        if (doc.aff.length) {
          hasAffiliation = true;
          // joining author and aff
          authorAff = _.zip(doc.author, doc.aff);
        }


        title = $.isArray(doc.title)? doc.title[0] : undefined;

        return {
          hasAffiliation: hasAffiliation,
          abstract: doc.abstract,
          title: title,
          authorAff: authorAff,
          pub: doc.pub,
          pubdate: doc.pubdate,
          keyword: doc.keyword,
          bibcode: doc.bibcode,
          pub_raw: doc.pub_raw,
          doi: doc.doi,
          bibcode: doc.bibcode
        }
      }
    });

    var AbstractView = Marionette.ItemView.extend({

      initialize: function () {
        this.listenTo(this.model, "change", this.render)
      },

      template: abstractTemplate,
      events: {
        "click #toggle-aff": "toggleAffiliation",
        'click a[target="prev"]': 'onClick',
        'click a[target="next"]': 'onClick'
      },

      toggleAffiliation: function () {

        this.$(".affiliation").toggleClass("hide");
        if (this.$(".affiliation").hasClass("hide")){
          this.$("#toggle-aff").text("(Show author affiliations)")
        }
        else {
          this.$("#toggle-aff").text("(Hide author affiliations)")
        }

      },

      onClick: function(ev) {
        ev.preventDefault();
        this.trigger($(ev.target).attr('target'));
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
        fl: 'title,abstract,bibcode,author,keyword,id,citation_count,pub,aff,volume,year,doi,pub_raw'
      },

      loadBibcodeData : function (bibcode) {
        if (this._docs[bibcode]) {
          this._current = bibcode;
          this.model.set(this._docs[bibcode]);
        }
        else {
          this.dispatchRequest(new ApiQuery({'q': 'bibcode:' + bibcode, '__show': bibcode}));
        }
      },

      onNewQuery: function () {
        this._docs = {};
      },

      onDisplayDocuments: function (apiQuery) {
        var q = apiQuery.clone();
        var bibcode = q.get('q');
        if (bibcode.length > 0 && bibcode[0].indexOf('bibcode:') > -1) {
          bibcode = bibcode[0].replace('bibcode:', '');
        }
        if (this._docs[bibcode]) { // we have already loaded it
          this._current = bibcode;
          this.model.set(this._docs[bibcode]);
        }
        else {
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
        var q = apiResponse.getApiQuery();
        this.setCurrentQuery(q);

        var r = apiResponse.toJSON();
        var d, self = this;
        if (r.response && r.response.docs) {
          _.each(r.response.docs, function (doc) {
            //add doi link
            if (doc.doi){
              doc.doi = {doi: doc.doi,  href: self.adsUrlRedirect("doi", doc.doi)}
            }
            d = self.model.parse(doc);
            self._docs[d.bibcode] = d;
          });

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