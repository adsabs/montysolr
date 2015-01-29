

define([
    'js/widgets/base/base_widget',
    'js/components/api_query',
    'hbs!./templates/export-button',
    'hbs!./templates/export-menu',
    'hbs!./templates/export_template',
    'marionette',
    'js/components/api_feedback',
    'jquery',
    'jquery-ui',
    'module',
    'js/components/api_targets'
  ],
  function(
    BaseWidget,
    ApiQuery,
    ButtonTemplate,
    MenuTemplate,
    WidgetTemplate,
    Marionette,
    ApiFeedback,
    $,
    $ui,
    WidgetConfig,
    ApiTargets
    ){


    var ExportModel = Backbone.Model.extend({
      defaults : {
        format: 'bibtex',
        export: undefined,
        query: undefined,
        identifiers: [],
        msg: undefined,
        maxExport: 300,
        userExportVal: undefined,
        numFound: undefined
      }
    });


    var ExportView = Marionette.ItemView.extend({

      tagName : "span",
      className : "s-export",

      initialize: function () {
      },

      template: WidgetTemplate,

      events: {
        'click #exportQuery': 'exportQuery',
        'click #exportRecords': 'exportRecords',
        'change input.userExportVal': 'userExportVal',
        "click .close-widget": "signalCloseWidget"
      },

      modelEvents: {
        "change:export": 'render',
        "change:msg" : 'render',
        'change:query': 'render',
        'change:numIdentifiers': 'render'
      },

      userExportVal: function(ev) {
        this.model.set('userExportVal', $(ev.target).val());
      },

      exportRecords: function(ev) {
        if (ev)
          ev.preventDefault()
        this.trigger('export-records');
      },

      exportQuery: function(ev) {
        if (ev)
          ev.preventDefault();
        this.trigger('export-query');
      },

      signalCloseWidget: function(ev) {
        this.trigger('close-widget');
      }

    });


    var ExportWidget = BaseWidget.extend({

      viewEvents: {
        'export-records': 'exportRecords',
        'export-query': 'exportQuery',
        'close-widget': 'closeWidget'
      },

      initialize: function(options){
        this.model = new ExportModel();
        this.view = new ExportView({model : this.model});
        this.defaultMax = options.defaultMax || 300;
        BaseWidget.prototype.initialize.apply(this, arguments);
        Marionette.bindEntityEvents(this, this.view, Marionette.getOption(this, "viewEvents"));
        Marionette.bindEntityEvents(this, this.model, Marionette.getOption(this, "modelEvents"));
      },

      activate: function (beehive) {
        _.bindAll(this, "handleFeedback", "processResponse");
        this.pubsub = beehive.Services.get('PubSub');

        // widget doesn't need to execute queries (but it needs to listen to them)
        this.pubsub.subscribe(this.pubsub.FEEDBACK, _.bind(this.handleFeedback, this));
      },

      handleFeedback: function(feedback) {
        switch (feedback.code) {
          case ApiFeedback.CODES.SEARCH_CYCLE_STARTED:
            this.setCurrentQuery(feedback.query);
            this.model.set('query', feedback.query.clone());
            this.model.set('numFound', feedback.numFound);
            break;
        }
      },

      /**
       * Export data directly - use the query, if supplied - if we already
       * know max 'numFound', then we'll use that. If not, we'll first find
       * out how many recs there are
       *
       * @param format
       * @param apiQuery
       */
      exportQuery: function(format, apiQuery) {

        var self = this;
        var q = apiQuery || this.model.get('query') || this.getCurrentQuery();
        var d = this._getQueryInfo(q);

        this.model.set('query', q);
        this.model.set('format', format);

        // first find out how many total docs there are in
        d.done(function(numFound) {
          var toSet = {};
          if (numFound == -1) {
            toSet['msg'] = 'Unknown number of results'
          }
          else {
            toSet['numFound'] = numFound;
            toSet['maxExport'] = Math.min(numFound, self.model.get('maxExport'));
            self.model.set('msg', 'Please wait, fetching data. The query returns ' + numFound + ' results (maximum export is: ' + toSet.maxExport + ')');
          }
          toSet['userExportVal'] = Math.min((self.model.get('userExportVal') || self.model.get('maxExport')) || self.model.get('maxExport'));
          self.model.set(toSet);

          q = q.clone();
          q.unlock();
          q.set('rows', toSet.userExportVal);
          q.set('fl', 'bibcode');
          self.model.set('format', format);

          // collect bibcodes of the query
          self._executeApiRequest(q)
            .done(function(apiResponse) {
              // export documents by their ids
              var ids = _.map(apiResponse.get('response.docs'), function(d) {return d.bibcode});

              self._getExports(self.model.get('format') || 'bibtex', ids)
                .done(function(exports) {
                  self.model.set('msg', exports.msg);
                })
            });
        });
      },

      exportRecords: function(format, recs) {
        recs = recs || this.model.get('identifiers');
        if (!_.isArray(recs)) throw new Error('Identifiers must be an array');
        if (recs.length <= 0) throw new Error('Do you want to export nothing? Let me be!');
        this.model.set('numIdentifiers', recs.length);
        this.model.set('format', format);
        this.model.set('identifiers');
        this._getExports(format, recs);
      },

      /**
       * Fetches data from the export api and saves into the model
       *
       * @param format
       * @param data
       */
      _getExports: function(format, data) {
        var self = this;
        var promise = this._getData(data)
          .done(function(exports) {
            if (exports && exports.has('export')) {
              self.model.set('export', exports.get('export'));
            }
            else {
              console.error('The export did return some garbage, tfuj!', exports);
            }
          });
        return promise;
      },


      /**
       * Returns 'numFound' for the given query
       *
       * @param apiQuery
       * @returns {*}
       */
      _getQueryInfo : function(apiQuery){
        var q = apiQuery || this.getCurrentQuery();
        var defer = $.Deferred();
        var self = this;

        if (q) {
          if (self._lastQueryId == q.url() && self._lastQueryNumFound) {
            defer.resolve(self._lastQueryNumFound);
            return defer;
          }
          self._lastQueryNumFound = null;
          var req = this.composeRequest(q);
          if (req) {
            this.pubsub.subscribeOnce(this.pubsub.DELIVERING_RESPONSE, _.bind(function(apiResponse) {
              self._lastQueryId = q.url();
              self._lastQueryNumFound = apiResponse.get('response.numFound');
              defer.resolve(self._lastQueryNumFound);
            }), this);
            this.pubsub.publish(this.pubsub.EXECUTE_REQUEST, req);
          }
          else {
            throw new Error('Well, this is unexpected behaviour! Who wrote this software?');
          }
        }
        else {
          defer.resolve(-1);
        }
        return defer;
      },

      /**
       * From the remote Api, retrieve data
       *
       * @param identifiers
       * @param format
       * @returns {*}
       * @private
       */
      _getData: function(identifiers, format){

          format = format || this.model.get('format') || 'bibtex';
           //export endpoints
          var tbl = {
            endnote: 'endnote',
            aastex: 'aastex',
            bibtex: 'bibtex'
          };
          format = tbl[format.toLowerCase()] || format.toLowerCase();

          if (!_.isArray(identifiers)) throw new Error('Identifiers must be an array');
          if (identifiers.length <= 0) throw new Error('Do you want to export nothing? Let me be!');

          var q = new ApiQuery();
          //export parameter is "bibcode"
          q.set('bibcode', identifiers);
          q.set('sort', 'NONE');

          var req = this.composeRequest(q);
          req.set("target",  ApiTargets.EXPORT + format);

          //use post, although get is also possible
          var reqOptions = {
            type: 'POST'
          };

          req.set('options', reqOptions);

          var defer = $.Deferred();

        if (req) {
          this.pubsub.subscribeOnce(this.pubsub.DELIVERING_RESPONSE, _.bind(function(data) {
            defer.resolve(data);
          }), this);
          this.pubsub.publish(this.pubsub.EXECUTE_REQUEST, req);
        }
        else {
          throw new Error('Well, this is unexpected behaviour! Who wrote this software?');
        }
        this.model.set('identifiers', identifiers);
        return defer;
      },

      _executeApiRequest: function(apiQuery){
        if (!apiQuery) throw new Error('Damn, and I thought you knew what you do!');

        var self = this;
        var req = this.composeRequest(apiQuery);
        var defer = $.Deferred();
        if (req) {
          this.pubsub.subscribeOnce(this.pubsub.DELIVERING_RESPONSE, _.bind(function(data) {
            defer.resolve(data);
          }), this);
          this.pubsub.publish(this.pubsub.EXECUTE_REQUEST, req);
        }
        else {
          throw new Error('Well, this is unexpected behaviour! Who wrote this software?');
        }
        return defer;
      },

      reset: function() {
        this.model.set({
          export: null,
          query: this.model.get('query'),
          numFound: this.model.get('numFound')});
      },

      closeWidget: function () {
        this.pubsub.publish(this.pubsub.NAVIGATE, "results-page");
      }
    });
    return ExportWidget;
  });