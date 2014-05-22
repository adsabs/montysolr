define(['backbone', 'marionette',
    'js/widgets/base/container_view',
    'js/mixins/widget_pagination'
  ],
  function (Backbone, Marionette, ContainerView, WidgetPagination) {

    var FacetContainerView = ContainerView.extend({

      initialize: function () {
        ContainerView.prototype.initialize.call(this, arguments);
        this.displayNum = Marionette.getOption(this, "displayNum") || 5;
        this.maxDisplayNum = Marionette.getOption(this, "maxDisplayNum") || 200;
      },

      id: "search-results",
      itemViewContainer: ".results-list",

      itemViewOptions: function (model, index) {
        //if this is the initial round, hide fetchnum - displaynum
        if (this.paginator.getCycle() <= 1) {
          if (index < this.displayNum) {
            return {hide: false};
          }
          else {
            return {hide: true};
          }

        }
        else {
          //otherwise, keep the defaults (as set by the template)
          return {};
        }
      },


      onRender: function() {
        if (this.openByDefault) {
          this.toggleWidget();
        }
        if (this.showOptions) {
          this.$(".widget-options:first").removeClass("hide");
        }
      },

      onShowMore: function() {
        this.trigger('fetchMore', this.$(".item-view.hide").length);
      },

      displayMore: function(howMany) {
        //show hidden data
        this.$(".results-item").filter(".hide").slice(0, howMany).removeClass("hide");
      },

      disableLoadMore: function(text) {
        this.$('.load-more:first').addClass('hide');
      },

      enableLoadMore: function(text) {
        this.$('.load-more:first').removeClass('hide');
      }
    });

    _.extend(FacetContainerView.prototype, WidgetPagination);

    return FacetContainerView;
  });