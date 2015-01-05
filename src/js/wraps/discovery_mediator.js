
define([
    'underscore',
    'jquery',
    'js/components/feedback_mediator',
    'js/components/api_feedback',
    'js/widgets/widget_states',
    'js/components/alerts',
    'js/components/api_response'
  ],

  function (
    _,
    $,
    FeedbackMediator,
    ApiFeedback,
    WidgetStates,
    Alerts,
    ApiResponse
    ) {


    var handlers = {};

    handlers[ApiFeedback.CODES.MAKE_SPACE] = function(feedback) {
      var mpm = this.getApp().getObject('MasterPageManager');
      if (mpm) {
        var child = mpm.getCurrentActiveChild();
        if (child.view && child.view.showCols) {
          child.view.showCols({right: false});
          // open the view again
          this.pubsub.once(this.pubsub.START_SEARCH,
            _.once(function() {child.view.showCols({right:true})}));
        }
      }
      this.pubsub.once(this.pubsub.DELIVERING_REQUEST, _.bind(function(apiRequest, psk) {
        if(this._tmp.callOnce[psk.getId()]) {
          return;
        }
        this._makeWidgetsSpin([psk.getId()]);
        this._tmp.callOnce[psk.getId()] = true;
      }, this));
    };

    handlers[ApiFeedback.CODES.UNMAKE_SPACE] = function(feedback) {
      var mpm = this.getApp().getObject('MasterPageManager');
      if (mpm) {
        var child = mpm.getCurrentActiveChild();
        if (child.view && child.view.showCols) {
          child.view.showCols({right: true});
        }
      }
    };

    handlers[ApiFeedback.CODES.SEARCH_CYCLE_STARTED] = function(feedback) {
      this.reset();
      var app = this.getApp();

      if (feedback.query) {
        app.getObject('AppStorage').setCurrentQuery(feedback.query);
      }
      else {
        app.getObject('AppStorage').setCurrentQuery(null);
      }

      app.getService('Navigator').navigate('results-page');

      if (feedback.request && feedback.request.get('target').indexOf('search') > -1 && feedback.query && !feedback.numFound) {
        var q = feedback.query;

        //TODO: in the future, we can look inside the query and decide whether they would like to expand it by
        // a) searching fulltext (if there is any unfielded query)
        // b) modifying phrases and/or operators

        var newQuery = q.clone();
        var msg = 'Your query returned 0 results: <a href="#" id="query-assistant">you can use this tool to build a new query.</a>';
        this.getAlerter().alert(new ApiFeedback({
            type: Alerts.TYPE.ALERT,
            msg: msg,
            events: {
              'click a#query-assistant': 'query-assistant'
            }
        }))
        .done(function(name) {
          if (name == 'query-assistant') {
            var search = app.getWidget('SearchWidget');
            var q = newQuery.get('q').join(' ');
            if (q)
              search.openQueryAssistant(q);
          }
        });
        return; // do not bother with the rest
      }


      // too many results
      if (feedback.numFound > 1000) {
        var search = app.getWidget('SearchWidget');
        if (search && search.view && search.view.highlightFields)
          // make the search form pulsate little bit
          search.view.highlightFields();
      }

      // retrieve ids of all components that wait for a query
      var ids = _.keys(feedback.cycle.waiting);

      // remove alerts from previous searches
      this.getAlerter().alert(new ApiFeedback({
        type: Alerts.TYPE.INFO,
        msg: null}));

      this._makeWidgetsSpin(ids);

    };


    handlers[ApiFeedback.CODES.SEARCH_CYCLE_FINISHED] = function(feedback) {
      return; // not doing anything right now
      // retrieve ids of all components that wait for a query
      var ids = _.keys(feedback.cycle.inprogress);

      // turn ids into a list of widgets
      var widgets = this.getWidgets(ids);

      // change widget state
      if (widgets) {
        //this.changeWidgetsState(widgets, {state: WidgetStates.IDLE});
      }
    };

    handlers[ApiFeedback.CODES.SEARCH_CYCLE_FAILED_TO_START] = function(feedback) {
      var apiRequest = feedback.request;
      var xhr = feedback.error.jqXHR;

      var app = this.getApp();
      var alerts = this.getAlerter();
      var self = this;
      var resolved = false;

      var errorDetails = {
        target: '',
        msg: xhr.statusText
      };

      if (xhr && apiRequest) {

        var target = apiRequest.get('target');
        errorDetails.target = target;
        errorDetails.query = apiRequest.get('query').toJSON();

        switch(xhr.status) {
          case 401: // unauthorized
          case 404: // for some unknow reason (yet) - 401 comes marked as 404
             // check the Api is working
             app.getApiAccess({reconnect: true})
               .done(function() {
                 // the access_token was refreshed, test the query
                 var api = app.getService('Api');
                 api.request(apiRequest, {done: function() {
                   // we've recovered - restart the search cycle
                   app.getController('QueryMediator').resetFailures();
                   self.pubsub.publish(self.pubSubKey, self.pubsub.START_SEARCH, apiRequest.get('query'));
                 }, fail: function() {
                   alerts.alert(new ApiFeedback({
                     msg: "I'm sorry, you don't have access rights to query: " + apiRequest.get('target'),
                     modal: true
                   }));
                 }});
               })
               .fail(function() {
                 alerts.alert(new ApiFeedback({
                   code: ApiFeedback.CODES.DANGER,
                   msg: 'There is a problem with our API, it does not respond to queries, very sad day for me...please retry later.',
                   modal: true
                 }));
               });
              return;
            break;
        }


        if (target.indexOf('search') > -1) {
          if (xhr.status == 400) { // wrong query syntax
            var apiQuery = apiRequest.get('query');

            var msg = '';
            if (xhr.responseJSON && xhr.responseJSON.error && xhr.responseJSON.error.msg) {
              msg = xhr.responseJSON.error.msg;
              if (msg.indexOf('INVALID_SYNTAX') > -1) {
                // what will remain: Syntax Error, cannot parse doi:a* keyword_schema:arXiv:
                msg = msg.replace('org.apache.solr.search.SyntaxError: INVALID_SYNTAX_CANNOT_PARSE:', '');
                msg = msg.replace('The parser reported a syntax error, antlrqueryparser hates errors!', '');
                msg = msg.replace('Syntax Error, cannot parse', 'Syntax Error, cannot parse<b>') + '</b>';
              }
            }

            alerts.alert(new ApiFeedback({
              msg: (msg || 'There is something wrong with the query,') + ' <a id="query-assistant">please use this tool to fix it.</a>',
              events: {
                'click a#query-assistant': 'query-assistant'
              }
            }))
            .done(function (name) {
              if (name == 'query-assistant') {
                var app = self.getApp();
                var search = app.getWidget('SearchWidget');
                var q = apiQuery.get('q').join(' ');
                if (q) {
                  search.openQueryAssistant(q);
                }
                else {
                  search.openQueryAssistant('ooops, the query is complex (we are not yet ready for that)');
                }
              }
            });
            return; // stop here
          }
          else if (xhr.status == 500) { // server errors; some are recoverable
            if (xhr.responseJSON) {
              var r = xhr.responseJSON;
              if (r.error && r.error.msg) {
                if (r.error.msg.indexOf('maxClauseCount') > -1) { // highlights failed, we can deliver response
                  var response = new ApiResponse(r);
                  var cycle = feedback.cycle;
                  var key = _.keys(cycle.inprogress)[0];
                  var req = cycle.inprogress[key];
                  response.setApiQuery(req.request.get('query'));
                  cycle.done[key] = cycle.inprogress[key];
                  delete cycle.inprogress[key];
                  self.pubsub.publish(self.pubSubKey, self.pubsub.DELIVERING_RESPONSE+key, response);
                  cycle.running = false;
                  app.getController('QueryMediator').startExecutingQueries();
                  return; // we are done!
                }
              }
            }
          }
        }
      }

      if (!resolved) {

        alerts.alert(new ApiFeedback({
          code: ApiFeedback.CODES.ALERT,
          msg: 'The ADS Api is having a bad day, I\'m sorry - I can\'t get data for this request <pre>' + JSON.stringify(errorDetails, null, ' ') + '</pre>' ,
          modal: true
        }));
      }

    };

    handlers[ApiFeedback.CODES.QUERY_ASSISTANT] = function(feedback) {
      var app = this.getApp();
      var search = app.getWidget('SearchWidget');
      var q = null;
      if (feedback.query) {
        q = feedback.query.get('q').join(' ');
      }
      search.openQueryAssistant(q);
    };

    return function() {
      var mediator = new (FeedbackMediator.extend({
        getAlerter: function() {
          return this.getApp().getController(this.alertsController || 'AlertsController');
        },
        createFeedback: function(options) {
          return new ApiFeedback(options);
        },

        _makeWidgetsSpin: function(ids) {
          // turn ids into a list of widgets
          var widgets = this.getWidgets(ids);

          // activate loading state
          if (widgets && widgets.length > 0) {
            this.changeWidgetsState(widgets, {state: WidgetStates.WAITING});
          }

          // register handlers which will remove the spinning wheel
          var self = this;
          var pubsub = this.getApp().getService('PubSub');
          _.each(ids, function(k) {
            var key = k;
            pubsub.once(pubsub.DELIVERING_RESPONSE + k, function() {
              self.changeWidgetsState(self.getWidgets([key]), {state: WidgetStates.RESET});
            })
          });
        },

        reset: function() {
          this._tmp = {callOnce: {}};
        }
      }))();
      _.each(_.pairs(handlers), function(pair) {
        mediator.addFeedbackHandler(pair[0], pair[1]);
      });
      return mediator;
    }

  });
