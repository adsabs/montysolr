define(["backbone",
    'hbs!./templates/abstract-title',
    'hbs!./templates/abstract-title-nav-descriptor',
    'js/components/api_query'
  ],
  function(Backbone,
    abstractTitleTemplate,
    abstractTitleNavDescriptor,
    ApiQuery
    ){

  var AbstractTitleModel = Backbone.Model.extend({

    defaults : {

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


    onRender : function(){
      this.renderTitleNavDescriptor();
    }


  });


  var AbstractViewMixin = {

    activate: function (beehive) {

      this.pubsub = beehive.Services.get('PubSub');

      _.bindAll(this, ['dispatchInitialRequest', 'processResponse']);

      //custom dispatchRequest function goes here
      this.pubsub.subscribe(this.pubsub.INVITING_REQUEST, this.dispatchInitialRequest);

      //custom handleResponse function goes here
      this.pubsub.subscribe(this.pubsub.DELIVERING_RESPONSE, this.processResponse);


    },

    //requires this.collection to already be defined
    returnNewTitleView : function(){

      return new AbstractTitleView({collection: this.collection});

    },

    updateTitleModel : function(current){

      var data = {};

      data.title = current.get("title");
      data.bibcode = current.get("bibcode");

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
        this.collection.add({title: data.title, bibcode : data.bibcode})

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

        var docsToAdd = _.map(docsToAdd, function(d){
          return {title : d.title, bibcode: d.bibcode}

        })

        this.collection.reset(docsToAdd)


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