/**
 * A widget for the internal ADS Deployment
 */

define([
    'underscore',
    'jquery',
    'backbone',
    'marionette',
    'bootstrap',

    'js/components/api_query',
    'js/components/api_request',
    'js/components/api_response',
    'js/components/api_feedback',
    'js/components/alerts',

    'js/widgets/base/base_widget',
    'hbs!./templates/widget-view',
    'hbs!./templates/item-view',
    'hbs!./templates/empty-view'
  ],
  function(
    _,
    $,
    Backbone,
    Marionette,
    Bootstrap,
    ApiQuery,
    ApiRequest,
    ApiResponse,
    ApiFeedback,
    Alerts,
    BaseWidget,
    WidgetTemplate,
    ItemTemplate,
    EmptyTemplate
    ){


    var Environment = Backbone.Model.extend({
      defaults : {
        msg: undefined
      }
    });

    var EnvironmentCollection = Backbone.Collection.extend({
      model : Environment
    });


    /**
     * View is 'bound' to the model, so everytime the model changes, the View will
     * update itself (ie. redraw)
     *
     * The idea is simple: your controller will update the model, and the view
     * will then show the model. Your controller will not be updating the view
     * directly. But it can ask the view to provide some information.
     *
     */
    var ItemView = Marionette.ItemView.extend({

      tagName : "div", // this view will create <div class="s-hello">....</div> html node
      className : "s-service",
      template: ItemTemplate,
      events: {
        'click a[data-value]': 'onAction',
        'enter a[data-value]': 'onAction'
      },
      modelEvents: {
        "change" : 'render'
      },

      onAction: function(ev) {
        var v = ev.target.getAttribute('data-value');
        if (v) {
          // send the data back to the controller, which will decide what to do with it
          this.triggerMethod('user:action', v, this.model);
        }
      }

    });

    var EmptyView = Marionette.ItemView.extend({
      template: EmptyTemplate
    });

    var WidgetView = Marionette.CompositeView.extend({
      template: WidgetTemplate,
      childView: ItemView,
      emptyView: EmptyView,
      attachHtml: function(collectionView, itemView, idx){
        var cols = collectionView.model.get('cols');
        if (cols === undefined) {
          collectionView.$el.append(itemView.el);
        }
        else {
          if (idx < cols) {
            if (collectionView.$('thead tr').length == 0) {
              collectionView.$("thead").append($('<tr>'));
            }
            var td = $('<td>');
            td.append(itemView.el);
            collectionView.$("thead tr").append(td);
          }
          else {
            var m = Math.floor(idx / cols);
            if (collectionView.$('tbody tr').length < m) {
              collectionView.$("tbody").append($('<tr>'));
            }

            var td = $('<td>');
            td.append(itemView.el);
            collectionView.$("tbody tr:last").append(td);
          }
        }
      },

      onChildviewUserAction: function(childView, action, model) {
        this.trigger('user:action', action, model);
      }
    });


    var Controller = BaseWidget.extend({

      viewEvents: {
         'user:action': 'onUserAction',
         'render': 'onRender'
      },

      initialize : function(options){
        this.model = new Environment({});
        this.collection = new EnvironmentCollection();
        this.view = new WidgetView({model: this.model, collection: this.collection});
        BaseWidget.prototype.initialize.apply(this, arguments);
      },

      onRender: function() {
        if (this.collection.models.length <= 0) {
          this.onRequest(new ApiQuery({do: 'update'}));
        }
        //this.update([{"previous_versions": [], "status": null, "date_last_modified": "2016-02-18T16:50:59.162201", "application": "myads", "environment": "sandbox", "tested": false, "tag": "v1.0.9:v1.0.0-32-g46c0c55", "version": "v1.0.9:v1.0.0-32-g46c0c55", "date_created": "2016-02-18T16:50:59.162190", "active": ["v1.0.9:v1.0.0-32-g46c0c55"], "commit": null, "deployed": true, "msg": "AWS bootstrapped"}, {"previous_versions": [], "status": null, "date_last_modified": "2016-02-18T16:50:59.724240", "application": "biblib-service", "environment": "eb-deploy", "tested": false, "tag": "v1.0.4:v1.0-14-g15feb65", "version": "v1.0.4:v1.0-14-g15feb65", "date_created": "2016-02-18T16:50:59.724235", "active": ["v1.0.4:v1.0-14-g15feb65"], "commit": null, "deployed": true, "msg": "AWS bootstrapped"}, {"previous_versions": [], "status": null, "date_last_modified": "2016-02-18T16:50:59.302227", "application": "citation_helper_service", "environment": "sandbox", "tested": false, "tag": "cb35350588c7f67e05cd52f359251aebfae047f6:v1.0.2-10-g1cd5fc6", "version": "cb35350588c7f67e05cd52f359251aebfae047f6:v1.0.2-10-g1cd5fc6", "date_created": "2016-02-18T16:50:59.302216", "active": ["cb35350588c7f67e05cd52f359251aebfae047f6:v1.0.2-10-g1cd5fc6"], "commit": null, "deployed": true, "msg": "AWS bootstrapped"}, {"previous_versions": [], "status": null, "date_last_modified": "2016-02-18T16:50:59.320216", "application": "biblib-service", "environment": "sandbox", "tested": false, "tag": "v1.0.4:v1.0.2-10-g1cd5fc6", "version": "v1.0.4:v1.0.2-10-g1cd5fc6", "date_created": "2016-02-18T16:50:59.320205", "active": ["v1.0.4:v1.0.2-10-g1cd5fc6"], "commit": null, "deployed": true, "msg": "AWS bootstrapped"}, {"previous_versions": [], "status": null, "date_last_modified": "2016-02-18T16:50:59.699516", "application": "citation_helper_service", "environment": "eb-deploy", "tested": false, "tag": "v1.0.2:v1.0.0", "version": "v1.0.2:v1.0.0", "date_created": "2016-02-18T16:50:59.699511", "active": ["v1.0.2:v1.0.0"], "commit": null, "deployed": true, "msg": "AWS bootstrapped"}, {"previous_versions": [], "status": null, "date_last_modified": "2016-02-18T16:50:59.673478", "application": "export_service", "environment": "eb-deploy", "tested": false, "tag": "19db04216b5d64394e768f040d3e6bf8fab64723:v1.0-14-g15feb65", "version": "19db04216b5d64394e768f040d3e6bf8fab64723:v1.0-14-g15feb65", "date_created": "2016-02-18T16:50:59.673473", "active": ["19db04216b5d64394e768f040d3e6bf8fab64723:v1.0-14-g15feb65"], "commit": null, "deployed": true, "msg": "AWS bootstrapped"}, {"previous_versions": [], "status": null, "date_last_modified": "2016-02-18T16:50:59.126432", "application": "vis-services", "environment": "sandbox", "tested": false, "tag": "HEAD:v1.0.2-10-g1cd5fc6", "version": "HEAD:v1.0.2-10-g1cd5fc6", "date_created": "2016-02-18T16:50:59.126421", "active": ["HEAD:v1.0.2-10-g1cd5fc6"], "commit": null, "deployed": true, "msg": "AWS bootstrapped"}, {"previous_versions": [], "status": null, "date_last_modified": "2016-02-18T16:50:59.536408", "application": "vis-services", "environment": "eb-deploy", "tested": false, "tag": "HEAD:v1.0.0-1-g974b2a7", "version": "HEAD:v1.0.0-1-g974b2a7", "date_created": "2016-02-18T16:50:59.536403", "active": ["HEAD:v1.0.0-1-g974b2a7"], "commit": null, "deployed": true, "msg": "AWS bootstrapped"}, {"previous_versions": [], "status": null, "date_last_modified": "2016-02-18T16:50:59.075147", "application": "orcid-service", "environment": "sandbox", "tested": false, "tag": "HEAD:v1.0.0-32-g46c0c55", "version": "HEAD:v1.0.0-32-g46c0c55", "date_created": "2016-02-18T16:50:59.075135", "active": ["HEAD:v1.0.0-32-g46c0c55"], "commit": null, "deployed": true, "msg": "AWS bootstrapped"}, {"previous_versions": [], "status": null, "date_last_modified": "2016-02-18T16:50:59.587297", "application": "object_service", "environment": "eb-deploy", "tested": false, "tag": "v1.0.6:v1.0.0-1-g974b2a7", "version": "v1.0.6:v1.0.0-1-g974b2a7", "date_created": "2016-02-18T16:50:59.587293", "active": ["v1.0.6:v1.0.0-1-g974b2a7"], "commit": null, "deployed": true, "msg": "AWS bootstrapped"}, {"previous_versions": [], "status": null, "date_last_modified": "2016-02-18T16:50:59.622533", "application": "recommender_service", "environment": "eb-deploy", "tested": false, "tag": "v1.0.3:v1.0.0-3-g4fd0e07", "version": "v1.0.3:v1.0.0-3-g4fd0e07", "date_created": "2016-02-18T16:50:59.622529", "active": ["v1.0.3:v1.0.0-3-g4fd0e07"], "commit": null, "deployed": true, "msg": "AWS bootstrapped"}, {"previous_versions": [], "status": null, "date_last_modified": "2016-02-18T16:50:59.198212", "application": "graphics_service", "environment": "sandbox", "tested": false, "tag": "v1.0.4:v1.0.2-29-g15d300c", "version": "v1.0.4:v1.0.2-29-g15d300c", "date_created": "2016-02-18T16:50:59.198201", "active": ["v1.0.4:v1.0.2-29-g15d300c"], "commit": null, "deployed": true, "msg": "AWS bootstrapped"}, {"previous_versions": [], "status": null, "date_last_modified": "2016-02-18T16:50:59.741247", "application": "adsws", "environment": "eb-deploy", "tested": false, "tag": "v1.0.0:v1.0.2-18-g69fe4cd", "version": "v1.0.0:v1.0.2-18-g69fe4cd", "date_created": "2016-02-18T16:50:59.741242", "active": ["v1.0.0:v1.0.2-18-g69fe4cd"], "commit": null, "deployed": true, "msg": "AWS bootstrapped"}, {"previous_versions": [], "status": null, "date_last_modified": "2016-02-18T16:50:59.563620", "application": "harbour-service", "environment": "eb-deploy", "tested": false, "tag": "v1.0.2:v1.0.1-1-g269bdaf", "version": "v1.0.2:v1.0.1-1-g269bdaf", "date_created": "2016-02-18T16:50:59.563613", "active": ["v1.0.2:v1.0.1-1-g269bdaf"], "commit": null, "deployed": true, "msg": "AWS bootstrapped"}, {"previous_versions": [], "status": null, "date_last_modified": "2016-02-18T16:50:59.518141", "application": "myads", "environment": "eb-deploy", "tested": false, "tag": "v1.0.9:v1.0.2", "version": "v1.0.9:v1.0.2", "date_created": "2016-02-18T16:50:59.518135", "active": ["v1.0.9:v1.0.2"], "commit": null, "deployed": true, "msg": "AWS bootstrapped"}, {"previous_versions": [], "status": null, "date_last_modified": "2016-02-18T16:50:59.216172", "application": "metrics_service", "environment": "sandbox", "tested": false, "tag": "v1.0.6:v1.0.2-10-g1cd5fc6", "version": "v1.0.6:v1.0.2-10-g1cd5fc6", "date_created": "2016-02-18T16:50:59.216161", "active": ["v1.0.6:v1.0.2-10-g1cd5fc6"], "commit": null, "deployed": true, "msg": "AWS bootstrapped"}, {"previous_versions": [], "status": null, "date_last_modified": "2016-02-18T16:50:59.656393", "application": "graphics_service", "environment": "eb-deploy", "tested": false, "tag": "v1.0.4:v1.0.2", "version": "v1.0.4:v1.0.2", "date_created": "2016-02-18T16:50:59.656389", "active": ["v1.0.4:v1.0.2"], "commit": null, "deployed": true, "msg": "AWS bootstrapped"}, {"previous_versions": [], "status": null, "date_last_modified": "2016-02-18T16:50:59.102434", "application": "adsws", "environment": "sandbox", "tested": false, "tag": "v1.0.0:v1.0.2-17-g1b31375", "version": "v1.0.0:v1.0.2-17-g1b31375", "date_created": "2016-02-18T16:50:59.102422", "active": ["v1.0.0:v1.0.2-17-g1b31375"], "commit": null, "deployed": true, "msg": "AWS bootstrapped"}, {"previous_versions": [], "status": null, "date_last_modified": "2016-02-18T16:50:59.639727", "application": "orcid-service", "environment": "eb-deploy", "tested": false, "tag": "HEAD:v1.0.2", "version": "HEAD:v1.0.2", "date_created": "2016-02-18T16:50:59.639720", "active": ["HEAD:v1.0.2"], "commit": null, "deployed": true, "msg": "AWS bootstrapped"}, {"previous_versions": [], "status": null, "date_last_modified": "2016-02-18T16:50:59.180220", "application": "object_service", "environment": "sandbox", "tested": false, "tag": "v1.0.6:v1.0.2-10-g1cd5fc6", "version": "v1.0.6:v1.0.2-10-g1cd5fc6", "date_created": "2016-02-18T16:50:59.180209", "active": ["v1.0.6:v1.0.2-10-g1cd5fc6"], "commit": null, "deployed": true, "msg": "AWS bootstrapped"}, {"previous_versions": [], "status": null, "date_last_modified": "2016-02-18T16:50:59.266194", "tested": false, "tag": "e1933676ecf8a12bd8be819e0c710e931d4561a1:v1.0.2-31-g37ba320", "commit": null, "active": ["HEAD:v1.0.2-30-g82e8d99", "e1933676ecf8a12bd8be819e0c710e931d4561a1:v1.0.2-31-g37ba320"], "deployed": true, "environment": "sandbox", "application": "harbour-service", "version": "e1933676ecf8a12bd8be819e0c710e931d4561a1:v1.0.2-31-g37ba320", "msg": "AWS bootstrapped", "date_created": "2016-02-18T16:50:59.266183"}, {"previous_versions": [], "status": null, "date_last_modified": "2016-02-18T16:50:59.284185", "application": "export_service", "environment": "sandbox", "tested": false, "tag": "19db04216b5d64394e768f040d3e6bf8fab64723:v1.0.2-10-g1cd5fc6", "version": "19db04216b5d64394e768f040d3e6bf8fab64723:v1.0.2-10-g1cd5fc6", "date_created": "2016-02-18T16:50:59.284174", "active": ["19db04216b5d64394e768f040d3e6bf8fab64723:v1.0.2-10-g1cd5fc6"], "commit": null, "deployed": true, "msg": "AWS bootstrapped"}, {"previous_versions": [], "status": null, "date_last_modified": "2016-02-18T16:50:59.604478", "application": "metrics_service", "environment": "eb-deploy", "tested": false, "tag": "v1.0.5:v1.0.0-3-g4fd0e07", "version": "v1.0.5:v1.0.0-3-g4fd0e07", "date_created": "2016-02-18T16:50:59.604474", "active": ["v1.0.5:v1.0.0-3-g4fd0e07"], "commit": null, "deployed": true, "msg": "AWS bootstrapped"}, {"previous_versions": [], "status": null, "date_last_modified": "2016-02-18T16:50:59.144227", "application": "recommender_service", "environment": "sandbox", "tested": false, "tag": "1d56dd562a9fb18dad615b510f59be622345665e:v1.0.2-10-g1cd5fc6", "version": "1d56dd562a9fb18dad615b510f59be622345665e:v1.0.2-10-g1cd5fc6", "date_created": "2016-02-18T16:50:59.144216", "active": ["1d56dd562a9fb18dad615b510f59be622345665e:v1.0.2-10-g1cd5fc6"], "commit": null, "deployed": true, "msg": "AWS bootstrapped"}]);
      },

      changeState: function() {
        // hack, to avoid the spinning wheel
        // see: https://github.com/adsabs/bumblebee/issues/425
      },

      activate: function (beehive) {
        this.setBeeHive(beehive);
        var pubsub = beehive.getService('PubSub');
        pubsub.subscribe(pubsub.INVITING_REQUEST, _.bind(this.onRequest, this));
        pubsub.subscribe(pubsub.DELIVERING_RESPONSE, _.bind(this.onResponse, this));
        pubsub.subscribe('confirmation', _.bind(this.onUserConfirmation, this));
      },

      onRequest: function(apiQuery) {
        if (!(apiQuery instanceof ApiQuery))
          throw new Error('You are kidding me!');
        var q = apiQuery.clone();
        q.unlock();
        this.dispatchRequest(q); // calling out parent's method
      },

      composeRequest: function (apiQuery) {
        var target = apiQuery.has('action') ? 'command' : 'status';
        return new ApiRequest({
          target: target,
          query: apiQuery
        });
      },

      // triggered externally, by a query-mediator, when it receives data for our query
      onResponse: function(apiResponse) {
        var data = apiResponse.toJSON();
        this.update(data);
      },

      /**
       *
       * @param data
       */
      update: function(data) {
        var coll = this._transform(data);
        this.model.set('cols', coll.cols, {silent: true});
        this.collection.reset(coll.models);
      },

      showAlert: function(msg, modal, events) {
        modal = modal || false;
        var pubsub = this.getPubSub();
        pubsub.publish(pubsub.FEEDBACK, new ApiFeedback({
          code: ApiFeedback.CODES.ALERT,
          msg: msg,
          modal: modal,
          events: events
        }));
      },

      onUserConfirmation: function(data) {
        console.log('user:confirmed', data);
        data = data || {};
        var app = data.application;
        var env = data.environment;
        var action = data.action;
        switch(action) {
          case 'restart':
            this.dispatchRequest(new ApiQuery({action: action, application: app, environment: env}));
            break;
          case 'revert':
            this.dispatchRequest(new ApiQuery({action: 'deploy', application: app, environment: env}));
            break;
          default:
            console.error('Unknown action: ' + action);
        }
      },

      onUserAction: function(action, model, events) {
        var app = model.get('application');
        var env = model.get('environment');
        var action_arg = null;

        if (action.indexOf(':') > -1) {
          var parts = action.split(':');
          action = parts[0];
          action_arg = parts.splice(1, parts.length()).join(':');
        }

        switch(action) {
          case 'show-in-aws':
            //TODO: must discover environment id
            var url = 'https://console.aws.amazon.com/elasticbeanstalk/home?region=us-east-1#/environment/dashboard?applicationName=__app__&environmentId=__eid__';
            url = url.replace('__app__', app);
            url = url.replace('__eid__', env);
            window.open(url, 'AWS ADSDeploy Window');
            break;
          case 'show-details':
            this.showAlert('Not implemented yet', true);
            break;
          case 'restart':
            this.showAlert('Are you sure you want to restart ' + env + ' (application: ' + app + ')? <a href="#">Yes!</a>',
              true,
              {
                'click a': {
                  signal: 'confirmation',
                  action: Alerts.ACTION.CALL_PUBSUB,
                    arguments: {application: app, environment: env, action: action}
                }
              }
            );
            break;
          case 'revert':
            if (action_arg) {
              this.showAlert('Are you sure you want to revert ' + env + ' (' + app + '). To version: ' + action_arg + '? <a href="#">Yes!</a>',
                true,
                {
                  'click a': {
                    signal: 'confirmation',
                    action: Alerts.ACTION.CALL_PUBSUB,
                    arguments: {application: app, environment: env, action: action, version: action_arg}
                  }
                }
              );
            }
            else {
              var msg = ['Please choose the version you want to deploy for ' + env + ' (' + app + ').</br>'];
              var events = {};
              var i = 0;
              _.each(model.get('previous_versions'), function(ver) {
                msg.push('<a id="v' + i + '" href="#">' + ver + '</a>');
                events['click a#v' + i] = {
                  signal: 'confirmation',
                  action: Alerts.ACTION.CALL_PUBSUB,
                  arguments: {application: app, environment: env, action: action, version: ver}
                }
                i = i + 1;
              });
              this.showAlert(msg.join('<br/>'),
                true,
                events
              );
            }
            break;

          default:
            this.showAlert('Unknown action: ' + action, true);

        }
      },

      /*
      transform the data we receive from the endpoint
      - group them by the service name (environment)
      - order by app name
      - insert empty model in place of missing cells
       */
      _transform: function(data) {
        var envs = {};
        var apps = {};

        _.each(data, function(service) {
          var sName = service.environment;
          if (!envs[sName])
            envs[sName] = {};
          envs[sName][service.application] = service;
          apps[service.application] = 1; // unique values
        });

        var appNames = this._sortApps(_.keys(apps));
        var envNames = this._sortEnvs(_.keys(envs));

        var row = [];
        for (var appName in appNames) {
          row.push(this._createAppModel(appNames[appName]))
        }

        for (var envIdx in envNames) {
          var envName = envNames[envIdx];
          var env = envs[envName];
          for (var appIdx in appNames) {
            var appName = appNames[appIdx];
            if (env[appName]) {
              row.push(this._createEnvModel(appName, envName, env[appName]));
            }
            else {
              row.push(this._createEmptyEnvModel(appName, envName));
            }
          }
        }
        var i = 0;
        _.each(row, function(model) {
          model.idx = i++;
        })

        return {
          cols: appNames.length,
          rows: envNames.length,
          models: row
        }
      },

      /*
      return the appnames in the order they should
      be displayed (these are columns)
       */
      _sortApps: function(appNames) {
        return appNames.sort(); //TODO
      },

      _sortEnvs: function(envNames) {
        var envs = _.object(envNames.sort(), _.range(envNames.length));
        var i = -10;
        for (var x in ['adsws', 'bumblebee']) {
          if (envs[x]) {
            envs[x] = i++;
          }
        }

        envs = _.pairs(envs).sort(function(a,b) {return a[1] - b[1]});
        return _.keys(_.object(envs));
      },

      _createAppModel: function(appName) {
        var data = {
          id: appName,
          title: appName
        };
        var options = [
          {title: 'Choose (manul)', command: 'deploy-manual'},
          {title: 'Automatically deploy latest changes (HEAD)', command: 'auto-deploy-head'},
          {title: 'Automatically deploy latest release (tag)', command: 'auto-deploy-tag'},
        ];
        data.options = options;
        return new Environment(data);
      },

      _createEnvModel: function(appName, envName, data) {
        data = _.extend({
          id: appName + ':' + envName,
          title: envName + ' (' + data.version + ')'
        }, data);
        var options = [
          {title: 'Show details', command: 'show-details'},
          {title: 'Show in AWS', command: 'show-in-aws'},
          {title: 'Restart', command: 'restart'},
        ]
        if (data.previous_versions) {
          options.push({title: 'Revert to (' + data.previous_versions[0] + ')', command: 'revert:' + data.previous_versions[0]})
        }
        data.options = options;
        if (data.previous_versions && data.previous_versions.length > 1) {
          data.extra_options = [
            {title: 'Revert to version...', command: 'revert'}
          ]
        }
        return new Environment(data);
      },

      _createEmptyEnvModel: function(appName, envName) {
        return new Environment({
          id: appName + ':' + envName
        });
      },


    });


    return Controller;
  });