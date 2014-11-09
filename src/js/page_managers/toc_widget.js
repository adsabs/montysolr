define([
  'backbone',
  'marionette',
  'hbs!./templates/abstract-nav',

], function(
  Backbone,
  Marionette,
  tocNavigationTemplate
  ){


  var WidgetData = Backbone.Model.extend({
    defaults : function(){
      return {
        id: undefined, // widgetId
        path: undefined,
        title: undefined,
        isActive : false,
        isSelected: false,
        numFound : 0,
        showCount: true
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
      var data = {};
      data = _.extend(data, this.model.toJSON());
      data = _.extend(data, {items : this.collection.toJSON()});
      return data;
    },

    template : tocNavigationTemplate,

    events : {
      "click a" : function(e){
       var $t  = $(e.currentTarget);
        var idAttribute = $t.find("div").attr("data-widget-id");
        if ($t.find("div").hasClass("s-abstract-nav-inactive")){
          return false;
        }
        else if (idAttribute !== $(".s-abstract-nav-active").attr("data-widget-id")) {
          this.trigger('page-manager-event', 'widget-selected', idAttribute);
          this.collection.selectOne(idAttribute);
        }
        return false;
      }
    },

    modelEvents : {
      "change:bibcode" : "render"
    },

    collectionEvents : {
      "add": "render",
      "change:isActive" : "render",
      "change:isSelected": "render",
      "change:numFound" : "render"
    },


    onPageManagerMessage: function(event, data) {
      if (event == 'new-widget') {
        //this.collection.set([new WidgetData({widgetData: arguments[1]})]);
        var widgetId = arguments[1]; var parent = this.$el.parent();
        if (parent.data(widgetId.toLowerCase())) {
          var title = widgetId; var path = '';
          var defs = _.clone(parent.data(widgetId.toLowerCase()));
          defs.id = widgetId;
          this.collection.add(defs);
        }
      }
      else if (event == 'widget-ready') {
        var model = this.collection.get(data.widgetId);
        _.defaults(data, {isActive: data.numFound ? true : false});
        if (model) {
          model.set(_.pick(data, model.keys()));
        }
      }
    }

  });

  return TocNavigationView;
});