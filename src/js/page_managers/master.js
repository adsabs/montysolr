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
  'hbs!js/page_managers/templates/aria-announcement',
  'hbs!js/page_managers/templates/master-page-manager',
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

      //transition between page managers
      changeManager: function() {

        var model = this.collection.findWhere({ isSelected : true });
        // call the subordinate page-manager
        var res = model.attributes.object.show.apply(model.attributes.object, model.attributes.options);

        //detach previous controller
        this.$(".dynamic-container").children().detach();
        this.$(".dynamic-container").append(res.$el);
        model.attributes.numAttach += 1;

        //scroll to top
        document.body.scrollTop = document.documentElement.scrollTop = 0;
        //and fix the search bar back in its default spot
        $(".s-search-bar-full-width-container").removeClass("s-search-bar-motion");
        $(".s-quick-add").removeClass("hidden");

      },

      //transition widgets within a manager
      changeWithinManager : function(){

        var model = this.collection.findWhere({ isSelected : true });
        model.attributes.object.show.apply(model.attributes.object, model.attributes.options);
        model.attributes.numAttach += 1;
      }
    }
  );

  var MasterPageManager = PageManagerController.extend({

    initialize : function(options){
      options = options || {};
      this.view = new MasterView(options);
      this.collection = this.view.collection;
      this.model = this.view.model;
      PageManagerController.prototype.initialize.apply(this, arguments);
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

    show: function(pageManagerName, options) {
      var app = this.getApp();

      if (!this.collection.find({'id': pageManagerName})) {
        this.collection.add({'id': pageManagerName});
      }
      
      var pageManagerModel = this.collection.find({id: pageManagerName});

      //if the model does not already reference the actual manager widget, add it now
      var pageManagerWidget;
      if (pageManagerModel.get('object')) {
        pageManagerWidget = pageManagerModel.get('object');
      }
      else {
        pageManagerWidget = app._getWidget(pageManagerName); // will throw error if not there
        pageManagerModel.set('object', pageManagerWidget);
      }

      if (!pageManagerWidget) { console.error("unable to find page manager: " + pageManagerName) }

      if (pageManagerWidget.assemble) {
        // assemble the new page manager (while the old one is still in place)
        pageManagerWidget.assemble(app);
      }
      else {
        console.error('eeeek, ' + pageManager + ' has no assemble() method!');
      }

      // it's a new page
      if (!pageManagerModel.get('isSelected')){
        pageManagerModel.set({options: options, object : pageManagerWidget});
        this.collection.selectOne(pageManagerName);
        this.view.changeManager();
      }
      else {
        //it's within a page
        pageManagerModel.set({options : options, object : pageManagerWidget});
        //it's already selected, trigger a change within the manager
        this.view.changeWithinManager();
      }

      var previousPMName = this.currentChild;
      this.currentChild = pageManagerName;

      // disassemble the old one (behind the scenes)
      if (previousPMName && previousPMName != pageManagerName) {
        var oldPM = this.collection.find({id: previousPMName});

        if (oldPM && oldPM.get('object')){
          oldPM.set("numDetach", oldPM.get("numDetach") + 1);
          oldPM.get('object').disAssemble(app);
        }
      }

      this.getPubSub().publish(this.getPubSub().ARIA_ANNOUNCEMENT, pageManagerName);
    },

    //used by discovery mediator
    getCurrentActiveChild: function() {
      return this.collection.get(this.currentChild).get('object'); // brittle?
    },

    /**
     * Return the instances that are under our control and are
     * not active any more
     */
    disAssemble: function() {
      _.each(this.collection.models, function(model) {
        if (model.attributes.isSelected) {
          var pManager = model.get('object');

          if (pManager.disAssemble) {
            pManager.disAssemble(this.getApp());
          }
          else if (pManager.destroy) {
            pManager.destroy();
          }
          else {
            throw new Error('Contract breach, no way to get ridd of the widget/page manager');
          }
        }
        model.set({'isSelected': false, 'object': null});
        this.assembled = false;
      }, this);
    },

    handleAriaAnnouncement: function(msg) {
      //template will match the page name with the proper message
      //this doesn't work using voiceover when it's inside a div container for some infuriating reason,
      //the skip to link becomes unfocusable
      $("a#skip-to-main-content").remove();
      $("div#aria-announcement-container").remove();
      $("#app-container").before(AriaAnnouncementTemplate({page : msg}));
    }

  });

  _.extend(MasterPageManager.prototype, Dependon.App);
  return MasterPageManager;
});
