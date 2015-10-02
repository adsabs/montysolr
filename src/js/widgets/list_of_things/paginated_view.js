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
    'hbs!./templates/pagination-template',
    'js/mixins/add_stable_index_to_collection',
    'hbs!./templates/empty-view-template',
    'hbs!./templates/initial-view-template',
    './item_view'
  ],

  function (Marionette,
            Backbone,
            ApiRequest,
            ApiQuery,
            BaseWidget,
            ItemTemplate,
            ResultsContainerTemplate,
            LinkGenerator,
            PaginationTemplate,
            WidgetPaginationMixin,
            EmptyViewTemplate,
            InitialViewTemplate,
            ItemView
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

      constructor: function (options) {
        var self = this;
        options = options || {};
        if (options) {
          _.defaults(options, _.pick(this, ['model', 'collectionEvents', 'modelEvents']));
        }
        if (!options.model) {
          options.model = new MainViewModel();
        }

        var args = _.toArray(arguments);
        args[0] = options;

        return Marionette.CompositeView.prototype.constructor.apply(this, args);
      },

      initialize: function (options) {
        this.EmptyViewClass =  Marionette.ItemView.extend({
          template: EmptyViewTemplate
        });
        this.InitialViewClass = Marionette.ItemView.extend({
          template: InitialViewTemplate
        });

        //for instructions on how to view list in results page
        if (options.operator) {
          this.model.set("operator", true);
          this.model.set("queryOperator", options.queryOperator);
        }

      },

      className: "list-of-things",
      childView: ItemView,
      alreadyRendered: false,

      xshowCollection: function(){
        var ItemView;
        this.collection.each(function(item, index){
          if (item.attributes.visible) {
            ItemView = this.getChildView(item);
            this.addChild(item, ItemView, index);
          }
        }, this);
      },


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
        "click a[data-paginate]": "changePage",
        "input .per-page": "changePerPage"
      },

      toggleHighlights : function(){
        if (this.model.get("showHighlights") == "open"){
          this.model.set("showHighlights", "closed");
        }
        else if (this.model.get("showHighlights") == "closed"){
          this.model.set("showHighlights", "open");
        }
      },

      toggleAbstract : function(){
        if (this.model.get("showAbstract") == "open"){
          this.model.set("showAbstract", "closed");
        }
        else if (this.model.get("showAbstract") == "closed"){
          this.model.set("showAbstract", "open");
        }
      },

      modelEvents: {
        "change": "render",
        "change:showHighlights" : "toggleChildrenHighlights",
        "change:showAbstract" : "toggleChildrenAbstracts"
      },

      collectionEvents : {
        "reset" : "resetViewModel"
      },

      template: ResultsContainerTemplate,

      resetViewModel : function(){
        var defaults = this.model.defaults();

        this.model.set({
          showAbstract : defaults.showAbstract,
          showHighlights : defaults.showHighlights
        });
      },

      /**
       * Displays the are inside of every item-view
       * with details (this place is normally hidden
       * by default)
       */
      toggleChildrenHighlights : function () {

        var show = this.model.get("showHighlights");

        var itemVal = show === 'open' ? true : false;

        this.collection.each(function(m){
          //notify each item view to rerender itself and show/hide details
          m.set("showHighlights", itemVal);
        });
      },

      toggleChildrenAbstracts : function () {

        var show = this.model.get("showAbstract");

        var itemVal = show === 'open' ? true : false;

        this.collection.each(function(m){
          //notify each item view to rerender itself and show/hide details
          m.set("showAbstract", itemVal);
        });
      },

      changePage: function (e) {
        var d = $(e.target).data("paginate");
        this.trigger('pagination:select', d);
        e.preventDefault();
        //scroll to top in preparation for loading of new records
        document.body.scrollTop = document.documentElement.scrollTop = 0;
      },

      changePerPage: _.debounce(function (e) {
        var perPage = parseInt($(e.target).val());
        this.trigger('pagination:change', perPage);
        e.preventDefault();
      }, 2000)
    });

    return ListOfThingsView;
  });