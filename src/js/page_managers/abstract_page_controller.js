

//knows about the central region manager (passed it on instantiation) and can manipulate it (also it can add sub regions)

//listens to any events that request the abstract page or a sub part of it and displays the necessary views

//provides an api that can be used by the router



define(["marionette", "hbs!./templates/abstract-page-layout",
    'js/widgets/base/paginated_base_widget', 'hbs!./templates/abstract-title',
    'js/components/api_query', 'hbs!./templates/abstract-nav', 'js/widgets/loading/widget'],
  function(Marionette, threeColumnTemplate, PaginatedBaseWidget,
    abstractTitleTemplate, ApiQuery, abstractNavTemplate, LoadingWidget){


    var AbstractTitleView = Backbone.View.extend({

      template : abstractTitleTemplate,

      render : function(bibcode, data, collection){
        var prevBib, nextBib, index;

        //only send number to template if it is in a set of results
        if (collection){
          index = collection.indexOf(data);
          prevBib = collection.filter(function(model, i){return i == index-1})[0]
          nextBib = collection.filter(function(model, i){return i == index+1})[0]

        }

        prevBib = prevBib? prevBib.get("bibcode") : undefined;
        nextBib = nextBib? nextBib.get("bibcode") : undefined;

        console.log(index, "index")

        this.$el.html(this.template({index : index+1, description: "Abstract for:" , title: data.get("title"), prev: prevBib, next : nextBib}))

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

        $leftCol.append(abstractNavTemplate({bibcode : this._bibcode}))

      },

      showTitle : function(){
        var $titleRow = $("#abstract-title-container");

        $titleRow.append(this.view.el)

      },

      showAbstractSubView: function(viewName) {

        if (!viewName){
          console.warn("viewname undefined")
          return
        }

        var $middleCol = $("#s-middle-col-container");

        $middleCol.children().detach();

        //removing highlight for another view nav
        $(".abstract-nav").removeClass("s-abstract-nav-active")

        var widget = this.abstractSubViews[viewName]["widget"];



        $middleCol.append(widget.render().el);

        //also amending the title
        $(".abstract-title-info-row").text(this.abstractSubViews[viewName]["descriptor"])

        //also highlighting the relevant nav

        $("#"+viewName).addClass("s-abstract-nav-active")

      },

      displayRightColumn : function(){
        var $rightCol = $("#s-right-col-container")

      },

      displayTopRow : function(){
        var currentSearchVal;
        var $searchBar = $("#search-bar-row");

        currentSearchVal = this.getMasterQuery();

        $searchBar.append(this.widgetDict.searchBar.render().el)
        if (currentSearchVal && (this.history.getPriorPage() === "resultsPage") || (this.history.getPriorPage() === "abstractPage")) {
          $(".opt-nav-button").append("<a href=" + "#search/?" + currentSearchVal.url()
            + " class=\"btn btn-sm \"> <i class=\"glyphicon glyphicon-arrow-left\"></i> back to results</a>")
        }

      }

    };

    var AbstractControllerModel = Backbone.Model.extend({
      defaults: function () {
        return {
          bibcode: undefined,
          title: undefined,
        }
      },

      parse : function(d){
        return {title : d.title[0], bibcode: d.bibcode}
      },

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

        if (eventData.event === "pagination" && eventData.data.cycle % 2 ==1 ){

          this.dispatchRequest(this.getApiQuery());

        }

      },

      initialize : function(options){

        options = options || {};

        this.view = new AbstractTitleView();

        this.collection = new AbstractControllerCollection();

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

        this.listenTo(this.view, "all", this.onAllInternalEvents)
        this.listenTo(this.collection, "reset", this.onCollectionReset)

        this.loadingWidget = new LoadingWidget();

        options.rows = 40;

        PaginatedBaseWidget.prototype.initialize.call(this, options);



      },

      onAllInternalEvents : function(ev, arg1, arg2){


        if (ev.indexOf("nextEvent")!== -1){
          this.checkLoadMore()
        }

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
          this._masterQuery = apiQuery

          this.paginator.reset();

          this.dispatchRequest(apiQuery)

        }

      },

      defaultQueryArguments: {
        fl: 'title,bibcode'
      },

      renderNewBibcode: function (bibcode, data) {
        //first, check if we have the info in current query docs
        if (this.getMasterQuery() && this.collection.findWhere({bibcode: bibcode})) {

          this.view.render(bibcode, this.collection.findWhere({bibcode: bibcode}), this.collection);

        }
        else if (data){
          this.view.render(bibcode,  new Backbone.Model(data));
        }
        else if (this.getMasterQuery()){
          //the information hasn't arrived yet, so listen to the collection reset event
          this.listenToOnce(this.collection, "collection:augmented", this.renderNewBibcode(bibcode));

        }
        else {
          //we dont have the bibcode
          //processResponse will re-call this function, but with the data parameter

          //is there a better way to avoid pagination?
          var req = this.dispatchRequest(new ApiQuery({'q': 'bibcode:' + bibcode, '__show': this._bibcode}));
        }
      },


      processResponse: function (apiResponse) {

        var r = apiResponse.toJSON();

        //it's an individual bibcode, just render it
        if (apiResponse.has('responseHeader.params.__show')) {
          //make the dict of associated data
          var data = r.response.docs[0]
          if (!data){
            throw new Error("did not recieve bibcode data")
          }
          var data = {title: data.title, bibcode : data.bibcode}
          this.renderNewBibcode(this._bibcode, data);
        }
        else {
          //it's from "inviting_request"

          if (this.paginator.getCycle() <= 1) {
            //it's the first set of results
            this.paginator.setMaxNum(r.response.numFound);
            this.collection.reset(r.response.docs, {
              parse: true
            });
            this.collection.trigger("collection:augmented")
          }
          else {
            this.collection.add(r.response.docs, {
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

          this.renderNewBibcode(bib);

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