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
  'hbs!./templates/aria-announcement',
  'hbs!./templates/master-page-manager',
  'marionette',
  'js/mixins/dependon'

], function(
  BaseWidget,
  GenericModule,
  PageManagerController,
  AriaAnnouncementTemplate,
  MasterPageManagerTemplate,
  Marionette,
  Dependon
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

      className : "s-master-page-manager",

      constructor: function(options) {
        options = options || {};
        if (!options.collection)
          options.collection = new WidgetCollection();

        if (!options.model)
          options.model = new WidgetModel();
        options.template = MasterPageManagerTemplate;
        Marionette.ItemView.prototype.constructor.call(this, options);
      },

      collectionEvents : {
        "change:isSelected" : "changeManager",
        "change:options": "changeWithinManager"
      },

      //transition between page managers
      changeManager: function(model, opts){

        var within = (opts.flag == "within");

        if (model.attributes.isSelected) {
          // call the subordinate page-manager
          var res = model.attributes.object.show.apply(model.attributes.object, model.attributes.options);

          if (this.notFirst && !within){
            //only show transitions for subsequent pages
            this.$(".dynamic-container").append(res.$el.addClass("fade-in"));
          }
          else if (!within) {
            this.$(".dynamic-container").append(res.el);
            this.notFirst = true;
          }
          model.attributes.numAttach += 1;

          //scroll up automatically
          window.scrollTo(0,0);
          //and fix the search bar back in its default spot
          $(".s-search-bar-full-width-container").removeClass("s-search-bar-motion");
          $(".s-quick-add").removeClass("hidden");
        }
        else {
          if (model.attributes.object.view.$el.parent().length > 0) {
            model.attributes.object.view.$el.detach();
            model.attributes.numDetach += 1;
          }
        }
        this.render();
      },

      //transition widgets within a manager
      changeWithinManager : function(model){
        this.changeManager(model, {flag: "within"})
      },

      render: function() {
        // render only once
        if (!this._rendered) {
          Marionette.ItemView.prototype.render.apply(this);
          this._rendered = true;
        }
        return this;
      }
    }
  );

  var MasterPageManager = PageManagerController.extend({
    constructor: function(options) {
      options = options || {};
      this._managers = {};
      _.extend(this, _.pick(options, ['debug']));
      this.view = new MasterView(options);
      this.collection = this.view.collection;
      this.model = this.view.model;
      this.initialize();
    },

    activate: function(beehive) {
      this.setBeeHive(beehive);
      var pubsub = this.getPubSub();
      pubsub.subscribe(pubsub.ARIA_ANNOUNCEMENT, this.handleAriaAnnouncement);
    },

    assemble: function(app) {
      this.setApp(app);
      PageManagerController.prototype.assemble.call(this, app);
    },


    show: function(pageManager, options) {
      //this.model.set('numCalled', this.model.attributes.numCalled+1, {silent: true});

      var app = this.getApp();
      var pm = app._getWidget(pageManager); // will throw error if not there

      if (pm && pm.assemble) {
        this.currentChild = pageManager;

        if (!this.collection.find({'id': pageManager}))
          this.collection.add({'id': pageManager});

        var coll = this.collection.find({id: pageManager});

        if (!coll.attributes.isSelected) {
          this.hideAll();
        }

        this.getPubSub().publish(this.getPubSub().ARIA_ANNOUNCEMENT, pageManager);

        pm.assemble(app);
        coll.set({'isSelected': true, options: options, object: pm});
      }
      else {
        console.error('eeeek, you want me to display: ' + pageManager + ' (but I cant, cause there is no such Page!)')
      }
    },

    getCurrentActiveChild: function() {
      return this.collection.get(this.currentChild).get('object'); // brittle?
    },

    /**
     * Return the instances that are under our control and are
     * not active any more
     */
    hideAll: function() {
      _.each(this.collection.models, function(model) {
        if (model.attributes.isSelected) {
          var pManager = model.get('object');
          var app = this.getApp();
          if (app) {
            app.returnWidget(model.get('id'));
          }
          else if (pManager) {
            pManager.destroy();
          }
          model.set('isSelected', false, 'object', null);
        }
      }, this);
    },

    handleAriaAnnouncement: function(msg) {
      //template will match the page name with the proper message
      $("#aria-announcement-container").text(AriaAnnouncementTemplate({page : msg}));
    },

    /**
     * Will find and insert any widget that is still not filled on the page
     * @param app
     */
    insertMasterWidgets: function(app){
      //for header and footer
      //var nav = app.getWidget("NavbarWidget");
      //$("#navbar-container").append(nav.render().el);

    }

  });

  _.extend(MasterPageManager.prototype, Dependon.App);
  return MasterPageManager;

});