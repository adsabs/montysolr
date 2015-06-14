define([
  "marionette",
  "hbs!../templates/library-container",
  "hbs!../templates/library-item",
  "hbs!../templates/library-item-edit",
  "hbs!../templates/empty-collection"

], function(
  Marionette,
  LibraryContainer,
  LibraryItemTemplate,
  LibraryItemEditTemplate,
  EmptyCollectionTemplate

  ){


  var LibraryModel = Backbone.Model.extend({

    defaults : function(){
      return {
        bibcode : undefined
      }
    },

    idAttribute : "bibcode"

  });

  var LibraryCollection = Backbone.Collection.extend({

    model : LibraryModel

  });



  var LibraryItemView = Marionette.ItemView.extend({

    getTemplate : function(){

      if ( Marionette.getOption(this, "permission") ) {
            return LibraryItemEditTemplate
      }
      else {
        return LibraryItemTemplate
      }
    },

    triggers : {
      "click .remove-record" : "removeRecord"
    },

    className : "library-item write-permission"

  });

  var EmptyLibraryView = Marionette.ItemView.extend({

    template : EmptyCollectionTemplate

  });

  var LibraryCollectionView = Marionette.CompositeView.extend({

    initialize : function(options){

      var options = options || {};
      //collection will be passed by widget on view creation

    },

    template : LibraryContainer,

    className : "library-detail-view",

    childView : LibraryItemView,

    childViewContainer : ".library-list-container",

    childViewOptions : function(){
      if ( Marionette.getOption(this, "permission")){
        return {permission : true}
      }
    },

    childEvents : {
      "removeRecord" : "removeRecord"
    },

    emptyView : EmptyLibraryView,

    removeRecord : function(view){
      var bibcode = view.model.get("bibcode");
      this.trigger("removeRecord", bibcode);
    },

    //override this method to provide animation
    removeChildView: function(view) {

      var that = this;

      if (view) {
        this.triggerMethod('before:remove:child', view);

        view.$el.addClass("animated").addClass("fadeOut");

        setTimeout(function () {

          if (view.destroy) {
            view.destroy();
          } else if (view.remove) {
            view.remove();
          }

          delete view._parent;
          that.stopListening(view);
          that.children.remove(view);
          that.triggerMethod('remove:child', view);

          that._updateIndices(view, false);

          return view;

        }, 750);

      }
    }
  });


  //store this for the widget
  LibraryCollectionView.Collection = LibraryCollection;

  return LibraryCollectionView;

})