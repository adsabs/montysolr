

define([
    'js/widgets/base/base_widget',
    'js/components/api_query',
    'hbs!./templates/export-button',
    'hbs!./templates/export-menu',
    'hbs!./templates/export_template',
    'marionette',
    'js/components/api_feedback',
    'jquery',
    'jquery-ui'

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
    $ui
    ){


    var ExportModel = Backbone.Model.extend({
      defaults : {
        format: 'bibtex',
        exports: undefined,
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
        'click #exportModal button.apply': 'selectedExport'
      },

      modelEvents: {
        "change:exports": 'render',
        "change:msg" : 'render'
      },

      render: function() {
        // this seems to be necessary (at least when the actions happen too fast ... like in unittests)
        if (this.$el && this.$el.find('#exportModal').length) {
          this.$el.find('#exportModal').modal('hideModal');
        }
        Marionette.ItemView.prototype.render.apply(this, arguments);
      },

      buildSlider: function (min, max) {
        var that = this;
        this.$(".slider").slider({
          range: 'min',
          min: min,
          max: max,
          value: Math.min(20, max),
          slide: function( event, ui ) {
            that.$(".show-slider-data-second").val(ui.value);
          }
        });
        this.$(".show-slider-data-first").val(min);
        this.$(".show-slider-data-second").val(Math.min(20, max));
      },

      selectedExport: function(ev) {
        this.model.set('userExportVal', this.$(".show-slider-data-second").val());
      }
    });


    var ExportWidget = BaseWidget.extend({

      modelEvents: {
        'change:userExportVal': 'exportRecords'
      },

      initialize : function(options){
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
            this.model.set('query', feedback.query.url());
            this.model.set('numFound', feedback.numFound);
            break;
        }
      },

      exportRecords: function() {
        var max = this.model.get('userExportVal') || this.model.get('maxExport');
        var q = this.getCurrentQuery();
        q = q.clone();
        q.unlock();
        q.set('rows', max);
        q.set('fl', 'bibcode');
        var self = this;
        this._executeApiRequest(q)
          .done(function(apiResponse) {
            var ids = _.map(apiResponse.get('response.docs'), function(d) {return d.bibcode});
            self.export(self.model.get('format') || 'bibtex', ids);
          });
      },

      export: function(format, data) {
        var self = this;

        if (data instanceof ApiQuery || !data) {
          var q = data || this.getCurrentQuery();
          if (q) {
            var d = this.getQueryInfo(q);
            d.done(function(numFound) {
              var toSet = {};
              if (numFound == -1) {
                toSet['msg'] = 'Unknown number of results'
              }
              else {
                toSet['numFound'] = numFound;
                toSet['maxExport'] = Math.min(numFound, self.maxExport);
              }
              this.model.set(toSet);
            })
          }
        }
        else { // set of identifiers
          this._getExport(data)
            .done(function(exports) {
              if (exports && exports.has('data')) {
                self.model.set('exports', exports.get('data'));
              }
              else {
                console.error('The export did return some garbage, tfuj!', exports);
              }
            });
        }
      },


      /**
       * Returns 'numFound' for the given query
       *
       * @param apiQuery
       * @returns {*}
       */
      getQueryInfo : function(apiQuery){
        var q = apiQuery || this.getCurrentQuery();
        var defer = $.Deferred();

        if (q) {
          if (this.model.get('query') == q.url() && this.model.has('numFound')) {
            defer.resolve(this.model.get('numFound'));
            return;
          }

          var req = this.composeRequest(q);
          if (req) {
            this.pubsub.subscribeOnce(this.pubsub.DELIVERING_RESPONSE, _.bind(function(apiResponse) {
              defer.resolve(apiResponse.get('response.numFound'));
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

      _getExport: function(identifiers, format){

        if (!_.isArray(identifiers)) throw new Error('Identifiers must be an array');
        if (identifiers.length <= 0) throw new Error('Do you want to export nothing? Let me be!');

        format = format || this.model.get('format');
        var self = this;
        var q = new ApiQuery();
        q.set('identifiers', identifiers);
        q.set('format', format);

        var req = this.composeRequest(q);
        req.set('target', '/export');

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


      post : function (path, params, method) {
        method = method || "post"; // Set method to post by default if not specified.

        var $f = $("<form/>", {method : method, action : path, "target": "_blank"});

        if ($.isArray(params)){
          _.each(params, function(l,i){
            l = _.pairs(l)[0];
            var hiddenField = $("<input>", {type : "hidden", "name": l[0], value: l[1]});
            $f.append(hiddenField);
          })
        }
        $f.submit();
      }

    });


    return ExportWidget;
  });