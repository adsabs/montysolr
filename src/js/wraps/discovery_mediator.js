
define([
    'underscore',
    'jquery',
    'js/components/feedback_mediator',
    'js/components/api_feedback',
    'js/widgets/widget_states',
    'js/components/alerts'
  ],

  function (
    _,
    $,
    FeedbackMediator,
    ApiFeedback,
    WidgetStates,
    Alerts
    ) {


    var handlers = {};

    handlers[ApiFeedback.CODES.MAKE_SPACE] = function(feedback) {
      var mpm = this.getApp().getObject('MasterPageManager');
      if (mpm) {
        var child = mpm.getCurrentActiveChild();
        if (child.view && child.view.showCols) {
          child.view.showCols({right: false});
          // open the view again
          this.pubsub.once(this.pubsub.NAVIGATE + ' ' + this.pubsub.START_SEARCH,
            _.once(function() {child.view.showCols({right:true})}));
        }
      }
    };

    handlers[ApiFeedback.CODES.SEARCH_CYCLE_STARTED] = function(feedback) {

      var app = this.getApp();

      if (feedback.query) {
        app.getObject('AppStorage').setCurrentQuery(feedback.query);
      }
      else {
        app.getObject('AppStorage').setCurrentQuery(null);
      }

      app.getService('Navigator').navigate('results-page');

      if (feedback.request.get('target').indexOf('search') > -1 && feedback.query && !feedback.numFound) {
        var q = feedback.query;

        //TODO: in the future, we can look inside the query and decide whether they would like to expand it by
        // a) searching fulltext (if there is any unfielded query)
        // b) modifying phrases and/or operators

        var newQuery = q.clone();
        var msg = 'Your query returned 0 results: <a href="#" id="query-assistant">you can use this tool to build a new query.</a>';
        this.getAlerter().alert(new ApiFeedback({
            type: Alerts.TYPE.DANGER,
            msg: msg,
            events: {
              'click a#query-assistant': 'query-assistant'
            }
        }))
        .done(function(name) {
          if (name == 'query-assistant') {
            var app = self.getApp();
            var search = app.getWidget('SearchWidget');
            var q = newQuery.query.get('q').join(' ');
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

      // turn ids into a list of widgets
      var widgets = this.getWidgets(ids);

      // activate loading state
      if (widgets) {
        this.changeWidgetsState(widgets, {state: WidgetStates.WAITING});
      }

      // register handlers which will remove the spinning wheel
      var self = this;
      var pubsub = app.getService('PubSub');
      _.each(ids, function(k) {
        var key = k;
        pubsub.once(pubsub.DELIVERING_RESPONSE + k, function() {
          self.changeWidgetsState(self.getWidgets([key]), {state: WidgetStates.IDLE});
        })
      });

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
      var alerts = self.getAlerter();
      var self = this;


      if (xhr && apiRequest) {
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
                   code: ApiFeedback.CODES.ALERT,
                   msg: 'There is a problem with our API, it does not respond to queries, very sad day for me...',
                   modal: true
                 }));
               });
              return;
            break;
        }


        var target = apiRequest.get('target');

        if (target.indexOf('/search') > -1 && xhr.status == 400) { // wrong query
          var apiQuery = apiRequest.getApiQuery();

          alerts.alert(new ApiFeedback({
            msg: 'Your query seems to be syntactically challenged, <span id="query-assistant">please use this tool to fix it.</span>',
            events: {
              'click a#query-assistant': 'query-assistant'
            }
          }))
          .done(function(name) {
            if (name == 'query-assistant') {
              var app = self.getApp();
              var search = app.getWidget('SearchWidget');
              var q = apiQuery.query.get('q').join(' ');
              if (q)
                search.openQueryAssistant(q);
            }
          });
        }
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
          return this.getBeeHive().getController('Alerts');
        },
        createFeedback: function(options) {
          return new ApiFeedback(options);
        }
      }))();
      _.each(_.pairs(handlers), function(pair) {
        mediator.addFeedbackHandler(pair[0], pair[1]);
      });
      return mediator;
    }

  });
