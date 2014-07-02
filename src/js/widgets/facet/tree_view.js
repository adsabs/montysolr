define(['underscore',
  'marionette',
  'js/widgets/base/tree_view',
  'hbs!./templates/item-tree'], function(
  _,
  Marionette,
  TreeView,
  TreeViewTemplate
  ) {

  var FacetTreeView = TreeView.extend({

    template: TreeViewTemplate,

    itemViewOptions: function(){
      var count = this.model.get("count");
      return {
        parentCount: count,
        hide: true
      }
    },

    onRender: function(view) {
      //      top-level
      if (!Marionette.getOption(this, "parentCount")){
        var percent = this.model.get("count") / this.model.get("total")
      }
      //      child
      else {
        var percent = this.model.get("count") / Marionette.getOption(this, "parentCount")

      }
      this.$(".size-graphic").width(percent*100 +"%");
    },

    events: {
      'click .widget-item': "onClick",
      'click .item-caret ': "toggleChildren",
      'click .show-more': 'onShowMore',
      'mouseenter label' : "onMouseEnter",
      'mouseleave label' : "onMouseLeave"
    },

    onMouseEnter: function(e){
      e.stopPropagation();
      var val;
      val = this.model.get("count")
      this.$(".facet-amount").html("&nbsp;(" + val + ")" );
      this.$(".item-caret").addClass("draw-attention-text")

    },

    onMouseLeave: function(e){
      e.stopPropagation();
      this.$(".facet-amount").empty();
      this.$("i.item-caret").removeClass("draw-attention-text")
    },

    //  utility function (better place to put this?)
    formatNum: function(num){
      var withCommas = [];
      num = num+"";
      if (num.length < 4){
        return num
      }
      else {
        num  = num.split("").reverse();
        _.each(num, function(n, i){
          withCommas.splice(0,0, n)
          if (i > 0 && (i+1) %3 === 0){
            withCommas.splice(0,0, ",")
          }

        })
      }
      withCommas = withCommas.join("");
      return withCommas.replace(/^\,(.+)/, "$1")
    },

    onClick: function (ev) {
      ev.stopPropagation();

      //select item and its children
      this.$("label:first").toggleClass("s-facet-selected");

      this.model.set('selected', $(ev.target).is(':checked'));
      this.trigger('itemClicked'); // we don't need to pass data because marionette includes 'this'

      if (this.model.children && this.model.children.length == 0) {
        this.trigger('treeNodeDisplayed');
      }
    }

  });

  return FacetTreeView;
});