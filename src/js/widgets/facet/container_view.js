define(['backbone', 'marionette',
    'js/widgets/base/container_view',
    'js/mixins/widget_pagination',
    'js/components/paginator',
    'js/widgets/facet/item_view',
    'hbs!./templates/tooltip',
    'hbs!./templates/widget-container'
  ],
  function (Backbone,
    Marionette,
    ContainerView,
    WidgetPagination,
    Paginator,
    BaseItemView,
    FacetTooltipTemplate,
    WidgetContainerTemplate
    ) {

    var FacetContainerView = ContainerView.extend({

      initialize: function (options) {
        ContainerView.prototype.initialize.call(this, arguments);
        this.displayNum = Marionette.getOption(this, "displayNum") || 5;
        this.maxDisplayNum = Marionette.getOption(this, "maxDisplayNum") || 200;
        this.paginator = Marionette.getOption(this, "paginator");


        if (!this.paginator) {
          this.paginator = new Paginator({start: 0, rows: 20});
        }

        this.logicOptions = Marionette.getOption(this, "logicOptions");

        if (this.logicOptions) {
          if (!_.isObject(this.logicOptions) || !('single' in this.logicOptions && 'multiple' in this.logicOptions)) {
            throw new Error('logicOptions should be null or an object with single/multiple keys and arrays of strings inside');
          }

          this.on('all', function (ev, info) {
            if (ev.indexOf('itemClicked') > -1
              || ev.indexOf('treeClicked') > -1
              || ev == "render:collection" )  {
              this.refreshLogicTooltip();
            }
          });

        }

        //show only tiny loading indicators, the widget_state_handling mixin will use this flag
        //in deciding which template to use
        this.smallLoadingIcon = true;

      },

      //id: "search-results",
      childView: BaseItemView,
      template: WidgetContainerTemplate,

      childViewContainer:".widget-body",

      events: {

        "click .widget-options.top": "onClickOptions",
        "click .widget-options.bottom": "onClickOptions",
        "click .widget-name" : "toggleWidget",
        "click .dropdown-menu .close": "closeLogic",
        "click .logic-container label": "onLogic"

      },

      childViewOptions: function (model, index) {
//       merging in options from factory stage
        var additionalOptions = Marionette.getOption(this, "additionalItemViewOptions") || {};

        return _.extend({hide: true}, additionalOptions);

        //if this is the initial round, hide fetchnum - displaynum
        if (this.paginator && this.paginator.getCycle() <= 1) {
          if (index < this.displayNum) {
            return _.extend({hide: false}, additionalOptions);
          }
          else {
            return _.extend({hide: true}, additionalOptions);
          }
        }
        else {
          //otherwise, keep the defaults (as set by the template)
          return additionalOptions;
        }
      },


      onRender: function() {
        this._onRender();
        if (this.collection && this.collection.models.length > this.displayNum) {
          this.enableShowMore();
        }
        else {
          this.disableShowMore();
        }
      },

      getNumHidden: function() {
        return this.$(".widget-body:first").children('.item-view.hide').length;
      },

      onShowMore: function() {
        this.trigger('fetchMore', this.getNumHidden());
      },

      displayMore: function(howMany) {
        //show hidden data
        var $hidden = this.$('.widget-body:first').children('.item-view').filter('.hide'),
            hiddenLength = $hidden.length;
            $hidden.slice(0,howMany).removeClass('hide');
        if (hiddenLength > 0) {
          var offset = this.collection.models.length - hiddenLength;
          var max = this.children.length-offset;
          for (var i=0;i<howMany && i<max;i++) {
            this.children.findByIndex(offset+i).trigger('treeNodeDisplayed');
          }
        }
      },

      disableShowMore: function(text) {
        var $sm = this._getShowMore();
        $sm.addClass('hide');
      },

      enableShowMore: function(text) {
        var $sm = this._getShowMore();
        $sm.removeClass('hide');
        //$sm.text('show more');
      },

      _getShowMore: function() {
        var $o = this.$('.widget-options.bottom:first');
        var $sm = $o.find("button[wtarget=ShowMore]");
        if (!$sm.length) {
          $sm = $('<button class="btn btn-xs btn-default btn-inverse" wtarget="ShowMore">more <i class="fa fa-caret-down"></i></button>');
          $o.append($sm);
        }
        return $sm;
      },


      closeLogic: function (e) {
        if (e){
          e.stopPropagation();
        }
        this.$(".logic-dropdown").addClass("hidden");
      },

      onLogic: function(ev) {
        if (ev)
        ev.stopPropagation();
        //close the logic dropdown
        this.closeLogic();
        var val = $(ev.currentTarget).find("input").val();
        this.trigger("containerLogicSelected", val);
      },

      refreshLogicTooltip: function(arg1){

        var selected = this.$(".widget-item:checked");

        if (selected.length == 0) {
          this.$(".logic-dropdown").addClass("hidden");
          return;
        }

        //open the tooltip for single or multi logic
        this.$(".logic-dropdown").removeClass("hidden");

        if  (selected.length == 1) {
          this.$(".dropdown-menu").html(FacetTooltipTemplate({
            single: true,
            logic: this.logicOptions.single
          }));

        }
        else if (selected.length > 1) {
          var multiLogic = this.logicOptions.multiple;
          if (multiLogic === "fullSet") {
            /*any multiple selection automatically grabs the full set */
            this.$(".dropdown-menu").html(FacetTooltipTemplate({
              fullSet: true
            }))
          }
          else {
            this.$(".dropdown-menu").html(FacetTooltipTemplate({
              multiLogic: true,
              logic: multiLogic
            }))

          }
        }
      }

    });

    _.extend(FacetContainerView.prototype, WidgetPagination);

    return FacetContainerView;
  });