define([
  "marionette",
  "js/widgets/list_of_things/item_view",
  "js/widgets/list_of_things/widget",
  "js/widgets/list_of_things/paginated_view",
  "js/widgets/list_of_things/model",
  "hbs!js/widgets/library_list/templates/library-container",
  "hbs!js/widgets/library_list/templates/library-item-edit",
  "hbs!js/widgets/library_list/templates/empty-collection",
  'js/mixins/link_generator_mixin',
  'js/mixins/papers_utils',
  'js/mixins/formatter',
  'js/components/api_query',
  'js/components/api_request',
  'js/components/api_response',
  'js/components/api_targets',
  'js/mixins/add_stable_index_to_collection',
  'js/mixins/add_secondary_sort',
  'bootstrap'

], function(
    Marionette,
    DefaultItemView,
    ListOfThingsWidget,
    ListOfThingsPaginatedContainerView,
    PaginatedCollection,
    LibraryContainer,
    LibraryItemEditTemplate,
    EmptyCollectionTemplate,
    LinkGeneratorMixin,
    PapersUtilsMixin,
    FormatMixin,
    ApiQuery,
    ApiRequest,
    ApiResponse,
    ApiTargets,
    PaginationMixin,
    SecondarySort,
    Bootstrap

){


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


  var LibraryContainerView =  ListOfThingsPaginatedContainerView.extend({

    childView : LibraryItemView,
    template : LibraryContainer,
    className : "library-detail-view",
    childViewContainer : ".library-list-container",

    events : {
      "change #sort-select" : "changeSort",
      "click a.page-control": "changePageWithButton",
      "keyup input.page-control": "tabOrEnterChangePageWithInput",
      "click .per-page": "changePerPage"
    },

    modelEvents : {
      "change" : "render"
    },

    changeSort : function(e){
      e.stopPropagation();
      this.model.set("sort", e.target.value);
      //trigger the event explicitly for the parent view
      this.trigger("changeSort");
    },

    childViewOptions : function(){
      if (this.model.get('editRecords')){
        return {permission : true}
      }
    },

    childEvents : {
      "removeRecord" : "removeRecord"
    },

    removeRecord : function(view){
      view.$(".remove-record").html('<i class="fa fa-spinner fa-pulse"></i>');
      var bibcode = view.model.get("bibcode");
      this.trigger("removeRecord", bibcode);
    }

  });

  var LibraryCollectionView = ListOfThingsWidget.extend({

    //called by the navigator
    //this data comes from the router OR from library_individual widget
    setData : function(data){
      //let library view (list of things widget) know about the new library id
      this.model.set({
       public: data.publicView,
       libraryID: data.id,
       editRecords : data.editRecords
     });
    },

    initialize : function(options){

      options = options || {};
      options.collection = new PaginatedCollection();

      options.view = new LibraryContainerView({ collection : options.collection, model : this.model});
      ListOfThingsWidget.prototype.initialize.apply(this, [options]);

      this.view.model = this.model;

      //clear the collection when the model is reset with a new bibcode
      // and set the model to default values
      this.listenTo(this.model, "change:libraryID", function(){
        this.reset();
      });
      this.listenTo(this.view, "all", this.handleViewEvents);
    },

    changeSort : function() {
      //cache sort before reset removes it
      var cachedSort = this.model.get("sort");
      this.reset();
      this.dispatchRequest({sort : cachedSort});
    },

    defaultQueryArguments: {
      fl: 'title,bibcode,author,keyword,pub,aff,volume,year,links_data,[citations],property,pubdate,abstract',
      rows : 25,
      start : 0,
      sort : "date desc"
    },

    activate : function(beehive){
      ListOfThingsWidget.prototype.activate.apply(this, [].slice.apply(arguments));
    },

    onShow : function(){
      this.dispatchRequest();
    },

    composeRequest: function (apiQuery) {

      var endpoint = ApiTargets["LIBRARIES"] + "/" + this.model.get("libraryID");

      return new ApiRequest({
        target: endpoint,
        query: apiQuery,
        options : {
          context : this,
          contentType : "application/x-www-form-urlencoded",
          done : this.createApiResponse.bind(this, apiQuery),
          fail : this.handleError
        }
      });
    },

    handleError : function(err) {
      console.error("data for library list view not received", err)
    },

    createApiResponse : function(apiQuery, resp){
      //hide possible record deleted success method
      this.model.set("itemDeleted", false);

      //might have been an error
      if (_.isString(resp.solr)) {
        throw new Error(resp.solr+ ": list of things widget can't render");
        return
      }
      if (resp.solr.response.docs.length > 1){
        //otherwise show a message urging users to add to collection
        this.model.set("hasRecords", true);
      }
      //set sort
      var sort = resp.solr.responseHeader.params.sort;
      this.model.set({ sort: sort.split(",")[0]});

      resp = new ApiResponse(resp.solr)
      resp.setApiQuery(apiQuery);
      this.processResponse(resp);
    },

    //this is called by list_of_things show:missing handler
    //it's overriding default behavior of going through pubsub
    executeRequest : function(req){
      this.getBeeHive().getService("Api").request(req);
    },

    dispatchRequest : function(queryOptions){
      //uses defaultQueryArguments
      var q = this.customizeQuery(new ApiQuery());

      if (queryOptions){
        q.set(queryOptions);
      }

      //add bibcode sort as secondary option
      SecondarySort.addSecondarySort(q)
      
      var req = this.composeRequest(q);
      this.getBeeHive().getService("Api").request(req);
    },

    processDocs: function(apiResponse, docs, paginationInfo) {
      if (!apiResponse.has('response')) return [];
      var params = apiResponse.get("response");
      var start = params.start || (paginationInfo.start || 0);
      docs = PaginationMixin.addPaginationToDocs(docs, start);

      _.each(docs, function(d, i ){

        d.identifier = d.bibcode;
        d.noCheckbox = true;

        var maxAuthorNames = 3;

        if (d.author && d.author.length > maxAuthorNames) {
          d.extraAuthors = d.author.length - maxAuthorNames;
          var shownAuthors = d.author.slice(0, maxAuthorNames);
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

      return this.parseLinksData(docs);

    },

    reset : function(){
      this.model.set({
        hasRecords : false
      });
      ListOfThingsWidget.prototype.reset.apply(this, arguments);

    },

    handleViewEvents : function (event, arg1, arg2){
      var that = this;

      switch (event) {
        case "changeSort":
          this.changeSort(arg1);
          break;
        case "removeRecord":
          //from library list view
          var data = {bibcode : [arg1], action : "remove"},
              id = this.model.get("libraryID");
          this.getBeeHive().getObject("LibraryController").updateLibraryContents(id, data)
              .done(function(){
                that.reset();
                //flash a success message
                that.model.set("itemDeleted", true);
                var data = that.model.get("sort") ? {sort : that.model.get("sort")} : {};
                that.dispatchRequest(data);
              });
          break;

      }
    }

  });

  _.extend(LibraryCollectionView.prototype, LinkGeneratorMixin);
  _.extend(LibraryCollectionView.prototype, PapersUtilsMixin);
  _.extend(LibraryCollectionView.prototype, FormatMixin);

  return LibraryCollectionView;

})
