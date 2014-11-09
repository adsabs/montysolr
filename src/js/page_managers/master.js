/*
 * Master manager is a simple widget which keeps track of what is
 * inside DOM - and on command swaps/adds/removes the subordinate
 * page managers (plus gives them commands on what to display).
 *
 * Page managers will be discovered automatically, from the
 * application object. But we need to know where in the page
 * should the managers be inserted.
 *
 * */

define([
  'js/widgets/base/base_widget',
  'js/components/generic_module',
  'js/page_managers/controller',
  'marionette'
], function(
  BaseWidget,
  GenericModule,
  PageManagerController,
  Marionette
  ){

  var WidgetData = Backbone.Model.extend({
    defaults : function(){
      return {
        id: undefined, // widgetId
        isSelected : false,
        object: undefined,
        options: undefined // options used for the last show() call
      }
    }
  });

  var WidgetCollection = Backbone.Collection.extend({
    model : WidgetData,
    selectOne: function(widgetId) {
      var s = null;
      this.each(function(m) {
        if (m.id == widgetId) {
          s = m;
        }
        else {
          m.set("isSelected", false, {silent: true});
        }
      });
      s.set("isSelected", true);
    }
  });

  var WidgetModel = Backbone.Model.extend({
    defaults : function(){
      return {
        name: undefined,
        numCalled: 0,
        numAttached: 0,
        ariaAnnouncement: undefined
      }
    }
  });

  var MasterView = Marionette.ItemView.extend({
      constructor: function(options) {
        options = options || {};
        if (!options.collection)
          options.collection = new WidgetCollection();

        if (!options.model)
          options.model = new WidgetModel();

        Marionette.ItemView.prototype.constructor.call(this, options);
      },

      collectionEvents : {
        "change:isSelected" : "onSelectedChange"
      },

      onSelectedChange: function(model) {
        if (model.attributes.isSelected) {
          // call the subordinate page-manager
          var res = model.attributes.object.show.apply(model.attributes.object, model.attributes.options);

          this.$el.append(res.el);
          model.attributes.numAttach += 1;

          //scroll up automatically
          window.scrollTo(0,0);
        }
        else {
          if (model.attributes.object.view.$el.parent().length > 0) {
            model.attributes.object.view.$el.detach();
            model.attributes.numDetach += 1;
          }
        }
        this.render();
      },
      render: function() {
        // ignoring templating
        return this;
      }
    }
  );

  var MasterPageManager = GenericModule.extend({
    constructor: function(options) {
      options = options || {};
      this._managers = {};
      _.extend(this, _.pick(options, ['debug']));
      this.view = new MasterView(options);
      this.collection = this.view.collection;
      this.model = this.view.model;
    },

    activate: function(beehive) {
      this.pubsub = beehive.getHardenedInstance().getService('PubSub');
      this.pubsub.subscribe(this.pubsub.ARIA_ANNOUNCEMENT, this.handleAriaAnnouncement);
    },

    assemble: function(app) {
      this.discoverPageManagers(app);
    },

    discoverPageManagers: function(app) {
      var self = this;
      var a = app;
      _.each([app.getAllModules, app.getAllPlugins, app.getAllWidgets], function(c) {
        _.each(c.call(a), function(m) {
          if (m[1] instanceof PageManagerController && m[1] !== self && m[1].assemble) {
            self.collection.add({'id': m[0], 'object': m[1]});
            m[1].assemble(a);
          }
        });
      });
    },

    show: function(pageManager, options) {
      //this.model.set('numCalled', this.model.attributes.numCalled+1, {silent: true});

      var pm = this.collection.get(pageManager);
      if (pm && pm.get('object')) {

        if (!pm.attributes.isSelected) {
          this.hideAll();
        }

        this.pubsub.publish(this.pubsub.ARIA_ANNOUNCEMENT, "Switching to: " + pageManager);

        pm.set({'id': pageManager, 'isSelected': true, options: options});
      }
      else {
        console.error('eeeek, you want me to display: ' + pageManager + ' (but I cant, cause there is no such Page!)')
      }
    },

    hideAll: function() {
      _.each(this.collection.models, function(model) {
        if (model.attributes.isSelected) {
          model.set('isSelected', false);
        }
      });
    },

    handleAriaAnnouncement: function(msg) {
      //TODO: insert this into the page
      console.log('Aria Announcement: ' + msg);
    }

  });

  return MasterPageManager;

});