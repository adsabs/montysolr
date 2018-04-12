
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

        //TODO: add in generic handler for this error

        return; // do not bother with the rest
      }

      // too many results
      if (feedback.numFound > 1000) {
        app.getWidget('SearchWidget').done(function(widget) {
          // make the search form pulsate little bit
          if (widget.view && widget.view.highlightFields) {
            widget.view.highlightFields();
          }
        });
      }

      // retrieve ids of all components that wait for a query
      var ids = _.keys(feedback.cycle.waiting);

      // remove alerts from previous searches
      this.getAlerter().alert(new ApiFeedback({
        type: Alerts.TYPE.INFO,
        msg: null}));

    };


    /**
     * This applies to the first request submitted by the user; from the search
     * form. So it is (perhaps not always) admissible to bother them with modal
     * messages.
     */
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

      function getAccessTokenStump() {
        var a = app.getService('Api');
        if (a && a.access_token) {
          return a.access_token.slice(30);
        }
        else {
          return 'no access token';
        }
      };

      analytics('send', 'event', 'error', 'search', xhr.statusText);

      if (xhr && apiRequest) {

        var target = apiRequest.get('target');
        errorDetails.target = target;
        errorDetails.query = apiRequest.get('query').toJSON();

        switch(xhr.status) {
          case 403:
            analytics('send', 'event', 'error', 'access-denied', 'request=' + apiRequest.url() + ' token=...' + getAccessTokenStump());
            alerts.alert(new ApiFeedback({
              code: ApiFeedback.CODES.ALERT,
              msg: "We are sorry, you do not have permission to access: " + target + ". This is likely our fault (misconfiguration) and the ADS team has been notified.",
              modal: true
            }));
            return;
          case 429:
            alerts.alert(new ApiFeedback({
              code: ApiFeedback.CODES.ALERT,
              msg: "We are sorry, it looks like you have exhausted allowed quota for: " + target + ". The limits will be reset in (usually) in 24 hours. If you believe this is an error or if you need to do something special, please contact us using the feedback button and describe what you are trying to accomplish. We may be able to help or provide efficient way of searching through ADS database. We also have an API that can be useful!",
              modal: true
            }));
            return;
          case 405: // method not allowed
            analytics('send', 'event', 'error', 'access-denied', 'request=' + apiRequest.url() + ' token=...' + getAccessTokenStump);
            alerts.alert(new ApiFeedback({
              code: ApiFeedback.CODES.ALERT,
              msg: "I am afraid you have just discovered a glitch in our system. (Congratulations!). What you are trying to do should be possible. It is likely a mis-configuration. Please try to reload the webpage; if problem persists - then send us an angry email and wait for a day.",
              modal: true
            }));
            return;
          case 401: // unauthorized
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
                  analytics('send', 'event', 'error', 'unauthorized', 'request=' + apiRequest.url() + ' token=...' + getAccessTokenStump());
                  alerts.alert(new ApiFeedback({
                    msg: "Seems like you are not authorized to access: " + target ,
                    modal: true,
                    type: "danger"
                  }));
                }});
              })
              .fail(function() {
                // TODO: check the status response (if API is down, we shouldn't tell them to reload)
                alerts.alert(new ApiFeedback({
                  code: ApiFeedback.CODES.DANGER,
                  msg: "The security token used to access our API has expired and we are unable to refresh it (though we tried). You will have to reload the webpage. Sorry about that!",
                  modal: true
                }));
              });
            return;
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
              msg: (msg || 'There is something wrong with the query:') +
              '<b><a href="#">&nbsp;&nbsp;Try looking at the search examples on the home page</a></b> ' +
              ' or <b><a href="https://adsabs.github.io/help/search/search-syntax" target="_blank">reading our help page</a>.</b>',
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

      // generic situation; do not use modal - display stuff in the top area
      // here we deal with a serious situation; the API is probably down or has
      // problems; and this is likely the second time we are seeing it (usually
      // query mediator has already tried to recover from these errors)
      
      analytics('send', 'event', 'error', 'unrecoverable-' + xhr.status, 'request=' + apiRequest.url() + ' token=...' + getAccessTokenStump);
      
      var msg;
      switch(xhr.status) {
        case 504: // gateway timeout
        case 408: // proxy timeout
          msg = "This particular query is taking too long. Please wait a moment and retry. If the problem persits, our backend is likely overwhelmed and will be sluggish for a little longer."
          break;
        case 500: // server error
        case 502: // bad gateway
        case 503: // service unavailable
          msg = "We are experiencing troubles accessing: " + target + " Probably the ADS backend service is down or is dealing with some other serious issues (terrorist attack, nuclear explosion, asteroids etc). Please be patient, our systems have been notified!"
          break;
        default:
          msg = "Today, we are double unlucky. Not only are you experiencing 'unrecoverable' error. In addition to this, you have to read this lame default message. Sorry about that. We have been notified about the problem."
          break;
      }

      alerts.alert(new ApiFeedback({
        code: ApiFeedback.CODES.ALERT,
        msg: msg,
        modal: false
      }));

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
