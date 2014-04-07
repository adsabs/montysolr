define(['marionette', 'backbone', 'js/components/api_request', 'js/components/api_query', 'hbs!./templates/item-template', 'hbs!./templates/list-template'],

  function(Marionette, Backbone, ApiRequest, ApiQuery, ItemTemplate, ListTemplate) {

    var ItemModel = Backbone.Model.extend({

    });

    var ListCollection = Backbone.Collection.extend({

      model: ItemModel,

      //this function takes the output of apiResponse.toJSON() and builds individual models for 
      //the collection.
      parse: function(raw) {
        var docs = raw.response.docs;
        var highlights = raw.highlighting;

        docs = _.map(docs, function(d) {
          var id = d.id;
          var h = (function() {

            var hl = highlights[id];
            var finalList = [];
            //adding abstract,title, etc highlights to one big list
            _.each(hl, function(val, key) {
              finalList.push(val);
            });
            finalList = _.flatten(finalList);

            return {
              "highlights": finalList
            }
          }());

          return _.extend(d, h);
        });

        return docs;

      }

    });

    var ResultsItemView = Marionette.ItemView.extend({

      initialize: function() {},

      template: ItemTemplate,

      events: {
        'click .view-more': 'toggleExtraInfo'
      },

      toggleExtraInfo: function(e) {
        e.preventDefault();
        this.$(".more-info").toggleClass("hide");
      }

    });


    var ResultsListView = Marionette.CompositeView.extend({
      template: ListTemplate,
      itemView: ResultsItemView,
      itemViewContainer: "#results"

    });


    var ResultsListController = Marionette.Controller.extend({

      activate: function(beehive) {

        this.pubsub = beehive.Services.get('PubSub');
        this.key = this.pubsub.getPubSubKey();

        this.afterActivate();

      },

      requestData: function(apiQuery) {

        var newQuery = apiQuery;

        newQuery.set({
          "hl": "true",
          "hl.fl": "title,abstract"

        });

        var apiRequest = new ApiRequest({
          query: newQuery
        });

        this.pubsub.publish(this.pubsub.DELIVERING_REQUEST, apiRequest)

      },

      processData: function(apiResponse) {

        this.collection.reset(apiResponse.toJSON(), {
          parse: true
        })

      },

      afterActivate: function() {

        this.pubsub.subscribe(this.pubsub.INVITING_REQUEST, this.requestData);
        this.pubsub.subscribe(this.pubsub.DELIVERING_RESPONSE, this.processData);

        //get initial data (not sure if doing this correctly)


      },

      initialize: function() {
        //bind all callback methods here
        _.bindAll(this, "requestData", "processData");
        this.collection = new ListCollection();
        this.view = new ResultsListView({
          collection: this.collection
        });
      },

      render: function() {
        this.view.render();
        return this.view.el;
      },

      returnView: function() {
        return this.view;
      },

      onClose : function(){
        this.view.close()

      }

    })

    return ResultsListController;

  });
