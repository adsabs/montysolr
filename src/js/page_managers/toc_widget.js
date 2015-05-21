define([
  'backbone',
  'marionette',
  /*default template is abstract nav, other instances will need to provide their own*/
  'hbs!./templates/abstract-nav',

], function(
  Backbone,
  Marionette,
  tocNavigationTemplate
  ){

  /*
  * widget to coordinate the showing of other widgets within the framework of a TOC page manager
  * mark up in the page manager looks like this:
  *
  * <div data-widget="TOCWidget"
   data-ShowAbstract='{"title": "Abstract", "path":"abstract", "showCount": false, "isSelected":true, "category":"view"}'
   data-ShowCitations='{"title": "Citations", "path":"citations", "category":"view"}'
   data-ShowReferences='{"title": "References", "path":"references", "category":"view"}'
   ...etc...
   />
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
        //arg to pass to widget
        arg : undefined
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

    constructor: function(options) {
      options = options || {};
      if (!options.collection)
        options.collection = new WidgetCollection();

      if (!options.model)
        options.model = new WidgetModel();

      Marionette.ItemView.prototype.constructor.call(this, options);
      this.on("page-manager-message", this.onPageManagerMessage);
    },

    serializeData : function(){
      var data = {},
          groupedCollectionJSON;

      data = _.extend(data, this.model.toJSON());

      groupedCollectionJSON = _.groupBy(this.collection.toJSON(), function(object){
        return object.category;
      });

      data = _.extend(data, groupedCollectionJSON);

      return data;
    },

    template : tocNavigationTemplate,

    events : {
      "click a" : function(e){
       var $t  = $(e.currentTarget);
        var idAttribute = $t.find("div").attr("data-widget-id");
        //it's inactive
        if ($t.find("div").hasClass("s-nav-inactive")){
          return false;
        }
        //it's active
        else if (idAttribute !== $(".s-nav-active").attr("data-widget-id")) {
          var href = $(e.currentTarget).attr("href");
          if (idAttribute.indexOf("__") > -1){
            //currently only used by export
            var widgetName = idAttribute.split("__")[0];
            this.trigger('page-manager-event', 'widget-selected', {idAttribute: widgetName , href : href});
          }
          else {
            this.trigger('page-manager-event', 'widget-selected', {idAttribute: idAttribute, href : href});
          }
        }
        return false;
      }
    },

    modelEvents : {
      "change:bibcode" : "resetActiveStates"
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
      var alwaysThere = ["ShowAbstract", "ShowPaperExport__bibtex", "ShowPaperExport__aastex", "ShowPaperExport__endnote", "ShowPaperExport__classic" ];
      this.collection.each(function(model){
        //abstract and all export options
        //reset only widgets that aren't there 100% of the time
        if (!_.contains(alwaysThere, model.id)){
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
        var widgetId = arguments[1]; var parent = this.$el.parent();
        var tocData = parent.data(widgetId.toLowerCase());
        if (tocData) {
          var toAdd = _.extend(_.clone(tocData), {id : widgetId });
          this.collection.add(toAdd);
        }
        //right now this handles export widget
        else {
          //id consists of widgetId + arg param
          var pairs = _.pairs(parent.data());
          var widgetList = _.where(pairs, function(v){return  v[0].split("__") && (v[0].split("__")[0] == widgetId.toLowerCase()) });
          _.each(widgetList, function(w){
            //arg is the identifying factor-- joining with double underscore so it can be split later
            var toAdd = _.extend(_.clone(w[1]), { id : widgetId+ "__" +  w[0].split("__")[1]});
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
    }

  });

  return TocNavigationView;
});