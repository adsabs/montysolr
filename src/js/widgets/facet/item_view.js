define(['underscore', 'js/widgets/base/item_view',
  'hbs!./templates/item-checkbox',
   'js/mixins/formatter'
], function(
  _,
  BaseItemView,
  ItemCheckBoxTemplate,
  FormatMixin
  ) {

  var FacetItemView = BaseItemView.extend({

    template: ItemCheckBoxTemplate,

    events: {
      'click .widget-item': "onClick"
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
    },

    serializeData : function(){
      var data = this.model.toJSON();
      data.count = this.formatNum(data.count);
      return data;
    }

  });

  _.extend(FacetItemView.prototype, FormatMixin)

  return FacetItemView;
});