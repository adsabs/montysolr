/**
 * This widget can paginate through a list of results. It can easily be inherited
 * by results widget ,table of contents widget, etc. It either listens to INVITING_REQUEST
 * in the case of the results widget, or the loadBibcode method (currently all other widgets).
 *
 * This widget consists of the following components:
 *
 * 1. a pagination view
 * 2. an associated pagination model (all pagination info is kept here and only here)
 * 3. a 'master' collection that holds all currently accessible records
 * 4. a 'visible' collection that shows all records that should currently be visible on screen
 * 5. a list view that listens to the visible collection and renders it
 * 6. an item view for each record rendered repeatedly by the list view
 * 7. a controller that handles requesting and recieving data from pubsub and initializing everything
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
    'js/mixins/add_stable_index_to_collection',
    'hbs!./templates/empty-view-template',
    'hbs!./templates/initial-view-template'
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
    WidgetPaginationMixin,
    EmptyViewTemplate,
    InitialViewTemplate
    ) {


    var PaginationModel = Backbone.Model.extend({

      initialize: function(attrs, options){

        this.defaults = options.defaults;

        this.attributes = _.result(this, "defaults");

      }

    })

    var PaginationView = Backbone.View.extend({

      initialize : function(options){

        /*
         * listening to change in perPage value or change in current page
         */
        this.listenTo(this.model, "change", this.render);
        this.getStartVal = options.getStartVal;


      },

      template : PaginationTemplate,

      render: function(){

        var pageData, baseQ, showFirst;

        var minAmountToShowPagination = 25;

        var page = this.model.get("page");
        var perPage = this.model.get("perPage");
        var numFound = this.model.get("numFound");

        var pageNums = this.generatePageNums(page);

        //iterate through remaining pageNums, keep them only if they're possible (< numFound)
        pageNums = this.ensurePagePossible(pageNums, perPage, numFound);

        //now, finally, generate links for each page number

        baseQ = this.model.get("currentQuery");

        if (baseQ){

          baseQ = baseQ.clone();

          //now, generating the link
          pageData = _.map(pageNums, function(n){
            var s = this.getStartVal(n.p, perPage);
            baseQ.set("start", s)
            n.link = baseQ.url();
            return n
          }, this);

          baseQ.set("rows", perPage);

        }

        //should we show a "back to first page" button?
        showFirst = (_.pluck(pageNums, "p").indexOf(1) !== -1) ? false : true;

        //only render pagination controls if there are more than 25 results
        if (numFound > minAmountToShowPagination){
          this.$el.html(PaginationTemplate({
            showFirst : showFirst,
            pageData : pageData,
            currentPage : page,
            perPage : this.model.get("perPage"),
            currentQuery : this.model.get("currentQuery")}));
        }
        else {
          this.$el.html("");
        }

        return this
      },


      //create list of up to 5 page numbers to show
      generatePageNums : function(page){

        var pageNums = _.map([-2,-1, 0, 1, 2 , 3, 4], function(d){
          var current = (d === 0) ? true: false;
          return {p : page + d, current : current}
        });

        //page number can't be less than 1
        pageNums = _.filter(pageNums, function(d){
          if (d.p > 0){
            return true
          }
        });

        return pageNums.slice(0,5);

      },

      //iterate through pageNums, keep them only if they're possible (< numFound)
      ensurePagePossible : function(pageNums, perPage, numFound){

        var endIndex = numFound - 1
        return  _.filter(pageNums, function(n){
          if (this.getStartVal(n.p, perPage)<= endIndex){
            return true
          }
        }, this)

      },

      events : {
        "click a" : "changePage",
        "input .per-page": "changePerPage"

      },

      changePage : function(e){

        var d = $(e.target).data("paginate")

        this.model.set("page", d);

        return false

      },

      changePerPage : _.debounce(function(e){

        var perPage = parseInt($(e.target).val());

        this.model.set("perPage", perPage);
      }, 2000)

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

      initialize : function(models, options){

        this.paginationModel = options.paginationModel;

        this.listenTo(this.paginationModel, "change:page", this.onPaginationChange);
        this.listenTo(this.paginationModel, "change:perPage", this.onPaginationChange);

        this.on("collection:augmented", this.onCollectionAugmented);

        //if nothing was found, show the empty view
        this.on("noneFound", function(){this.visibleCollection.reset()})

        this.visibleCollection = options.visibleCollection;

        _.extend(MasterCollection.prototype, WidgetPaginationMixin);

      },

      model : ItemModel,

      numFound : undefined,

      comparator: "resultsIndex",

      updateStartAndEndIndex : function(){

        var pageNum = this.paginationModel.get("page");
        var perPage = this.paginationModel.get("perPage");
        var numFound = this.paginationModel.get("numFound")
        //used as a metric to see if we need to fetch new data or if data at these indexes
        //already exist
        this.currentStartIndex = this.getStartVal(pageNum, perPage);
        this.currentEndIndex = this.getEndVal(pageNum, perPage, numFound);

      },

      onPaginationChange: function(){

        //so controller can allow a request
        this.trigger("pagination:change");

        this.updateStartAndEndIndex();

        this.transferModels();

      },

      onCollectionAugmented : function(){

        this.updateStartAndEndIndex();

        this.transferModels();

      },

      requestData : function(){
        //values for start and rows
        this.trigger("dataRequest", this.currentStartIndex, this.paginationModel.get("perPage"))
      },


      transferModels : function(){

        //add one to the end to make sure the final index is inclusive
        var indexes = _.range(this.currentStartIndex, this.currentEndIndex + 1);

        //check to see if we have the data
        var testList = this.filter(function(d){ if (indexes.indexOf(d.get("resultsIndex"))!== -1){return true}})

        //basically it was able to find a record that corresponded with every needed index
        //probably should be equal rather than greater or equal, but maybe there could be duplicate records??
        //checking for length to prevent unecessary initial render
        if (testList.length){

          this.visibleCollection.reset(testList);
        }
        else {

          this.requestData();

        }

      }

    })

    var ItemView = Marionette.ItemView.extend({

      tagName : "li",

      className : "col-sm-12 s-display-block",


      template: ItemTemplate,

      /**
       * This method prepares data for consumption by the template
       *
       * @returns {*}
       */
      serializeData: function () {

        var data ,shownAuthors;
        data = this.model.toJSON();

        var maxAuthorNames = 3;

        if (data.author && data.author.length > maxAuthorNames) {
          data.extraAuthors = data.author.length - maxAuthorNames;
          shownAuthors = data.author.slice(0, maxAuthorNames);
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
        'mouseenter .letter-icon' : "showLinks",
        'mouseleave .letter-icon' : "hideLinks",
        'click .letter-icon' : "pinLinks"

      },

      toggleSelect: function () {
        this.$el.toggleClass("chosen");
      },

      /*
      * adding this to make the dropdown
      * accessible, and so people can click to sticky
      * open the quick links
      * */


    removeActiveQuickLinkState : function($node){

      $node.removeClass("pinned");
      $node.find("i").removeClass("s-icon-draw-attention")
      $node.find(".link-details").addClass("hidden");
      $node.find('ul').attr('aria-expanded', false);

      },

     addActiveQuickLinkState : function($node){

       $node.find("i").addClass("s-icon-draw-attention")
       $node.find(".link-details").removeClass("hidden");
       $node.find('ul').attr('aria-expanded', true);

     },

     deactivateOtherQuickLinks: function($c){

       var $hasList = this.$(".letter-icon").filter(function(){
         if ($(this).find("i").hasClass("s-icon-draw-attention")){
           return true
         }
       }).eq(0);

       //there should be max 1 other icon that is active

       if ($hasList.length && $hasList[0] !== $c[0]){

         this.removeActiveQuickLinkState($hasList)
       }

     },

       pinLinks : function(e){

        $c = $(e.currentTarget);

        if (!$c.find(".active-link").length){
           return
         }

        $c.toggleClass("pinned");

        if($c.hasClass("pinned")){

          this.deactivateOtherQuickLinks($c);

          this.addActiveQuickLinkState($c);
        }
        else {

          this.removeActiveQuickLinkState($c);

        }

      },

      showLinks : function(e){

        $c = $(e.currentTarget);

        if (!$c.find(".active-link").length){
          return
        }

        if ($c.hasClass("pinned")){
          return
        }
        else {

          this.deactivateOtherQuickLinks($c);
          this.addActiveQuickLinkState($c)
        }

      },

      hideLinks : function(e){

        $c = $(e.currentTarget);

        if ($c.hasClass("pinned")){
          return
        }

        this.removeActiveQuickLinkState($c)

      }

    });

    var ListViewModel = Backbone.Model.extend({

      defaults : function(){


        return {
          showDetailsButton : false,
          mainResults : false
        }
      }

    });


    var VisibleCollectionEmptyView = Marionette.ItemView.extend({

      template :  EmptyViewTemplate

    });

    var VisibleCollectionInitialView = Marionette.ItemView.extend({

      template : InitialViewTemplate
    })

    var ListView = Marionette.CompositeView.extend({

      initialize: function (options) {

        this.paginationView = options.paginationView;
        this.model = new ListViewModel();
        this.sortView = this.sortView  || options.sortView;
      },

      className: "list-of-things",
      itemView: ItemView,
      alreadyRendered : false,

      getEmptyView : function(){
        if (this.alreadyRendered)
          return VisibleCollectionEmptyView;
        else {
          this.alreadyRendered = true;
          return VisibleCollectionInitialView;

        }
      },

      itemViewContainer: ".results-list",
      events: {
        "click .show-details": "showDetails"
      },

      //calls to render will render only the model after the 1st time
      modelEvents : {

        "change" : "render"

      },

      template: ResultsContainerTemplate,

      onRender: function(){

        if  (this.sortView){

          this.sortView.setElement(this.$(".sort-container")).render();

        }

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

        paginationOptions.numFound = undefined;
        paginationOptions.currentQuery = undefined;
        paginationOptions.page = 1;

        //have to use a cloned copy or else it will work on the first go but then
        //be modified once the model itself is modified!!!
        this.paginationModel = new PaginationModel({}, {defaults : function(){return _.clone(paginationOptions)}});

        this.paginationView = new PaginationView({
          model : this.paginationModel,
          //from the pagination mixin
          getStartVal : this.getStartVal

        });


        //showdetails defaults to false, so details button will be hidden
        this.view = new ListView({
          collection: this.visibleCollection,
          paginationView : this.paginationView,
          showDetailsButton : this.showDetailsButton,
          mainResults : this.mainResults

        });

        this.collection = new MasterCollection({}, {visibleCollection : this.visibleCollection,
          paginationModel: this.paginationModel});

        this.listenTo(this.collection, "all", this.onAllInternalEvents);
        this.on("all", this.onAllInternalEvents);

        BaseWidget.prototype.initialize.call(this, options);

      },


      activate: function (beehive) {

        _.bindAll(this,  "processResponse");

        this.pubsub = beehive.Services.get('PubSub');

        //custom handleResponse function goes here
        this.pubsub.subscribe(this.pubsub.DELIVERING_RESPONSE, this.processResponse);
      },

      resetWidget : function(){

        //this will trigger paginationModel to reset itself
        var defaults =  _.result(this.paginationModel, 'defaults')
        //reset pagination model, but prevent the view from immediately re-rendering
        //by passing silent
        this.paginationModel.set(defaults, {silent : true});

        //this will hopefully be overridden by the pagination information
        //gathered in processResponse, but just to be safe I'm setting it here too.

        //also resetting the main collection and the visible collection

        this.collection.reset(null, {silent : true});

        //prevent rendering of empty view
        this.visibleCollection.reset(null, {silent: true});

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
        //need to know whether to add or reset the collection (the absence of numFound means it is a new
        //query that needs to be reset)
        this.resetWidget();
        this.resetBibcode(bibcode);

        if ((!this.solrOperator && !this.solrField) || (this.solrOperator && this.solrField)){
          throw new Error("Can't call loadBibcodeData without either a solrOperator or a solrField, and can't have both!")
        }

        var searchTerm = this.solrOperator? this.solrOperator + "(bibcode:" + bibcode +")" : this.solrField + ":" + bibcode

        this.deferredObject =  $.Deferred();

        var q = this.composeQuery(this.defaultQueryArguments, new ApiQuery());

        q.set("q", searchTerm);

        if (this.sortOrder){
          q.set("sort", this.sortOrder)
        }

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

        var toSet = {"numFound":  apiResponse.get("response.numFound"),
          "currentQuery":this.getCurrentQuery()};

        //checking to see if we need to reset start or rows values
        var r =  this.getCurrentQuery().get("rows");
        var s = this.getCurrentQuery().get("start");

        if (r){

          r = $.isArray(r) ? r[0] : r;
          toSet.perPage = r;

        }

        if (s) {

          var perPage =  toSet.perPage || this.paginationModel.get("perPage");

          s = $.isArray(s) ? s[0] : s;

          //getPageVal comes from the pagination mixin
          toSet.page= this.getPageVal(s, perPage);

        }

        var docs = apiResponse.get("response.docs")

        docs = _.map(docs, function(d) {
          d.identifier = d.bibcode;
          return d
        });


        docs = this.parseLinksData(docs);

        docs = this.addPaginationToDocs(docs, apiResponse);

        if (docs.length) {

          //reset the pagination model with toSet values
          //has to happen right before collection changes
          this.paginationModel.set(toSet, {silent : true});

          //just using add because the collection was emptied
          //when a new request was made
          this.collection.add(docs);

          /*
           * we need a special event that fires only once in event
           * of a reset OR an add
           * */
          this.collection.trigger("collection:augmented");

        }

        else {
          //used by loading view
          //and to re-render collection
          this.collection.trigger("noneFound");
        }

        //resolving the promises generated by "loadBibcodeData"
        if (this.deferredObject){

          this.deferredObject.resolve(this.paginationModel.get("numFound"))
        }

        // XXX:rca - hack, to be solved later
        this.trigger('page-manager-event', 'widget-ready',
          {numFound: apiResponse.get("response.numFound"), widget: this});

      },

      setPaginationRequestPending : function(){

        this._paginationRequestPending = true;

      },

      resetPaginationRequest : function(){

        this._paginationRequestPending = false;

      },

      isPaginationPending : function(){

        return this._paginationRequestPending;

      },

      onAllInternalEvents: function(ev, arg1, arg2) {

        if (ev === "pagination:change"){

          this.resetPaginationRequest()
        }

        if (ev === "dataRequest") {

          if (this.isPaginationPending()){
            return
          }

          var start = arg1;

          var rows = arg2;

          var q = this.getCurrentQuery().clone();

          q.unlock();
          q = this.composeQuery(this.defaultQueryArguments, q);

          q.set("start", start);
          q.set("rows", rows)

          var req = this.composeRequest(q);
          if (req) {

            this.setPaginationRequestPending();

            this.pubsub.publish(this.pubsub.DELIVERING_REQUEST, req);
          }

          if (this.mainResults){
            //letting other interested widgets know that more info was fetched
            //i.e. there was a pagination event
            this.pubsub.publish(this.pubsub.CUSTOM_EVENT, {event: "pagination", data: {start: start, rows: rows}});

          }


        }

      }


    });

    // add mixins
    _.extend(ListOfThingsWidget.prototype, LinkGenerator);
    _.extend(ListOfThingsWidget.prototype, WidgetPaginationMixin)


    return ListOfThingsWidget;

  });
