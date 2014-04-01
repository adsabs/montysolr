define(['marionette', 'hbs!./templates/search_bar_template', 'js/components/api_query', 'bootstrap'], function(Marionette, SearchBarTemplate, apiQuery) {

    //this widget is just a view with access to beehive pubsub to publish the new query
    //when it is submitted


    var SearchBarView = Marionette.ItemView.extend({

        template: SearchBarTemplate,

        events: {
            "click .search-submit": "submitQuery",
            "keydown .q": "suggestQuery",
        },

        submitQuery: function() {
            var query = (this.$(".q").val());
            query = new apiQuery({q :query});
            beehive.publish(beehive.NEW_QUERY, query)
        },

        //if we want to do autocomplete?
        suggestQuery: function() {
            // console.log(this.$(".q").val())

        },

        activate: function() {

            var pubsub = beehive.Services.get('PubSub');
            this.pubSubKey = pubsub.getPubSubKey();
        }


    })


    return SearchBarView




})
