
define([
    'underscore',
    'jquery',
    'js/components/feedback_mediator',
    'js/components/api_feedback',
    'js/widgets/widget_states',
    'js/components/alerts',
    'js/components/api_response',
    'analytics'
  ],

  function (
    _,
    $,
    FeedbackMediator,
    ApiFeedback,
    WidgetStates,
    Alerts,
    ApiResponse,
    analytics
    ) {

    var handlers = {};

    handlers[ApiFeedback.CODES.MAKE_SPACE] = function(feedback) {
      var mpm = this.getApp().getObject('MasterPageManager');
      if (mpm) {
        var child = mpm.getCurrentActiveChild();
        if (child.view && child.view.showCols) {
          child.view.showCols({right: false, left: false});
          // open the view again
          this.getBeeHive().getService('PubSub').once(this.getPubSub().START_SEARCH,
            _.once(function() {child.view.showCols({right:true})}));
        }
      }
      this.getBeeHive().getService('PubSub').once(this.getPubSub().DELIVERING_REQUEST, _.bind(function(apiRequest, psk) {
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
          child.view.showCols({right: true, left: true});
        }
      }
    };

    handlers[ApiFeedback.CODES.SEARCH_CYCLE_STARTED] = function(feedback) {
      this._tmp.cycle_started = true;

      var app = this.getApp();

      if (feedback.query) {
        app.getObject('AppStorage').setCurrentQuery(feedback.query);
        app.getObject('AppStorage').setCurrentNumFound(feedback.numFound);
      }
      else {
        app.getObject('AppStorage').setCurrentQuery(null);
      }

      this.getPubSub().publish(this.getPubSub().NAVIGATE, "results-page");

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
              var q = newQuery.get('q').join(' ');
              app.getWidget('SearchWidget')
                .done(function(widget) {
                  if (q) {
                    widget.openQueryAssistant(q);
                  }
                  else {
                    widget.openQueryAssistant('ooops, the query is complex (we are not yet ready for that)');
                  }
                });
            }
          });
        return; // do not bother with the rest
      }

      // too many results
      if (feedback.numFound > 1000) {
        app.getWidget('SearchWidget').done(function(widget) {
          // make the search form pulsate little bit
          if (widget.view && widget.view.highlightFields)
            search.view.highlightFields();
        });
      }

      // retrieve ids of all components that wait for a query
      var ids = _.keys(feedback.cycle.waiting);

      // remove alerts from previous searches
      this.getAlerter().alert(new ApiFeedback({
        type: Alerts.TYPE.INFO,
        msg: null}));

    };


    handlers[ApiFeedback.CODES.SEARCH_CYCLE_FAILED_TO_START] = function(feedback) {
      var apiRequest = feedback.request;
      var xhr = feedback.error.jqXHR || {}; // when the response comes from cache, it's empty

      var app = this.getApp();
      var alerts = this.getAlerter();
      var self = this;
      var resolved = false;

      var errorDetails = {
        target: '',
        msg: xhr.statusText
      };

      analytics('send', 'event', 'error', 'search', xhr.statusText);

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
                  self.getPubSub().publish(self.getPubSub().START_SEARCH, apiRequest.get('query'));
                }, fail: function() {
                  alerts.alert(new ApiFeedback({
                    msg: "At the moment we can't connect to : " + apiRequest.get('target') + " \n Please try again later.",
                    modal: true,
                    type: "danger"
                  }));
                }});
              })
              .fail(function() {
                alerts.alert(new ApiFeedback({
                  code: ApiFeedback.CODES.DANGER,
                  msg: 'Our API is currently not responding to queries. Please try again later.',
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
              if (msg.indexOf('SyntaxError') > -1) {
                // what will remain: Syntax Error, cannot parse doi:a* keyword_schema:arXiv:
                msg = msg.replace('org.apache.solr.search.SyntaxError:', '');
                msg = msg.replace('org.apache.solr.common.SolrException:', '');
                msg = msg.replace('INVALID_SYNTAX_CANNOT_PARSE:', '');
                msg = msg.replace(': The parser reported a syntax error, antlrqueryparser hates errors!', '');
                msg = msg.trim();
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
                  app.getWidget('SearchWidget').done(function(widget) {
                    var q = apiQuery.get('q').join(' ');
                    if (q) {
                      widget.openQueryAssistant(q);
                    }
                    else {
                      widget.openQueryAssistant('ooops, the query is complex (we are not yet ready for that)');
                    }
                  });
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
                  self.getPubSub().publish(self.getPubSub().DELIVERING_RESPONSE+key, response);
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
          msg: 'Please try again later. \n The server returned an error: <pre class="pre-scrollable">' +
           JSON.stringify(errorDetails, null, ' ') + '</pre>' ,
          modal: true
        }));
      }

    };

    handlers[ApiFeedback.CODES.API_REQUEST_ERROR] = function(feedback) {

      var req = feedback.request;
      var q = req.get('query');
      var target = req.get('target');
      var psk = feedback.psk;
      var errorDetails = {
        error: feedback.errorThrown,
        errorCode: feedback.error.status,
        destination: target,
        //not all requests will have a query
        query: q ? q.toJSON() : undefined
      };
      var app = this.getApp();
      var alerter = this.getAlerter();

      analytics('send', 'event', 'error', 'api-request', JSON.stringify(errorDetails));

      if (!psk) {
        console.error('We are not going to handle the error (PSK is empty, can\'t identify the component');
        return;
      }

      var n = app.getPluginOrWidgetName(psk.getId());
      if (n) {
        console.error('Query failed for widget: ' + n, errorDetails);

        // If this is an ORCID response and it is an unauthorized response, we will want to tell the user
        // that they should log back in, and deactivate any ORCID widgets.
        if (/orcid/.exec(target) && feedback.error.status == 401) {
          alerter.alert(new ApiFeedback({
            code: ApiFeedback.CODES.ALERT,
            msg: 'Your connection with Orcid is no longer valid, it may be that you revoked your token. To continue using this feature, please log back in to Orcid.',
            modal: true
          }));

          // Turn off ORCID modes
          var orcidApi = app.getService('OrcidApi');
          var user = app.getObject('User');
          orcidApi.signOut();
          user.setOrcidMode(0);

          return; // We are done
        }

        // var widgets = this.getWidgets([psk.getId()]);
        // if (widgets && widgets.length == 0) {
        //   return; // we can ignore it
        // }
        // this.changeWidgetsState(widgets, {state: WidgetStates.RESET});
        // this.changeWidgetsState(widgets, {state: WidgetStates.ERRORED});

        this._tmp.api_failures = this._tmp.api_failures || {};
        this._tmp.api_failures[n] = this._tmp.api_failures[n] || 0;
        this._tmp.api_failures[n] += 1;
        var numErr = this._tmp.api_failures[n];
      }
      else {
        if (!feedback.beVerbose) return;
      }

      // we'll not show messages until search cycle is over
      if (!this._tmp.cycle_started) return;

    };

    handlers[ApiFeedback.CODES.QUERY_ASSISTANT] = function(feedback) {
      var app = this.getApp();
      app.getWidget('SearchWidget').done(function(widget) {
        var q = null;
        if (feedback.query) {
          q = feedback.query.get('q').join(' ');
        }
        widget.openQueryAssistant(q);
      });
    };

    return function() {
      var mediator = new (FeedbackMediator.extend({
        _tmp: {},

        activate: function() {
          FeedbackMediator.prototype.activate.apply(this, arguments);
          var pubsub = this.getPubSub();
          pubsub.subscribe(pubsub.INVITING_REQUEST, _.bind(this.onNewCycle, this));
          pubsub.subscribe(pubsub.ARIA_ANNOUNCEMENT, _.bind(this.onPageChange, this));
          pubsub.subscribe(pubsub.APP_EXIT, _.bind(this.onAppExit, this));

          pubsub.subscribeOnce(pubsub.APP_STARTING, _.bind(this.onAppStarting, this));
        },

        onAppStarting: function() {
          // get the unwrapped version of PubSub
          var pubsub = this.getBeeHive().getService('PubSub');
          var qm = this.getApp().getController('QueryMediator');

          // remove the handler of START_SEARCH - we'll do it in place of search-mediator
          pubsub.unsubscribe(qm.getPubSub().getCurrentPubSubKey(), pubsub.START_SEARCH);

          // insert our own handler
          this.getPubSub().subscribe(pubsub.START_SEARCH, _.bind(function(apiQuery, senderKey) {
            var pubsub = this.getPubSub();
            var app = this.getApp();
            var qm, widget, storage;

            if (!pubsub)
              return; // gone

            qm = app.getController('QueryMediator');

            if (!qm)
              return; // gone

            storage = app.getObject('AppStorage');

            if (storage && storage.getCurrentQuery() ){
              try{
                console.log("URL: ", storage.getCurrentQuery().url());

              }
              catch (e){

              }
            }

            //ignore repeated queries (if the widgets are loaded with data)
            if (storage && storage.hasCurrentQuery() &&
              apiQuery.url() == storage.getCurrentQuery().url() &&
              app.getPluginOrWidgetName(senderKey.getId()) != "widget:SearchWidget" &&
              app.getWidgetRefCount('Results') >= 1
              ) {
              //simply navigate to search results page, widgets are already stocked with data
              if (app.hasService('Navigator')) {
                app.getService('Navigator').navigate('results-page', {replace: true});
                return;
              }
            }

            if (this.getCurrentPage() !== 'SearchPage' && app.getWidgetRefCount('Results') <= 0) {
              // switch immediately to the results page -make widgets listen to the START_SEARCH
              app.getService('Navigator').navigate('results-page', {replace: false});

              // another way to accomplish the same (however, this has the undesired effect of
              // search bar temporarily disappearing - as it is snatched from the previous page
              // and inserted into the other one)

              //widget = app._getWidget('SearchPage');
              //widget.assemble(app);
              //setTimeout(function() {
              //  app.returnWidget('SearchPage');
              //}, 10000);
            }

          qm.getQueryAndStartSearchCycle.apply(qm, arguments);

          }, this))
        },

        onNewCycle: function() {
          this.reset();
        },

        onPageChange: function(msg) {
          msg = msg.replace('Switching to: ', '');
          this._currentPage = msg;
          analytics('send', 'pageview', {
            page: msg
          });
        },

        getCurrentPage: function() {
          return this._currentPage || 'LandingPage';
        },

        onAppExit: function(data) {
          console.log('App exit requested to: ' + data);
          //TODO:rca - save the application history and persist it

          var alerts = this.getAlerter();
          if (data && data.url) {
            if (data.type == 'orcid') {
              alerts.alert(new ApiFeedback({
                code: ApiFeedback.CODES.WARNING,
                msg: '<p>You will be redirected to ORCID.</p> <p>Please sign in with your ORCID credentials and click on the "authorize" button.</p><p><b>This service is experimental. Should you have any suggestions, please let us know at <a href="mailto:adshelp@cfa.harvard.edu">adshelp@cfa.harvard.edu</a></b></p><button class="btn btn-success" id="okOrcid">Take me to ORCID</button>',
                modal: true,
                events: {
                  'click button#okOrcid': 'OK'
                }
              }))
                .done(function(resp) {
                  if (resp == 'OK') {
                    window.location = data.url
                  }
                })
            }
            else {
              window.location = data.url;
            }
          }
        },

        getAlerter: function() {
          return this.getApp().getController(this.alertsController || 'AlertsController');
        },
        createFeedback: function(options) {
          return new ApiFeedback(options);
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
