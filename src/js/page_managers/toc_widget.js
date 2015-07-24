define([
  'backbone',
  'marionette'

], function(
  Backbone,
  Marionette
  ){

  /*
   * widget to coordinate the showing of other widgets within the framework of a TOC page manager
   * You need to provide a template with a nav that looks like this: (with the data attributes
   * corresponding to the widgets in the main page manager template)
   *
   * <nav data-widget="TOCWidget"
   data-ShowAbstract='{"title": "Abstract", "path":"abstract", "showCount": false, "isSelected":true, "category":"view"}'
   data-ShowCitations='{"title": "Citations", "path":"citations", "category":"view"}'
   data-ShowReferences='{"title": "References", "path":"references", "category":"view"}'
   ...etc...
   >

   toc widget listens to "new-widget" event and, if it can find teh corresponding data in the markup,
   adds an entry to its nav
   the toc controller will call a navigate event when the toc widget emits a "widget-selected" event
   * */


  var WidgetData = Backbone.Model.extend({
    defaults : function(){
      return {
        id: undefined, // widgetId
        path: undefined,
        title: undefined,
        showCount: false,
        category: undefined,
        isActive : false,
        isSelected: false,
        numFound : 0,
        showCount: true,
        alwaysThere : false

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
        bibcode : undefined,
        query: undefined
      }
    }
  });

  var TocNavigationView = Marionette.ItemView.extend({

    initialize : function(options){
      options = options || {};

      this.collection = options.collection || new WidgetCollection();
      this.model = options.model || new WidgetModel();
      this.on("page-manager-message", this.onPageManagerMessage);
      if (!options.template){
        //for testing
        this.template = function(){return ""};
      }

    },

    //or else controller will detach, and then never put it back
    noDetach : true,

    serializeData : function(){
      var data = this.model.toJSON(),
        col = this.collection.toJSON(),
        groupedCollectionJSON;

      //if any entries from the data has a "category" param, group by that, otherwise, just return it
      if (_.find(col, function(c){ return c.category !== undefined; })){
        groupedCollectionJSON = _.groupBy(this.collection.toJSON(), function(object){
          return object.category;
        });
        data = _.extend(data, groupedCollectionJSON);
      }
      else {
        data = _.extend(data, col);
      }

      return data;
    },

    events : {
      "click a": "navigateToPage"
    },

    navigateToPage :  function (e) {
      var $t = $(e.currentTarget), idAttribute = $t.find("div").attr("data-widget-id");

      var data = { idAttribute : idAttribute };

      //it's inactive
      if ($t.find("div").hasClass("s-nav-inactive")) {
        return false;
      }
      //it's active
      else if (idAttribute !== this.$(".s-nav-active").attr("data-widget-id")) {
        data.href = $t.attr("href");

        var splitName = idAttribute.indexOf("__") > -1 ? idAttribute.split("__") : undefined;

        if (splitName){
          data.idAttribute = splitName[0];
          data.subView = splitName[1];
        }

        //click triggers event that is shared by the navigator, which is responsible for updating the nav view to
        //the correct view
        this.trigger('page-manager-event', 'widget-selected', data);
      }
      return false;
    },

    modelEvents : {
      "change:bibcode" : "resetActiveStates",
      "change" : "render"
    },

    collectionEvents : {
      "change:isActive" : "render",
      "change:isSelected": "render",
      "change:numFound" : "render"
    },

    /*
     every time the bibcode changes (got by subscribing to this.pubsub.DISPLAY_DOCUMENTS)
     clear the collection of isactive and numfound in the models of the toc widget, so that the next view on
     the widget will show the appropriate defaults
     */
    resetActiveStates : function(){
      this.collection.each(function(model){
        //abstract and all export options
        //reset only widgets that aren't there 100% of the time
        if (!model.get("alwaysThere")){
          model.set("isActive", false);
          model.set("numFound", 0);
        }
        else {
          model.set("isActive", true);
        }
      });
    },

    onPageManagerMessage: function(event, data) {
      if (event == 'new-widget') {
        //building the toc collection
        var widgetId = arguments[1];
        var $nav = this.$("nav");
        var tocData = $nav.data(widgetId.toLowerCase());
        if (tocData) {
          var toAdd = _.extend(_.clone(tocData), {id: widgetId });
          this.collection.add(toAdd);
        }
        else {
          //id consists of widgetId + arg param
          var pairs = _.pairs($nav.data());
          var widgetList = _.where(pairs, function (v) {
            return  v[0].split("__") && (v[0].split("__")[0] == widgetId.toLowerCase())
          });
          _.each(widgetList, function (w) {
            //arg is the identifying factor-- joining with double underscore so it can be split later
            var toAdd = _.extend(_.clone(w[1]), { id: widgetId + "__" + w[0].split("__")[1]});
            this.collection.add(toAdd);
          }, this);
        }
      }
      else if (event == 'widget-ready') {
        var model = this.collection.get(data.widgetId);
        _.defaults(data, {isActive: data.numFound ? true : false});
        if (model) {
          model.set(_.pick(data, model.keys()));
        }
      }
      else if (event === "broadcast-payload"){
        this.model.set("bibcode", data.bibcode);
      }
      else if (event == "dynamic-nav"){
        //expects object like {links : [{title: x, id : y}]}
        //insert dynamic nav entries into the nav template

      }
    }

  });

  return TocNavigationView;
});