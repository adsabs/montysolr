define(['marionette', 'hbs!./templates/search_bar_template', 'js/components/api_query'], function(Marionette, SearchBarTemplate, ApiQuery) {


  var SearchBarView = Marionette.ItemView.extend({

    template: SearchBarTemplate,

    initialize: function() {},

    events: {
      "click .search-submit": "submitQuery",
      "click .field-options span": "addField",
    },

    addField: function(e) {

      var currentVal, newVal;

      var df = $(e.target).attr("data-field");

      if (df.split("-")[0] === "operator") {

        currentVal = this.$(".q").val();
        if (currentVal !== "") {
          newVal = df.split("-")[1] + ":(" + currentVal + ")";
          this.$(".q").val(newVal);
        } else {
          this.$(".q").val(df.split("-")[1] + ":( )");
        }

      } else {

        currentVal = this.$(".q").val();
        newVal = currentVal + " " + df + ":\"\"";
        this.$(".q").val(newVal);
      }

    },

    submitQuery: function() {
      var query = (this.$(".q").val());
      this.trigger("new_query", query)
    }

  })

  var SearchBarWidget = Marionette.Controller.extend({

    initialize: function() {
      this.view = new SearchBarView();
      this.currentSystemQuery = new ApiQuery(); // empty
    },

    activate: function(beehive) {

      this.pubsub = beehive.Services.get('PubSub');

      _.bindAll(this, "storeQuery");
      this.pubsub.subscribe(this.pubsub.NEW_QUERY, this.storeQuery)

      this.listenTo(this.view, "new_query", function(q) {
        this.submitNewQuery(q);
      });

    },

    onClose: function() {
      this.view.close();
    },

    storeQuery : function(apiQuery){
      this.currentSystemQuery = apiQuery;  
    },

    render: function() {
      return this.view.render().el;
    },

    submitNewQuery: function(query) {
       var queryToSend = this.currentSystemQuery;
       queryToSend.set("q", query);
      this.pubsub.publish(this.pubsub.NEW_QUERY, queryToSend);
    }
  })


  return SearchBarWidget;


});
