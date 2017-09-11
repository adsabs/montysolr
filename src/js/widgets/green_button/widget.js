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
    'hbs!js/widgets/green_button/templates/widget-view',
    'hbs!js/widgets/green_button/templates/item-view',
    'hbs!js/widgets/green_button/templates/empty-view'
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
        this._store = new ApiQuery();
      },

      onRender: function() {
        if (this.collection.models.length <= 0) {
          this.onRequest(new ApiQuery({action: 'status'})); // ask for models
          this.onRequest(new ApiQuery({action: 'store'})); // ask for storage data
        }
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
        var action = (apiQuery.get('action') || ['status'])[0];
        switch (action) {
          case 'status':
            return new ApiRequest({
              target: 'status',
              query: apiQuery
            });
            break;
          case 'command':
            return new ApiRequest({
              target: 'command',
              query: apiQuery
            });
            break;
          case 'store':
            var key = apiQuery.has('key') ? apiQuery.get('key')[0] : 'green-button';
            apiQuery.unset('action');
            apiQuery.unset('key');
            return new ApiRequest({
              target: 'store/' + key,
              query: apiQuery,
              options: {
                contentType: 'application/json',
                type: apiQuery.url() == "" ? 'GET' : 'POST'
              }
            });
            break;
          default:
            console.log('eror, unknown action', apiQuery.url());
        }

      },

      // triggered externally, by a query-mediator, when it receives data for our query
      onResponse: function(apiResponse) {
        var data = apiResponse.toJSON();
        var q = apiResponse.getApiQuery();
        if (data) {
          if (q.has('action')) {
            this.updateCollections(data);
          }
          else {
            if (this.collection.models.length == 0) {
              var self = this;
              _.delay(function() {
                self.refreshStore(data);
              }, 2000);
            }
            else {
              this.refreshStore(data);
            }

          }
        }

      },

      refreshStore: function(data) {
        _.each(data, function(value, key) {
          this._store.set(key, value);
        }, this);
      },

      updateStore: function(data) {
        var q = this._store.clone();
        q.set('action', 'store');
        this.onRequest(q);
      },

      /**
       *
       * @param data
       */
      updateCollections: function(data) {
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
          action_arg = parts.splice(1, parts.length).join(':');
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

          case 'deploy-manual':
          case 'auto-deploy-head':
          case 'auto-deploy-tag':
            var s = action.replace('auto-deploy-', '').replace('deploy-', '');
            this._store.set('deploy-strategy:'+ action_arg, s);

            var m = this.collection.get(action_arg);
            m.set('title', action_arg + ' (' + s + ')');
            var opts = _.clone(m.attributes.options);
            _.each(opts, function(o) {
              o.checked = false;
              if (o.command == action + ':' + action_arg)
                o.checked = true;
            });
            m.set('options', opts);
            this.updateStore();
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
          title: appName + ' (' + (this._store.get('deploy-strategy:' + appName) || ['manual'])[0] + ')'
        };
        var options = [
          {title: 'Choose what to deploy (manual)', command: 'deploy-manual:' + appName},
          {title: 'Automatically deploy latest changes (HEAD)', command: 'auto-deploy-head:' + appName},
          {title: 'Automatically deploy latest release (tag)', command: 'auto-deploy-tag:' + appName},
        ];
        data.options = options;
        return new Environment(data);
      },

      _shortenVersion: function(label) {
        var parts = (label || '').split(':');
        var out = [];
        _.each(parts, function(s) {
          var prefix = ''
          if (s.indexOf('-') > -1) {
            var ps = s.split('-');
            prefix = ps.slice(0,2).join('-');
            s = ps[2];
          }

          var x = (s.substring(0,10));
          if (x.substring(x.length-1) == '-')
            x = x.substring(0, x.length-1);
          out.push(x);
        })
        return out.join(':');
      },

      _createEnvModel: function(appName, envName, data) {
        data = _.extend({
          id: appName + ':' + envName,
          title: envName + ' (' + this._shortenVersion(data.version) + ')'
        }, data, this);
        var options = [
          {title: 'Show details', command: 'show-details'},
          {title: 'Show in AWS', command: 'show-in-aws'},
          {title: 'Restart', command: 'restart'},
        ]
        if (data.previous_versions && data.previous_versions.length > 0) {
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
