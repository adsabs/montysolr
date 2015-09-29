define([
  "marionette",
  "../../list_of_things/item_view",
  "hbs!../templates/library-container",
  "hbs!../templates/library-item-edit",
  "hbs!../templates/empty-collection",
  'js/mixins/link_generator_mixin',
  'js/mixins/papers_utils',
  'js/mixins/formatter',
  'bootstrap'


], function(
  Marionette,
  DefaultItemView,
  LibraryContainer,
  LibraryItemEditTemplate,
  EmptyCollectionTemplate,
  LinkGeneratorMixin,
  PapersUtilsMixin,
  FormatMixin,
  Bootstrap

  ){

  //used for sorting
  function toDate(datestring){
    var date = datestring.split("-");
    return new Date(date[0], date[1]);
  }


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


  var LibCollectionModel = Backbone.Model.extend({

    defaults : function(){
      return {
        order: "desc", //asc, desc
        sort: "pubdate", //pubdate, citations, reads
        page: 1,
        pagesToShow : undefined
      }
    }

  });

  var LibraryCollectionView = Marionette.CompositeView.extend({

    initialize : function(options){

      var options = options || {};
      //initialize pagination model
      this.model = new LibCollectionModel();

      this.listenTo(this.collection, "remove", function(){

        if (this.collection.length){
          this.collection.updateNumbers();
          this.render();
        }

      });

      //disable Marionette's auto sorting
      this.sort = false;

      //for pagination
      this.perPage = options.perPage || 50;

      this.setPagesToShow();

    },

    events : {
      "click button.sort-options" : "changeSort",
      "click input[name=order-options]" : "changeOrder",
      "click .pagination a " : "changePage"

    },

    modelEvents : {
      "change:sort" : "render",
      "change:order": "render",
      "change:page" : "render"
    },

    collectionEvents : {
      //why is this necessary?? shouldn't marionette do it anyway :( :(
      "reset" : "render"
    },

    changePage : function(e){
      e.preventDefault();
      var page = $(e.currentTarget).data("page");
      this.model.set("page", page);
    },

    changeSort : function(e){
      e.stopPropagation();
      var sort = $(e.currentTarget).data("sort");
      this.model.set("sort", sort);
    },

    changeOrder : function(e){
      this.model.set("order", $(e.currentTarget).val());
    },

    getRecordsToShow : function(){

      var endIndex = this.model.get("page") * this.perPage,
        startIndex = endIndex - this.perPage,
        range = _.range(startIndex, endIndex);

      this.filter =  function (child, index) {
        if (range.indexOf(index) > -1){
          return true
        }
      }
    },

    setPagesToShow : function(){

      var page = this.model.get("page"),
        lastPage = Math.ceil(this.collection.length/ this.perPage),
        pageRange = _.range(page-4, page+4);

      //add first page link if it's missing
      if (pageRange.indexOf(1) < 0){
        pageRange.unshift(1);
      }

      pageRange = _.filter(pageRange, function(p,i){
        if (p >= 1 && p <= lastPage){
          return true
        }
      });

      var hasRecords = this.collection.length > 1 ? true : false,
        hasPages = this.collection.length > this.perPage ? true : false;

      this.model.set({pagesToShow : pageRange, hasRecords : hasRecords, hasPages : hasPages});

    },

    onBeforeRender : function(){

      this.reorderCollection();
      this.collection.sort();
      //renumber the items
      this.collection.each(function(m, i ){
        m.set("indexToShow", i + 1);
      });

      this.getRecordsToShow();
      this.setPagesToShow();

    },

    reorderCollection : function(model1, model2) {
      var sort = this.model.get("sort"),
        order = this.model.get("order");

      this.collection.comparator = function (model1, model2) {

        if (sort == "num_citations" || sort == "read_count") {
          if (order == "desc") {
            return parseInt(model2.get(sort)) - parseInt(model1.get(sort));
          }
          else {
            return parseInt(model1.get(sort)) - parseInt(model2.get(sort));;
          }
        }
        else if (sort == "pubdate") {

          if (order == "asc") {
            return toDate(model1.get(sort)) - toDate(model2.get(sort));
          }
          else {
            return toDate(model2.get(sort)) - toDate(model1.get(sort));
          }
        }
      }
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
      view.$(".remove-record").html('<i class="fa fa-spinner fa-pulse"></i>');
      var bibcode = view.model.get("bibcode");
      this.trigger("removeRecord", bibcode);
    }

  });


  //store this for the widget
  LibraryCollectionView.Collection = LibraryCollection;

  return LibraryCollectionView;


})