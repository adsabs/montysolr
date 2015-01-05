/**
 * Created by alex on 5/19/14.
 */
define(['marionette',
  'backbone',
  'jquery',
  'js/widgets/base/base_widget',
  'hbs!./templates/sort_template',
  'bootstrap',
  'js/components/api_feedback'
], function (Marionette,
  Backbone,
  $,
  BaseWidget,
  SortTemplate,
  bootstrap,
  ApiFeedback
  ) {

    var SortModel = Backbone.Model.extend({

      defaults: function () {
        return {
          sortOptions : [
          {value: "classic_factor", title: "Relevancy", default: true },
          {value: "date", title: "Publication Date" },
          {value: "citation_count", title: "Citation Count" },
          {value: "read_count", title: "Popularity" }],

          orderOptions : [
            {value: "desc", title: "Descending", default: true},
            {value: "asc", title: "Ascending"}]
        }
      },

      removeDefaults : function(){

        //arrays are pass by reference so
        //don't need to explicitly set

        var sortOptions = this.get("sortOptions");
        var orderOptions = this.get("orderOptions");

        _.each(sortOptions, function(o) {
          if (o.default) {
            delete o.default
          }
        });
        _.each(orderOptions, function(o) {
          if (o.default) {
            delete o.default
          }

        });
      },

      addDefault : function(attributeKey, value){

        _.each(this.get(attributeKey), function(d){
          if(d.value === value){
            d.default = true
          }
        })
      }

    });

    var SortView = Marionette.ItemView.extend({

      initialize: function (options) {
        //using custom event because backbone doesnt do nested events
        this.listenTo(this.model, "change:formData", this.render)
      },

      template: SortTemplate,

      events: {
        "click .choose-sort": "changeSort",
        "click .dropdown-menu label": "preventClose"
      },

      preventClose : function(e){
        e.stopPropagation();
      },

      serializeData: function () {
        var newJSON;
        newJSON = this.model.toJSON();
        _.each(newJSON.sortOptions, function(input){

          if (input.default){
            newJSON.current = input.title
          }
        });

        //and appending "asc" if that is the default order
        _.each(newJSON.orderOptions, function(input){
          if (input.value === "asc" && input.default === true){
            newJSON.current += " Asc";
          }
        });

        //so we have a reference
        this.model.set("current", newJSON.current);
        return newJSON;
      },

      getCurrentSortVal : function() {
        var currentSortVal = '';
        var j = this.model.toJSON();
        _.each(j.sortOptions, function(input){
          if (input.default){
            currentSortVal = input.title
          }
        });

        _.each(j.orderOptions, function(input){
          if ( input.default === true){
            currentSortVal += " ";
            currentSortVal += input.value;
          }
        });
        return currentSortVal;
      },

      changeSort: function (ev) {
        var newVal, current;
        ev.preventDefault();

        current = this.getCurrentSortVal();
        newVal = this.$("input[name=sort-options]:checked").attr("value");
        var order = this.$("input[name=order-options]:checked").attr("value");

        newVal += " ";
        newVal += order;

        //console.log('BANG!');
        if (newVal !== current) {
          this.trigger("sortChange", newVal)
        }
      }
    });

    var SortWidget = BaseWidget.extend({

      initialize: function (options) {
        this.model = new SortModel();
        this.view = new SortView({model : this.model});
        this.listenTo(this.view, "all", this.onAll);
        BaseWidget.prototype.initialize.apply(this, arguments)
      },

      activate: function (beehive) {
        _.bindAll(this, "handleFeedback");
        this.pubsub = beehive.Services.get('PubSub');

        // widget doesn't need to execute queries (but it needs to listen to them)
        this.pubsub.subscribe(this.pubsub.FEEDBACK, _.bind(this.handleFeedback, this));
      },

      onAll: function (ev, data) {
        if (ev == "sortChange") {
          //    find current sort values
          this.submitQuery(data)
        }
      },

      submitQuery: function (data) {
        var apiQuery = this.getCurrentQuery();
        apiQuery.set("sort", data);
        this.pubsub.publish(this.pubsub.START_SEARCH, apiQuery);
      },

      handleFeedback: function(feedback) {
        switch (feedback.code) {
          case ApiFeedback.CODES.SEARCH_CYCLE_STARTED:
            this.setCurrentQuery(feedback.query);
            this.extractSort(feedback.query);
            break;
        }
      },

      extractSort: function (q) {

        var params, sortVals;

        if (q.has('sort')) {
          sortVals = q.get('sort')[0].split(/\s+/);
          this.model.removeDefaults();
          this.model.addDefault("sortOptions", sortVals[0]);
          this.model.addDefault("orderOptions", sortVals[1]);
        }
        else { // if there is no sort indicated, it is the default, "relevant"
          this.view.model.set(_.result(this.model, "defaults"))
        }
        //need to explicitly tell the view that the model changed
        this.model.trigger("change:formData")
      }

    });

    return SortWidget
  });