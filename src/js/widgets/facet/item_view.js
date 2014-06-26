define('underscore', 'js/widgets/base/item_view',
  'hbs!./templates/item-checkbox', function(
  _,
  BaseItemView,
  ItemCheckBoxTemplate
  ) {

  var FacetItemView = BaseItemView.extend({

    template: ItemCheckBoxTemplate,

    events: {
      'click .widget-item': "onClick",
      'mouseenter label': "onMouseEnter",
      'mouseleave label': "onMouseLeave"
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

    onMouseEnter : function(){
      var val;
      val = this.model.get("count")
      this.$(".facet-amount").html("&nbsp;(" + this.formatNum(val) + ")" )
    },

    onMouseLeave : function(){
      this.$(".facet-amount").empty();

    },


    onClick: function(ev) {
      ev.stopPropagation();
      this.model.set('selected', ev.target.checked);
      this.$("label").toggleClass("s-facet-selected");
      this.trigger('itemClicked'); // we don't need to pass data because marionette includes 'this'
    },
    onRender : function(){
      var percent = this.model.get("count") / this.model.get("total")
      this.$(".size-graphic").width(percent*100 +"%")
    }
  });

  return FacetItemView;
});