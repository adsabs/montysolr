/**
 * This widget can paginate through the list of 'things' - it accepts a query
 * as an input. It listens to:
 *
 *    INVITING_REQUEST
 *    DELIVERING_RESPONSE
 *
 * If you need to, change the activate() method to listen to other signals
 * or stop listening altogether
 *
 */

define([
    'marionette',
    'backbone',
    'js/components/api_request',
    'js/components/api_query',
    'js/widgets/base/base_widget',
    'hbs!./templates/item-template',
    'hbs!./templates/results-container-template',
    'js/mixins/link_generator_mixin',
    'hbs!./templates/pagination-template',
    'js/mixins/add_stable_index_to_collection'
  ],

  function (Marionette,
    Backbone,
    ApiRequest,
    ApiQuery,
    BaseWidget,
    ItemTemplate,
    ResultsContainerTemplate,
    LinkGenerator,
    PaginationTemplate,
    WidgetPaginationMixin) {


    var PaginationModel = Backbone.Model.extend({

      defaults : function(){
        return {
          perPage : undefined,
          page : 1,
          currentQuery : undefined,
          numFound : undefined
        }
      }

    })

    var PaginationView = Backbone.View.extend({

      initialize : function(options){

        this.listenTo(this.model, "change", this.render)

      },

      template : PaginationTemplate,

      render: function(){

        var page = this.model.get("page");
        var perPage = this.model.get("perPage");

        var pageNums = _.map([-2,-1, 0, 1, 2 , 3, 4], function(d,i){
          var current = (d === 0) ? true: false;
          return {p : page + d, current : current}
        });

       /*now, filtering page numbers to include only
       * those that are possible given several constraints
       * */

        pageNums = _.filter(pageNums, function(d){
          if (d.p > 0){
            return true
          }
        });
        pageNums = pageNums.slice(0,5);

        var numFound = this.model.get("numFound");

        //iterate through remaining pageNums, keep them only if they're possible (< numFound)
        pageNums = _.filter(pageNums, function(d){
          if ( d.p * perPage - (perPage + 1) <= numFound){
            return true
          }
        });

        //now, finally, generate links for each page number

        var baseQ = new ApiQuery();

        var currentQuery = this.model.get("currentQuery");

        if (currentQuery && currentQuery.get("q")){
          baseQ.set("q", currentQuery.get("q"))
        }
        if (currentQuery && currentQuery.get("fq")){
          baseQ.set("fq", currentQuery.get("fq"))
        }
        baseQ.set("rows", perPage)

        //now, generating the link
       pageData = _.map(pageNums, function(n){
         var s = n.p * perPage - perPage;
         baseQ.set("start", s)
         n.link = baseQ.url();
         return n
       })


        //should we show a "back to first page" button?
        showFirst = (_.pluck(pageNums, "p").indexOf(1) !== -1) ? false : true;

        this.$el.html(PaginationTemplate({
          showFirst : showFirst,
          pageData : pageData,
          currentPage : page,
          perPage : this.model.get("perPage"),
          currentQuery : this.model.get("currentQuery")}));

        return this
      },

      events : {
        "click a" : "changePage",
        "blur .per-page": "changePerPage"

      },

      changePage : function(e){

        var d = $(e.target).data("paginate")

        this.model.set("page", d);

        return false

      },

      changePerPage : function(e){

        var perPage = parseInt($(e.target).val());

        this.model.set("perPage", perPage);
      }

    })

    var ItemModel = Backbone.Model.extend({
      defaults: function () {
        return {
          abstract: undefined,
          title: undefined,
          authorAff: undefined,
          pub: undefined,
          pubdate: undefined,
          keywords: undefined,
          bibcode: undefined,
          pub_raw: undefined,
          doi: undefined,
          details: undefined,
          links_data : undefined,
          resultsIndex : undefined
        }
      },
      idAttribute : "resultsIndex"

    });

    var VisibleCollection = Backbone.Collection.extend({
      model: ItemModel

    });


    var MasterCollection = Backbone.Collection.extend({

      initialize : function(options){

        this.paginationModel = options.paginationModel;

        this.listenTo(this.paginationModel, "change:page", this.onPaginationChange);
        this.listenTo(this.paginationModel, "change:perPage", this.onPaginationChange);
        this.listenTo(this.paginationModel, "change:numFound", this.updateStartAndEndIndex);

        this.on("add", this.transferModels);
        this.on("reset", this.transferModels);

        this.visibleCollection = options.visibleCollection;

        var pageNum, perPage;
        pageNum = this.paginationModel.get("page");
        perPage = this.paginationModel.get("perPage");

        this.currentEndIndex =  pageNum * perPage;
        this.currentStartIndex = this.currentEndIndex - perPage ;

      },

      model : ItemModel,

      numFound : undefined,

      comparator: "resultsIndex",

      updateStartAndEndIndex : function(){

        var pageNum, perPage, numFound;
        pageNum = this.paginationModel.get("page");
        perPage = this.paginationModel.get("perPage");
        numFound = this.paginationModel.get("numFound")

        //used as a metric to see if we need to fetch new data or if data at these indexes
        //already exists
        this.currentEndIndex =  pageNum * perPage;
        this.currentStartIndex = this.currentEndIndex - perPage ;
        //making sure it's not actually more than the number of results
        this.currentEndIndex = (numFound < this.currentEndIndex) ? numFound : pageNum * perPage;

      },

      onPaginationChange: function(model, options){

        this.updateStartAndEndIndex();

        this.transferModels();

      },

      requestData : function(){
        this.trigger("dataRequest", this.currentStartIndex, this.paginationModel.get("perPage"))
      },

      transferModels : function(){

        var indexes = _.range(this.currentStartIndex, this.currentEndIndex);

        //check to make sure this request is possible
        if (this.currentEndIndex > this.numFound){
          return
        }

        //check to see if we have the data
        var testList = this.filter(function(d){ if (indexes.indexOf(d.get("resultsIndex"))!== -1){return true}})

        //basically it was able to find a record that corresponded with every needed index
        if (testList.length  >= indexes.length){

          this.visibleCollection.reset(testList);
        }
        else if (this.paginationModel.get("numFound") > 0) {
          this.requestData()
        }
      }


    })

    var ItemView = Marionette.ItemView.extend({


      template: ItemTemplate,

      /**
       * This method prepares data for consumption by the template
       *
       * @returns {*}
       */
      serializeData: function () {

        var data ,shownAuthors;
        data = this.model.toJSON();

        if (data.author && data.author.length > 3) {
          data.extraAuthors = data.author.length - 3;
          shownAuthors = data.author.splice(0, 3);
        } else if (data.author) {
          shownAuthors = data.author
        }

        if (data.author) {
          var l = shownAuthors.length-1;
          data.authorFormatted = _.map(shownAuthors, function (d, i) {
            if (i == l || l == 0) {
              return d; //last one, or only one
            } else {
              return d + ";";
            }
          })
        }
         //if details/highlights
        if (data.details) {
          data.highlights = data.details.highlights
        }

        data.orderNum = this.model.get("resultsIndex") + 1;

        return data;

      },

      events: {
        'change input[name=identifier]': 'toggleSelect',
        'mouseover .letter-icon' : "showLinks",
        'mouseleave .letter-icon' : "hideLinks"
      },

      toggleSelect: function () {
        this.$el.toggleClass("chosen");
      },

      showLinks : function(e){
        $c = $(e.currentTarget);
        $c.find("i").addClass("s-icon-draw-attention");
        $c.find(".s-link-details").removeClass("no-display");
      },
      hideLinks : function(e){
        $c = $(e.currentTarget);
        $c.find("i").removeClass("s-icon-draw-attention");
        $c.find(".s-link-details").addClass("no-display");
      }

    });

    var ListView = Marionette.CompositeView.extend({

      initialize: function (options) {
        this.paginationView = options.paginationView;
        this.showDetailsButton = options.showDetailsButton;
      },

      className: "list-of-things",
      itemView: ItemView,

      itemViewContainer: ".results-list",
      events: {
        "click .show-details": "showDetails"
      },

      serializeData : function(){

        return {showDetailsButton : this.showDetailsButton}

      },

      template: ResultsContainerTemplate,

      onRender: function(){
        this.paginationView.setElement(this.$(".pagination-controls")).render();
      },

      /**
       * Displays the are inside of every item-view
       * with details (this place is normally hidden
       * by default)
       */
      showDetails: function (ev) {
        if (ev)
          ev.stopPropagation();
        this.$(".more-info").toggleClass("hide");
        if (this.$(".more-info").hasClass("hide")) {
          this.$(".show-details").text("Show details");
        } else {
          this.$(".show-details").text("Hide details");
        }
      }


    });


    var ListOfThingsWidget = BaseWidget.extend({

      initialize: function (options) {

        BaseWidget.prototype.initialize.call(this, options);

        //so each widget gets its own copy instead of sharing one on the prototype chain
        this.defaultQueryArguments = _.result(this, "defaultQueryArguments");

        //adding data for the letter links
        this.defaultQueryArguments.fl = this.defaultQueryArguments.fl + "," + this.resultsPageFields;

        //letting widget know that the loading transition should happen
        this.showLoad =  true;

        this.visibleCollection = new VisibleCollection();

        var paginationOptions = {};

        if (options.perPage){
          paginationOptions.perPage = options.perPage;
          this.defaultQueryArguments.rows = options.perPage
        }
        else {
          paginationOptions.perPage =  this.defaultQueryArguments.rows;
        }

        this.paginationModel = new PaginationModel(paginationOptions);

        this.paginationView = new PaginationView({
          model : this.paginationModel

        });

        //showdetails defaults to false, so details button will be hidden
        this.view = new ListView({
          collection: this.visibleCollection,
          paginationView : this.paginationView,
          showDetailsButton : this.showDetailsButton
        });

        this.collection = new MasterCollection({visibleCollection : this.visibleCollection,
         paginationModel: this.paginationModel});

        this.listenTo(this.collection, "all", this.onAllInternalEvents);
        this.on("all", this.onAllInternalEvents);

      },

      showDetailsButton : false,

      activate: function (beehive) {

        _.bindAll(this,  "processResponse");

        this.pubsub = beehive.Services.get('PubSub');

        //custom handleResponse function goes here
        this.pubsub.subscribe(this.pubsub.DELIVERING_RESPONSE, this.processResponse);
      },

      resetWidget : function(bibcode){
        //this will trigger paginationModel to reset itself as well
        this.numFound = undefined;
        this.trigger("change:numFound", this.numFound);

        this.paginationModel.set("start",0 )

      },

      resetBibcode : function(bibcode){

        this._bibcode = bibcode;

      },

    /*
     -a way to get data on command on a per-bibcode-basis
     -used by the page managers
     -checks first to see if bibcode is the same as the current bibcode, in which case nothing happens
     -it returns a promise which is resolved when the data
       is available
     -this function has to be overridden for some less straightforward
       solr queries, such as "similar/more like this"
      */
      loadBibcodeData: function (bibcode) {


        if (bibcode === this._bibcode){

          this.deferredObject =  $.Deferred();
          this.deferredObject.resolve(this.paginationModel.get("numFound"));
          return this.deferredObject.promise();

        }

        //numFound needs to equal undefined as a signal to other functions that the request cycle has restarted

        this.resetWidget();
        this.resetBibcode(bibcode);

        if ((!this.solrOperator && !this.solrField) || (this.solrOperator && this.solrField)){
          throw new Error("Can't call loadBibcodeData without either a solrOperator or a solrField, and can't have both!")
        }

        var searchTerm = this.solrOperator? this.solrOperator + "(" + bibcode +")" : this.solrField + ":" + bibcode

        this.deferredObject =  $.Deferred();

        var q = this.composeQuery(this.defaultQueryArguments, new ApiQuery());

        q.set("q", searchTerm);

        var req = this.composeRequest(q);
        if (req) {
          this.pubsub.publish(this.pubsub.DELIVERING_REQUEST, req);
        }
        return this.deferredObject.promise();

      },

      //will be requested in composeRequest
      defaultQueryArguments: function(){
        return {
          fl: 'title,abstract,bibcode,author,keyword,citation_count,pub,aff,volume,year',
          rows : 25,
          start : 0
        }
      },

      processResponse: function (apiResponse) {

        this.setCurrentQuery(apiResponse.getApiQuery());

        //also let pagination model know
        this.paginationModel.set("currentQuery", this.getCurrentQuery())

        //checking to see if we need to reset start or rows values

         var r =  this.getCurrentQuery().get("rows");
         var s = this.getCurrentQuery().get("start");
          if (r){
            if ($.isArray(r)){
              this.paginationModel.set("perPage",r[0])
            }
            else {
              this.paginationModel.set("perPage",r)
            }
          }
          if (s) {
            if ($.isArray(s)) {
              this.paginationModel.set("page", s[0]/ this.paginationModel.get("perPage") + 1)
            }
            else {
              this.paginationModel.set("page", s/ this.paginationModel.get("perPage") + 1)
            }

          }

        var docs = apiResponse.get("response.docs")

        //any preprocessing before adding the resultsIndex is done here
        var docs = _.map(docs, function(d){
          d.identifier = d.bibcode;
          return d
        });

        docs = this.parseLinksData(docs);

        this.insertPaginatedDocsIntoCollection(docs, apiResponse)

        //resolving the promises generated by "loadBibcodeData"
        if (this.deferredObject){
          this.deferredObject.resolve(this.paginationModel.get("numFound"))
        }

      },

      onAllInternalEvents: function(ev, arg1, arg2) {

       if (ev === "dataRequest") {

         console.log("requesting data", arg1, arg2)

          var start = arg1;

          var rows = arg2;

         var q = this.getCurrentQuery().clone();

         q.unlock();
         q = this.composeQuery(this.defaultQueryArguments, q);

         q.set("start", start);
         q.set("rows", rows)

         var req = this.composeRequest(q);
         if (req) {
           this.pubsub.publish(this.pubsub.DELIVERING_REQUEST, req);
         }

         if (this.mainResults){
           //letting other interested widgets know that more info was fetched
           //i.e. there was a pagination event
           this.pubsub.publish(this.pubsub.CUSTOM_EVENT, {event: "pagination", data: {start: start, rows: rows}});

         }

         if (this.showLoad === true){
           this.startWidgetLoad()
         }

       }

        if (ev === "change:numFound"){
          this.paginationModel.set("numFound", arg1);
        }
      },


      startWidgetLoad : function(){

          if (this.view.itemViewContainer) {
            var removeLoadingView = function () {
              this.view.$el.find(".s-loading").remove();
            }
            this.listenToOnce(this.visibleCollection, "reset", removeLoadingView);

          if (this.view.$el.find(".s-loading").length === 0){
            this.view.$el.append(this.loadingTemplate());
          }
        }
      }

    });

    // add mixins
    _.extend(ListOfThingsWidget.prototype, LinkGenerator);
    _.extend(ListOfThingsWidget.prototype, WidgetPaginationMixin)


    return ListOfThingsWidget;

  });
