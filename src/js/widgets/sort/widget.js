/**
 * Created by alex on 5/19/14.
 */
define(['marionette',
  'backbone',
  'jquery',
  'js/widgets/base/base_widget',
  'hbs!js/widgets/sort/templates/sort_template',
  'bootstrap',
  'js/components/api_feedback',
  'analytics'

], function (Marionette,
             Backbone,
             $,
             BaseWidget,
             SortTemplate,
             bootstrap,
             ApiFeedback,
             analytics) {

  var SortModel = Backbone.Model.extend();

  var SortView = Marionette.ItemView.extend({

    template: SortTemplate,

    modelEvents: {
      change: "render"
    },

    events: {
      "change #sort-select": "changeSort"
    },

    changeSort: function (ev) {
      this.trigger("change-sort", ev.target.value);
    }
  });

  var SortWidget = BaseWidget.extend({

    initialize: function (options) {
      this.model = new SortModel();
      this.view = new SortView({model: this.model});
      this.listenTo(this.view, "change-sort", this.submitQuery);
      BaseWidget.prototype.initialize.apply(this, arguments)
    },

    activate: function (beehive) {
      _.bindAll(this, "handleFeedback");
      this.setBeeHive(beehive);
      this.getPubSub().subscribe(this.getPubSub().FEEDBACK, _.bind(this.handleFeedback, this));
    },

    submitQuery: function (data) {
      var apiQuery = this.getCurrentQuery().clone();
      apiQuery.set("sort", data);
      this.getPubSub().publish(this.getPubSub().START_SEARCH, apiQuery);
      analytics('send', 'event', 'interaction', 'sort-applied', data);
    },

    extractSort : function(sortString) {
      //sort string could have multiple automatically-applied secondary sorts, so remove them
      //relevancy desc is default solr sort, so use that as a back up (but shouldnt be necessary)
      var sort = sortString.split(",")[0] || "relevancy desc";
      return sort;
    },

    handleFeedback: function (feedback) {
      if (feedback.code === ApiFeedback.CODES.SEARCH_CYCLE_STARTED ) {
        try {
          this.setCurrentQuery(feedback.query);
          this.model.set("sort", this.extractSort(feedback.query.get("sort")[0]))
        } catch (e) {
          console.warn('sort widget could not detect any query sort')
        }
      }
    }

  });

  return SortWidget
});
