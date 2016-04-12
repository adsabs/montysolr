/**
 * Paginated view - it displays controls under the list of items.
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
    'js/mixins/add_stable_index_to_collection',
    'hbs!./templates/empty-view-template',
    'hbs!./templates/initial-view-template',
    './item_view',
    'analytics',
    'mathjax'
  ],

  function (
            Marionette,
            Backbone,
            ApiRequest,
            ApiQuery,
            BaseWidget,
            ItemTemplate,
            ResultsContainerTemplate,
            LinkGenerator,
            WidgetPaginationMixin,
            EmptyViewTemplate,
            InitialViewTemplate,
            ItemView,
            analytics,
            MathJax
    ) {

    /**
     * A simple model that holds attributes of the
     * paginated view. Changes in this model are
     * propagated to the view
     */
    var MainViewModel = Backbone.Model.extend({
      defaults: function () {
        return {
          mainResults: false,
          title : undefined,
          //assuming there will always be abstracts
          showAbstract: "closed",
          //often they won't exist
          showHighlights: false,
          pagination: true,
          start: 0
        }
      }
    });

    /**
     * This is the main view of the list of things. A composite
     * view that holds collection of items.
     */
    var ListOfThingsView = Marionette.CompositeView.extend({

      childView : ItemView,

      initialize: function (options) {
        this.EmptyViewClass = Marionette.ItemView.extend({
          template: EmptyViewTemplate
        });
        this.InitialViewClass = Marionette.ItemView.extend({
          template: InitialViewTemplate
        });

        this.model = new MainViewModel();

      },

      serializeData : function(){
        var data = this.model.toJSON();
        //if it's an abstract page list with an 'export to results page'
        //option, provide the properly escaped url
        if (data.queryOperator){
          data.queryURL = "#search/q=" + data.queryOperator + "(";
          data.queryURL += encodeURIComponent("bibcode:" + data.bibcode) + ")";
          if (data.removeSelf) data.queryURL += encodeURIComponent(" -bibcode:" + data.bibcode);
          if (data.sortOrder) data.queryURL += "&sort=" + encodeURIComponent(data.sortOrder);
        }
        return data;
      },

      onRender: function () {
        if (MathJax) MathJax.Hub.Queue(["Typeset", MathJax.Hub, this.el]);
      },

      className: "list-of-things",
      
      alreadyRendered: false,

      getEmptyView: function () {
        if (this.alreadyRendered)
          return this.EmptyViewClass;
        else {
          this.alreadyRendered = true;
          return this.InitialViewClass;
        }
      },

      childViewContainer: ".results-list",

      events: {
        "click .show-highlights": "toggleHighlights",
        "click .show-abstract": "toggleAbstract",
        "click a.page-control": "changePageWithButton",
        "keyup input.page-control": "tabOrEnterChangePageWithInput",
        "click .per-page": "changePerPage"
      },

      toggleHighlights: function () {
        if (this.model.get("showHighlights") == "open") {
          this.model.set("showHighlights", "closed");
        }
        else if (this.model.get("showHighlights") == "closed") {
          this.model.set("showHighlights", "open");
        }
      },

      toggleAbstract: function () {
        if (this.model.get("showAbstract") == "open") {
          this.model.set("showAbstract", "closed");
        }
        else if (this.model.get("showAbstract") == "closed") {

          this.model.set("showAbstract", "open");
          analytics('send', 'event', 'interaction', 'abstracts-toggled-on');
        }
      },

      modelEvents: {
        "change": "render",
        "change:showHighlights": "toggleChildrenHighlights",
        "change:showAbstract": "toggleChildrenAbstracts"
      },

      collectionEvents: {
        "reset": "resetViewModel"
      },

      template: ResultsContainerTemplate,

      resetViewModel: function () {
        var defaults = this.model.defaults();

        this.model.set({
          showAbstract: defaults.showAbstract,
          showHighlights: defaults.showHighlights
        });
      },

      /**
       * Displays the are inside of every item-view
       * with details (this place is normally hidden
       * by default)
       */
      toggleChildrenHighlights: function () {

        var show = this.model.get("showHighlights");

        var itemVal = show === 'open' ? true : false;

        this.collection.each(function (m) {
          //notify each item view to rerender itself and show/hide details
          m.set("showHighlights", itemVal);
        });
      },

      toggleChildrenAbstracts: function () {

        var show = this.model.get("showAbstract");
        var itemVal = show === 'open' ? true : false;
        this.collection.each(function (m) {
          //notify each item view to rerender itself and show/hide details
          m.set("showAbstract", itemVal);
        });
      },

      changePageWithButton: function (e) {

        e.preventDefault();
        var $target = $(e.currentTarget);
        if ($target.parent().hasClass("disabled")) return;
        var transform = $target.hasClass("next-page") ? 1 : -1;
        var pageVal = this.model.get("page") + transform;
        this.trigger('pagination:select', pageVal);

        if (this.resultsWidget) {analytics('send', 'event', 'interaction', 'results-list-pagination', pageVal) }
      },

      tabOrEnterChangePageWithInput : function (e) {
        //subtract one since pages are 0 indexed
        var pageVal = parseInt($(e.target).val() - 1);
        //enter or tab
        if (e.keyCode == 13 || e.keyCode == 9){
          this.trigger('pagination:select', pageVal);
        }

        if (this.resultsWidget) {analytics('send', 'event', 'interaction', 'results-list-pagination', pageVal) }

      },

      changePerPage: function (e) {
         e.preventDefault();
        var val = parseInt($(e.target).text().trim());
        if (val === this.model.get("perPage")) return;
        this.trigger("pagination:changePerPage", val)
      }


    });



    return ListOfThingsView;

  });