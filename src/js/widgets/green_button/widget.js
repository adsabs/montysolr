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
        'click button': 'onButtonClick'
      },
      modelEvents: {
        "change" : 'render'
      },

      onButtonClick: function(ev) {
        if (ev) {
          ev.stopPropagation();
        }
        // send the data back to the controller, which will decide what to do with it
        this.trigger('service-action', this.$el.find('input').val());
      },
      onRender: function() {
        console.log('onRender', arguments)
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
      }
    });


    var Controller = BaseWidget.extend({

      viewEvents: {
         'service-action': 'onServiceAction'
      },

      initialize : function(options){
        this.model = new Environment({});
        this.collection = new EnvironmentCollection();
        this.view = new WidgetView({model: this.model, collection: this.collection});

        BaseWidget.prototype.initialize.apply(this, arguments);
      },

      activate: function (beehive) {
        this.setBeeHive(beehive);
        var pubsub = beehive.getService('PubSub');
        pubsub.subscribe(pubsub.INVITING_REQUEST, _.bind(this.onRequest, this));
        pubsub.subscribe(pubsub.DELIVERING_RESPONSE, _.bind(this.onResponse, this));
      },

      onRequest: function(apiQuery) {
        if (!(apiQuery instanceof ApiQuery))
          throw new Error('You are kidding me!');
        var q = apiQuery.clone();
        q.unlock();
        q.set('command', 'update');
        this.dispatchRequest(q); // calling out parent's method
      },

      // triggered externally, by a query-mediator, when it receives data for our query
      onResponse: function(apiResponse) {
        var data = apiResponse.toJSON();
        this.update(data);
        console.log(data);
      },

      onServiceAction: function(name) {
        if (name.toLowerCase() == 'bumblebee') {
          this.model.set('name', 'wonderful ' + name);
        }
        else {
          this.model.set('name', name);
        }
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
          {title: 'Show in AWS', commmand: 'show-in-aws'},
          {title: 'Restart', commmand: 'restart'},
        ]
        if (data.previous_version) {
          options.push({title: 'Revert to (' + data.previous_version + ')', command: 'revert:' + data.previous_version})
        }
        data.options = options;
        data.extra_options = [
          {title: 'Deploy version...', command:'deploy-version'}
        ]
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