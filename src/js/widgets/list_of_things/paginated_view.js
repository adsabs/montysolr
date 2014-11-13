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
    'hbs!./templates/initial-view-template'
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
            InitialViewTemplate
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
        var d = $(e.target).data("paginate")
        this.model.set("page", d);
        return false
      },

      changePerPage: _.debounce(function (e) {
        var perPage = parseInt($(e.target).val());
        this.model.set("perPage", perPage);
      }, 2000)

    });


    var ItemView = Marionette.ItemView.extend({
      tagName: "li",
      className: "col-sm-12 s-display-block",
      template: ItemTemplate,

      /**
       * This method prepares data for consumption by the template
       *
       * @returns {*}
       */
      serializeData: function () {

        var data , shownAuthors;
        data = this.model.toJSON();

        var maxAuthorNames = 3;

        if (data.author && data.author.length > maxAuthorNames) {
          data.extraAuthors = data.author.length - maxAuthorNames;
          shownAuthors = data.author.slice(0, maxAuthorNames);
        } else if (data.author) {
          shownAuthors = data.author
        }

        if (data.author) {
          var l = shownAuthors.length - 1;
          data.authorFormatted = _.map(shownAuthors, function (d, i) {
            if (i == l || l == 0) {
              return d; //last one, or only one
            } else {
              return d + ";";
            }
          })
        }
        //if details/highlights
        if (data.details) {
          data.highlights = data.details.highlights
        }

        data.orderNum = this.model.get("resultsIndex") + 1;
        return data;
      },

      events: {
        'change input[name=identifier]': 'toggleSelect',
        'mouseenter .letter-icon': "showLinks",
        'mouseleave .letter-icon': "hideLinks",
        'click .letter-icon': "pinLinks"
      },

      toggleSelect: function () {
        this.$el.toggleClass("chosen");
      },

      /*
       * adding this to make the dropdown
       * accessible, and so people can click to sticky
       * open the quick links
       * */


      removeActiveQuickLinkState: function ($node) {

        $node.removeClass("pinned");
        $node.find("i").removeClass("s-icon-draw-attention")
        $node.find(".link-details").addClass("hidden");
        $node.find('ul').attr('aria-expanded', false);

      },

      addActiveQuickLinkState: function ($node) {

        $node.find("i").addClass("s-icon-draw-attention")
        $node.find(".link-details").removeClass("hidden");
        $node.find('ul').attr('aria-expanded', true);

      },

      deactivateOtherQuickLinks: function ($c) {

        var $hasList = this.$(".letter-icon").filter(function () {
          if ($(this).find("i").hasClass("s-icon-draw-attention")) {
            return true
          }
        }).eq(0);

        //there should be max 1 other icon that is active

        if ($hasList.length && $hasList[0] !== $c[0]) {

          this.removeActiveQuickLinkState($hasList)
        }
      },

      pinLinks: function (e) {
        var $c = $(e.currentTarget);

        if (!$c.find(".active-link").length) {
          return
        }
        $c.toggleClass("pinned");
        if ($c.hasClass("pinned")) {
          this.deactivateOtherQuickLinks($c);
          this.addActiveQuickLinkState($c);
        }
        else {
          this.removeActiveQuickLinkState($c);
        }
      },

      showLinks: function (e) {
        var $c = $(e.currentTarget);
        if (!$c.find(".active-link").length) {
          return;
        }
        if ($c.hasClass("pinned")) {
          return;
        }
        else {
          this.deactivateOtherQuickLinks($c);
          this.addActiveQuickLinkState($c)
        }
      },

      hideLinks: function (e) {
        $c = $(e.currentTarget);
        if ($c.hasClass("pinned")) {
          return
        }
        this.removeActiveQuickLinkState($c)
      }
    });

    var ListViewModel = Backbone.Model.extend({
      defaults: function () {
        return {
          showDetailsButton: false,
          mainResults: false
        }
      }
    });


    var VisibleCollectionEmptyView = Marionette.ItemView.extend({
      template: EmptyViewTemplate
    });

    var VisibleCollectionInitialView = Marionette.ItemView.extend({
      template: InitialViewTemplate
    });

    var ListView = Marionette.CompositeView.extend({
      initialize: function (options) {
        this.paginationView = options.paginationView;
        this.model = new ListViewModel();
      },

      className: "list-of-things",
      itemView: ItemView,
      alreadyRendered: false,

      getEmptyView: function () {
        if (this.alreadyRendered)
          return VisibleCollectionEmptyView;
        else {
          this.alreadyRendered = true;
          return VisibleCollectionInitialView;

        }
      },

      itemViewContainer: ".results-list",
      events: {
        "click .show-details": "showDetails"
      },

      //calls to render will render only the model after the 1st time
      modelEvents: {
        "change": "render"
      },

      template: ResultsContainerTemplate,

      onRender: function () {
        this.paginationView.setElement(this.$(".pagination-controls")).render();
      },

      /**
       * Displays the are inside of every item-view
       * with details (this place is normally hidden
       * by default)
       */
      showDetails: function (ev) {
        if (ev)
          ev.stopPropagation();

        this.$(".more-info").toggleClass("hide");
        if (this.$(".more-info").hasClass("hide")) {
          this.$(".show-details").text("Show details");
        } else {
          this.$(".show-details").text("Hide details");
        }
      }
    });
  });