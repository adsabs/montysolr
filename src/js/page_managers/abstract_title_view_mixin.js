define(["backbone",
    'hbs!./templates/abstract-title',
    'hbs!./templates/abstract-title-nav-descriptor',
  ],
  function(Backbone,
    abstractTitleTemplate,
    abstractTitleNavDescriptor
    ){

  var AbstractTitleModel = Backbone.Model.extend({

    defaults : {
      originalSearchResult : undefined,
      index: undefined,
      prev: undefined,
      next : undefined,
      bibcode : undefined,
      title : undefined,
      numResults : undefined,
      queryUrl : undefined

    }

  })

  var AbstractTitleView = Backbone.View.extend({

    template : abstractTitleTemplate,

    descriptorTemplate : abstractTitleNavDescriptor,

    initialize : function(){
      this.model = new AbstractTitleModel();
      this.listenTo(this.model, "change", this.render);
      this.listenTo(this.collection, "subPageChanged", this.renderTitleNavDescriptor)
      this.on("render", this.onRender)

    },

    showLoad : true,

    render : function(){

      this.$el.html(this.template(this.model.toJSON()));

      this.trigger("render")

      return this
    },

    renderTitleNavDescriptor : function(){

      var descriptor = this.collection.subPage? this.collection.subPage.descriptor : "";

      this.$(".nav-descriptor").html(this.descriptorTemplate({descriptor: descriptor}))

    },

    events : {"click .abstract-paginator-next" : "LoadMore",
      "click .abstract-paginator-prev": "LoadMore"
    },

    onRender : function(){
      this.renderTitleNavDescriptor();
    },

    LoadMore : function(e){
      this.trigger("loadMore")
    }

  });


  var AbstractViewMixin = {

    activate: function (beehive) {

      this.pubsub = beehive.Services.get('PubSub');

      _.bindAll(this, ['dispatchInitialRequest', 'processResponse', 'autoPaginate']);

      //custom dispatchRequest function goes here
      this.pubsub.subscribe(this.pubsub.INVITING_REQUEST, this.dispatchInitialRequest);

      //custom handleResponse function goes here
      this.pubsub.subscribe(this.pubsub.DELIVERING_RESPONSE, this.processResponse);

      //for the title view (widget mixin)
      this.pubsub.subscribe(this.pubsub.CUSTOM_EVENT, this.autoPaginate);


    },

    //requires this.collection to already be defined
    returnNewTitleView : function(){

      return new AbstractTitleView({collection: this.collection});

    },

    updateTitleModel : function(current){

      var data = {};

      data.title = current.get("title");
      data.bibcode = current.get("bibcode");
      data.originalSearchResult = false;

      //only send number to template if it is in a set of results
      if (current.get("resultsIndex") !== undefined){
        data.originalSearchResult = true;
        data.queryURL =  "/search/" + this.getMasterQuery().url();
        data.totalResults = this.numFound;

        data.index = current.get("resultsIndex");
        data.prevBib = this.collection.findWhere({"resultsIndex": data.index-1});
        data.nextBib = this.collection.findWhere({"resultsIndex": data.index+1});
        //so we start at 1 rather than 0 in the template
        data.index++;


        data.prevBib = data.prevBib ? data.prevBib.get("bibcode"): undefined;
        data.nextBib = data.nextBib ? data.nextBib.get("bibcode") : undefined;
      }

      this.titleView.model.set(data)
    },


    processResponse: function (apiResponse) {

      //it's an individual bibcode, just render it
      if (apiResponse.has('responseHeader.params.__show')) {
        //make the dict of associated data
        var data = apiResponse.get("response.docs")[0]
        if (!data){
          throw new Error("did not receive bibcode data")
        };
        /* we are adding to the model the notion that this bib didn't come from
         * a system-wide query, i.e. they clicked on a title within the abstract page
         * rather than in the results page
         *
         */
        this.collection.add({title: data.title, bibcode : data.bibcode, resultsIndex : undefined})

        this.renderNewBibcode(this._bibcode);
      }
      else {
        /*
            it's from "inviting_request"
            indicate that this came from a system-wide search
            this is using the paginated mixin method
         */

        var docs = apiResponse.get("response.docs")

        //will reset or add to the collection with paginated docs
        //this comes from the widget pagination mixin
        this.insertPaginatedDocsIntoCollection(docs, apiResponse)

      }

    },

    //keep in sync with results list on results page

    //will reset the collection to anything returned here
    //this is probably inefficient?

    autoPaginate : function(eventData){

      //requesting too much?
      if (eventData.event === "pagination" ){

        var q = this.getCurrentQuery().clone();
        var start = _.max([eventData.data.start-10 , 0]);
        q.unlock();
        q.set("start", start );
        //assuming that most people wont be interested in that many abstracts
        q.set("rows", 30);

        this.dispatchRequest(q);

      }

    },

    checkLoadMore : function(){

      var resultsIndexes = this.collection.pluck("resultsIndex");

      var maxIndex = _.max(resultsIndexes);
      var minIndex = _.min(resultsIndexes);
      var ind = this.collection.findWhere({bibcode: this._bibcode}).get("resultsIndex")

      //fetch more if there are 5 or fewer records on either side
      if (maxIndex - ind === 5  && maxIndex < this.numFound){

        var q = this.getCurrentQuery().clone();
        q.unlock();
        q.set("start",maxIndex + 1 );
        q.set("rows", 10 );

      }
      else if (ind - minIndex === 5 && minIndex > 0){
        var q = this.getCurrentQuery().clone();
        q.unlock();
        q.set("start", _.max([minIndex, 0]));
        q.set("rows", 10);

      }

      if (q){
        this.dispatchRequest(q)

      }

    },


    dispatchInitialRequest: function (apiQuery) {

      this.numFound = undefined;


      this.setCurrentQuery(apiQuery);

      //tells you what the people have searched (not the widget's query)
      this._masterQuery = apiQuery;

      this.dispatchRequest(apiQuery)

    },

    defaultQueryArguments: {
      fl: 'title,bibcode'
    },


    renderNewBibcode: function () {

     var current = this.collection.findWhere({bibcode: this._bibcode});

      if (current) {

        //this function is in the abstract title view mixin
        this.updateTitleModel(current);
      }

      else {
        //we dont have the bibcode
        //processResponse will re-call this function, but with the data parameter

        var req = this.composeRequest(new ApiQuery({'q': 'bibcode:' + this._bibcode, 'fl': "title,bibcode", '__show': this._bibcode}));
        if (req) {
          this.pubsub.publish(this.pubsub.DELIVERING_REQUEST, req);
        }
      }
    }

  }

  return AbstractViewMixin


})