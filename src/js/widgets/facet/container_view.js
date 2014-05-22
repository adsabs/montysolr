define(['backbone', 'marionette',
    'js/widgets/base/container_view',
    'js/mixins/widget_pagination',
    'js/components/paginator'
  ],
  function (Backbone, Marionette, ContainerView, WidgetPagination, Paginator) {

    var FacetContainerView = ContainerView.extend({

      initialize: function () {
        ContainerView.prototype.initialize.call(this, arguments);
        this.displayNum = Marionette.getOption(this, "displayNum") || 5;
        this.maxDisplayNum = Marionette.getOption(this, "maxDisplayNum") || 200;
        this.paginator = Marionette.getOption(this, "paginator");
        if (!this.paginator) {
          this.paginator = new Paginator({start: 0, rows: 20});
        }
      },

      //id: "search-results",
      itemViewContainer: ".widget-body",

      itemViewOptions: function (model, index) {
        //if this is the initial round, hide fetchnum - displaynum
        if (this.paginator && this.paginator.getCycle() <= 1) {
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
        this._onRender();
        if (this.collection.models.length >= this.displayNum) {
          this.enableShowMore();
        }
      },

      onShowMore: function() {
        this.trigger('fetchMore', this.$(".item-view.hide").length);
      },

      displayMore: function(howMany) {
        //show hidden data
        this.$(".item-view").filter(".hide").slice(0, howMany).removeClass("hide");
      },

      disableShowMore: function(text) {
        var $sm = this._getShowMore();
        $sm.text('');
      },

      enableShowMore: function(text) {
        var $sm = this._getShowMore();
        $sm.text('Show More');
      },

      _getShowMore: function() {
        var $o = this.$('.widget-options.bottom:first');
        var $sm = $o.find('a[target="ShowMore"]');
        if (! $sm.length) {
          $sm = $('<a title="Show more facets" target="ShowMore"></a>');
          $o.append($sm);
        }
        return $sm;
      }
    });

    _.extend(FacetContainerView.prototype, WidgetPagination);

    return FacetContainerView;
  });