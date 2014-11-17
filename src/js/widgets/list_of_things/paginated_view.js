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


    var PaginationModel = Backbone.Model.extend({

      initialize: function (attrs, options) {
        this.defaults = options.defaults;
        this.attributes = _.result(this, "defaults");
      }
    });

    var PaginationView = Backbone.View.extend({

      initialize: function (options) {
        /*
         * listening to change in perPage value or change in current page
         */
        this.listenTo(this.model, "change", this.render);
        this.getStartVal = options.getStartVal;
      },

      template: PaginationTemplate,

      render: function () {
        var pageData, baseQ, showFirst;
        var minAmountToShowPagination = 25;

        var page = this.model.get("page");
        var perPage = this.model.get("perPage");
        var numFound = this.model.get("numFound");

        var pageNums = this.generatePageNums(page);
        //iterate through remaining pageNums, keep them only if they're possible (< numFound)
        pageNums = this.ensurePagePossible(pageNums, perPage, numFound);

        //now, finally, generate links for each page number
        baseQ = this.model.get("currentQuery");

        if (baseQ) {
          baseQ = baseQ.clone();
          //now, generating the link
          pageData = _.map(pageNums, function (n) {
            var s = this.getStartVal(n.p, perPage);
            baseQ.set("start", s)
            n.link = baseQ.url();
            return n
          }, this);

          baseQ.set("rows", perPage);
        }

        //should we show a "back to first page" button?
        showFirst = (_.pluck(pageNums, "p").indexOf(1) !== -1) ? false : true;

        //only render pagination controls if there are more than 25 results
        if (numFound > minAmountToShowPagination) {
          this.$el.html(PaginationTemplate({
            showFirst: showFirst,
            pageData: pageData,
            currentPage: page,
            perPage: this.model.get("perPage"),
            currentQuery: this.model.get("currentQuery")}));
        }
        else {
          this.$el.html("");
        }
        return this
      },


      //create list of up to 5 page numbers to show
      generatePageNums: function (page) {

        var pageNums = _.map([-2, -1, 0, 1, 2 , 3, 4], function (d) {
          var current = (d === 0) ? true : false;
          return {p: page + d, current: current}
        });

        //page number can't be less than 1
        pageNums = _.filter(pageNums, function (d) {
          if (d.p > 0) {
            return true
          }
        });
        return pageNums.slice(0, 5);
      },

      //iterate through pageNums, keep them only if they're possible (< numFound)
      ensurePagePossible: function (pageNums, perPage, numFound) {
        var endIndex = numFound - 1
        return  _.filter(pageNums, function (n) {
          if (this.getStartVal(n.p, perPage) <= endIndex) {
            return true
          }
        }, this)
      },

      events: {
        "click a": "changePage",
        "input .per-page": "changePerPage"

      },

      changePage: function (e) {
        var d = $(e.target).data("paginate");
        this.model.set("page", d);
        return false
      },

      changePerPage: _.debounce(function (e) {
        var perPage = parseInt($(e.target).val());
        this.model.set("perPage", perPage);
      }, 2000)

    });



    /**
     * A simple model that holds attributes of the
     * paginated view. Changes in this model are
     * propagated to the view
     */
    var MainViewModel = Backbone.Model.extend({
      defaults: function () {
        return {
          showDetailsButton: false,
          mainResults: false
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
      },

      className: "list-of-things",
      itemView: ItemView,
      alreadyRendered: false,

      xshowCollection: function(){
        var ItemView;
        this.collection.each(function(item, index){
          if (item.attributes.visible) {
            ItemView = this.getItemView(item);
            this.addItemView(item, ItemView, index);
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

      itemViewContainer: ".results-list",
      events: {
        "click .show-details": "showDetails"
      },

      modelEvents: {
        "change": "render"
      },

      template: ResultsContainerTemplate,

      onRender: function () {
        //this.paginationView.setElement(this.$(".pagination-controls")).render();
      },

      /**
       * Displays the are inside of every item-view
       * with details (this place is normally hidden
       * by default)
       */
      showDetails: function (ev) {
        if (ev)
          ev.stopPropagation();
        this.model.set('showDetailsButton', false, {silent: true});

        this.$(".more-info").toggleClass("hide");
        if (this.$(".more-info").hasClass("hide")) {
          this.$(".show-details").text("Show details");
        } else {
          this.$(".show-details").text("Hide details");
        }
      }
    });

    return ListOfThingsView;
  });