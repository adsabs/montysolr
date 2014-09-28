define([
  'backbone',
  'marionette',
  'hbs!./templates/abstract-nav',

], function(
  Backbone,
  Marionette,
  abstractNavTemplate
  ){


  var AbstractNavModel = Backbone.Model.extend({

    defaults : function(){
      return {
        id : undefined,
        currentSubView : false,
        numFound : 0

      }
    }


  })

  var AbstractNavCollection = Backbone.Collection.extend({

    model : AbstractNavModel,


    setActive: function(subPage){

      this.each(function(m){
        if (m.get("id")=== subPage){

          m.set("currentSubView", true)

        }
        else {

          m.set("currentSubView", false)
        }
      })

      this.trigger("setActive");

    },

    defaults : [ {id:"bibcode"},
      {id : "abstract", currentSubView : true, title : "Abstract"},
      {id: "citations", showCount : true, title: "Citations"},
      {id : "references", showCount : true, title: "References"},
      {id : "coreads", title : "Co-Reads"},
      {id: "tableofcontents", title: "Table of Contents"},
      {id: "similar", title: "Similar (to add)"}

    ]

})

  var AbstractBibcodeModel = Backbone.Model.extend({

    defaults : function(){
      return {
        bibcode : undefined
      }
    }
  })

  var AbstractNavView = Marionette.ItemView.extend({

    serializeData : function(){

      var data = {};

      data = _.extend(data, this.model.toJSON());

      data = _.extend(data, {items : this.collection.toJSON()});

      return data;

    },

    template : abstractNavTemplate,

    events : {
      "click a" : function(e){
        var $t  = $(e.currentTarget);
        var idAttribute = $t.find("div").attr("id");
        if ($t.find("div").hasClass("s-abstract-nav-inactive")){
          return false
        }
        else if (idAttribute !== $(".s-abstract-nav-active").attr("id")) {
          this.emitNavigateEvent($t);
          this.collection.setActive(idAttribute)

        }

        return false

      }
    },

    modelEvents : {

      "change:bibcode" : "render"

    },

    collectionEvents : {

      "setActive" : "render",
      "change:numFound" : "render"

    },

    emitNavigateEvent : function($t){
      var route = $t.attr("href");
      //taking only final path
      this.trigger("navigate", route);
    }

  })


  return {

    returnNewAbstractNavView: function(){

      return  new AbstractNavView({collection: new AbstractNavCollection(), model: new AbstractBibcodeModel()});

    }

  }


})