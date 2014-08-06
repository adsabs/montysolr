

//knows about the central region manager (passed it on instantiation) and can manipulate it (also it can add sub regions)

//listens to any events that request the abstract page or a sub part of it and displays the necessary views

//provides an api that can be used by the router



define(["marionette", "hbs!./templates/abstract-page-layout",
    'js/widgets/base/paginated_base_widget', 'hbs!./templates/abstract-title',
    'js/components/api_query', 'hbs!./templates/abstract-nav', 'js/widgets/loading/widget'],
  function(Marionette, threeColumnTemplate, PaginatedBaseWidget,
    abstractTitleTemplate, ApiQuery, abstractNavTemplate, LoadingWidget){


    var currentBibcode;

    var AbstractNavView = Backbone.View.extend({
      template : abstractNavTemplate,

      render : function(){
        this.$el.html(this.template({bibcode: currentBibcode}))
        return this
      },

      events : {
        "click a" : "emitNavigateEvent"
      },

      emitNavigateEvent : function(e){
        var $t  = $(e.currentTarget);
        var route = $t.attr("href");
        //taking only final path
        e.preventDefault();
        e.stopPropagation();
        this.trigger("navigate", route);

      }
    })

    var AbstractTitleView = Backbone.View.extend({

      template : abstractTitleTemplate,

      render : function(){

        var prevBib, nextBib, index;

        var model = this.collection.findWhere({bibcode : currentBibcode});

        //only send number to template if it is in a set of results
        if (model.get("originalSearchResult") === true){
          index = this.collection.indexOf(model);
          prevBib = this.collection.filter(function(model, i){return i == index-1})[0]
          nextBib = this.collection.filter(function(model, i){return i == index+1})[0]
        }

        prevBib = prevBib? prevBib.get("bibcode") : undefined;
        nextBib = nextBib? nextBib.get("bibcode") : undefined;

        this.$el.html(this.template({index : index+1, description: this.collection.subPage , title: model.get("title"), prev: prevBib, next : nextBib}))

        return this

      },

      events : {"click .abstract-paginator-next" : "checkLoadMore"},

      checkLoadMore : function(){
       this.trigger("nextEvent")
      }

    })


    var API = {

      insertTemplate : function(){

        var $bodyContainer = $("#body-template-container");

        $bodyContainer.children().detach()

        $bodyContainer.append(threeColumnTemplate())

      },

      insertLoadingView : function(){
        $("#body-template-container").append(this.loadingWidget.render().el)

        this.loadingWidget.trigger("showLoading")

      },

      loadWidgetData : function(){
        var that = this;

        _.each(this.abstractSubViews, function(v, k){

          if (k === "abstract"){

            v.widget.loadBibcodeData(this._bibcode);
            this.activateNavButton(k)

          }
          else {
            var promise= v.widget.loadBibcodeData(this._bibcode);

              promise.done(function () {
                if (v.widget.collection.length >=1) {
                  that.activateNavButton(k, v.showNumFound, v.widget.collection.length)
                }
                else {
                  that.deactivateNavButton(k)
                }

              })
          }

        }, this)

      },

      activateNavButton : function(k, showNumFound, colLength){

        var $navButton = $("#"+k);

        if (showNumFound){
          $navButton.removeClass("s-abstract-nav-inactive")
            .find(".num-items").text("("+colLength +")")
        }
        else {
          $navButton.parent().off(this, this.deactivateLink)
          $navButton.removeClass("s-abstract-nav-inactive")
        }

      },

      deactivateLink : function(){
      return false
    },


      deactivateNavButton : function(k){

        var $navButton = $("#"+k);

        $navButton.addClass("s-abstract-nav-inactive")

        $navButton.parent().on("click", this.deactivateLink)

      },


      displayAbstractNav: function(){

        var $leftCol = $("#s-left-col-container")

        $leftCol.append(this.navView.render().el);

      },

      showTitle : function(){
        var $titleRow = $("#abstract-title-container");

        $titleRow.append(this.titleView.el)

      },

      showAbstractSubView: function(viewName) {

        var dataForRouter, $middleCol, widget;

        //tell router to display the correct url

         dataForRouter = {page:"abstractPage", subPage: viewName, data : this._bibcode, path: "abs/"+ this._bibcode + "/" + viewName}

        this.pubsub.publish(this.pubsub.NAVIGATE_WITHOUT_TRIGGER, dataForRouter)

        if (!viewName){
          console.warn("viewname undefined")
          return
        }

        $middleCol = $("#s-middle-col-container");

        $middleCol.children().detach();

        //removing highlight for another view nav
        $(".abstract-nav").removeClass("s-abstract-nav-active")

        widget = this.abstractSubViews[viewName]["widget"];


        $middleCol.append(widget.render().el);

        //also amending the title
        $(".abstract-title-info-row").text(this.abstractSubViews[viewName]["descriptor"])

        //also highlighting the relevant nav

        $("#"+viewName).addClass("s-abstract-nav-active")

      },

      displayRightColumn : function(){
        var $rightCol = $("#s-right-col-container");

      },

      displayTopRow : function(){
        var $searchBar = $("#search-bar-row");

        $searchBar.append(this.widgetDict.searchBar.render().el)
        if (this.getMasterQuery()  && (this.history.getPriorPage() === "resultsPage") || (this.history.getPriorPage() === "abstractPage")) {
          $(".opt-nav-button").append("<a href=" + "/search/" + this.getMasterQuery().url()
            + " class=\"btn btn-sm \"> <i class=\"glyphicon glyphicon-arrow-left\"></i> back to results</a>")
        }
      }

    };

    var AbstractControllerModel = Backbone.Model.extend({
      defaults: function () {
        return {
          bibcode: undefined,
          title: undefined
        }
      },

      parse : function(d){
        d.title = d.title[0];
        return d
      }

    });

    var AbstractControllerCollection = Backbone.Collection.extend({
      model: AbstractControllerModel
    });


    var AbstractController = PaginatedBaseWidget.extend({

      activate: function (beehive) {

        this.pubsub = beehive.Services.get('PubSub');

        _.bindAll(this, ['dispatchInitialRequest', 'processResponse', 'autoPaginate']);

        //custom dispatchRequest function goes here
        this.pubsub.subscribe(this.pubsub.INVITING_REQUEST, this.dispatchInitialRequest);

        //custom handleResponse function goes here
        this.pubsub.subscribe(this.pubsub.DELIVERING_RESPONSE, this.processResponse);

        this.pubsub.subscribe(this.pubsub.CUSTOM_EVENT, this.autoPaginate);


      },

      autoPaginate : function(eventData){

        //requesting too much?
        if (eventData.event === "pagination" ){

          this.dispatchRequest(this.getCurrentQuery());

        }

      },

      initialize : function(options){

        options = options || {};

        this.collection = new AbstractControllerCollection();

        this.titleView = new AbstractTitleView({collection: this.collection});

        this.navView = new AbstractNavView();

        this.listenTo(this.navView, "navigate", this.subPageNavigate)

        //represents the current bibcode (always only 1)
        this._bibcode = undefined;

        if (!options.widgetDict){
          throw new error("page managers need a dictionary of widgets to render")
        }

        this.widgetDict = options.widgetDict

        _.extend(this, API);

        this.history = options.history;

//      to be explicit, transferring only those widgets considered "sub views" to this dict
        this.abstractSubViews = {
          //the keys are also the sub-routes
          "abstract": {widget: this.widgetDict.abstract, title:"Abstract", descriptor: "Abstract for:"},
          "references": {widget: this.widgetDict.references, title:"References", descriptor: "References in:", showNumFound : true},
          "citations": {widget: this.widgetDict.citations, title: "Citations", descriptor: "Papers that cite:", showNumFound : true},
          "coreads" : {widget: this.widgetDict.coreads, title: "Co-Reads", descriptor: "Other papers read by those who read:"},
          "tableofcontents" : {widget: this.widgetDict.tableOfContents, title: "Table of Contents", descriptor: "Table of Contents for:", showNumFound : true},
          "similar": {widget : this.widgetDict.similar, title : "Similar Papers", descriptor : "Papers with similar characteristics to:"}
        };

        this.listenTo(this.titleView, "all", this.onAllInternalEvents)

        this.loadingWidget = new LoadingWidget();

        options.rows = 40;

        PaginatedBaseWidget.prototype.initialize.call(this, options);

      },

      onAllInternalEvents : function(ev, arg1, arg2){

        if (ev.indexOf("nextEvent")!== -1){
          this.checkLoadMore()
        }

      },

      subPageNavigate : function(route){

        //now, just render the relevant page
        var subView  = route.match(/\/(\w+)$/)[1];
        this.showAbstractSubView(subView);

      },


      checkLoadMore : function(){

        //first, find position of current bib in this._docs
        var ind = this.collection.indexOf(this.collection.findWhere({bibcode: this._bibcode}))

        //fetch more if there are 10 or fewer records remaining
        if (this.collection.length - ind === 10 ){

          this.dispatchRequest(this.getCurrentQuery())

        }

      },

      getMasterQuery : function(){
        return this._masterQuery;
      },

      dispatchInitialRequest: function (apiQuery) {

        //does this work to compare apiQueries?
        if (JSON.stringify(apiQuery.toJSON()) !== JSON.stringify(this.getCurrentQuery().toJSON())){
          this.setCurrentQuery(apiQuery);

          //tells you what the people have searched (not the widget's query)
          this._masterQuery = apiQuery;

          this.paginator.reset();

          this.dispatchRequest(apiQuery)

        }

      },

      defaultQueryArguments: {
        fl: 'title,bibcode'
      },

      renderNewBibcode: function () {

        //automatically renders this._bibcode

        //first, check if we have the info in current query docs
        if (this.collection.findWhere({bibcode: this._bibcode})) {

          this.titleView.render();

        }
        else if (this.getMasterQuery()){
          //the information hasn't arrived yet, so listen to the collection reset/add event
          //i made a new event to encompass both of them-- there must be a better way to do this!
          this.listenToOnce(this.collection, "collection:augmented", this.renderNewBibcode)

        }

        else {
          //we dont have the bibcode
          //processResponse will re-call this function, but with the data parameter

          //is there a better way to avoid pagination?
          var req = this.dispatchRequest(new ApiQuery({'q': 'bibcode:' + this._bibcode, '__show': this._bibcode}));
        }
      },


      processResponse: function (apiResponse) {

        var r = apiResponse.toJSON();

        //it's an individual bibcode, just render it
        if (apiResponse.has('responseHeader.params.__show')) {
          //make the dict of associated data
          var data = r.response.docs[0]
          if (!data){
            throw new Error("did not receive bibcode data")
          };
          /* we are adding to the model the notion that this bib didn't come from
          * a system-wide query, i.e. they clicked on a title within the abstract page
          * rather than in the results page
          *
          */
          this.collection.add({title: data.title, bibcode : data.bibcode, originalSearchResult: false})

          this.renderNewBibcode(this._bibcode);
        }
        else {
          //it's from "inviting_request"
          //indicate that this came from a system-wide search

          var docs = _.map(r.response.docs, function(d){
            d.originalSearchResult = true
            return d});

          if (this.paginator.getCycle() <= 1) {
            //it's the first set of results
            this.paginator.setMaxNum(r.response.numFound);
            this.collection.reset(docs, {
              parse: true
            });
            this.collection.trigger("collection:augmented")
          }
          else {
            this.collection.add(docs, {
              parse: true
            });
            this.collection.trigger("collection:augmented")


          }

        }

      },


      //   called by the router

      showPage : function(bib, subPage, fromWithinPage){

        if (fromWithinPage){
          //just need to switch out the subPage
          this.showAbstractSubView(subPage)

        }

        else {

          this._bibcode = bib;

          currentBibcode = bib;

          this.renderNewBibcode();

          this.insertTemplate();
          this.showTitle();
          this.displayAbstractNav();
          this.displayRightColumn();
          this.displayTopRow();

          //this calls "displayNav" upon individual widget completion
          this.loadWidgetData();

          this.showAbstractSubView("abstract");

          this.insertLoadingView()

        }

      }

    })

    return AbstractController


  })