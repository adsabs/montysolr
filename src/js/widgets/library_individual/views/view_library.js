define([
  "marionette",
  "../../list_of_things/item_view",
  "hbs!../templates/library-container",
  "hbs!../templates/library-item-edit",
  "hbs!../templates/empty-collection",
  'js/mixins/link_generator_mixin',
  'js/mixins/papers_utils',
  'js/mixins/formatter'


], function(
  Marionette,
  DefaultItemView,
  LibraryContainer,
  LibraryItemEditTemplate,
  EmptyCollectionTemplate,
  LinkGeneratorMixin,
  PapersUtilsMixin,
  FormatMixin

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

    initialize : function(options){
      var options = options || {};
    },

    model : LibraryModel,

    reset : function(docs){
      docs = this.processSolrDocs(docs);
      Backbone.Collection.prototype.reset.call(this, docs);
    },

    updateNumbers : function(){
      this.each(function(m, i){
        m.set("indexToShow", i + 1);
      });
    },

    processSolrDocs : function(docs){

      _.map(docs, function(d, i ){

        d.identifier = d.bibcode;
        d.noCheckbox = true;
        d.indexToShow = i + 1;

        var maxAuthorNames = 3;

        if (d.author && d.author.length > maxAuthorNames) {
          d.extraAuthors = d.author.length - maxAuthorNames;
          shownAuthors = d.author.slice(0, maxAuthorNames);
        } else if (d.author) {
          shownAuthors = d.author
        }

        if (d.author) {
          var l = shownAuthors.length - 1;
          d.authorFormatted = _.map(shownAuthors, function (d, i) {
            if (i == l || l == 0) {
              return d; //last one, or only one
            } else {
              return d + ";";
            }
          })
        }

        if(d["[citations]"] && d["[citations]"]["num_citations"]>0){
          d.num_citations = this.formatNum(d["[citations]"]["num_citations"]);
        }
        else {
          //formatNum would return "0" for zero, which would then evaluate to true in the template
          d.num_citations = 0;
        }

        d.formattedDate = d.pubdate ? this.formatDate(d.pubdate, {format: 'yy/mm', missing: {day: 'yy/mm', month: 'yy'}}) : undefined;
        d.shortAbstract = d.abstract? this.shortenAbstract(d.abstract) : undefined;

        return d;

      }, this);

      docs = this.parseLinksData(docs);
      return docs;

    }

  });

  _.extend(LibraryCollection.prototype, LinkGeneratorMixin);
  _.extend(LibraryCollection.prototype, PapersUtilsMixin);
  _.extend(LibraryCollection.prototype, FormatMixin);


  var LibraryItemView = DefaultItemView.extend({

    template :   LibraryItemEditTemplate,

    triggers : {
      "click .remove-record" : "removeRecord"
    },

    className : "library-item s-library-item write-permission",

    //there is some weirdness with the default render view emptying the element
    render : Marionette.ItemView.prototype.render,

    serializeData : function(){
      var data = this.model.toJSON();
      return _.extend(data, {permission : Marionette.getOption(this, "permission")});
    }

  });

  var EmptyLibraryView = Marionette.ItemView.extend({

    template : EmptyCollectionTemplate

  });

  var LibraryCollectionView = Marionette.CompositeView.extend({

    initialize : function(options){

      var options = options || {};
      //collection will be passed by widget on view creation

//      this.on("all", function(){console.log(this.vid, arguments)})

      this.listenTo(this.collection, "remove", function(){

        if (this.collection.length){
          this.collection.updateNumbers();
          this.render();
        }

      });

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
          that._updateIndices(view, false);
          that.triggerMethod('remove:child', view);
          return view;

        }, 750);

      }
    }
  });


  //store this for the widget
  LibraryCollectionView.Collection = LibraryCollection;

  return LibraryCollectionView;


})